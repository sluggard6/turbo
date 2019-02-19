package xyz.sluggard.transmatch.service.impl;

import java.util.Collections;
import java.util.List;

import xyz.sluggard.transmatch.entity.Order;
import xyz.sluggard.transmatch.service.InitService;

public class NoneInitServiceImpl implements InitService {

	@Override
	public List<Order> initOrder() {
		return Collections.emptyList();
	}

}
