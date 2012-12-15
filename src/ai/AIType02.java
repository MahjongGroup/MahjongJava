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
 * AI弐号機。
 * 戦略を用いる（つもり）
 * AI01の再現を目安に。
 * @author shio
 *
 */
public class AIType02 extends AbstractAI {
	
	
	public AIType02(Player p) {
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
		int index = DiscardEnum.INDEX_0_DISCARD.discard(kyoku, super.player);
		
		return index;
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
