package xyz.sluggard.transmatch.vo;

import lombok.Data;
import xyz.sluggard.transmatch.entity.Order;

@Data
public class Action {
	
	private Order order;

	private String orderId;
	
	private boolean cancal;

	public Action(Order order, String orderId, boolean cancal) {
		super();
		this.order = order;
		this.orderId = orderId;
		this.cancal = cancal;
	}

	public Action(Order order) {
		this(order,null,false);
	}

	public Action(String orderId) {
		this(null,orderId,true);
	}
}
