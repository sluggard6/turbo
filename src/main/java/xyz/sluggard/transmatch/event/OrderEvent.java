package xyz.sluggard.transmatch.event;

import lombok.Getter;
import xyz.sluggard.transmatch.entity.Order;

public class OrderEvent implements Event {
	
	@Getter
	private final Order order;

	public OrderEvent(Order order) {
		super();
		this.order = order;
	}

}
