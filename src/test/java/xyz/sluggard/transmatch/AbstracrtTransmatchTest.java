package xyz.sluggard.transmatch;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import xyz.sluggard.transmatch.engine.SyncEngine;
import xyz.sluggard.transmatch.event.CancelEvent;
import xyz.sluggard.transmatch.event.EngineDestoryEvent;
import xyz.sluggard.transmatch.event.EngineEvent;
import xyz.sluggard.transmatch.event.EngineListener;
import xyz.sluggard.transmatch.event.EngineStartUpEvent;
import xyz.sluggard.transmatch.event.MakerEvent;
import xyz.sluggard.transmatch.event.OrderEvent;
import xyz.sluggard.transmatch.event.TradeEvent;
import xyz.sluggard.transmatch.service.impl.AbstractEventService;

public abstract class AbstracrtTransmatchTest {
	
	protected SyncEngine engine;
	
	protected List<EngineEvent> events = new CopyOnWriteArrayList<EngineEvent>();
	
	protected MatchEventCount mec;
	
	@BeforeMethod
	public void init() {
		mec = new MatchEventCount();
		engine = new SyncEngine("BTC_USD", new AbstractEventService() {
		});
		engine.getEventService().addListener(new EngineListener() {
			
			@Override
			public void onEvent(EngineEvent event) {
				if(event instanceof MakerEvent) {
					mec.countMakerEvent();
				}
				if(event instanceof OrderEvent) {
					mec.countOrderEvent();
				}
				if(event instanceof CancelEvent) {
					mec.countCancelEvent();
				}
				if(event instanceof TradeEvent) {
					mec.countTradeEvent();
				}
				if(event instanceof EngineStartUpEvent) {
					mec.countStartUpEvent();
				}
				if(event instanceof EngineDestoryEvent) {
					mec.countDestoryEvent();
				}
				events.add(event);
			}
		});
		engine.start();
	}
	
	@AfterMethod
	public void destroy() throws Exception {
		engine.stop();
		events.clear();
		
	}

}
