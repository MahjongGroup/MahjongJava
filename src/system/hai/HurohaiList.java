package system.hai;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import system.hai.Mentsu.Type;


/**
 * 副露牌を表すリストクラス。
 * 
 * @author kohei
 *
 */
public class HurohaiList implements List<Mentsu>{
	private final List<Mentsu> list;
	
	public HurohaiList() {
		this.list = new ArrayList<Mentsu>();
	}

	public HurohaiList(int cap) {
		this.list = new ArrayList<Mentsu>(cap);
	}

	
	public HurohaiList(HurohaiList list) {
		this.list = new ArrayList<Mentsu>(list);
	}

	public boolean isKakan(HaiType type) {
		for (Mentsu m : list) {
			if(m.type() == Type.KOTU && m.get(0).type() == type){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 指定された牌で加槓する。
	 * 
	 * @param hai 加槓する牌
	 * @return 加槓して出来た面子
	 */
	public Mentsu doKakan(Hai hai) {
		Mentsu addMentu = null;
		for (Iterator<Mentsu> itr = this.iterator(); itr.hasNext();) {
			Mentsu m = itr.next();
			if(m.type() == Type.KOTU && m.get(0).type() == hai.type()){
				itr.remove();
				addMentu = m.doKakan(hai);
				break;
			}
		}
		this.add(addMentu);
		return addMentu;
	}

	public Mentsu getKotu(HaiType type) {
		for (Mentsu m : list) {
			if(m.type() == Type.KOTU && m.get(0).type() == type){
				return m;
			}
		}
		return null;
	}
	
	public boolean isNaki() {
		for (Mentsu mentu: this) {
			if(mentu.isNaki())
				return true;
		}
		return false;
	}
	
	
	@Override
	public boolean add(Mentsu mentu) {
		return list.add(mentu);
	}

	@Override
	public void add(int index, Mentsu mentu) {
		list.add(index, mentu);
	}

	@Override
	public boolean addAll(Collection<? extends Mentsu> c) {
		return list.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends Mentsu> c) {
		return list.addAll(index, c);
	}

	@Override
	public void clear() {
		list.clear();
	}

	@Override
	public boolean contains(Object mentu) {
		return list.contains(mentu);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	@Override
	public Mentsu get(int index) {
		return list.get(index);
	}

	@Override
	public int indexOf(Object mentu) {
		return list.indexOf(mentu);
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public Iterator<Mentsu> iterator() {
		return list.iterator();
	}

	@Override
	public int lastIndexOf(Object mentu) {
		return list.lastIndexOf(mentu);
	}

	@Override
	public ListIterator<Mentsu> listIterator() {
		return list.listIterator();
	}

	@Override
	public ListIterator<Mentsu> listIterator(int index) {
		return list.listIterator(index);
	}

	@Override
	public boolean remove(Object mentu) {
		return list.remove(mentu);
	}

	@Override
	public Mentsu remove(int index) {
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
	public Mentsu set(int index, Mentsu mentu) {
		return list.set(index, mentu);
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public List<Mentsu> subList(int head, int tail) {
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
