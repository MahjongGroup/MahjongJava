package test.server.offline;

import java.util.List;
import java.util.Map;

import system.Player;
import system.hai.Hai;
import system.hai.Kaze;
import system.hai.Mentsu;
import system.result.KyokuResult;
import test.client.ClientListener;
import test.server.SingleServerSender;

/**
 * オフラインにおけるサーバーセンダーの実装.
 */
public class SingleServerSenderImpl implements SingleServerSender{
	private ClientListener listener;
	
	public SingleServerSenderImpl() {
		
	}
	
	public void setListener(ClientListener l) {
		this.listener = l;
	}
	
	@Override
	public void requestKyusyukyuhai() {
		listener.onKyusyukyuhaiRequested();
	}

	@Override
	public void sendAnkanableIndexLists(List<List<Integer>> lists) {
		listener.onAnkanableIndexListsReceived(lists);		
	}
	
	@Override
	public void requestKakanableIndex(List<Integer> list) {
		listener.onKakanableIndexRequested(list);
	}

	@Override
	public void requestReachableIndex(List<Integer> list) {
		listener.onReachableIndexRequested(list);
	}

	@Override
	public void requestTsumoAgari() {
		listener.onTsumoAgariRequested();
	}

	@Override
	public void notifyRon(Map<Player, List<Hai>> map) {
		listener.onRonReceived(map);
	}
	
	@Override
	public void sendGameOver() {
		listener.onGameOverReceived();		
	}

	@Override
	public void requestNaki(boolean ron, List<Integer> minkan, List<List<Integer>> pon, List<List<Integer>> chi) {
		listener.onNakiRequested(ron, minkan, pon, chi);
	}
	
	@Override
	public void sendGameStart(Map<Kaze, Player> pMap, Kaze kaze, int initialScore) {
		listener.onGameStartReceived(pMap, kaze, initialScore);
	}

	@Override
	public void notifyStartKyoku(Kaze bakaze, Kaze kaze, int kyokusu, int honba, int tsumibou, List<Hai> haipai, Hai dora) {
		listener.onStartKyokuReceived(bakaze, kaze,  kyokusu, honba, tsumibou, haipai, dora);
	}

	@Override
	public void requestDiscardIndex(Hai tsumohai) {
		listener.onDiscardRequested(tsumohai);
	}

	@Override
	public void notifyDiscard(Kaze kaze, Hai hai, boolean tsumokiri) {
		listener.onDiscardReceived(kaze, hai, tsumokiri);
	}

	@Override
	public void notifyNaki(Kaze kaze, Mentsu m) {
		listener.onNakiNotified(kaze, m);
	}

	@Override
	public void notifyReach(Kaze currentTurn, Hai hai, boolean tsumokiri) {
		listener.onReachNotified(currentTurn, hai, tsumokiri);
	}

	@Override
	public void notifyKyokuResult(KyokuResult kr, Map<Kaze, Integer> scoreDiff, List<Hai> uradoraList) {
		listener.onKyokuResultReceived(kr, scoreDiff, uradoraList);
	}

	@Override
	public void notifyTempai(Map<Kaze, List<Hai>> map) {
		listener.onTempaiReceived(map);
	}

	@Override
	public void notifyGameResult(Map<Kaze, Integer> scoreMap) {
		listener.onGameResultReceived(scoreMap);
	}

}
