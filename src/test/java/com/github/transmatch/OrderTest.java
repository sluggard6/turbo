package com.github.transmatch;

import static org.testng.Assert.assertEquals;

import java.math.BigDecimal;

import org.testng.annotations.Test;

import com.github.transmatch.entity.Order;
import com.github.transmatch.entity.Order.Side;
import com.github.transmatch.entity.Order.Type;

public class OrderTest {
	
	@Test
	public void doneTest() {
		Order order = new Order("1", Type.LIMIT, Side.BID);
		order.setPrice(new BigDecimal("0.29200000000000000000"));
		order.setAmount(new BigDecimal("0E-20"));
		order.setFunds(new BigDecimal("0"));
		assertEquals(true, order.isDone());
	}

}
