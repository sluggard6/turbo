package com.github.transmatch.entity;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Data;

@Data
public class Order implements Comparable<Order>, Cloneable{
	
	private String id;
	
	private BigDecimal price;
	
	private BigDecimal amount;
	
	private BigDecimal funds;
	
	private long nanotime;
	
	private long timestamp = System.currentTimeMillis();
	
	private Side side;
	
	/**
	 * true为ASK，false为BID
	 */
	private boolean ask;
	
	private Type type;

	private boolean stop;
	
	private Category category;
	
	private Status status;
	
	private String extend;
	
	public boolean isDone() {
		return amount.compareTo(BigDecimal.ZERO) == 0;
	}

	public boolean isBid() {
		return !ask;
	}
	
	public boolean isMarket() {
		return type.equals(Type.MARKET);
	}

	public void negate() {
		if(amount != null)amount = amount.negate();
		if(funds != null)funds = funds.negate();
	}

	public enum Side {
		ASK,BID
	}
	
	public enum Type {
		LIMIT,MARKET
	}
	
	public enum Category {
		/**
		 * ROD（Rest of Day）：是「當日有效單」，也就是你掛出委託之後，當日一直到收盤，這張單子都是有效的。通常你掛限價單，系統會自動跳成 「ROD」。
		 */
		ROD,
		/**
		 * IOC(Immediate-or-Cancel)：指「立即成交否則取消」，投資人委託單送出後，允許部份單子成交，其他沒有滿足的單子則取消。當投資人掛出市價單時，系統會自動設定為「IOC」。
		 */
		IOC,
		/**
		 * FOK(Fill-or-Kill)：指「立即全部成交否則取消」，當投資人掛單的當下，只要全部的單子成交，沒有全部成交時則全部都取消。
		 */
		FOK;
	}
	
	public enum Status {
		TAKER,MAKER;
	}

	@Override
	public int compareTo(Order o) {
		//异边订单无法比较
		if(!this.side.equals(o.side)) {
			throw new IllegalArgumentException("askorder can't compare to bidorder");
		}
		//市价单永远大于限价单
		int i = o.type.ordinal() - type.ordinal();
		if(i != 0) return i;
		if(type.equals(Type.LIMIT)) {
			//价格优先
			i = price.compareTo(o.price);
			//价格相同比较下单时间
			if(i == 0) {
				return (int) (this.nanotime - o.nanotime);
			}
		}else {
			return (int) (this.nanotime - o.nanotime);
		}
		//价格不同时，根据买卖方价格顺序取反
		if(isAsk()) {
			return i;
		}else {
			return Math.negateExact(i);
		}
	}
	
	public Order(String id, Type type, Side side) {
		this(id, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, side, type, null, null);
	}
	
	public Order(BigDecimal amount, BigDecimal funds, Type type, Side side) {
		this(null, BigDecimal.ZERO, amount, funds, side, type, null, null);
	}
	
//	public Order(BigDecimal amount, Side side) {
//		this(null, null, amount, side, Type.MARKET, null, null);
//	}
	
	public Order(BigDecimal price, BigDecimal amount, Side side) {
		this(null, price, amount, side);
	}
	
	public Order(String id,BigDecimal price, BigDecimal amount, Side side) {
		this(id, price, amount, side, Type.LIMIT, null, null);
	}
	
	public Order(String id,BigDecimal price, BigDecimal amount, Side side, Category category) {
		this(id, price, amount, side, Type.LIMIT, null, category);
	}
	
	public Order(String id,BigDecimal price, BigDecimal amount, Side side, String extend) {
		this(null, price, amount, side, Type.LIMIT, extend, null);
	}
	
	public Order(String id,BigDecimal price, BigDecimal amount, Side side, Type type, String extend, Category category) {
		this(id, price, amount, null, side, Type.LIMIT,  category, extend);
	}
	
	private Order(String id, BigDecimal price, BigDecimal amount, BigDecimal funds,
			Side side, Type type, Category category, String extend) {
		if(id == null) {
			this.id = UUID.randomUUID().toString().replaceAll("-", "");
		}else {
			this.id = id;
		}
		this.price = price;
		this.amount = amount;
		this.funds = funds;
		this.side = side;
		this.ask = side.equals(Side.ASK);
		this.type = type;
		if(category == null) {
			this.category = Category.ROD;
		}else {
			this.category = category;
		}
		this.status = Status.TAKER;
		this.extend = extend;
	}

	@Override
	public Order clone() {
		try {
			return (Order) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean isFok() {
		return category.equals(Category.FOK);
	}

	public boolean isIoc() {
		return category.equals(Category.IOC);
	}
	
	public void subtractAmount(BigDecimal amount, BigDecimal price) {
		if(isMarket() && isBid()) {
			this.funds = this.funds.subtract(amount.multiply(price));
		}else {
			this.amount = this.amount.subtract(amount);
		}
	}

	public boolean isMaker() {
		return status == Status.MAKER;
	}

}
