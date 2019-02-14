package xyz.sluggard.transmatch.event;

public interface EngineContextListener {
	
	default void onEngineStartUp(EngineStartUpEvent event) {
		System.out.println(event);
	}
	
	default void onEngineDestory(EngineDestoryEvent event) {
		System.out.println(event);
	}

}
