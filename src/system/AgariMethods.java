package system;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import system.algo.PatternMethod;

/**
 * あがりに関するstaticメソッドを提供するクラス。
 */
public class AgariMethods {

	/**
	 * 切ってリーチできる牌インデックスリストを返す。どの牌を切ってもテンパイにならない場合は空のリストを返す。
	 * 鳴いていないことが前提で呼び出される.
	 * 
	 * @param tehaiList 手牌リスト
	 * @param f フィールド.
	 * @return 牌インデックスリスト。リーチできない場合は空のリスト
	 */
	public static List<Integer> getReachableIndexList(TehaiList tehaiList, Hai tsumohai, Field f) {
		List<Integer> result = new ArrayList<Integer>(13);

		for (int i = 0; i < tehaiList.size(); i++) {
			TehaiList tempTehai = new TehaiList(tehaiList);
			tempTehai.remove(i);
			tempTehai.add(tsumohai);

			if (isTenpai(tempTehai, false)) {
				result.add(i);
			}
		}

		if (isTenpai(new TehaiList(tehaiList), false)) {
			result.add(13);
		}

		return result;
	}

	/**
	 * 指定された手牌がテンパイしている場合、trueを返す。
	 * 
	 * @param tehaiList　手牌リスト
	 * @param naki 鳴いているかどうか
	 * @return　テンパイしている場合true
	 */
	public static boolean isTenpai(TehaiList tehaiList, boolean naki) {
		// TODO 関連牌種だけでチェックするように変更
		for (HaiType type : HaiType.values()) {
			Hai hai = MajanHai.valueOf(type, false);
			if (isKeisikiAgari(tehaiList, naki, hai)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 余分な牌を含めてテンパイしているかどうかを判定する.
	 * 例えば111,333,566, 東東東、南南という手牌で7をツモった場合などである．
	 * 
	 * @param tsumohai　ツモ牌.
	 * @param tehaiList　手牌リスト
	 * @param f フィールド.
	 * @return　テンパイしている場合true
	 */
	public static boolean isTenpaiWithExtra(TehaiList tehaiList, Hai tsumohai, Field f) {
		if (isTenpai(tehaiList, false)) {
			return true;
		}

		for (int i = 0; i < tehaiList.size(); i++) {
			TehaiList tempTehai = new TehaiList(tehaiList);
			tempTehai.remove(i);
			tempTehai.add(tsumohai);
			if (isTenpai(tempTehai, false)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 待ち牌の牌種リストを返す。
	 * 
	 * @param tehaiList 手牌リスト
	 * @param hurohaiList 副露牌リスト
	 * @param tsumo
	 * @param naki
	 * @param jikaze
	 * @param f フィールド.
	 * @return 牌種リスト
	 */
	public static List<HaiType> getAgariHaiTypeList(TehaiList tehaiList, HurohaiList hurohaiList, Set<Yaku> yakuFlag, boolean tsumo, boolean naki, Kaze jikaze, Field f) {
		List<HaiType> result = new ArrayList<HaiType>();
		for (HaiType type : HaiType.values()) {
			Hai hai = MajanHai.valueOf(type, false);
			if (isAgari(tehaiList, hurohaiList, hai, yakuFlag, tsumo, naki, jikaze, f)) {
				result.add(type);
			}
		}
		return result;
	}

	/**
	 * あがれる場合trueを返す。あがれない場合,役がない場合はfalse
	 * 
	 * @param tlist 手牌のリスト
	 * @param hlist 副露牌のリスト
	 * @param param 役チェックパラメーター
	 * @param f 場風,ルールなど.
	 * @return　あがれる場合true
	 */
	public static boolean isAgari(TehaiList tlist, HurohaiList hlist, Hai agariHai, Set<Yaku> yakuFlags, boolean tsumo, boolean naki, Kaze jikaze, Field f) {
		AgariParam agParam = new AgariParam(tsumo, naki, agariHai, jikaze, yakuFlags);

		CheckParam chParam = new CheckParam();
		List<Hai> haiList = new ArrayList<Hai>(tlist);
		haiList.add(agariHai);
		for (Mentu m : hlist) {
			haiList.addAll(m.asList());
		}
		chParam.setHaiList(haiList);

		List<Hai> tehaiPlusAgariHai = new ArrayList<Hai>(tlist);
		tehaiPlusAgariHai.add(agariHai);

		// 4面子1雀頭である
		if (isNMentu1Janto(tehaiPlusAgariHai)) {
			PatternMethod.Value pvalue = PatternMethod.getValue(tehaiPlusAgariHai);
			if (pvalue.isKotsuRm()) {
				NMentsu1Janto nmj = NMentsu1Janto.newInstanceFromKotsu(tehaiPlusAgariHai);
				chParam.setJanto(nmj.getJanto());
				chParam.setMentuList(nmj);
			} else if (pvalue.isSyuntsuRm()) {
				NMentsu1Janto nmj = NMentsu1Janto.newInstanceFromSyuntsu(tehaiPlusAgariHai);
				chParam.setJanto(nmj.getJanto());
				chParam.setMentuList(nmj);
			} else if(pvalue.isException()) {
				NMentsu1Janto nmj = NMentsu1Janto.newInstanceOfException(tehaiPlusAgariHai);
				chParam.setJanto(nmj.getJanto());
				chParam.setMentuList(nmj);
			}

			// 両面待ちととれる場合
			if (MatiType.RYANMEN.check(chParam.getMentuList(), agParam.getAgarihai().type(), chParam.getJanto())) {
				chParam.setMatiType(MatiType.RYANMEN);
				for (Yaku yaku : NormalYaku.values()) {
					if (yaku.check(agParam, chParam, f)) {
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

					if (yaku.check(agParam, chParam, f)) {
						return true;
					}
				}
			}

			for (Yaku yaku : Yakuman.values()) {
				if (yaku.check(agParam, chParam, f)) {
					return true;
				}
			}
		}
		// 4面子1雀頭でない
		else {

			if (agParam.isNaki()) {
				return false;
			}

			if (NormalYaku.CHITOI.check(agParam, chParam, f)) {
				return true;
			}

			if (Yakuman.KOKUSIMUSOU.check(agParam, chParam, f)) {
				return true;
			}

			if (Yakuman.KOKUSIMUSOU_13MEN.check(agParam, chParam, f)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * フリテンの場合はtrueを返す．つまり、自分の待ち牌が既に捨て牌にある場合はtrueを返す.
	 * この関数は同順フリテンなどは考慮しない.
	 * 
	 * @param machiList 待っている牌のリスト.
	 * @param sutehaiList 捨てた牌のリスト.
	 * @return フリテンの場合はtrue.
	 */
	public static boolean isFuriten(List<Hai> machiList, SutehaiList sutehaiList) {
		for (Hai hai : machiList) {
			if (sutehaiList.contains(hai))
				return true;
		}
		return false;
	}

	/**
	 * 指定された手牌がテンパイしている場合、その待ち牌リストを返す.
	 * テンパイしていない場合は空のリストを返す.
	 * 
	 * この関数から得られる待ち牌を合わせた手牌は役がない場合もある.
	 * 
	 * @param list 手牌リスト.
	 * @param naki 鳴いている場合true. 
	 * @return 待ち牌リスト.
	 */
	public static List<Hai> getMachiHaiList(TehaiList list, boolean naki) {
		List<Hai> machiHaiList = new ArrayList<Hai>();
		for (Hai hai : MajanHai.values()) {
			if (isKeisikiAgari(list, naki, hai))
				machiHaiList.add(hai);
		}
		return machiHaiList;
	}

	// TODO このメソッドはAgariResultが代わりに提供するように変更．
	//	/**
	//	 * 指定された牌リストなどから役を判定し,役セットを返す。役がない場合は空のセットを返す。
	//	 * 
	//	 * @param tehaiList 手牌のリスト
	//	 * @param hurohaiList 副露牌のリスト
	//	 * @param param 役判定に必要な材料
	//	 * @param f フィールド.
	//	 * @return 判定した役一覧
	//	 */
	//	public static Set<Yaku> checkYaku(TehaiList tehaiList, HurohaiList hurohaiList, Param param, Field f) {
	//		Set<Yaku> yakuSet = new HashSet<Yaku>();
	//
	//		// チェックパラムに上がり牌,副露牌を含めた牌リストをセット
	//		List<Hai> haiList = new ArrayList<Hai>(tehaiList);
	//		haiList.add(param.getAgariHai());
	//		for (Mentu m : hurohaiList) {
	//			haiList.addAll(m.asList());
	//		}
	//		param.setHaiList(haiList);
	//
	//		List<Hai> tehaiPlusAgariHai = new ArrayList<Hai>(tehaiList);
	//		tehaiPlusAgariHai.add(param.getAgariHai());
	//
	//		// 4面子1雀頭である
	//		if (isNMentu1Janto(tehaiPlusAgariHai)) {
	//			setMentuListAndJanto(tehaiList, param.getAgariHai(), hurohaiList, param);
	//		} else {
	//			param.setMentuList(null);
	//			param.setJanto(null);
	//		}
	//
	//		// 役満をチェックする
	//		for (Yakuman yaku : Yakuman.values()) {
	//			if (param.getMentuList() != null) {
	//				if (yaku.check(param, f)) {
	//					yakuSet.add(yaku);
	//				}
	//			} else {
	//				if (!yaku.is4Mentu1Janto() && yaku.check(param, f)) {
	//					yakuSet.add(yaku);
	//				}
	//			}
	//		}
	//
	//		// 役満である
	//		if (yakuSet.size() > 0) {
	//			return yakuSet;
	//		}
	//
	//		// 4面子1雀頭である
	//		if (param.getMentuList() != null) {
	//			List<MatiType> matiTypeList = MatiType.getMatiTypeList(param);
	//
	//			assert matiTypeList.size() > 0;
	//
	//			boolean pinhu = false;
	//
	//			// 両面待ちととれる場合			
	//			if (matiTypeList.contains(MatiType.RYANMEN)) {
	//				param.setMatiType(MatiType.RYANMEN);
	//
	//				if (NormalYaku.PINHU.check(param, f)) {
	//					yakuSet.add(NormalYaku.PINHU);
	//					pinhu = true;
	//
	//					for (Yaku yaku : NormalYaku.values()) {
	//						if (yaku == NormalYaku.PINHU)
	//							continue;
	//						if (yaku == NormalYaku.CHITOI)
	//							continue;
	//						if (yaku.check(param, f))
	//							yakuSet.add(yaku);
	//					}
	//
	//				} else if (matiTypeList.size() > 1) {
	//					if (!NormalYaku.PINHU.check(param, f)) {
	//						matiTypeList.remove(MatiType.RYANMEN);
	//					}
	//				}
	//			}
	//			if (!pinhu) {
	//				param.setMatiType(matiTypeList.get(0));
	//
	//				for (Yaku yaku : NormalYaku.values()) {
	//					if (yaku == NormalYaku.PINHU)
	//						continue;
	//					if (yaku == NormalYaku.CHITOI)
	//						continue;
	//					if (yaku.check(param, f))
	//						yakuSet.add(yaku);
	//				}
	//			}
	//		}
	//		// 4面子1雀頭でない
	//		else {
	//			if (NormalYaku.CHITOI.check(param, f)) {
	//				yakuSet.add(NormalYaku.CHITOI);
	//
	//				for (Yaku yaku : NormalYaku.values()) {
	//					if (yaku == NormalYaku.CHITOI)
	//						continue;
	//					if (!yaku.is4Mentu1Janto() && yaku.check(param, f))
	//						yakuSet.add(yaku);
	//				}
	//			}
	//		}
	//		return yakuSet;
	//	}

	/**
	 * あがれる場合trueを返す。isAgariとの違いは役がなくてもtrueを返しうるということである。
	 * 
	 * @param tehaiList 手牌のリスト
	 * @param naki 鳴いているかどうか
	 * @param agariHai あがり牌
	 * @return　あがれる場合true
	 */
	public static boolean isKeisikiAgari(TehaiList tehaiList, boolean naki, Hai agariHai) {
		CheckParam param = new CheckParam();

		// 役判定はしないので自風はとりあえす東を入れておく
		AgariParam agParam = new AgariParam(false, naki, agariHai, Kaze.TON, null);

		List<Hai> tehaiPlusAgariHai = new ArrayList<Hai>(tehaiList);
		tehaiPlusAgariHai.add(agariHai);
		param.setHaiList(tehaiPlusAgariHai);

		// 4面子1雀頭である
		if (isNMentu1Janto(tehaiPlusAgariHai)) {
			return true;
		}
		// 4面子1雀頭でない
		else {
			if (naki) {
				return false;
			}

			if (NormalYaku.CHITOI.check(agParam, param, null)) {
				return true;
			}

			if (Yakuman.KOKUSIMUSOU.check(agParam, param, null)) {
				return true;
			}

			if (Yakuman.KOKUSIMUSOU_13MEN.check(agParam, param, null)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 指定されたリストがn面子1雀頭で構成されている場合trueを返す．
	 * 
	 * @param haiList n面子1雀頭か検査するリスト
	 * @return 指定されたリストがn面子1雀頭で構成されている場合true
	 */
	public static boolean isNMentu1Janto(List<? extends Hai> tlist) {
		if (tlist.size() % 3 != 2)
			return false;
		// パターン法を採用
		return (PatternMethod.isNMentsu1Janto(tlist) & 0x43) != 0x0;

		// バックトラック法
		//		return BackTrackMethod.isNMentu1Janto(haiList);
	}

	//	/**
	//	 * あがり形がn面子1雀頭の場合に手牌リストの牌を順子刻子->とって出来た面子リストを返す．
	//	 * 
	//	 * @param haiList 手牌リスト
	//	 * @param agariHai 上がり牌
	//	 * @param huroList 副露牌リスト
	//	 * @param chParam チェッカーパラメータ
	//	 * @return 面子リストの設定に成功した場合true
	//	 */
	//	public static boolean getMentuListAndJanto(List<? extends Hai> haiList, Hai agariHai, HurohaiList huroList, CheckParam chParam) {
	//		Set<HaiType> haiTypeSet = HaiType.toHaiTypeSet(haiList);
	//		List<HaiType> haiListCopy = new ArrayList<HaiType>();
	//		for (Hai hai : haiList) {
	//			haiListCopy.add(hai.type());
	//		}
	//		haiListCopy.add(agariHai.type());
	//
	//		for (HaiType haiType : haiTypeSet) {
	//			if (Functions.sizeOfHaiTypeList(haiType, haiListCopy) >= 2) {
	//				List<HaiType> tempList = new ArrayList<HaiType>(haiListCopy);
	//				HaiType janto = haiType;
	//				List<Mentu> tempMentuList = new ArrayList<Mentu>();
	//
	//				tempList.remove(haiType);
	//				tempList.remove(haiType);
	//
	//				// 刻子を取り除く
	//				for (HaiType haiType2 : haiTypeSet) {
	//					if (Functions.sizeOfHaiTypeList(haiType2, tempList) >= 3) {
	//						Hai hai = MajanHai.valueOf(haiType2, false);
	//						tempMentuList.add(new Mentu(hai, hai, hai));
	//
	//						tempList.remove(haiType2);
	//						tempList.remove(haiType2);
	//						tempList.remove(haiType2);
	//					}
	//				}
	//
	//				// 順子を取り除く
	//				for (SuType suType : SuType.values()) {
	//					for (int i = 1; i <= 7; i++) {
	//						HaiType haiArray[] = new HaiType[3];
	//						haiArray[0] = HaiType.valueOf(suType, i);
	//						haiArray[1] = HaiType.valueOf(suType, i + 1);
	//						haiArray[2] = HaiType.valueOf(suType, i + 2);
	//
	//						if (tempList.containsAll(Arrays.asList(haiArray))) {
	//							Hai hai0 = MajanHai.valueOf(haiArray[0], false);
	//							Hai hai1 = MajanHai.valueOf(haiArray[1], false);
	//							Hai hai2 = MajanHai.valueOf(haiArray[2], false);
	//
	//							tempMentuList.add(new Mentu(hai0, hai1, hai2));
	//							tempList.remove(haiArray[0]);
	//							tempList.remove(haiArray[1]);
	//							tempList.remove(haiArray[2]);
	//							i--;
	//						}
	//					}
	//				}
	//
	//				if (tempList.size() == 0) {
	//					tempMentuList.addAll(huroList);
	//
	//					chParam.setJanto(janto);
	//					chParam.setMentuList(tempMentuList);
	//					return true;
	//				}
	//			}
	//		}
	//		return false;
	//	}

	//	/**
	//	 * チェッカーパラメータの面子リスト,雀頭を設定する。
	//	 * 
	//	 * @param haiList 手牌リスト
	//	 * @param agariHai 上がり牌
	//	 * @param huroList 副露牌リスト
	//	 * @param chParam チェッカーパラメータ
	//	 * @return 面子リストの設定に成功した場合true
	//	 */
	//	public static boolean setMentuListAndJanto(List<? extends Hai> haiList, Hai agariHai, HurohaiList huroList, CheckParam chParam) {
	//		Set<HaiType> haiTypeSet = HaiType.toHaiTypeSet(haiList);
	//		List<HaiType> haiListCopy = new ArrayList<HaiType>();
	//		for (Hai hai : haiList) {
	//			haiListCopy.add(hai.type());
	//		}
	//		haiListCopy.add(agariHai.type());
	//
	//		for (HaiType haiType : haiTypeSet) {
	//			if (Functions.sizeOfHaiTypeList(haiType, haiListCopy) >= 2) {
	//				List<HaiType> tempList = new ArrayList<HaiType>(haiListCopy);
	//				HaiType janto = haiType;
	//				List<Mentu> tempMentuList = new ArrayList<Mentu>();
	//
	//				tempList.remove(haiType);
	//				tempList.remove(haiType);
	//
	//				// 刻子を取り除く
	//				for (HaiType haiType2 : haiTypeSet) {
	//					if (Functions.sizeOfHaiTypeList(haiType2, tempList) >= 3) {
	//						Hai hai = MajanHai.valueOf(haiType2, false);
	//						tempMentuList.add(new Mentu(hai, hai, hai));
	//
	//						tempList.remove(haiType2);
	//						tempList.remove(haiType2);
	//						tempList.remove(haiType2);
	//					}
	//				}
	//
	//				// 順子を取り除く
	//				for (SuType suType : SuType.values()) {
	//					for (int i = 1; i <= 7; i++) {
	//						HaiType haiArray[] = new HaiType[3];
	//						haiArray[0] = HaiType.valueOf(suType, i);
	//						haiArray[1] = HaiType.valueOf(suType, i + 1);
	//						haiArray[2] = HaiType.valueOf(suType, i + 2);
	//
	//						if (tempList.containsAll(Arrays.asList(haiArray))) {
	//							Hai hai0 = MajanHai.valueOf(haiArray[0], false);
	//							Hai hai1 = MajanHai.valueOf(haiArray[1], false);
	//							Hai hai2 = MajanHai.valueOf(haiArray[2], false);
	//
	//							tempMentuList.add(new Mentu(hai0, hai1, hai2));
	//							tempList.remove(haiArray[0]);
	//							tempList.remove(haiArray[1]);
	//							tempList.remove(haiArray[2]);
	//							i--;
	//						}
	//					}
	//				}
	//
	//				if (tempList.size() == 0) {
	//					tempMentuList.addAll(huroList);
	//
	//					chParam.setJanto(janto);
	//					chParam.setMentuList(tempMentuList);
	//					return true;
	//				}
	//			}
	//		}
	//		return false;
	//	}

}
