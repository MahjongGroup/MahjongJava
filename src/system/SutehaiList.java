package system;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * 捨牌を表すリストクラス。
 * 
 * @author kohei
 *
 */
public class SutehaiList implements List<Sutehai> {
	private final List<Sutehai> list;

	public SutehaiList() {
		this.list = new ArrayList<Sutehai>();
	}

	public SutehaiList(int cap) {
		this.list = new ArrayList<Sutehai>(cap);
	}

	public SutehaiList(SutehaiList list) {
		this.list = new ArrayList<Sutehai>(list);
	}

	/**
	 * この捨て牌リストを鳴かれた牌を除いた牌リストに変換して、それを返す．
	 * @return 鳴かれた牌を除いた牌リスト．
	 */
	public List<Hai> toNakiExcludedHaiList() {
		List<Hai> ret = new ArrayList<Hai>();
		for (Sutehai hai : list) {
			if (!hai.isNaki())
				ret.add(MajanHai.valueOf(hai.type(), hai.aka()));
		}
		return ret;
	}

	@Override
	public boolean add(Sutehai hai) {
		return list.add(hai);
	}

	@Override
	public void add(int index, Sutehai hai) {
		list.add(index, hai);
	}

	@Override
	public boolean addAll(Collection<? extends Sutehai> c) {
		return list.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends Sutehai> c) {
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
	public Sutehai get(int index) {
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
	public Iterator<Sutehai> iterator() {
		return list.iterator();
	}

	@Override
	public int lastIndexOf(Object hai) {
		return list.lastIndexOf(hai);
	}

	@Override
	public ListIterator<Sutehai> listIterator() {
		return list.listIterator();
	}

	@Override
	public ListIterator<Sutehai> listIterator(int index) {
		return list.listIterator(index);
	}

	@Override
	public boolean remove(Object hai) {
		return list.remove(hai);
	}

	@Override
	public Sutehai remove(int index) {
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
	public Sutehai set(int index, Sutehai hai) {
		return list.set(index, hai);
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public List<Sutehai> subList(int head, int tail) {
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