package test.client;

import java.util.List;
import java.util.Map;

import system.Player;
import system.hai.Hai;
import system.hai.Kaze;
import system.hai.Mentsu;
import system.result.KyokuResult;

public class MahjongClient implements ClientListener{
	private final MahjongListener listener;
	
	public MahjongClient(MahjongListener l) {
		this.listener = l;
	}
	
	@Override
	public void onAnkanableIndexListsReceived(List<List<Integer>> lists) {
		listener.onAnkanableIndexListsReceived(lists);
	}

	@Override
	public void onDiscardRequested(Hai tsumohai) {
		listener.onDiscardRequested(tsumohai);
	}

	@Override
	public void onKakanableIndexRequested(List<Integer> list) {
		listener.onKakanableIndexRequested(list);
	}

	@Override
	public void onKyusyukyuhaiRequested() {
		listener.onKyusyukyuhaiRequested();
	}

	@Override
	public void onNakiRequested(boolean ron, List<Integer> minkan, List<List<Integer>> pon, List<List<Integer>> chi) {
		listener.onNakiRequested(ron, minkan, pon, chi);
	}

	@Override
	public void onReachableIndexRequested(List<Integer> list) {
		listener.onReachableIndexRequested(list);
	}

	@Override
	public void onTsumoAgariRequested() {
		listener.onTsumoAgariRequested();
	}

	@Override
	public void onDiscardReceived(Kaze kaze, Hai hai, boolean tsumokiri) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameOverReceived() {
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
	public void onRonReceived(Map<Player, List<Hai>> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStartKyokuReceived(Kaze bakaze, Kaze kaze, int kyokusu, int honba, int tsumibou, List<Hai> haipai, Hai dora) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTempaiReceived(Map<Kaze, List<Hai>> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTsumoAgariReceived() {
		// TODO Auto-generated method stub
		
	}

}
