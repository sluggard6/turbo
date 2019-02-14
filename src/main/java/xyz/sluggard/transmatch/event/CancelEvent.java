package xyz.sluggard.transmatch.event;

import lombok.Getter;
import xyz.sluggard.transmatch.core.Engine;
import xyz.sluggard.transmatch.entity.Order;

@Getter
public class CancelEvent extends EngineEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CancelEvent(Order order, Engine<? extends Order> source) {
		super(source);
		this.order = order;
	}

	private final Order order;
	
	

}
