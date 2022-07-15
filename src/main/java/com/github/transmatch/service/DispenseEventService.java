package com.github.transmatch.service;

import com.github.transmatch.event.EngineContextListener;

public interface DispenseEventService extends EventService {
	
	void setContextListener(EngineContextListener contextListener);

}
