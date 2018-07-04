package xyz.sluggard.transmatch.service.impl;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import xyz.sluggard.transmatch.event.Event;
import xyz.sluggard.transmatch.listener.EngineListener;
import xyz.sluggard.transmatch.service.EventService;

public abstract class AbstractEventService implements EventService {

	private static ExecutorService executorService;
	
	private Set<EngineListener> listeners;

	public AbstractEventService() {
		super();
		listeners = new ConcurrentSkipListSet<>();
		if(executorService.isShutdown()) {
			executorService = Executors.newSingleThreadExecutor();
		}
	}

	@Override
	public void addListener(EngineListener listener) {
		if (listener == null)
            throw new NullPointerException();
		listeners.add(listener);
	}

	@Override
	public void removeListener(EngineListener listener) {
		listeners.remove(listener);
	}

	@Override
	public int countLinsteners() {
		return listeners.size();
	}

	@Override
	public Set<EngineListener> getLinsteners() {
		return Collections.unmodifiableSet(listeners);
	}
	
}

