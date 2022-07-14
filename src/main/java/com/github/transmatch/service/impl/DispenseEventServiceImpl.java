package com.github.transmatch.service.impl;

import com.github.transmatch.event.CancelEvent;
import com.github.transmatch.event.EngineContextListener;
import com.github.transmatch.event.EngineDestoryEvent;
import com.github.transmatch.event.EngineEvent;
import com.github.transmatch.event.EngineListener;
import com.github.transmatch.event.EngineStartUpEvent;
import com.github.transmatch.event.MakerEvent;
import com.github.transmatch.event.OrderEvent;
import com.github.transmatch.event.OrderListener;
import com.github.transmatch.event.TradeEvent;
import com.github.transmatch.event.TradeListener;
import com.github.transmatch.service.DispenseEventService;

import lombok.Data;

public class DispenseEventServiceImpl extends EventServiceImpl implements DispenseEventService {
	
	public DispenseEventServiceImpl() {
		dispenseListener = new DispenseListener();
		addListener(dispenseListener);
	}
	
	private DispenseListener dispenseListener;
	
	@Override
	public void setContextListener(EngineContextListener contextListener) {
		dispenseListener.setEngineContextListener(contextListener);
	}

	public void setOrderListener(OrderListener orderListener) {
		dispenseListener.setOrderListener(orderListener);
	}

	public void setTradeListener(TradeListener tradeListener) {
		dispenseListener.setTradeListener(tradeListener);
	}

}

@Data
class DispenseListener implements EngineListener {
	
	private EngineContextListener engineContextListener;
	
	private OrderListener orderListener;
	
	private TradeListener tradeListener;
	
	@Override
	public void onEvent(EngineEvent event) {
		switch (event.getClass().getSimpleName()) {
		case "OrderEvent":
			orderListener.onOrderCreate((OrderEvent) event);
			break;
		case "CancelEvent":
			orderListener.onOrderCancel((CancelEvent) event);
			break;
		case "EngineStartUpEvent":
			engineContextListener.onEngineStartUp((EngineStartUpEvent) event);
			break;
		case "EngineDestoryEvent":
			engineContextListener.onEngineDestory((EngineDestoryEvent) event);
			break;
		case "MakerEvent":
			tradeListener.onOrderMaker((MakerEvent) event);
			break;
		case "TradeEvent":
			tradeListener.onOrderTrade((TradeEvent) event);
			break;
		default:
			throw new IllegalArgumentException("unknow event : " + event.getClass().getSimpleName());
		}
	}

	public DispenseListener() {
		this(null, null, null);
	}

	public DispenseListener(EngineContextListener engineContextListener, OrderListener orderListener,
			TradeListener tradeListener) {
		super();
		if(engineContextListener == null) {
			this.engineContextListener = new EngineContextListener() {
			};
		}else {
			this.engineContextListener = engineContextListener;
		}
		if(engineContextListener == null) {
			this.orderListener = new OrderListener() {
			};
		}else {
			this.orderListener = orderListener;
		}
		if(engineContextListener == null) {
			this.tradeListener = new TradeListener() {
			};
		}else {
			this.tradeListener = tradeListener;
		}
		
		
		
	}

}