package xyz.sluggard.transmatch.engine;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Observable;
import java.util.PriorityQueue;

import lombok.Setter;
import xyz.sluggard.transmatch.entity.AskOrder;
import xyz.sluggard.transmatch.entity.BidOrder;
import xyz.sluggard.transmatch.entity.Order;
import xyz.sluggard.transmatch.entity.Trade;
import xyz.sluggard.transmatch.event.OrderEvent;
import xyz.sluggard.transmatch.event.TradeEvent;
import xyz.sluggard.transmatch.service.EventService;

public class MatchEngine extends Observable{
	
	private PriorityQueue<BidOrder> bidQueue = new PriorityQueue<>();
	
	private PriorityQueue<AskOrder> askQueue = new PriorityQueue<>();
	
	@Setter
	private EventService eventService;
	
	public synchronized void newOrder(Order order) {
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
	
	public synchronized boolean cancelOrder(String orderId, boolean type) {
		if(type) {
			return bidQueue.remove(new BidOrder(orderId));
		}else {
			return askQueue.remove(new AskOrder(orderId));
		}
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
	
	public synchronized void newBuy(BidOrder bidOrder) {
		AskOrder selling = askQueue.peek();
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

	public synchronized void newSell(AskOrder askOrder) {
		BidOrder buying = bidQueue.peek();
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
		return buying.getPrice().compareTo(selling.getPrice()) > 0;
	}

	private final boolean match(BidOrder bidOrder, AskOrder askOrder) {
		if(preMatch(bidOrder, askOrder)) {
			BigDecimal min = bidOrder.getAmount().min(askOrder.getAmount());
			bidOrder.setAmount(bidOrder.getAmount().subtract(min));
			askOrder.setAmount(askOrder.getAmount().subtract(min));
			Trade trade = new Trade(bidOrder.getId(), askOrder.getId(), getPrice(bidOrder, askOrder), min);
			eventService.publishEvent(new TradeEvent(trade));
			return true;
		}
		return false;
	}
	
	private static final BigDecimal getPrice(Order o1, Order o2) {
		if(o1.getTimestamp() < o2.getUserId()) {
			return o1.getPrice();
		}else {
			return o2.getPrice();
		}
	}

	public Collection<BidOrder> getBidQueue() {
		return Collections.unmodifiableCollection(bidQueue);
	}

	public Collection<AskOrder> getAskQueue() {
		return Collections.unmodifiableCollection(askQueue);
	}

	
	
	
}
