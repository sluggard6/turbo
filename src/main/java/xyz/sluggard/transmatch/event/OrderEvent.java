package xyz.sluggard.transmatch.event;

import lombok.Getter;
import lombok.ToString;
import xyz.sluggard.transmatch.core.Engine;
import xyz.sluggard.transmatch.entity.Order;

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
