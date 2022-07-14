package com.github.transmatch.event;

import java.math.BigDecimal;

import com.github.transmatch.core.Engine;
import com.github.transmatch.entity.Order;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CancelEvent extends EngineEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CancelEvent(Order order, Engine source) {
		super(source);
		if(order.isMarket()) {
			order.setPrice(BigDecimal.ZERO);
		}
		order.negate();
		this.order = order;
	}

	private final Order order;
	
	

}
