package explain;

import java.util.List;
import java.util.Map;

import system.Hai;
import system.Mentu;
import system.Player;

/**
 * クライアントとの通信を行うメソッドを実装するインターフェース。
 */
public interface ServerTransporter {
	/**
	 * クライアントに九種九牌するか問い合わせる。
	 */
	public void requestKyusyukyuhai();

	/**
	 * クライアントから九種九牌するという結果を受け取る。
	 */
	public void onKyusyukyuhaiReceived();

	/**
	 * クライアントにツモ牌を送信する。
	 * @param hai　クライアントに送るツモ牌。
	 */
	public void sendTsumoHai(Hai hai);

	/**
	 * クライアントにどの牌を切るのか問い合わせる。
	 * @param tumoari ツモ牌がある場合はtrue。
	 */
	public void sendDiscard(boolean tumoari);

	/**
	 * クライアントから切る牌のインデックスを受け取る。
	 * @param index　切る牌のインデックス。ツモ牌の場合は13。牌を切らない場合は-1。
	 */
	public void onDiscardIndexReceived(int index);

	/**
	 * クライアントにチーするかを問い合わせる。
	 * 
	 * @param lists チーできる牌のインデックスのリスト(サイズは必ず2)のリスト。
	 */
	public void sendChiableIndexLists(List<List<Integer>> lists);

	/**
	 * クライアントからチーする牌のインデックスのリストを受け取る。
	 * @param list チーするリストの牌のインデックス。チーしない場合はnull。
	 */
	public void onChiIndexListReceived(List<Integer> list);

	/**
	 * クライアントにポンするかを問い合わせる。
	 * @param lists ポンできる牌のインデックスのリスト(サイズは必ず2)のリスト。
	 */
	public void sendPonableIndexLists(List<List<Integer>> lists);

	/**
	 * クライアントからポンする牌のインデックスのリストを受け取る。
	 * @param list ポンするリストの牌のインデックス。ポンしない場合はnull。
	 */
	public void onPonIndexListReceived(List<Integer> list);

	/**
	 * クライアントに暗槓するかを問い合わせる。
	 * @param lists 暗槓できる牌のインデックスのリスト(サイズは必ず4)のリスト。
	 */
	public void sendAnkanableIndexLists(List<List<Integer>> lists);

	/**
	 * クライアントから暗槓する牌のインデックスのリストを受け取る。
	 * @param list 暗槓するリストの牌のインデックス。暗槓しない場合はnull。
	 */
	public void onAnkanIndexListReceived(List<Integer> list);

	/**
	 * クライアントに明槓するかを問い合わせる。
	 * @param list 明槓できる牌のインデックスのリスト(サイズは必ず3)。
	 */
	public void sendMinkanableIndexList(List<Integer> list);

	/**
	 * クライアントから明槓するかどうかの結果を受け取る。
	 * @param answer 明槓するならtrue
	 */
	public void onMinkanIndexReceived(boolean result);

	/**
	 * クライアントに加槓するかを問い合わせる。
	 * @param list 加槓できる牌のインデックスのリスト。
	 */
	public void sendKakanableIndexList(List<Integer> list);

	/**
	 * クライアントから加槓するかどうかの結果を受け取る。
	 * @param index 加槓する牌のインデックス。ツモ牌の場合は13。加槓しない場合は-1。
	 */
	public void onKakanIndexReceived(int index);

	/**
	 * クライアントにリーチするかを問い合わせる。
	 * @param list (その牌を切って)リーチできる牌のインデックスのリスト。
	 */
	public void sendReachableIndexList(List<Integer> list);

	/**
	 * クライアントから加槓するかどうかの結果を受け取る。
	 * @param index 加槓する牌のインデックス。ツモ牌の場合は13。加槓しない場合は-1。
	 */
	public void onReachIndexReceived(int index);

	/**
	 * クライアントにロンするか問い合わせる。
	 */
	public void requestRon();

	/**
	 * クライアントからロンするかどうかの結果を受け取る。
	 * @param result ロンする場合はtrue。
	 */
	public void onRonReceived(boolean result);

	/**
	 * クライアントにツモ上がりするか問い合わせる。
	 */
	public void requestTsumoAgari();

	/**
	 * クライアントからツモ和了するという結果を受け取る。
	 */
	public void onTsumoAgariReceived();

	/**
	 * 場の情報(捨て牌、副露牌など？)を送る。とりあえず実装はまだしない。
	 */
	public void sendFileld();

	/**
	 * 誰かが牌を切ったことを各クライアントに伝える。
	 * @param p 牌を切ったプレイヤー。
	 * @param hai 切った牌
	 * @param tumokiri ツモ切りの場合はtrue。
	 */
	public void notifyDiscard(Player p, Hai hai, boolean tumokiri);

	/**
	 * 誰かが鳴いたことを各プレイヤーに伝える。
	 * @param p 鳴いたプレイヤー。
	 * @param m 鳴いて出来た面子。
	 */
	public void notifyNaki(Player p, Mentu m);

	/**
	 * 誰かがロンをしたことを各プレイヤーに伝える。
	 * @param map (ロンしたプレイヤー)->(そのプレイヤーの手牌リスト)を表すマップ。
	 */
	public void notifyRon(Map<Player, List<Hai>> map);
}
