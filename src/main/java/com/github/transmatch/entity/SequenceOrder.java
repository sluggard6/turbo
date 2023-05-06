package com.github.transmatch.entity;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SequenceOrder extends Order {
	
	private Sequence sequence;

	public SequenceOrder(BigDecimal price, BigDecimal amount, Side side) {
		super(price, amount, side);
	}

	public SequenceOrder(BigDecimal amount, BigDecimal funds, Type type, Side side) {
		super(amount, funds, type, side);
	}

	public SequenceOrder(String id, BigDecimal price, BigDecimal amount, Side side, Category category) {
		super(id, price, amount, side, category);
	}

	public SequenceOrder(String id, BigDecimal price, BigDecimal amount, Side side, String extend) {
		super(id, price, amount, side, extend);
	}

	public SequenceOrder(String id, BigDecimal price, BigDecimal amount, Side side, Type type, String extend,
			Category category) {
		super(id, price, amount, side, type, extend, category);
	}

	public SequenceOrder(String id, BigDecimal price, BigDecimal amount, Side side) {
		super(id, price, amount, side);
	}

	public SequenceOrder(String id, Type type, Side side) {
		super(id, type, side);
	}

	@Override
	public int compareTo(Order o) {
		SequenceOrder other = (SequenceOrder) o;
		//异边订单无法比较
		if(!this.getSide().equals(other.getSide())) {
			throw new IllegalArgumentException("askorder can't compare to bidorder");
		}
		//市价单永远大于限价单
		int i = other.getType().ordinal() - this.getType().ordinal();
		if(i != 0) return i;
		if(this.getType().equals(Type.LIMIT)) {
			//价格优先
			i = this.getPrice().compareTo(other.getPrice());
			//价格相同比较序列号
			if(i == 0) {
				return sequence.compareTo(other.sequence);
			}
		}else {
			return sequence.compareTo(other.sequence);
		}
		//价格不同时，根据买卖方价格顺序取反
		if(isAsk()) {
			return i;
		}else {
			return Math.negateExact(i);
		}
	}

}
