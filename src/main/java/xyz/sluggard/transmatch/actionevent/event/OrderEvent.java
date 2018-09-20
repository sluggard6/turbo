package xyz.sluggard.transmatch.actionevent.event;

import lombok.Getter;
import lombok.ToString;
import xyz.sluggard.transmatch.entity.Order;

@ToString
public class OrderEvent extends EngineEvent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1072116264234637875L;
	
	@Getter
	private final Order order;
	
	public OrderEvent(Order order, Object source) {
		super(source);
		this.order = order;
	}

}
