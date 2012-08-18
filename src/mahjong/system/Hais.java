package mahjong.system;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 牌に関するスタティックメソッドを提供するクラス。
 * 
 * @author kohei
 * 
 */
public class Hais {
	/**
	 * 牌リストの中の特定の牌の個数を返す。
	 * 
	 * @param list 調べる牌リスト
	 * @param type 個数を知りたい牌
	 * @return 牌リストの中の特定の牌の個数
	 */
	public static int getHaiSize(List<Hai> list, HaiType type) {
		int count = 0;
		for (Hai h : list) {
			if (h.getType() == type)
				count++;
		}
		return count;
	}

	/**
	 * 与えられた牌リストから種類の重複のないHaiTypeリストを返す。
	 * 
	 * @param list 牌リスト
	 * @return 種類の重複のないHaiTypeリスト
	 */
	public static List<HaiType> getSingleHaiList(List<Hai> list) {
		Set<HaiType> result = new HashSet<HaiType>();
		for (Hai hai : list) {
			result.add(hai.getType());
		}
		return new ArrayList<HaiType>(result);
	}
}
