package xyz.sluggard.transmatch.entity;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Order implements Comparable<Order>{
	
	private String id;
	
	private BigDecimal price;
	
	private BigDecimal amount;
	
	private long nanotime = System.nanoTime();
	
	private long timestamp = System.currentTimeMillis();
	
	private Side side;
	
	private transient boolean booleanSide;
	
	private Type type;
	
	private String extend;
	
	public boolean isDone() {
		return amount.compareTo(BigDecimal.ZERO) == 0;
	}
	
	public boolean isAsk() {
		return booleanSide;
	}
	
	public boolean isBid() {
		return !booleanSide;
	}

	public void negate() {
		amount = amount.negate();
	}

	public enum Side {
		ASK,BID
	}
	
	public enum Type {
		LIMIT,MARKET
	}

	@Override
	public int compareTo(Order o) {
		if(!side.equals(o.getSide())) {
			throw new IllegalArgumentException("askorder can't compare to bidorder");
		}
		int i = o.type.ordinal() - type.ordinal();
		if(i != 0) return i;
		if(type.equals(Type.LIMIT)) {
			i = price.compareTo(o.getPrice());
			if(i == 0) {
				return (int) (nanotime - o.getNanotime());
			}
		}else {
			return (int) (nanotime - o.getNanotime());
		}
		if(isAsk()) {
			return i;
		}else {
			return i*(-1);
		}
	}
	
	public Order(BigDecimal amount, Side side) {
		this(null, null, amount, side, Type.MARKET, null);
	}
	
	public Order(BigDecimal price, BigDecimal amount, Side side) {
		this(null, price, amount, side, null);
	}
	
	public Order(String id,BigDecimal price, BigDecimal amount, Side side) {
		this(id, price, amount, side, Type.LIMIT, null);
	}
	
	public Order(String id,BigDecimal price, BigDecimal amount, Side side, String extend) {
		this(null, price, amount, side, Type.LIMIT, extend);
	}
	
	public Order(String id,BigDecimal price, BigDecimal amount, Side side, Type type, String extend) {
		if(id == null) {
			id = UUID.randomUUID().toString().replaceAll("-", "");
		}else {
			this.id = id;
		}
		this.price = price;
		this.amount = amount;
		this.side = side;
		this.type = type;
		this.extend = extend;
		this.booleanSide = side.equals(Side.ASK);
	}
}
