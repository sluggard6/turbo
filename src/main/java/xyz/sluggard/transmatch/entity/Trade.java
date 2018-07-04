package xyz.sluggard.transmatch.entity;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Trade{
	
	private final String id = UUID.randomUUID().toString().replaceAll("-", "");
	
	private final String buyingId;
	
	private final String sellingId;
	
	private final BigDecimal price;
	
	private final BigDecimal amount;
	
	private final long timestamp = System.currentTimeMillis();

	public Trade(String buyingId, String sellingId, BigDecimal price, BigDecimal amount) {
		super();
		this.buyingId = buyingId;
		this.sellingId = sellingId;
		this.price = price;
		this.amount = amount;
	}

}
