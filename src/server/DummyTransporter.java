package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import system.Player;
import system.hai.Hai;
import system.hai.HurohaiList;
import system.hai.Kaze;
import system.hai.Mentsu;
import system.result.KyokuResult;
import test.system.test.Order;
import test.system.test.OrderCommunicator;
import test.system.test.OrderPacket;
import test.system.test.SerializableHurohaiList;
import test.system.test.SerializableMentsu;
import test.system.test.SerializablePlayer;

public class DummyTransporter extends Transporter implements Server {
	private boolean grandFlag = false;
	private boolean kyusyukyuhaiReceivedFlag = false;
	private boolean kyusyukyuhaiReceivedResult = false;
	private boolean discardedReceivedFlag = false;
	private int discardedIndexResult = -1;
	private boolean tumoari = true;
	private boolean chiReceivedFlag = false;
	private List<Integer> chiIndexList = null;
	private boolean ponReceivedFlag = false;
	private List<Integer> ponIndexList = null;
	private boolean ankanReceivedFlag = false;
	private List<Integer> ankanIndexList = null;
	private boolean minkanReceivedFlag = false;
	private boolean isMinkanResult = false;
	private boolean kakanReceivedFlag = false;
	private int kakanIndex = -1;
	private boolean reachReceivedFlag = false;
	private int reachHaiIndex = -1;
	private boolean ronReceivedFlag = false;
	private boolean ronReceivedResult = false;
	private boolean tsumoagariReceivedResult = false;
	private boolean isWait = true;
	private boolean endResultPage = false;
	private ServerCommunicator communicator;

	
	
	@Override
	public void onGameRequested(int id) {
		isWait = false;
	}

	public void init() {
		grandFlag = false;
		kyusyukyuhaiReceivedFlag = false;
		kyusyukyuhaiReceivedResult = false;
		discardedReceivedFlag = false;
		discardedIndexResult = -1;
		tumoari = true;
		chiReceivedFlag = false;
		chiIndexList = null;
		ponReceivedFlag = false;
		ponIndexList = null;
		ankanReceivedFlag = false;
		ankanIndexList = null;
		minkanReceivedFlag = false;
		isMinkanResult = false;
		kakanReceivedFlag = false;
		kakanIndex = -1;
		reachReceivedFlag = false;
		reachHaiIndex = -1;
		ronReceivedFlag = false;
		ronReceivedResult = false;
		tsumoagariReceivedResult = false;
		endResultPage = false;
	}

	public boolean getGrandFlag() {
		return grandFlag;
	}

	public boolean isKyusyukyuhaiReceived() {
		return kyusyukyuhaiReceivedFlag;
	}

	public boolean getKyusyukyuhaiResult() {
		return kyusyukyuhaiReceivedResult;
	}

	public boolean isDiscardedReceived() {
		return discardedReceivedFlag;
	}

	public int getDiscardedIndex() {
		return discardedIndexResult;
	}

	public boolean isChiReceived() {
		return chiReceivedFlag;
	}

	public List<Integer> getChiIndexList() {
		return chiIndexList;
	}

	public boolean isPonReceived() {
		return ponReceivedFlag;
	}

	public List<Integer> getPonIndexList() {
		return ponIndexList;
	}

	public boolean isAnkanReceived() {
		return ankanReceivedFlag;
	}

	public List<Integer> getAnkanIndexList() {
		return ankanIndexList;
	}

	public boolean isMinkanReceived() {
		return minkanReceivedFlag;
	}

	public boolean isMinkanDo() {
		return isMinkanResult;
	}

	public boolean isKakanReceived() {
		return kakanReceivedFlag;
	}

	public int getKakanindex() {
		return kakanIndex;
	}

	public boolean isReachReceived() {
		return reachReceivedFlag;
	}

	public int getReachHaiIndex() {
		return reachHaiIndex;
	}

	public boolean isRonReceived() {
		return ronReceivedFlag;
	}

	public boolean isRonDo() {
		return ronReceivedResult;
	}

	public boolean isTsumoagariDo() {
		return tsumoagariReceivedResult;
	}

	public boolean isThereTsumohai() {
		return tumoari;
	}

	public boolean isEndResultPage() {
		return endResultPage;
	}

	@Override
	public void sendGameStart(List<Player> playerList, int index, int[] scores) {
		List<SerializablePlayer> dummy = new ArrayList<SerializablePlayer>();
		for(int i = 0;i < playerList.size();i++){
			dummy.add(new SerializablePlayer(playerList.get(i)));
		}
		sendPacket(new OrderPacket(Order.ON_GAME_START_RECEIVED, 1, dummy));
		sendPacket(new OrderPacket(Order.ON_GAME_START_RECEIVED, 2,
				Integer.valueOf(index)));
		sendPacket(new OrderPacket(Order.ON_GAME_START_RECEIVED, 3, scores));
	}

	@Override
	public void notifyStartKyoku(Kaze bakaze, int kyokusu, int honba,
			int tsumibou) {
		sendPacket(new OrderPacket(Order.ON_START_KYOKU_RECEIVED, 1, bakaze));
		sendPacket(new OrderPacket(Order.ON_START_KYOKU_RECEIVED, 2,
				Integer.valueOf(kyokusu)));
		sendPacket(new OrderPacket(Order.ON_START_KYOKU_RECEIVED, 3,
				Integer.valueOf(honba)));
		sendPacket(new OrderPacket(Order.ON_START_KYOKU_RECEIVED, 4,
				Integer.valueOf(tsumibou)));
	}

	@Override
	public void requestKyusyukyuhai() {
		sendPacket(new OrderPacket(Order.ON_KYUSYUKYUHAI_REQUESTED, 0, null));
	}

	public boolean isWait() {
		return isWait;
	}

	public void setWait(boolean flag) {
		isWait = flag;
	}

	@Override
	public void onKyusyukyuhaiReceived(boolean answer) {
		kyusyukyuhaiReceivedFlag = true;
		grandFlag = true;
		kyusyukyuhaiReceivedResult = answer;
	}

	@Override
	public void sendTsumoHai(Hai hai) {
		sendPacket(new OrderPacket(Order.ON_TSUMO_HAI_RECEIVED, 1, hai));
	}

	@Override
	public void sendTsumoGiri() {
		sendPacket(new OrderPacket(Order.ON_TSUMOGIRI_RECEIVED, 0, null));
	}

	@Override
	public void sendDiscard(boolean tumoari) {
		sendPacket(new OrderPacket(Order.ON_DISCARD_RECEIVED_0, 1, tumoari));
	}

	@Override
	public void onDiscardIndexReceived(int index) {
		grandFlag = true;
		discardedReceivedFlag = true;
		discardedIndexResult = index;
	}

	@Override
	public void sendChiableIndexLists(List<List<Integer>> lists) {
		sendPacket(new OrderPacket(Order.ON_CHIABLE_INDEX_LISTS_RECEIVED, 1,
				lists));
	}

	@Override
	public void onChiIndexListReceived(List<Integer> list) {
		chiIndexList = list;
		chiReceivedFlag = true;
		tumoari = false;
	}

	@Override
	public void sendPonableIndexLists(List<List<Integer>> lists) {
		sendPacket(new OrderPacket(Order.ON_PONABLE_INDEX_LISTS_RECEIVED, 1,
				lists));
	}

	@Override
	public void onPonIndexListReceived(List<Integer> list) {
		ponIndexList = list;
		ponReceivedFlag = true;
		tumoari = false;
	}

	@Override
	public void sendAnkanableIndexLists(List<List<Integer>> lists) {
		sendPacket(new OrderPacket(Order.ON_ANKANABLE_INDEX_LISTS_RECEIVED, 1,
				lists));
	}

	@Override
	public void onAnkanIndexListReceived(List<Integer> list) {
		grandFlag = true;
		ankanReceivedFlag = true;
		ankanIndexList = list;
	}

	@Override
	public void sendMinkanableIndexList(List<Integer> list) {
		sendPacket(new OrderPacket(Order.ON_MINKANABLE_INDEX_LISTS_RECEIVED, 1,
				list));
	}

	@Override
	public void onMinkanableIndexReceived(boolean answer) {
		minkanReceivedFlag = true;
		isMinkanResult = answer;
	}

	@Override
	public void sendKakanableIndexList(List<Integer> list) {
		sendPacket(new OrderPacket(Order.ON_KAKANABLE_INDEX_LISTS_RECEIVED, 1,
				list));
	}

	@Override
	public void onKakanableIndexReceived(int index) {
		grandFlag = true;
		kakanReceivedFlag = true;
		kakanIndex = index;
	}

	@Override
	public void sendReachableIndexList(List<Integer> list) {
		sendPacket(new OrderPacket(Order.ON_REACHABLE_INDEX_LIST_RECEIVED, 1,
				list));
	}

	@Override
	public void onReachIndexReceived(int index) {
		grandFlag = true;
		reachReceivedFlag = true;
		reachHaiIndex = index;
	}

	@Override
	public void requestRon() {
		sendPacket(new OrderPacket(Order.ON_RON_REQUESTED, 0, null));
	}

	@Override
	public void onRonReceived(boolean result) {
		ronReceivedFlag = true;
		ronReceivedResult = result;
	}

	@Override
	public void requestTsumoAgari() {
		sendPacket(new OrderPacket(Order.ON_TSUMO_AGARI_REQUESTED, 0, null));
	}

	@Override
	public void onTsumoAgariReceived() {
		tsumoagariReceivedResult = true;
		grandFlag = true;
	}

	@Override
	public void notifyDiscard(Player p, Hai hai, boolean tumokiri) {
		sendPacket(new OrderPacket(Order.ON_DISCARD_RECEIVED_1, 1, p));
		sendPacket(new OrderPacket(Order.ON_DISCARD_RECEIVED_1, 2, hai));
		sendPacket(new OrderPacket(Order.ON_DISCARD_RECEIVED_1, 3,
				Boolean.valueOf(tumokiri)));
	}

	@Override
	public void notifyNaki(Player p, Mentsu m) {
		sendPacket(new OrderPacket(Order.ON_DISCARD_RECEIVED_1, 1, p));
		sendPacket(new OrderPacket(Order.ON_DISCARD_RECEIVED_1, 2, new SerializableMentsu(m)));
	}

	@Override
	public void notifyRon(Map<Player, List<Hai>> map) {
		sendPacket(new OrderPacket(Order.ON_RON_RECEIVED, 1, map));
	}

	@Override
	public void sendField(List<Hai> tehai, Map<Kaze, HurohaiList> nakihai,
			Map<Kaze, List<Hai>> sutehai, Kaze currentTurn, Hai currentSutehai,
			List<Integer> tehaiSize, int yamaSize, int wanpaiSize,
			List<Hai> doraList) {
		Map<Kaze,SerializableHurohaiList> tmpNakihai = new HashMap<Kaze, SerializableHurohaiList>();
		for(Kaze k:nakihai.keySet()){
			tmpNakihai.put(k, new SerializableHurohaiList(nakihai.get(k)));
		}
		sendPacket(new OrderPacket(Order.ON_FIELD_RECEIVED, 1, tehai));
		sendPacket(new OrderPacket(Order.ON_FIELD_RECEIVED, 2, tmpNakihai));
		sendPacket(new OrderPacket(Order.ON_FIELD_RECEIVED, 3, sutehai));
		sendPacket(new OrderPacket(Order.ON_FIELD_RECEIVED, 4, currentTurn));
		sendPacket(new OrderPacket(Order.ON_FIELD_RECEIVED, 5, currentSutehai));
		sendPacket(new OrderPacket(Order.ON_FIELD_RECEIVED, 6, tehaiSize));
		sendPacket(new OrderPacket(Order.ON_FIELD_RECEIVED, 7, yamaSize));
		sendPacket(new OrderPacket(Order.ON_FIELD_RECEIVED, 8, wanpaiSize));
		sendPacket(new OrderPacket(Order.ON_FIELD_RECEIVED, 9, doraList));
	}

	@Override
	public void notifyReach(Kaze currentTurn, int sutehaiIndex) {
		sendPacket(new OrderPacket(Order.ON_REACH_RECEIVED, 1, currentTurn));
		sendPacket(new OrderPacket(Order.ON_REACH_RECEIVED, 2,
				Integer.valueOf(sutehaiIndex)));
	}

	@Override
	public void notifyKyokuResult(KyokuResult kr, int[] newScore,
			int[] oldScore, List<Integer> soten, List<Hai> uradoraList) {
		sendPacket(new OrderPacket(Order.ON_KYOKU_RESULT_RECEIVED, 1, kr));
		sendPacket(new OrderPacket(Order.ON_KYOKU_RESULT_RECEIVED, 2, newScore));
		sendPacket(new OrderPacket(Order.ON_KYOKU_RESULT_RECEIVED, 3, oldScore));
		sendPacket(new OrderPacket(Order.ON_KYOKU_RESULT_RECEIVED, 4, soten));
		sendPacket(new OrderPacket(Order.ON_KYOKU_RESULT_RECEIVED, 5,
				uradoraList));
	}

	@Override
	public void onNextKyokuRequested() {
		endResultPage = true;
	}

	@Override
	public void notifyGameResult(int[] Score) {
		sendPacket(new OrderPacket(Order.ON_GAME_RESULT_RECEIVED, 1, Score));
	}

	@Override
	public void sendGameOver() {
		sendPacket(new OrderPacket(Order.ON_GAME_OVER_RECEIVED, 0, null));
	}

	private void sendPacket(OrderPacket packet) {
		communicator.sendPacket(packet);
	}

	private class ServerCommunicator extends OrderCommunicator {

		@Override
		public void dispatch(Order order) {
			List<OrderPacket> packets = getPackets(order);
			System.out.println("dispatch");
			System.out.println(order);
			switch (order) {
			case ON_ANKAN_INDEX_LIST_RECEIVED:
				onAnkanIndexListReceived((List<Integer>) packets.get(0)
						.getArg());
				break;
			case ON_ANKANABLE_INDEX_LISTS_RECEIVED:
				break;
			case ON_CHI_INDEX_LIST_RECEIVED:
				onChiIndexListReceived((List<Integer>) packets.get(0).getArg());
				break;
			case ON_CHIABLE_INDEX_LISTS_RECEIVED:
				break;
			case ON_DISCARD_INDEX_RECEIVED:
				onDiscardIndexReceived((Integer) packets.get(0).getArg());
				break;
			case ON_DISCARD_RECEIVED_0:
				break;
			case ON_DISCARD_RECEIVED_1:
				break;
			case ON_FIELD_RECEIVED:
				break;
			case ON_GAME_OVER_RECEIVED:
				break;
			case ON_GAME_REQUESTED:
				onGameRequested((Integer) packets.get(0).getArg());
				break;
			case ON_GAME_RESULT_RECEIVED:
				break;
			case ON_GAME_START_RECEIVED:
				break;
			case ON_KAKANABLE_INDEX_LISTS_RECEIVED:
				break;
			case ON_KAKANABLE_INDEX_RECEIVED:
				onKakanableIndexReceived((Integer) packets.get(0).getArg());
				break;
			case ON_KYOKU_RESULT_RECEIVED:
				break;
			case ON_KYUSYUKYUHAI_RECEIVED:
				onKyusyukyuhaiReceived((Boolean) packets.get(0).getArg());
				break;
			case ON_KYUSYUKYUHAI_REQUESTED:
				break;
			case ON_MINKANABLE_INDEX_LISTS_RECEIVED:
				break;
			case ON_MINKANABLE_INDEX_RECEIVED:
				onMinkanableIndexReceived((Boolean)packets.get(0).getArg());
				break;
			case ON_NAKI_RECEIVED:
				break;
			case ON_NEXT_KYOKU_REQUESTED:
				onNextKyokuRequested();
				break;
			case ON_PON_INDEX_LIST_RECEIVED:
				onPonIndexListReceived((List<Integer>)packets.get(0).getArg());
				break;
			case ON_PONABLE_INDEX_LISTS_RECEIVED:
				break;
			case ON_REACH_INDEX_RECEIVED:
				onReachIndexReceived((Integer)packets.get(0).getArg());
				break;
			case ON_REACH_RECEIVED:
				break;
			case ON_REACHABLE_INDEX_LIST_RECEIVED:
				break;
			case ON_RON_RECEIVED:
				onRonReceived((Boolean)packets.get(0).getArg());
				break;
			case ON_RON_REQUESTED:
				break;
			case ON_START_KYOKU_RECEIVED:
				break;
			case ON_TSUMO_AGARI_REQUESTED:
				break;
			case ON_TSUMO_ARGARI_RECEIVED:
				onTsumoAgariReceived();
				break;
			case ON_TSUMO_HAI_RECEIVED:
				break;
			case ON_TSUMOGIRI_RECEIVED:
				break;
			}
		}

	}
	
	public void connectClient(ObjectInputStream ois,ObjectOutputStream oos){
		communicator = new ServerCommunicator();
		communicator.launch(ois, oos);
	}

}
