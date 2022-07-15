package com.github.transmatch.event;

import java.util.EventObject;

import com.github.transmatch.core.Engine;
import com.github.transmatch.utils.IdManager;

public abstract class EngineEvent extends EventObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long updateId;
	
	public EngineEvent(Engine engine) {
		super(engine);
		updateId = IdManager.makeId();
	}

	public String getEventName() {
		return this.getClass().getSimpleName();
	}
	
	public String getCurrencyPair() {
		return ((Engine) source).getCurrencyPair();
	}
	
	public long getUpdateId() {
		return updateId;
	}
	
	public void setUpdateId(long updateId) {
		this.updateId = updateId;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(currencyPai:" + getCurrencyPair() + ")";
	}

}
