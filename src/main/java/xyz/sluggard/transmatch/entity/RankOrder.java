package xyz.sluggard.transmatch.entity;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xyz.sluggard.transmatch.core.Rankable;

@Data
@EqualsAndHashCode(callSuper=false)
public class RankOrder extends Order implements Rankable{
	
	/**
	 * 用户身份，true表示maker，false表示taker
	 */
	private boolean maker;

	@Override
	public int getRank() {
		return 0;
	}

	public int compareTo(RankOrder o) {
		int ret = getRank() - o.getRank();
		if(ret != 0) return ret;
		return super.compareTo(o);
	}

	public RankOrder(BigDecimal amount, Side side, boolean maker) {
		super(amount, side);
		this.maker = maker;
	}
	
}
