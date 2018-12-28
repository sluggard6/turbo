package xyz.sluggard.transmatch.entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Order {
	
	private String id;
	
	private BigDecimal price;
	
	private BigDecimal amount;
	
	private long timestamp;
	
	private Side side;
	
	private String extend;
	
	public boolean isDone() {
		return amount.compareTo(BigDecimal.ZERO) == 0;
	}
	
	public boolean isAsk() {
		return side == Side.ASK;
	}
	
	public boolean isBid() {
		return side == Side.BID;
	}

	public void negate() {
		amount = amount.negate();
	}

	public enum Side {
		ASK,BID
	}
	
}
