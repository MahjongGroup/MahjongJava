package util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 2つオブジェクトの対を表す.2つのオブジェクトの順番はない.つまり、例えば整数(Integer)の
 * ペアの場合、(1,2)と(2,1)は同じものを指す.ここでいう同じものとは、equalsメソッドで比較して trueを返すという意味である.
 * 要素となるオブジェクトはnullであってはならない.
 */
public class Pair<E> implements Iterable<E> {
	private final E obj0;
	private final E obj1;

	/**
	 * 2つのオブジェクトのペアをつくる.引数の順番に関係なく同じオブジェクトが生成される.
	 * 
	 * @param obj0 対を作るオブジェクトのうちの1つ.nullであってはならない.
	 * @param obj1 対を作るオブジェクトのうちの1つ.nullであってはならない.
	 */
	public Pair(E obj0, E obj1) {
		if (obj0 == null || obj1 == null)
			throw new NullPointerException();
		this.obj0 = obj0;
		this.obj1 = obj1;
	}

	/**
	 * このペアが指定されたオブジェクトを持っている場合trueを返す.
	 * 
	 * @param obj 持っているか確認するオブジェクト.
	 * @return 持っている場合true.
	 */
	public boolean contains(Object obj) {
		return obj0.equals(obj) || obj1.equals(obj);
	}

	@Override
	public int hashCode() {
		return 31 + obj0.hashCode() + obj1.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Pair))
			return false;
		@SuppressWarnings("rawtypes")
		Pair other = (Pair) obj;
		if (obj0.equals(other.obj0)) {
			return obj1.equals(other.obj1);
		} else {
			return obj0.equals(other.obj1) && obj1.equals(other.obj0);
		}
	}

	@Override
	public String toString() {
		return String.format("(%s,%s)", obj0, obj1);
	}

	@Override
	public Iterator<E> iterator() {
		return new ItrOfPair();
	}

	private class ItrOfPair implements Iterator<E> {
		int cursor; // index of next element to return

		public boolean hasNext() {
			return cursor != 2;
		}

		public E next() {
			if (cursor == 0) {
				cursor++;
				return obj0;
			} else if (cursor == 1) {
				cursor++;
				return obj1;
			} else
				throw new NoSuchElementException();
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

}
