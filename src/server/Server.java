package server;

import java.util.List;
import java.util.Map;

import system.Hai;
import system.HaiType;
import system.HurohaiList;
import system.Kaze;
import system.KyokuResult;
import system.Mentu;
import system.Player;

/**
 * クライアントとの通信を行うメソッドを実装するインターフェース。
 */
public interface Server {
	
	// ゲーム開始時
	
	/**
	 * クライアントから対局を希望しているということを受け取る.引数は
	 * そのプレイヤー(クライアント)のID.
	 * 
	 * @param id プレイヤーのID.
	 */
	public void onGameRequested(int id);
	
	/**
	 * 対局が開始したことをクライアントに知らせる.引数のプレイヤーリストは
	 * 自分を含めた対局するプレイヤーのリストである.0には東(起親)が入る.
	 * 
	 * @param playerList 自分を含めた対局するプレイヤーのリスト.
	 * @param index そのプレイヤーのインデックス
	 */
	public void sendGameStart(List<Player> playerList,int index);
	

	// 局開始前
	/**
	 * 局が開始したことをプレイヤーに知らせる.引数はその局の場風,局数である.
	 * 例えば「南場3局目」のときはKaze.NANと3を渡す.
	 * 
	 * @param bakaze その局の場風.
	 * @param kyokusu その局の局数.
	 */
	public void notifyStartKyoku(Kaze bakaze, int kyokusu);

	// 局
	/**
	 * クライアントに九種九牌するか問い合わせる。
	 */
	public void requestKyusyukyuhai();

	/**
	 * クライアントから九種九牌するかどうかの結果を受け取る.
	 * 
	 * @param answer 九種九牌する場合true.
	 */
	public void onKyusyukyuhaiReceived(boolean answer);

	/**
	 * クライアントにツモ牌を送信する。
	 * @param hai　クライアントに送るツモ牌。
	 */
	public void sendTsumoHai(Hai hai);

	/**
	 * ツモ切りしたことを送信する.
	 */
	public void sendTsumoGiri();

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

	// TODO 名前がおかしいのを結合後にリファクタリングで修正
	/**
	 * クライアントから明槓するかどうかの結果を受け取る。
	 * @param answer 明槓するならtrue.
	 */
	public void onMinkanableIndexReceived(boolean answer);

	/**
	 * クライアントに加槓するかを問い合わせる。
	 * @param list 加槓できる牌のインデックスのリスト。
	 */
	public void sendKakanableIndexList(List<Integer> list);

	/**
	 * クライアントから加槓するかどうかの結果を受け取る。
	 * @param index 加槓する牌のインデックス。ツモ牌の場合は13。加槓しない場合は-1。
	 */
	public void onKakanableIndexReceived(int index);

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

	/**
	 * 局の場の情報(手牌リスト，鳴き牌マップ,捨て牌マップなど)を送る.
	 * @param tehai そのクライアントの手牌リスト.
	 * @param nakihai 鳴き牌マップ.
	 * @param sutehai 捨て牌マップ(鳴かれた牌は除く).
	 * @param currentTurn 現在ターンの風.
	 * @param hai 
	 */
	void sendField(List<Hai> tehai, Map<Kaze, HurohaiList> nakihai,
			Map<Kaze, List<Hai>> sutehai, Kaze currentTurn, Hai currentSutehai,
			List<Integer> tehaiSize, int yamaSize, int wanpaiSize,
			List<Hai> doraList);

	/**
	 * 指定された風の人がリーチしたことを各プレイヤーに伝える.
	 * @param currentTurn リーチした人の風.
	 * @param sutehaiIndex 捨て牌インデックス.
	 */
	public void notifyReach(Kaze currentTurn, int sutehaiIndex);

	// 局の終わり
	
	/**
	 * 局が終わったときにその結果を返す．
	 * 
	 * @param result 局の結果．
	 * @param newScore 新しいスコア
	 * @param oldScore 前のスコア
	 */


	public void notifyKyokuResult(KyokuResult kr,int[] newScore,int[] oldScore);

	
	/**
	 * 局が終わったあとの結果表示を終わらせるメソッド
	 * 
	 */
	public void onNextKyokuRequested();
	

	
	
}