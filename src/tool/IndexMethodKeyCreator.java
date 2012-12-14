package tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.Combination;

/**
 * インデックス法で用いるキーマップを生成する
 */
public class IndexMethodKeyCreator {
	public static void main(String[] args) {
		System.out.println(Combination.calc(34, 12));
		System.exit(1);
		Itr itr = new Itr();
		int arr[] = null;
		Set<Integer> set = new HashSet<Integer>();

		while ((arr = itr.next()) != null) {
						System.out.println(Arrays.toString(arr));
			//			IndexMethod.calkKey(arr);
			//			set.add(IndexMethod.calkKey(arr));
		}

		//		System.out.println(set.size());
	}

	static class Itr {
		int arr[] = new int[37];
		List<Integer> search;
		int n_size;
		int kindIndex;
		Combination<Integer> c;
		public static final List<Integer> indexList;
		public static final Combination<Integer> c14;
		public static final Combination<Integer> c13;
		public static final Combination<Integer> c12;
		public static final Combination<Integer> c11;
		
		
		static {
			List<Integer> list = new ArrayList<Integer>();
			for (int i = 1; i <= 36; i++) {
				if(i == 10 || i == 20)
					continue;
				list.add((Integer)i);
			}
			indexList = Collections.unmodifiableList(list);
			c11 = new Combination<Integer>(indexList, 11);
			c12 = new Combination<Integer>(indexList, 12);
			c13 = new Combination<Integer>(indexList, 13);
			c14 = new Combination<Integer>(indexList, 14);
		}
		

		public Itr() {
			Arrays.fill(arr, 0);
			n_size = 1;
			c = c14;
		}

		public boolean nextKind() {
			if(kindIndex >= c.size())
				return false;
			search = c.get(kindIndex++);
			return true;
		}
		
		public boolean isValidArray() {
			int size = 0;
			for (int i : search) {
				if(arr[i] == 0)
					continue;
				size += arr[i];
				if (size > 14) {
					return false;
				}
			}
			return (size % 4) == 2;
		}

		public int[] next() {
			while (add(1)) {
				if (isValidArray()) {
					return arr;
				}
			}
			if(nextKind()) {
				return next();
			}
			n_size++;
			switch(n_size) {
			case 2:
				c = c13;
				break;
			case 3:
				c = c12;
				break;
			case 4:
				c = c11;
				break;
			}
			if(n_size <= 4) {
				return next();
			}
			return null;
		}

		public boolean add(int i) {
			if (i == 37)
				return false;
			if (i == 10 || i == 20)
				return add(i + 1);
			if (!search.contains(i))
				return add(i + 1);

			arr[i] += 1;
			if (arr[i] > n_size) {
				arr[i] = 0;
				return add(i + 1);
			}
			return true;
		}
	}
}
