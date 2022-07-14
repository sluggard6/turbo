package com.github.transmatch.core;

import com.github.transmatch.entity.Sequence;

public interface SequenceEngine<S extends Sequence<S>> extends Engine {
	
	S nextSequence();
}
