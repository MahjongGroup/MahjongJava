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
	 * クライアントの九種九牌するかどうかのメッセージ.
	 * @param answer 九種九牌する場合true.boolean型.
	 */
	KYUSYUKYUHAI_RECEIVED,
	
	/**
	 * クライアントから切る牌のインデックスを受け取る。
	 * @param index　切る牌のインデックス.ツモ牌の場合は13.牌を切らない場合は-1.整数型.
	 */
	DISCARD_INDEX_RECEIVED,
	
	/**
	 * クライアントからチーする牌のインデックスのリストを受け取る。
	 * @param list チーするリストの牌のインデックス。チーしない場合はnull.整数リスト型.
	 */
	CHIINDEX_LIST_RECEIVED,
	
	/**
	 * クライアントからポンする牌のインデックスのリストを受け取る。
	 * @param list ポンするリストの牌のインデックス。ポンしない場合はnull.整数リスト型.
	 */
	PON_INDEX_LIST_RECEIVED,
	
	/**
	 * クライアントから暗槓する牌のインデックスのリストを受け取る。
	 * @param list 暗槓するリストの牌のインデックス。暗槓しない場合はnull。整数リスト型.
	 */
	ANKAN_INDEX_LIST_RECEIVED,
	
	/**
	 * クライアントから明槓するかどうかの結果を受け取る。
	 * @param answer 明槓するならtrue.boolean型.
	 */
	MINKAN_RECEIVED,
	
	/**
	 * クライアントから加槓するかどうかの結果を受け取る。
	 * @param index 加槓する牌のインデックス。ツモ牌の場合は13。加槓しない場合は-1.整数型.
	 */
	KAKANABLE_INDEX_RECEIVED,

	/**
	 * クライアントから加槓するかどうかの結果を受け取る。
	 * @param index 加槓する牌のインデックス。ツモ牌の場合は13。加槓しない場合は-1.整数型.
	 */
	REACH_INDEX_RECEIVED,
	
	/**
	 * クライアントからロンするかどうかの結果を受け取る.
	 * @param result ロンする場合はtrue.boolean型.
	 */
	RON_RECEIVED,
	
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