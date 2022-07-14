package com.github.transmatch.utils;

import java.util.concurrent.atomic.AtomicLong;

public abstract class IdManager {
	
	private static AtomicLong id = new AtomicLong();
	
	public static long getId() {
		return id.get();
	}
	
	public static long makeId() {
		return id.addAndGet(1);
	}

}
