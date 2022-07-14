package com.github.transmatch.service.impl;

import com.github.transmatch.core.NanoTimeSequence;
import com.github.transmatch.service.SequenceMaker;

public class NanoTimeSequenceMaker implements SequenceMaker<NanoTimeSequence> {

	@Override
	public NanoTimeSequence nextSequence() {
		return new NanoTimeSequence(System.nanoTime());
	}


}
