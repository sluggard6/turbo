package xyz.sluggard.transmatch.actionevent.listener;

import java.util.EventListener;

import xyz.sluggard.transmatch.actionevent.event.EngineEvent;

public interface EngineListener<E extends EngineEvent> extends EventListener {
	
	void onApplicationEvent(E event);
	
}
