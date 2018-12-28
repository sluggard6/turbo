package xyz.sluggard.transmatch.event;

import xyz.sluggard.transmatch.core.Engine;

public class EngineDestoryEvent extends EngineEvent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EngineDestoryEvent(Engine engine) {
		super(engine);
	}
	

}
