package xyz.sluggard.transmatch.service.impl;

import lombok.Data;
import xyz.sluggard.transmatch.event.CancelEvent;
import xyz.sluggard.transmatch.event.EngineContextListener;
import xyz.sluggard.transmatch.event.EngineDestoryEvent;
import xyz.sluggard.transmatch.event.EngineEvent;
import xyz.sluggard.transmatch.event.EngineListener;
import xyz.sluggard.transmatch.event.EngineStartUpEvent;
import xyz.sluggard.transmatch.event.MakerEvent;
import xyz.sluggard.transmatch.event.OrderEvent;
import xyz.sluggard.transmatch.event.OrderListener;
import xyz.sluggard.transmatch.event.TradeEvent;
import xyz.sluggard.transmatch.event.TradeListener;
import xyz.sluggard.transmatch.service.DispenseEventService;

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