package xyz.sluggard.transmatch.actionevent.publisher;

import xyz.sluggard.transmatch.actionevent.event.EngineEvent;
import xyz.sluggard.transmatch.actionevent.listener.EngineListener;

public interface EngineEventPublisher {
	
	void publishEvent(EngineEvent event);
	
	void addEngineListener(EngineListener<?> listener);
	
	void removeEngineListener(EngineListener<?> listener);
	
	void removeAllListeners();
	
}
