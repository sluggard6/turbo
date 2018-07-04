package xyz.sluggard.transmatch.entity;

public class AskOrder extends Order implements Comparable<AskOrder>{

	@Override
	public int compareTo(AskOrder o) {
		int r = this.getPrice().compareTo(o.getPrice());
		if(r == 0) {
			return (int)(o.getTimestamp() - this.getTimestamp());
		}else {
			return r;
		}
	}

}
