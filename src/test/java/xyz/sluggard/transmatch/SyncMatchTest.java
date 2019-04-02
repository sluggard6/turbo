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
		MatchEventCount mec = new MatchEventCount();
		for(EngineEvent event : events) {
			System.out.println(event);
			if(event instanceof CancelEvent) {
				assertEquals("1", ((CancelEvent) event).getOrder().getId());
				mec.countCancelEvent();
			}
		}
		assertEquals(1, mec.getCancelEventCount());
	}
	
	@Test
	public void marketBidMatch() throws Exception {
		Order order1 = new Order("1", Type.MARKET, Side.ASK);
		order1.setAmount(BigDecimal.valueOf(1));
		engine.newOrder(order1);
		engine.stop();
		System.out.println("---------------market match------------------");
		MatchEventCount mec = new MatchEventCount();
		for(EngineEvent event : events) {
			System.out.println(event);
			if(event instanceof CancelEvent) {
				assertEquals("1", ((CancelEvent) event).getOrder().getId());
				mec.countCancelEvent();
			}
		}
		assertEquals(1, mec.getCancelEventCount());
	}
	
	@Test
	public void marketTradeMatch() throws Exception {
		Order order1 = new Order("1", Type.LIMIT, Side.ASK);
		order1.setPrice(new BigDecimal("1"));
		order1.setAmount(new BigDecimal("3"));
		Order order2 = new Order("2", Type.MARKET, Side.BID);
		order2.setFunds(new BigDecimal(5));
		engine.newOrder(order1);
		engine.newOrder(order2);
		engine.stop();
		System.out.println("---------------market trade match------------------");
		MatchEventCount mec = new MatchEventCount();
		for(EngineEvent event : events) {
			System.out.println(event);
			if(event instanceof CancelEvent) {
				assertEquals("2", ((CancelEvent) event).getOrder().getId());
				mec.countCancelEvent();
			}
			if(event instanceof TradeEvent) {
				assertEquals(0, ((TradeEvent) event).getTrade().getPrice().compareTo(BigDecimal.ONE));
				mec.countTradeEvent();
			}
		}
		assertEquals(1, mec.getCancelEventCount());
		assertEquals(1, mec.getTradeEventCount());
	}
	
	@Test
	public void marketTradeMatch2() throws Exception {
		Order order1 = new Order("1", Type.LIMIT, Side.BID);
		order1.setPrice(new BigDecimal("1"));
		order1.setAmount(new BigDecimal("3"));
		Order order2 = new Order("2", Type.MARKET, Side.ASK);
		order2.setAmount(new BigDecimal(5));
		engine.newOrder(order1);
		engine.newOrder(order2);
		engine.stop();
		System.out.println("---------------market trade match2------------------");
		MatchEventCount mec = new MatchEventCount();
		for(EngineEvent event : events) {
			System.out.println(event);
			if(event instanceof CancelEvent) {
				assertEquals("2", ((CancelEvent) event).getOrder().getId());
				mec.countCancelEvent();
			}
			if(event instanceof TradeEvent) {
				assertEquals(0, ((TradeEvent) event).getTrade().getPrice().compareTo(BigDecimal.ONE));
				mec.countTradeEvent();
			}
		}
		assertEquals(1, mec.getCancelEventCount());
		assertEquals(1, mec.getTradeEventCount());
	}
	
	@Test
	public void marketTradeMatch3() throws Exception {
		Order order1 = new Order("1", Type.LIMIT, Side.ASK);
		order1.setPrice(new BigDecimal("1"));
		order1.setAmount(new BigDecimal("3"));
		Order order2 = new Order("2", Type.MARKET, Side.BID);
		order2.setFunds(new BigDecimal(3));
		engine.newOrder(order1);
		engine.newOrder(order2);
		engine.stop();
		System.out.println("---------------market trade match3------------------");
		MatchEventCount mec = new MatchEventCount();
		for(EngineEvent event : events) {
			System.out.println(event);
			if(event instanceof CancelEvent) {
				mec.countCancelEvent();
			}
			if(event instanceof TradeEvent) {
				assertEquals(0, ((TradeEvent) event).getTrade().getPrice().compareTo(BigDecimal.ONE));
				mec.countTradeEvent();
			}
		}
		assertEquals(0, mec.getCancelEventCount());
		assertEquals(1, mec.getTradeEventCount());
	}
	
	
	@Test
	public void marketTradeMatch4() throws Exception {
		Order order1 = new Order("1", Type.LIMIT, Side.BID);
		order1.setPrice(new BigDecimal("1"));
		order1.setAmount(new BigDecimal("3"));
		Order order2 = new Order("2", Type.MARKET, Side.ASK);
		order2.setAmount(new BigDecimal(3));
		engine.newOrder(order1);
		engine.newOrder(order2);
		engine.stop();
		System.out.println("---------------market trade match4------------------");
		MatchEventCount mec = new MatchEventCount();
		for(EngineEvent event : events) {
			System.out.println(event);
			if(event instanceof CancelEvent) {
				mec.countCancelEvent();
			}
			if(event instanceof TradeEvent) {
				assertEquals(0, ((TradeEvent) event).getTrade().getPrice().compareTo(BigDecimal.ONE));
				mec.countTradeEvent();
			}
		}
		assertEquals(0, mec.getCancelEventCount());
		assertEquals(1, mec.getTradeEventCount());
	}
	
	@Test
	public void marketTradeMatch5() throws Exception {
		Order order1 = new Order("1", Type.LIMIT, Side.ASK);
		order1.setPrice(new BigDecimal("1"));
		order1.setAmount(new BigDecimal("3"));
		Order order2 = new Order("2", Type.MARKET, Side.BID);
		order2.setFunds(new BigDecimal("2.5"));
		engine.setQuotePrecision(0);
		engine.newOrder(order1);
		engine.newOrder(order2);
		engine.stop();
		System.out.println("---------------market trade match5------------------");
		MatchEventCount mec = new MatchEventCount();
		for(EngineEvent event : events) {
			System.out.println(event);
			if(event instanceof CancelEvent) {
				assertEquals("2", ((CancelEvent) event).getOrder().getId());
				assertEquals(0, ((CancelEvent) event).getOrder().getFunds().negate().compareTo(new BigDecimal("0.5")));
				mec.countCancelEvent();
			}
			if(event instanceof TradeEvent) {
				assertEquals(0, ((TradeEvent) event).getTrade().getPrice().compareTo(BigDecimal.ONE));
				mec.countTradeEvent();
			}
		}
		assertEquals(1, mec.getCancelEventCount());
		assertEquals(1, mec.getTradeEventCount());
	}
	

	
	@Test
	public void marketSideTest() {
		Order order1 = new Order("1", Type.LIMIT, Side.ASK);
		order1.setPrice(BigDecimal.valueOf(5));
		order1.setAmount(BigDecimal.valueOf(50));
		Order order2 = new Order("2", Type.LIMIT, Side.BID);
		order2.setPrice(BigDecimal.valueOf(4));
		order2.setAmount(BigDecimal.valueOf(50));
		Order order3 = new Order("3", Type.MARKET, Side.BID);
		order3.setFunds(BigDecimal.valueOf(150));
		Order order4 = new Order("4", Type.MARKET, Side.ASK);
		order4.setAmount(BigDecimal.valueOf(30));
		Order order5 = new Order("5", Type.MARKET, Side.BID);
		order5.setFunds(BigDecimal.valueOf(150));
		Order order6 = new Order("6", Type.MARKET, Side.ASK);
		order6.setAmount(BigDecimal.valueOf(30));
		engine.newOrder(order1);
		engine.newOrder(order2);
		engine.newOrder(order3);
		engine.newOrder(order4);
		engine.newOrder(order5);
		engine.newOrder(order6);
		engine.stop();
		System.out.println("---------------market side match------------------");
		MatchEventCount mec = new MatchEventCount();
		for(EngineEvent event : events) {
			System.out.println(event);
			if(event instanceof CancelEvent) {
				mec.countCancelEvent();
			}
			if(event instanceof TradeEvent) {
				mec.countTradeEvent();
			}
		}
		assertEquals(2, mec.getCancelEventCount());
		assertEquals(4, mec.getTradeEventCount());
	}
	
	@Test
	public void marketPartMatchTest() {
		Order order1 = new Order("1", Type.LIMIT, Side.ASK);
		order1.setPrice(new BigDecimal("0.52"));
		order1.setAmount(BigDecimal.valueOf(500));
		Order order2 = new Order("2", Type.MARKET, Side.BID);
		order2.setFunds(BigDecimal.valueOf(1));
		engine.newOrder(order1);
		engine.newOrder(order2);
		engine.stop();
		System.out.println("---------------market part match------------------");
		MatchEventCount mec = new MatchEventCount();
		for(EngineEvent event : events) {
			System.out.println(event);
			if(event instanceof CancelEvent) {
				mec.countCancelEvent();
			}
			if(event instanceof TradeEvent) {
				mec.countTradeEvent();
			}
		}
		assertEquals(1, mec.getCancelEventCount());
		assertEquals(1, mec.getTradeEventCount());
	}
	
	@Test 
	public void sideQueueTest() {
		Order order1 = new Order("1", Type.LIMIT, Side.ASK);
		order1.setPrice(new BigDecimal("2"));
		order1.setAmount(new BigDecimal("3"));
		Order order2 = new Order("1", Type.LIMIT, Side.ASK);
		order2.setPrice(new BigDecimal("2"));
		order2.setAmount(new BigDecimal("3"));
		Order order3 = new Order("1", Type.LIMIT, Side.BID);
		order3.setPrice(new BigDecimal("1"));
		order3.setAmount(new BigDecimal("3"));
		Order order4 = new Order("1", Type.LIMIT, Side.BID);
		order4.setPrice(new BigDecimal("1"));
		order4.setAmount(new BigDecimal("3"));
		Order order5 = new Order("1", Type.LIMIT, Side.BID);
		order5.setPrice(new BigDecimal("1"));
		order5.setAmount(new BigDecimal("3"));
		engine.newOrder(order1);
		engine.newOrder(order2);
		engine.newOrder(order3);
		engine.newOrder(order4);
		engine.newOrder(order5);
		assertEquals(2, engine.getAskQueue().size());
		assertEquals(3, engine.getBidQueue().size());
		engine.stop();
	}
	
	@Test
	public void realDataTest() {
		Order order1 = new Order("1", Type.LIMIT, Side.BID);
		order1.setPrice(new BigDecimal("0.29"));
		order1.setAmount(new BigDecimal("9"));
		Order order2 = new Order("2", Type.LIMIT, Side.BID);
		order2.setPrice(new BigDecimal("0.28"));
		order2.setAmount(new BigDecimal("5"));
		Order order3 = new Order("3", Type.LIMIT, Side.ASK);
		order3.setPrice(new BigDecimal("0.28"));
		order3.setAmount(new BigDecimal("3"));
		engine.newOrder(order1);
		engine.newOrder(order2);
		engine.newOrder(order3);
		System.out.println("---------------real data test------------------");
		MatchEventCount mec = new MatchEventCount();
		for(EngineEvent event : events) {
			System.out.println(event);
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
		}
		assertEquals(2, engine.getBidQueue().size());
	}

}
