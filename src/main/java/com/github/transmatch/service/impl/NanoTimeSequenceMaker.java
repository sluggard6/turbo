package com.github.transmatch.service.impl;

import com.github.transmatch.entity.NanoTimeSequence;
import com.github.transmatch.service.SequenceMaker;

public class NanoTimeSequenceMaker implements SequenceMaker {

	@Override
	public NanoTimeSequence nextSequence() {
		return new NanoTimeSequence(System.nanoTime());
	}


}
