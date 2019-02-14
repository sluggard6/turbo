package xyz.sluggard.transmatch.core;

import java.util.Collection;

import xyz.sluggard.transmatch.entity.Order;
import xyz.sluggard.transmatch.entity.Order.Side;
import xyz.sluggard.transmatch.service.EventService;

public interface Engine<O extends Order> {
	
	String getCurrencyPair();
	
	boolean newOrder(O order);

	boolean cancelOrder(String orderId, Side side);

	boolean cancelOrder(O order);

	Collection<O> getBidQueue();

	Collection<O> getAskQueue();
	
	EventService getEventService();

	void start();

	void stop();

}
