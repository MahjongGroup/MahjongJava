package junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static system.hai.MajanHai.GO_MAN;
import static system.hai.MajanHai.ITI_MAN;
import static system.hai.MajanHai.NAN;
import static system.hai.MajanHai.NI_MAN;
import static system.hai.MajanHai.ROKU_MAN;
import static system.hai.MajanHai.SAN_MAN;
import static system.hai.MajanHai.TON;
import static system.hai.MajanHai.YO_MAN;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import system.Field;
import system.Rule;
import system.agari.AgariParam;
import system.agari.AgariResult;
import system.agari.CheckParam;
import system.agari.MatiType;
import system.hai.Hai;
import system.hai.HaiType;
import system.hai.HurohaiList;
import system.hai.Kaze;
import system.hai.TehaiList;
import system.yaku.NormalYaku;
import system.yaku.Yaku;

public class AgariResultTest {
	// 1,2,2,3,3,3,4,4, TON,TON,TON, NAN,NAN
	static TehaiList list1 = new TehaiList(Arrays.asList(new Hai[] { ITI_MAN, NI_MAN, NI_MAN, SAN_MAN, SAN_MAN, SAN_MAN, YO_MAN, YO_MAN, TON, TON, TON, NAN, NAN }));

	// 1,1,1,2,2,2,3,3,3,4,5,6,6
	static TehaiList list2 = new TehaiList(Arrays.asList(new Hai[] { ITI_MAN, ITI_MAN, ITI_MAN, NI_MAN, NI_MAN, NI_MAN, SAN_MAN, SAN_MAN, SAN_MAN, YO_MAN, GO_MAN, ROKU_MAN, ROKU_MAN}));

	static TehaiList list3;

	static Field field = new Field(new Rule(), Kaze.TON);
	
	static List<HaiType> odora = new ArrayList<HaiType>();
	static List<HaiType> udora = new ArrayList<HaiType>();
	
	static {
		odora.add(HaiType.ITI_MAN);
		odora.add(HaiType.ROKU_MAN);
	}
	
	@Test
	public void testList1() {
		// 1,2,2,3,3,3,4,4, TON,TON,TON, NAN,NAN
		
		AgariParam param = new AgariParam(true, false, NI_MAN, Kaze.TON);
		
		AgariResult.Builder builder = new AgariResult.Builder();
		builder.setAgariParam(param);
		builder.setField(field);
		builder.setHojuKaze(Kaze.NAN);
		builder.setHurohaiList(new HurohaiList());
		builder.setOpenDoraList(odora);
		builder.setTehaiList(list1);
		builder.setUraDoraList(udora);
		builder.setYakuFlag(new HashSet<Yaku>());
		
		AgariResult ar = builder.build();
		System.out.println(ar);
	}
	
	@Test
	public void testList2() {
		// 1,1,1,2,2,2,3,3,3,4,5,6,6
		AgariParam param = new AgariParam(true, false, SAN_MAN, Kaze.TON);
		
		AgariResult.Builder builder = new AgariResult.Builder();
		builder.setAgariParam(param);
		builder.setField(field);
		builder.setHojuKaze(Kaze.NAN);
		builder.setHurohaiList(new HurohaiList());
		builder.setOpenDoraList(odora);
		builder.setTehaiList(list2);
		builder.setUraDoraList(udora);
		builder.setYakuFlag(new HashSet<Yaku>());
		
		AgariResult ar = builder.build();
		System.out.println(ar);
	}
	

}
