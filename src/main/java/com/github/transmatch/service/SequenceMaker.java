package com.github.transmatch.service;

import com.github.transmatch.core.Sequence;

public interface SequenceMaker<T extends Sequence<T>> {
	
	T nextSequence();

}
