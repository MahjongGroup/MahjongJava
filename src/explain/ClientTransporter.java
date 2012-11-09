package explain;

import java.util.List;
import java.util.Map;

import system.Hai;
import system.Mentu;
import system.Player;

/**
 * サーバーとの通信を行うメソッドを実装するインターフェース。
 */
public interface ClientTransporter {
	/**
	 * サーバーから九種九牌するかの問い合わせを受け取る。
	 */
	public void onKyusyukyuhaiRequested();

	/**
	 * サーバーへ九種九牌するという結果を送信する。
	 */
	public void sendKyusyukyuhai();

	/**
	 * サーバーからツモ牌を受け取る。
	 * @param hai ツモ牌。
	 */
	public void onTsumoHaiReceived(Hai hai);

	/**
	 * サーバーからどの牌を捨てるかの問い合わせを受け取る。
	 * @param tumoari ツモ牌がある場合true
	 */
	public void onDiscardReceived(boolean tumoari);

	/**
	 * サーバーへ捨てる牌のインデックスを送信する。
	 * @param index 捨てる牌のインデックス。
	 */
	public void sendDiscardIndex(int index);

	/**
	 * サーバーからチーするかの問い合わせを受け取る。
	 * @param lists チーできる牌のインデックスのリスト(サイズは必ず2)のリスト。
	 */
	public void onChiableIndexListsReceived(List<List<Integer>> lists);

	/**
	 * サーバーへチーする牌のインデックスのリストを送信する。
	 * @param list チーする牌のインデックスのリスト。チーしない場合はnull。
	 */
	public void sendChiIndexList(List<Integer> list);

	/**
	 * サーバーからポンするかの問い合わせを受け取る。
	 * @param lists ポンできる牌のインデックスのリスト(サイズは必ず2)のリスト。
	 */
	public void onPonableIndexListsReceived(List<List<Integer>> lists);

	/**
	 * サーバーへポンする牌のインデックスのリストを送信する。
	 * @param list ポンする牌のインデックスのリスト。ポンしない場合はnull。
	 */
	public void sendPonIndexList(List<Integer> list);

	/**
	 * サーバーから暗槓するかの問い合わせを受け取る。
	 * @param lists 暗槓できる牌のインデックスのリスト(サイズは必ず4)のリスト。
	 */
	public void onAnkanableIndexListsReceived(List<List<Integer>> lists);

	/**
	 * クライアントへ暗槓する牌のインデックスのリストを送信する。
	 * @param list 暗槓する牌のインデックスのリスト。暗槓しない場合はnull。
	 */
	public void sendAnkanIndexList(List<Integer> list);

	/**
	 * サーバーから明槓するかの問い合わせを受け取る。
	 * @param list 明槓できる牌のインデックスのリスト。
	 */
	public void onMinkanableIndexReceived(List<Integer> list);

	/**
	 * サーバーへ明槓するかどうかの結果を送信する。
	 * @param result 明槓する場合true。
	 */
	public void sendMinkan(boolean result);

	/**
	 * サーバーから加槓するかの問い合わせを受け取る。
	 * @param list 加槓できる牌のインデックスのリスト。
	 */
	public void onKakanableIndexListReceived(List<Integer> list);

	/**
	 * サーバーへ加槓する牌のインデックスを送信する。
	 * @param index 加槓する牌のインデックス。ツモ牌の場合は13。
	 */
	public void sendKakanIndex(int index);

	/**
	 * サーバーからリーチするかの問い合わせを受け取る。
	 * @param list (それを切って)リーチできる牌のインデックスのリスト。
	 */
	public void onReachableIndexListReceived(List<Integer> list);

	/**
	 * サーバーへ加槓する牌のインデックスを送信する。
	 * @param index 加槓する牌のインデックス。ツモ牌の場合は13。
	 */
	public void sendReachIndex(int index);

	/**
	 * サーバーからロンするかの問い合わせを受け取る。
	 */
	public void onRonRequested();

	/**
	 * サーバーへロンするかどうかの結果を送信する。
	 * @param result ロンする場合true。
	 */
	public void sendRon(boolean result);

	/**
	 * サーバーからツモ上がりするかの問い合わせを受け取る。
	 */
	public void onTsumoAgariRequested();

	/**
	 * サーバーへツモ上がりするという結果を送信する。
	 */
	public void sendTsumoAgari();

	/**
	 * 場の情報(捨て牌、副露牌など？)を受け取る。とりあえず実装はまだしない。
	 */
	public void onFieldReceived();

	/**
	 * サーバーから誰かが牌を切ったことを知らされる。
	 * @param p 牌を切ったプレイヤー。
	 * @param hai 切った牌。
	 * @param tumokiri ツモ切りの場合true。
	 */
	public void onDiscardReceived(Player p, Hai hai, boolean tumokiri);

	/**
	 * サーバーから誰かが鳴いたことを知らされる。
	 * @param p 鳴いたプレイヤー。
	 * @param m 鳴いて出来た面子。
	 */
	public void onNakiReceived(Player p, Mentu m);

	/**
	 * サーバーから誰かがロンをしたことを知らされる。
	 * @param map (ロンしたプレイヤー)->(そのプレイヤーの手牌リスト)を表すマップ。
	 */
	public void onRonReceived(Map<Player, List<Hai>> map);
}
