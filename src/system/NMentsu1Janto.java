package system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * あがり形、n面子1雀頭を表すクラス．</br>
 * </br>
 * 面子のとり方は雀頭->順子->刻子、雀頭->刻子->順子の2通りのとり方があり，</br>
 * 両方の点数が高い方を採用する必要がある．</br>
 * </br>
 * 雀頭のとり方が複数ある場合もあるが点数には影響しないため昇順で牌を並べた場合に</br>
 * 小さいほうから雀頭をとることにする．</br>
 */
public class NMentsu1Janto {
	private final Hai janto;
	private final List<Mentu> list;

	private NMentsu1Janto(Hai janto, List<Mentu> list) {
		this.janto = janto;
		this.list = list;
	}

	public static NMentsu1Janto newInstanceFromSyuntsu(List<? extends Hai> tlist, Hai agariHai) {
		Set<HaiType> haiTypeSet = HaiType.toHaiTypeSet(tlist);
		List<Hai> haiListCopy = new ArrayList<HaiType>();
		for (Hai hai : tlist) {
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

	}

	// 刻子を取り除く
	private static void removeKotsu(Set<HaiType> haiTypeSet, TehaiList tempList, List<Mentu> tempMentuList) {
		for (HaiType type : haiTypeSet) {
			if (tempList.sizeOf(type) >= 3) {
				Hai hai0 = tempList.remove(type);
				Hai hai1 = tempList.remove(type);
				Hai hai2 = tempList.remove(type);
				tempMentuList.add(new Mentu(hai0, hai1, hai2));
			}
		}
	}

	// 順子を取り除く
	public static void removeSyuntsu(TehaiList tempList, List<Mentu> tempMentuList) {
		for (SuType suType : SuType.values()) {
			for (int i = 1; i <= 7; i++) {
				HaiType types[] = new HaiType[3];
				types[0] = HaiType.valueOf(suType, i);
				types[1] = HaiType.valueOf(suType, i + 1);
				types[2] = HaiType.valueOf(suType, i + 2);

				if (tempList.contains(types[0]) && tempList.contains(types[1]) && tempList.contains(types[2])) {
					Hai hai0 = tempList.remove(types[0]);
					Hai hai1 = tempList.remove(types[1]);
					Hai hai2 = tempList.remove(types[2]);
					tempMentuList.add(new Mentu(hai0, hai1, hai2));
					i--;
				}
			}
		}

	}

}
