# turbo

[![Build Status](https://travis-ci.org/sluggard6/turbo.svg?branch=master)](https://travis-ci.org/sluggard6/turbo)
![license](https://img.shields.io/github/license/alibaba/dubbo.svg)

A Market Order Matching Engine


## Getting started

```java
package com.github.turbo.demo;

import java.math.BigDecimal;

import com.github.transmatch.core.Engine;
import com.github.transmatch.engine.ThreadWapper;
import com.github.transmatch.entity.Order;
import com.github.transmatch.entity.Order.Side;
import com.github.transmatch.event.EngineEvent;
import com.github.transmatch.event.EngineListener;
import com.github.transmatch.service.EventService;
import com.github.transmatch.service.SingleThreadPrintEventServiceImpl;

public class Test {
	
	public static void main(String... args) {
		try(EventService eventService = new SingleThreadPrintEventServiceImpl()) {
			eventService.addListener(new EngineListener() {
	
				@Override
				public void onEvent(EngineEvent event) {
					System.out.println(event);
				}
			});
			Engine engine = new ThreadWapper("BTC_USDT", eventService);
			engine.start();
			
			Order askOrder = new Order("1", new BigDecimal(100), new BigDecimal(10), Side.ASK);
			Order bidOrder = new Order("2", new BigDecimal(100), new BigDecimal(20), Side.BID);
			engine.newOrder(askOrder);
			engine.newOrder(bidOrder);
			Thread.sleep(1000);
			engine.stop();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
}

```