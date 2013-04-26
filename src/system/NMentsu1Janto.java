package system;

import java.util.ArrayList;
import java.util.Collections;
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
public class NMentsu1Janto extends UnmodifiableList<Mentu> {
	private final HaiType janto;

	private NMentsu1Janto(HaiType janto, List<Mentu> list) {
		super(list);
		this.janto = janto;
	}

	public HaiType getJanto() {
		return janto;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Mentu m : this) {
			if (sb.length() != 0)
				sb.append(", ");
			sb.append(m);
		}
		sb.append(", 雀頭 : " + janto.toString());
		return sb.toString();
	}

	/**
	 * 雀頭->順子->刻子の順番に面子を取り除いて出来たn面子1雀頭オブジェクトを生成してそれを返す．
	 * 
	 * @param tlist 手牌リスト．
	 * @return n面子1雀頭オブジェクト．
	 */
	public static NMentsu1Janto newInstanceFromSyuntsu(List<? extends Hai> tlist) {
		Set<HaiType> haiTypeSet = HaiType.toHaiTypeSet(tlist);
		TehaiList cptlist = new TehaiList(tlist);
		Collections.sort(cptlist, Hai.HaiComparator.ASCENDING_ORDER);

		for (HaiType haiType : haiTypeSet) {
			if (cptlist.sizeOf(haiType) >= 2) {
				TehaiList tempList = new TehaiList(cptlist);

				HaiType janto = haiType;
				List<Mentu> tempMentuList = new ArrayList<Mentu>();

				// 雀頭を取り除く
				tempList.remove(haiType);
				tempList.remove(haiType);

				// 順子を取り除く
				removeSyuntsu(tempList, tempMentuList);

				// 刻子を取り除く
				removeKotsu(haiTypeSet, tempList, tempMentuList);

				if (tempList.size() == 0) {
					return new NMentsu1Janto(janto, tempMentuList);
				}
			}
		}
		throw new IllegalArgumentException("指定された手牌では順子から面子を取り出してn面子1雀頭を生成できない");
	}

	/**
	 * 雀頭->刻子->順子の順番に面子を取り除いて出来たn面子1雀頭オブジェクトを生成してそれを返す．
	 * 
	 * @param tlist 手牌リスト．
	 * @return n面子1雀頭オブジェクト．
	 */
	public static NMentsu1Janto newInstanceFromKotsu(List<? extends Hai> tlist) {
		Set<HaiType> haiTypeSet = HaiType.toHaiTypeSet(tlist);
		TehaiList cptlist = new TehaiList(tlist);
		Collections.sort(cptlist, Hai.HaiComparator.ASCENDING_ORDER);

		for (HaiType haiType : haiTypeSet) {
			if (cptlist.sizeOf(haiType) >= 2) {
				TehaiList tempList = new TehaiList(cptlist);

				HaiType janto = haiType;
				List<Mentu> tempMentuList = new ArrayList<Mentu>();

				// 雀頭を取り除く
				tempList.remove(haiType);
				tempList.remove(haiType);

				// 刻子を取り除く
				removeKotsu(haiTypeSet, tempList, tempMentuList);

				// 順子を取り除く
				removeSyuntsu(tempList, tempMentuList);

				if (tempList.size() == 0) {
					return new NMentsu1Janto(janto, tempMentuList);
				}
			}
		}
		throw new IllegalArgumentException("指定された手牌では刻子から面子を取り出してn面子1雀頭を生成できない");
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
	private static void removeSyuntsu(TehaiList tempList, List<Mentu> tempMentuList) {
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

	public static NMentsu1Janto newInstanceOfException(List<? extends Hai> tlist) {
		Set<HaiType> haiTypeSet = HaiType.toHaiTypeSet(tlist);
		TehaiList cptlist = new TehaiList(tlist);
		Collections.sort(cptlist, Hai.HaiComparator.ASCENDING_ORDER);

		for (HaiType haiType : haiTypeSet) {
			if (cptlist.sizeOf(haiType) >= 2) {
				TehaiList tempList = new TehaiList(cptlist);

				HaiType janto = haiType;

				// 雀頭を取り除く
				tempList.remove(haiType);
				tempList.remove(haiType);

				NMentsu1Janto nmj = newInstanceOfException(tempList, new ArrayList<Mentu>(4), janto);
				if(nmj != null) {
					return nmj;
				}
			}
		}
		throw new IllegalArgumentException("指定された手牌では順子から面子を取り出してn面子1雀頭を生成できない");
	}

	private static NMentsu1Janto newInstanceOfException(TehaiList tlist, List<Mentu> mentsuList, HaiType janto) {
		// 刻子を取り除く
		Set<HaiType> haiTypeSet = HaiType.toHaiTypeSet(tlist);
		for (HaiType type : haiTypeSet) {
			if (tlist.sizeOf(type) >= 3) {
				TehaiList templist = new TehaiList(tlist);
				List<Mentu> tempmentsu = new ArrayList<Mentu>(mentsuList);
				
				Hai hai0 = templist.remove(type);
				Hai hai1 = templist.remove(type);
				Hai hai2 = templist.remove(type);
				tempmentsu.add(new Mentu(hai0, hai1, hai2));
				NMentsu1Janto nmj = newInstanceOfException(templist, tempmentsu, janto);
				if(nmj != null)
					return nmj;
			}
		}

		// 順子を取り除く
		for (SuType suType : SuType.values()) {
			for (int i = 1; i <= 7; i++) {
				HaiType types[] = new HaiType[3];
				types[0] = HaiType.valueOf(suType, i);
				types[1] = HaiType.valueOf(suType, i + 1);
				types[2] = HaiType.valueOf(suType, i + 2);

				if (tlist.contains(types[0]) && tlist.contains(types[1]) && tlist.contains(types[2])) {
					TehaiList templist = new TehaiList(tlist);
					List<Mentu> tempmentsu = new ArrayList<Mentu>(mentsuList);
					
					Hai hai0 = templist.remove(types[0]);
					Hai hai1 = templist.remove(types[1]);
					Hai hai2 = templist.remove(types[2]);
					
					tempmentsu.add(new Mentu(hai0, hai1, hai2));
					NMentsu1Janto nmj = newInstanceOfException(templist, tempmentsu, janto);
					if(nmj != null)
						return nmj;
				}
			}
		}

		if (tlist.size() == 0) {
			return new NMentsu1Janto(janto, mentsuList);
		}
		return null;
	}

}
