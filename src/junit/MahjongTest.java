package junit;

import static org.junit.Assert.*;

import org.junit.Test;

import test.Main;

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
