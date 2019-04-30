package xyz.sluggard.transmatch;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;

import org.testng.annotations.Test;

import xyz.sluggard.transmatch.entity.Order;
import xyz.sluggard.transmatch.entity.Trade;
import xyz.sluggard.transmatch.entity.Order.Side;
import xyz.sluggard.transmatch.event.CancelEvent;
import xyz.sluggard.transmatch.event.EngineEvent;
import xyz.sluggard.transmatch.event.MakerEvent;
import xyz.sluggard.transmatch.event.OrderEvent;
import xyz.sluggard.transmatch.event.TradeEvent;

public class CancelTest extends AbstracrtTransmatchTest{
	
	@Test
	public void OneMatch() throws Exception {
		Order order1 = new Order("1", BigDecimal.ONE, new BigDecimal("10"), Side.ASK);
		Order order2 = new Order("2", BigDecimal.ONE, new BigDecimal("5"), Side.BID);
		engine.newOrder(order1);
		engine.newOrder(order2);
		engine.cancelOrder(order1);
		System.out.println("---------------one match------------------");
		for(EngineEvent event : events) {
			System.out.println(event);
			if(event instanceof TradeEvent) {
				Trade trade = ((TradeEvent) event).getTrade();
				assertTrue(trade.getAmount().compareTo(new BigDecimal("5")) == 0);
				assertTrue(trade.getPrice().compareTo(BigDecimal.ONE) == 0);
				assertTrue(trade.getAskOrderId().equals("1"));
				assertTrue(trade.getBidOrderId().equals("2"));
				assertTrue(trade.getMakerOrderId().equals("1"));
				assertTrue(trade.getTakerOrderId().equals("2"));
			}
			if(event instanceof OrderEvent) {
				assertFalse(((OrderEvent) event).getOrder().isDone());
			}
			if(event instanceof MakerEvent) {
				assertTrue(((MakerEvent) event).getOrder().getId().equals("1"));
			}
			if(event instanceof CancelEvent) {
				assertEquals(new BigDecimal("-5"), ((CancelEvent) event).getOrder().getAmount());
			}
		}
		assertEquals(2, mec.getOrderEventCount());
		assertEquals(1, mec.getMakerEventCount());
		assertEquals(1, mec.getTradeEventCount());
		assertEquals(1, mec.getCancelEventCount());
	}
	
	@Test
	public void OtherMatch() throws Exception {
		Order order1 = new Order("5639", new BigDecimal("0.00032226"), new BigDecimal("57"), Side.BID);
		Order order2 = new Order("2", BigDecimal.ONE, new BigDecimal("5"), Side.BID);
		engine.newOrder(order1);
		engine.cancelOrder(order1);
		engine.newOrder(order2);
		System.out.println("---------------other match------------------");
		for(EngineEvent event : events) {
			System.out.println(event);
//			if(event instanceof TradeEvent) {
//				Trade trade = ((TradeEvent) event).getTrade();
//				assertTrue(trade.getAmount().compareTo(new BigDecimal("5")) == 0);
//				assertTrue(trade.getPrice().compareTo(BigDecimal.ONE) == 0);
//				assertTrue(trade.getAskOrderId().equals("1"));
//				assertTrue(trade.getBidOrderId().equals("2"));
//				assertTrue(trade.getMakerOrderId().equals("1"));
//				assertTrue(trade.getTakerOrderId().equals("2"));
//			}
			if(event instanceof OrderEvent) {
				assertFalse(((OrderEvent) event).getOrder().isDone());
			}
//			if(event instanceof MakerEvent) {
//				assertTrue(((MakerEvent) event).getOrder().getId().equals("5639"));
//			}
			if(event instanceof CancelEvent) {
				assertEquals(new BigDecimal("-57"), ((CancelEvent) event).getOrder().getAmount());
			}
		}
		assertEquals(2, mec.getOrderEventCount());
		assertEquals(2, mec.getMakerEventCount());
		assertEquals(0, mec.getTradeEventCount());
		assertEquals(1, mec.getCancelEventCount());
		System.out.println("------------------------");
		System.out.println(engine.getBidQueue());
	}

}
