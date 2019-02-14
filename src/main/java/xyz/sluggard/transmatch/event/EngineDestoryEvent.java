package xyz.sluggard.transmatch.event;

import xyz.sluggard.transmatch.core.Engine;
import xyz.sluggard.transmatch.entity.Order;

public class EngineDestoryEvent extends EngineEvent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EngineDestoryEvent(Engine<? extends Order> engine) {
		super(engine);
	}
	

}
