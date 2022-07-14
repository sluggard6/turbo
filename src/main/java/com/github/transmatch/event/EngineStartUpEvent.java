package com.github.transmatch.event;

import com.github.transmatch.core.Engine;

public class EngineStartUpEvent extends EngineEvent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EngineStartUpEvent(Engine engine) {
		super(engine);
	}
	
}
