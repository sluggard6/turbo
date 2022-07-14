package com.github.transmatch.engine;

import com.github.transmatch.core.SequenceEngine;
import com.github.transmatch.entity.Sequence;
import com.github.transmatch.service.EventService;
import com.github.transmatch.service.InitService;
import com.github.transmatch.service.SequenceMaker;

public abstract class AbstractSequenceEngine<S extends Sequence<S>> extends AbstractEngine implements SequenceEngine<S> {
	
	private SequenceMaker<S> sequenceMaker;

	public AbstractSequenceEngine(String currencyPair, EventService eventService, InitService initService, SequenceMaker<S> sequenceMaker) {
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

	public AbstractSequenceEngine(String currencyPair, SequenceMaker<S> sequenceMaker) {
		super(currencyPair);
		this.sequenceMaker = sequenceMaker;
	}

	@Override
	public S nextSequence() {
		return sequenceMaker.nextSequence();
	}

}
