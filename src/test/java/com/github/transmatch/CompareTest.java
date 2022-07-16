package com.github.transmatch;

import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;

import org.testng.annotations.Test;

import com.github.transmatch.entity.Order;
import com.github.transmatch.entity.Order.Side;
import com.github.transmatch.entity.Order.Type;

public class CompareTest {
	
	@Test
	public void bidCompareTest() {
		Order order1 = new Order(new BigDecimal("100"), new BigDecimal(100), Side.BID);
		Order order2 = new Order(new BigDecimal("90"), new BigDecimal(100), Side.BID);
		Order order3 = new Order(new BigDecimal("100"), new BigDecimal(100), Side.BID);
		assertTrue(order1.compareTo(order2)<0);
		assertTrue(order1.compareTo(order3)<0);
		assertTrue(order2.compareTo(order3)>0);
	}
	
	@Test
	public void askCompareTest() {
		Order order1 = new Order(new BigDecimal("100"), new BigDecimal(100), Side.ASK);
		Order order2 = new Order(new BigDecimal("90"), new BigDecimal(100), Side.ASK);
		Order order3 = new Order(new BigDecimal("100"), new BigDecimal(100), Side.ASK);
		assertTrue(order1.compareTo(order2)>0);
		assertTrue(order1.compareTo(order3)<0);
		assertTrue(order2.compareTo(order3)<0);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void exceptionTest() {
		Order order1 = new Order(new BigDecimal("100"), new BigDecimal(100), Side.ASK);
		Order order2 = new Order(new BigDecimal("90"), new BigDecimal(100), Side.BID);
		order1.compareTo(order2);
	}
	
	@Test
	public void marketCompile() {
		Order order1 = new Order(BigDecimal.ZERO, new BigDecimal(100), Type.MARKET, Side.ASK);
		Order order2 = new Order(BigDecimal.ZERO, new BigDecimal(100), Type.MARKET, Side.ASK);
//		Order order1 = new Order(new BigDecimal(100), Side.ASK);
//		Order order2 = new Order(new BigDecimal(100), Side.ASK);
		assertTrue(order1.compareTo(order2)<0);
	}
	
	
	@Test
	public void marketLimitCompile() {
		Order order1 = new Order(new BigDecimal(100), new BigDecimal(100), Side.ASK);
		Order order2 = new Order(BigDecimal.ZERO, new BigDecimal(100), Type.MARKET, Side.ASK);
//		Order order2 = new Order(new BigDecimal(100), Side.ASK);
		assertTrue(order1.compareTo(order2)>0);
	}
}
