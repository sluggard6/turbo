package xyz.sluggard.transmatch.service;

import java.util.Set;

import xyz.sluggard.transmatch.listener.EngineListener;

public interface EventService {

	void addListener(EngineListener listener);
	
	void removeListener(EngineListener listener);
	
	int countLinsteners();
	
	Set<EngineListener> getLinsteners();

}
