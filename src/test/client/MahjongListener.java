package test.client;

import java.util.List;

import system.hai.Hai;

public interface MahjongListener {
	/**
	 * サーバーから暗槓するかの問い合わせを受け取る。
	 * @param lists 暗槓できる牌のインデックスのリスト(サイズは必ず4)のリスト。
	 */
	public void onAnkanableIndexListsReceived(List<List<Integer>> lists);

	/**
	 * サーバーからどの牌を捨てるかの問い合わせを受け取る。
	 * @param tsumohai ツモった牌.ツモ牌がない場合はnull.
	 */
	public void onDiscardRequested(Hai tsumohai);

	/**
	 * サーバーから加槓するかの問い合わせを受け取る。
	 * @param list 加槓できる牌のインデックスのリスト。
	 */
	public void onKakanableIndexRequested(List<Integer> list);

	/**
	 * サーバーから九種九牌するかの問い合わせを受け取る。
	 */
	public void onKyusyukyuhaiRequested();

	/**
	 * サーバーから可能ならばロン、明槓、ポン、チーするかどうかの問い合わせを受けたときに呼び出される.
	 * 
	 * @param ron ロンできるならばtrue.
	 * @param minkan 明槓できる牌のインデックスのリスト(サイズは必ず3).明槓できない場合null.
	 * @param pon ポンできる牌のインデックスのリスト(サイズは必ず2)のリスト.ポンできない場合null.
	 * @param chi チーできる牌のインデックスのリスト(サイズは必ず2)のリスト.チーできない場合null.
	 */
	public void onNakiRequested(boolean ron, List<Integer> minkan, List<List<Integer>> pon, List<List<Integer>> chi);

	/**
	 * サーバーからリーチするかの問い合わせを受け取る。
	 * @param list (それを切って)リーチできる牌のインデックスのリスト。
	 */
	public void onReachableIndexRequested(List<Integer> list);

	/**
	 * サーバーからツモ上がりするかの問い合わせを受け取る。
	 */
	public void onTsumoAgariRequested();
}
