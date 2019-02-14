package xyz.sluggard.transmatch.core;

import xyz.sluggard.transmatch.entity.Order;

public interface PreMatchable<T extends Order> extends Engine<T> {
	
	String preMatch(T order);
}
