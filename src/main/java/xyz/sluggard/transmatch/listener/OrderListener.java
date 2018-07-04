package xyz.sluggard.transmatch.listener;

import xyz.sluggard.transmatch.event.OrderEvent;

public interface OrderListener extends EngineListener<OrderEvent> {
	
	void orderCreated(OrderEvent orderEvent);

	void orderCanceled(OrderEvent orderEvent);
	
}
