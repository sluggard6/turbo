package xyz.sluggard.transmatch;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

import xyz.sluggard.transmatch.engine.MatchEngine;
import xyz.sluggard.transmatch.entity.Buying;
import xyz.sluggard.transmatch.entity.Selling;
import xyz.sluggard.transmatch.entity.Trade;

public class SpeedTest {
	
	public static void main(String... args) {
		MatchEngine engin = new MatchEngine();
		
	}
	
}

class CreateThread implements Runnable {
	
	private final boolean state;
	
	public CreateThread(boolean state) {
		this.state = state;
	}

	@Override
	public void run() {
		Trade trade;
		if(state) {
			trade = new Buying();
		} else {
			trade = new Selling();
		}
		Random random = new Random();
		trade.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		trade.setAmount(new BigDecimal(random.nextInt(50)));
		trade.setPrice(new BigDecimal(random.nextInt(100)));
		trade.setTimestamp(System.currentTimeMillis());
	}
	
}
