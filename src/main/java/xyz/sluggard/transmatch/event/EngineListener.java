package xyz.sluggard.transmatch.event;

public interface EngineListener extends Comparable<EngineListener>{
	
	void onEvent(EngineEvent event);
	
	@Override
	default int compareTo(EngineListener o) {
		return this.hashCode() - o.hashCode();
	}
	
}
