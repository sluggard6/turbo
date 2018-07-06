package xyz.sluggard.transmatch.event;

import xyz.sluggard.transmatch.entity.Order;

public class OrderEvent implements Event {
	
	private final Order order;
	

	public OrderEvent(Order order) {
		super();
		this.order = order;
	}

	public Order getOrder() {
		return order;
	}
	
}
