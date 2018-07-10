package xyz.sluggard.transmatch.entity;

import java.math.BigInteger;
import java.util.UUID;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Trade{
	
	private final String id = UUID.randomUUID().toString().replaceAll("-", "");
	
	private final String buyingId;
	
	private final String sellingId;
	
	private final BigInteger price;
	
	private final BigInteger amount;
	
	private final long timestamp = System.currentTimeMillis();

	public Trade(String buyingId, String sellingId, BigInteger price, BigInteger amount) {
		super();
		this.buyingId = buyingId;
		this.sellingId = sellingId;
		this.price = price;
		this.amount = amount;
	}

}
