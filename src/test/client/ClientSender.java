package test.client;

import java.util.List;

/**
 * サーバーとの通信を行うメソッドを実装するインターフェース。
 */
public interface ClientSender {

	/**
	 * サーバーに対局を希望する.自分のIDを送る.
	 * 
	 * @param id 自分のID.
	 */
	public void requestGame(int id);

	/**
	 * resultページから、次の局の開始をサーバ側に要求する
	 */
	public void requestNextKyoku();

	/**
	 * クライアントへ暗槓する牌のインデックスのリストを送信する。
	 * @param list 暗槓する牌のインデックスのリスト。暗槓しない場合はnull。
	 */
	public void sendAnkanIndexList(List<Integer> list);

	/**
	 * サーバーへチーする牌のインデックスのリストを送信する。
	 * @param list チーする牌のインデックスのリスト。チーしない場合はnull。
	 */
	public void sendChiIndexList(List<Integer> list);

	/**
	 * サーバーへ捨てる牌のインデックスを送信する。
	 * @param index 捨てる牌のインデックス。
	 */
	public void sendDiscardIndex(int index);

	/**
	 * サーバーへ加槓する牌のインデックスを送信する。
	 * @param index 加槓する牌のインデックス。ツモ牌の場合は13。
	 */
	public void sendKakanIndex(int index);

	/**
	 * サーバーへ九種九牌するかどうかを送信する。
	 */
	public void sendKyusyukyuhai();

	/**
	 * サーバーへ明槓するかどうかの結果を送信する。
	 */
	public void sendMinkan();

	/**
	 * サーバーへポンする牌のインデックスのリストを送信する。
	 * @param list ポンする牌のインデックスのリスト。ポンしない場合はnull。
	 */
	public void sendPonIndexList(List<Integer> list);

	public void sendReachIndex(int index);

	/**
	 * サーバーへロンするかどうかの結果を送信する。
	 */
	public void sendRon();

	/**
	 * サーバーへツモ上がりするという結果を送信する。
	 */
	public void sendTsumoAgari();

	/**
	 * サーバーへロン、明槓、ポン、チーをしないということを送信する.
	 */
	public void sendReject();
}