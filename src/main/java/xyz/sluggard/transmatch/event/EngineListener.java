package xyz.sluggard.transmatch.event;

public interface EngineListener<T extends EngineEvent> {
	
	void onEvent(EngineEvent event);

}
