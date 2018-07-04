package xyz.sluggard.transmatch.listener;

import xyz.sluggard.transmatch.event.TradeEvent;

public interface TradeListener extends EngineListener<TradeEvent> {
	
	void tradeCreated(TradeEvent tradeEvent);

}
