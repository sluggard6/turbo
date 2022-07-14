package com.github.transmatch;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.github.transmatch.engine.ThreadWapper;
import com.github.transmatch.entity.Order;
import com.github.transmatch.entity.Order.Side;
import com.github.transmatch.service.SingleThreadPrintEventServiceImpl;

public class PressTest {
	
	static SingleThreadPrintEventServiceImpl eventService = new SingleThreadPrintEventServiceImpl();
	
	static ThreadWapper engine = new ThreadWapper("btc_usdt", eventService);
	
	static Map<Integer, Integer> countMap = new HashMap<Integer, Integer>();
	
	static Random random = new Random();
	
	static final int MAX = 100000;
	
	public static void main(String... args) {
		engine.start();
		for(int i = 0;i  < MAX; i++) {
			Order ask = new Order(String.valueOf(i+1),new BigDecimal(i+MAX+1), new BigDecimal(i+1), Side.ASK);
			Order bid = new Order(String.valueOf(i+MAX+1),new BigDecimal(i+1), new BigDecimal(i+1), Side.BID);
			engine.newOrder(ask);
			engine.newOrder(bid);
		}
		ExecutorService es = Executors.newFixedThreadPool(100);
		System.out.println(engine.getAskQueue().size());
		System.out.println(engine.getBidQueue().size());
		MatchEventCount mec = eventService.getMec();
		System.out.println(mec);
		System.out.println("cancel start");
		for(int j = 0; j < MAX*3; j++) {
			int id = random.nextInt(MAX*3) + 1;
			if(id <= MAX) {
				es.execute(new Command(System.currentTimeMillis(), id, Side.ASK));
			}else if(id <= 2000) {
				es.execute(new Command(System.currentTimeMillis(), id, Side.BID));
			}else {
				Order order;
				if(random.nextBoolean()) {
					order = new Order(String.valueOf(id),new BigDecimal(MAX+1), new BigDecimal(1), Side.ASK);
				}else {
					order = new Order(String.valueOf(id),new BigDecimal(1), new BigDecimal(1), Side.BID);
				}
				engine.newOrder(order);
			}
			Integer i = countMap.getOrDefault(id, 0);
			countMap.put(id, i+1);
//			System.out.println(j);
		}
		System.out.println("---------------------------done----------------------------");
//		countMap.forEach((k, v) -> {
//			System.out.println(k + " : " + v);
//		});
		System.out.println(engine.getAskQueue().size());
		System.out.println(engine.getBidQueue().size());
		es.shutdown();
	}
	
	private static class Command implements Runnable{
		
		public Command(long starttime, int id, Side side) {
			super();
			this.starttime = starttime;
			this.id = id;
			this.side = side;
		}

		long starttime;
		int id;
		Side side;

		@Override
		public void run() {
			long start = System.currentTimeMillis();
			boolean b = engine.cancelOrder(String.valueOf(id), side);
			long end = System.currentTimeMillis();
			System.out.println(String.format("cancel id : %d, %s, use time : %d, full time : %d", id, String.valueOf(b), end - start, end - starttime));
		}
		
	}

}
