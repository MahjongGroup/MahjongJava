package test.server;

import java.util.List;
import java.util.Map;

import system.Player;
import system.hai.Hai;
import system.hai.Kaze;
import system.hai.Mentsu;
import system.result.KyokuResult;

public interface ServerSender extends Map<Player, SingleServerSender> {
	/**
	 * 誰かが牌を切ったことを各クライアントに伝える。
	 * 
	 * @param kaze 牌を切ったプレイヤーの風.
	 * @param hai 切った牌
	 * @param tumokiri ツモ切りの場合はtrue。
	 */
	public void notifyDiscard(Kaze kaze, Hai hai, boolean tumokiri);

	/**
	 * 誰かが鳴いたことを各プレイヤーに伝える。
	 * 
	 * @param kaze 鳴いたプレイヤーの風.
	 * @param m 鳴いて出来た面子.
	 */
	public void notifyNaki(Kaze kaze, Mentsu m);

	/**
	 * 誰かがロンをしたことを各プレイヤーに伝える。
	 * 
	 * @param map (ロンしたプレイヤー)->(そのプレイヤーの手牌リスト)を表すマップ。
	 */
	public void notifyRon(Map<Player, List<Hai>> map);

	/**
	 * 指定された風の人がリーチしたことを各プレイヤーに伝える.
	 * 
	 * @param currentTurn リーチした人の風.
	 * @param hai 立直牌.
	 * @param tsumokiri ツモ切りの場合true.
	 */
	public void notifyReach(Kaze currentTurn, Hai hai, boolean tusmokiri);

	/**
	 * 局が終わったときにその結果を返す．
	 * 
	 * @param result 局の結果．
	 * @param scoreDiff 点数の差.
	 * @param uradora 裏ドラリスト
	 */
	public void notifyKyokuResult(KyokuResult kr, Map<Kaze, Integer> scoreDiff, List<Hai> uradoraList);

	/**
	 * 局が終わったあと、聴牌しているプレイヤーとその手牌を送るメソッド
	 */
	public void notifyTempai(Map<Kaze, List<Hai>> map);

	/**
	 * 半荘が終わった時にその結果を送信
	 * 
	 * @param scoreMap
	 */
	public void notifyGameResult(Map<Kaze, Integer> scoreMap);
}
