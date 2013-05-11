package ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import system.Functions;
import system.Player;
import system.hai.Hai;
import system.hai.HaiType;
import system.hai.Kaze;
import system.hai.MajanHai;
import system.hai.SuType;
import system.hai.TehaiList;

/**
 *  AI初号機。
 * 名前：ユイ　（命名：もせし）
 * 難しい計算はしないアホの子。
 * 面子を確定させて浮いた牌を切ります。
 * 鳴きません←new!!
 * 聴牌即リー全ツッパ。 
 * 防御はしない。
 * 
 * 打点：★★☆☆☆ 
 * 速度：★★★☆☆ 
 * 防御：★☆☆☆☆
 * 
 * @author shio
 */
public class AIType01 extends AbstractAI {
	public AIType01(Player p) {
		super(p);
	}

	@Override
	public boolean isKyusyukyuhai() {
		return false;
	}

	@Override
	public boolean isTumoAgari() {
		return true;
	}

	@Override
	public int kakan(List<Integer> list) {
		return -1;
	}

	@Override
	public int ankan(List<List<Integer>> list) {
		return 0;
	}

	@Override
	public int reach(List<Integer> list) {
		return -1;
	}

	@Override
	public int discard() {
		int index = 0;
		// 自風を取得。
		Kaze kaze = kyoku.getKazeOf(super.player);
		// 手牌を生成。
		TehaiList tlist = new TehaiList(kyoku.getTehaiList(kaze));
		Hai tsumohai = kyoku.getCurrentTsumoHai();
		if (tsumohai != null)
			tlist.add(kyoku.getCurrentTsumoHai());
		// 牌種リストを生成。
		Set<HaiType> haiTypeSet = tlist.toHaiTypeSet();
		List<HaiType> haiTypeList = tlist.toHaiTypeList();
		List<HaiType> tempList = new ArrayList<HaiType>(haiTypeList);

		// リーチ可能なら聴牌状態に入る。
		if (kyoku.isReachable())
			return kyoku.getReachableHaiList().get(0);
		// 刻子を抜く。
		for (HaiType haiType : haiTypeSet) {
			if (Functions.sizeOfHaiTypeList(haiType, tempList) >= 3) {
				tempList.remove(haiType);
				tempList.remove(haiType);
				tempList.remove(haiType);
			}
		}

		// 順子を抜く。
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
		// 孤立した字牌を切る。
		for (HaiType haiType : haiTypeSet) {
			if (haiType.isTsuhai()) {
				if (Functions.sizeOfHaiTypeList(haiType, tempList) == 1) {
					index = tlist.indexOf(MajanHai.valueOf(haiType, false));
					if (index == -1) {
						index = tlist.indexOf(MajanHai.valueOf(haiType, true));
						if (index == -1) {
							throw new IllegalStateException();
						}
					}

					if (index == tlist.size() - 1 && tsumohai != null)
						return 13;
					return index;
				}
			}
		}
		List<HaiType> templist2 = new ArrayList<HaiType>(tempList);
		// 対子を1つ抜く。
		int t = 0;
		int toitsuArray[] = new int[7];
		for (HaiType haiType : haiTypeSet) {
			if (Functions.sizeOfHaiTypeList(haiType, tempList) == 2) {
				tempList.remove(haiType);
				tempList.remove(haiType);
				toitsuArray[t] = tlist
						.indexOf(MajanHai.valueOf(haiType, false));
				t++;
				break;
			}
		}
		// 両面を抜く。
		for (SuType suType : SuType.values()) {
			for (int i = 2; i <= 6; i++) {
				HaiType haiArray[] = new HaiType[2];
				haiArray[0] = HaiType.valueOf(suType, i);
				haiArray[1] = HaiType.valueOf(suType, i + 1);
				if (tempList.containsAll(Arrays.asList(haiArray))) {
					tempList.remove(haiArray[0]);
					tempList.remove(haiArray[1]);
					i--;
				}
			}
		}
		// 嵌張を抜く。
		for (SuType suType : SuType.values()) {
			for (int i = 1; i <= 7; i++) {
				HaiType haiArray[] = new HaiType[2];
				haiArray[0] = HaiType.valueOf(suType, i);
				haiArray[1] = HaiType.valueOf(suType, i + 2);
				if (tempList.containsAll(Arrays.asList(haiArray))) {
					tempList.remove(haiArray[0]);
					tempList.remove(haiArray[1]);
					i--;
				}
			}
		}
		// 辺張を抜く。
		for (SuType suType : SuType.values()) {
			HaiType haiArray[] = new HaiType[2];
			haiArray[0] = HaiType.valueOf(suType, 1);
			haiArray[1] = HaiType.valueOf(suType, 2);
			if (tempList.containsAll(Arrays.asList(haiArray))) {
				tempList.remove(haiArray[0]);
				tempList.remove(haiArray[1]);
			}
			haiArray[0] = HaiType.valueOf(suType, 8);
			haiArray[1] = HaiType.valueOf(suType, 9);
			if (tempList.containsAll(Arrays.asList(haiArray))) {
				tempList.remove(haiArray[0]);
				tempList.remove(haiArray[1]);
			}
		}
		// 対子を抜く。（2回目以降）
		for (HaiType haiType : haiTypeSet) {
			if (Functions.sizeOfHaiTypeList(haiType, tempList) == 2) {
				tempList.remove(haiType);
				tempList.remove(haiType);
				toitsuArray[t] = tlist
						.indexOf(MajanHai.valueOf(haiType, false));
				t++;
			}
		}
		// 端に最も近い浮いた数牌を切る。
		int n = 6;
		index = 0;
		for (HaiType haiType : tempList) {
			int x = (-(Math.abs(haiType.number() - 5)) + 5);
			if (x < n) {
				n = x;
				if (Functions.sizeOfHaiTypeList(haiType, tempList) == 1) {
					index = tlist.indexOf(MajanHai.valueOf(haiType, false));
					if (index == -1) {
						index = tlist.indexOf(MajanHai.valueOf(haiType, true));
						if (index == -1) {
							throw new IllegalStateException();
						}
					}
				}
			}
		}
		if (n != 6) {
			if (index == tlist.size() - 1 && tsumohai != null)
				return 13;
			return index;
		}
		// 対子が2つ以上なら, 1つを切る。
		if (toitsuArray[1] != 0) {
			index = toitsuArray[0];
			if (index == tlist.size() - 1 && tsumohai != null)
				return 13;
			return index;
		}
		// 端に近い牌を切る。
		n = 6;
		index = 0;
		for (HaiType haiType : templist2) {
			if (haiType.isYaotyuhai()) {
				index = tlist.indexOf(MajanHai.valueOf(haiType, false));
				if (index == tlist.size() - 1 && tsumohai != null)
					return 13;
				return index;
			} else {
				int x = (-(Math.abs(haiType.number() - 5)) + 5);
				if (x < n) {
					n = x;
					if (Functions.sizeOfHaiTypeList(haiType, tempList) == 1) {
						index = tlist.indexOf(MajanHai.valueOf(haiType, false));
						if (index == -1) {
							index = tlist.indexOf(MajanHai.valueOf(haiType,
									true));
							if (index == -1) {
								throw new IllegalStateException();
							}
						}
					}
				}
			}
		}
		if (index == tlist.size() - 1 && tsumohai != null)
			return 13;
		return index;
	}

	@Override
	public boolean isRon() {
		return true;
	}

	@Override
	public int pon(List<List<Integer>> ponnableHaiList) {
		return -1;
	}

	@Override
	public int chi(List<List<Integer>> chiableHaiList) {
		return -1;
	}

	@Override
	public boolean minkan() {
		return false;
	}

}
