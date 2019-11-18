package xyz.sluggard.transmatch;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import xyz.sluggard.transmatch.engine.ThreadWapper;
import xyz.sluggard.transmatch.entity.Order;
import xyz.sluggard.transmatch.entity.Order.Side;

public class PressTest {
	
	static ThreadWapper engine = new ThreadWapper("btc_usdt");
	
	static Map<Integer, Integer> countMap = new HashMap<Integer, Integer>();
	
	static Random random = new Random();
	
	public static void main(String... args) {
		engine.start();
		for(int i = 0;i  < 1000; i++) {
			Order ask = new Order(String.valueOf(i+1),new BigDecimal(i+1001), new BigDecimal(i), Side.ASK);
			Order bid = new Order(String.valueOf(i+1001),new BigDecimal(i+1), new BigDecimal(i), Side.ASK);
			engine.newOrder(ask);
			engine.newOrder(bid);
		}
		ExecutorService es = Executors.newFixedThreadPool(100);
		System.out.println("cancel start");
		for(int j = 0; j < 3000; j++) {
			int id = random.nextInt(3000) + 1;
			if(id < 1000) {
				es.execute(new Command(System.currentTimeMillis(), id));
			}else if(id < 2000) {
				es.execute(new Command(System.currentTimeMillis(), id));
			}else {
				Order order;
				if(random.nextBoolean()) {
					order = new Order(String.valueOf(id),new BigDecimal(20001), new BigDecimal(1), Side.ASK);
				}else {
					order = new Order(String.valueOf(id),new BigDecimal(1), new BigDecimal(1), Side.BID);
				}
				engine.newOrder(order);
			}
			Integer i = countMap.getOrDefault(id, 0);
			countMap.put(id, i+1);
//			System.out.println(j);
		}
		es.shutdown();
		System.out.println("---------------------------done----------------------------");
		countMap.forEach((k, v) -> {
			System.out.println(k + " : " + v);
		});
		System.out.println(engine.getAskQueue().size());
		System.out.println(engine.getBidQueue().size());
	}
	
	private static class Command implements Runnable{
		
		public Command(long starttime, int id) {
			super();
			this.starttime = starttime;
			this.id = id;
		}

		long starttime;
		int id;

		@Override
		public void run() {
			boolean b = engine.cancelOrder(String.valueOf(id), Side.ASK);
			System.out.println(String.format("cancel id : %d, %s, use time : %d", id, String.valueOf(b), System.currentTimeMillis() - starttime));
		}
		
	}

}
