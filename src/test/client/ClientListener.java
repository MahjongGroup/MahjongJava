package test.client;

import java.util.List;
import java.util.Map;

import system.Player;
import system.hai.Hai;
import system.hai.Kaze;
import system.hai.Mentsu;
import system.result.KyokuResult;

/**
 * サーバーとの通信を行うメソッドを実装するインターフェース。
 */
public interface ClientListener extends MahjongListener {

	/**
	 * サーバーから誰かが牌を切ったことを知らされる。
	 * @param kaze 牌を切った人の風.
	 * @param hai 切った牌.
	 * @param tsumokiri ツモ切りの場合true.
	 */
	public void onDiscardReceived(Kaze kaze, Hai hai, boolean tsumokiri);

	/**
	 * ゲームが終了したことをサーバ側から受け取る
	 */
	public void onGameOverReceived();

	public void onGameResultReceived(Map<Kaze, Integer> scoreMap);

	/**
	 * 対局が開始したことをサーバーから受け取る.
	 * 
	 * @param pMap 風->その風のプレイヤーを表すマップ.
	 * @param kaze 自分の風.
	 */
	public void onGameStartReceived(Map<Kaze, Player> pMap, Kaze kaze, int initialScore);

	/**
	 * 局が終わったときのその結果をサーバーから受け取る.
	 * @param result 局の結果．
	 * @param scoreDiff 点数の差.
	 * @param uradoraList 裏ドラのリスト
	 */
	public void onKyokuResultReceived(KyokuResult result, Map<Kaze, Integer> scoreDiff, List<Hai> uradoraList);

	/**
	 * サーバーから誰かが鳴いたことを知らされる。
	 * 鳴きのアニメーションなどを実行する.
	 * @param kaze 鳴いた人の風.
	 * @param m 鳴いて出来た面子。
	 */
	public void onNakiNotified(Kaze kaze, Mentsu m);

	/**
	 * サーバーから立直したことを受信する.
	 * @param currentTurn 立直した人の風.
	 * @param hai 立直牌.
	 * @param tsumokiri ツモ切りの場合true.
	 */
	public void onReachNotified(Kaze currentTurn, Hai hai, boolean tsumokiri);

	/**
	 * サーバーから誰かがロンをしたことを知らされる。
	 * @param map (ロンしたプレイヤー)->(そのプレイヤーの手牌リスト)を表すマップ。
	 */
	public void onRonReceived(Map<Player, List<Hai>> map);

	/**
	 * 局が開始したことをサーバーから受け取る.引数はその局の場風,局数である.
	 * 例えば「南場3局目」のときはKaze.NANと3を渡す.
	 * 
	 * @param bakaze その局の場風.
	 * @param kaze そのプレイヤーの風.
	 * @param kyokusu その局の局数.
	 * @param honba その局の本場.
	 * @param tsumibou 現在の積み棒の数.
	 * @param haipai 配牌.
	 * @param dora ドラ牌.
	 */
	public void onStartKyokuReceived(Kaze bakaze, Kaze kaze, int kyokusu, int honba, int tsumibou, List<Hai> haipai, Hai dora);

	/**
	 * 流局時に、テンパイしているプレイヤーの情報とその手牌を受信する
	 * @param map
	 */
	public void onTempaiReceived(Map<Kaze, List<Hai>> map);

	/**
	 * ツモあがったことをサーバーから受信する.
	 * ツモあがり時のアニメーションを表示するなどするとよい.
	 */
	public void onTsumoAgariReceived();

}