package client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pages.Page;
import pages.ResultPage;
import server.Server;
import server.Transporter;
import system.Hai;
import system.HurohaiList;
import system.Kaze;
import system.KyokuResult;
import system.Mentu;
import system.Mentu.Type;
import system.Player;

public class ClientOperator implements Client{
	private Server tr;
	private Page page;
	private MajanFrame frame;
	
	public void setFrame(MajanFrame frame){
		this.frame = frame;
	}
	public ClientOperator(){
	}
	public void setTransporter(Transporter tr){
		this.tr = tr;
	}
	
	public boolean isPageIsCanvas(){
		return page instanceof MajanCanvas;
	}
	
	public ClientOperator(Server tr){
		this.tr = tr;
	}
	
	public void setPage(Page page){
		this.page = page;
	}
	
	
	public ClientOperator(MajanCanvas c) {
		this.page = c;
	}

	public void setTransporter(Server tr){
		this.tr = tr;
	}
	
	
	public void sendDiscardIndex(int index) {
		if(page == null || !isPageIsCanvas())
			return;
		MajanCanvas canvas = (MajanCanvas)page;
		tr.onDiscardIndexReceived(index);
		canvas.getInfo().tsumoHai = null;
		canvas.refreshStateCodes();
	}
	
	public void onTsumoGiriReceived(){
		if(page == null || !isPageIsCanvas())
			return;
		MajanCanvas canvas = (MajanCanvas)page;
		canvas.getInfo().tsumoHai = null;
		canvas.refreshStateCodes();
	}


	public void sendChiIndexList(List<Integer> hais) {
		if(page == null || !isPageIsCanvas())
			return;
		MajanCanvas canvas = (MajanCanvas)page;
		tr.onChiIndexListReceived(hais!=null?new ArrayList<Integer>(hais):null);
		canvas.refreshStateCodes();
	}

	public void sendPonIndexList(List<Integer> hais) {
		if(page == null || !isPageIsCanvas())
			return;
		MajanCanvas canvas = (MajanCanvas)page;
		tr.onPonIndexListReceived(hais!=null?new ArrayList<Integer>(hais):null);
		canvas.refreshStateCodes();
	}

	public void sendAnkanIndexList(List<Integer> hais) {
		if(page == null || !isPageIsCanvas())
			return;
		MajanCanvas canvas = (MajanCanvas)page;
		tr.onAnkanIndexListReceived(hais!=null?new ArrayList<Integer>(hais):null);
		canvas.refreshStateCodes();
	}

	public void sendMinkan(boolean answer) {
		if(page == null || !isPageIsCanvas())
			return;
		MajanCanvas canvas = (MajanCanvas)page;
		tr.onMinkanableIndexReceived(answer);
		canvas.refreshStateCodes();
	}

	public void sendKakanIndex(int index) {
		if(page == null || !isPageIsCanvas())
			return;
		MajanCanvas canvas = (MajanCanvas)page;
		tr.onKakanableIndexReceived(index);
		canvas.refreshStateCodes();
	}

	public void sendReachIndex(int index) {
		if(page == null || !isPageIsCanvas())
			return;
		MajanCanvas canvas = (MajanCanvas)page;
		tr.onReachIndexReceived(index);
		canvas.refreshStateCodes();
		if(index != -1)
			canvas.getInfo().tsumoHai = null;
	}

	public void sendRon(boolean answer) {
		if(page == null || !isPageIsCanvas())
			return;
		MajanCanvas canvas = (MajanCanvas)page;
		tr.onRonReceived(answer);
		canvas.refreshStateCodes();
	}
	
	public void sendKyusyukyuhai(boolean flag){
		tr.onKyusyukyuhaiReceived(flag);
	}


	public void onChiableIndexListsReceived(List<List<Integer>> hais) {
		if(page == null || !isPageIsCanvas())
			return;
		MajanCanvas canvas = (MajanCanvas)page;
		//TODO to be removed
		canvas.getInfo().tsumoHai = null;
		canvas.addButtonList(StateCode.SELECT_CHI);
		canvas.addStateCode(StateCode.SELECT_BUTTON);
		canvas.getInfo().ableIndexList.put(StateCode.SELECT_CHI_HAI, hais);
	}

	public void onPonableIndexListsReceived(List<List<Integer>> hais) {
		if(page == null || !isPageIsCanvas())
			return;
		MajanCanvas canvas = (MajanCanvas)page;
		//TODO to be removed
		canvas.getInfo().tsumoHai = null;
		
		canvas.addButtonList(StateCode.SELECT_PON);
		canvas.addStateCode(StateCode.SELECT_BUTTON);
		canvas.getInfo().ableIndexList.put(StateCode.SELECT_PON_HAI, hais);
	}

	public void onMinkanableIndexListReceived(List<Integer> hais) {
		if(page == null || !isPageIsCanvas())
			return;
		MajanCanvas canvas = (MajanCanvas)page;
		//TODO to be removed
		canvas.getInfo().tsumoHai = null;
		
		List<List<Integer>> tmpList = new ArrayList<List<Integer>>();
		tmpList.add(hais);
		canvas.addButtonList(StateCode.SELECT_MINKAN);
		canvas.addStateCode(StateCode.SELECT_BUTTON);
		canvas.getInfo().ableIndexList.put(StateCode.SELECT_MINKAN, tmpList);
	}

	public void onKyusyukyuhaiRequested() {
		if(page == null || !isPageIsCanvas())
			return;
		MajanCanvas canvas = (MajanCanvas)page;
		
		canvas.addButtonList(StateCode.KYUSYUKYUHAI);
		canvas.addStateCode(StateCode.SELECT_BUTTON);
	}

	public void onAnkanableIndexListsReceived(List<List<Integer>> hais) {
		if(page == null || !isPageIsCanvas())
			return;
		MajanCanvas canvas = (MajanCanvas)page;
		
		canvas.addStateCode(StateCode.SELECT_BUTTON);
		canvas.addButtonList(StateCode.SELECT_ANKAN);
		canvas.getInfo().ableIndexList.put(StateCode.SELECT_ANKAN_HAI, hais);
	}

	public void onKakanableIndexListReceived(List<Integer> hais) {
		if(page == null || !isPageIsCanvas())
			return;
		MajanCanvas canvas = (MajanCanvas)page;
		
		List<List<Integer>> tmpList = new ArrayList<List<Integer>>();
		tmpList.add(hais);
		canvas.addStateCode(StateCode.SELECT_BUTTON);
		canvas.addButtonList(StateCode.SELECT_KAKAN);
		canvas.getInfo().ableIndexList.put(StateCode.SELECT_KAKAN_HAI, tmpList);
	}

	public void onReachableIndexListReceived(List<Integer> hais) {
		if(page == null || !isPageIsCanvas())
			return;
		MajanCanvas canvas = (MajanCanvas)page;
		
		List<List<Integer>> tmpList = new ArrayList<List<Integer>>();
		for(Integer i:hais){
			List<Integer> tmp = new ArrayList<Integer>();
			tmp.add(i);
			tmpList.add(tmp);
		}
		canvas.addStateCode(StateCode.SELECT_BUTTON);
		canvas.addButtonList(StateCode.SELECT_REACH);
		canvas.getInfo().ableIndexList.put(StateCode.SELECT_REACH_HAI, tmpList);
	}

	public void onReachReceived(Kaze currentTurn,int sutehaiIndex){
		if(page == null || !isPageIsCanvas())
			return;
		MajanCanvas canvas = (MajanCanvas)page;
		ClientInfo info = canvas.getInfo();
		canvas.startAnimation(info.kaze.get(currentTurn),StateCode.SELECT_REACH);
		int currentIndex = info.kaze.get(currentTurn);
		info.reachPosMap.put(currentIndex, sutehaiIndex + 1);
	}
	
	public void onTsumoAgariRequested() {
		if(page == null || !isPageIsCanvas())
			return;
		MajanCanvas canvas = (MajanCanvas)page;
		canvas.addStateCode(StateCode.SELECT_BUTTON);
		canvas.addButtonList(StateCode.SELECT_TSUMO);
	}

	public void onRonRequested() {
		if(page == null || !isPageIsCanvas())
			return;
		MajanCanvas canvas = (MajanCanvas)page;
		//TODO to be removed
		canvas.getInfo().tsumoHai = null;
		
		canvas.addStateCode(StateCode.SELECT_BUTTON);
		canvas.addButtonList(StateCode.SELECT_RON);
	}


	public void onNakiReceived(Player player, Mentu mentu) {
		if(page == null || !isPageIsCanvas())
			return;
		MajanCanvas canvas = (MajanCanvas)page;
		int index;
		for(index = 0;index < canvas.getInfo().players.length;index++){
			if(canvas.getInfo().players[index] == player)
				break;
		}
		switch (mentu.type()) {
		case KANTU:
			canvas.startAnimation(index,StateCode.SELECT_KAN);
			break;
		case SYUNTU:
			canvas.startAnimation(index,StateCode.SELECT_CHI);
			break;
		case KOTU:
			canvas.startAnimation(index,StateCode.SELECT_PON);
			break;
		default:
			break;
		}
		canvas.refreshStateCodes();
		canvas.refreshButtonList();
	}

	public void onRonReceived(Map<Player, List<Hai>> playerHaiMap) {
		if(page == null || !isPageIsCanvas())
			return;
		MajanCanvas canvas = (MajanCanvas)page;
		canvas.startAnimation(-1,StateCode.SELECT_RON);
		try{
			Thread.sleep(100);
		}catch(InterruptedException e){}
		page.movePage("result");
	}

	public void onTsumoHaiReceived(Hai hai) {
		if(page == null || !isPageIsCanvas())
			return;
		MajanCanvas canvas = (MajanCanvas)page;
		canvas.getInfo().tsumoHai = hai;
		canvas.addStateCode(StateCode.DISCARD_SELECT);
	}

	public void onDiscardReceived(boolean existTsumo) {
		if(page == null || !isPageIsCanvas())
			return;
		MajanCanvas canvas = (MajanCanvas)page;
		
		canvas.addStateCode(StateCode.DISCARD_SELECT);
	}

	public void onDiscardReceived(Player player, Hai hai, boolean isTsumo) {
		System.out.println(page == null);
		if(page == null || !isPageIsCanvas())
			return;
		MajanCanvas canvas = (MajanCanvas)page;
		
		canvas.getInfo().tsumoHai = null;
	}

	@Override
	public void sendTsumoAgari() {
		if(page == null || !isPageIsCanvas())
			return;
		MajanCanvas canvas = (MajanCanvas)page;
		tr.onTsumoAgariReceived();
		canvas.refreshStateCodes();
	}



	@Override
	public void onTsumoAgariReceived() {
		if(page == null || !isPageIsCanvas())
			return;
		MajanCanvas canvas = (MajanCanvas)page;
		canvas.startAnimation(-1,StateCode.SELECT_TSUMO);
		try{
			Thread.sleep(100);
		}catch(InterruptedException e){}
		page.movePage("result");
	}
	@Override
	public void requestGame(int id) {
		tr.onGameRequested(id);
		// TODO Auto-generated method stub
	}
	@Override
	public void onGameStartReceived(List<Player> playerList,int index,int[] score) {
		Map<Kaze, Player> playerMap = new HashMap<Kaze, Player>(4);
		for(int i = 0; i < playerList.size(); i++) {
			playerMap.put(Kaze.valueOf(i), playerList.get(i));
		}
		
		Kaze playerKaze = Kaze.valueOf(index);
		
		
		// TODO insert score
		Player[] players = new Player[playerList.size()];
		for(int i = 0;i < playerList.size();i++)
			players[i] = playerList.get(i);
		//TODO to be changed
		index = 1;
		ClientInfo tmpInfo = frame.getInfo();
		tmpInfo.setIndex(index);
		tmpInfo.playerNumber = index;
		tmpInfo.players = players;
		tmpInfo.sekiMap = new HashMap<Player, Integer>(4);
		for (int i = 0; i < 4; i++) {
			tmpInfo.sekiMap.put(tmpInfo.players[i], (4 - index + i)%4);
			tmpInfo.setScore((4 - index + i) % 4, score[i]);
		}
//		tmpInfo.setIndex(index);
		frame.setInfo(tmpInfo);
		frame.setPage("game");
	}
	@Override
	public void onStartKyokuReceived(Kaze bakaze, int kyokusu,int honba,int tsumibou) {
		// TODO Auto-generated method stub
		if(page == null)
			return;
		page.movePage("game");
		frame.getInfo().honba = honba;
		frame.getInfo().tsumiBou = tsumibou;
		MajanCanvas canvas = (MajanCanvas)page;
		canvas.number = frame.getInfo().playerNumber;
		canvas.setKyokusu(kyokusu);
		canvas.setBakaze(bakaze);
	}


	public void requestNextKyoku(){
		System.out.println("requestNextKyoku");
		tr.onNextKyokuRequested();
	}
	
	public void onGameResultReceived(int[] score){
		//TODO to be changed
	}
	
	
	@Override
	public void onFieldReceived(List<Hai> tehai,
			Map<Kaze, HurohaiList> nakihai, Map<Kaze, List<Hai>> sutehai,
			Kaze currentTurn, Hai currentSutehai, List<Integer> tehaiSize,
			int yamaSize, int wanpaiSize, List<Hai> doraList) {
		if(page == null || !isPageIsCanvas())
			return;
		MajanCanvas canvas = (MajanCanvas)page;
		ClientInfo info = canvas.getInfo();
		info.tehai = tehai;
		info.currentTurn = info.kaze.get(currentTurn);
		for (Kaze k : Kaze.values()) {
			int i = info.kaze.get(k);
			synchronized (info.sutehaiMap) {
				info.sutehaiMap.put(i, sutehai.get(k));
			}
			if(k == currentTurn)
				info.sutehaiMap.get(i).add(currentSutehai);
			info.hurohaiMap.put(i, nakihai.get(k));
		}

		info.tehaiSizeMap.put(info.kaze.get(Kaze.TON), tehaiSize.get(0));
		info.tehaiSizeMap.put(info.kaze.get(Kaze.NAN), tehaiSize.get(1));
		info.tehaiSizeMap.put(info.kaze.get(Kaze.SYA), tehaiSize.get(2));
		info.tehaiSizeMap.put(info.kaze.get(Kaze.PE), tehaiSize.get(3));
		

		info.yamaSize = yamaSize;
		info.wanpaiSize = wanpaiSize;
		info.doraList = doraList;
	}

	@Override
	public void onKyokuResultReceived(KyokuResult result, int[] newScores,
			int[] oldScores,List<Integer> changeScore, List<Hai> uradoraList) {
		try{
			Thread.sleep(100);
		}catch(InterruptedException e){}
		page.movePage("result");
		((ResultPage)page).setResult(result,newScores,oldScores,changeScore,uradoraList);
		//TODO ok?
	}
}
