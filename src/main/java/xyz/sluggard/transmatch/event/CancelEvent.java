package xyz.sluggard.transmatch.event;

import lombok.Getter;
import lombok.ToString;
import xyz.sluggard.transmatch.core.Engine;
import xyz.sluggard.transmatch.entity.Order;

@Getter
@ToString
public class CancelEvent extends EngineEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CancelEvent(Order order, Engine source) {
		super(source);
		order.negate();
		this.order = order;
	}

	private final Order order;
	
	

}
