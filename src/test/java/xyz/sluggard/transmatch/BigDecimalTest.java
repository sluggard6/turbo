package xyz.sluggard.transmatch;

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

}
