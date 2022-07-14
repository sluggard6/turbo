package com.github.transmatch;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.github.transmatch.engine.SyncEngine;
import com.github.transmatch.event.CancelEvent;
import com.github.transmatch.event.EngineDestoryEvent;
import com.github.transmatch.event.EngineEvent;
import com.github.transmatch.event.EngineListener;
import com.github.transmatch.event.EngineStartUpEvent;
import com.github.transmatch.event.MakerEvent;
import com.github.transmatch.event.OrderEvent;
import com.github.transmatch.event.TradeEvent;
import com.github.transmatch.service.impl.AbstractEventService;

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
