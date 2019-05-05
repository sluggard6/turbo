package xyz.sluggard.transmatch.engine;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import lombok.extern.slf4j.Slf4j;
import xyz.sluggard.transmatch.core.Engine;
import xyz.sluggard.transmatch.entity.Order;
import xyz.sluggard.transmatch.entity.Order.Side;
import xyz.sluggard.transmatch.service.EventService;
import xyz.sluggard.transmatch.service.InitService;

@Slf4j
public class ThreadWapper implements Engine {
	

	private static final long TIME_OUT = 5000;
	
	
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
		executorService.submit(new Command(order));
		return true;
	}

	@Override
	public boolean cancelOrder(String orderId, Side side) {
		long start = System.currentTimeMillis();
		try {
			Future<Boolean> future = executorService.submit(new Command(orderId, side));
			while (!future.isDone()) {
				// 超时判断
				if (System.currentTimeMillis() - start > TIME_OUT) {
					if(log.isWarnEnabled()) {
						log.warn("cancel order timeout : " + (System.currentTimeMillis() - start));
					}
					return false;
				}
				Thread.sleep(100);
			}
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
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
		proxy.start();
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
}
