package test.system.hai;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 牌の種類を表す列挙型.
 * 牌の種類には数牌、字牌の２タイプが存在する．
 * 数牌はさらに萬子、筒子、索子の３タイプに分かれる．各３つのタイプの数牌は属性として1～9の数値を持つ．
 * 字牌は風牌、三元牌の２タイプに分かれる．
 * 風牌は東、南、西、北の４つに分かれる．
 * 三元牌は白、撥、中の３つに分かれる．
 * それぞれはIDを持つ．IDは固定されており変更されることはないものとする．
 */
public enum HaiType{
	ITI_MAN("一萬", 1),
	NI_MAN("二萬", 2),
	SAN_MAN("三萬", 3),
	YO_MAN("四萬", 4),
	GO_MAN("五萬", 5),
	ROKU_MAN("六萬", 6),
	NANA_MAN("七萬", 7),
	HATI_MAN("八萬", 8),
	KYU_MAN("九萬", 9),
	ITI_PIN("一筒", 11),
	NI_PIN("二筒", 12),
	SAN_PIN("三筒", 13),
	YO_PIN("四筒", 14),
	GO_PIN("五筒", 15),
	ROKU_PIN("六筒", 16),
	NANA_PIN("七筒", 17),
	HATI_PIN("八筒", 18),
	KYU_PIN("九筒", 19),
	ITI_SOU("一索", 21),
	NI_SOU("二索", 22),
	SAN_SOU("三索", 23),
	YO_SOU("四索", 24),
	GO_SOU("五索", 25),
	ROKU_SOU("六索", 26),
	NANA_SOU("七索", 27),
	HATI_SOU("八索", 28),
	KYU_SOU("九索", 29),
	TON("東", 30),
	NAN("南", 31),
	SYA("西", 32),
	PE("北", 33),
	HAKU("白", 34),
	HATU("撥", 35),
	TYUN("中", 36);

	private final String notation;
	private final int id;

	private HaiType(String notation, int id) {
		this.notation = notation;
		this.id = id;
	}

	/**
	 * この牌タイプを表す日本語文字列を返す(例えば"一萬","東"など).
	 * @return この牌タイプの日本語文字列.
	 */
	public String notation() {
		return notation;
	}

	/**
	 * この牌タイプのID(整数値)を返す.このIDは牌タイプごとにユニークな値である.
	 * @return この牌の種類のID(整数値).
	 */
	public int id() {
		return id;
	}

	/**
	 * ヤオチュー牌の場合trueを返す.
	 * @return ヤオチュー牌の場合true.
	 */
	public boolean isYaotyuhai() {
		if (isTsuhai())
			return true;
		if (number() == 1 || number() == 9)
			return true;
		return false;
	}

	/**
	 * チュンチャン牌の場合falseを返す.
	 * @return チュンチャン牌の場合false.
	 */
	public boolean isTyuntyanhai() {
		if (this.isTsuhai())
			return false;

		int num = number();
		if (isSuhai() && 2 <= num && num <= 8)
			return true;
		return false;
	}

	/**
	 * この牌タイプが数牌の場合trueを返す.
	 * このメソッドとisTsuhai()メソッドは対称なものであり,isTsuhai() == !isSuhai() が常に成り立つ.
	 * 
	 * @return 数牌の場合true.
	 */
	public boolean isSuhai() {
		if (id < 30)
			return true;
		return false;
	}

	/**
	 * この牌タイプが字牌の場合trueを返す.
	 * このメソッドとisSuhai()メソッドは対称なものであり,isTsuhai() == !isSuhai() が常に成り立つ.
	 * 
	 * @return 字牌の場合true.
	 */
	public boolean isTsuhai() {
		if (id < 30)
			return false;
		return true;
	}

	/**
	 * この牌タイプが三元牌の場合trueを返す.
	 * @return 三元牌の場合true.
	 */
	public boolean isSangenhai() {
		return id == 34 || id == 35 || id == 36;
	}
	
	/**
	 * この牌タイプが風牌の場合trueを返す.
	 * @return この牌タイプが三元牌の場合true
	 */
	public boolean isKazehai() {
		return 30 <= id && id <= 33;
	}

	/**
	 * この牌タイプが数牌の場合,その数字を返す.数牌でない場合は例外を投げる.
	 * @return この数牌の数字.
	 * @throws UnsupportedOperationException 数牌ではない牌タイプに対してこのメソッドを呼び出した場合.
	 */
	public int number() {
		if (id % 10 == 0)
			return 5;
		if (id < 30)
			return id % 10;

		throw new UnsupportedOperationException(UNSUPPORTED_ERROR_MESSAGE);
	}

	/**
	 * この牌タイプが風牌の場合,その風を返す.風牌でない場合は例外を投げる.
	 * @return この風牌の風.
	 * @throws UnsupportedOperationException 風牌でない牌タイプに対してこのメソッドを呼び出した場合.
	 */
	public Kaze kaze() {
		switch (this) {
		case TON:
			return Kaze.TON;
		case NAN:
			return Kaze.NAN;
		case SYA:
			return Kaze.SYA;
		case PE:
			return Kaze.PE;
		default:
			break;
		}
		throw new UnsupportedOperationException(UNSUPPORTED_ERROR_MESSAGE);
	}

	/**
	 * この牌タイプが三元牌の場合,三元牌の種類(白,撥,中)を返す.三元牌でない場合は例外を投げる.
	 * @return 三元牌の種類(白,撥,中).
	 * @throws 三元牌でない牌タイプに対してこのメソッドを呼び出した場合.
	 */
	public SangenType sangenType() {
		switch (this) {
		case HAKU:
			return SangenType.HAKU;
		case HATU:
			return SangenType.HATU;
		case TYUN:
			return SangenType.TYUN;
		default:
			break;
		}
		throw new UnsupportedOperationException(UNSUPPORTED_ERROR_MESSAGE);
	}

	/**
	 * この牌タイプが数牌の場合,その種類(萬子,筒子,索子)を返す.この牌タイプが数牌でない場合は例外を投げる.
	 * @return 数牌の種類(萬子,筒子,索子).
	 * @throws UnsupportedOperationException この牌タイプが数牌でない場合.
	 */
	public SuType suType() {
		if (id < 10)
			return SuType.MAN;
		if (id < 20)
			return SuType.PIN;
		if (id < 30)
			return SuType.SOU;
		throw new UnsupportedOperationException(UNSUPPORTED_ERROR_MESSAGE);
	}
	
	/**
	 * この牌タイプのドラの牌タイプを表す．
	 * @return この牌タイプのドラの牌タイプを表す．
	 */
	public HaiType nextOfDora() {
		if(isSuhai()) {
			int num = this.number();
			num = (++num == 10 ? num - 9 : num);
			return valueOf(this.suType(), num);
		}
		if(isSangenhai()) {
			switch(sangenType()) {
			case HAKU:
				return HATU;
			case HATU:
				return TYUN;
			case TYUN:
				return HAKU;
			}
			throw new IllegalStateException();
		}
		return valueOf(kaze().simo());
	}

	/**
	 * 指定された数牌の種類と数字を持つ,数牌の牌タイプを返す.もし,そのような牌タイプが存在しない場合は
	 * nullを返す.
	 * @param suType 数牌の種類(萬子，筒子,索子).
	 * @param number 数牌の数字.
	 * @return 指定された数牌の牌タイプ.そのような牌タイプが存在しない場合はnull.
	 */
	public static HaiType valueOf(SuType suType, int number) {
		for (HaiType ht : values()) {
			if (ht.isSuhai() && ht.suType() == suType && ht.number() == number) {
				return ht;
			}
		}
		return null;
	}
	
	public static HaiType valueOf(int id) {
		for (HaiType ht : values()) {
			if (ht.id() == id) {
				return ht;
			}
		}
		return null;
	}

	public static HaiType valueOf(Kaze kaze) {
		for (HaiType ht : values()) {
			if (ht.isKazehai() && kaze == ht.kaze()) {
				return ht;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return notation;
	}

	public static final String UNSUPPORTED_ERROR_MESSAGE = "この牌に対してこのメソッドはサポートされてません";

	/**
	 * 指定された牌コレクションを牌の種類リストに変換してそれを返す。
	 * 
	 * @param c 変換したい牌コレクション
	 * @return 変換した牌の種類リスト
	 */
	public static List<HaiType> toHaiTypeList(Collection<? extends Hai> c) {
		List<HaiType> result = new ArrayList<HaiType>(c.size());
		for (Hai hai : c) {
			result.add(hai.type());
		}
		return result;
	}

	/**
	 * 牌コレクションから牌の種類ごとに1牌だけを入れた牌の種類セットを返す。例えば
	 * [一萬,一萬,二萬,東,東,東,撥]というリストがあれば,[一萬,二萬,東,撥]という
	 * セットが返ってくる。
	 * 
	 * @param c 牌コレクション
	 * @return 牌の種類セット
	 */
	public static Set<HaiType> toHaiTypeSet(Collection<? extends Hai> c) {
		Set<HaiType> result = new HashSet<HaiType>();
		for (Hai hai : c) {
			result.add(hai.type());
		}
		return result;
	}

	/**
	 * 指定された牌タイプのリストを牌タイプセットに変換する.
	 * @param c
	 * @return
	 */
	public static Set<HaiType> toHaiTypeSet(List<HaiType> c) {
		Set<HaiType> result = new HashSet<HaiType>();
		for (HaiType type : c) {
			result.add(type);
		}
		return result;
	}

	/**
	 * 牌の種類をソートするためのコンパレーターオブジェクトの列挙型.
	 */
	public static enum HaiComparator implements Comparator<HaiType> {
		/**
		 * 牌タイプを昇順に並び替えるためのコンパレーター.
		 */
		ASCENDING_ORDER {
			@Override
			public int compare(HaiType t0, HaiType t1) {
				return t0.id - t1.id;
			}
		},

		/**
		 * 牌タイプを降順に並び替えるためのコンパレーター.
		 */
		DESCENDING_ORDER {
			@Override
			public int compare(HaiType t0, HaiType t1) {
				return t1.id - t1.id;
			}
		}
	}
}
