package test.client;

import java.util.List;
import java.util.Map;

import system.Kyoku;
import system.Player;
import system.hai.Hai;
import system.hai.HurohaiList;
import system.hai.Kaze;
import system.hai.Mentsu;
import system.result.KyokuResult;
import ai.AI;

public class AIClient implements ClientListener {
	private final AI ai;
	private ClientSender sender;

	public AIClient(ClientSender sender, AI ai) {
		this.sender = sender;
		this.ai = ai;
	}
	
	public void updateKyoku(Kyoku kyoku) {
		ai.update(kyoku);
	}

	@Override
	public void onAnkanableIndexListsReceived(List<List<Integer>> lists) {
		int index = ai.ankan(lists);
		if (index != -1)
			sender.sendAnkanIndexList(lists.get(index));
	}

	@Override
	public void onDiscardReceived(boolean tumoari) {
		int index = ai.discard();
		sender.sendDiscardIndex(index);
	}

	@Override
	public void onDiscardReceived(Player p, Hai hai, boolean tumokiri) {
		// to do nothind
	}

	@Override
	public void onFieldReceived(List<Hai> tehai, Map<Kaze, HurohaiList> nakihai, Map<Kaze, List<Hai>> sutehai, Kaze currentTurn,
			Hai currentSutehai, List<Integer> tehaiSize, int yamaSize, int wanpaiSize, List<Hai> doraList) {
		// to do nothind
	}

	@Override
	public void onGameOverReceived() {
		// to do nothind
	}

	@Override
	public void onGameResultReceived(int[] score) {
		// to do nothind
	}

	@Override
	public void onGameStartReceived(List<Player> playerList, int index, int[] scores) {
		// to do nothind
	}

	@Override
	public void onKakanableIndexListReceived(List<Integer> list) {
		int index = ai.kakan(list);
		if (index != -1)
			sender.sendKakanIndex(index);
	}

	@Override
	public void onNakiRequested(boolean ron, List<Integer> minkan, List<List<Integer>> pon, List<List<Integer>> chi) {
		boolean ronFlag = false;
		boolean minkanFlag = false;
		int ponIndex = -1;
		int chiIndex = -1;
		
		if(ron) {
			ronFlag = ai.isRon();
		}
		if(minkan != null) {
			minkanFlag = ai.minkan();
		}
		if(pon != null) {
			ponIndex = ai.pon(pon);
		}
		if(chi != null) {
			chiIndex = ai.chi(chi);
		}
		

		if (ronFlag) {
			sender.sendRon();
			return;
		}

		if (minkanFlag) {
			sender.sendMinkan();
			return;
		}

		if (ponIndex != -1) {
			sender.sendPonIndexList(pon.get(ponIndex));
			return;
		}

		if (chiIndex != -1) {
			sender.sendChiIndexList(chi.get(chiIndex));
			return;
		}
		sender.sendReject();
	}

	@Override
	public void onKyokuResultReceived(KyokuResult result, int[] newScores, int[] oldScores, List<Integer> soten, List<Hai> uradoraList) {
		// to do nothind
	}

	@Override
	public void onKyusyukyuhaiRequested() {
		boolean flag = ai.isKyusyukyuhai();
		if (flag) {
			sender.sendKyusyukyuhai();
		}
	}

	@Override
	public void onNakiReceived(Player p, Mentsu m) {
		// to do nothind
	}

	@Override
	public void onReachableIndexListReceived(List<Integer> list) {
		int index = ai.reach(list);
		if (index != -1)
			sender.sendReachIndex(index);
	}

	@Override
	public void onReachReceived(Kaze currentTurn, int sutehaiIndex) {
		// to do nothind
	}

	@Override
	public void onRonReceived(Map<Player, List<Hai>> map) {
		// to do nothind
	}

	@Override
	public void onStartKyokuReceived(Kaze bakaze, int kyokusu, int honba, int tsumibou) {
		// to do nothind
	}

	@Override
	public void onTempaiReceived(Map<Player, List<Hai>> map) {
		// to do nothind
	}

	@Override
	public void onTsumoAgariReceived() {
		// to do nothind
	}

	@Override
	public void onTsumoAgariRequested() {
		if (ai.isTumoAgari())
			sender.sendTsumoAgari();
	}

	@Override
	public void onTsumoGiriReceived() {
		// to do nothind
	}

	@Override
	public void onTsumoHaiReceived(Hai hai) {
		// to do nothind
	}

}
