package com.github.transmatch.entity;

public class AutoIncrementSequence implements Sequence {
	
	private Long value;

	public AutoIncrementSequence(long value) {
		this.value = value;
	}

	@Override
	public int compareTo(Sequence o) {
		if(o instanceof AutoIncrementSequence) {
			return Long.valueOf(this.value - ((AutoIncrementSequence)o).value).intValue();
		}else {
			throw new IllegalArgumentException(String.format("Error Sequence Type : %s", o.getClass().getName()));
		}
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

}
