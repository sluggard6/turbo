package xyz.sluggard.transmatch.core;

import java.util.Collection;

import xyz.sluggard.transmatch.entity.Order;
import xyz.sluggard.transmatch.entity.Order.Side;
import xyz.sluggard.transmatch.service.EventService;

public interface Engine {
	
	String getCurrencyPair();
	
	boolean newOrder(Order order);

	boolean cancelOrder(String orderId, Side side);

	boolean cancelOrder(Order order);

	Collection<Order> getBidQueue();

	Collection<Order> getAskQueue();
	
	EventService getEventService();

	void start();

	void stop();

}
