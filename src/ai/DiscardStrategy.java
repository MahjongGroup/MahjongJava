package ai;

import system.Kyoku;
import system.Player;

/**
 * AIが牌をdiscardをするときに用いられる戦略のインターフェース．
 */
public interface DiscardStrategy {

	/**
	 * AIが判断した切る牌の手牌のインデックスを返す.
	 * ツモ切りの場合,13を返す.
	 * 
	 * @return 切る牌の手牌のインデックス.ツモ切りの場合は13.
	 */
	public int discard(Kyoku kyoku,Player player);
}
