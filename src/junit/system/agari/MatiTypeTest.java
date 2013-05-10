package junit.system.agari;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import system.agari.MatiType;
import system.hai.HaiType;
import system.hai.Mentsu;
import system.hai.Mentsu.MentsuHai;

import static org.junit.Assert.*;
import static system.hai.MajanHai.*;

public class MatiTypeTest {

	@Test
	public void test() {
		List<Mentsu> mlist1 = new ArrayList<Mentsu>();
		Mentsu m1 = new Mentsu(ITI_MAN, NI_MAN, SAN_MAN);
		Mentsu m2 = new Mentsu(NANA_MAN, HATI_MAN, KYU_MAN);
		Mentsu m3 = new Mentsu(NI_MAN, SAN_MAN, YO_MAN);
		Mentsu m4 = new Mentsu(SYA, SYA, SYA, SYA);
		Mentsu m5 = new Mentsu(YO_PIN, GO_PIN, ROKU_PIN);
		mlist1.add(m1);
		mlist1.add(m2);
		mlist1.add(m3);
		mlist1.add(m5);
		List<MatiType> mtl = MatiType.getMatiType(mlist1, HaiType.NI_MAN, HaiType.NI_MAN);
		assertTrue(mtl.contains(MatiType.KANTYAN));
		assertTrue(mtl.contains(MatiType.RYANMEN));
		assertTrue(mtl.contains(MatiType.TANKI));
	}

}
