package xyz.sluggard.transmatch.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import xyz.sluggard.transmatch.service.EventService;

@Configuration
public class EngineConfig {
	
	@Autowired
	private EventService eventService;
	


}
