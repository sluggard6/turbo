package xyz.sluggard.transmatch;


import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import xyz.sluggard.transmatch.engine.SyncEngine;
import xyz.sluggard.transmatch.entity.Order;
import xyz.sluggard.transmatch.entity.Order.Category;
import xyz.sluggard.transmatch.entity.Order.Side;
import xyz.sluggard.transmatch.entity.Order.Type;
import xyz.sluggard.transmatch.entity.Trade;
import xyz.sluggard.transmatch.event.CancelEvent;
import xyz.sluggard.transmatch.event.EngineEvent;
import xyz.sluggard.transmatch.event.EngineListener;
import xyz.sluggard.transmatch.event.MakerEvent;
import xyz.sluggard.transmatch.event.OrderEvent;
import xyz.sluggard.transmatch.event.TradeEvent;
import xyz.sluggard.transmatch.service.impl.AbstractEventService;

public class SyncMatchTest {
	
	private SyncEngine engine;
	
	private List<EngineEvent> events = new CopyOnWriteArrayList<EngineEvent>();
	
//	private TestEventServiceImpl eventService = new TestEventServiceImpl();
	
	@BeforeMethod
	public void init() {
//		engine = new ExecutorEngine("BTC_USD", new DispenseEventServiceImpl());
//		engine = new ExecutorEngine("BTC_USD", eventService);
		engine = new SyncEngine("BTC_USD", new AbstractEventService() {
		});
//		engine = new ExecutorEngine("BTC_USD");
		engine.getEventService().addListener(new EngineListener() {
			
			@Override
			public void onEvent(EngineEvent event) {
				events.add(event);
			}
		});
		engine.start();
	}
	
	@AfterMethod
	public void destroy() throws Exception {
		events.clear();
	}
	
	@Test
	public void OneMatch() throws Exception {
		Order order1 = new Order("1", BigDecimal.ONE, BigDecimal.ONE, Side.ASK);
		Order order2 = new Order("2", BigDecimal.ONE, BigDecimal.ONE, Side.BID);
		engine.newOrder(order1);
		engine.newOrder(order2);
		engine.stop();
		System.out.println("---------------one match------------------");
		for(EngineEvent event : events) {
			System.out.println(event);
			if(event instanceof TradeEvent) {
				Trade trade = ((TradeEvent) event).getTrade();
				assertTrue(trade.getAmount().compareTo(BigDecimal.ONE) == 0);
				assertTrue(trade.getPrice().compareTo(BigDecimal.ONE) == 0);
				assertTrue(trade.getAskOrderId().equals("1"));
				assertTrue(trade.getBidOrderId().equals("2"));
				assertTrue(trade.getMakerOrderId().equals("1"));
				assertTrue(trade.getTakerOrderId().equals("2"));
			}
			if(event instanceof OrderEvent) {
				assertFalse(((OrderEvent) event).getOrder().isDone());
			}if(event instanceof MakerEvent) {
				assertTrue(((MakerEvent) event).getOrder().getId().equals("1"));
			}
		}
	}
	
	@Test
	public void TowMatch() throws Exception {
		Order order1 = new Order("1", BigDecimal.valueOf(2), BigDecimal.ONE, Side.ASK);
		Order order2 = new Order("2", BigDecimal.ONE, BigDecimal.ONE, Side.BID);
		Order order3 = new Order("3", BigDecimal.ONE, BigDecimal.ONE, Side.ASK);
		engine.newOrder(order1);
		engine.newOrder(order2);
		engine.newOrder(order3);
		engine.stop();
		System.out.println("---------------two match------------------");
		for(EngineEvent event : events) {
			System.out.println(event);
			if(event instanceof TradeEvent) {
				Trade trade = ((TradeEvent) event).getTrade();
				assertTrue(trade.getAmount().compareTo(BigDecimal.ONE) == 0);
				assertEquals(trade.getPrice().compareTo(new BigDecimal("1")), 0);
				assertTrue(trade.getAskOrderId().equals("3"));
				assertTrue(trade.getBidOrderId().equals("2"));
				assertTrue(trade.getMakerOrderId().equals("2"));
				assertTrue(trade.getTakerOrderId().equals("3"));
			}
			if(event instanceof OrderEvent) {
				Order order = ((OrderEvent) event).getOrder();
				assertFalse(order.isDone());
//				if(order.getId().equals("1")) {
//					assertTrue(!order.isDone());
//				}else {
//					assertTrue(order.isDone());
//				}
			}
			if(event instanceof MakerEvent) {
				Order order = ((MakerEvent) event).getOrder();
				assertTrue((order.getId().equals("1") || order.getId().equals("2")));
			}
		}
	}
	
	@Test
	public void fokMatch() throws Exception {
		Order order1 = new Order("1", BigDecimal.ONE, BigDecimal.ONE, Side.ASK);
		Order order2 = new Order("2", BigDecimal.valueOf(2), BigDecimal.ONE, Side.ASK);
		Order order3 = new Order("3", BigDecimal.valueOf(3), BigDecimal.ONE, Side.ASK);
		Order order4 = new Order("4", BigDecimal.valueOf(4), BigDecimal.ONE, Side.ASK);
		Order order5 = new Order("5", BigDecimal.valueOf(3), BigDecimal.valueOf(5), Side.BID, Category.FOK);
		Order order6 = new Order("6", BigDecimal.valueOf(3), BigDecimal.valueOf(3), Side.BID, Category.FOK);
		engine.newOrder(order1);
		engine.newOrder(order2);
		engine.newOrder(order3);
		engine.newOrder(order4);
		engine.newOrder(order5);
		engine.newOrder(order6);
		engine.stop();
		System.out.println("---------------fok match------------------");
		for(EngineEvent event : events) {
			System.out.println(event);
			if(event instanceof CancelEvent) {
				assertEquals("5", ((CancelEvent) event).getOrder().getId());
			}
		}
	}
	
	@Test
	public void marketMatch() throws Exception {
		Order order1 = new Order("1", Type.MARKET, Side.ASK);
		order1.setAmount(BigDecimal.valueOf(1));
		engine.newOrder(order1);
		engine.stop();
		System.out.println("---------------market match------------------");
		for(EngineEvent event : events) {
			System.out.println(event);
			if(event instanceof CancelEvent) {
				assertEquals("1", ((CancelEvent) event).getOrder().getId());
			}
		}
	}

}
