package ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import system.Functions;
import system.Player;
import system.hai.Hai;
import system.hai.HaiType;
import system.hai.Kaze;
import system.hai.TehaiList;
/**
 * AI弐号機。
 * 戦略を用いる（つもり）
 * AI01の再現を目安に。
 * @author shio
 *
 */
public class AIType02 extends AbstractAI {
	
	private Set<HaiType> extendedValidHaiSet;
	
	public AIType02(Player p) {
		super(p);
		extendedValidHaiSet = new TreeSet<HaiType>();
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
		Hai hai = kyoku.getCurrentTsumoHai();
		TehaiList tlist = kyoku.getTehaiList(kyoku.getKazeOf(player));
		
		if(extendedValidHaiSet == null)
			extendedValidHaiSet = AIMethods.getExtendedValidHaiTypeSet(tlist);
		
		if(!extendedValidHaiSet.contains(hai)){
			return 13;
		}else{
			extendedValidHaiSet.addAll(AIMethods.getNearHaiTypeList(hai.type()));
		}
		int index = DiscardEnum.ISOLATED_HAI_DISCARD.discard(kyoku, super.player);
		if(index != -1)
			return index;
		
		
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
