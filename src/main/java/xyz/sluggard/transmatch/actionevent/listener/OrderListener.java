package xyz.sluggard.transmatch.actionevent.listener;

import xyz.sluggard.transmatch.actionevent.event.OrderEvent;

public interface OrderListener extends EngineListener<OrderEvent> {
	
	void orderCreated(OrderEvent orderEvent);

	void orderCanceled(OrderEvent orderEvent);
	
}
