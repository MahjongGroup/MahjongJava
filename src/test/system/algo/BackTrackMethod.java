package test.system.algo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import system.hai.Hai;
import system.hai.HaiType;
import system.hai.SuType;
import system.test.Functions;

/**
 * バックトラック法によるn面子1雀頭の判定アルゴリズムを表すクラス．
 */
public class BackTrackMethod {
	/**
	 * 指定されたリストがn面子1雀頭で構成されている場合trueを返す．
	 * 
	 * @param haiList n面子1雀頭か検査するリスト．
	 * @return 指定されたリストがn面子1雀頭で構成されている場合はtrue．
	 */
	public static boolean isNMentu1Janto(List<? extends Hai> haiList) {
		if (haiList.size() % 3 != 2)
			return false;
		Set<HaiType> haiTypeSet = HaiType.toHaiTypeSet(haiList);
		List<HaiType> haiTypeList = HaiType.toHaiTypeList(haiList);

		for (HaiType haiType : haiTypeSet) {
			List<HaiType> tempList = new ArrayList<HaiType>(haiTypeList);
			if (Functions.sizeOfHaiTypeList(haiType, tempList) >= 2) {
				tempList.remove(haiType);
				tempList.remove(haiType);

				List<HaiType> tempList2 = new ArrayList<HaiType>(tempList);
				removeKotsu(tempList, haiTypeSet);
				removeSyuntsu(tempList);

				if (tempList.size() == 0) {
					return true;
				}

				removeSyuntsu(tempList2);
				removeKotsu(tempList2, haiTypeSet);

				if (tempList2.size() == 0) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 指定された牌種リストからsetにある種類の刻子をすべて取り除く．
	 * 
	 * @param list
	 * @param set
	 * @return 取り除いた刻子の数．
	 */
	public static int removeKotsu(List<HaiType> list, Set<HaiType> set) {
		int size = 0;
		for (HaiType haiType2 : set) {
			if (Functions.sizeOfHaiTypeList(haiType2, list) >= 3) {
				list.remove(haiType2);
				list.remove(haiType2);
				list.remove(haiType2);
				size++;
			}
		}
		return size;
	}

	/**
	 * 指定された牌種リストから順子をすべて取り除く．
	 * @param list 
	 * @return 取り除いた順子の数．
	 */
	public static int removeSyuntsu(List<HaiType> list) {
		int size = 0;
		for (SuType suType : SuType.values()) {
			for (int i = 1; i <= 7; i++) {
				HaiType haiArray[] = new HaiType[3];
				haiArray[0] = HaiType.valueOf(suType, i);
				haiArray[1] = HaiType.valueOf(suType, i + 1);
				haiArray[2] = HaiType.valueOf(suType, i + 2);
				if (list.containsAll(Arrays.asList(haiArray))) {
					list.remove(haiArray[0]);
					list.remove(haiArray[1]);
					list.remove(haiArray[2]);
					i--;
					size++;
				}
			}
		}
		return size;
	}
}
