package xyz.sluggard.transmatch.engine;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Queue;
import java.util.SortedSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import lombok.extern.slf4j.Slf4j;
import xyz.sluggard.transmatch.core.SortedSetQueue;
import xyz.sluggard.transmatch.entity.Order;
import xyz.sluggard.transmatch.entity.Order.Side;
import xyz.sluggard.transmatch.entity.Trade;
import xyz.sluggard.transmatch.event.CancelEvent;
import xyz.sluggard.transmatch.event.MakerEvent;
import xyz.sluggard.transmatch.event.OrderEvent;
import xyz.sluggard.transmatch.event.TradeEvent;
import xyz.sluggard.transmatch.service.EventService;
import xyz.sluggard.transmatch.service.InitService;

@Slf4j
public class ExecutorEngine extends AbstractEngine{

	private final SortedSetQueue<Order> bidQueue = new SortedSetQueue<>(new SideComparator());

	private final SortedSetQueue<Order> askQueue = new SortedSetQueue<>(new SideComparator());
	
	private static class SideComparator implements Comparator<Order> {

		@Override
		public int compare(Order o1, Order o2) {
			if(o1.isAsk()^o2.isAsk()) {
				if(o1.isAsk()) {
					if(canMatch(o2, o1)) {
						return -1;
					}else {
						return 1;
					}
				}else {
					if(canMatch(o1, o2)) {
						return -1;
					}else {
						return 1;
					}
				}
			}else {
				return o1.compareTo(o2);
			}
		}
	}
	
	private boolean fokCheck;
	
	public ExecutorEngine(String currencyPair) {
		super(currencyPair);
	}
	
	public ExecutorEngine(String currencyPair, EventService eventService) {
		super(currencyPair, eventService);
	}
	
	public ExecutorEngine(String currencyPair, EventService eventService, InitService initService) {
		super(currencyPair, eventService, initService);
	}
	

	private ExecutorService executorService;
	
	private static final long TIME_OUT = 5000;

	@Override
	@PostConstruct
	public void start() {
		try {
			if (executorService != null && !executorService.isShutdown())
				throw new IllegalStateException();
			executorService = Executors.newSingleThreadExecutor();
			super.start();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@Override
	@PreDestroy
	public void stop() {
		executorService.shutdown();
		super.stop();
	}
	
	@Override
	public EventService getEventService() {
		return eventService;
	}

	public void setEventService(EventService eventService) {
		checkBeforeStart();
		this.eventService = eventService;
	}


	public void setInitService(InitService initService) {
		this.initService = initService;
	}

	private boolean checkBeforeStart() {
		return !executorService.isShutdown();
	}


	@Override
	public boolean newOrder(Order order) {
		if(log.isDebugEnabled()) {
			log.debug(order.toString());
		}
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
	
	private void addSameQueue(Order order) {
		eventService.publishEvent(new MakerEvent(order.clone(), ExecutorEngine.this));
		getSameQueue(order.isAsk()).add(order);
		
	}

	private Queue<Order> getOppositeQueue(boolean booleanSide) {
		if(booleanSide) {
			return askQueue;
		}else {
			return bidQueue;
		}
	}
	
	private NavigableSet<Order> getOppositeSet(boolean booleanSide) {
		if(booleanSide) {
			return askQueue;
		}else {
			return bidQueue;
		}
	}
	
	private Queue<Order> getSameQueue(boolean booleanSide) {
		if(booleanSide) {
			return bidQueue;
		}else {
			return askQueue;
		}
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
					eventService.publishEvent(new OrderEvent(order.clone(), ExecutorEngine.this));
					newOrder(order);
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		
		private void newOrder(Order order) {
			if(order.isFok() && !fokCheck) {
				SortedSet<Order> set = getOppositeSet(order.isAsk()).headSet(order);
				BigDecimal sum = set.stream().map(Order::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
				if(sum.compareTo(order.getAmount()) >= 0) {
					fokCheck = true;
				}else {
					eventService.publishEvent(new CancelEvent(order, ExecutorEngine.this));
					return;
				}
			}
			Order op = getOppositeQueue(order.isAsk()).peek();
			if(op == null) {
				addSameQueue(order);
				return;
			}
			if(!noneMatch(order, op)) {
				if(order.isIoc()) {
					eventService.publishEvent(new CancelEvent(order, ExecutorEngine.this));
				}else {
					addSameQueue(order);
				}
				return;
			}else {
				if(op.isDone()) {
					getOppositeQueue(order.isAsk()).poll();
				}
				if(!order.isDone()) {
					newOrder(order);
				}else {
					if(order.isFok()) {
						fokCheck = false;
					}
					if(order.isMarket() && order.isBid() && order.getFands().compareTo(BigDecimal.ZERO) > 0) {
						eventService.publishEvent(new CancelEvent(order, ExecutorEngine.this));
					}
				}
			}
		}
		

		/**
		 * 成交方法
		 * @param bidOrder 买单
		 * @param askOrder 卖单
		 * @return
		 */
		private final boolean match(Order bidOrder, Order askOrder) {
			if(canMatch(bidOrder, askOrder)) {
//				BigDecimal min = bidOrder.getAmount().min(askOrder.getAmount());
				BigDecimal min = getAmount(bidOrder, askOrder);
				if(min.compareTo(BigDecimal.ZERO) == 0) {
					if(bidOrder.isMarket()) {
						bidOrder.setMarketDone(true);
					}
					return false;
				}
//				bidOrder.setAmount(bidOrder.getAmount().subtract(min));
//				askOrder.setAmount(askOrder.getAmount().subtract(min));
				bidOrder.subtractAmount(min, askOrder.getPrice());
				askOrder.subtractAmount(min, bidOrder.getPrice());
				MatchPrice price = getPrice(bidOrder, askOrder);
//				ExecutorEngine.this.lastPrice = price.price;
				Trade trade = new Trade(bidOrder.getId(), askOrder.getId(), price.price, min, price.maker.getId(), price.taker.getId());
				eventService.publishEvent(new TradeEvent(trade, ExecutorEngine.this));
				return true;
			}
			return false;
		}
		
		private final boolean noneMatch(Order o1, Order o2) {
			if(o1.isAsk()^o2.isAsk()) {
				if(o1.isAsk()) {
					return match(o2,o1);
				}else {
					return match(o1,o2);
				}
			}else {
				throw new IllegalArgumentException("same side order can't make trade");
			}
		}
		
		
		private Order cancelOrderIter(Queue<Order> queue, String orderId) {
			Iterator<Order> iter = queue.iterator();
			while(iter.hasNext()) {
				Order  order = iter.next();
				if(order.getId().equals(orderId)) {
					iter.remove();
					order.negate();
					eventService.publishEvent(new CancelEvent(order.clone(), ExecutorEngine.this));
					return order;
				}
			}
			return null;
		}
		
	}
	
	private static final MatchPrice getPrice(Order o1, Order o2) {
		if(o1.getNanotime() < o2.getNanotime()) {
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
			this.price = maker.isMarket() ? maker.getPrice() : taker.getPrice();
		}
		
	}

}