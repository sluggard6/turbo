package xyz.sluggard.transmatch.controller;

import java.util.Collection;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.viewfin.commons.protocol.HttpResult;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import xyz.sluggard.transmatch.engine.MatchEngine;
import xyz.sluggard.transmatch.entity.Order;
import xyz.sluggard.transmatch.vo.Action;
import xyz.sluggard.transmatch.vo.VoOrder;

@Slf4j
@RestController
public class MatchController {
	
	private static final long WAIT_TIME = 5000;
	
	@Value("${engine.pair}")
	private String pair;
	
	private MatchEngine matchEngine = MatchEngine.ENGIN;
	
	@RequestMapping(value="/createOrder/{pair}",method=RequestMethod.POST)
	@ApiOperation("新建订单")
	public HttpResult<?> newOrder(@PathVariable String pair,@RequestBody VoOrder voOrder) {
		checkPair(pair);
//		matchEngine.newOrder(voOrder.buildOrder());
		try {
			matchEngine.newAction(new Action(voOrder.buildOrder()));
			return HttpResult.SUCCESS();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return HttpResult.newResult(550, "队列已满");
		}
	}
	
	@RequestMapping(value="/cancleOrder/{pair}/{orderId}",method=RequestMethod.POST)
	@ApiOperation("撤销订单")
	public HttpResult<?> canelOrder(@PathVariable String pair,@PathVariable String orderId) {
		checkPair(pair);
		try {
			matchEngine.newAction(new Action(orderId));
			long startTime = System.currentTimeMillis();
			while(true) {
				Integer state = matchEngine.getCancelState(orderId);
				if(state != null) {
					return HttpResult.newResult(state);
				}else {
					if(System.currentTimeMillis() - startTime > WAIT_TIME) {
						return HttpResult.newResult(408, "Time Out");
					}else {
						Thread.currentThread().wait();
					}
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			return HttpResult.newResult(550, "队列已满");
		}
	}
	
	@RequestMapping(value="/getDepth/{pair}",method=RequestMethod.GET)
	@ApiOperation("获取摆单列表")
	public HttpResult<OrderBook> getOrderBook() {
		return HttpResult.SUCCESS(new OrderBook(matchEngine.getAskQueue(), matchEngine.getBidQueue()));
	}
	
	private void checkPair(String pair) {
		if(!this.pair.equals(pair)) {
			throw new IllegalArgumentException("error pair : " + pair);
		}
	}
	
	@KafkaListener(topics="${engine.kafka.topic}", id="self")
	public void listener(ConsumerRecord<String, String> consumer) throws Exception {
//		log.info("consumer:"+consumer.value());
		
	}
	
	@Data
	@AllArgsConstructor
	private class OrderBook {
		
		private Collection<Order> ask;
		
		private Collection<Order> bid;
		
		
	}

}
