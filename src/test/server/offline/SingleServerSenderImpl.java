package test.server.offline;

import java.util.List;
import java.util.Map;

import system.Player;
import system.hai.Hai;
import system.hai.HurohaiList;
import system.hai.Kaze;
import system.hai.Mentsu;
import system.result.KyokuResult;
import test.server.SingleServerSender;

/**
 * オフラインにおけるサーバーセンダーの実装.
 */
public class SingleServerSenderImpl implements SingleServerSender{
	
	@Override
	public void sendGameStart(List<Player> playerList, int index, int[] scores) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyStartKyoku(Kaze bakaze, int kyokusu, int honba,
			int tsumibou) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestKyusyukyuhai() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendTsumoHai(Hai hai) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendTsumoGiri() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendDiscard(boolean tumoari) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendChiableIndexLists(List<List<Integer>> lists) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendPonableIndexLists(List<List<Integer>> lists) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendAnkanableIndexLists(List<List<Integer>> lists) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendMinkanableIndexList(List<Integer> list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendKakanableIndexList(List<Integer> list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendReachableIndexList(List<Integer> list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestRon() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestTsumoAgari() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyDiscard(Player p, Hai hai, boolean tumokiri) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyNaki(Player p, Mentsu m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyRon(Map<Player, List<Hai>> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendField(List<Hai> tehai, Map<Kaze, HurohaiList> nakihai,
			Map<Kaze, List<Hai>> sutehai, Kaze currentTurn, Hai currentSutehai,
			List<Integer> tehaiSize, int yamaSize, int wanpaiSize,
			List<Hai> doraList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyReach(Kaze currentTurn, int sutehaiIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyKyokuResult(KyokuResult kr, int[] newScore,
			int[] oldScore, List<Integer> soten, List<Hai> uradoraList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyTempai(Map<Player, List<Hai>> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyGameResult(int[] Score) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendGameOver() {
		// TODO Auto-generated method stub
		
	}

}
