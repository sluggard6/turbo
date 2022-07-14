package com.github.transmatch.engine;

import java.util.concurrent.Flow.Publisher;

import com.github.transmatch.core.SequenceEngine;
import com.github.transmatch.entity.Sequence;
import com.github.transmatch.event.EngineEvent;
import com.github.transmatch.service.SequenceMaker;

public abstract class AbstractFlowEngine<S extends Sequence<S>> implements SequenceEngine<S>{
	
	protected SequenceMaker<S> sequenceMaker;

	protected Publisher<EngineEvent> publisher;
	
	
}
