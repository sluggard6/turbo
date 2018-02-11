package xyz.sluggard.transmatch.entity;

public class Buying extends Trade implements Comparable<Buying> {

	@Override
	public int compareTo(Buying o) {
		int r = o.getPrice().compareTo(this.getPrice());
		if(r == 0) {
			return (int)(o.getTimestamp() - this.getTimestamp());
		}else {
			return r;
		}
	}

}
