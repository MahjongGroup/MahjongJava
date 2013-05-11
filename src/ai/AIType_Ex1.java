package ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import system.hai.Hai;
import system.hai.HaiType;
import system.hai.Kaze;
import system.hai.MajanHai;
import system.hai.TehaiList;
import system.test.Functions;
import system.test.Player;

/**
 * おふざけAIその1。 国士無双しか狙いません。
 * 
 * @author shio
 * 
 */
public class AIType_Ex1 extends AbstractAI {

	public AIType_Ex1(Player p) {
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
	public int ankan(List<List<Integer>> lists) {
		return -1;
	}

	@Override
	public boolean isReach() {
		return false;
	}

	@Override
	public int discard() {
		//即リー。
		if(kyoku.isReachable())
			return kyoku.getReachableHaiList().get(0);
		int index = 0;
		// 自風を取得。
		Kaze kaze = kyoku.getKazeOf(super.player);
		// 手牌を生成。
		TehaiList tlist = new TehaiList(kyoku.getTehaiList(kaze));
		Hai tsumohai = kyoku.getCurrentTsumoHai();
		if (tsumohai != null) {
			// ツモ牌がヤオチューでなければツモ切り。
			if (tsumohai.type().isTyuntyanhai())
				return 13;
			tlist.add(kyoku.getCurrentTsumoHai());
		}
		// 牌種リストを生成。
		 Set<HaiType> haiTypeSet = tlist.toHaiTypeSet();
		 List<HaiType> haiTypeList = tlist.toHaiTypeList();
		 //ヤオチューでない牌を切る。
		for (Hai hai : tlist) {
			if (hai.type().isTyuntyanhai()) {
				index = tlist.indexOf(hai);
				return index;
			}
		}
		//重なった牌を切る。
		for(HaiType haiType : haiTypeSet){
			if(Functions.sizeOfHaiTypeList(haiType, haiTypeList) > 1)
				index = tlist.indexOf(MajanHai.valueOf(haiType, false));
			if (index == -1) {
				index = tlist.indexOf(MajanHai.valueOf(haiType, true));
				if (index == -1) {
					throw new IllegalStateException();
				}
			}
		}
		return index;
	}

	@Override
	public boolean isRon() {
		return true;
	}

	@Override
	public int pon(List<List<Integer>> list) {
		return -1;
	}

	@Override
	public int chi(List<List<Integer>> list) {
		return -1;
	}

	@Override
	public boolean minkan() {
		return false;
	}

}
