package com.github.transmatch.core;

import com.github.transmatch.entity.Sequence;

public interface SequenceEngine extends Engine {
	
	Sequence nextSequence();
}
