package xyz.sluggard.transmatch.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import xyz.sluggard.transmatch.event.Event;
import xyz.sluggard.transmatch.service.EventService;

@Component
public class EventServiceImpl extends AbstractEventService implements EventService{
	
	@Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
	
	@Value("match.kafka.topic")
	private String topic;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	private int i = 0;

	@Override
	@Async
	public void publishEvent(Event event) {
		System.out.print(i);
		System.out.print(" : ");
		System.out.println(event);
		i++;
	}

	@Override
	@Async
	public void deployOrderEvent(Event event) {
		try {
			kafkaTemplate.send(topic, objectMapper.writeValueAsString(event));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	@Override
	@Async
	public void deployTradeEvent(Event event) {
		try {
			kafkaTemplate.send(topic, objectMapper.writeValueAsString(event));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
	}

}