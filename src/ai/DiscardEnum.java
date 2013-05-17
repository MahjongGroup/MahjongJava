package ai;

import java.util.List;

import system.Kyoku;
import system.Player;
import system.hai.Hai;
import system.hai.Kaze;
import system.hai.TehaiList;

/** 
 * AIのdiscardの戦略を実際に実装したオブジェクトの列挙型．
 */
public enum DiscardEnum implements DiscardStrategy {
	/**
	 * ひたすら手牌リストのインデックス0を切る戦略．
	 */
	INDEX_0_DISCARD() {
		@Override
		public int discard(Kyoku kyoku,Player player) {
			return 0;
		}
	},
	/**
	 *　孤立牌を切る戦略.
	 */
	ISOLATED_HAI_DISCARD(){
		@Override
		public int discard(Kyoku kyoku,Player player) {
			Kaze jikaze = kyoku.getKazeOf(player);
			TehaiList tlist = kyoku.getTehaiList(jikaze);
			List<Hai> haiList = AIMethods.getInvalidHaiList(tlist);
			Hai hai = AIMethods.getHuyouHai(haiList,kyoku.getBakaze(),jikaze);
			int index = -1;
			
			if(hai != null)
				index = tlist.indexOf(hai);
			
			return index;
		}
		
	},

	;
}
