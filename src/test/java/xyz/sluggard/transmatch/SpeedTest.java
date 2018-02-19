package xyz.sluggard.transmatch;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

import xyz.sluggard.transmatch.engine.MatchEngine;
import xyz.sluggard.transmatch.entity.Buying;
import xyz.sluggard.transmatch.entity.Selling;
import xyz.sluggard.transmatch.entity.Trade;
import xyz.sluggard.transmatch.service.impl.EventServiceImpl;

public class SpeedTest {
	
	public static MatchEngine engin = new MatchEngine();
	
	public static void main(String... args) throws Exception {
		engin.setEventService(new EventServiceImpl());
//		Thread.currentThread().setDaemon(true);
		Thread buyThread = new Thread(new CreateThread(true));
		Thread sellThread = new Thread(new CreateThread(false));
		buyThread.start();
		sellThread.start();
		Thread.sleep(6000l);
		System.out.println(engin.getBuyingQueue().size());
		System.out.println(engin.getSellingQueue().size());
	}
	
}

class CreateThread implements Runnable {
	
	private final boolean state;
	
	public CreateThread(boolean state) {
		this.state = state;
	}

	@Override
	public void run() {
		int i = 0;
		while(i < 1000) {
			Trade trade;
			if(state) {
				trade = new Buying();
			} else {
				trade = new Selling();
			}
			Random random = new Random();
			trade.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			trade.setAmount(new BigDecimal(random.nextInt(50)+1));
			trade.setPrice(new BigDecimal(random.nextInt(99)+1));
			trade.setTimestamp(System.currentTimeMillis());
			SpeedTest.engin.newTrade(trade);
			System.out.println(trade);
			i++;
		}
	}
	
}
