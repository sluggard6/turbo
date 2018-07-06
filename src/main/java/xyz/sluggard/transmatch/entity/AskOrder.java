package xyz.sluggard.transmatch.entity;

public class AskOrder extends Order implements Comparable<AskOrder>{

	public AskOrder(String orderId) {
		this.setId(orderId);
	}

	@Override
	public int compareTo(AskOrder o) {
		int r = this.getPrice().compareTo(o.getPrice());
		if(r == 0) {
			return (int)(o.getTimestamp() - this.getTimestamp());
		}else {
			return r;
		}
	}

	@Override
	public String getType() {
		return "ask";
	}

}
