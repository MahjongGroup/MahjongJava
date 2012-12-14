package tool;

import java.util.ArrayList;
import java.util.List;

public class Nmrs {
	public static void main(String[] args) {
	}
	
	private List<List<Integer>> build_n(List<Integer> elemList, int cur, int leftSize) {
		List<List<Integer>> retList = new ArrayList<List<Integer>>();
		if (leftSize == 0){
			retList.add(new ArrayList<Integer>());
			return retList;
		}
		
		for (int i = cur; i < elemList.size() - (leftSize - 1); i++) {
			if (elemList.size() - (i + 1) >= leftSize - 1) {
				List<List<Integer>> list_next = build_n(elemList, i+1, leftSize - 1);
				Integer elem = elemList.get(i);
				for (List<Integer> list3 : list_next) {
					list3.add(elem);
					retList.add(list3);
				}
			}
		}
		return retList;
	}
}
