package xyz.sluggard.transmatch.listener;

import java.util.EventListener;

import xyz.sluggard.transmatch.event.EngineEvent;

public interface EngineListener<E extends EngineEvent> extends EventListener {
	
	void onApplicationEvent(E event);
	
}
