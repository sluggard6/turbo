package com.github.transmatch.core;

import com.github.transmatch.entity.Order;

public interface PreMatchable extends Engine {
	
	String preMatch(Order order);
}
