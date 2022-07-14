package com.github.turbo.demo;

import java.math.BigDecimal;

import com.github.transmatch.core.Engine;
import com.github.transmatch.engine.ExecutorEngine;
import com.github.transmatch.entity.Order;
import com.github.transmatch.entity.Order.Side;
import com.github.transmatch.event.EngineEvent;
import com.github.transmatch.event.EngineListener;
import com.github.transmatch.service.EventService;
import com.github.transmatch.service.impl.EventServiceImpl;

public class Test {
	
	public static void main(String... args) {
		EventService eventService = new EventServiceImpl();
		eventService.addListener(new EngineListener() {

			@Override
			public void onEvent(EngineEvent event) {
				System.out.println(event);
			}
		});
		Engine engine = new ExecutorEngine("BTC_USD", eventService);
		engine.start();
		
		Order askOrder = new Order("1", new BigDecimal(100), new BigDecimal(10), Side.ASK);
		Order bidOrder = new Order("2", new BigDecimal(100), new BigDecimal(20), Side.BID);
		engine.newOrder(askOrder);
		engine.newOrder(bidOrder);
		
		engine.stop();
	}

}
