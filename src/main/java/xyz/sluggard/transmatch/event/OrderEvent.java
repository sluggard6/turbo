package xyz.sluggard.transmatch.event;

import lombok.Getter;
import xyz.sluggard.transmatch.core.Engine;
import xyz.sluggard.transmatch.entity.Order;

@Getter
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
