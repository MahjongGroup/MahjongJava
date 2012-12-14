package ai;

import java.util.List;

import system.Hai;
import system.HaiType;
import system.Kyoku;
import system.Player;
import system.TehaiList;

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
			TehaiList tlist = kyoku.getTehaiList(kyoku.getKazeOf(player));
			List<Hai> haiList = AIMethods.getInvalidHaiList(tlist);
			AIMethods.getHuyouHai(haiList);
			
			int index = -1;
			return index;
		}
		
	},

	;
}
