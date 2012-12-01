package test;

import static org.junit.Assert.*;

import org.junit.Test;

import system.NormalYaku;

public class NormalYakuTest {

	@Test
	public void test() {
		
	}

	@Test
	public void testReach() {
		assertTrue(NormalYaku.RICHI.isNaki() == false);
		assertEquals(NormalYaku.RICHI.isNaki(), false);
	}

	@Test
	public void testIpeko() {
		
	}

}
