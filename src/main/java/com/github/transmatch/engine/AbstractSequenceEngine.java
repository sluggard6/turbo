package com.github.transmatch.engine;

import com.github.transmatch.core.Sequence;
import com.github.transmatch.core.SequenceEngine;
import com.github.transmatch.service.EventService;
import com.github.transmatch.service.InitService;
import com.github.transmatch.service.SequenceMaker;

public abstract class AbstractSequenceEngine extends AbstractEngine implements SequenceEngine {
	
	private SequenceMaker sequenceMaker;

	public AbstractSequenceEngine(String currencyPair, EventService eventService, InitService initService, SequenceMaker sequenceMaker) {
		super(currencyPair, eventService, initService);
		this.sequenceMaker = sequenceMaker;
	}

	public AbstractSequenceEngine(String currencyPair, EventService eventService, InitService initService) {
		super(currencyPair, eventService, initService);
	}

	public AbstractSequenceEngine(String currencyPair, EventService eventService) {
		super(currencyPair, eventService);
	}

	public AbstractSequenceEngine(String currencyPair) {
		super(currencyPair);
	}

	@Override
	public Sequence nextSequence() {
		return sequenceMaker.nextSequence();
	}

}
