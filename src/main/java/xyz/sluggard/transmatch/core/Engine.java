package xyz.sluggard.transmatch.core;

import java.util.Collection;

import xyz.sluggard.transmatch.entity.Order;
import xyz.sluggard.transmatch.entity.Order.Side;
import xyz.sluggard.transmatch.entity.OrderBook;
import xyz.sluggard.transmatch.service.EventService;

public interface Engine {
	
	String getCurrencyPair();
	
	boolean newOrder(Order order);

	boolean cancelOrder(String orderId, Side side);

	boolean cancelOrder(Order order);
	
	int getQuotePrecision();
	
	void setQuotePrecision(int quotePrecision);

	Collection<Order> getBidQueue();

	Collection<Order> getAskQueue();
	
	EventService getEventService();
	
	default OrderBook getOrderBook() {
		throw new UnsupportedOperationException("not implement yet!");
	}

	void start();

	void stop();
	
	default long getUpdateId() {
		throw new UnsupportedOperationException("未实现的方法");
	}

}
