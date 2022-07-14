package com.github.transmatch.entity;

public class AutoIncrementSequence implements Sequence<AutoIncrementSequence> {
	
	private Long value;

	public AutoIncrementSequence(long value) {
		this.value = value;
	}

	@Override
	public int compareTo(AutoIncrementSequence o) {
		return Long.valueOf(this.value - o.value).intValue();
	}

}
