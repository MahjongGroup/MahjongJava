package client;

import java.util.List;
import java.util.Map;

import system.hai.Hai;
import system.hai.HurohaiList;
import system.hai.Kaze;
import system.hai.Mentsu;
import system.result.KyokuResult;
import system.test.Player;

public interface ClientReceiver {

	// ゲーム開始時
	
	/**
	 * サーバーから暗槓するかの問い合わせを受け取る。
	 * @param lists 暗槓できる牌のインデックスのリスト(サイズは必ず4)のリスト。
	 */
	public void onAnkanableIndexListsReceived(List<List<Integer>> lists);
	
	/**
	 * サーバーからチーするかの問い合わせを受け取る。
	 * @param lists チーできる牌のインデックスのリスト(サイズは必ず2)のリスト。
	 */
	public void onChiableIndexListsReceived(List<List<Integer>> lists);

	// 局開始前
	
	/**
	 * サーバーからどの牌を捨てるかの問い合わせを受け取る。
	 * @param tumoari ツモ牌がある場合true
	 */
	public void onDiscardReceived(boolean tumoari);
	
	
	// 局

	/**
	 * サーバーから誰かが牌を切ったことを知らされる。
	 * @param p 牌を切ったプレイヤー。
	 * @param hai 切った牌。
	 * @param tumokiri ツモ切りの場合true。
	 */
	public void onDiscardReceived(Player p, Hai hai, boolean tumokiri);

	/**
	 * サーバーから場の情報(手牌リスト,鳴き牌マップ,捨て牌マップ)を受け取る.
	 * @param tehai クライアントの手牌リスト.
	 * @param nakihaiMap 場の鳴き牌マップ.
	 * @param sutehaiMap 場の捨て牌マップ(鳴かれた牌は除く).
	 * @param currentTurn 現在の誰の番か.
	 * @param currentSutehai 
	 */
	public void onFieldReceived(List<Hai> tehai,
			Map<Kaze, HurohaiList> nakihai, Map<Kaze, List<Hai>> sutehai,
			Kaze currentTurn, Hai currentSutehai, List<Integer> tehaiSize,
			int yamaSize, int wanpaiSize, List<Hai> doraList);

	/**
	 * ゲームが終了したことをサーバ側から受け取る
	 */
	public void onGameOverReceived();

	public void onGameResultReceived(int[] score);

	/**
	 * 対局が開始したことをサーバーから受け取る.引数のプレイヤーリストは
	 * 自分を含めた対局するプレイヤーのリストである.0には東(起親)が入る.
	 * 
	 * @param playerList 自分を含めた対局するプレイヤーのリスト.
	 * @param index そのプレイヤーの上のリストにおけるインデックス
	 * @param scores それぞれのプレイヤーの最初の持ち点の配列
	 */
	public void onGameStartReceived(List<Player> playerList, int index,int[] scores);

	/**
	 * サーバーから加槓するかの問い合わせを受け取る。
	 * @param list 加槓できる牌のインデックスのリスト。
	 */
	public void onKakanableIndexListReceived(List<Integer> list);

	/**
	 * 局が終わったときのその結果をサーバーから受け取る.
	 * @param result 局の結果．
	 * @param newScores 新しいスコア
	 * @param oldScores 前のスコア
	 * @param soten 役点数の素点のリスト
	 * @param uradoraList 裏ドラのリスト
	 */
	public void onKyokuResultReceived(KyokuResult result,int[] newScores,int[] oldScores,List<Integer> soten ,List<Hai> uradoraList);

	/**
	 * サーバーから九種九牌するかの問い合わせを受け取る。
	 */
	public void onKyusyukyuhaiRequested();

	/**
	 * サーバーから明槓するかの問い合わせを受け取る。
	 * @param list 明槓できる牌のインデックスのリスト。
	 */
	public abstract void onMinkanableIndexListReceived(List<Integer> hais);

	/**
	 * サーバーから誰かが鳴いたことを知らされる。
	 * @param p 鳴いたプレイヤー。
	 * @param m 鳴いて出来た面子。
	 */
	public void onNakiReceived(Player p, Mentsu m);

	/**
	 * サーバーからポンするかの問い合わせを受け取る。
	 * @param lists ポンできる牌のインデックスのリスト(サイズは必ず2)のリスト。
	 */
	public void onPonableIndexListsReceived(List<List<Integer>> lists);

	/**
	 * サーバーからリーチするかの問い合わせを受け取る。
	 * @param list (それを切って)リーチできる牌のインデックスのリスト。
	 */
	public void onReachableIndexListReceived(List<Integer> list);

	// TODO 引数currentTurn確認.
	/**
	 * サーバーから立直したことを受信する.
	 * @param currentTurn
	 * @param sutehaiIndex
	 */
	public void onReachReceived(Kaze currentTurn, int sutehaiIndex);

	/**
	 * サーバーから誰かがロンをしたことを知らされる。
	 * @param map (ロンしたプレイヤー)->(そのプレイヤーの手牌リスト)を表すマップ。
	 */
	public void onRonReceived(Map<Player, List<Hai>> map);

	/**
	 * サーバーからロンするかの問い合わせを受け取る。
	 */
	public void onRonRequested();

	/**
	 * 局が開始したことをサーバーから受け取る.引数はその局の場風,局数である.
	 * 例えば「南場3局目」のときはKaze.NANと3を渡す.
	 * 
	 * @param bakaze その局の場風.
	 * @param kyokusu その局の局数.
	 * @param honba その局の本場.
	 * @param tsumibou 現在の積み棒の数.
	 */
	public void onStartKyokuReceived(Kaze bakaze, int kyokusu,int honba,int tsumibou);

	/**
	 * 流局時に、テンパイしているプレイヤーの情報とその手牌を受信する
	 * @param map
	 */
	public void onTempaiReceived(Map<Player,List<Hai>> map);

	/**
	 * ツモあがったことをサーバーから受信する.
	 */
	public void onTsumoAgariReceived();

	/**
	 * サーバーからツモ上がりするかの問い合わせを受け取る。
	 */
	public void onTsumoAgariRequested();

	/**
	 * サーバーから立直後のツモ切りしたことを受け取る.
	 * これを受け取ったクライアントは牌を強制的に切ることになる.
	 */
	// TODO これ必要？
	public void onTsumoGiriReceived();

	/**
	 * サーバーからツモ牌を受け取る。
	 * @param hai ツモ牌。
	 */
	public void onTsumoHaiReceived(Hai hai);
}
