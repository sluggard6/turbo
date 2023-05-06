package com.github.transmatch.engine;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.github.transmatch.core.Engine;
import com.github.transmatch.entity.Order;
import com.github.transmatch.entity.SequenceOrder;
import com.github.transmatch.event.EngineDestoryEvent;
import com.github.transmatch.event.EngineStartUpEvent;
import com.github.transmatch.service.EventService;
import com.github.transmatch.service.InitService;
import com.github.transmatch.service.impl.EventServiceImpl;
import com.github.transmatch.service.impl.NoneInitServiceImpl;

public abstract class AbstractEngine implements Engine{
	
	private String currencyPair;

	private int quotePrecision = 10;
	
	protected static class SideComparator implements Comparator<Order> {

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
	
	@Override
	public String getCurrencyPair() {
		return currencyPair;
	}
	
	public AbstractEngine(String currencyPair) {
		this(currencyPair, new EventServiceImpl(), new NoneInitServiceImpl());
	}
	
	public AbstractEngine(String currencyPair, EventService eventService) {
		this(currencyPair, eventService, new NoneInitServiceImpl());
	}
	
	public AbstractEngine(String currencyPair, EventService eventService, InitService initService) {
		if(currencyPair == null || currencyPair.trim().length() == 0) {
			throw new NullPointerException("currencyPair can't be null");
		}
		this.currencyPair = currencyPair;
		this.eventService = eventService;
		this.initService = initService;
	}

	protected EventService eventService;
	
	protected InitService initService;
	
	@Override
	public String toString() {
		return "ExecutorEngine [currencyPair=" + currencyPair + "]";
	}
	
	@Override
	@PostConstruct
	public void start() {
		eventService.publishEvent(new EngineStartUpEvent(this));
		List<Order> orders = initService.initOrder();
		for(Order order: orders) {
			this.newOrder(order);
		}
	}
	
	@Override
	@PreDestroy
	public void stop() {
		eventService.publishEvent(new EngineDestoryEvent(this));
	}

	protected static final boolean canMatch(Order bidOrder, Order askOrder) {
		if(bidOrder.isMarket() || askOrder.isMarket()) {
			if(bidOrder.isMarket()) {
				bidOrder.setPrice(askOrder.getPrice());
			}else {
				askOrder.setPrice(bidOrder.getPrice());
			}
			return true;
		}else {
			return bidOrder.getPrice().compareTo(askOrder.getPrice()) >= 0;
		}
	}
	
	protected BigDecimal getAmount(Order order, BigDecimal price) {
		if(order.isMarket() && order.isBid()) {
			return order.getFunds().divide(price, quotePrecision, RoundingMode.DOWN);
		}else {
			return order.getAmount();
		}
	}
	
	protected BigDecimal getMinAmount(Order bidOrder, Order askOrder) {
//		if(bidOrder.isMarket()) {
		return getAmount(bidOrder, askOrder.getPrice()).min(askOrder.getAmount());
//		}else {
//			return bidOrder.getAmount().min(askOrder.getAmount());
//		}
	}
	
	
	protected final boolean overPrice(BigDecimal lastPrice, BigDecimal orderPrice, Order.Side side) {
		return side.equals(Order.Side.ASK) ? lastPrice.compareTo(orderPrice) >= 0 : lastPrice.compareTo(orderPrice) <= 0;
	}

	public int getQuotePrecision() {
		return quotePrecision;
	}

	public void setQuotePrecision(int quotePrecision) {
		this.quotePrecision = quotePrecision;
	}
	
	protected boolean orderIsDone(Order order) {
		if(order.isMarket() && order.isBid()) {
			return order.getFunds().divide(order.getPrice(), quotePrecision, RoundingMode.DOWN).compareTo(BigDecimal.ZERO) == 0;
		}else {
			return order.isDone();
		}
	}
	
	protected void newSequenceOrder(SequenceOrder sequenceOrder) {
		
	}

}
