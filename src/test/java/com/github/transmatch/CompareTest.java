package com.github.transmatch;

import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.github.transmatch.entity.Order;
import com.github.transmatch.entity.Order.Side;
import com.github.transmatch.entity.Order.Type;
import com.github.transmatch.service.SequenceMaker;
import com.github.transmatch.service.impl.AutoIncrementSequenceMaker;

public class CompareTest {
	
	private SequenceMaker sequenceMaker;
	
	@BeforeMethod
	public void before() {
		sequenceMaker = new AutoIncrementSequenceMaker();
	}
	
	@Test
	public void bidCompareTest() {
		Order order1 = new Order(new BigDecimal("100"), new BigDecimal(100), Side.BID);
		order1.setSequence(sequenceMaker.nextSequence());
		Order order2 = new Order(new BigDecimal("90"), new BigDecimal(100), Side.BID);
		order2.setSequence(sequenceMaker.nextSequence());
		Order order3 = new Order(new BigDecimal("100"), new BigDecimal(100), Side.BID);
		order3.setSequence(sequenceMaker.nextSequence());
		assertTrue(order1.compareTo(order2)<0);
		assertTrue(order1.compareTo(order3)<0);
		assertTrue(order2.compareTo(order3)>0);
	}
	
	@Test
	public void askCompareTest() {
		Order order1 = new Order(new BigDecimal("100"), new BigDecimal(100), Side.ASK);
		order1.setSequence(sequenceMaker.nextSequence());
		Order order2 = new Order(new BigDecimal("90"), new BigDecimal(100), Side.ASK);
		order2.setSequence(sequenceMaker.nextSequence());
		Order order3 = new Order(new BigDecimal("100"), new BigDecimal(100), Side.ASK);
		order3.setSequence(sequenceMaker.nextSequence());
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
		order1.setSequence(sequenceMaker.nextSequence());
		Order order2 = new Order(BigDecimal.ZERO, new BigDecimal(100), Type.MARKET, Side.ASK);
		order2.setSequence(sequenceMaker.nextSequence());
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
