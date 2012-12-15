package junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static system.MajanHai.GO_MAN;
import static system.MajanHai.ITI_MAN;
import static system.MajanHai.NAN;
import static system.MajanHai.NI_MAN;
import static system.MajanHai.ROKU_MAN;
import static system.MajanHai.SAN_MAN;
import static system.MajanHai.TON;
import static system.MajanHai.YO_MAN;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import system.AgariResult;
import system.Field;
import system.Hai;
import system.HaiType;
import system.HurohaiList;
import system.Kaze;
import system.MatiType;
import system.NormalYaku;
import system.Param;
import system.Rule;
import system.TehaiList;
import system.Yaku;

public class AgariResultTest {
	// 1,2,2,3,3,3,4,4, TON,TON,TON, NAN,NAN
	static TehaiList list1 = new TehaiList(Arrays.asList(new Hai[] { ITI_MAN, NI_MAN, NI_MAN, SAN_MAN, SAN_MAN, SAN_MAN, YO_MAN, YO_MAN, TON, TON, TON, NAN, NAN }));

	// 1,1,1,2,2,2,3,3,3,4,5,6,6
	static TehaiList list2 = new TehaiList(Arrays.asList(new Hai[] { ITI_MAN, ITI_MAN, ITI_MAN, NI_MAN, NI_MAN, NI_MAN, SAN_MAN, SAN_MAN, SAN_MAN, YO_MAN, GO_MAN, ROKU_MAN, ROKU_MAN}));

	static TehaiList list3;

	static Field field = new Field(new Rule(), Kaze.TON);
	
	@Test
	public void testList1() {
		// 1,2,2,3,3,3,4,4, TON,TON,TON, NAN,NAN
		
		Param param = new Param();
		param.setTsumo(true);
		param.setNaki(false);
		param.setJikaze(Kaze.TON);
		param.setFlagCheckYakuSet(new HashSet<Yaku>());
		param.setAgariHai(NI_MAN);
		
		AgariResult ar = AgariResult.createAgariResult(list1, new HurohaiList(), param, field, Kaze.NAN, new ArrayList<HaiType>(0), new ArrayList<HaiType>(0));
		assertEquals(ar.getMatiTye(), MatiType.KANTYAN);
	}
	
	@Test
	public void testList2() {
		// 1,1,1,2,2,2,3,3,3,4,5,6,6
		// agarihai 3
		Param param = new Param();
		param.setTsumo(false);
		param.setNaki(false);
		param.setJikaze(Kaze.TON);
		param.setAgariHai(SAN_MAN);
		param.setFlagCheckYakuSet(new HashSet<Yaku>());
		
		AgariResult ar = AgariResult.createAgariResult(list2, new HurohaiList(), param, field, Kaze.NAN, new ArrayList<HaiType>(0), new ArrayList<HaiType>(0));
		assertEquals(ar.getMatiTye(), MatiType.RYANMEN);
		assertTrue(ar.getYakuSet().contains(NormalYaku.SANNANKO));
		assertTrue(!ar.getYakuSet().contains(NormalYaku.IPEKO));
		assertTrue(!ar.getYakuSet().contains(NormalYaku.PINHU));
	}
	

}
