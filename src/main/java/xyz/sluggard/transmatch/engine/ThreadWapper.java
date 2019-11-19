package xyz.sluggard.transmatch.engine;

import static xyz.sluggard.transmatch.utils.OrderValidater.validateOrder;

import java.util.Collection;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import lombok.extern.slf4j.Slf4j;
import xyz.sluggard.transmatch.core.Engine;
import xyz.sluggard.transmatch.entity.Order;
import xyz.sluggard.transmatch.entity.Order.Side;
import xyz.sluggard.transmatch.entity.OrderBook;
import xyz.sluggard.transmatch.event.OrderBookEvent;
import xyz.sluggard.transmatch.service.EventService;
import xyz.sluggard.transmatch.service.InitService;

@Slf4j
public class ThreadWapper implements Engine {
	
	/**
	 * 5秒最长等待时间
	 */
	private static final int TIME_OUT = 5;
	
	
	public ThreadWapper(String currencyPair) {
		proxy = new SyncEngine(currencyPair);
	}
	
	public ThreadWapper(String currencyPair, EventService eventService) {
		proxy = new SyncEngine(currencyPair, eventService);
	}
	
	public ThreadWapper(String currencyPair, EventService eventService, InitService initService) {
		proxy = new SyncEngine(currencyPair, eventService, initService);
	}

	private SyncEngine proxy;
	
	private ExecutorService executorService;
	
	@Override
	public String getCurrencyPair() {
		return proxy.getCurrencyPair();
	}

	@Override
	public boolean newOrder(Order order) {
		if(validateOrder(order)) {
			executorService.submit(new Command(order));
			return true;
		}
		return false;
	}

	@Override
	public boolean cancelOrder(String orderId, Side side) {
		try {
			long start = System.currentTimeMillis();
			Future<Boolean> future = executorService.submit(new Command(orderId, side));
			boolean b = future.get(TIME_OUT, TimeUnit.SECONDS);
			System.out.println("running time : " + (System.currentTimeMillis() - start));
			return b;
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			log.error("cancel order error", e);
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean cancelOrder(Order order) {
		return cancelOrder(order.getId(), order.getSide());
	}

	@Override
	public int getQuotePrecision() {
		return proxy.getQuotePrecision();
	}

	@Override
	public void setQuotePrecision(int quotePrecision) {
		proxy.setQuotePrecision(quotePrecision);
	}

	@Override
	public Collection<Order> getBidQueue() {
		return proxy.getBidQueue();
	}

	@Override
	public Collection<Order> getAskQueue() {
		return proxy.getAskQueue();
	}

	@Override
	public EventService getEventService() {
		return proxy.getEventService();
	}

	@Override
	public long getUpdateId() {
		return proxy.getUpdateId();
	}

	@Override
	public OrderBookEvent getOrderBook() {
		try {
			Future<OrderBookEvent> future = executorService.submit(new OrderBookCommand());
			return future.get(TIME_OUT, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			log.error("getOrderBook error", e);
			return null;
		}
	}

	@Override
	@PostConstruct
	public void start() {
		try {
			if (executorService != null && !executorService.isShutdown())
				throw new IllegalStateException();
			executorService = Executors.newSingleThreadExecutor();
			proxy.start();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@Override
	@PreDestroy
	public void stop() {
		executorService.shutdown();
		proxy.stop();
	}
	
	private class Command implements Callable<Boolean> {
		
		private boolean cancal;
		
		private Order order;
		
		private String orderId;
		
		private Side side;
		
		public Command(String orderId, Side side) {
			this(orderId, side, true, null);
		}
		
		public Command(Order order) {
			this(null, null, false, order);
		}
		
		public Command(String orderId, Side side, boolean cancel, Order order) {
			this.order = order;
			this.orderId = orderId;
			this.side = side;
			this.cancal = cancel;
		}
		
		private boolean isAsk() {
			return side == Side.ASK;
		}
		
		@Override
		public Boolean call() {
			try {
				if(cancal) {
					if(isAsk()) {
						return proxy.cancelOrder(orderId, side);
					}else {
						return proxy.cancelOrder(orderId, side);
					}
				}else {
					proxy.newOrder(order);
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error("match error", e);
				return false;
			}
		}
	}
	
	private class OrderBookCommand implements Callable<OrderBookEvent>{

		@Override
		public OrderBookEvent call() throws Exception {
			OrderBook orderBook = new OrderBook();
			orderBook.setAsks(new TreeSet<>(getAskQueue()));
			orderBook.setBids(new TreeSet<>(getBidQueue()));
			return new OrderBookEvent(orderBook, proxy);
		}
		
	}
}
