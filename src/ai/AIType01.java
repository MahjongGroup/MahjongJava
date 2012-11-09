package ai;

import java.util.List;

import system.HaiType;
import system.Kaze;
import system.Player;
import system.TehaiList;

public class AIType01 extends AbstractAI {
	public AIType01(Player p) {
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
		// TODO Auto-geperated method stub
//		Kaze kaze = kyoku.getKazeOf(super.player);
//		TehaiList tlist = new TehaiList(kyoku.getTehaiList(kaze));
		
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
