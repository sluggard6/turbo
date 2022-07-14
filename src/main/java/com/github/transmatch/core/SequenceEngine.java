package com.github.transmatch.core;

public interface SequenceEngine extends Engine {
	
	<T extends Sequence<T>> Sequence<T> nextSequence();

}
