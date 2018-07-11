package xyz.sluggard.transmatch.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import xyz.sluggard.transmatch.event.EngineEvent;
import xyz.sluggard.transmatch.event.OrderEvent;
import xyz.sluggard.transmatch.event.TradeEvent;
import xyz.sluggard.transmatch.service.EventService;

@Component
@Slf4j
public class EventServiceImpl extends AbstractEventService implements EventService{
	
	@Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
	
	@Value("${engine.kafka.topic}")
	private String topic;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	

	@Override
	@Async
	public void publishEvent(EngineEvent event) {
		log.info(event.toString());
		if(event instanceof OrderEvent) {
			deployOrderEvent((OrderEvent) event);
		}else if(event instanceof TradeEvent) {
			deployTradeEvent((TradeEvent) event);
		}
	}

	@Override
	@Async
	public void deployOrderEvent(OrderEvent event) {
		try {
			kafkaTemplate.send(topic, objectMapper.writeValueAsString(event));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	@Override
	@Async
	public void deployTradeEvent(TradeEvent event) {
		try {
			kafkaTemplate.send(topic, objectMapper.writeValueAsString(event));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
	}

}
