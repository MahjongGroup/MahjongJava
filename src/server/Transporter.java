package server;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import system.hai.Hai;
import system.hai.HurohaiList;
import system.hai.Kaze;
import system.hai.Mentsu;
import system.result.KyokuResult;
import system.test.Player;
import client.Client;
/**
 * クライアントと通信を行う,サーバー-クライアント間のインターフェースとなるクラス.
 */
public class Transporter implements Server ,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -574065982529849083L;
	private boolean grandFlag = false;
	private boolean kyusyukyuhaiReceivedFlag = false;
	private boolean kyusyukyuhaiReceivedResult = false;
	private boolean discardedReceivedFlag = false;
	private int discardedIndexResult = -1;
	private boolean tumoari = true;
	private boolean chiReceivedFlag = false;
	private List<Integer> chiIndexList = null;
	private boolean ponReceivedFlag = false;
	private List<Integer> ponIndexList = null;
	private boolean ankanReceivedFlag = false;
	private List<Integer> ankanIndexList = null;
	private boolean minkanReceivedFlag = false;
	private boolean isMinkanResult = false;
	private boolean kakanReceivedFlag = false;
	private int kakanIndex = -1;
	private boolean reachReceivedFlag = false;
	private int reachHaiIndex = -1;
	private boolean ronReceivedFlag = false;
	private boolean ronReceivedResult = false;
	private boolean tsumoagariReceivedResult = false;
	private boolean isWait = true;
	private boolean endResultPage = false;
	
	// DEBUG 結合用
	private Client client;
	
	// DEBUG 結合用
	/**
	 * 指定されたクライアントで生成するコンストラクタ.
	 * clientはnullで,後からsetしてもよい.
	 * 
	 * @param client
	 */
	public Transporter(Client client) {
		this.client = client;
	}
		
	/**
	 * コンストラクタ.
	 */
	public Transporter() {
	}

	public void init() {
		grandFlag = false;
		kyusyukyuhaiReceivedFlag = false;
		kyusyukyuhaiReceivedResult = false;
		discardedReceivedFlag = false;
		discardedIndexResult = -1;
		tumoari = true;
		chiReceivedFlag = false;
		chiIndexList = null;
		ponReceivedFlag = false;
		ponIndexList = null;
		ankanReceivedFlag = false;
		ankanIndexList = null;
		minkanReceivedFlag = false;
		isMinkanResult = false;
		kakanReceivedFlag = false;
		kakanIndex = -1;
		reachReceivedFlag = false;
		reachHaiIndex = -1;
		ronReceivedFlag = false;
		ronReceivedResult = false;
		tsumoagariReceivedResult = false;
		endResultPage = false;
		
	}
	
	// DEBUG 結合用
	public void setClient(Client client) {
		this.client = client;
	}
	
	
	public boolean getGrandFlag(){
		return grandFlag;
	}
	
	public boolean isKyusyukyuhaiReceived() {
		return kyusyukyuhaiReceivedFlag;
	}
	
	public boolean getKyusyukyuhaiResult() {
		return kyusyukyuhaiReceivedResult;
	}
	
	public boolean isDiscardedReceived(){
		return discardedReceivedFlag;
	}
	
	public int getDiscardedIndex(){
		return discardedIndexResult;
	}	
	
	public boolean isChiReceived(){
		return chiReceivedFlag;
	}
	
	public List<Integer> getChiIndexList(){
		return chiIndexList;
	}
	
	public boolean isPonReceived(){
		return ponReceivedFlag;
	}
	
	public List<Integer> getPonIndexList(){
		return ponIndexList;
	}
	
	public boolean isAnkanReceived(){
		return ankanReceivedFlag;
	}
	
	public List<Integer> getAnkanIndexList(){
		return ankanIndexList;
	}
	
	public boolean isMinkanReceived(){
		return minkanReceivedFlag;
	}
	
	public boolean isMinkanDo(){
		return isMinkanResult;
	}
	
	public boolean isKakanReceived(){
		return kakanReceivedFlag;
	}
	
	public int getKakanindex(){
		return kakanIndex;
	}
	
	public boolean isReachReceived(){
		return reachReceivedFlag;
	}
	
	public int getReachHaiIndex(){
		return reachHaiIndex;
	}
	
	public boolean isRonReceived(){
		return ronReceivedFlag;
	}
	
	public boolean isRonDo(){
		return ronReceivedResult;
	}
	
	
	public boolean isTsumoagariDo(){
		return tsumoagariReceivedResult;
	}
	
	public boolean isThereTsumohai(){
		return tumoari;
	}
	
	public boolean isEndResultPage(){
		return endResultPage;
	}
	
	// 九種九牌できるときに流局するかどうかをクライアントに聞く
	@Override
	public void requestKyusyukyuhai() {
		client.onKyusyukyuhaiRequested();
	}

	// 九種九牌するかどうかを受信する
	@Override
	public void onKyusyukyuhaiReceived(boolean answer) {
		//boolean answer = false;
		kyusyukyuhaiReceivedFlag = true;
		grandFlag = true;
		kyusyukyuhaiReceivedResult = answer;
	}

	// ツモした牌をクライアント側に送る
	@Override
	public void sendTsumoHai(Hai hai) {
		client.onTsumoHaiReceived(hai);
	}

	// どの牌を切るかを聞く
	@Override
	public void sendDiscard(boolean tsumoari) {
		client.onDiscardReceived(tsumoari);
	}
	
	@Override
	public void sendTsumoGiri(){
		client.onTsumoGiriReceived();
	}

	// クライアントが切った牌のインデックスを受け取る
	@Override
	public void onDiscardIndexReceived(int index) {
		grandFlag = true;
		discardedReceivedFlag = true;
		discardedIndexResult = index;
		
	}
	

	// チーできるときにチーするかどうか聞く,手牌の中でチーできる2枚を渡す
	@Override
	public void sendChiableIndexLists(List<List<Integer>> list) {
		client.onChiableIndexListsReceived(list);
	}
	

	// チーすると返ってきたら,チーした牌のリストが返ってくるのを待つ
	@Override
	public void onChiIndexListReceived(List<Integer> list) {
		chiIndexList = list;
		chiReceivedFlag = true;
		tumoari = false;
		System.out.println("onChiIndexReceived");
	}

	// ポンできるときにポンするかどうかを聞く,手牌の中でポンできる2枚を渡す
	@Override
	public void sendPonableIndexLists(List<List<Integer>> list) {
		client.onPonableIndexListsReceived(list);
	}

	// ポンすると返ってきたら,ポンした牌のリストが返ってくるのを待つ
	@Override
	public void onPonIndexListReceived(List<Integer> list) {
		ponIndexList = list;
		ponReceivedFlag = true;
		tumoari = false;
	}

	// 暗槓できるときに暗槓するかどうかを聞く,手牌の中で槓できる牌リストを渡す
	@Override
	public void sendAnkanableIndexLists(List<List<Integer>> list) {
		client.onAnkanableIndexListsReceived(list);
	}

	// 暗槓すると返ってきたら,槓した牌のタイプが返ってくるのを待つ
	@Override
	public void onAnkanIndexListReceived(List<Integer> list) {
		grandFlag = true;
		ankanReceivedFlag = true;
		ankanIndexList = list;
	}

	// 明槓できるときに明槓するかどうかを聞く,手牌の中で槓できる牌リストを渡す
	@Override
	public void sendMinkanableIndexList(List<Integer> indexList) {
		client.onMinkanableIndexListReceived(indexList);
	}

	// 明槓すると返ってきたら,明槓するかしないかの返答が返ってくるのを待つ
	@Override
	public void onMinkanableIndexReceived(boolean answer) {
		minkanReceivedFlag = true;
		isMinkanResult = answer;
	}

	// 加槓できるときに加槓するかどうかを聞く,手牌の中で槓できる牌リストを渡す
	@Override
	public void sendKakanableIndexList(List<Integer> list) {
		client.onKakanableIndexListReceived(list);
	}

	// 加槓すると返ってきたら,槓した牌のタイプが返ってくるのを待つ
	@Override
	public void onKakanableIndexReceived(int index) {
		grandFlag = true;
		kakanReceivedFlag = true;
		kakanIndex = index;
	}

	// リーチできるときにリーチするかどうかを聞く
	@Override
	public void sendReachableIndexList(List<Integer> list) {
		client.onReachableIndexListReceived(list);
	}

	// リーチしたとき,そのリーチした牌のインデックスを返す
	@Override
	public void onReachIndexReceived(int index) {
		grandFlag = true;
		reachReceivedFlag = true;
		reachHaiIndex = index;
	}

	// ロンできるときにロンするかどうかを聞く
	@Override
	public void requestRon() {
		client.onRonRequested();
	}

	// ロンしたかどうかを受信する
	@Override
	public void onRonReceived(boolean ron) {
		ronReceivedFlag = true;
		ronReceivedResult = ron;
	}

	// ツモ上がりできるときにあがるかどうかを聞く
	@Override
	public void requestTsumoAgari() {
		client.onTsumoAgariRequested();
	}

	// ツモ上がりしたかどうかを受信する
	@Override
	public void onTsumoAgariReceived(){
		tsumoagariReceivedResult = true;
		grandFlag = true;
	}

	public void sendField(List<Hai> tehai,Map<Kaze,HurohaiList> nakihai,Map<Kaze, List<Hai>> sutehai,Kaze currentTurn,
			Hai currentSutehai,List<Integer> tehaiSize,int yamaSize,int wanpaiSize,List<Hai> doraList) {
		client.onFieldReceived(tehai,nakihai,sutehai,currentTurn,currentSutehai,tehaiSize,yamaSize,wanpaiSize,doraList);
	}

	@Override
	public void notifyDiscard(Player player, Hai hai, boolean tumokiri) {
		client.onDiscardReceived(player, hai, tumokiri);
	}

	@Override
	public void notifyNaki(Player player, Mentsu mentu) {
		client.onNakiNotified(player, mentu);
	}

	@Override
	public void notifyRon(Map<Player, List<Hai>> map) {
		client.onRonReceived(map);
	}
	
	@Override
	public void notifyReach(Kaze currentTurn,int sutehaiIndex){
		client.onReachReceived(currentTurn,sutehaiIndex);
	}

	@Override
	public void onGameRequested(int id) {
		// TODO to be changed
		//for debug
		isWait = false;
	}

	@Override
	public void sendGameStart(List<Player> playerList,int index, int[] scores) {
		client.onGameStartReceived(playerList,index,scores);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyStartKyoku(Kaze bakaze, int kyokusu,int honba ,int tsumibou) {
		client.onStartKyokuReceived(bakaze, kyokusu,honba,tsumibou);
		
	}
	
	//TODO to be removed
	//for debug	
	public boolean isWait(){
		return isWait;
	}
	//TODO to be removed
	//for debug	
	public void setWait(boolean flag){
		isWait = flag;
	}
	
	@Override
	public void notifyKyokuResult(KyokuResult result,int[] newScore,int[] oldScore,List<Integer> soten,List<Hai> uradoraList) {
		client.onKyokuResultReceived(result,newScore,oldScore,soten,uradoraList);
	}

	@Override
	public void onNextKyokuRequested(){
		System.out.println("call");
		endResultPage = true;
	}
	
	@Override
	public  void notifyTempai(Map<Player,List<Hai>> map){
//		client.onTempaiReceived(map);
	}	
	
	public void notifyGameResult(int[] score){
		client.onGameResultReceived(score);
	}
	
	public void sendGameOver(){
		client.onGameOverReceived();
	}
//	@Override
//	public void sendField(List<Hai> tehai, Map<Kaze, HurohaiList> nakihai,
//			Map<Kaze, List<Hai>> sutehai, Kaze currentTurn, Hai currentSutehai,
//			List<Integer> tehaiSize, int yamaSize, int wanpaiSize,
//			List<HaiType> doraList) {
//		// TODO Auto-generated method stub
//		
}

