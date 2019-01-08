package xyz.sluggard.transmatch.event;

import lombok.Getter;
import lombok.ToString;
import xyz.sluggard.transmatch.core.Engine;
import xyz.sluggard.transmatch.entity.Trade;

@Getter
@ToString
public class TradeEvent extends EngineEvent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Trade trade;

	public TradeEvent(Trade trade, Engine engine) {
		super(engine);
		this.trade = trade;
	}
	
}
