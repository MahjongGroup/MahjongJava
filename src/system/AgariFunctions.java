package system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * あがりに関するstaticメソッドを提供するクラス。
 * 
 * @author kohei
 */
public class AgariFunctions {

	/**
	 * 切ってテンパイとなる牌インデックスリストを返す。どの牌を切ってもテンパイにならない場合は空のリストを返す。
	 * 
	 * @param tehaiList 手牌リスト
	 * @param param　チェッカーパラメータ(鳴き,場風,風,ルールだけをセットすれば良い)
	 * @param f フィールド.
	 * @return 牌インデックスリスト。リーチできない場合は空のリスト
	 */
	public static List<Integer> getReachableIndexList(TehaiList tehaiList, Hai tsumohai,
			Param param, Field f) {
		if (param.isNaki()) {
			return new ArrayList<Integer>(0);
		}

		List<Integer> result = new ArrayList<Integer>(13);

		for (int i = 0; i < tehaiList.size(); i++) {
			TehaiList tempTehai = new TehaiList(tehaiList);
			tempTehai.remove(i);
			tempTehai.add(tsumohai);
			if (isTenpai(tempTehai, new HurohaiList(0), param.isNaki())) {
				result.add(i);
			}
		}

		if (isTenpai(new TehaiList(tehaiList), new HurohaiList(0), param.isNaki())) {
			result.add(13);
		}

		return result;
	}

	/**
	 * (余分な牌を含めずに)テンパイしている場合,trueを返す。
	 * 
	 * @param tehaiList　手牌リスト
	 * @param hurohaiList　副露牌リスト
	 * @param naki 鳴いているかどうか
	 * @return　テンパイしている場合true
	 */
	public static boolean isTenpai(TehaiList tehaiList, HurohaiList hurohaiList, boolean naki) {
		for (HaiType type : HaiType.values()) {
			Hai hai = MajanHai.valueOf(type, false);
			if (isKeisikiAgari(tehaiList, hurohaiList, naki, hai)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 余分な牌を含めてテンパイしているかどうかを判定する.
	 * 
	 * @param tsumohai　ツモ牌.
	 * @param tehaiList　手牌リスト
	 * @param hurohaiList　副露牌リスト
	 * @param param　チェック用パラメータ(鳴き,場風,風,ルールだけをセットすれば良い)
	 * @param f フィールド.
	 * @return　テンパイしている場合true
	 */
	public static boolean isTenpai(TehaiList tehaiList, HurohaiList hurohaiList, Hai tsumohai,
			Param param, Field f) {
		if (param.isNaki()) {
			return false;
		}

		if (isTenpai(tehaiList, new HurohaiList(0), param.isNaki())) {
			return true;
		}
		
		for (int i = 0; i < tehaiList.size(); i++) {
			TehaiList tempTehai = new TehaiList(tehaiList);
			tempTehai.remove(i);
			tempTehai.add(tsumohai);
			if (isTenpai(tempTehai, new HurohaiList(0), param.isNaki())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 待ち牌の種類リストを返す。
	 * 
	 * @param tehaiList 手牌リスト
	 * @param hurohaiList 副露牌リスト
	 * @param param チェッカーパラメータ(上がり牌は設定しなくてもよい)
	 * @param f フィールド.
	 * @return 牌種リスト
	 */
	public static List<HaiType> getAgariHaiTypeList(TehaiList tehaiList, HurohaiList hurohaiList,
			Param param, Field f) {
		List<HaiType> result = new ArrayList<HaiType>();
		for (HaiType type : HaiType.values()) {
			Hai hai = MajanHai.valueOf(type, false);
			param.setAgariHai(hai);
			if (isAgari(tehaiList, hurohaiList, param, f)) {
				result.add(type);
			}
		}
		return result;
	}
	
	/**
	 * あがれる場合trueを返す。あがれない場合,役がない場合はfalse
	 * 
	 * @param tehaiList 手牌のリスト
	 * @param hurohaiList 副露牌のリスト
	 * @param param 役チェックパラメーター
	 * @param f 場風,ルールなど.
	 * @return　あがれる場合true
	 */
	public static boolean isAgari(TehaiList tehaiList, HurohaiList hurohaiList, Param param, Field f) {
		assert param.getFlagCheckYakuSet() != null;
		assert param.getJikaze() != null;

		List<Hai> haiList = new ArrayList<Hai>(tehaiList);
		haiList.add(param.getAgariHai());
		for (Mentu m : hurohaiList) {
			haiList.addAll(m.asList());
		}
		param.setHaiList(haiList);

		List<Hai> tehaiPlusAgariHai = new ArrayList<Hai>(tehaiList);
		tehaiPlusAgariHai.add(param.getAgariHai());
		
		// 4面子1雀頭である
		if (isNMentu1Janto(tehaiPlusAgariHai)) {
			if (!setMentuListAndJanto(tehaiList, param.getAgariHai(), hurohaiList, param)) {
				throw new IllegalStateException();
			}

			// 両面待ちととれる場合
			if (MatiType.RYANMEN.check(param)) {
				param.setMatiType(MatiType.RYANMEN);
				for (Yaku yaku : NormalYaku.values()) {
					if (yaku.check(param, f)) {
						return true;
					}
				}
			}
			// 両面待ちととれない場合
			else {
				for (Yaku yaku : NormalYaku.values()) {

					// 平和は飛ばす
					if (yaku == NormalYaku.PINHU)
						continue;

					if (yaku.check(param, f)) {
						return true;
					}
				}
			}

			for (Yaku yaku : Yakuman.values()) {
				if (yaku.check(param, f)) {
					return true;
				}
			}
		}
		// 4面子1雀頭でない
		else {

			if (param.isNaki()) {
				return false;
			}

			if (NormalYaku.CHITOI.check(param, f)) {
				return true;
			}

			if (Yakuman.KOKUSIMUSOU.check(param, f)) {
				return true;
			}

			if (Yakuman.KOKUSIMUSOU_13MEN.check(param, f)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 指定された牌リストなどから役を判定し,役セットを返す。役がない場合は空のセットを返す。
	 * 
	 * @param tehaiList 手牌のリスト
	 * @param hurohaiList 副露牌のリスト
	 * @param param 役判定に必要な材料
	 * @param f フィールド.
	 * @return 判定した役一覧
	 */
	public static Set<Yaku> checkYaku(TehaiList tehaiList, HurohaiList hurohaiList,
			Param param, Field f) {
		Set<Yaku> yakuSet = new HashSet<Yaku>();

		// チェックパラムに上がり牌,副露牌を含めた牌リストをセット
		List<Hai> haiList = new ArrayList<Hai>(tehaiList);
		haiList.add(param.getAgariHai());
		for (Mentu m : hurohaiList) {
			haiList.addAll(m.asList());
		}
		param.setHaiList(haiList);

		List<Hai> tehaiPlusAgariHai = new ArrayList<Hai>(tehaiList);
		tehaiPlusAgariHai.add(param.getAgariHai());

		// 4面子1雀頭である
		if (isNMentu1Janto(tehaiPlusAgariHai)) {
			setMentuListAndJanto(tehaiList, param.getAgariHai(), hurohaiList, param);
		} else {
			param.setMentuList(null);
			param.setJanto(null);
		}

		// 役満をチェックする
		for (Yakuman yaku : Yakuman.values()) {
			if(param.getMentuList() != null) {
				if (yaku.check(param, f)) {
					yakuSet.add(yaku);
				}
			}
			else {
				if (!yaku.is4Mentu1Janto() && yaku.check(param, f)) {
					yakuSet.add(yaku);
				}
			}
		}

		// 役満である
		if (yakuSet.size() > 0) {
			return yakuSet;
		}

		// 4面子1雀頭である
		if (param.getMentuList() != null) {
			List<MatiType> matiTypeList = MatiType.getMatiTypeList(param);

			assert matiTypeList.size() > 0;

			boolean pinhu = false;

			// 両面待ちととれる場合			
			if (matiTypeList.contains(MatiType.RYANMEN)) {
				param.setMatiType(MatiType.RYANMEN);

				if (NormalYaku.PINHU.check(param, f)) {
					yakuSet.add(NormalYaku.PINHU);
					pinhu = true;

					for (Yaku yaku : NormalYaku.values()) {
						if (yaku == NormalYaku.PINHU)
							continue;
						if (yaku == NormalYaku.CHITOI)
							continue;
						if (yaku.check(param, f))
							yakuSet.add(yaku);
					}

				} else if (matiTypeList.size() > 1) {
					if (!NormalYaku.PINHU.check(param, f)) {
						matiTypeList.remove(MatiType.RYANMEN);
					}
				}
			}
			if (!pinhu) {
				param.setMatiType(matiTypeList.get(0));

				for (Yaku yaku : NormalYaku.values()) {
					if (yaku == NormalYaku.PINHU)
						continue;
					if (yaku == NormalYaku.CHITOI)
						continue;
					if (yaku.check(param, f))
						yakuSet.add(yaku);
				}
			}
		}
		// 4面子1雀頭でない
		else {
			if (NormalYaku.CHITOI.check(param, f)) {
				yakuSet.add(NormalYaku.CHITOI);

				for (Yaku yaku : NormalYaku.values()) {
					if (yaku == NormalYaku.CHITOI)
						continue;
					if (!yaku.is4Mentu1Janto() && yaku.check(param, f))
						yakuSet.add(yaku);
				}
			}
		}
		return yakuSet;
	}

	/**
	 * あがれる場合trueを返す。isAgariとの違いは役がなくてもtrueを返しうるということである。
	 * 
	 * @param tehaiList 手牌のリスト
	 * @param hurohaiList 副露牌のリスト
	 * @param naki 鳴き
	 * @param agariHai あがり牌
	 * @return　あがれる場合true
	 */
	public static boolean isKeisikiAgari(TehaiList tehaiList, HurohaiList hurohaiList,
			boolean naki, Hai agariHai) {
		Param param = new Param();
		param.setNaki(naki);
		param.setAgariHai(agariHai);
		
		List<Hai> haiList = new ArrayList<Hai>(tehaiList);
		haiList.add(param.getAgariHai());
		for (Mentu m : hurohaiList) {
			haiList.addAll(m.asList());
		}
		param.setHaiList(haiList);

		List<Hai> tehaiPlusAgariHai = new ArrayList<Hai>(tehaiList);
		tehaiPlusAgariHai.add(param.getAgariHai());
		
		// 4面子1雀頭である
		if (isNMentu1Janto(tehaiPlusAgariHai)) {
			return true;
		}
		// 4面子1雀頭でない
		else {
			if (param.isNaki()) {
				return false;
			}

			if (NormalYaku.CHITOI.check(param, null)) {
				return true;
			}

			if (Yakuman.KOKUSIMUSOU.check(param, null)) {
				return true;
			}

			if (Yakuman.KOKUSIMUSOU_13MEN.check(param, null)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 指定されたリストがn面子1雀頭で構成されている場合trueを返す。
	 * 
	 * @param haiList n面子1雀頭か検査するリスト
	 * @return 指定されたリストがn面子1雀頭で構成されている場合true
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

				// 刻子を取り除く
				for (HaiType haiType2 : haiTypeSet) {
					if (Functions.sizeOfHaiTypeList(haiType2, tempList) >= 3) {
						tempList.remove(haiType2);
						tempList.remove(haiType2);
						tempList.remove(haiType2);
					}
				}

				// 順子を取り除く
				for (SuType suType : SuType.values()) {
					for (int i = 1; i <= 7; i++) {
						HaiType haiArray[] = new HaiType[3];
						haiArray[0] = HaiType.valueOf(suType, i);
						haiArray[1] = HaiType.valueOf(suType, i + 1);
						haiArray[2] = HaiType.valueOf(suType, i + 2);
						if (tempList.containsAll(Arrays.asList(haiArray))) {
							tempList.remove(haiArray[0]);
							tempList.remove(haiArray[1]);
							tempList.remove(haiArray[2]);
							i--;
						}
					}
				}

				if (tempList.size() == 0) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * チェッカーパラメータの面子リスト,雀頭を設定する。
	 * 
	 * @param haiList 手牌リスト
	 * @param agariHai 上がり牌
	 * @param huroList 副露牌リスト
	 * @param chParam チェッカーパラメータ
	 * @return 面子リストの設定に成功した場合true
	 */
	public static boolean setMentuListAndJanto(List<? extends Hai> haiList, Hai agariHai,
			HurohaiList huroList, Param chParam) {
		Set<HaiType> haiTypeSet = HaiType.toHaiTypeSet(haiList);
		List<HaiType> haiListCopy = new ArrayList<HaiType>();
		for (Hai hai : haiList) {
			haiListCopy.add(hai.type());
		}
		haiListCopy.add(agariHai.type());

		for (HaiType haiType : haiTypeSet) {
			if (Functions.sizeOfHaiTypeList(haiType, haiListCopy) >= 2) {
				List<HaiType> tempList = new ArrayList<HaiType>(haiListCopy);
				HaiType janto = haiType;
				List<Mentu> tempMentuList = new ArrayList<Mentu>();

				tempList.remove(haiType);
				tempList.remove(haiType);

				// 刻子を取り除く
				for (HaiType haiType2 : haiTypeSet) {
					if (Functions.sizeOfHaiTypeList(haiType2, tempList) >= 3) {
						Hai hai = MajanHai.valueOf(haiType2, false);
						tempMentuList.add(new Mentu(hai, hai, hai));

						tempList.remove(haiType2);
						tempList.remove(haiType2);
						tempList.remove(haiType2);
					}
				}

				// 順子を取り除く
				for (SuType suType : SuType.values()) {
					for (int i = 1; i <= 7; i++) {
						HaiType haiArray[] = new HaiType[3];
						haiArray[0] = HaiType.valueOf(suType, i);
						haiArray[1] = HaiType.valueOf(suType, i + 1);
						haiArray[2] = HaiType.valueOf(suType, i + 2);

						if (tempList.containsAll(Arrays.asList(haiArray))) {
							Hai hai0 = MajanHai.valueOf(haiArray[0], false);
							Hai hai1 = MajanHai.valueOf(haiArray[1], false);
							Hai hai2 = MajanHai.valueOf(haiArray[2], false);

							tempMentuList.add(new Mentu(hai0, hai1, hai2));
							tempList.remove(haiArray[0]);
							tempList.remove(haiArray[1]);
							tempList.remove(haiArray[2]);
							i--;
						}
					}
				}

				if (tempList.size() == 0) {
					tempMentuList.addAll(huroList);

					chParam.setJanto(janto);
					chParam.setMentuList(tempMentuList);
					return true;
				}
			}
		}
		return false;
	}

}
