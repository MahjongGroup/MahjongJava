package mahjong.system;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * あがっているかどうかを確認するスタティックメソッドを提供するクラス。
 * 
 * @author kohei
 * 
 */
public class AgariChecker {

	/**
	 * あがっているならtrueを返す。
	 * 
	 * @param tehaiList 手牌のリスト
	 * @param tumohai ツモ牌
	 * @return あがっているならtrueを返す
	 */
	public static boolean isAgari(List<Hai> tehaiList, Hai tumohai) {
		// 手牌リストから牌種のリスト生成
		final List<HaiType> tehaiTypeList = new ArrayList<HaiType>();
		for (Hai hai : tehaiList) {
			tehaiTypeList.add(hai.getType());
		}

		// ツモ牌も加える
		tehaiTypeList.add(tumohai.getType());

		// 牌種リストから牌種セット生成
		Set<HaiType> haiSet = new HashSet<HaiType>(tehaiTypeList);

		for (HaiType janto : haiSet) {
			// 雀頭候補が手牌に2枚以上あるか
			if (getHaiSize(tehaiTypeList, janto) >= 2) {
				List<HaiType> temp = new ArrayList<HaiType>(tehaiTypeList);
				temp.remove(janto);
				temp.remove(janto);

				// 暗刻を除く
				for (HaiType anko : haiSet) {
					if (getHaiSize(temp, anko) >= 3) {
						temp.remove(anko);
						temp.remove(anko);
						temp.remove(anko);
					}
				}

				// 順子を除く
				for (SuhaiType suhaiType : SuhaiType.values()) {
					for (int i = 1; i <= 7; i++) {
						HaiType hai0 = HaiType.getSuhai(suhaiType, i);
						HaiType hai1 = HaiType.getSuhai(suhaiType, i + 1);
						HaiType hai2 = HaiType.getSuhai(suhaiType, i + 2);
						if (temp.contains(hai0) && temp.contains(hai1) && temp.contains(hai2)) {
							temp.remove(hai0);
							temp.remove(hai1);
							temp.remove(hai2);
							i--;
						}
					}
				}

				// tempに牌が１枚も残っていなければtrueを返す
				if (temp.size() == 0)
					return true;
			}
		}

		return false;
	}

	private static int getHaiSize(List<HaiType> list, HaiType type) {
		int count = 0;
		for (HaiType hai : list) {
			if (hai == type)
				count++;
		}
		return count;
	}
}
