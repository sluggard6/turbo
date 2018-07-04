package xyz.sluggard.transmatch.entity;

public class BidOrder extends Order implements Comparable<BidOrder> {

	@Override
	public int compareTo(BidOrder o) {
		int r = o.getPrice().compareTo(this.getPrice());
		if(r == 0) {
			return (int)(o.getTimestamp() - this.getTimestamp());
		}else {
			return r;
		}
	}

}
