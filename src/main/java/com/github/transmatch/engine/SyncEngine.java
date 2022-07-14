package com.github.transmatch.engine;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.github.transmatch.entity.Order;
import com.github.transmatch.entity.Trade;
import com.github.transmatch.entity.Order.Side;
import com.github.transmatch.entity.Order.Status;
import com.github.transmatch.event.CancelEvent;
import com.github.transmatch.event.MakerEvent;
import com.github.transmatch.event.OrderEvent;
import com.github.transmatch.event.TradeEvent;
import com.github.transmatch.service.EventService;
import com.github.transmatch.service.InitService;
import com.github.transmatch.utils.IdManager;

public class SyncEngine extends AbstractEngine {

//	private final SortedSetQueue<Order> bidQueue = new SortedSetQueue<>(new SideComparator());
//
//	private final SortedSetQueue<Order> askQueue = new SortedSetQueue<>(new SideComparator());

	private final Queue<Order> bidQueue = new PriorityBlockingQueue<>();

	private final Queue<Order> askQueue = new PriorityBlockingQueue<>();
	
//	private final Set<String> idSet = new ConcurrentHashMap<String, String>().keySet();
	
//	private final Queue<Action> actionQueue = new ConcurrentLinkedQueue<>();

	private boolean fokCheck;

	public SyncEngine(String currencyPair) {
		super(currencyPair);
	}

	public SyncEngine(String currencyPair, EventService eventService) {
		super(currencyPair, eventService);
	}

	public SyncEngine(String currencyPair, EventService eventService, InitService initService) {
		super(currencyPair, eventService, initService);
	}

	@Override
	public boolean newOrder(Order order) {
//		chekcOrderId(order);
		order.setNanotime(System.nanoTime());
		eventService.publishEvent(new OrderEvent(order.clone(), this));
		doOrder(order);
		return true;
	}

//	private void chekcOrderId(Order order) {
//		if(order == null) throw new NullPointerException();
//		if(idSet.contains(order.getId())) {
//			throw new ExistedIdException(String.format("orderId %s is existed in engine", order.getId()));
//		}
//	}

	private void doOrder(Order order) {
		if (order.isFok() && !fokCheck) {
//			SortedSet<Order> set = getOppositeQueue(order.isBid()).headSet(order);
//			BigDecimal sum = set.stream().map(Order::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
			BigDecimal sum = getOppositeQueue(order.isBid()).stream().filter(o -> {
				if (order.isBid()) {
					return canMatch(order, o);
				} else {
					return canMatch(o, order);
				}
			}).map(Order::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
			if (sum.compareTo(order.getAmount()) >= 0) {
				fokCheck = true;
			} else {
				eventService.publishEvent(new CancelEvent(order, this));
				return;
			}
		}
		Order op = getOppositeQueue(order.isBid()).peek();
		if (op == null) {
			if (order.isMarket()) {
				eventService.publishEvent(new CancelEvent(order, this));
			} else {
				addSameQueue(order);
			}
			return;
		}
		if (!noneMatch(order, op)) {
			if (order.isMarket() || order.isIoc()) {
				eventService.publishEvent(new CancelEvent(order, this));
			} else {
				addSameQueue(order);
			}
		} else {
			if (op.isDone()) {
				getOppositeQueue(order.isBid()).poll();
			}
			if (!orderIsDone(order)) {
				doOrder(order);
			} else {
				if (order.isFok()) {
					fokCheck = false;
				}
				if (order.isMarket() && order.isBid() && order.getFunds().compareTo(BigDecimal.ZERO) > 0) {
					eventService.publishEvent(new CancelEvent(order, this));
				}
			}
		}
	}

	private final boolean noneMatch(Order o1, Order o2) {
		if (o1.isAsk() ^ o2.isAsk()) {
			if (o1.isAsk()) {
				return match(o2, o1);
			} else {
				return match(o1, o2);
			}
		} else {
			throw new IllegalArgumentException("same side order can't make trade");
		}
	}

	/**
	 * 成交方法
	 * 
	 * @param bidOrder 买单
	 * @param askOrder 卖单
	 * @return
	 */
	private final boolean match(Order bidOrder, Order askOrder) {
		if (canMatch(bidOrder, askOrder)) {
//			BigDecimal min = bidOrder.getAmount().min(askOrder.getAmount());
			BigDecimal min = getMinAmount(bidOrder, askOrder);
			if (min.compareTo(BigDecimal.ZERO) == 0) {
				return false;
			}
			bidOrder.subtractAmount(min, askOrder.getPrice());
			askOrder.subtractAmount(min, bidOrder.getPrice());
			MatchPrice price = getPrice(bidOrder, askOrder);
			Trade trade = new Trade(bidOrder.getId(), askOrder.getId(), price.price, min, price.maker.getId(),
					price.taker.getId());
			eventService.publishEvent(new TradeEvent(trade, this));
			return true;
		}
		return false;
	}

	private static final MatchPrice getPrice(Order o1, Order o2) {
		if (o1.getStatus() == o2.getStatus()) {
			throw new IllegalStateException("不能同时出现2个" + o1.getStatus());
		}
		if (o1.isMaker()) {
			return new MatchPrice(o1, o2);
		} else {
			return new MatchPrice(o2, o1);
		}
//		if(o1.getNanotime() < o2.getNanotime()) {
//			return new MatchPrice(o1, o2);
//		}else {
//			return new MatchPrice(o2, o1);
//		}
	}

	private static class MatchPrice {
		private final Order maker;

		private final Order taker;

		private final BigDecimal price;

		public MatchPrice(Order maker, Order taker) {
			super();
			if (maker.isMarket()) {
				throw new IllegalStateException("market order can't to be maker");
			}
			this.maker = maker;
			this.taker = taker;
			this.price = maker.getPrice();
		}

	}

	@Override
	public boolean cancelOrder(String orderId, Side side) {
		if (side.equals(Side.ASK)) {
			return cancelOrderIter(askQueue, orderId) != null;
		} else {
			return cancelOrderIter(bidQueue, orderId) != null;
		}
	}

	private void addSameQueue(Order order) {
		order.setStatus(Status.MAKER);
		eventService.publishEvent(new MakerEvent(order.clone(), this));
		getSameQueue(order.isBid()).add(order);

	}

	private Queue<Order> getOppositeQueue(boolean isBid) {
		if (isBid) {
			return askQueue;
		} else {
			return bidQueue;
		}
	}

//	private NavigableSet<Order> getOppositeSet(boolean isBid) {
//		if(isBid) {
//			return askQueue;
//		}else {
//			return bidQueue;
//		}
//	}

	private Queue<Order> getSameQueue(boolean isBid) {
		if (isBid) {
			return bidQueue;
		} else {
			return askQueue;
		}
	}

	@Override
	public boolean cancelOrder(Order order) {
		return cancelOrder(order.getId(), order.getSide());
	}

	@Override
	public Collection<Order> getBidQueue() {
		return Collections.unmodifiableCollection(bidQueue);
	}

	@Override
	public Collection<Order> getAskQueue() {
		return Collections.unmodifiableCollection(askQueue);
	}

	@Override
	public EventService getEventService() {
		return this.eventService;
	}

	@Override
	@PostConstruct
	public void start() {
		super.start();
	}

	@Override
	@PreDestroy
	public void stop() {
		super.stop();
	}

	private Order cancelOrderIter(Queue<Order> queue, String orderId) {
		Iterator<Order> iter = queue.iterator();
		while (iter.hasNext()) {
			Order order = iter.next();
			if (order.getId().equals(orderId)) {
				iter.remove();
				eventService.publishEvent(new CancelEvent(order.clone(), this));
//				long i = queue.stream().filter(o -> {
//					return o.getId().equals(orderId);
//				}).count();
//				if(i > 0) {
//					System.out.println(orderId + " : " + i);
//					throw new RuntimeException("double check faile, cancel '" + orderId + "' order failed!!!");
//				}
				return order;
			}
		}
		return null;
	}

	@Override
	public long getUpdateId() {
		return IdManager.getId();
	}

}
