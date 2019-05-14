# turbo

[![Build Status](https://travis-ci.org/sluggard6/turbo?branch=master)](https://travis-ci.org/sluggard6/turbo)

A Market Order Matching Engine


## Getting started

```java
package xyz.sluggard.turbo.demo;

import java.math.BigDecimal;

import xyz.sluggard.transmatch.core.Engine;
import xyz.sluggard.transmatch.engine.ExecutorEngine;
import xyz.sluggard.transmatch.entity.Order;
import xyz.sluggard.transmatch.entity.Order.Side;
import xyz.sluggard.transmatch.event.EngineEvent;
import xyz.sluggard.transmatch.event.EngineListener;
import xyz.sluggard.transmatch.service.EventService;
import xyz.sluggard.transmatch.service.impl.EventServiceImpl;

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

```