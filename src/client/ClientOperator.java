package client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pages.Page;
import server.Server;
import system.Hai;
import system.HurohaiList;
import system.Kaze;
import system.KyokuResult;
import system.Mentu;
import system.Player;
import test.GlobalVar;

public class ClientOperator implements Client{
	private MajanCanvas canvas;
	private Server tr;
	private Page page;
	
	public ClientOperator(Server tr){
		this.tr = tr;
	}
	
	public void setPage(Page page){
		this.page = page;
		if(page != null)
			System.out.println(page.getPageName());
		else
			System.out.println("null");
		System.out.println(page instanceof MajanCanvas);
		if(page instanceof MajanCanvas)
			canvas = (MajanCanvas)this.page;
	}
	public void setCanvas(MajanCanvas canvas){
		this.canvas = canvas;
		this.page = canvas;
	}
	
	public ClientOperator(MajanCanvas c) {
		this.canvas = c;
	}

	public void setTransporter(Server tr){
		this.tr = tr;
	}
	
	
	public void sendDiscardIndex(int index) {
		if(canvas == null)
			return;
		hideFocus();
		canvas.getInfo().tsumoHai = null;
		canvas.refreshStateCodes();
		tr.onDiscardIndexReceived(index);
	}
	
	public void onTsumoGiriReceived(){
		if(canvas == null)
			return;
		canvas.getInfo().tsumoHai = null;
		canvas.refreshStateCodes();
	}

//	public void sendKyusyukyuhai() {
//		canvas.refreshStateCodes();
//		tr.onKyusyukyuhaiReceived();
//	}

	public void sendChiIndexList(List<Integer> hais) {
		if(canvas == null)
			return;
		canvas.refreshStateCodes();
		tr.onChiIndexListReceived(hais!=null?new ArrayList<Integer>(hais):null);
	}

	public void sendPonIndexList(List<Integer> hais) {
		if(canvas == null)
			return;
		canvas.refreshStateCodes();
		tr.onPonIndexListReceived(hais!=null?new ArrayList<Integer>(hais):null);
	}

	public void sendAnkanIndexList(List<Integer> hais) {
		if(canvas == null)
			return;
		canvas.refreshStateCodes();
		tr.onAnkanIndexListReceived(hais!=null?new ArrayList<Integer>(hais):null);
	}

	public void sendMinkan(boolean answer) {
		if(canvas == null)
			return;
		canvas.refreshStateCodes();
		tr.onMinkanableIndexReceived(answer);
	}

	public void sendKakanIndex(int index) {
		if(canvas == null)
			return;
		canvas.refreshStateCodes();
		tr.onKakanableIndexReceived(index);
	}

	public void sendReachIndex(int index) {
		if(canvas == null)
			return;
		canvas.refreshStateCodes();
		if(index != -1)
			canvas.getInfo().tsumoHai = null;
		tr.onReachIndexReceived(index);
	}

	public void sendRon(boolean answer) {
		if(canvas == null)
			return;
		canvas.refreshStateCodes();
		tr.onRonReceived(answer);
	}
	
	public void sendKyusyukyuhai(boolean flag){
		tr.onKyusyukyuhaiReceived(flag);
	}


	public void onChiableIndexListsReceived(List<List<Integer>> hais) {
		if(canvas == null)
			return;
		setFocus();
		canvas.getInfo().ableIndexList.put(StateCode.SELECT_CHI_HAI, hais);
		canvas.addButtonList(StateCode.SELECT_CHI);
		canvas.addStateCode(StateCode.SELECT_BUTTON);
	}

	public void onPonableIndexListsReceived(List<List<Integer>> hais) {
		if(canvas == null)
			return;
		setFocus();
		canvas.getInfo().ableIndexList.put(StateCode.SELECT_PON_HAI, hais);
		canvas.addButtonList(StateCode.SELECT_PON);
		canvas.addStateCode(StateCode.SELECT_BUTTON);
	}

	public void onMinkanableIndexListReceived(List<Integer> hais) {
		if(canvas == null)
			return;
		setFocus();
		List<List<Integer>> tmpList = new ArrayList<List<Integer>>();
		tmpList.add(hais);
		canvas.getInfo().ableIndexList.put(StateCode.SELECT_MINKAN, tmpList);
		canvas.addButtonList(StateCode.SELECT_MINKAN);
		canvas.addStateCode(StateCode.SELECT_BUTTON);
	}

	public void onKyusyukyuhaiRequested() {
		if(canvas == null)
			return;
		setFocus();
		canvas.addButtonList(StateCode.KYUSYUKYUHAI);
		canvas.addStateCode(StateCode.SELECT_BUTTON);
	}

	public void onAnkanableIndexListsReceived(List<List<Integer>> hais) {
		if(canvas == null)
			return;
		setFocus();
		canvas.getInfo().ableIndexList.put(StateCode.SELECT_ANKAN_HAI, hais);
		canvas.addStateCode(StateCode.SELECT_BUTTON);
		canvas.addButtonList(StateCode.SELECT_ANKAN);
	}

	public void onKakanableIndexListReceived(List<Integer> hais) {
		if(canvas == null)
			return;
		setFocus();
		List<List<Integer>> tmpList = new ArrayList<List<Integer>>();
		tmpList.add(hais);
		canvas.getInfo().ableIndexList.put(StateCode.SELECT_KAKAN_HAI, tmpList);
		canvas.addStateCode(StateCode.SELECT_BUTTON);
		canvas.addButtonList(StateCode.SELECT_KAKAN);
	}

	public void onReachableIndexListReceived(List<Integer> hais) {
		if(canvas == null)
			return;
		setFocus();
		List<List<Integer>> tmpList = new ArrayList<List<Integer>>();
		for(Integer i:hais){
			List<Integer> tmp = new ArrayList();
			tmp.add(i);
			tmpList.add(tmp);
		}
		canvas.getInfo().ableIndexList.put(StateCode.SELECT_REACH_HAI, tmpList);
		canvas.addStateCode(StateCode.SELECT_BUTTON);
		canvas.addButtonList(StateCode.SELECT_REACH);
	}

	public void onReachReceived(Kaze currentTurn,int sutehaiIndex){
		if(canvas == null)
			return;
		hideFocus();
		ClientInfo info = canvas.getInfo();
		int currentIndex = info.kaze.get(currentTurn);
		info.reachPosMap.put(currentIndex, sutehaiIndex + 1);
	}
	
	public void onTsumoAgariRequested() {
		if(canvas == null)
			return;
		canvas.addStateCode(StateCode.SELECT_BUTTON);
		canvas.addButtonList(StateCode.SELECT_TSUMO);
	}

	public void onRonRequested() {
		if(canvas == null)
			return;
		setFocus();
		canvas.addStateCode(StateCode.SELECT_BUTTON);
		canvas.addButtonList(StateCode.SELECT_RON);
	}


	public void onNakiReceived(Player player, Mentu mentu) {
		if(canvas == null)
			return;
		hideFocus();
		int i = -1;
		if(mentu.getKaze() != null) {
			i = canvas.getInfo().kaze.get(mentu.getKaze());
		}else {
			i = 0;
		}
		canvas.refreshStateCodes();
		canvas.refreshButtonList();
//		List<Sutehai> tmp = canvas.getInfo().sutehaiMap.get(i);
//		tmp.remove(tmp.size() - 1);
//		ClientInfo info = canvas.getInfo();
//		info.hurohaiMap.get(info.sekiMap.get(player)).add(mentu);
//		info.currentTurn = info.sekiMap.get(player);
	}

	public void onRonReceived(Map<Player, List<Hai>> playerHaiMap) {
		if(canvas == null)
			return;
		canvas.movePage("result");
	}

	public void onTsumoHaiReceived(Hai hai) {
		if(canvas == null)
			return;
		canvas.getInfo().tsumoHai = hai;
		canvas.addStateCode(StateCode.DISCARD_SELECT);
	}

	public void onDiscardReceived(boolean existTsumo) {
		if(canvas == null)
			return;
		setFocus();
		canvas.setExistTsumo(existTsumo);
		canvas.addStateCode(StateCode.DISCARD_SELECT);
	}

	public void onDiscardReceived(Player player, Hai hai, boolean isTsumo) {
		System.out.println(canvas == null);
		if(canvas == null)
			return;
		hideFocus();
		canvas.getInfo().tsumoHai = null;
	}

	@Override
	public void sendTsumoAgari() {
		if(canvas == null)
			return;
		canvas.refreshStateCodes();
		tr.onTsumoAgariReceived();
	}

	@Override
	public void onFieldReceived(List<Hai> tehai,
			Map<Kaze, HurohaiList> nakihaiMap,
			Map<Kaze, List<Hai>> sutehaiMap,
			Kaze currentTurn) {
		if(canvas == null)
			return;
		ClientInfo info = canvas.getInfo();
		info.tehai = tehai;
		info.currentTurn = info.kaze.get(currentTurn);
		for (Kaze k : Kaze.values()) {
			int i = info.kaze.get(k);
			synchronized (info.sutehaiMap) {
				info.sutehaiMap.put(i, sutehaiMap.get(k));
			}
			info.hurohaiMap.put(i, nakihaiMap.get(k));
		}
	}

	@Override
	public void onTsumoAgariReceived() {
		if(canvas == null)
			return;
		canvas.movePage("result");
	}
	//TODO for debug
	private void setFocus(){
		if(canvas == null)
			return;
		canvas.setFocus();
	}
	
	private void hideFocus(){
		if(canvas == null)
			return;
		canvas.hideFocus();
	}

	@Override
	public void requestGame(int id) {
		tr.onGameRequested(id);
		// TODO Auto-generated method stub
	}
	@Override
	public void onGameStartReceived(List<Player> playerList) {
		GlobalVar.players = new Player[playerList.size()];
		for(int i = 0;i <GlobalVar.players.length;i++)
			GlobalVar.players[i] = playerList.get(i);
		page.movePage("game");
		// TODO Auto-generated method stubx
	}
	@Override
	public void onStartKyokuReceived(Kaze bakaze, int kyokusu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKyokuResultReceived(KyokuResult result) {
		// TODO Auto-generated method stub
		
	}

}
