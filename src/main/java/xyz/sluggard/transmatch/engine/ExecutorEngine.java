package xyz.sluggard.transmatch.engine;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import xyz.sluggard.transmatch.core.Engine;
import xyz.sluggard.transmatch.entity.Order;
import xyz.sluggard.transmatch.entity.Trade;
import xyz.sluggard.transmatch.entity.Order.Side;
import xyz.sluggard.transmatch.event.CancelEvent;
import xyz.sluggard.transmatch.event.EngineDestoryEvent;
import xyz.sluggard.transmatch.event.EngineStartUpEvent;
import xyz.sluggard.transmatch.event.MakerEvent;
import xyz.sluggard.transmatch.event.OrderEvent;
import xyz.sluggard.transmatch.event.TradeEvent;
import xyz.sluggard.transmatch.service.EventService;
import xyz.sluggard.transmatch.service.InitService;

@Slf4j
@Data
public class ExecutorEngine implements Engine{
	
	static PriorityBlockingQueue<Order> bidQueue = new PriorityBlockingQueue<>();
	
	static PriorityBlockingQueue<Order> askQueue = new PriorityBlockingQueue<>();
	
	private String currencyPair;
	
	public ExecutorEngine(String currencyPair) {
		if(currencyPair == null || currencyPair.trim().length() == 0) {
			throw new NullPointerException("currencyPair can't be null");
		}
		this.currencyPair = currencyPair;
	}

	private ExecutorService executorService;
	
	private static final long TIME_OUT = 5000;

	@Setter
	private EventService eventService;
	
	@Setter
	private InitService initService;
	
	@PostConstruct
	public void init() {
		try {
			if (executorService != null && !executorService.isShutdown())
				throw new IllegalStateException();
			executorService = Executors.newSingleThreadExecutor();
			eventService.publishEvent(new EngineStartUpEvent(this));
			List<Order> orders = initService.initOrder();
			for(Order order: orders) {
				newOrder(order);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@PreDestroy
	public void destory() {
		executorService.shutdown();
		eventService.publishEvent(new EngineDestoryEvent(this));
	}

	@Override
	public String getCurrencyPair() {
		return currencyPair;
	}

	@Override
	public boolean newOrder(Order order) {
		log.info(order.toString());
		executorService.submit(new Command(order));
		return true;
	}

	@Override
	public boolean cancelOrder(String orderId, Side side) {
		long start = System.currentTimeMillis();
		try {
			Future<Boolean> future = executorService.submit(new Command(orderId, side));
			while (!future.isDone()) {
				// 超时判断
				System.out.println(System.currentTimeMillis() - start);
				if (System.currentTimeMillis() - start > TIME_OUT) {
					return false;
				}
				Thread.sleep(100);
			}
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean cancelOrder(Order order) {
		return cancelOrder(order.getId(), order.getSide());
	}

	@Override
	public Collection<Order> getAskQueue() {
		return Collections.unmodifiableCollection(askQueue);
	}

	@Override
	public Collection<Order> getBidQueue() {
		return Collections.unmodifiableCollection(bidQueue);
	}
	
	private class Command implements Callable<Boolean> {
		
		private boolean cancal;
		
		private Order order;
		
		private String orderId;
		
		private Side side;
		
		public Command(String orderId, Side side) {
			this(orderId, side, true, null);
		}
		
		public Command(Order order) {
			this(null, null, false, order);
		}
		
		public Command(String orderId, Side side, boolean cancel, Order order) {
			this.order = order;
			this.orderId = orderId;
			this.side = side;
			this.cancal = cancel;
		}
		
		private boolean isAsk() {
			return side == Side.ASK;
		}
		
		@Override
		public Boolean call() {
			try {
				if(cancal) {
					if(isAsk()) {
						return cancelOrderIter(askQueue, orderId) != null;
					}else {
						return cancelOrderIter(bidQueue, orderId) != null;
					}
				}else {
					newOrder(order);
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		private void newBid(Order bidOrder) {
			Order askOrder = askQueue.peek();
			if(askOrder == null) {
				addBid(bidOrder);
				return;
			}
			if(!match(bidOrder, askOrder)) {
				addBid(bidOrder);
				return;
			}else {
				if(askOrder.isDone()) {
					askQueue.poll();
				}
				if(!bidOrder.isDone()) {
					newBid(bidOrder);
				}
			}
		}

		private void newAsk(Order askOrder) {
			Order bidOrder = bidQueue.peek();
			if(bidOrder == null) {
				addAsk(askOrder);
				return;
			}
			if(!match(bidOrder, askOrder)) {
				addAsk(askOrder);
				return;
			}else {
				if(bidOrder.isDone()) {
					bidQueue.poll();
				}
				if(!askOrder.isDone()) {
					newAsk(askOrder);
				}
			}
		}
		
		private void addAsk(Order askOrder) {
			eventService.publishEvent(new MakerEvent(askOrder, ExecutorEngine.this));
			askQueue.add(askOrder);
		}
		
		private void addBid(Order bidOrder) {
			eventService.publishEvent(new MakerEvent(bidOrder, ExecutorEngine.this));
			bidQueue.add(bidOrder);
		}
		
		private final boolean preMatch(Order bidOrder, Order askOrder) {
			return bidOrder.getPrice().compareTo(askOrder.getPrice()) >= 0;
		}

		private final boolean match(Order bidOrder, Order askOrder) {
			if(preMatch(bidOrder, askOrder)) {
				BigDecimal min = bidOrder.getAmount().min(askOrder.getAmount());
				bidOrder.setAmount(bidOrder.getAmount().subtract(min));
				askOrder.setAmount(askOrder.getAmount().subtract(min));
				MatchPrice price = getPrice(bidOrder, askOrder);
				Trade trade = new Trade(bidOrder.getId(), askOrder.getId(), price.price, min, price.maker.getId(), price.taker.getId());
				eventService.publishEvent(new TradeEvent(trade, ExecutorEngine.this));
				return true;
			}
			return false;
		}
		
		private void newOrder(Order order) {
			eventService.publishEvent(new OrderEvent(order, ExecutorEngine.this));
			switch (order.getSide()) {
			case BID:
				newBid(order);
				break;
			case ASK:
				newAsk(order);
				break;
			default:
				throw new IllegalArgumentException("unknow trade type : " + order.getSide());
			}
		}
		
		/**
		 * @deprecated "不推荐使用，取消订单尽量提前确实是买单还是卖单"
		 * @param orderId
		 * @return
		 */
		@SuppressWarnings("unused")
		@Deprecated()
		private boolean cancelOrder(String orderId) {
				return (cancelOrderIter(bidQueue, orderId) != null) || (cancelOrderIter(askQueue, orderId) != null);
		}
		
		private Order cancelOrderIter(Queue<Order> queue, String orderId) {
			Iterator<Order> iter = queue.iterator();
			while(iter.hasNext()) {
				Order  order = iter.next();
				if(order.getId().equals(orderId)) {
					iter.remove();
					order.negate();
					eventService.publishEvent(new CancelEvent(order, ExecutorEngine.this));
					return order;
				}
			}
			return null;
		}
		
	}
	
	private static final MatchPrice getPrice(Order o1, Order o2) {
		if(o1.getTimestamp() < o2.getTimestamp()) {
			return new MatchPrice(o1, o2);
		}else {
			return new MatchPrice(o2, o1);
		}
	}
	
	private static class MatchPrice {
		private final Order maker;
		
		private final Order taker;
		
		private final BigDecimal price;

		public MatchPrice(Order maker, Order taker) {
			super();
			this.maker = maker;
			this.taker = taker;
			this.price = maker.getPrice();
		}
		
	}
}
