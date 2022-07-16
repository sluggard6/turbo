package com.github.transmatch.utils;

import java.math.BigDecimal;

import com.github.transmatch.entity.Order;

public abstract class OrderValidater {
	
	public static boolean validateOrder(Order order) {
		if(order.isMarket()) {
			return order.isBid()?order.getFunds().compareTo(BigDecimal.ZERO)>0:order.getAmount().compareTo(BigDecimal.ZERO)>0;
		}else {
			return order.getPrice().compareTo(BigDecimal.ZERO)>0&&order.getAmount().compareTo(BigDecimal.ZERO)>0;
		}
	}

}
