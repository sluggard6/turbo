package xyz.sluggard.transmatch.service.impl;

import java.util.Collections;
import java.util.List;

import xyz.sluggard.transmatch.entity.Order;
import xyz.sluggard.transmatch.service.InitService;

public class NoneInitServiceImpl<O extends Order> implements InitService<O> {

	@Override
	public List<O> initOrder() {
		return Collections.emptyList();
	}

}
