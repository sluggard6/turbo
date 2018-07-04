package xyz.sluggard.transmatch.listener;

import java.util.EventListener;

import xyz.sluggard.transmatch.event.Event;

public interface EngineListener<E extends Event> extends EventListener {
	
	void onApplicationEvent(E event);

}
