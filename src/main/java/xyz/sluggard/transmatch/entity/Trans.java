package xyz.sluggard.transmatch.entity;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Getter;
import lombok.ToString;
import xyz.sluggard.transmatch.event.Event;

@Getter
@ToString
public class Trans implements Event{
	
	private final String id = UUID.randomUUID().toString();
	
	private final String buyingId;
	
	private final String sellingId;
	
	private final BigDecimal price;
	
	private final BigDecimal amount;
	
	private final long timestamp = System.currentTimeMillis();

	public Trans(String buyingId, String sellingId, BigDecimal price, BigDecimal amount) {
		super();
		this.buyingId = buyingId;
		this.sellingId = sellingId;
		this.price = price;
		this.amount = amount;
	}

}
