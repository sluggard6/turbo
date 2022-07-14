package com.github.transmatch;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;

import com.github.transmatch.event.EngineEvent;
import com.github.transmatch.event.EngineListener;
import com.github.transmatch.service.impl.AbstractEventService;

public class TestEventServiceImpl extends AbstractEventService {

	private ThreadLocal<Set<EngineListener>> localListeners;
	
	private CompletionService<EngineEvent> executorService =  new ExecutorCompletionService<EngineEvent>(Executors.newFixedThreadPool(4));

	public TestEventServiceImpl() {
		super();
		
	}

	@Override
	public void addListener(EngineListener listener) {
		super.addListener(listener);
		localListeners = new ThreadLocal<Set<EngineListener>>() {
			@Override 
			protected Set<EngineListener> initialValue() {
				return TestEventServiceImpl.super.getLinsteners();
			}
		};
	}

	@Override
	public Set<EngineListener> getLinsteners() {
		if(localListeners == null) return Collections.emptySet();
		return localListeners.get();
	}

	@Override
	public void publishEvent(EngineEvent event) {
		if(event == null) {
			throw new NullPointerException();
		}
		if(localListeners == null) return;
		executorService.submit(new PublishCommand(event));
	}
	
	private class PublishCommand implements Callable<EngineEvent>{
		
		private EngineEvent event;
		
		public PublishCommand(EngineEvent event) {
			this.event = event;
		}

		@Override
		public EngineEvent call() {
			localListeners.get().forEach((linstener) -> {
				linstener.onEvent(event);
			});
			return event;
		}
	}

	public CompletionService<EngineEvent> getExecutorService() {
		return executorService;
	}
}
