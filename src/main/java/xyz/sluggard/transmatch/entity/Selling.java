package xyz.sluggard.transmatch.entity;

public class Selling extends Trade implements Comparable<Selling>{

	@Override
	public int compareTo(Selling o) {
		int r = this.getPrice().compareTo(o.getPrice());
		if(r == 0) {
			return (int)(o.getTimestamp() - this.getTimestamp());
		}else {
			return r;
		}
	}

}
