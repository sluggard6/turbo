package xyz.sluggard.transmatch.actionevent.publisher;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.Executor;

import lombok.Getter;
import xyz.sluggard.transmatch.actionevent.event.EngineEvent;
import xyz.sluggard.transmatch.actionevent.event.OrderEvent;
import xyz.sluggard.transmatch.actionevent.event.TradeEvent;
import xyz.sluggard.transmatch.actionevent.listener.EngineListener;
import xyz.sluggard.transmatch.actionevent.listener.OrderListener;
import xyz.sluggard.transmatch.actionevent.listener.TradeListener;

public abstract class AbstractEngineEventPublisher implements EngineEventPublisher {
	
	@Getter
	private Executor executor;
	
	private Map<Class<? extends EngineEvent>, Collection<EngineListener<?>>> listenerMap;
	
	private Collection<EngineListener<?>> getListeners(Class<? extends EngineEvent> clazz) {
		synchronized (listenerMap) {
			if(listenerMap == null) {
				listenerMap = new HashMap<>();
			}
			Collection<EngineListener<?>> listeners = listenerMap.get(clazz);
			if(listeners == null) {
				listeners = new LinkedList<>();
			}
			return listeners;
		}
		
	}

	
	@Override
	public void publishEvent(EngineEvent event) {
		Collection<EngineListener<?>> listeners= listenerMap.get(event.getClass());
		for(EngineListener<?> listener : listeners) {
			Executor executor = getExecutor();
			if (executor != null) {
				executor.execute(() -> invokeListener(listener, event));
			}
			else {
				invokeListener(listener, event);
			}
		}
		
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private void invokeListener(EngineListener listener, EngineEvent event) {
		listener.onApplicationEvent(event);

	}

	@Override
	public void addEngineListener(EngineListener<?> listener) {
		if(listener instanceof OrderListener) {
			getListeners(OrderEvent.class).add(listener);
		}
		if(listener instanceof TradeListener) {
			getListeners(TradeEvent.class).add(listener);
		}
	}

	@Override
	public void removeEngineListener(EngineListener<?> listener) {
		
	}

	@Override
	public void removeAllListeners() {
		
	}
	


	

}
