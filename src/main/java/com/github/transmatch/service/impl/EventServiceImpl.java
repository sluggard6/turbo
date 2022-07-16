package com.github.transmatch.service.impl;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.github.transmatch.event.EngineEvent;
import com.github.transmatch.event.EngineListener;

public class EventServiceImpl extends AbstractEventService implements AutoCloseable{
	
	private ThreadLocal<Set<EngineListener>> localListeners;

	private ExecutorService executorService =  new ThreadPoolExecutor(4, 10,
            10L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>());

	public EventServiceImpl() {
		super();
		
	}

	@Override
	public void addListener(EngineListener listener) {
		super.addListener(listener);
		localListeners = new ThreadLocal<Set<EngineListener>>() {
			@Override 
			protected Set<EngineListener> initialValue() {
				return EventServiceImpl.super.getLinsteners();
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
		executorService.execute(new PublishCommand(event));
	}
	
	private class PublishCommand implements Runnable{
		
		private EngineEvent event;
		
		public PublishCommand(EngineEvent event) {
			this.event = event;
		}

		@Override
		public void run() {
			localListeners.get().forEach((linstener) -> {
				linstener.onEvent(event);
			});
		}
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	@Override
	public void close() throws Exception {
		executorService.shutdown();
	}

}
