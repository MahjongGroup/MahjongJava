package ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import system.Functions;
import system.Hai;
import system.HaiType;
import system.Kaze;
import system.Player;
import system.TehaiList;
/**
 * 手牌評価型AI。
 * 近くに牌があるかどうかで点数を決める。
 * 聴牌にはたどり着かないかもしれない駄作。
 * 
 * @author shio
 *
 */
public class AIType02 extends AbstractAI {
	public AIType02(Player p) {
		super(p);
	}

	@Override
	public boolean isKyusyukyuhai() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTumoAgari() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int kakan(List<Integer> list) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int ankan(List<List<Integer>> list) {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public boolean isReach() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int discard() {
		// TODO Auto-generated method stub
		Kaze kaze = kyoku.getKazeOf(super.player);
		TehaiList tlist = new TehaiList(kyoku.getTehaiList(kaze));
		List<HaiType> haiTypeList = tlist.toHaiTypeList();
		List<HaiType> tempList = new ArrayList<HaiType>(haiTypeList);

		int[] a = null;
		a = new int[14];		
		ArrayList<Integer> idList = new ArrayList<Integer>();
		Hai hai;
		HaiType haiType;
		
		for (int i = 0; i < 14; i++) {
			hai = tlist.get(i);
			haiType = hai.type();
			int x = haiType.id();
			idList.add(x);
		}
		
		for (int i = 0; i < 14; i++) {
			int value = 0;
			hai = tlist.get(i);
			haiType = hai.type();
			int x = idList.get(i);
			if (30 <= x) {
				value += Functions.sizeOfHaiTypeList(haiType, tempList);
				a[i] += 4 ^ value;
			} else if(3 <=(x % 10) && (x % 10) <= 7){
				int n = haiType.number();
				value += -Math.abs(n-5)+5;
				a[i] += value;
			}
		}
		
		
		return 0;
	}

	@Override
	public boolean isRon() {
		// TODO Auto-generated method stub
		
		return false;
	}

	@Override
	public int pon(List<List<Integer>> ponnableHaiList) {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public int chi(List<List<Integer>> ponnableHaiList) {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public boolean minkan() {
		// TODO Auto-generated method stub
		return false;
	}

}
