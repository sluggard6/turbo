package com.github.transmatch.entity;

public class NanoTimeSequence implements Sequence {
	
	private long nanotime;

	public NanoTimeSequence(long nanotime) {
		super();
		this.nanotime = nanotime;
	}

	@Override
	public int compareTo(Sequence o) {
		if(o instanceof NanoTimeSequence) {
			return Long.valueOf(this.nanotime - ((NanoTimeSequence)o).nanotime).intValue();
		}else {
			throw new IllegalArgumentException(String.format("Error Sequence Type : %s", o.getClass().getName()));
		}
	}

}
