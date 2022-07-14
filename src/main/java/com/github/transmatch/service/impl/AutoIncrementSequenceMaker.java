package com.github.transmatch.service.impl;

import java.util.concurrent.atomic.AtomicLong;

import com.github.transmatch.entity.AutoIncrementSequence;
import com.github.transmatch.service.SequenceMaker;

public class AutoIncrementSequenceMaker implements SequenceMaker<AutoIncrementSequence> {
	
	private final AtomicLong value = new AtomicLong(0);

	@Override
	public AutoIncrementSequence nextSequence() {
		return new AutoIncrementSequence(value.addAndGet(1l));
	}

}
