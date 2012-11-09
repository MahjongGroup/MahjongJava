package debug;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import system.AgariFunctions;
import system.CheckerParam;
import system.Hai;
import system.HaiType;
import system.HurohaiList;
import system.Kaze;
import system.MajanHai;
import system.Rule;
import system.TehaiList;
import system.Yaku;

public class Debugger {
	/**
	 * サイズ：13 暗刻：１、順子：２ 5-8索待ちテンパイ
	 */
	public static List<Hai> haiList0 = new ArrayList<Hai>();

	/**
	 * サイズ：10 順子：３ 4-7萬待ちテンパイ
	 */
	public static List<Hai> haiList1 = new ArrayList<Hai>();

	/**
	 * サイズ：7 暗槓：TON テンパイしていない
	 */
	public static List<Hai> haiList2 = new ArrayList<Hai>();

	/**
	 * サイズ：13
	 * 2-3-4-5-6索テンパイ
	 */
	public static List<Hai> haiList3 = new ArrayList<Hai>();

	/**
	 * サイズ：13
	 */
	public static List<Hai> haiList4 = new ArrayList<Hai>();

	static {
		haiList0.add(MajanHai.ITI_PIN);
		haiList0.add(MajanHai.ITI_PIN);

		haiList0.add(MajanHai.AKA_GO_SOU);
		haiList0.add(MajanHai.ROKU_SOU);
		haiList0.add(MajanHai.NANA_SOU);

		haiList0.add(MajanHai.HAKU);
		haiList0.add(MajanHai.HAKU);
		haiList0.add(MajanHai.HAKU);

		haiList0.add(MajanHai.NANA_MAN);
		haiList0.add(MajanHai.HATI_MAN);
		haiList0.add(MajanHai.KYU_MAN);

		haiList0.add(MajanHai.ROKU_SOU);
		haiList0.add(MajanHai.NANA_SOU);

		haiList1.add(MajanHai.NAN);
		haiList1.add(MajanHai.NAN);

		haiList1.add(MajanHai.NI_MAN);
		haiList1.add(MajanHai.SAN_MAN);
		haiList1.add(MajanHai.YO_MAN);

		haiList1.add(MajanHai.SAN_MAN);
		haiList1.add(MajanHai.YO_MAN);
		haiList1.add(MajanHai.GO_MAN);

		haiList1.add(MajanHai.GO_MAN);
		haiList1.add(MajanHai.ROKU_MAN);

		haiList2.add(MajanHai.NAN);
		haiList2.add(MajanHai.NAN);

		haiList2.add(MajanHai.HATU);

		haiList2.add(MajanHai.TON);
		haiList2.add(MajanHai.TON);
		haiList2.add(MajanHai.TON);
		haiList2.add(MajanHai.TON);

		haiList3.add(MajanHai.NI_MAN);
		haiList3.add(MajanHai.SAN_MAN);
		haiList3.add(MajanHai.YO_MAN);

		haiList3.add(MajanHai.SAN_SOU);
		haiList3.add(MajanHai.SAN_SOU);
		haiList3.add(MajanHai.SAN_SOU);
		haiList3.add(MajanHai.YO_SOU);
		haiList3.add(MajanHai.GO_SOU);
		haiList3.add(MajanHai.GO_SOU);
		haiList3.add(MajanHai.GO_SOU);

		haiList3.add(MajanHai.PE);
		haiList3.add(MajanHai.PE);
		haiList3.add(MajanHai.PE);
		
		haiList4.add(MajanHai.SAN_MAN);
		haiList4.add(MajanHai.YO_MAN);
		haiList4.add(MajanHai.GO_MAN);

		haiList4.add(MajanHai.ROKU_PIN);
		haiList4.add(MajanHai.ROKU_PIN);
		haiList4.add(MajanHai.NANA_PIN);
		
		haiList4.add(MajanHai.NI_SOU);
		haiList4.add(MajanHai.NI_SOU);
		haiList4.add(MajanHai.NI_SOU);
		haiList4.add(MajanHai.SAN_SOU);
		
		haiList4.add(MajanHai.SYA);
		haiList4.add(MajanHai.SYA);
		haiList4.add(MajanHai.SYA);
	}

	public static void asrt(boolean exp, String msg) {
		if (!exp) {
			System.err.println("assertion error! : " + msg);
			throw new AssertionError();
		}
	}

	public static void asrt(boolean exp) {
		if (!exp) {
			System.err.println("assertion error!");
			throw new AssertionError();
		}
	}

	public static void main(String[] args) {
		TehaiList tehai0 = new TehaiList(haiList0);
		TehaiList tehai1 = new TehaiList(haiList1);
		TehaiList tehai2 = new TehaiList(haiList2);
		TehaiList tehai3 = new TehaiList(haiList3);
		TehaiList tehai4 = new TehaiList(haiList4);

		List<List<Integer>> indexList0 = tehai0.getPonableIndexList(HaiType.ITI_PIN);
		List<List<Integer>> indexList1 = tehai0.getPonableIndexList(HaiType.HAKU);

		asrt(indexList0.size() == 1, "size = " + indexList0.size());
		asrt(indexList0.get(0).get(0) == 0);
		asrt(indexList0.get(0).get(1) == 1);

		asrt(indexList1.size() == 3, "size = " + indexList1.size());
		asrt(indexList1.get(0).get(0) == 5);
		asrt(indexList1.get(0).get(1) == 6);
		asrt(indexList1.get(1).get(0) == 5);
		asrt(indexList1.get(1).get(1) == 7);
		asrt(indexList1.get(2).get(0) == 6);
		asrt(indexList1.get(2).get(1) == 7);

		asrt(tehai0.isMinkanable(HaiType.HAKU));

		asrt(!tehai0.isAnkanable(HaiType.HAKU));
		asrt(!tehai0.isAnkanable());

		asrt(tehai2.isAnkanable());
		asrt(tehai2.getAnkanableIndexList().size() == 1);
		asrt(tehai2.getAnkanableIndexList().get(0).size() == 4);
		asrt(tehai2.getAnkanableIndexList().get(0).get(0) == 3);
		asrt(tehai2.getAnkanableIndexList().get(0).get(1) == 4);
		asrt(tehai2.getAnkanableIndexList().get(0).get(2) == 5);
		asrt(tehai2.getAnkanableIndexList().get(0).get(3) == 6);

		asrt(tehai1.isChiable(HaiType.SAN_MAN));
		asrt(tehai1.getChiableHaiList(HaiType.SAN_MAN).size() == 2);
		asrt(tehai1.getChiableHaiList(HaiType.SAN_MAN).get(0).size() == 2);
		asrt(tehai1.getChiableHaiList(HaiType.SAN_MAN).get(1).size() == 2);

		TehaiList tehai01 = new TehaiList(tehai0);
		tehai01.add(MajanHai.HATI_SOU);

		asrt(AgariFunctions.isNMentu1Janto(tehai01));

		System.out.println(tehai1.getChiableHaiList(HaiType.SAN_MAN));
		CheckerParam param = new CheckerParam();

		param.setNaki(false);
		param.setBakaze(Kaze.NAN);
		param.setJikaze(Kaze.TON);
		param.setRule(new Rule());

		asrt(AgariFunctions.isTenpai(tehai0, new HurohaiList(0), param));

		asrt(AgariFunctions.getReachableIndexList(tehai0, MajanHai.ITI_PIN, param).size() != 0);
		asrt(AgariFunctions.getReachableIndexList(tehai4, MajanHai.YO_SOU, param).size() != 0);

		param.setFlagCheckYakuSet(new HashSet<Yaku>(0));
		param.setTsumo(true);
		asrt(AgariFunctions.getAgariHaiTypeList(tehai3, new HurohaiList(), param).size() == 5);
		
		assert(!HaiType.GO_MAN.isYaotyuhai());
		assert(HaiType.GO_MAN.isTyuntyanhai());
		assert(HaiType.KYU_SOU.isYaotyuhai());
		assert(!HaiType.KYU_SOU.isTyuntyanhai());
		assert(HaiType.TON.isYaotyuhai());
		assert(!HaiType.TON.isTyuntyanhai());
		
		System.out.println("assert complete!");
	}
}
