package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * コンビネーションを表す．
 *
 * @param <T> コンビネーションの要素．
 */
public class Combination<T> implements List<List<T>>{
	private final List<List<T>> list;
	private final int size;

	/**
	 * コンビネーションを生成するコンストラクタ．
	 * @param elems 要素のコレクション．
	 * @param size nCrのrの部分．
	 */
	public Combination(Collection<T> elems, int size) {
		this.size = size;
		if(elems.size() < this.size) {
			throw new IllegalArgumentException();
		}
		List<T> elemArray = new ArrayList<T>(elems);
		this.list = build_n(elemArray, 0, this.size);
	}

	/**
	 * elemListのcurからleftSize個の要素を取り出したときのすべての通りのリストを返す．
	 * @param elemList 要素のリスト
	 * @param list 
	 * @param cur 現在のカーソル
	 * @param leftSize のこりサイズ
	 * @return
	 */
	private List<List<T>> build_n(List<T> elemList, int cur, int leftSize) {
		List<List<T>> retList = new ArrayList<List<T>>();
		if (leftSize == 0){
			retList.add(new ArrayList<T>());
			return retList;
		}
		for (int i = cur; i < elemList.size() - (leftSize - 1); i++) {
			if (elemList.size() - (i + 1) >= leftSize - 1) {
				List<List<T>> list_next = build_n(elemList, i+1, leftSize - 1);
				T elem = elemList.get(i);
				for (List<T> list3 : list_next) {
					list3.add(elem);
					retList.add(list3);
				}
			}
		}
		return retList;
	}
	
	/**
	 * nCrの値を計算してそれを返す．
	 * @param n nCrのnの値．
	 * @param m nCrのmの値．
	 * @return nCrの値．
	 */
	public static int calc(int n, int m) {
		if(m == 0) {
			return 1;
		}
		if(m == 1) {
			return n;
		}
		if(n < 2 * m) {
			return calc(n, n - m);
		}
		return n * calc(n - 1, m - 1) / m;
	}

	
	@Override
	public String toString() {
		return list.toString();
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}

	@Override
	public Iterator<List<T>> iterator() {
		return list.iterator();
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}

	@Override
	public boolean add(List<T> e) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends List<T>> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(int index, Collection<? extends List<T>> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<T> get(int index) {
		return list.get(index);
	}

	@Override
	public List<T> set(int index, List<T> element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(int index, List<T> element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<T> remove(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	@Override
	public ListIterator<List<T>> listIterator() {
		return list.listIterator();
	}

	@Override
	public ListIterator<List<T>> listIterator(int index) {
		return list.listIterator(index);
	}

	@Override
	public List<List<T>> subList(int fromIndex, int toIndex) {
		return list.subList(fromIndex, toIndex);
	}
}
