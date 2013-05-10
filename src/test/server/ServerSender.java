package test.server;

import java.util.List;
import java.util.Map;

import system.Player;
import system.hai.Hai;
import system.hai.Kaze;
import system.hai.Mentsu;
import system.result.KyokuResult;

public interface ServerSender extends Map<Player, SingleServerSender>{
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
	 * 指定された風の人がリーチしたことを各プレイヤーに伝える.
	 * @param currentTurn リーチした人の風.
	 * @param sutehaiIndex 捨て牌インデックス.
	 */
	public void notifyReach(Kaze currentTurn, int sutehaiIndex);

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
}
