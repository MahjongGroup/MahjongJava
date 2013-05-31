package test.server;

import java.util.List;
import java.util.Map;

import system.Player;
import system.hai.Hai;
import system.hai.Kaze;
import system.hai.Mentsu;
import system.result.KyokuResult;

/**
 * クライアントへメッセージを送信するサーバー側のクラス.
 */
public interface SingleServerSender {
	
	/**
	 * 対局が開始したことをクライアントに知らせる.
	 * 
	 * @param pMap
	 * @param kaze
	 * @param initialScore
	 */
	public void sendGameStart(Map<Kaze, Player> pMap, Kaze kaze, int initialScore);

	/**
	 * 局が開始したことをプレイヤーに知らせる.引数はその局の場風,局数である.
	 * 
	 * @param bakaze その局の場風.
	 * @param kaze そのプレイヤーの風.
	 * @param kyokusu その局の局数.
	 * @param honba その局の本場
	 * @param tsumibou 積み棒の数
	 * @param haipai 配牌.
	 * @param dora ドラ牌.
	 */
	public void notifyStartKyoku(Kaze bakaze, Kaze kaze, int kyokusu,int honba,int tsumibou, List<Hai> haipai, Hai dora);

	/**
	 * クライアントに九種九牌するか問い合わせる。
	 */
	public void requestKyusyukyuhai();

	/**
	 * クライアントにどの牌を切るのか問い合わせる。
	 * @param tsumohai ツモ牌.
	 */
	public void requestDiscardIndex(Hai tsumohai);

	/**
	 * クライアントに暗槓するかを問い合わせる。
	 * @param lists 暗槓できる牌のインデックスのリスト(サイズは必ず4)のリスト。
	 */
	public void sendAnkanableIndexLists(List<List<Integer>> lists);

	/**
	 * クライアントに可能ならばロン、明槓、ポン、チーするかどうかを問い合わせる.
	 * 
	 * @param ron ロンできるならばtrue.
	 * @param minkan 明槓できる牌のインデックスのリスト(サイズは必ず3).明槓できない場合null.
	 * @param pon ポンできる牌のインデックスのリスト(サイズは必ず2)のリスト.ポンできない場合null.
	 * @param chi チーできる牌のインデックスのリスト(サイズは必ず2)のリスト.チーできない場合null.
	 */
	public void requestNaki(boolean ron, List<Integer> minkan, List<List<Integer>> pon, List<List<Integer>> chi);
	
	/**
	 * クライアントに加槓するかを問い合わせる。
	 * @param list 加槓できる牌のインデックスのリスト。
	 */
	public void requestKakanableIndex(List<Integer> list);

	/**
	 * クライアントにリーチするかを問い合わせる。
	 * @param list (その牌を切って)リーチできる牌のインデックスのリスト。
	 */
	public void requestReachableIndex(List<Integer> list);

	/**
	 * クライアントにツモ上がりするか問い合わせる。
	 */
	public void requestTsumoAgari();

	/**
	 * 誰かが牌を切ったことを各クライアントに伝える。
	 * @param kaze 牌を切った人の風.
	 * @param hai 切った牌
	 * @param tumokiri ツモ切りの場合はtrue。
	 */
	public void notifyDiscard(Kaze kaze, Hai hai, boolean tumokiri);

	/**
	 * 誰かが鳴いたことを各プレイヤーに伝える。
	 * @param kaze 鳴いた人の風.
	 * @param m 鳴いて出来た面子.
	 */
	public void notifyNaki(Kaze kaze, Mentsu m);

	/**
	 * 誰かがロンをしたことを各プレイヤーに伝える。
	 * @param map (ロンしたプレイヤー)->(そのプレイヤーの手牌リスト)を表すマップ。
	 */
	public void notifyRon(Map<Player, List<Hai>> map);

	/**
	 * 指定された風の人がリーチしたことを各プレイヤーに伝える.
	 * @param currentTurn リーチした人の風.
	 * @param hai 立直牌.
	 * @param tsumokiri ツモ切り.
	 */
	public void notifyReach(Kaze currentTurn, Hai hai, boolean tsumokiri);

	/**
	 * 局が終わったときにその結果を返す．
	 * 
	 * @param result 局の結果．
	 * @param scoreDiff
	 * @param uradora 裏ドラリスト
	 */
	public void notifyKyokuResult(KyokuResult kr, Map<Kaze, Integer> scoreDiff ,List<Hai> uradoraList);

	/**
	 * 局が終わったあと、聴牌しているプレイヤーとその手牌を送るメソッド
	 */
	public void notifyTempai(Map<Kaze,List<Hai>> map);
	
	
	/**
	 * 半荘が終わった時にその結果を送信
	 * @param scoreMap
	 */
	public void notifyGameResult(Map<Kaze, Integer> scoreMap);

	/** 
	 *　ゲームが終わったことをクライアントに送信
	 */
	public void sendGameOver();
}
