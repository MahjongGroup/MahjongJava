package client;

import java.util.List;
import java.util.Map;

import server.KyokuRunner;
import system.Player;
import system.hai.Hai;
import system.hai.HaiType;
import system.hai.HurohaiList;
import system.hai.Kaze;
import system.hai.Mentsu;
import system.result.KyokuResult;

/**
 * サーバーとの通信を行うメソッドを実装するインターフェース。
 */
public interface Client {

	// ゲーム開始時
	
	/**
	 * サーバーに対局を希望する.自分のIDを送る.
	 * 
	 * @param id 自分のID.
	 */
	public void requestGame(int id);
	
	/**
	 * 対局が開始したことをサーバーから受け取る.引数のプレイヤーリストは
	 * 自分を含めた対局するプレイヤーのリストである.0には東(起親)が入る.
	 * 
	 * @param playerList 自分を含めた対局するプレイヤーのリスト.
	 * @param index そのプレイヤーの上のリストにおけるインデックス
	 * @param scores それぞれのプレイヤーの最初の持ち点の配列
	 */
	public void onGameStartReceived(List<Player> playerList, int index,int[] scores);

	// 局開始前
	
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
	
	
	// 局

	/**
	 * サーバーから九種九牌するかの問い合わせを受け取る。
	 */
	public void onKyusyukyuhaiRequested();

	/**
	 * サーバーへ九種九牌するかどうかを送信する。
	 */
	public void sendKyusyukyuhai(boolean flag);

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
	public abstract void onMinkanableIndexListReceived(List<Integer> hais);

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

	// TODO javadoc
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
	public void onNakiReceived(Player p, Mentsu m);

	/**
	 * サーバーから誰かがロンをしたことを知らされる。
	 * @param map (ロンしたプレイヤー)->(そのプレイヤーの手牌リスト)を表すマップ。
	 */
	public void onRonReceived(Map<Player, List<Hai>> map);

	/**
	 * ツモあがったことをサーバーから受信する.
	 */
	public void onTsumoAgariReceived();

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
	 * サーバーから立直後のツモ切りしたことを受け取る.
	 * これを受け取ったクライアントは牌を強制的に切ることになる.
	 */
	// TODO これ必要？
	public void onTsumoGiriReceived();
	public void onGameResultReceived(int[] score);	
	
	// TODO 引数currentTurn確認.
	/**
	 * サーバーから立直したことを受信する.
	 * @param currentTurn
	 * @param sutehaiIndex
	 */
	public void onReachReceived(Kaze currentTurn, int sutehaiIndex);

	 
	/**
	 * 流局時に、テンパイしているプレイヤーの情報とその手牌を受信する
	 * @param map
	 */
	public void onTempaiReceived(Map<Player,List<Hai>> map);
	
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
	 * resultページから、次の局の開始をサーバ側に要求する
	 */
	public void requestNextKyoku();
	
	/**
	 * ゲームが終了したことをサーバ側から受け取る
	 */
	public void onGameOverReceived();

}