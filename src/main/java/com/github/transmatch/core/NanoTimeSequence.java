package com.github.transmatch.core;

public class NanoTimeSequence implements Sequence<NanoTimeSequence> {
	
	private long nanotime;

	public NanoTimeSequence(long nanotime) {
		super();
		this.nanotime = nanotime;
	}

	@Override
	public int compareTo(NanoTimeSequence o) {
		return Long.valueOf(this.nanotime - o.nanotime).intValue();
	}

}
