package test.server;

import java.util.List;
import java.util.Map;

import system.Player;
import system.hai.Hai;
import system.hai.HurohaiList;
import system.hai.Kaze;
import system.hai.Mentsu;
import system.result.KyokuResult;

/**
 * クライアントへメッセージを送信するサーバー側のクラス.
 */
public interface SingleServerSender {
	
	// ゲーム開始時
	
	/**
	 * 対局が開始したことをクライアントに知らせる.引数のプレイヤーリストは
	 * 自分を含めた対局するプレイヤーのリストである.0には東(起親)が入る.
	 * 
	 * @param playerList 自分を含めた対局するプレイヤーのリスト.
	 * @param index そのプレイヤーの上のリストにおけるインデックス
	 */
	public void sendGameStart(List<Player> playerList,int index,int scores[]);
	

	// 局開始前
	/**
	 * 局が開始したことをプレイヤーに知らせる.引数はその局の場風,局数である.
	 * 例えば「南場3局目」のときはKaze.NANと3を渡す.
	 * 
	 * @param bakaze その局の場風.
	 * @param kyokusu その局の局数.
	 * @param honba その局の本場
	 * @param tsumibou 積み棒の数
	 */
	public void notifyStartKyoku(Kaze bakaze, int kyokusu,int honba,int tsumibou);

	// 局
	/**
	 * クライアントに九種九牌するか問い合わせる。
	 */
	public void requestKyusyukyuhai();

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
	 * クライアントにチーするかを問い合わせる。
	 * 
	 * @param lists チーできる牌のインデックスのリスト(サイズは必ず2)のリスト。
	 */
	public void sendChiableIndexLists(List<List<Integer>> lists);

	/**
	 * クライアントにポンするかを問い合わせる。
	 * @param lists ポンできる牌のインデックスのリスト(サイズは必ず2)のリスト。
	 */
	public void sendPonableIndexLists(List<List<Integer>> lists);

	/**
	 * クライアントに暗槓するかを問い合わせる。
	 * @param lists 暗槓できる牌のインデックスのリスト(サイズは必ず4)のリスト。
	 */
	public void sendAnkanableIndexLists(List<List<Integer>> lists);

	/**
	 * クライアントに明槓するかを問い合わせる。
	 * @param list 明槓できる牌のインデックスのリスト(サイズは必ず3)。
	 */
	public void sendMinkanableIndexList(List<Integer> list);

	/**
	 * クライアントに加槓するかを問い合わせる。
	 * @param list 加槓できる牌のインデックスのリスト。
	 */
	public void sendKakanableIndexList(List<Integer> list);

	/**
	 * クライアントにリーチするかを問い合わせる。
	 * @param list (その牌を切って)リーチできる牌のインデックスのリスト。
	 */
	public void sendReachableIndexList(List<Integer> list);

	/**
	 * クライアントにロンするか問い合わせる。
	 */
	public void requestRon();

	/**
	 * クライアントにツモ上がりするか問い合わせる。
	 */
	public void requestTsumoAgari();

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
	public void notifyNaki(Player p, Mentsu m);

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
	 * @param currentSutehai 現在の捨て牌
	 * @param tehaiSize 全てのプレイヤーの手牌の数
	 * @param yamaSize 山牌の数
	 * @param wanpaiSize 王牌の数
	 * @param doraList 現在のドラのリスト
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
	 * @param soten 役点数の素点
	 * @param uradora 裏ドラリスト
	 */
	public void notifyKyokuResult(KyokuResult kr,int[] newScore,int[] oldScore,List<Integer> soten,List<Hai> uradoraList);

	/**
	 * 局が終わったあと、聴牌しているプレイヤーとその手牌を送るメソッド
	 */
	public void notifyTempai(Map<Player ,List<Hai>> map);
	
	
	/**
	 * 半荘が終わった時にその結果を送信
	 * @param Score 全員の最終の持ち点が入った配列
	 */
	public void notifyGameResult(int[] Score);

	/** 
	 *　ゲームが終わったことをクライアントに送信
	 */
	public void sendGameOver();
}
