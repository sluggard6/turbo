package xyz.sluggard.transmatch.engine;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.PriorityQueue;

import lombok.Setter;
import xyz.sluggard.transmatch.entity.Buying;
import xyz.sluggard.transmatch.entity.Selling;
import xyz.sluggard.transmatch.entity.Trade;
import xyz.sluggard.transmatch.entity.Trans;
import xyz.sluggard.transmatch.service.EventService;

public class MatchEngine{
	
	private PriorityQueue<Buying> buyingQueue = new PriorityQueue<>();
	
	private PriorityQueue<Selling> sellingQueue = new PriorityQueue<>();
	
	@Setter
	private EventService eventService;
	
	public synchronized void newTrade(Trade trade) {
		if(trade instanceof Buying) {
			newBuy((Buying) trade);
			return;
		}else if(trade instanceof Selling) {
			newSell((Selling) trade);
			return;
		}else {
			throw new IllegalArgumentException("unknow trade type : " + trade.getClass().getName());
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
	
	public synchronized void newBuy(Buying buying) {
		Selling selling = sellingQueue.peek();
		if(selling == null) {
			buyingQueue.add(buying);
			return;
		}
		if(!match(buying, selling)) {
			buyingQueue.add(buying);
			return;
		}else {
			if(selling.isDone()) {
				sellingQueue.poll();
			}
			if(!buying.isDone()) {
				newBuy(buying);
			}
		}
	}

	public synchronized void newSell(Selling selling) {
		Buying buying = buyingQueue.peek();
		if(buying == null) {
			sellingQueue.add(selling);
			return;
		}
		if(!match(buying, selling)) {
			sellingQueue.add(selling);
			return;
		}else {
			if(buying.isDone()) {
				buyingQueue.poll();
			}
			if(!selling.isDone()) {
				newSell(selling);
			}
		}
	}
	
	private final boolean preMatch(Buying buying, Selling selling) {
		return buying.getPrice().compareTo(selling.getPrice()) > 0;
	}

	private final boolean match(Buying buying, Selling selling) {
		if(preMatch(buying, selling)) {
			BigDecimal min = buying.getAmount().min(selling.getAmount());
			buying.setAmount(buying.getAmount().subtract(min));
			selling.setAmount(selling.getAmount().subtract(min));
			Trans trans = new Trans(buying.getId(), selling.getId(), selling.getPrice(), min);
			eventService.deployEvent(trans);
			return true;
		}
		return false;
	}

	public Collection<Buying> getBuyingQueue() {
		return Collections.unmodifiableCollection(buyingQueue);
	}

	public Collection<Selling> getSellingQueue() {
		return Collections.unmodifiableCollection(sellingQueue);
	}

	
	
	
}
