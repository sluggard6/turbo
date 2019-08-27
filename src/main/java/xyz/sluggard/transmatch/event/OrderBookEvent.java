package xyz.sluggard.transmatch.event;

import lombok.Getter;
import lombok.ToString;
import xyz.sluggard.transmatch.core.Engine;
import xyz.sluggard.transmatch.entity.OrderBook;

@Getter
@ToString
public class OrderBookEvent extends EngineEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private OrderBook orderBook;
	
	public OrderBookEvent(OrderBook orderBook, Engine engine) {
		super(engine);
		this.orderBook = orderBook;
	}

}
