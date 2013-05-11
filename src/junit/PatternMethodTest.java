package junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static system.hai.MajanHai.GO_MAN;
import static system.hai.MajanHai.GO_PIN;
import static system.hai.MajanHai.GO_SOU;
import static system.hai.MajanHai.HATI_MAN;
import static system.hai.MajanHai.HATI_PIN;
import static system.hai.MajanHai.ITI_MAN;
import static system.hai.MajanHai.ITI_PIN;
import static system.hai.MajanHai.ITI_SOU;
import static system.hai.MajanHai.KYU_MAN;
import static system.hai.MajanHai.KYU_PIN;
import static system.hai.MajanHai.KYU_SOU;
import static system.hai.MajanHai.NAN;
import static system.hai.MajanHai.NANA_MAN;
import static system.hai.MajanHai.NANA_SOU;
import static system.hai.MajanHai.NI_MAN;
import static system.hai.MajanHai.NI_PIN;
import static system.hai.MajanHai.NI_SOU;
import static system.hai.MajanHai.ROKU_MAN;
import static system.hai.MajanHai.ROKU_PIN;
import static system.hai.MajanHai.ROKU_SOU;
import static system.hai.MajanHai.SAN_MAN;
import static system.hai.MajanHai.SAN_PIN;
import static system.hai.MajanHai.SAN_SOU;
import static system.hai.MajanHai.TON;
import static system.hai.MajanHai.YO_MAN;
import static system.hai.MajanHai.YO_PIN;
import static system.hai.MajanHai.YO_SOU;

import java.util.Arrays;

import org.junit.Test;

import system.agari.AgariMethods;
import system.algo.PatternMethod;
import system.hai.Hai;
import system.hai.TehaiList;

public class PatternMethodTest {
	static TehaiList list1;
	static TehaiList list2;
	static TehaiList list3;
	static TehaiList list4;
	static TehaiList list5;
	static TehaiList list6;
	static TehaiList list7;
	static TehaiList list8;
	static TehaiList list9;
	static TehaiList noList1;

	static {
		PatternMethod.loadClass();
		// 1,2,2,2,3,3,3,4,4, TON,TON,TON, NAN,NAN
		// 一盃口
		list1 = new TehaiList(Arrays.asList(new Hai[] { ITI_MAN, NI_MAN, NI_MAN, NI_MAN, SAN_MAN, SAN_MAN, SAN_MAN, YO_MAN, YO_MAN, TON, TON, TON, NAN, NAN }));

		// 2,3,4,4,5,6, 二,三,四, ５,６,７,９,９
		list2 = new TehaiList(Arrays.asList(new Hai[] { NI_MAN, SAN_MAN, YO_MAN, YO_MAN, GO_MAN, ROKU_MAN, NI_PIN, SAN_PIN, YO_PIN, GO_SOU, ROKU_SOU, NANA_SOU, KYU_SOU, KYU_SOU }));

		// 1,2,2,3,3,3,4,4,5,6,7,8,9,9
		list3 = new TehaiList(Arrays.asList(new Hai[] { ITI_MAN, NI_MAN, NI_MAN, SAN_MAN, SAN_MAN, SAN_MAN, YO_MAN, YO_MAN, GO_MAN, ROKU_MAN, NANA_MAN, HATI_MAN, KYU_MAN, KYU_MAN }));

		// 1,2,3,4,5,6,7,8,9,TON,TON
		// 一気通貫
		list4 = new TehaiList(Arrays.asList(new Hai[] { ITI_MAN, NI_MAN, SAN_MAN, YO_MAN, GO_MAN, ROKU_MAN, NANA_MAN, HATI_MAN, KYU_MAN, TON, TON }));

		// 1,2,3,4,5,6,7,7,8,8,9,9,TON,TON
		// 一気通貫
		// 一盃口
		list5 = new TehaiList(Arrays.asList(new Hai[] { ITI_MAN, NI_MAN, SAN_MAN, YO_MAN, GO_MAN, ROKU_MAN, NANA_MAN, NANA_MAN, HATI_MAN, HATI_MAN, KYU_MAN, KYU_MAN, TON, TON }));

		// １,１,２,２,３,３,５,５,６,６,７,７,９,９
		// 二盃口
		list6 = new TehaiList(Arrays.asList(new Hai[] { ITI_SOU, ITI_SOU, NI_SOU, NI_SOU, SAN_SOU, SAN_SOU, GO_SOU, GO_SOU, ROKU_SOU, ROKU_SOU, NANA_SOU, NANA_SOU, KYU_SOU, KYU_SOU }));

		// 一一二二三三四四五五六六八八
		// 二盃口
		list7 = new TehaiList(Arrays.asList(new Hai[] { ITI_PIN, ITI_PIN, NI_PIN, NI_PIN, SAN_PIN, SAN_PIN, YO_PIN, YO_PIN, GO_PIN, GO_PIN, ROKU_PIN, ROKU_PIN, HATI_PIN, HATI_PIN }));

		// １１１２２２２３３３３４４４
		list8 = new TehaiList(Arrays.asList(new Hai[] { ITI_SOU, ITI_SOU, ITI_SOU, NI_SOU, NI_SOU, NI_SOU, NI_SOU, SAN_SOU, SAN_SOU, SAN_SOU, SAN_SOU, YO_SOU, YO_SOU, YO_SOU }));

		// 1,2,2,3,3,3,4,4,5,6,7,8,9,
		list9 = new TehaiList(Arrays.asList(new Hai[] { ITI_MAN, NI_MAN, NI_MAN, SAN_MAN, SAN_MAN, SAN_MAN, YO_MAN, YO_MAN, GO_MAN, ROKU_MAN, NANA_MAN, HATI_MAN, KYU_MAN }));

		// 一二二三三四四四五六六八八九
		noList1 = new TehaiList(Arrays.asList(new Hai[] { ITI_PIN, NI_PIN, NI_PIN, SAN_PIN, SAN_PIN, YO_PIN, YO_PIN, YO_PIN, GO_PIN, ROKU_PIN, ROKU_PIN, HATI_PIN, HATI_PIN, KYU_PIN }));

	}

	@Test
	public void testPatternMethodList1() {
		int n[] = list1.toSizeArray();
		Integer keys[] = PatternMethod.calcKey(n);
		assertEquals(keys.length, 3);
		PatternMethod.Value value = PatternMethod.getValue(list1);
		assertTrue(value.isSyuntsuRm());
		assertTrue(!value.isRyanpeko());
		assertTrue(value.isIpeko());
		assertTrue(!value.isIkkiTsukan());
		
		for (int i = 0; i < 1000; i++) {
			n = list1.toSizeArray();
			PatternMethod.getValue(list1);
		}
	}

	@Test
	public void testPatternMethodList2() {
		// 2,3,4,4,5,6, 二,三,四, ５,６,７,９,９
		int n[] = list2.toSizeArray();
		Integer keys[] = PatternMethod.calcKey(n);
		assertEquals(keys.length, 4);
		PatternMethod.Value value = PatternMethod.getValue(list2);
		assertTrue(value.isSyuntsuRm() && value.isKotsuRm());
		assertTrue(!value.isRyanpeko());
		assertTrue(!value.isIpeko());
		assertTrue(!value.isIkkiTsukan());

		for (int i = 0; i < 1000; i++) {
			PatternMethod.getValue(list2);
		}
	}

	@Test
	public void testPatternMethodList3() {
		int n[] = list3.toSizeArray();
		Integer keys[] = PatternMethod.calcKey(n);
		assertEquals(keys.length, 1);
		PatternMethod.Value value = PatternMethod.getValue(list3);
		assertTrue(value.isSyuntsuRm());
		assertTrue(!value.isRyanpeko());
		assertTrue(!value.isIpeko());
		assertTrue(!value.isIkkiTsukan());

		for (int i = 0; i < 1000; i++) {
			PatternMethod.getValue(list3);
		}
	}

	@Test
	public void testPatternMethodList4() {
		// 1,2,3,4,5,6,7,8,9,TON,TON
		// 一気通貫
		int n[] = list4.toSizeArray();
		Integer keys[] = PatternMethod.calcKey(n);
		assertEquals(keys.length, 2);
		PatternMethod.Value value = PatternMethod.getValue(list4);
		assertTrue(value.isSuccessful());
		assertTrue(!value.isRyanpeko());
		assertTrue(!value.isIpeko());
		assertTrue(value.isIkkiTsukan());

		for (int i = 0; i < 1000; i++) {
			PatternMethod.getValue(list4);
		}
	}

	@Test
	public void testPatternMethodList5() {
		// 1,2,3,4,5,6,7,7,8,8,9,9,TON,TON
		// 一気通貫
		// 一盃口
		int n[] = list5.toSizeArray();
		Integer keys[] = PatternMethod.calcKey(n);
		assertEquals(keys.length, 2);
		PatternMethod.Value value = PatternMethod.getValue(list5);
		assertTrue(value.isSyuntsuRm() && value.isKotsuRm());
		assertTrue(!value.isRyanpeko());
		assertTrue(value.isIpeko());
		assertTrue(value.isIkkiTsukan());

		for (int i = 0; i < 1000; i++) {
			PatternMethod.getValue(list5);
		}
	}

	@Test
	public void testPatternMethodList6() {
		// １,１,２,２,３,３,５,５,６,６,７,７,９,９
		// 二盃口
		int n[] = list6.toSizeArray();
		Integer keys[] = PatternMethod.calcKey(n);
		assertEquals(keys.length, 3);
		PatternMethod.Value value = PatternMethod.getValue(list6);
		assertTrue(value.isSyuntsuRm() && value.isKotsuRm());
		assertTrue(value.isRyanpeko());
		assertTrue(!value.isIkkiTsukan());
		for (int i = 0; i < 1000; i++) {
			PatternMethod.getValue(list6);
		}
	}

	@Test
	public void testPatternMethodList8() {
		// １１１２２２２３３３３４４４
		// 一盃口
		int n[] = list8.toSizeArray();
		Integer keys[] = PatternMethod.calcKey(n);
		assertEquals(keys.length, 1);
		PatternMethod.Value value = PatternMethod.getValue(list8);
		assertTrue(value.isSyuntsuRm() && value.isKotsuRm());
		assertTrue(!value.isRyanpeko());
		assertTrue(value.isIpeko());
		assertTrue(!value.isIkkiTsukan());

		for (int i = 0; i < 1000; i++) {
			PatternMethod.getValue(list8);
		}
	}

	@Test
	public void testPatternMethodList7() {
		// 一一二二三三四四五五六六八八
		// 二盃口
		int n[] = list7.toSizeArray();
		Integer keys[] = PatternMethod.calcKey(n);
		assertEquals(keys.length, 2);
		PatternMethod.Value value = PatternMethod.getValue(list7);
		assertTrue(value.isSyuntsuRm() && value.isKotsuRm());
		assertTrue(value.isRyanpeko());
		assertTrue(!value.isIkkiTsukan());

		for (int i = 0; i < 1000; i++) {
			PatternMethod.getValue(list7);
		}
	}

	@Test
	public void testPatternMethodNoList1() {
		int n[] = noList1.toSizeArray();
		Integer keys[] = PatternMethod.calcKey(n);
		assertEquals(keys.length, 2);
		PatternMethod.Value value = PatternMethod.getValue(noList1);
		assertTrue(!value.isSuccessful());

		for (int i = 0; i < 1000; i++) {
			PatternMethod.getValue(noList1);
		}
	}

	@Test
	public void testBackTrackList1() {
		assertTrue(AgariMethods.isNMentu1Janto(list1));
		for (int i = 0; i < 1000; i++) {
			AgariMethods.isNMentu1Janto(list1);
		}
	}

	@Test
	public void testBackTrackList2() {
		assertTrue(AgariMethods.isNMentu1Janto(list2));
		for (int i = 0; i < 1000; i++) {
			AgariMethods.isNMentu1Janto(list2);
		}
	}

	@Test
	public void testBackTrackList3() {
		assertTrue(AgariMethods.isNMentu1Janto(list3));
		for (int i = 0; i < 1000; i++) {
			AgariMethods.isNMentu1Janto(list3);
		}
	}

	@Test
	public void testBackTrackList8() {
		assertTrue(AgariMethods.isNMentu1Janto(list8));
		for (int i = 0; i < 1000; i++) {
			AgariMethods.isNMentu1Janto(list8);
		}
	}

	@Test
	public void testBackTrackNoList1() {
		assertTrue(!AgariMethods.isNMentu1Janto(noList1));
		for (int i = 0; i < 1000; i++) {
			AgariMethods.isNMentu1Janto(noList1);
		}
	}

	@Test
	public void testIsTenpai() {
		// 1,2,2,3,3,3,4,4,5,6,7,8,9
		for (int i = 0; i < 1000; i++) {
			assertTrue(AgariMethods.isTenpai(list9, false));
		}
	}
}
