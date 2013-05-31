package test.client.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import system.Player;
import system.hai.Hai;
import system.hai.Kaze;
import system.hai.Mentsu;
import system.result.KyokuResult;
import test.client.ClientListener;
import test.client.ClientSender;
import util.MyLogger;

public class GameManager implements ClientListener {
	private final static MyLogger logger = MyLogger.getLogger();
	
	private ClientSender sender;

	private Kaze bakaze;

	private List<Hai> tehai;

	private Hai tsumohai;
	private boolean kiri;
	private boolean reach;

	private List<Integer> reachableIndexList;

	public void setSender(ClientSender sender)	 {
		this.sender = sender;
	}
	
	public Kaze bakaze() {
		return bakaze;
	}

	public List<Hai> tehai() {
		return tehai;
	}

	public Hai tsumohai() {
		return tsumohai;
	}

	public boolean onHaiPressed(int index) {
		logger.debug("index : " + index);
		if((index != 13 && index > tehai.size()) || index < 0) {
			return false;
		}
		
		if (reach) {
			if (!this.reachableIndexList.contains(index))
				return false;
			sender.sendReachIndex(index);
			kiri = false;
			reach = false;
			return true;
		} else if (kiri) {
			if(index != 13) {
				tehai.remove(index);
				tehai.add(tsumohai);
				Collections.sort(tehai, Hai.HaiComparator.ASCENDING_ORDER);
			}
			sender.sendDiscardIndex(index);
			kiri = false;
			return true;
		}
		return false;
	}

	@Override
	public void onAnkanableIndexListsReceived(List<List<Integer>> lists) {
	}

	@Override
	public void onGameOverReceived() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onKakanableIndexRequested(List<Integer> list) {
	}

	@Override
	public void onNakiRequested(boolean ron, List<Integer> minkan, List<List<Integer>> pon, List<List<Integer>> chi) {
		sender.sendReject();
	}

	@Override
	public void onKyusyukyuhaiRequested() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReachableIndexRequested(List<Integer> list) {
		this.reachableIndexList = list;
		System.out.println("reach");
		// button.add(new Button("立直"));
	}

	@Override
	public void onRonReceived(Map<Player, List<Hai>> map) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTsumoAgariReceived() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTsumoAgariRequested() {
		// button.add(new Button("ツモ"));
	}

	@Override
	public void onDiscardRequested(Hai tsumohai) {
		this.tsumohai = tsumohai;
		this.kiri = true;
	}

	@Override
	public void onDiscardReceived(Kaze kaze, Hai hai, boolean tsumokiri) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameResultReceived(Map<Kaze, Integer> scoreMap) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameStartReceived(Map<Kaze, Player> pMap, Kaze kaze, int initialScore) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKyokuResultReceived(KyokuResult result, Map<Kaze, Integer> scoreDiff, List<Hai> uradoraList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNakiNotified(Kaze kaze, Mentsu m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReachNotified(Kaze currentTurn, Hai hai, boolean tsumokiri) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStartKyokuReceived(Kaze bakaze, Kaze kaze, int kyokusu, int honba, int tsumibou, List<Hai> haipai, Hai dora) {
		this.tehai = new ArrayList<Hai>(haipai);
		Collections.sort(this.tehai, Hai.HaiComparator.ASCENDING_ORDER);
	}

	@Override
	public void onTempaiReceived(Map<Kaze, List<Hai>> map) {
		// TODO Auto-generated method stub
	}

}
