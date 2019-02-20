package xyz.sluggard.transmatch.core;

import xyz.sluggard.transmatch.entity.Order;

public interface PreMatchable extends Engine {
	
	String preMatch(Order order);
}
