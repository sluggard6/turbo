package com.github.transmatch.engine;

import com.github.transmatch.core.SequenceEngine;
import com.github.transmatch.entity.Sequence;
import com.github.transmatch.entity.SequenceOrder;
import com.github.transmatch.service.EventService;
import com.github.transmatch.service.InitService;
import com.github.transmatch.service.SequenceMaker;
import com.github.transmatch.service.impl.AutoIncrementSequenceMaker;
import com.github.transmatch.service.impl.EventServiceImpl;
import com.github.transmatch.service.impl.NoneInitServiceImpl;

public abstract class AbstractSequenceEngine extends AbstractEngine implements SequenceEngine {
	
	private SequenceMaker sequenceMaker;

	public AbstractSequenceEngine(String currencyPair, EventService eventService, InitService initService, SequenceMaker sequenceMaker) {
		super(currencyPair, eventService, initService);
		this.sequenceMaker = sequenceMaker;
	}

	public AbstractSequenceEngine(String currencyPair, EventService eventService, InitService initService) {
		this(currencyPair, eventService, initService, new AutoIncrementSequenceMaker());
	}

	public AbstractSequenceEngine(String currencyPair, EventService eventService) {
		this(currencyPair, eventService, new NoneInitServiceImpl());
	}

	public AbstractSequenceEngine(String currencyPair) {
		this(currencyPair, new EventServiceImpl());
	}

	public AbstractSequenceEngine(String currencyPair, SequenceMaker sequenceMaker) {
		super(currencyPair);
		this.sequenceMaker = sequenceMaker;
	}

	@Override
	public Sequence nextSequence() {
		return sequenceMaker.nextSequence();
	}
	
	protected boolean newOrder(SequenceOrder order) {
		order.setSequence(nextSequence());
		return false;
	}

}
