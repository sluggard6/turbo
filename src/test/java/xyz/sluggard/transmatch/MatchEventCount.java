package xyz.sluggard.transmatch;

import lombok.Data;
import xyz.sluggard.transmatch.event.CancelEvent;
import xyz.sluggard.transmatch.event.EngineDestoryEvent;
import xyz.sluggard.transmatch.event.EngineEvent;
import xyz.sluggard.transmatch.event.EngineStartUpEvent;
import xyz.sluggard.transmatch.event.MakerEvent;
import xyz.sluggard.transmatch.event.OrderEvent;
import xyz.sluggard.transmatch.event.TradeEvent;

@Data
public class MatchEventCount {
	
	private int startUpEventCount;
	
	private int destoryEventCount;
	
	private int cancelEventCount;
	
	private int orderEventCount;
	
	private int tradeEventCount;
	
	private int makerEventCount;
	
	public void countStartUpEvent(){
		startUpEventCount++;
	}
	
	public void countDestoryEvent(){
		destoryEventCount++;
	}
	
	public void countCancelEvent(){
		cancelEventCount++;
	}
	
	public void countOrderEvent(){
		orderEventCount++;
	}
	
	public void countTradeEvent(){
		tradeEventCount++;
	}
	
	public void countMakerEvent(){
		makerEventCount++;
	}

	public void count(EngineEvent event) {
		if(event instanceof EngineStartUpEvent) {
			countStartUpEvent();
		}else if(event instanceof EngineDestoryEvent) {
			countDestoryEvent();
		}else if(event instanceof CancelEvent) {
			countCancelEvent();
		}else if(event instanceof OrderEvent) {
			countOrderEvent();
		}else if(event instanceof TradeEvent) {
			countTradeEvent();
		}else if(event instanceof MakerEvent) {
			countMakerEvent();
		}else {
			throw new RuntimeException("unknow EngineEvet class : " + event.getClass().getName());
		}
	}

}
