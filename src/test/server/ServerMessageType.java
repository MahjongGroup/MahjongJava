package test.server;

/**
 * クライアントから受け取るメッセージの種類.
 */
public enum ServerMessageType {
	
	/**
	 * クライアントが対局を希望しているというメッセージ.
	 * @param id プレイヤーのID.整数型.
	 */
	GAME_REQUESTED,
	
	/**
	 * クライアントの九種九牌するというメッセージ.
	 * @param empty
	 */
	KYUSYUKYUHAI_RECEIVED,
	
	/**
	 * クライアントから切る牌のインデックスを受け取る。
	 * @param index　切る牌のインデックス.ツモ牌の場合は13.整数型.
	 */
	DISCARD_INDEX_RECEIVED,
	
	/**
	 * クライアントからチーする牌のインデックスのリストを受け取る。
	 * @param list チーするリストの牌のインデックス.整数リスト型.
	 */
	CHIINDEX_LIST_RECEIVED,
	
	/**
	 * クライアントからポンする牌のインデックスのリストを受け取る。
	 * @param list ポンするリストの牌のインデックス.整数リスト型.
	 */
	PON_INDEX_LIST_RECEIVED,
	
	/**
	 * クライアントから暗槓する牌のインデックスのリストを受け取る。
	 * @param list 暗槓するリストの牌のインデックス.整数リスト型.
	 */
	ANKAN_INDEX_LIST_RECEIVED,
	
	/**
	 * クライアントからの明槓するというメッセージ.
	 * @param empty
	 */
	MINKAN_RECEIVED,
	
	/**
	 * クライアントからの加槓をするというメッセージ.
	 * @param index 加槓する牌のインデックス.ツモ牌の場合は13.整数型.
	 */
	KAKANABLE_INDEX_RECEIVED,

	/**
	 * クライアントからのリーチをするというメッセージ.
	 * @param index どの牌を切ってリーチするか.
	 */
	REACH_INDEX_RECEIVED,
	
	/**
	 * クライアントからのロンするというメッセージ.
	 * @param empty
	 */
	RON_RECEIVED,
	
	/**
	 * クライアントからのロン、明槓、ポン、チーどれもしないというメッセージ.
	 * @param empty
	 */
	REJECT_RECEIVED,
	
	/**
	 * ツモ和了するというメッセージ.
	 * @param empty.
	 */
	TSUMO_AGARI_RECEIVED,
	
	/**
	 * 局が終わったあとの結果表示を終わらせるというメッセージ.
	 * @param empty.
	 */
	NEXT_KYOKU_REQUESTED
}