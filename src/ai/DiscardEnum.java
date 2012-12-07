package ai;

import system.Kyoku;

/**
 * AIのdiscardの戦略を実際に実装したオブジェクトの列挙型．
 */
public enum DiscardEnum implements DiscardStrategy {
	/**
	 * ひたすら手牌リストのインデックス0を切る戦略．
	 */
	INDEX_0_DISCARD() {
		@Override
		public int discard(Kyoku kyoku) {
			return 0;
		}
	},

	;
}
