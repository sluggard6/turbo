package xyz.sluggard.transmatch.event;

import lombok.ToString;
import xyz.sluggard.transmatch.entity.Order;

@ToString
public class OrderEvent implements EngineEvent {
	
	private final Order order;
	
	public OrderEvent(Order order) {
		super();
		this.order = order;
	}

	public Order getOrder() {
		return order;
	}
	
}
