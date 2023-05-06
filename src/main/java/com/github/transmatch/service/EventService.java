package com.github.transmatch.service;

import java.util.Set;

import com.github.transmatch.event.EngineEvent;
import com.github.transmatch.event.EngineListener;

public interface EventService extends AutoCloseable{

	void addListener(EngineListener listener);
	
	int countLinsteners();
	
	Set<EngineListener> getLinsteners();
	
	default void publishEvent(EngineEvent event) {
		getLinsteners().forEach(listener -> {
			listener.onEvent(event);
		});
	}

}
