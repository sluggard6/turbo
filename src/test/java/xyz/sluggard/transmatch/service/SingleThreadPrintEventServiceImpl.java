package xyz.sluggard.transmatch.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.Getter;
import xyz.sluggard.transmatch.MatchEventCount;
import xyz.sluggard.transmatch.event.EngineEvent;
import xyz.sluggard.transmatch.service.impl.AbstractEventService;

public class SingleThreadPrintEventServiceImpl extends AbstractEventService {
	
	private ExecutorService executorService = Executors.newSingleThreadExecutor();
	
	@Getter
	private MatchEventCount mec = new MatchEventCount();

	@Override
	public void publishEvent(EngineEvent event) {
		executorService.execute(new PublishRun(event));
	}
	
	private class PublishRun implements Runnable {
		
		private EngineEvent event;
		
		public PublishRun(EngineEvent event) {
			this.event = event;
		}

		@Override
		public void run() {
			mec.count(event);
//			System.out.println(event);
		}
		
		
	}

}
