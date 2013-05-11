package ai;

import java.util.List;

import system.Kyoku;

/**
 * 麻雀を行うAIを表すインターフェース.
 * このインターフェースを実装したオブジェクトは麻雀におけるすべての選択肢における判断を提供する.
 */
public interface AI {
	/**
	 * 局オブジェクトを更新する。
	 * 局ごとに呼び出されて更新される。
	 * 
	 * @param kyoku 更新する局
	 */
	public void update(Kyoku kyoku);
	
	/**
	 * このAIが九種九牌すると判断した場合trueを返す.
	 * @return 九種九牌する場合true.
	 */
	public boolean isKyusyukyuhai();
	
	/**
	 * このAIがツモあがりすると判断した場合trueを返す.
	 * @return ツモあがりする場合true.
	 */
	public boolean isTumoAgari();
	
	/**
	 * このAIが加槓すると判断した場合,指定されたリストのインデックスを返す.
	 * 
	 * この引数のlistは手牌リストの中の加槓できるindexを持つリストである.
	 * たとえば[222],[東東東]を鳴いていて,手牌リストが[12349東西白]の場合,
	 * 2と東で加槓できるのでlistには[1,5]が入る.
	 * 
	 * 加槓しない場合-1を返す.
	 * 
	 * @param list 手牌リストの中の加槓できるindexを持つリスト.
	 * @return 指定されたリストのインデックス.加槓しない場合は-1.
	 */
	public int kakan(List<Integer> list);
	
	/**
	 * このAIが暗槓すると判断した場合,指定されたリストのインデックスを返す.
	 * 暗槓しない場合-1を返す.
	 * 
	 * @param lists 暗槓できる牌のインデックスのリストのリスト.
	 * @return 指定されたリストのインデックス.暗槓しない場合は-1.
	 */
	public int ankan(List<List<Integer>> lists);
	
	/**
	 * このAIが立直をすると判断した場合,trueを返す.
	 * 
	 * @return 立直をする場合はtrue.
	 */
	public boolean isReach();
	
	/**
	 * AIが判断した切る牌の手牌のインデックスを返す.
	 * ツモ切りの場合,13を返す.
	 * 
	 * @return 切る牌の手牌のインデックス.ツモ切りの場合は13.
	 */
	public int discard();
	
	/**
	 * このAIがロンをすると判断した場合,trueを返す.
	 * 
	 * @return ロンをする場合はtrue.
	 */
	public boolean isRon();
	
	/**
	 * このAIがポンすると判断した場合,指定されたリストのインデックスを返す.
	 * ポンしない場合-1を返す.
	 * 
	 * @param lists ポンできる牌のインデックスのリストのリスト.
	 * @return 指定されたリストのインデックス.ポンしない場合は-1.
	 */
	public int pon(List<List<Integer>> list);
	
	/**
	 * このAIがチーすると判断した場合,指定されたリストのインデックスを返す.
	 * チーしない場合-1を返す.
	 * 
	 * @param lists チーできる牌のインデックスのリストのリスト.
	 * @return 指定されたリストのインデックス.チーしない場合は-1.
	 */
	public int chi(List<List<Integer>> list);
	
	/**
	 * このAIが明槓をすると判断した場合,trueを返す.
	 * 
	 * @return 明槓をする場合はtrue.
	 */
	public boolean minkan();
}
