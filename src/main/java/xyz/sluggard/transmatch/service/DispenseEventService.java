package xyz.sluggard.transmatch.service;

import xyz.sluggard.transmatch.event.EngineContextListener;

public interface DispenseEventService extends EventService {
	
	void setContextListener(EngineContextListener contextListener);

}
