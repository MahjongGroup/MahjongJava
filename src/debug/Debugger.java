package debug;


import java.util.ArrayList;
import java.util.List;

import system.Hai;
import system.MajanHai;

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
	}
}
