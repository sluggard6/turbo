package xyz.sluggard.transmatch.service;

import java.util.List;

import xyz.sluggard.transmatch.entity.Order;

public interface InitService<O extends Order> {
	
	List<O> initOrder();

}
