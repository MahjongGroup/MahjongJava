package system.hai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import system.Functions;

/**
 * 面子を表すクラス.面子は３枚か４枚の牌で構成される.
 * 面子は次の７つの種類に大別出来る.
 * 　明順子,明刻,明槓子(明槓),明槓子(加槓),
 * 　暗順子,暗刻,暗槓子
 * 上記のように,このオブジェクトは鳴き面子以外にも用いられる(上がり判定時などに使用).
 * 
 * この面子の構成牌は昇順にソートされていることが保証される．
 */
public class Mentsu {
	/**
	 * 面子を構成する面子牌クラス.この牌が鳴かれたのかどうか,という情報を持つ.例えば
	 * 1萬,2萬という形から3萬を鳴いた場合,[1萬,2萬,3萬]という面子オブジェクトを生成できるが
	 * 3萬が鳴かれてこの面子が出来たので,3萬は鳴かれたという情報を持つことになる. 
	 */
	public static class MentsuHai extends AbstractMajanHai {
		private final boolean naki;

		/**
		 * 面子牌を生成するコンストラクタ.
		 * @param hai 牌.
		 * @param naki 鳴いている場合true.
		 */
		public MentsuHai(Hai hai, boolean naki) {
			super(hai);
			this.naki = naki;
		}

		/**
		 * 鳴かれた牌の場合trueを返す.
		 * @return 鳴かれた牌の場合true.
		 */
		public boolean isNaki() {
			return naki;
		}

		@Override
		public String toString() {
			if (naki) {
				return hai + "(鳴き)";
			}
			return hai.notation();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + (naki ? 1231 : 1237);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (!super.equals(obj))
				return false;
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (!(obj instanceof MentsuHai))
				return false;
			MentsuHai other = (MentsuHai) obj;
			if (naki != other.naki)
				return false;
			return true;
		}
		

	}

	/**
	 * 面子の種類を表す列挙体.順子,刻子,槓子の3種類が存在する.
	 */
	public static enum Type {
		SYUNTU("順子", 0),
		KOTU("刻子", 2),
		KANTU("樌子", 8);

		private final String notation;
		private final int hu;

		private Type(String n, int hu) {
			this.notation = n;
			this.hu = hu;
		}

		/**
		 * この面子タイプの基本符を返す.
		 * @return 基本符.
		 */
		public int hu() {
			return hu;
		}

		/**
		 * この面子タイプを表す日本語文字列("順子","刻子","槓子")を返す.
		 * @return この面子タイプを表す日本語文字列.
		 */
		public String notation() {
			return notation;
		}

		@Override
		public String toString() {
			return notation;
		}
	}

	private final List<MentsuHai> haiList;
	private final Kaze kaze;
	private final boolean naki;

	private final Type type;

	private final boolean kakanFlag;

	/**
	 * 指定された牌で鳴きなしの面子を生成するコンストラクタ.
	 * 
	 * @param hais 面子を構成する牌(可変長引数).
	 * @throws IllegalArgumentException 指定された牌で面子が構成できない場合.
	 */
	public Mentsu(Hai... hais) {
		this.type = Functions.getMentuType(hais);
		if (this.type == null) {
			throw new IllegalArgumentException("指定された牌では面子が構成できない：" + Arrays.toString(hais));
		}

		this.haiList = new ArrayList<MentsuHai>(hais.length);
		for (Hai hai : hais) {
			this.haiList.add(new MentsuHai(hai, false));
		}
		this.kaze = null;
		this.naki = false;
		this.kakanFlag = false;
		Collections.sort(haiList, Hai.HaiComparator.ASCENDING_ORDER);
	}

	/**
	 * 指定された牌で鳴き面子を構成する.
	 * 
	 * @param nakihai 鳴いた牌.
	 * @param kaze 鳴いた場所(風).
	 * @param hais 面子を構成する牌(可変長引数).
	 * @throws IllegalArgumentException 指定された牌で面子が構成できない場合.
	 */
	public Mentsu(Hai nakihai, Kaze kaze, Hai... hais) {
		Hai haiArray[] = new Hai[hais.length + 1];
		System.arraycopy(hais, 0, haiArray, 0, hais.length);
		haiArray[hais.length] = nakihai;

		this.type = Functions.getMentuType(haiArray);
		if (this.type == null) {
			throw new IllegalArgumentException();
		}

		this.haiList = new ArrayList<MentsuHai>(hais.length);
		for (Hai h : hais) {
			this.haiList.add(new MentsuHai(h, false));
		}
		this.haiList.add(new MentsuHai(nakihai, true));
		this.kaze = kaze;
		this.naki = true;

		this.kakanFlag = false;
		Collections.sort(haiList, Hai.HaiComparator.ASCENDING_ORDER);
	}

	/**
	 * コピーコンストラクタ.
	 * 
	 * @param mentu コピーする面子オブジェクト.
	 */
	public Mentsu(Mentsu mentu) {
		this.haiList = new ArrayList<MentsuHai>(mentu.haiList);
		this.naki = mentu.naki;
		this.type = mentu.type;
		this.kaze = mentu.kaze;
		this.kakanFlag = false;
	}
	
	/**
	 * 指定された面子を指定された牌で鳴かれたことにするコンストラクタ．
	 * @param mentu 面子．
	 * @param kaze どこから鳴いたのか．
	 * @param naki 鳴かれた牌．
	 */
	public Mentsu(Mentsu mentu, Kaze kaze, Hai naki) {
		this.haiList = new ArrayList<MentsuHai>(mentu.haiList);
		this.haiList.remove(new MentsuHai(naki, false));
		this.haiList.add(new MentsuHai(naki, true));
		this.naki = true;
		this.type = mentu.type;
		this.kaze = kaze;
		this.kakanFlag = false;
		Collections.sort(haiList, Hai.HaiComparator.ASCENDING_ORDER);
	}

	// 加槓コンストラクタ.
	private Mentsu(Mentsu mentu, Hai hai) {
		this.haiList = new ArrayList<MentsuHai>(mentu.haiList);
		this.haiList.add(new MentsuHai(hai, false));
		this.naki = mentu.naki;
		this.type = Type.KANTU;
		this.kaze = mentu.kaze;
		this.kakanFlag = true;
		Collections.sort(haiList, Hai.HaiComparator.ASCENDING_ORDER);
	}

	public List<MentsuHai> asList() {
		return new ArrayList<MentsuHai>(haiList);
	}

	/**
	 * この面子の符を計算してそれを返す.
	 * 
	 * @return この面子の符.
	 */
	public int calcHu() {
		int hu = type.hu;
		if (hu == 0)
			return 0;
		return hu * (naki ? 1 : 2) * (haiList.get(0).type().isYaotyuhai() ? 2 : 1);
	}

	public boolean contains(Object hai) {
		return haiList.contains(hai);
	}
	
	/**
	 * この面子が指定された牌種を含んでいる場合trueを返す．
	 * @param type the hai type whose presence in this list to be tested.
	 * @return true if this mentu contains the specified type.
	 */
	public boolean contains(HaiType type) {
		for (Hai hai : haiList) {
			if(hai.type() == type) {
				return true;
			}
		}
		return false;
	}

	public boolean containsAll(Collection<?> c) {
		return haiList.containsAll(c);
	}

	/**
	 * 加樌して出来た面子を返す.
	 * 
	 * @param hai 加樌する牌
	 * @return 加樌して出来た面子
	 * @throws IllegalStateException 刻子以外の面子に対してこのメソッドを呼び出した場合.
	 * @throws IllegalArgumentException 指定された牌で加槓できない場合.
	 */
	public Mentsu doKakan(Hai hai) {
		if (type != Type.KOTU)
			throw new IllegalStateException("この種類の面子に対して加樌は出来ない");
		if (hai.type() != haiList.get(0).type())
			throw new IllegalArgumentException("この牌では加樌できない");
		return new Mentsu(this, hai);
	}

	public MentsuHai get(int index) {
		return haiList.get(index);
	}

	/**
	 * この面子が鳴き面子の場合鳴いた風を返す.この面子が鳴き面子でない場合はnullを返す.
	 * 
	 * @return 鳴いた風.この面子が鳴き面子でない場合はnull.
	 */
	public Kaze getKaze() {
		return this.kaze;
	}

	public int indexOf(Object hai) {
		return haiList.indexOf(hai);
	}

	public boolean isEmpty() {
		return haiList.isEmpty();
	}

	/**
	 * 加槓している場合trueを返す.
	 * @return 加槓している場合true.
	 */
	public boolean isKakan() {
		return this.kakanFlag;
	}

	/**
	 * 鳴き面子の場合trueを返す.
	 * 
	 * @return 鳴き面子の場合true. 
	 */
	public boolean isNaki() {
		return naki;
	}

	public Iterator<MentsuHai> iterator() {
		return haiList.iterator();
	}

	public int lastIndexOf(Object hai) {
		return haiList.lastIndexOf(hai);
	}

	public ListIterator<MentsuHai> listIterator() {
		return haiList.listIterator();
	}

	public ListIterator<MentsuHai> listIterator(int index) {
		return haiList.listIterator(index);
	}

	public boolean retainAll(Collection<?> c) {
		return haiList.retainAll(c);
	}

	public int size() {
		return haiList.size();
	}

	public List<MentsuHai> subList(int head, int tail) {
		return haiList.subList(head, tail);
	}

	public Object[] toArray() {
		return haiList.toArray();
	}

	public <T> T[] toArray(T[] array) {
		return haiList.toArray(array);
	}

	@Override
	public String toString() {
		if (naki) {
			return "明" + type + "(" + kaze + ") :" + haiList;
		}
		return "暗" + type + ":" + haiList;
	}

	/**
	 * 面子の種類(順子,刻子,槓子)を返す.
	 * 
	 * @return 面子の種類.
	 */
	public Type type() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		int sum = 0;
		for (Hai hai : haiList) {
			sum += hai.hashCode();
		}
		result = prime * result + sum;
		result = prime * result + (kakanFlag ? 1231 : 1237);
		result = prime * result + ((kaze == null) ? 0 : kaze.hashCode());
		result = prime * result + (naki ? 1231 : 1237);
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Mentsu))
			return false;
		Mentsu other = (Mentsu) obj;
		if (kakanFlag != other.kakanFlag)
			return false;
		if (kaze != other.kaze)
			return false;
		if (naki != other.naki)
			return false;
		if (type != other.type)
			return false;
		List<Hai> temp = new ArrayList<Hai>(haiList);
		return !temp.retainAll(other.haiList);
	}
	
}
