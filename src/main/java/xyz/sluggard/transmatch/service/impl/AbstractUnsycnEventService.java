package xyz.sluggard.transmatch.service.impl;

import java.util.concurrent.ExecutorService;

import xyz.sluggard.transmatch.listener.EngineListener;
import xyz.sluggard.transmatch.service.UnsyncEventService;

public abstract class AbstractUnsycnEventService extends AbstractEventService implements UnsyncEventService{
	
	private ExecutorService executorService;
	
	class Task implements Runnable {

		@Override
		public void run() {
			for(EngineListener listener: getLinsteners()) {
				
			}
		}
		
	}

}

