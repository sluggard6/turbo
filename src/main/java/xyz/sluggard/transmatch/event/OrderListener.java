package xyz.sluggard.transmatch.event;

public interface OrderListener {
	
	default void onOrderCreate(OrderEvent event) {
		System.out.println(event);
	}
	
	default void onOrderCancel(CancelEvent event) {
		System.out.println(event);
	}

}
