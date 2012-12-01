package system;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

/**
 * 手牌を表すリストクラス。
 */
public class TehaiList implements List<Hai> {
	private final List<Hai> list;

	public TehaiList() {
		this.list = new ArrayList<Hai>();
	}

	public TehaiList(int cap) {
		this.list = new ArrayList<Hai>(cap);
	}

	public TehaiList(TehaiList list) {
		this.list = new ArrayList<Hai>(list);
	}

	public TehaiList(Collection<? extends Hai> c) {
		this.list = new ArrayList<Hai>(c);
	}
	
	/**
	 * 
	 * @param index
	 * @param hai
	 * @return
	 */
	public Hai swap(int index, Hai hai){
		list.add(index, hai);
		return list.remove(index + 1);
	}

	/**
	 * 指定された牌でポンできる手牌の場合trueを返す。
	 * 
	 * @param suteHai ポンする牌
	 * @return ポンできる手牌の場合true
	 */
	public boolean isPonable(HaiType type) {
		int size = Functions.sizeOf(type, this);
		return size >= 2;
	}

	/**
	 * ポン可能な牌インデックスリストのリストを返す。
	 * 
	 * @param ponHaiType ポンする牌の種類
	 * @return　ポン可能な牌インデックスリストのリスト
	 */
	public List<List<Integer>> getPonableIndexList(HaiType ponHaiType) {
		// TODO 実装変更する(4枚以上のときにも対応する)
		assert isPonable(ponHaiType);
		List<List<Integer>> result = new ArrayList<List<Integer>>(2);
		int size = Functions.sizeOf(ponHaiType, this);
		if (size == 2) {
			List<Integer> list = new ArrayList<Integer>(2);
			for (int i = 0; i < size(); i++) {
				Hai hai = get(i);
				if (hai.type() == ponHaiType) {
					list.add(i);
				}
			}
			result.add(list);
		} else if (size == 3) {
			int haiArray[] = new int[3];
			int index = 0;
			for (int i = 0; i < size(); i++) {
				Hai hai = get(i);
				if (hai.type() == ponHaiType) {
					haiArray[index++] = i;
				}
			}

			result.add(getList(haiArray[0], haiArray[1]));
			result.add(getList(haiArray[0], haiArray[2]));
			result.add(getList(haiArray[1], haiArray[2]));
		}

		return result;
	}

	private List<Integer> getList(int hai0, int hai1) {
		List<Integer> result = new ArrayList<Integer>(2);
		result.add(hai0);
		result.add(hai1);
		return result;
	}

	/**
	 * 指定された牌種で明槓できる場合trueを返す。
	 * 
	 * @param type 牌の種類
	 * @return 指定された牌種で明槓できる場合true
	 */
	public boolean isMinkanable(HaiType type) {
		int size = Functions.sizeOf(type, this);
		return size >= 3;
	}

	/**
	 * 指定された牌タイプで明槓できる牌のインデックスのリストを返す.
	 * 
	 * @param type 明槓する牌タイプ.
	 * @return 明槓できる牌のインデックスのリスト.
	 * @throws IllegalArgumentException 明槓できない牌タイプの場合.
	 */
	public List<Integer> getMinkanableIndexList(HaiType type) {
		List<Integer> ret = new ArrayList<Integer>(3);
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).type() == type) {
				ret.add(i);
			}
		}
		if (ret.size() != 3)
			throw new IllegalArgumentException("明槓できない牌タイプに対してこのメソッドを呼び出せません:" + type);
		return ret;
	}

	/**
	 * 指定された牌種で暗槓できる場合trueを返す.指定されたツモ牌も考慮に入れる.
	 * 
	 * @param type 暗槓できるか確かめたい牌の種類.
	 * @return　暗槓できる場合true.
	 */
	public boolean isAnkanable(Hai tsumohai, HaiType type) {
		List<Hai> list = new ArrayList<Hai>(this);
		list.add(tsumohai);
		int size = Functions.sizeOf(type, list);
		return size == 4;
	}

	/**
	 * 手牌に暗槓可能な牌があればtrueを返す.指定されたツモ牌も考慮に入れる.
	 * 
	 * @param tsumohai ツモ牌.
	 * @return　手牌に暗槓可能な牌があればtrue
	 */
	public boolean isAnkanable(Hai tsumohai) {
		for (HaiType type : toHaiTypeSet()) {
			if (isAnkanable(tsumohai, type)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 暗槓可能な牌のインデックスリストを返す.ツモ牌も考慮に入れる.
	 * 
	 * @param tsumohai ツモ牌.
	 * @return 暗槓可能な牌のインデックスリスト(13はツモ牌を表す).
	 */
	public List<List<Integer>> getAnkanableIndexList(Hai tsumohai) {
		List<List<Integer>> result = new ArrayList<List<Integer>>();

		for (HaiType type : toHaiTypeSet()) {
			if (isAnkanable(tsumohai, type)) {
				List<Integer> l = new ArrayList<Integer>(4);
				for (int i = 0; i < size(); i++) {
					if (get(i).type() == type) {
						l.add(i);
					}
				}
				// ツモ牌の場合は13をaddする.
				if (tsumohai.type() == type)
					l.add(13);
				result.add(l);
			}
		}
		return result;
	}

	/**
	 * 指定された牌でチーできる牌のインデックスリストを返す。
	 * 
	 * @param haiType 牌の種類
	 * @return　牌のインデックスリスト
	 */
	public List<List<Integer>> getChiableHaiList(HaiType haiType) {
		// to be changed

		if (haiType.group2() != HaiGroup2.SU)
			return null;

		SuType suType = haiType.suType();
		List<Hai> list = Functions.extract(this, suType);

		int number = haiType.number();

		boolean bp1 = false;
		boolean bp2 = false;
		boolean bm1 = false;
		boolean bm2 = false;

		Hai haiP1 = null;
		Hai haiP2 = null;
		Hai haiM1 = null;
		Hai haiM2 = null;

		for (Hai hai : list) {
			int n = hai.type().number();
			if (n + 2 == number) {
				bm2 = true;
				haiM2 = hai;
			} else if (n + 1 == number) {
				bm1 = true;
				haiM1 = hai;
			} else if (n - 1 == number) {
				bp1 = true;
				haiP1 = hai;
			} else if (n - 2 == number) {
				bp2 = true;
				haiP2 = hai;
			}
		}

		List<List<Integer>> result = new ArrayList<List<Integer>>(3);

		if (bm2 && bm1) {
			List<Integer> l = new ArrayList<Integer>(2);
			l.add(indexOf(haiM2));
			l.add(indexOf(haiM1));
			result.add(l);
		}
		if (bm1 && bp1) {
			List<Integer> l = new ArrayList<Integer>(2);
			l.add(indexOf(haiM1));
			l.add(indexOf(haiP1));
			result.add(l);
		}
		if (bp1 && bp2) {
			List<Integer> l = new ArrayList<Integer>(2);
			l.add(indexOf(haiP1));
			l.add(indexOf(haiP2));
			result.add(l);
		}
		return result;
	}

	public boolean isChiable(HaiType haiType) {
		if (haiType.group2() != HaiGroup2.SU)
			return false;

		SuType suType = haiType.suType();
		List<Hai> list = Functions.extract(this, suType);

		int number = haiType.number();

		boolean bp1 = false;
		boolean bp2 = false;
		boolean bm1 = false;
		boolean bm2 = false;

		for (Hai hai : list) {
			int n = hai.type().number();
			if (n + 2 == number) {
				bm2 = true;
			} else if (n + 1 == number) {
				bm1 = true;
			} else if (n - 1 == number) {
				bp1 = true;
			} else if (n - 2 == number) {
				bp2 = true;
			}
		}

		if (bm1 && (bm2 || bp1))
			return true;
		if (bp1 && bp2)
			return true;
		return false;
	}
	
	/**
	 * 指定された牌種のうちインデックスが一番若い牌をリストから削除してそれを返す。
	 * 
	 * @param type　削除する牌の種類
	 * @return　削除された牌
	 */
	public Hai remove(HaiType type) {
		for (Hai hai : this) {
			if (hai.type() == type) {
				remove(hai);
				return hai;
			}
		}
		return null;
	}

	/**
	 * この手牌リストを牌タイプのリストに変換する.
	 * 
	 * @return 変換された牌タイプのリスト.
	 */
	public List<HaiType> toHaiTypeList() {
		List<HaiType> result = new ArrayList<HaiType>(this.list.size());
		for (Hai hai : this.list) {
			result.add(hai.type());
		}
		return result;
	}

	/**
	 * この手牌リストを牌タイプのセットに変換する.例えば [一萬、一萬、二萬、東、東、東、撥]というリストがあれば、[一萬、二萬、東、撥]という
	 * セットが返ってくる.
	 * 
	 * @return 牌タイプのセット.
	 */
	public Set<HaiType> toHaiTypeSet() {
		Set<HaiType> result = new HashSet<HaiType>();
		for (Hai hai : this.list) {
			result.add(hai.type());
		}
		return result;
	}

	/**
	 * この手牌リストの中に含まれる指定された種類の牌の数を返す.
	 * 
	 * @param type 数が知りたい牌の種類
	 * @return 指定された種類の牌の数
	 */
	public int sizeOf(HaiType type) {
		int count = 0;

		for (Hai h : this.list) {
			HaiType t = h.type();
			if (type == t)
				count++;
		}
		return count;
	}

	// override method ----------------------------------------------------

	@Override
	public boolean add(Hai hai) {
		return list.add(hai);
	}

	@Override
	public void add(int index, Hai hai) {
		list.add(index, hai);
	}

	@Override
	public boolean addAll(Collection<? extends Hai> c) {
		return list.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends Hai> c) {
		return list.addAll(index, c);
	}

	@Override
	public void clear() {
		list.clear();
	}

	@Override
	public boolean contains(Object hai) {
		return list.contains(hai);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	@Override
	public Hai get(int index) {
		return list.get(index);
	}

	@Override
	public int indexOf(Object hai) {
		return list.indexOf(hai);
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public Iterator<Hai> iterator() {
		return list.iterator();
	}

	@Override
	public int lastIndexOf(Object hai) {
		return list.lastIndexOf(hai);
	}

	@Override
	public ListIterator<Hai> listIterator() {
		return list.listIterator();
	}

	@Override
	public ListIterator<Hai> listIterator(int index) {
		return list.listIterator(index);
	}

	@Override
	public boolean remove(Object hai) {
		return list.remove(hai);
	}

	@Override
	public Hai remove(int index) {
		return list.remove(index);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return list.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}

	@Override
	public Hai set(int index, Hai hai) {
		return list.set(index, hai);
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public List<Hai> subList(int head, int tail) {
		return list.subList(head, tail);
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] array) {
		return list.toArray(array);
	}

	@Override
	public String toString() {
		return list.toString();
	}

}