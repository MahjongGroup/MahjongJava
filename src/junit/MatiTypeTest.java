package junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static system.hai.MajanHai.GO_PIN;
import static system.hai.MajanHai.HATI_SOU;
import static system.hai.MajanHai.ITI_MAN;
import static system.hai.MajanHai.KYU_SOU;
import static system.hai.MajanHai.NAN;
import static system.hai.MajanHai.NANA_SOU;
import static system.hai.MajanHai.NI_MAN;
import static system.hai.MajanHai.ROKU_PIN;
import static system.hai.MajanHai.SAN_MAN;
import static system.hai.MajanHai.TON;
import static system.hai.MajanHai.YO_PIN;

import org.junit.Test;

import system.agari.MatiType;
import system.hai.Mentsu;

public class MatiTypeTest {
	static Mentsu m1 = new Mentsu(ITI_MAN, NI_MAN, SAN_MAN);
	static Mentsu m2 = new Mentsu(NANA_SOU, HATI_SOU, KYU_SOU);
	static Mentsu m3 = new Mentsu(YO_PIN, GO_PIN, ROKU_PIN);
	static Mentsu m4 = new Mentsu(NAN, NAN, NAN);
	
	@Test
	public void testGetMatiType() {
		assertEquals(MatiType.getMatiType(NI_MAN.type(), m1, TON.type()), MatiType.KANTYAN);
		assertEquals(MatiType.getMatiType(SAN_MAN.type(), m1, TON.type()), MatiType.PENTYAN);
		assertEquals(MatiType.getMatiType(ITI_MAN.type(), m1, TON.type()), MatiType.RYANMEN);
	}

	@Test
	public void testRyanmen() {
		assertTrue(MatiType.RYANMEN.check(ITI_MAN.type(), m1, TON.type()));
		assertFalse(MatiType.RYANMEN.check(SAN_MAN.type(), m1, TON.type()));
		assertFalse(MatiType.RYANMEN.check(NI_MAN.type(), m1, TON.type()));
		
		assertFalse(MatiType.RYANMEN.check(NANA_SOU.type(), m2, TON.type()));
		assertFalse(MatiType.RYANMEN.check(HATI_SOU.type(), m2, TON.type()));
		assertTrue(MatiType.RYANMEN.check(KYU_SOU.type(), m2, TON.type()));

		assertTrue(MatiType.RYANMEN.check(YO_PIN.type(), m3, TON.type()));
		assertFalse(MatiType.RYANMEN.check(GO_PIN.type(), m3, TON.type()));
		assertTrue(MatiType.RYANMEN.check(ROKU_PIN.type(), m3, TON.type()));

		assertFalse(MatiType.RYANMEN.check(TON.type(), m4, TON.type()));
	}

	@Test
	public void testKantyan() {
		assertFalse(MatiType.KANTYAN.check(ITI_MAN.type(), m1, TON.type()));
		assertTrue(MatiType.KANTYAN.check(NI_MAN.type(), m1, TON.type()));
		assertFalse(MatiType.KANTYAN.check(SAN_MAN.type(), m1, TON.type()));
		
		assertFalse(MatiType.KANTYAN.check(NANA_SOU.type(), m2, TON.type()));
		assertTrue(MatiType.KANTYAN.check(HATI_SOU.type(), m2, TON.type()));
		assertFalse(MatiType.KANTYAN.check(KYU_SOU.type(), m2, TON.type()));

		assertFalse(MatiType.KANTYAN.check(YO_PIN.type(), m3, TON.type()));
		assertTrue(MatiType.KANTYAN.check(GO_PIN.type(), m3, TON.type()));
		assertFalse(MatiType.KANTYAN.check(ROKU_PIN.type(), m3, TON.type()));

		assertFalse(MatiType.KANTYAN.check(TON.type(), m4, TON.type()));
	}
	
	@Test
	public void testPentyan() {
		assertFalse(MatiType.PENTYAN.check(ITI_MAN.type(), m1, TON.type()));
		assertFalse(MatiType.PENTYAN.check(NI_MAN.type(), m1, TON.type()));
		assertTrue(MatiType.PENTYAN.check(SAN_MAN.type(), m1, TON.type()));
		
		assertTrue(MatiType.PENTYAN.check(NANA_SOU.type(), m2, TON.type()));
		assertFalse(MatiType.PENTYAN.check(HATI_SOU.type(), m2, TON.type()));
		assertFalse(MatiType.PENTYAN.check(KYU_SOU.type(), m2, TON.type()));

		assertFalse(MatiType.PENTYAN.check(YO_PIN.type(), m3, TON.type()));
		assertFalse(MatiType.PENTYAN.check(GO_PIN.type(), m3, TON.type()));
		assertFalse(MatiType.PENTYAN.check(ROKU_PIN.type(), m3, TON.type()));

		assertFalse(MatiType.PENTYAN.check(TON.type(), m4, TON.type()));
	}

	@Test
	public void testTanki() {
		assertFalse(MatiType.TANKI.check(ITI_MAN.type(), m1, TON.type()));
		assertFalse(MatiType.TANKI.check(NI_MAN.type(), m1, TON.type()));
		assertFalse(MatiType.TANKI.check(SAN_MAN.type(), m1, TON.type()));
		
		assertFalse(MatiType.TANKI.check(NANA_SOU.type(), m2, TON.type()));
		assertFalse(MatiType.TANKI.check(HATI_SOU.type(), m2, TON.type()));
		assertFalse(MatiType.TANKI.check(KYU_SOU.type(), m2, TON.type()));

		assertFalse(MatiType.TANKI.check(YO_PIN.type(), m3, TON.type()));
		assertFalse(MatiType.TANKI.check(GO_PIN.type(), m3, TON.type()));
		assertFalse(MatiType.TANKI.check(ROKU_PIN.type(), m3, TON.type()));

		assertTrue(MatiType.TANKI.check(TON.type(), m4, TON.type()));
		assertFalse(MatiType.TANKI.check(NAN.type(), m4, TON.type()));
	}

	@Test
	public void testSyabo() {
		assertFalse(MatiType.SYABO.check(ITI_MAN.type(), m1, TON.type()));
		assertFalse(MatiType.SYABO.check(NI_MAN.type(), m1, TON.type()));
		assertFalse(MatiType.SYABO.check(SAN_MAN.type(), m1, TON.type()));
		
		assertFalse(MatiType.SYABO.check(NANA_SOU.type(), m2, TON.type()));
		assertFalse(MatiType.SYABO.check(HATI_SOU.type(), m2, TON.type()));
		assertFalse(MatiType.SYABO.check(KYU_SOU.type(), m2, TON.type()));

		assertFalse(MatiType.SYABO.check(YO_PIN.type(), m3, TON.type()));
		assertFalse(MatiType.SYABO.check(GO_PIN.type(), m3, TON.type()));
		assertFalse(MatiType.SYABO.check(ROKU_PIN.type(), m3, TON.type()));

		assertFalse(MatiType.SYABO.check(TON.type(), m4, TON.type()));
		assertTrue(MatiType.SYABO.check(NAN.type(), m4, TON.type()));
	}


}
