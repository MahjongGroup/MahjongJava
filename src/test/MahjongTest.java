package test;

import static org.junit.Assert.*;

import org.junit.Test;

public class MahjongTest {

	@Test
	public void test() {
		try {
			for (int i = 0; i < 3; i++) {
				Main.run();
			}
		} catch(Exception e) {
			fail(e.toString());
		}
	}

}
