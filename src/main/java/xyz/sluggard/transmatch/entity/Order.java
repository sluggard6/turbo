package xyz.sluggard.transmatch.entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public abstract class Order {
	
	private String id;
	
	private Long userId;
	
	private BigDecimal price;
	
	private BigDecimal amount;
	
	private long timestamp;
	
	public boolean isDone() {
		return amount.compareTo(BigDecimal.ZERO) == 0;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [id=" + id + ", price=" + price + ", amount=" + amount + ", timestamp=" + timestamp + "]";
	}

}
