package xyz.sluggard.transmatch.entity;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class Trade{
	
	private final String id = UUID.randomUUID().toString().replaceAll("-", "");
	
	private final String bidOrderId;
	
	private final String askOrderId;
	
	private final BigDecimal price;
	
	private final BigDecimal amount;
	
	private final String makerOrderId;
	
	private final String takerOrderId;
	
	private final long timestamp = System.currentTimeMillis();

}
