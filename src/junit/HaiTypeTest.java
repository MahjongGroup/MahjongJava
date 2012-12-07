package junit;

import static org.junit.Assert.*;

import org.junit.Test;

import system.HaiType;

import static system.HaiType.*;

public class HaiTypeTest {

	@Test
	public void testDora() {
		for (HaiType type : HaiType.values()) {
			assertTrue(type.nextOfDora() != null);
		}
		
		assertTrue(ITI_MAN.nextOfDora() == NI_MAN);
		assertTrue(NI_MAN.nextOfDora() == SAN_MAN);
		assertTrue(SAN_MAN.nextOfDora() == YO_MAN);
		assertTrue(YO_MAN.nextOfDora() == GO_MAN);
		assertTrue(GO_MAN.nextOfDora() == ROKU_MAN);
		assertTrue(ROKU_MAN.nextOfDora() == NANA_MAN);
		assertTrue(NANA_MAN.nextOfDora() == HATI_MAN);
		assertTrue(HATI_MAN.nextOfDora() == KYU_MAN);
		assertTrue(KYU_MAN.nextOfDora() == ITI_MAN);

		assertTrue(ITI_PIN.nextOfDora() == NI_PIN);
		assertTrue(NI_PIN.nextOfDora() == SAN_PIN);
		assertTrue(SAN_PIN.nextOfDora() == YO_PIN);
		assertTrue(YO_PIN.nextOfDora() == GO_PIN);
		assertTrue(GO_PIN.nextOfDora() == ROKU_PIN);
		assertTrue(ROKU_PIN.nextOfDora() == NANA_PIN);
		assertTrue(NANA_PIN.nextOfDora() == HATI_PIN);
		assertTrue(HATI_PIN.nextOfDora() == KYU_PIN);
		assertTrue(KYU_PIN.nextOfDora() == ITI_PIN);

		assertTrue(ITI_SOU.nextOfDora() == NI_SOU);
		assertTrue(NI_SOU.nextOfDora() == SAN_SOU);
		assertTrue(SAN_SOU.nextOfDora() == YO_SOU);
		assertTrue(YO_SOU.nextOfDora() == GO_SOU);
		assertTrue(GO_SOU.nextOfDora() == ROKU_SOU);
		assertTrue(ROKU_SOU.nextOfDora() == NANA_SOU);
		assertTrue(NANA_SOU.nextOfDora() == HATI_SOU);
		assertTrue(HATI_SOU.nextOfDora() == KYU_SOU);
		assertTrue(KYU_SOU.nextOfDora() == ITI_SOU);
		
		assertTrue(TON.nextOfDora() == NAN);
		assertTrue(NAN.nextOfDora() == SYA);
		assertTrue(SYA.nextOfDora() == PE);
		assertTrue(PE.nextOfDora() == TON);

		assertTrue(HAKU.nextOfDora() == HATU);
		assertTrue(HATU.nextOfDora() == TYUN);
		assertTrue(TYUN.nextOfDora() == HAKU);
	}

}
