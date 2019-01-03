package xyz.sluggard.transmatch.core;

import java.util.Collection;

import xyz.sluggard.transmatch.entity.Order;
import xyz.sluggard.transmatch.entity.Order.Side;
import xyz.sluggard.transmatch.service.EventService;
import xyz.sluggard.transmatch.service.InitService;

public interface Engine {
	
	String getCurrencyPair();
	
	boolean newOrder(Order order);

	boolean cancelOrder(String orderId, Side side);

	boolean cancelOrder(Order order);

	Collection<Order> getBidQueue();

	Collection<Order> getAskQueue();

	void setEventService(EventService eventService);

	void setInitService(InitService initService);
	
}
