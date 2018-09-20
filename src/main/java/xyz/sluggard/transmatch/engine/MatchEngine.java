package xyz.sluggard.transmatch.engine;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

import lombok.Setter;
import xyz.sluggard.transmatch.entity.AskOrder;
import xyz.sluggard.transmatch.entity.BidOrder;
import xyz.sluggard.transmatch.entity.Order;
import xyz.sluggard.transmatch.entity.Trade;
import xyz.sluggard.transmatch.event.OrderEvent;
import xyz.sluggard.transmatch.event.TradeEvent;
import xyz.sluggard.transmatch.service.EventService;
import xyz.sluggard.transmatch.vo.Action;

public enum MatchEngine{
	
	ENGIN;
	
	private LinkedBlockingQueue<Action> preQueue = new LinkedBlockingQueue<>();
	
	private PriorityBlockingQueue<Order> bidQueue = new PriorityBlockingQueue<>();
	
	private PriorityBlockingQueue<Order> askQueue = new PriorityBlockingQueue<>();
	
	private ConcurrentHashMap<String, Integer> cancelMap = new ConcurrentHashMap<>();
	
	private boolean status;
	
	@Setter
	private EventService eventService;
	
	public void start() {
		if(status) throw new IllegalStateException();
		status = true;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(status) {
					try {
						Action action = preQueue.poll(1l, TimeUnit.MINUTES);
						if(action != null) {
							if(action.isCancal()) {
								cancelOrder(action.getOrderId());
							}else {
								newOrder(action.getOrder());
							}
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	public void newAction(Action action) throws InterruptedException {
		if(action == null) throw new NullPointerException();
		preQueue.put(action);
	}
	
	public void stop() {
		status = false;
	}
	
	public Integer getCancelState(String orderId) {
		return cancelMap.remove(orderId);
	}
	
	private void newOrder(Order order) {
		eventService.publishEvent(new OrderEvent(order));
		if(order instanceof BidOrder) {
			newBuy((BidOrder) order);
			return;
		}else if(order instanceof AskOrder) {
			newSell((AskOrder) order);
			return;
		}else {
			throw new IllegalArgumentException("unknow trade type : " + order.getClass().getName());
		}
	}
	
//	private boolean cancelOrder(String orderId, boolean type) {
//		if(type) {
//			return cancelOrderIter(bidQueue, orderId) != null;
//		}else {
//			return cancelOrderIter(askQueue, orderId) != null;
//		}
//	}
	
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
				eventService.publishEvent(new OrderEvent(order));
				return order;
			}
		}
		return null;
	}
	
//	private synchronized void doTrade(PriorityQueue<? super Trade> myQueue, PriorityQueue<? extends Trade> otherQueue, Trade me) {
//		Trade other = otherQueue.peek();
//		if(other == null) {
//			myQueue.add(me);
//			return;
//		}
//		if(!match(buying, selling)) {
//			myQueue.add(me);
//			return;
//		}else {
//			if(other.isDone()) {
//				otherQueue.poll();
//			}
//			if(!me.isDone()) {
//				doTrade(myQueue, otherQueue, me);
//			}
//		}
//	}
	
	private void newBuy(BidOrder bidOrder) {
		AskOrder selling = (AskOrder) askQueue.peek();
		if(selling == null) {
			bidQueue.add(bidOrder);
			return;
		}
		if(!match(bidOrder, selling)) {
			bidQueue.add(bidOrder);
			return;
		}else {
			if(selling.isDone()) {
				askQueue.poll();
			}
			if(!bidOrder.isDone()) {
				newBuy(bidOrder);
			}
		}
	}

	private void newSell(AskOrder askOrder) {
		BidOrder buying = (BidOrder) bidQueue.peek();
		if(buying == null) {
			askQueue.add(askOrder);
			return;
		}
		if(!match(buying, askOrder)) {
			askQueue.add(askOrder);
			return;
		}else {
			if(buying.isDone()) {
				bidQueue.poll();
			}
			if(!askOrder.isDone()) {
				newSell(askOrder);
			}
		}
	}
	
	private final boolean preMatch(BidOrder buying, AskOrder selling) {
		return buying.getPrice().compareTo(selling.getPrice()) >= 0;
	}

	private final boolean match(BidOrder bidOrder, AskOrder askOrder) {
		if(preMatch(bidOrder, askOrder)) {
			BigDecimal min = bidOrder.getAmount().min(askOrder.getAmount());
			bidOrder.setAmount(bidOrder.getAmount().subtract(min));
			askOrder.setAmount(askOrder.getAmount().subtract(min));
			MatchPrice price = getPrice(bidOrder, askOrder);
//			Trade trade = new Trade(bidOrder.getId(), askOrder.getId(), price.price, price.maker, price.taker);
			Trade trade = new Trade(bidOrder.getId(), askOrder.getId(), price.price, min, price.maker.getId(), price.taker.getId());
			eventService.publishEvent(new TradeEvent(trade));
			return true;
		}
		return false;
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

	public Collection<Order> getBidQueue() {
		return Collections.unmodifiableCollection(bidQueue);
	}

	public Collection<Order> getAskQueue() {
		return Collections.unmodifiableCollection(askQueue);
	}
	
}
