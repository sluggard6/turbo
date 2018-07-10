package xyz.sluggard.transmatch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import xyz.sluggard.transmatch.entity.Order;

@RestController
public class MatchController {
	
	@Autowired
	private String pair;
	
	@RequestMapping("/createOrder/{pair}")
	public String newOrder(@PathVariable String pair,@RequestBody Order order) {
		checkPair(pair);
		
		return null;
	}
	
	@RequestMapping("/cancleOrder/{pair}/{orderId}")
	public String canelOrder(@PathVariable String pair,@PathVariable String orderId) {
		
		return null;
	}
	
	@RequestMapping("/getDepth/{pair}")
	public String getOrderBook() {
		return null;
	}
	
	private void checkPair(String pair) {
		if(!this.pair.equals(pair)) {
			throw new IllegalArgumentException("error pair : " + pair);
		}
	}

}
