package xyz.sluggard.transmatch.event;

import java.util.EventObject;

import xyz.sluggard.transmatch.core.Engine;

public abstract class EngineEvent extends EventObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EngineEvent(Engine engine) {
		super(engine);
	}


	public String getEventName() {
		return this.getClass().getSimpleName();
	}
	
	public String getCurrencyPair() {
		return ((Engine) source).getCurrencyPair();
	}

}
