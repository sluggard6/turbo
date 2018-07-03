package xyz.sluggard.transmatch.service;

import xyz.sluggard.transmatch.event.Event;

public interface EventService {

	void deployEvent(Event event);
	
	void deployOrderEvent(Event event);
	
	void deployTradeEvent(Event event);

}
