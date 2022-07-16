package com.github.transmatch.event;

import com.github.transmatch.core.Engine;
import com.github.transmatch.entity.OrderBook;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OrderBookEvent extends EngineEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private OrderBook orderBook;
	
	public OrderBookEvent(OrderBook orderBook, Engine engine) {
		super(engine);
		this.orderBook = orderBook;
	}

}
