package test.client;

import java.util.List;

import system.Kyoku;
import system.hai.Hai;
import ai.AI;

public class AIClient implements MahjongListener {
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
	public void onKakanableIndexRequested(List<Integer> list) {
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
	public void onKyusyukyuhaiRequested() {
		boolean flag = ai.isKyusyukyuhai();
		if (flag) {
			sender.sendKyusyukyuhai();
		}
	}

	@Override
	public void onReachableIndexRequested(List<Integer> list) {
		int index = ai.reach(list);
		if (index != -1)
			sender.sendReachIndex(index);
	}

	@Override
	public void onTsumoAgariRequested() {
		if (ai.isTumoAgari())
			sender.sendTsumoAgari();
	}

	@Override
	public void onDiscardRequested(Hai tsumohai) {
		int index = ai.discard();
		sender.sendDiscardIndex(index);
	}

}
