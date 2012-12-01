package client;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import system.Hai;
import system.HurohaiList;
import system.Kaze;
import system.Mentu;
import system.Player;
import test.GlobalVar;

public class ClientOperator implements Client{
	private MajanCanvas canvas;

	public ClientOperator(MajanCanvas c) {
		this.canvas = c;
	}

	public void sendDiscardIndex(int index) {
		canvas.getInfo().tsumoHai = null;
		canvas.refreshStateCodes();
		GlobalVar.trs[canvas.number].onDiscardIndexReceived(index);
	}
	
	public void onTsumoGiriReceived(){
		canvas.getInfo().tsumoHai = null;
		canvas.refreshStateCodes();
	}

//	public void sendKyusyukyuhai() {
//		canvas.refreshStateCodes();
//		GlobalVar.trs[canvas.number].onKyusyukyuhaiReceived();
//	}

	public void sendChiIndexList(List<Integer> hais) {
		canvas.refreshStateCodes();
		GlobalVar.trs[canvas.number]
				.onChiIndexListReceived(hais!=null?new ArrayList<Integer>(hais):null);
	}

	public void sendPonIndexList(List<Integer> hais) {
		System.out.println(hais);
		canvas.refreshStateCodes();
		GlobalVar.trs[canvas.number].onPonIndexListReceived(hais!=null?new ArrayList<Integer>(hais):null);
	}

	public void sendAnkanIndexList(List<Integer> hais) {
		canvas.refreshStateCodes();
		GlobalVar.trs[canvas.number].onAnkanIndexListReceived(hais!=null?new ArrayList<Integer>(hais):null);
	}

	public void sendMinkan(boolean answer) {
		canvas.refreshStateCodes();
		GlobalVar.trs[canvas.number].onMinkanableIndexReceived(answer);
	}

	public void sendKakanIndex(int index) {
		canvas.refreshStateCodes();
		GlobalVar.trs[canvas.number].onKakanableIndexReceived(index);
	}

	public void sendReachIndex(int index) {
		canvas.refreshStateCodes();
		if(index != -1)
			canvas.getInfo().tsumoHai = null;
		GlobalVar.trs[canvas.number].onReachIndexReceived(index);
	}

	public void sendRon(boolean answer) {
		
		canvas.refreshStateCodes();
		GlobalVar.trs[canvas.number].onRonReceived(answer);
	}
	
	public void sendKyusyukyuhai(boolean flag){
		GlobalVar.trs[canvas.number].onKyusyukyuhaiReceived(flag);
	}


	public void onChiableIndexListsReceived(List<List<Integer>> hais) {
		canvas.getInfo().ableIndexList.put(StateCode.SELECT_CHI_HAI, hais);
		canvas.addButtonList(StateCode.SELECT_CHI);
		canvas.addStateCode(StateCode.SELECT_BUTTON);
	}

	public void onPonableIndexListsReceived(List<List<Integer>> hais) {
		canvas.getInfo().ableIndexList.put(StateCode.SELECT_PON_HAI, hais);
		canvas.addButtonList(StateCode.SELECT_PON);
		canvas.addStateCode(StateCode.SELECT_BUTTON);
	}

	public void onMinkanableIndexListReceived(List<Integer> hais) {
		List<List<Integer>> tmpList = new ArrayList<List<Integer>>();
		tmpList.add(hais);
		canvas.getInfo().ableIndexList.put(StateCode.SELECT_MINKAN, tmpList);
		canvas.addButtonList(StateCode.SELECT_MINKAN);
		canvas.addStateCode(StateCode.SELECT_BUTTON);
	}

	public void onKyusyukyuhaiRequested() {
		canvas.addButtonList(StateCode.KYUSYUKYUHAI);
		canvas.addStateCode(StateCode.SELECT_BUTTON);
	}

	public void onAnkanableIndexListsReceived(List<List<Integer>> hais) {
		canvas.getInfo().ableIndexList.put(StateCode.SELECT_ANKAN_HAI, hais);
		canvas.addStateCode(StateCode.SELECT_BUTTON);
		canvas.addButtonList(StateCode.SELECT_ANKAN);
	}

	public void onKakanableIndexListReceived(List<Integer> hais) {
		List<List<Integer>> tmpList = new ArrayList<List<Integer>>();
		tmpList.add(hais);
		canvas.getInfo().ableIndexList.put(StateCode.SELECT_KAKAN_HAI, tmpList);
		canvas.addStateCode(StateCode.SELECT_BUTTON);
		canvas.addButtonList(StateCode.SELECT_KAKAN);
	}

	public void onReachableIndexListReceived(List<Integer> hais) {
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
		ClientInfo info = canvas.getInfo();
		int currentIndex = info.kaze.get(currentTurn);
		info.reachPosMap.put(currentIndex, sutehaiIndex);
	}
	
	public void onTsumoAgariRequested() {
		canvas.addStateCode(StateCode.SELECT_BUTTON);
		canvas.addButtonList(StateCode.SELECT_TSUMO);
	}

	public void onRonRequested() {
		canvas.addStateCode(StateCode.SELECT_BUTTON);
		canvas.addButtonList(StateCode.SELECT_RON);
	}


	public void onNakiReceived(Player player, Mentu mentu) {
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
	}

	public void onTsumoHaiReceived(Hai hai) {
		canvas.getInfo().tsumoHai = hai;
		canvas.addStateCode(StateCode.DISCARD_SELECT);
	}

	public void onDiscardReceived(boolean existTsumo) {
		canvas.setExistTsumo(existTsumo);
		canvas.addStateCode(StateCode.DISCARD_SELECT);
	}

	public void onDiscardReceived(Player player, Hai hai, boolean isTsumo) {
		canvas.getInfo().tsumoHai = null;
//		ClientInfo info = canvas.getInfo();
		// info.sutehaiMap.get(player).add(hai);
//		info.sutehai = hai;
//		Map<Integer, Integer> tmp = info.tehaiSizeMap;
//		if (info.tsumoHai != null)
//			tmp.put(info.sekiMap.get(player), tmp.get(info.currentTurn) - 1);
//		info.sutehaiMap.get(info.sekiMap.get(player)).add(hai);
//		info.currentTurn = (info.sekiMap.get(player) + 1) % 4;
	}

	@Override
	public void sendTsumoAgari() {
		canvas.refreshStateCodes();
		GlobalVar.trs[canvas.number].onTsumoAgariReceived();
	}

	@Override
	public void onFieldReceived(List<Hai> tehai,
			Map<Kaze, HurohaiList> nakihaiMap,
			Map<Kaze, List<Hai>> sutehaiMap,
			Kaze currentTurn) {
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
		canvas.movePage("result");
		// TODO Auto-generated method stub		
	}

}
