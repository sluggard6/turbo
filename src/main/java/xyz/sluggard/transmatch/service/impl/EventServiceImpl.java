package xyz.sluggard.transmatch.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import xyz.sluggard.transmatch.event.Event;
import xyz.sluggard.transmatch.listener.EngineListener;
import xyz.sluggard.transmatch.service.EventService;

public class EventServiceImpl implements EventService{
	
	@Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
	
	@Value("match.kafka.topic")
	private String topic;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	private int i = 0;

	@Override
	@Async
	public void deployEvent(Event event) {
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

	@Override
	public void addListener(EngineListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeListener(EngineListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int countLinsteners() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Set<EngineListener> getLinsteners() {
		// TODO Auto-generated method stub
		return null;
	}

}
