package xyz.sluggard.transmatch.engine;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.annotation.PreDestroy;

import xyz.sluggard.transmatch.core.Engine;
import xyz.sluggard.transmatch.entity.Order;
import xyz.sluggard.transmatch.event.EngineDestoryEvent;
import xyz.sluggard.transmatch.event.EngineStartUpEvent;
import xyz.sluggard.transmatch.service.EventService;
import xyz.sluggard.transmatch.service.InitService;
import xyz.sluggard.transmatch.service.impl.EventServiceImpl;
import xyz.sluggard.transmatch.service.impl.NoneInitServiceImpl;

public abstract class AbstractEngine implements Engine{
	
	private String currencyPair;

	private int quotePrecision;
	
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
			return true;
		}else {
			return bidOrder.getPrice().compareTo(askOrder.getPrice()) >= 0;
		}
	}
	
	protected BigDecimal getAmount(Order order, BigDecimal price) {
		if(order.isMarket() && order.isBid()) {
			order.setPrice(price);
			return order.getFunds().divide(price, quotePrecision, RoundingMode.DOWN);
		}else {
			return order.getAmount();
		}
	}
	
	protected BigDecimal getAmount(Order bidOrder, Order askOrder) {
		if(bidOrder.isMarket()) {
			return getAmount(bidOrder, askOrder.getPrice()).min(askOrder.getAmount());
		}else {
			return bidOrder.getAmount().min(askOrder.getAmount());
		}
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

}
