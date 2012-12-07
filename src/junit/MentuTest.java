package junit;

import static org.junit.Assert.*;

import org.junit.Test;

import system.Hai;
import system.Kaze;
import system.MajanHai;
import system.Mentu;

import static system.MajanHai.*;

public class MentuTest {

	@Test
	public void testAnko() {
		Mentu m1 = new Mentu(ITI_MAN, ITI_MAN, ITI_MAN);
		assertEquals(m1.type(), Mentu.Type.KOTU);
		assertEquals(m1.calcHu(), 8);
		assertEquals(m1.size(), 3);
		assertEquals(m1.isKakan(), false);
		assertEquals(m1.isNaki(), false);
		assertEquals(m1.getKaze(), null);

		Mentu m2 = new Mentu(TON, TON, TON);
		assertEquals(m2.type(), Mentu.Type.KOTU);
		assertEquals(m2.calcHu(), 8);
		assertEquals(m2.size(), 3);
		assertEquals(m2.isKakan(), false);
		assertEquals(m2.isNaki(), false);
		assertEquals(m2.getKaze(), null);
	}

	@Test
	public void testEquals() {
		Mentu m1 = new Mentu(ITI_MAN, ITI_MAN, ITI_MAN);
		Mentu m2 = new Mentu(TON, TON, TON);

		Mentu m3 = new Mentu(ITI_SOU, NI_SOU, SAN_SOU);
		Mentu m4 = new Mentu(ITI_SOU, NI_SOU, SAN_SOU);
		Mentu m5 = new Mentu(ITI_SOU, Kaze.TON, NI_SOU, SAN_SOU);

		assertFalse(m1.equals(m2));
		assertTrue(m1.equals(m1));
		assertTrue(m3.equals(m4));
		assertFalse(m3.equals(m5));
	}

	@Test
	public void testKakan() {
		Mentu m1 = new Mentu(ITI_MAN, Kaze.TON, ITI_MAN, ITI_MAN);
		Mentu m2 = m1.doKakan(ITI_MAN);
		assertTrue(m2.isKakan());
		assertTrue(m2.isNaki());
		assertTrue(m2.size() == 4);
	}

	@Test
	public void testConstruct() {
		for (Hai hai1 : MajanHai.values()) {
			for (Hai hai2 : MajanHai.values()) {
				for (Hai hai3 : MajanHai.values()) {
					Mentu m = null;
					try {
						m = new Mentu(hai1, hai2, hai3);
					} catch (IllegalArgumentException e) {
						assertEquals(m, null);
					}
					if(m != null) {
						assertTrue(m.type() != null);
					}
				}
			}
		}
	}
}
