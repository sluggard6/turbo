package xyz.sluggard.transmatch;

import static org.testng.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.testng.annotations.Test;

public class BigDecimalTest {
	
//	@Test
//	public void intValueTest() {
//		BigDecimal b = new BigDecimal("-1.5");
//		assertEquals(-1, b.intValue());
//
//		b = new BigDecimal("-1.000000000000000000000000000000000000000000000005");
//		b = new BigDecimal("-10000000000000000000000000000000000000000000000000000000");
//		assertEquals(-1, b.intValue());
//	}
	
	@Test
	public void yihuoTest() {
		System.out.println(true^false);
		System.out.println(false^true);
		System.out.println(true^true);
		System.out.println(false^false);
		System.out.println("-----------------------------------");
		System.out.println(true&false);
		System.out.println(false&true);
		System.out.println(true&true);
		System.out.println(false&false);
		System.out.println("-----------------------------------");
		System.out.println(true|false);
		System.out.println(false|true);
		System.out.println(true|true);
		System.out.println(false|false);
		System.out.println("-----------------------------------");
		System.out.println(~50);
		System.out.println(~-50);
		//6、~   非运算  二进制所有的位数取反;
			int	a = 4;
			int	b = 2;
				a = ~a;
				b = ~a;
				System.out.println("a: "+a+" b: "+b);

	}
	
	@Test
	public void divideTest() {
		BigDecimal b1 = new BigDecimal("0.000000002");
		BigDecimal b2 = new BigDecimal("0.01");
		assertEquals(0, b1.divide(b2, 4, RoundingMode.DOWN).compareTo(BigDecimal.ZERO));
		b1 = new BigDecimal(5);
		b2 = new BigDecimal(1);
		assertEquals(0, b1.divide(b2, 8, RoundingMode.DOWN).compareTo(new BigDecimal(5)));
	}

}
