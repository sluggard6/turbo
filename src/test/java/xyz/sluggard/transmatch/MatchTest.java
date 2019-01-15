package xyz.sluggard.transmatch;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import xyz.sluggard.transmatch.core.Engine;
import xyz.sluggard.transmatch.engine.ExecutorEngine;
import xyz.sluggard.transmatch.entity.Order;
import xyz.sluggard.transmatch.entity.Order.Side;
import xyz.sluggard.transmatch.event.EngineEvent;
import xyz.sluggard.transmatch.event.EngineListener;

public class MatchTest {
	
	private Engine engine;
	
	private List<EngineEvent> events = new ArrayList<>();
	
	@Before
	public void init() {
		engine = new ExecutorEngine("BTC_USD");
		engine.getEventService().addListener(new EngineListener() {
			
			@Override
			public void onEvent(EngineEvent event) {
				System.out.println("add event : " + event);
				events.add(event);
			}
		});
		engine.start();
	}
	
	@After
	public void destroy() {
//		engine.stop();
		events.clear();
	}
	
	@Test
	public void OneMatch() throws Exception {
		Order order1 = new Order("1", BigDecimal.ONE, BigDecimal.ONE, Side.ASK);
		Order order2 = new Order("2", BigDecimal.ONE, BigDecimal.ONE, Side.BID);
		engine.newOrder(order1);
		engine.newOrder(order2);
		Thread.sleep(100);
		engine.stop();
		Thread.sleep(100);
		for(EngineEvent event : events) {
			System.out.println(event);
		}
	}
	
//	@Test
//	public void TowMatch() {
//		
//	}

}
