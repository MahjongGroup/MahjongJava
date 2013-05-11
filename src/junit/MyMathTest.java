package junit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import util.MyMath;

public class MyMathTest {

	@Test
	public void testCeil() {
		assertEquals(MyMath.ceil(1280, 2), 1300);
		assertEquals(MyMath.ceil(8000, 2), 8000);
		assertEquals(MyMath.ceil(3840, 2), 3900);
	}

}
