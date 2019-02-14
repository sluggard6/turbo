package xyz.sluggard.transmatch.engine;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;

import xyz.sluggard.transmatch.core.PreMatchable;
import xyz.sluggard.transmatch.entity.Order.Side;
import xyz.sluggard.transmatch.entity.RankOrder;
import xyz.sluggard.transmatch.entity.Trade;
import xyz.sluggard.transmatch.event.TradeEvent;
import xyz.sluggard.transmatch.service.EventService;
import xyz.sluggard.transmatch.service.InitService;

public class RankableEngine extends AbstractEngine<RankOrder> implements PreMatchable<RankOrder> {
	
	public RankableEngine(String currencyPair) {
		super(currencyPair);
	}

	public RankableEngine(String currencyPair, EventService eventService, InitService<RankOrder> initService) {
		super(currencyPair, eventService, initService);
	}

	public RankableEngine(String currencyPair, EventService eventService) {
		super(currencyPair, eventService);
	}
	
	private SortedSet<RankOrder> askOrderSet = new ConcurrentSkipListSet<>();
	
	private SortedSet<RankOrder> bidOrderSet = new ConcurrentSkipListSet<>();
	
	@Override
	public synchronized boolean newOrder(RankOrder order) {
		return match(order, false) != null;
	}
	
	private synchronized String match(RankOrder order, boolean pre) {
		if(order.isMaker()) {
			if(order.isAsk()) {
				askOrderSet.add(order);
			}else {
				bidOrderSet.add(order);
			}
		}else {
			SortedSet<RankOrder> opSet = getOppositeQueue(order.isAsk());
			Iterator<RankOrder> iter = opSet.iterator();
			while(iter.hasNext()) {
				RankOrder makerOrder = iter.next();
				if(makerOrder.getAmount().compareTo(order.getAmount()) < 0) {continue;}
				if(canMatch(order, makerOrder)) {
					if(!pre) {
						iter.remove();
						makerOrder.setAmount(makerOrder.getAmount().subtract(order.getAmount()));
						RankOrder ask;
						RankOrder bid;
						if(order.isAsk()) {
							ask = order;
							bid = makerOrder;
						}else {
							ask = makerOrder;
							bid = order;
						}
						Trade trade = new Trade(ask.getId(), bid.getId(), makerOrder.getPrice(), order.getAmount(), makerOrder.getId(), order.getId());
						eventService.publishEvent(new TradeEvent(trade, this));
						if(makerOrder.getAmount().compareTo(BigDecimal.ZERO) != 0) {
							opSet.add(makerOrder);
						}
					}
					return makerOrder.getId();
				}
			}
		}
		return null;
	}
	
	private boolean canMatch(RankOrder o1, RankOrder o2) {
		if(o1.isAsk()^o2.isAsk()) {
			RankOrder ask;
			RankOrder bid;
			if(o1.isAsk()) {
				ask = o1;
				bid = o2;
			}else {
				ask = o2;
				bid = o1;
			}
			return bid.getPrice().compareTo(ask.getPrice()) >= 0;
		}else {
			throw new IllegalArgumentException("same side order can't make trade");
		}
		
	}
	
	private SortedSet<RankOrder> getOppositeQueue(boolean booleanSide) {
		if(booleanSide) {
			return askOrderSet;
		}else {
			return bidOrderSet;
		}
	}

	@Override
	public boolean cancelOrder(String orderId, Side side) {
		return false;
	}

	@Override
	public boolean cancelOrder(RankOrder order) {
		return false;
	}

	@Override
	public Collection<RankOrder> getBidQueue() {
		return null;
	}

	@Override
	public Collection<RankOrder> getAskQueue() {
		return null;
	}

	@Override
	public EventService getEventService() {
		return null;
	}

	
	@Override
	public void start() {
		
	}

	@Override
	public void stop() {
		
	}

	@Override
	public String preMatch(RankOrder order) {
		return match(order, true);
	}

}
