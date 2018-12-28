package xyz.sluggard.transmatch.service.impl;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import xyz.sluggard.transmatch.event.EngineEvent;
import xyz.sluggard.transmatch.event.EngineListener;
import xyz.sluggard.transmatch.service.EventService;

public abstract class AbstractEventService implements EventService {

	private Set<EngineListener<? extends EngineEvent>> listeners;

	public AbstractEventService() {
		super();
		listeners = new ConcurrentSkipListSet<>();
	}

	@Override
	public void addListener(EngineListener<? extends EngineEvent> listener) {
		if (listener == null)
            throw new NullPointerException();
		listeners.add(listener);
	}

	@Override
	public void removeListener(EngineListener<? extends EngineEvent> listener) {
		listeners.remove(listener);
	}

	@Override
	public int countLinsteners() {
		return listeners.size();
	}

	@Override
	public Set<EngineListener<? extends EngineEvent>> getLinsteners() {
		return Collections.unmodifiableSet(listeners);
	}
	
}

