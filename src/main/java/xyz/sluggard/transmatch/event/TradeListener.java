package xyz.sluggard.transmatch.event;

public interface TradeListener {
	
	default void onOrderMaker(MakerEvent event) {
		System.out.println(event);
	}
	
	default void onOrderTrade(TradeEvent event) {
		System.out.println(event);
	}

}
