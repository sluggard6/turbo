package com.github.transmatch.entity;

import java.util.SortedSet;

import lombok.Data;

@Data
public class OrderBook {
	
	private SortedSet<Order> asks;
	
	private SortedSet<Order> bids;

}
