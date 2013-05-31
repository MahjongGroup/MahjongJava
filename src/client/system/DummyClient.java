package client.system;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import client.datapack.PackName;

import system.Player;
import system.hai.Hai;
import system.hai.HurohaiList;
import system.hai.Kaze;
import system.hai.Mentsu;
import system.result.KyokuResult;
import test.system.test.Order;
import test.system.test.OrderCommunicator;
import test.system.test.OrderPacket;

public class DummyClient extends ClientOperator implements Client {
	private BackgroundSystemOfClient background;
	private ClientCommunicator communicator;

	public void setBackground(BackgroundSystemOfClient background) {
		this.background = background;
	}

	
	public void requestGame(int id) {
		sendPacket(new OrderPacket(Order.ON_GAME_REQUESTED, 1,
				Integer.valueOf(id)));
	}

	
	public void onGameStartReceived(List<Player> playerList, int index,
			int[] scores) {
		
/*
 * 		players[i] = new ClientPlayer(playerList.get((index + i) % 4));
 * 		players[i].score = score[(index + i) % 4];
 * 		players[i].kaze = Kaze.valueOf((index + i) % 4);
 */
		
		
		
		
		
		background.setMode(PackName.Game);
		Map<Kaze, Player> playerMap = new HashMap<Kaze, Player>(4);
		for (int i = 0; i < playerList.size(); i++) {
			playerMap.put(Kaze.valueOf(i), playerList.get(i));
		}
		// TODO to be changed
//		index = 1;
		background.setPlayerNumber(index);
		background.setPlayerToTheChair(playerList, index);
		background.setSekiMap(new HashMap<Player, Integer>(4));
		for (int i = 0; i < 4; i++) {
//			background.getSekiMap().put(background.getPlayers()[i],
//					(4 - index + i) % 4);
			background.setScore((index + i) % 4, scores[i]);
		}
		// tmpbackground.setIndex(index);

	}

	
	public void onStartKyokuReceived(Kaze bakaze, int kyokusu, int honba,
			int tsumibou) {
		background.setMode(PackName.Game);
		background.setHonba(honba);
		background.setTsumiBou(tsumibou);
		background.setNumber(background.getPlayerNumber());
		background.setKyokusu(kyokusu);
		background.setBakaze(bakaze);
	}

	
	public void onKyusyukyuhaiRequested() {
		background.addButtonList(StateCode.KYUSYUKYUHAI);
		background.addStateCode(StateCode.SELECT_BUTTON);
	}

	
	public void sendKyusyukyuhai(boolean flag) {
		sendPacket(new OrderPacket(Order.ON_KYUSYUKYUHAI_RECEIVED, 1,
				Boolean.valueOf(flag)));
	}

	
	public void onTsumoHaiReceived(Hai hai) {
		background.setTsumoHai(hai);
		background.addStateCode(StateCode.DISCARD_SELECT);
	}

	
	public void onDiscardReceived(boolean tumoari) {
		background.addStateCode(StateCode.DISCARD_SELECT);
	}

	
	public void sendDiscardIndex(int index) {
		sendPacket(new OrderPacket(Order.ON_DISCARD_INDEX_RECEIVED, 1,
				Integer.valueOf(index)));
		background.setTsumoHai(null);
		background.refreshStateCodes();
	}

	
	public void onChiableIndexListsReceived(List<List<Integer>> lists) {
		background.setTsumoHai(null);
		background.addButtonList(StateCode.SELECT_CHI);
		background.addStateCode(StateCode.SELECT_BUTTON);
		background.getAbleIndexList().put(StateCode.SELECT_CHI_HAI, lists);
	}

	
	public void sendChiIndexList(List<Integer> list) {
		List<Integer> dummy;
		if (list == null)
			dummy = null;
		else
			dummy = new ArrayList<Integer>(list);
		sendPacket(new OrderPacket(Order.ON_CHI_INDEX_LIST_RECEIVED, 1, dummy));
		background.refreshStateCodes();
	}

	
	public void onPonableIndexListsReceived(List<List<Integer>> lists) {
		background.setTsumoHai(null);
		background.addButtonList(StateCode.SELECT_PON);
		background.addStateCode(StateCode.SELECT_BUTTON);
		background.getAbleIndexList().put(StateCode.SELECT_PON_HAI, lists);
	}

	
	public void sendPonIndexList(List<Integer> list) {
		List<Integer> dummy;
		if (list == null)
			dummy = null;
		else
			dummy = new ArrayList<Integer>(list);
		sendPacket(new OrderPacket(Order.ON_PON_INDEX_LIST_RECEIVED, 1, dummy));
		background.refreshStateCodes();
	}

	
	public void onAnkanableIndexListsReceived(List<List<Integer>> lists) {
		background.addStateCode(StateCode.SELECT_BUTTON);
		background.addButtonList(StateCode.SELECT_ANKAN);
		background.getAbleIndexList().put(StateCode.SELECT_ANKAN_HAI, lists);
	}

	
	public void sendAnkanIndexList(List<Integer> list) {
		List<Integer> dummy;
		if (list == null)
			dummy = null;
		else
			dummy = new ArrayList<Integer>(list);
		sendPacket(new OrderPacket(Order.ON_ANKAN_INDEX_LIST_RECEIVED, 1, dummy));
		background.refreshStateCodes();
	}

	
	public void onMinkanableIndexListReceived(List<Integer> hais) {
		background.setTsumoHai(null);
		List<List<Integer>> tmpList = new ArrayList<List<Integer>>();
		tmpList.add(hais);
		background.addButtonList(StateCode.SELECT_MINKAN);
		background.addStateCode(StateCode.SELECT_BUTTON);
		background.getAbleIndexList().put(StateCode.SELECT_MINKAN, tmpList);
	}

	
	public void sendMinkan(boolean result) {
		sendPacket(new OrderPacket(Order.ON_MINKANABLE_INDEX_LISTS_RECEIVED, 1,
				Boolean.valueOf(result)));
		background.refreshStateCodes();
	}

	
	public void onKakanableIndexListReceived(List<Integer> list) {
		List<List<Integer>> tmpList = new ArrayList<List<Integer>>();
		tmpList.add(list);
		background.addButtonList(StateCode.SELECT_KAKAN);
		background.addStateCode(StateCode.SELECT_BUTTON);
		background.getAbleIndexList().put(StateCode.SELECT_KAKAN_HAI, tmpList);
	}

	
	public void sendKakanIndex(int index) {
		sendPacket(new OrderPacket(Order.ON_KAKANABLE_INDEX_RECEIVED, 1,
				Integer.valueOf(index)));
		background.refreshStateCodes();
	}

	
	public void onReachableIndexListReceived(List<Integer> list) {
		List<List<Integer>> tmpList = new ArrayList<List<Integer>>();
		for (Integer i : list) {
			List<Integer> tmp = new ArrayList<Integer>();
			tmp.add(i);
			tmpList.add(tmp);
		}
		background.addStateCode(StateCode.SELECT_BUTTON);
		background.addButtonList(StateCode.SELECT_REACH);
		background.getAbleIndexList().put(StateCode.SELECT_REACH_HAI, tmpList);
	}

	
	public void sendReachIndex(int index) {
		sendPacket(new OrderPacket(Order.ON_REACH_INDEX_RECEIVED, 1,
				Integer.valueOf(index)));
		background.refreshStateCodes();
		if (index != -1)
			background.setTsumoHai(null);
	}

	
	public void onRonRequested() {
		background.setTsumoHai(null);
		background.addStateCode(StateCode.SELECT_BUTTON);
		background.addButtonList(StateCode.SELECT_RON);
	}

	
	public void sendRon(boolean result) {
		sendPacket(new OrderPacket(Order.ON_RON_RECEIVED, 1,
				Boolean.valueOf(result)));
		background.refreshStateCodes();
	}

	
	public void onTsumoAgariRequested() {
		background.addStateCode(StateCode.SELECT_BUTTON);
		background.addButtonList(StateCode.SELECT_TSUMO);
	}

	
	public void sendTsumoAgari() {
		sendPacket(new OrderPacket(Order.ON_TSUMO_ARGARI_RECEIVED, 0, null));
		background.refreshStateCodes();
	}

	
	public void onDiscardReceived(Player p, Hai hai, boolean tumokiri) {
		background.setTsumoHai(null);
	}

	
	public void onNakiReceived(Player p, Mentsu m) {
		int index;
		for (index = 0; index < background.getPlayers().length; index++) {
			if (background.getPlayers()[index] == p)
				break;
		}
		background.refreshStateCodes();
		background.refreshButtonList();
	}

	
	public void onRonReceived(Map<Player, List<Hai>> map) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		background.setMode(PackName.Result);
	}

	
	public void onTsumoAgariReceived() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		background.setMode(PackName.Result);
	}

	
	public void onFieldReceived(List<Hai> tehai,
			Map<Kaze, HurohaiList> nakihai, Map<Kaze, List<Hai>> sutehai,
			Kaze currentTurn, Hai currentSutehai, List<Integer> tehaiSize,
			int yamaSize, int wanpaiSize, List<Hai> doraList) {
		background.setTehai(tehai);
		background.setCurrentTurn(background.getKaze().get(currentTurn));
		for (Kaze k : Kaze.values()) {
			int i = background.getKaze().get(k);
			synchronized (background.getSutehaiMap()) {
				background.getSutehaiMap().put(i, sutehai.get(k));
			}
			if (k == currentTurn)
				background.getSutehaiMap().get(i).add(currentSutehai);
			background.getHurohaiMap().put(i, nakihai.get(k));
		}
		Map<Integer, Integer> tehaiSizeMap = background.getTehaiSizeMap();
		tehaiSizeMap.put(background.getKaze().get(Kaze.TON), tehaiSize.get(0));
		tehaiSizeMap.put(background.getKaze().get(Kaze.NAN), tehaiSize.get(1));
		tehaiSizeMap.put(background.getKaze().get(Kaze.SYA), tehaiSize.get(2));
		tehaiSizeMap.put(background.getKaze().get(Kaze.PE), tehaiSize.get(3));

		background.setYamaSize(yamaSize);
		background.setWanpaiSize(wanpaiSize);
		background.setDoraList(doraList);
	}

	
	public void onTsumoGiriReceived() {
		// TODO ツモぎりアクション
		background.setTsumoHai(null);
		background.refreshStateCodes();
	}

	
	public void onGameResultReceived(int[] score) {
		// TODO to be changed

	}

	
	public void onReachReceived(Kaze currentTurn, int sutehaiIndex) {
		int currentIndex = background.getKaze().get(currentTurn);
		background.getReachPosMap().put(currentIndex, sutehaiIndex + 1);
	}

	
	public void onKyokuResultReceived(KyokuResult result, int[] newScores,
			int[] oldScores, List<Integer> soten, List<Hai> uradoraList) {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
		background.setMode(PackName.Result);
		background.setResult(result, newScores, oldScores, soten, uradoraList);
	}

	
	public void requestNextKyoku() {
		sendPacket(new OrderPacket(Order.ON_NEXT_KYOKU_REQUESTED, 0, null));
	}

	
	public void onGameOverReceived() {
		// TODO to be changed
	}

	private class ClientCommunicator extends OrderCommunicator {

		
		public void dispatch(Order order) {
			List<OrderPacket> packets = getPackets(order);
			System.out.println(order);
			switch (order) {
			case ON_ANKAN_INDEX_LIST_RECEIVED:
				break;
			case ON_ANKANABLE_INDEX_LISTS_RECEIVED:
				onAnkanableIndexListsReceived((List<List<Integer>>) packets
						.get(0).getArg());
				break;
			case ON_CHI_INDEX_LIST_RECEIVED:
				break;
			case ON_CHIABLE_INDEX_LISTS_RECEIVED:
				onChiableIndexListsReceived((List<List<Integer>>) packets
						.get(0).getArg());
				break;
			case ON_DISCARD_INDEX_RECEIVED:
				break;
			case ON_DISCARD_RECEIVED_0:
				onDiscardReceived((Boolean) packets.get(0).getArg());
				break;
			case ON_DISCARD_RECEIVED_1:
				onDiscardReceived((Player) packets.get(0).getArg(),
						(Hai) packets.get(1).getArg(), (Boolean) packets.get(2)
								.getArg());
				break;
			case ON_FIELD_RECEIVED:
				onFieldReceived((List<Hai>) packets.get(0).getArg(),
						(Map<Kaze, HurohaiList>) packets.get(1).getArg(),
						(Map<Kaze, List<Hai>>) packets.get(2).getArg(),
						(Kaze) packets.get(3).getArg(), (Hai) packets.get(4)
								.getArg(), (List<Integer>) packets.get(5)
								.getArg(), (Integer) packets.get(6).getArg(),
						(Integer) packets.get(7).getArg(), (List<Hai>) packets
								.get(8).getArg());
				break;
			case ON_GAME_OVER_RECEIVED:
				onGameOverReceived();
				break;
			case ON_GAME_REQUESTED:
				break;
			case ON_GAME_RESULT_RECEIVED:
				onGameResultReceived((int[]) packets.get(0).getArg());
				break;
			case ON_GAME_START_RECEIVED:
				onGameStartReceived((List<Player>) packets.get(0).getArg(),
						(Integer) packets.get(1).getArg(),
						(int[]) packets.get(2).getArg());
				break;
			case ON_KAKANABLE_INDEX_LISTS_RECEIVED:
				onKakanableIndexListReceived((List<Integer>) packets.get(0)
						.getArg());
				break;
			case ON_KAKANABLE_INDEX_RECEIVED:
				break;
			case ON_KYOKU_RESULT_RECEIVED:
				onKyokuResultReceived((KyokuResult) packets.get(0).getArg(),
						(int[]) packets.get(1).getArg(), (int[]) packets.get(2)
								.getArg(), (List<Integer>) packets.get(3)
								.getArg(), (List<Hai>) packets.get(4).getArg());
				break;
			case ON_KYUSYUKYUHAI_RECEIVED:
				break;
			case ON_KYUSYUKYUHAI_REQUESTED:
				onKyusyukyuhaiRequested();
				break;
			case ON_MINKANABLE_INDEX_LISTS_RECEIVED:
				onMinkanableIndexListReceived((List<Integer>) packets.get(0)
						.getArg());
				break;
			case ON_MINKANABLE_INDEX_RECEIVED:
				break;
			case ON_NAKI_RECEIVED:
				onNakiNotified((Player) packets.get(0).getArg(),
						(Mentsu) packets.get(1).getArg());
				break;
			case ON_NEXT_KYOKU_REQUESTED:
				break;
			case ON_PON_INDEX_LIST_RECEIVED:
				break;
			case ON_PONABLE_INDEX_LISTS_RECEIVED:
				onPonableIndexListsReceived((List<List<Integer>>) packets
						.get(0).getArg());
				break;
			case ON_REACH_INDEX_RECEIVED:
				break;
			case ON_REACH_RECEIVED:
				onReachReceived((Kaze) packets.get(0).getArg(),
						(Integer) packets.get(1).getArg());
				break;
			case ON_REACHABLE_INDEX_LIST_RECEIVED:
				onReachableIndexListReceived((List<Integer>) packets.get(0)
						.getArg());
				break;
			case ON_RON_RECEIVED:
				onRonReceived((Map<Player, List<Hai>>) packets.get(0).getArg());
				break;
			case ON_RON_REQUESTED:
				onRonRequested();
				break;
			case ON_START_KYOKU_RECEIVED:
				onStartKyokuReceived((Kaze) packets.get(0).getArg(),
						(Integer) packets.get(1).getArg(), (Integer) packets
								.get(2).getArg(), (Integer) packets.get(3)
								.getArg());
				break;
			case ON_TSUMO_AGARI_REQUESTED:
				onTsumoAgariRequested();
				break;
			case ON_TSUMO_ARGARI_RECEIVED:
				onTsumoAgariReceived();
				break;
			case ON_TSUMO_HAI_RECEIVED:
				onTsumoHaiReceived((Hai) packets.get(0).getArg());
				break;
			case ON_TSUMOGIRI_RECEIVED:
				onTsumoAgariReceived();
				break;
			default:
				break;
			}
		}
	}

	private void sendPacket(OrderPacket packet) {
		communicator.sendPacket(packet);
	}

	public void connectServer() {
		try {
			communicator = new ClientCommunicator();
			Socket s = new Socket("localhost", 5555);
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			oos.flush();
			communicator.launch(ois, oos);
			sendPacket(new OrderPacket(Order.ON_GAME_REQUESTED, 1, 200));
		} catch (IOException e) {
		}
	}


	@Override
	public void onTempaiReceived(Map<Player, List<Hai>> map) {
		background.onTempaiReceived(map);		
	}

}
