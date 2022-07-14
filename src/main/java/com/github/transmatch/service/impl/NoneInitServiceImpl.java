package com.github.transmatch.service.impl;

import java.util.Collections;
import java.util.List;

import com.github.transmatch.entity.Order;
import com.github.transmatch.service.InitService;

public class NoneInitServiceImpl implements InitService {

	@Override
	public List<Order> initOrder() {
		return Collections.emptyList();
	}

}
