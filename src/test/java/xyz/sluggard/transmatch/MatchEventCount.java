package xyz.sluggard.transmatch;

import lombok.Data;

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

}
