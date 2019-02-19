package xyz.sluggard.transmatch.event;

import xyz.sluggard.transmatch.core.Engine;
import xyz.sluggard.transmatch.entity.Order;

public class EngineStartUpEvent extends EngineEvent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EngineStartUpEvent(Engine engine) {
		super(engine);
	}
	
}
