package test.server.offline;

import java.util.List;
import java.util.Map;

import system.Player;
import system.hai.Hai;
import system.hai.HurohaiList;
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
	public void sendGameStart(List<Player> playerList, int index, int[] scores) {
		listener.onGameStartReceived(playerList, index, scores);
	}

	@Override
	public void notifyStartKyoku(Kaze bakaze, int kyokusu, int honba,
			int tsumibou) {
		listener.onStartKyokuReceived(bakaze, kyokusu, honba, tsumibou);
	}

	@Override
	public void requestKyusyukyuhai() {
		listener.onKyusyukyuhaiRequested();
	}

	@Override
	public void sendTsumoHai(Hai hai) {
		listener.onTsumoHaiReceived(hai);
	}

	@Override
	public void sendTsumoGiri() {
		listener.onTsumoGiriReceived();
	}

	@Override
	public void sendDiscard(boolean tumoari) {
		listener.onDiscardReceived(tumoari);
	}

	@Override
	public void sendAnkanableIndexLists(List<List<Integer>> lists) {
		listener.onAnkanableIndexListsReceived(lists);		
	}
	
	@Override
	public void sendKakanableIndexList(List<Integer> list) {
		listener.onKakanableIndexListReceived(list);
	}

	@Override
	public void sendReachableIndexList(List<Integer> list) {
		listener.onReachableIndexListReceived(list);
	}

	@Override
	public void requestTsumoAgari() {
		listener.onTsumoAgariRequested();
	}

	@Override
	public void notifyDiscard(Player p, Hai hai, boolean tumokiri) {
		listener.onDiscardReceived(p, hai, tumokiri);
	}

	@Override
	public void notifyNaki(Player p, Mentsu m) {
		listener.onNakiReceived(p, m);
	}

	@Override
	public void notifyRon(Map<Player, List<Hai>> map) {
		listener.onRonReceived(map);
	}

	@Override
	public void sendField(List<Hai> tehai, Map<Kaze, HurohaiList> nakihai,
			Map<Kaze, List<Hai>> sutehai, Kaze currentTurn, Hai currentSutehai,
			List<Integer> tehaiSize, int yamaSize, int wanpaiSize,
			List<Hai> doraList) {
		listener.onFieldReceived(tehai, nakihai, sutehai, currentTurn, currentSutehai, tehaiSize, yamaSize, wanpaiSize, doraList);		
	}

	@Override
	public void notifyReach(Kaze currentTurn, int sutehaiIndex) {
		listener.onReachReceived(currentTurn, sutehaiIndex);
	}

	@Override
	public void notifyKyokuResult(KyokuResult kr, int[] newScore,
			int[] oldScore, List<Integer> soten, List<Hai> uradoraList) {
		listener.onKyokuResultReceived(kr, newScore, oldScore, soten, uradoraList);		
	}

	@Override
	public void notifyTempai(Map<Player, List<Hai>> map) {
		listener.onTempaiReceived(map);		
	}

	@Override
	public void notifyGameResult(int[] score) {
		listener.onGameResultReceived(score);		
	}

	@Override
	public void sendGameOver() {
		listener.onGameOverReceived();		
	}

	@Override
	public void requestNaki(boolean ron, List<Integer> minkan, List<List<Integer>> pon, List<List<Integer>> chi) {
		listener.onNakiRequested(ron, minkan, pon, chi);
	}

}
