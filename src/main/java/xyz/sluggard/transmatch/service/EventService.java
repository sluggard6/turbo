package xyz.sluggard.transmatch.service;

import java.util.Set;

import xyz.sluggard.transmatch.event.EngineEvent;
import xyz.sluggard.transmatch.event.OrderEvent;
import xyz.sluggard.transmatch.event.TradeEvent;
import xyz.sluggard.transmatch.listener.EngineListener;

public interface EventService {

	void addListener(EngineListener<? extends EngineEvent> listener);
	
	void removeListener(EngineListener<? extends EngineEvent> listener);
	
	int countLinsteners();
	
	Set<EngineListener<? extends EngineEvent>> getLinsteners();

	void publishEvent(EngineEvent event);
	
	void deployOrderEvent(OrderEvent event);
	
	void deployTradeEvent(TradeEvent event);


}
