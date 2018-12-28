package xyz.sluggard.transmatch.service;

import java.util.Set;

import xyz.sluggard.transmatch.event.EngineEvent;
import xyz.sluggard.transmatch.event.EngineListener;

public interface EventService {

	void addListener(EngineListener<? extends EngineEvent> listener);
	
	void removeListener(EngineListener<? extends EngineEvent> listener);
	
	int countLinsteners();
	
	Set<EngineListener<? extends EngineEvent>> getLinsteners();
	
	default void publishEvent(EngineEvent event) {
		getLinsteners().forEach(listener -> {
			listener.onEvent(event);
		});
	}

}
