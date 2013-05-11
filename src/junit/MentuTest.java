package junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static system.hai.MajanHai.ITI_MAN;
import static system.hai.MajanHai.ITI_SOU;
import static system.hai.MajanHai.NI_MAN;
import static system.hai.MajanHai.NI_SOU;
import static system.hai.MajanHai.SAN_MAN;
import static system.hai.MajanHai.SAN_SOU;
import static system.hai.MajanHai.TON;

import org.junit.Test;

import system.hai.Hai;
import system.hai.Kaze;
import system.hai.MajanHai;
import system.hai.Mentsu;

public class MentuTest {
	@Test
	public void testCopy() {
		Mentsu m1 = new Mentsu(SAN_MAN, ITI_MAN, NI_MAN);
		Mentsu m2 = new Mentsu(m1, Kaze.TON, NI_MAN);
		assertEquals(m2.isNaki(), true);
		assertEquals(m2.getKaze(), Kaze.TON);
		assertTrue(m2.get(1).isNaki());
	}

	@Test
	public void testOrder() {
		Mentsu m1 = new Mentsu(SAN_MAN, ITI_MAN, NI_MAN);
		assertEquals(m1.type(), Mentsu.Type.SYUNTU);
		assertEquals(m1.get(0).type(), ITI_MAN.type());
		assertEquals(m1.get(1).type(), NI_MAN.type());
		assertEquals(m1.get(2).type(), SAN_MAN.type());
	}

	@Test
	public void testAnko() {
		Mentsu m1 = new Mentsu(ITI_MAN, ITI_MAN, ITI_MAN);
		assertEquals(m1.type(), Mentsu.Type.KOTU);
		assertEquals(m1.calcHu(), 8);
		assertEquals(m1.size(), 3);
		assertEquals(m1.isKakan(), false);
		assertEquals(m1.isNaki(), false);
		assertEquals(m1.getKaze(), null);

		Mentsu m2 = new Mentsu(TON, TON, TON);
		assertEquals(m2.type(), Mentsu.Type.KOTU);
		assertEquals(m2.calcHu(), 8);
		assertEquals(m2.size(), 3);
		assertEquals(m2.isKakan(), false);
		assertEquals(m2.isNaki(), false);
		assertEquals(m2.getKaze(), null);
	}

	@Test
	public void testEquals() {
		Mentsu m1 = new Mentsu(ITI_MAN, ITI_MAN, ITI_MAN);
		Mentsu m2 = new Mentsu(TON, TON, TON);

		Mentsu m3 = new Mentsu(ITI_SOU, NI_SOU, SAN_SOU);
		Mentsu m4 = new Mentsu(ITI_SOU, NI_SOU, SAN_SOU);
		Mentsu m5 = new Mentsu(ITI_SOU, Kaze.TON, NI_SOU, SAN_SOU);

		assertFalse(m1.equals(m2));
		assertTrue(m1.equals(m1));
		assertTrue(m3.equals(m4));
		assertFalse(m3.equals(m5));
	}

	@Test
	public void testKakan() {
		Mentsu m1 = new Mentsu(ITI_MAN, Kaze.TON, ITI_MAN, ITI_MAN);
		Mentsu m2 = m1.doKakan(ITI_MAN);
		assertTrue(m2.isKakan());
		assertTrue(m2.isNaki());
		assertTrue(m2.size() == 4);
	}

	@Test
	public void testConstruct() {
		for (Hai hai1 : MajanHai.values()) {
			for (Hai hai2 : MajanHai.values()) {
				for (Hai hai3 : MajanHai.values()) {
					Mentsu m = null;
					try {
						m = new Mentsu(hai1, hai2, hai3);
					} catch (IllegalArgumentException e) {
						assertEquals(m, null);
					}
					if (m != null) {
						assertTrue(m.type() != null);
					}
				}
			}
		}
	}
}
