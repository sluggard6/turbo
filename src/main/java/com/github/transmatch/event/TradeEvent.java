package com.github.transmatch.event;

import com.github.transmatch.core.Engine;
import com.github.transmatch.entity.Trade;

import lombok.Getter;
import lombok.ToString;

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
