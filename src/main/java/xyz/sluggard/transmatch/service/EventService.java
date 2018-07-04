package xyz.sluggard.transmatch.service;

import java.util.Set;

import xyz.sluggard.transmatch.event.Event;
import xyz.sluggard.transmatch.listener.EngineListener;

public interface EventService {


	void addListener(EngineListener listener);
	
	void removeListener(EngineListener listener);
	
	int countLinsteners();
	
	Set<EngineListener> getLinsteners();

	void deployEvent(Event event);
	
	void deployOrderEvent(Event event);
	
	void deployTradeEvent(Event event);


}
