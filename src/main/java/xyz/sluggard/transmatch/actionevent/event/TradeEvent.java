package xyz.sluggard.transmatch.actionevent.event;

import lombok.Getter;
import lombok.ToString;
import xyz.sluggard.transmatch.entity.Trade;

@ToString
public class TradeEvent extends EngineEvent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -889543497045457714L;
	
	@Getter
	private final Trade trade;

	public TradeEvent(Trade trade, Object source) {
		super(source);
		this.trade = trade;
	}

}
