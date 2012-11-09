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
	public int kakan() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public HaiType ankan() {
		// TODO Auto-generated method stub
		return null;
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
		Set<HaiType> haiTypeSet = Functions.getHaiTypeSetFrom(tlist);
		List<HaiType> haiTypeList = Functions
				.toHaiTypeListFromHaiCollection(tlist);
		List<HaiType> tempList = new ArrayList<HaiType>(haiTypeList);

		int[] a = null;
		a = new int[14];
		for (int i = 0; i < 14; i++) {
			Hai hai = tlist.get(i);
			HaiType haiType = hai.type();
			int x = haiType.id();
			if (30 <= x) {
				x = Functions.sizeOfHaiTypeList(haiType, tempList);
				a[i] = 4 ^ x;
				i++;
			} else if(3 <=(x % 10) && (x % 10) <= 7){
				int n = haiType.number();
				n = -Math.abs(n-5)+5;
				//interrupted
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
	public List<Integer> pon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> chi() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean minkan() {
		// TODO Auto-generated method stub
		return false;
	}

}
