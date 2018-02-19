package xyz.sluggard.transmatch.service.impl;

import xyz.sluggard.transmatch.event.Event;
import xyz.sluggard.transmatch.service.EventService;

public class EventServiceImpl implements EventService{
	
	private int i = 0;

	@Override
	public void deployEvent(Event event) {
		System.out.print(i);
		System.out.print(" : ");
		System.out.println(event);
		i++;
	}

}
