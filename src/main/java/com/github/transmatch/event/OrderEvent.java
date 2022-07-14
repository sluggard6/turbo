package com.github.transmatch.event;

import com.github.transmatch.core.Engine;
import com.github.transmatch.entity.Order;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OrderEvent extends EngineEvent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OrderEvent(Order order, Engine engine) {
		super(engine);
		this.order = order;
	}

	private final Order order;
}
