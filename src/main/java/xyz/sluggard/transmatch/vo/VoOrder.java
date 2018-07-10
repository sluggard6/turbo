package xyz.sluggard.transmatch.vo;

import java.math.BigInteger;

import lombok.Data;
import xyz.sluggard.transmatch.entity.AskOrder;
import xyz.sluggard.transmatch.entity.BidOrder;
import xyz.sluggard.transmatch.entity.Order;

@Data
public class VoOrder{
	
	public static final String BUY = "BUY";
	public static final String SELL = "SELL";
	public static final String LIMIT = "LIMIT";
	public static final String MARKET = "MARKET";
	
	private Long userId;
	
	private String orderId;
	
	private String side;
	
	private String lots;
	
	private BigInteger ticks;
	
	private String type;
	
	private long timestamp;	
	
	public Order buildOrder() {
		Order order = newOrder();
		order.setUserId(userId);
		order.setAmount(new BigInteger(lots));
		order.setPrice(ticks);
		order.setTimestamp(timestamp);
		return order;
	}
	
	private Order newOrder() {
		if(side.equals(BUY)) {
			return new BidOrder(orderId);
		}else if(side.equals(SELL)) {
			return new AskOrder(orderId);
		}
		throw new IllegalArgumentException("error side : " + side);
	}

}
