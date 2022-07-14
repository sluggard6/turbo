package com.github.transmatch.service.impl;

import java.util.Set;

import com.github.transmatch.event.EngineEvent;
import com.github.transmatch.event.EngineListener;
import com.github.transmatch.service.EventService;

import reactor.core.publisher.Flux;

public class FlowEventService implements EventService {
	
	Flux<EngineEvent> publisher;

	@Override
	public void addListener(EngineListener listener) {
		// TODO Auto-generated method stub
	}

	@Override
	public int countLinsteners() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Set<EngineListener> getLinsteners() {
		// TODO Auto-generated method stub
		return null;
	}

}
