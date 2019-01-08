package xyz.sluggard.transmatch.service;

import java.util.Set;

import xyz.sluggard.transmatch.event.EngineEvent;
import xyz.sluggard.transmatch.event.EngineListener;

public interface EventService {

	void addListener(EngineListener listener);
	
	int countLinsteners();
	
	Set<EngineListener> getLinsteners();
	
	default void publishEvent(EngineEvent event) {
		getLinsteners().forEach(listener -> {
			listener.onEvent(event);
		});
	}

}
