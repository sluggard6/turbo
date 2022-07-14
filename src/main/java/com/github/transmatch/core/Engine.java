package com.github.transmatch.core;

import java.util.Collection;

import com.github.transmatch.entity.Order;
import com.github.transmatch.entity.Order.Side;
import com.github.transmatch.event.OrderBookEvent;
import com.github.transmatch.service.EventService;

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
	
	default OrderBookEvent getOrderBook() {
		throw new UnsupportedOperationException("not implement yet!");
	}

	void start();

	void stop();
	
	default long getUpdateId() {
		throw new UnsupportedOperationException("未实现的方法");
	}

}
