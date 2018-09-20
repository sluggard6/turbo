package xyz.sluggard.transmatch.actionevent.listener;

import xyz.sluggard.transmatch.actionevent.event.TradeEvent;

public interface TradeListener extends EngineListener<TradeEvent> {
	
	void tradeCreated(TradeEvent tradeEvent);

}
