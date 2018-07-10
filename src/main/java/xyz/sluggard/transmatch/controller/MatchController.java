package xyz.sluggard.transmatch.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viewfin.commons.protocol.HttpResult;

import lombok.AllArgsConstructor;
import lombok.Data;
import xyz.sluggard.transmatch.engine.MatchEngine;
import xyz.sluggard.transmatch.entity.AskOrder;
import xyz.sluggard.transmatch.entity.BidOrder;
import xyz.sluggard.transmatch.vo.VoOrder;

@RestController
public class MatchController {
	
	@Autowired
	private String pair;
	
	private MatchEngine matchEngine = MatchEngine.ENGIN;
	
	@RequestMapping("/createOrder/{pair}")
	public HttpResult<?> newOrder(@PathVariable String pair,@RequestBody VoOrder voOrder) {
		checkPair(pair);
		matchEngine.newOrder(voOrder.buildOrder());
		return HttpResult.SUCCESS();
	}
	
	@RequestMapping("/cancleOrder/{pair}/{orderId}")
	public HttpResult<?> canelOrder(@PathVariable String pair,@PathVariable String orderId) {
		checkPair(pair);
		if(matchEngine.cancelOrder(orderId)) {
			return HttpResult.SUCCESS();
		}else {
			return HttpResult.newResult(404, "orderId not found");
		}
	}
	
	@RequestMapping("/getDepth/{pair}")
	public HttpResult<OrderBook> getOrderBook() {
		return HttpResult.SUCCESS(new OrderBook(matchEngine.getAskQueue(), matchEngine.getBidQueue()));
	}
	
	private void checkPair(String pair) {
		if(!this.pair.equals(pair)) {
			throw new IllegalArgumentException("error pair : " + pair);
		}
	}
	
	@Data
	@AllArgsConstructor
	private class OrderBook {
		
		private Collection<AskOrder> ask;
		
		private Collection<BidOrder> bid;
		
		
	}

}
