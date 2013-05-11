package client;

import server.Transporter;
import system.hai.Hai;
import system.hai.HurohaiList;
import system.hai.Kaze;
import system.hai.Mentsu;
import system.hai.Mentsu.Type;
import system.result.KyokuResult;
import system.Player;

public class ClientOperator implements Client {
	private Server tr;
	private Page page;
	private MahjongFrame frame;

	public void setFrame(MahjongFrame frame) {
		this.frame = frame;
	}

	public ClientOperator() {
	}
 
	public void setTransporter(Transporter tr) {
		this.tr = tr;
	}

	public boolean isPageIsCanvas() {
		return page instanceof MahjongCanvas;
	}

	public ClientOperator(Server tr) {
		this.tr = tr;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public ClientOperator(MahjongCanvas c) {
		this.page = c;
	}

	public void setTransporter(Server tr) {
		this.tr = tr;
	}

	public void sendDiscardIndex(int index) {
		if (page == null || !isPageIsCanvas())
			return;
		MahjongCanvas canvas = (MahjongCanvas) page;
		tr.onDiscardIndexReceived(index);
		canvas.getInfo().tsumoHai = null;
		canvas.refreshStateCodes();
	}

	public void onTsumoGiriReceived() {
		if (page == null || !isPageIsCanvas())
			return;
		MahjongCanvas canvas = (MahjongCanvas) page;
		canvas.getInfo().tsumoHai = null;
		canvas.refreshStateCodes();
	}

	public void sendChiIndexList(List<Integer> hais) {
		if (page == null || !isPageIsCanvas())
			return;
		MahjongCanvas canvas = (MahjongCanvas) page;
		tr.onChiIndexListReceived(hais != null ? new ArrayList<Integer>(hais)
				: null);
		canvas.refreshStateCodes();
	}

	public void sendPonIndexList(List<Integer> hais) {
		if (page == null || !isPageIsCanvas())
			return;
		MahjongCanvas canvas = (MahjongCanvas) page;
		tr.onPonIndexListReceived(hais != null ? new ArrayList<Integer>(hais)
				: null);
		canvas.refreshStateCodes();
	}

	public void sendAnkanIndexList(List<Integer> hais) {
		if (page == null || !isPageIsCanvas())
			return;
		MahjongCanvas canvas = (MahjongCanvas) page;
		tr.onAnkanIndexListReceived(hais != null ? new ArrayList<Integer>(hais)
				: null);
		canvas.refreshStateCodes();
	}

	public void sendMinkan(boolean answer) {
		if (page == null || !isPageIsCanvas())
			return;
		MahjongCanvas canvas = (MahjongCanvas) page;
		tr.onMinkanableIndexReceived(answer);
		canvas.refreshStateCodes();
	}

	public void sendKakanIndex(int index) {
		if (page == null || !isPageIsCanvas())
			return;
		MahjongCanvas canvas = (MahjongCanvas) page;
		tr.onKakanableIndexReceived(index);
		canvas.refreshStateCodes();
	}

	public void sendReachIndex(int index) {
		if (page == null || !isPageIsCanvas())
			return;
		MahjongCanvas canvas = (MahjongCanvas) page;
		tr.onReachIndexReceived(index);
		canvas.refreshStateCodes();
		if (index != -1)
			canvas.getInfo().tsumoHai = null;
	}

	public void sendRon(boolean answer) {
		if (page == null || !isPageIsCanvas())
			return;
		MahjongCanvas canvas = (MahjongCanvas) page;
		tr.onRonReceived(answer);
		canvas.refreshStateCodes();
	}

	public void sendKyusyukyuhai(boolean flag) {
		tr.onKyusyukyuhaiReceived(flag);
	}

	public void onChiableIndexListsReceived(List<List<Integer>> hais) {
		if (page == null || !isPageIsCanvas())
			return;
		MahjongCanvas canvas = (MahjongCanvas) page;
		// TODO to be removed
		canvas.getInfo().tsumoHai = null;
		canvas.addButtonList(StateCode.SELECT_CHI);
		canvas.addStateCode(StateCode.SELECT_BUTTON);
		canvas.getInfo().ableIndexList.put(StateCode.SELECT_CHI_HAI, hais);
	}

	public void onPonableIndexListsReceived(List<List<Integer>> hais) {
		if (page == null || !isPageIsCanvas())
			return;
		MahjongCanvas canvas = (MahjongCanvas) page;
		// TODO to be removed
		canvas.getInfo().tsumoHai = null;

		canvas.addButtonList(StateCode.SELECT_PON);
		canvas.addStateCode(StateCode.SELECT_BUTTON);
		canvas.getInfo().ableIndexList.put(StateCode.SELECT_PON_HAI, hais);
	}

	public void onMinkanableIndexListReceived(List<Integer> hais) {
		if (page == null || !isPageIsCanvas())
			return;
		MahjongCanvas canvas = (MahjongCanvas) page;
		// TODO to be removed
		canvas.getInfo().tsumoHai = null;

		List<List<Integer>> tmpList = new ArrayList<List<Integer>>();
		tmpList.add(hais);
		canvas.addButtonList(StateCode.SELECT_MINKAN);
		canvas.addStateCode(StateCode.SELECT_BUTTON);
		canvas.getInfo().ableIndexList.put(StateCode.SELECT_MINKAN, tmpList);
	}

	public void onKyusyukyuhaiRequested() {
		if (page == null || !isPageIsCanvas())
			return;
		MahjongCanvas canvas = (MahjongCanvas) page;

		canvas.addButtonList(StateCode.KYUSYUKYUHAI);
		canvas.addStateCode(StateCode.SELECT_BUTTON);
	}

	public void onAnkanableIndexListsReceived(List<List<Integer>> hais) {
		if (page == null || !isPageIsCanvas())
			return;
		MahjongCanvas canvas = (MahjongCanvas) page;

		canvas.addStateCode(StateCode.SELECT_BUTTON);
		canvas.addButtonList(StateCode.SELECT_ANKAN);
		canvas.getInfo().ableIndexList.put(StateCode.SELECT_ANKAN_HAI, hais);
	}

	public void onKakanableIndexListReceived(List<Integer> hais) {
		if (page == null || !isPageIsCanvas())
			return;
		MahjongCanvas canvas = (MahjongCanvas) page;

		List<List<Integer>> tmpList = new ArrayList<List<Integer>>();
		tmpList.add(hais);
		canvas.addStateCode(StateCode.SELECT_BUTTON);
		canvas.addButtonList(StateCode.SELECT_KAKAN);
		canvas.getInfo().ableIndexList.put(StateCode.SELECT_KAKAN_HAI, tmpList);
	}

	public void onReachableIndexListReceived(List<Integer> hais) {
		if (page == null || !isPageIsCanvas())
			return;
		MahjongCanvas canvas = (MahjongCanvas) page;

		List<List<Integer>> tmpList = new ArrayList<List<Integer>>();
		for (Integer i : hais) {
			List<Integer> tmp = new ArrayList<Integer>();
			tmp.add(i);
			tmpList.add(tmp);
		}
		canvas.addStateCode(StateCode.SELECT_BUTTON);
		canvas.addButtonList(StateCode.SELECT_REACH);
		canvas.getInfo().ableIndexList.put(StateCode.SELECT_REACH_HAI, tmpList);
	}

	public void onReachReceived(Kaze currentTurn, int sutehaiIndex) {
		if (page == null || !isPageIsCanvas())
			return;
		MahjongCanvas canvas = (MahjongCanvas) page;
		ClientInfo info = canvas.getInfo();
		canvas.startAnimation(info.kaze.get(currentTurn),
				StateCode.SELECT_REACH);
		int currentIndex = info.kaze.get(currentTurn);
		info.reachPosMap.put(currentIndex, sutehaiIndex + 1);
	}

	public void onTsumoAgariRequested() {
		if (page == null || !isPageIsCanvas())
			return;
		MahjongCanvas canvas = (MahjongCanvas) page;
		canvas.addStateCode(StateCode.SELECT_BUTTON);
		canvas.addButtonList(StateCode.SELECT_TSUMO);
	}

	public void onRonRequested() {
		if (page == null || !isPageIsCanvas())
			return;
		MahjongCanvas canvas = (MahjongCanvas) page;
		// TODO to be removed
		canvas.getInfo().tsumoHai = null;

		canvas.addStateCode(StateCode.SELECT_BUTTON);
		canvas.addButtonList(StateCode.SELECT_RON);
	}

	public void onNakiReceived(Player player, Mentsu mentu) {
		if (page == null || !isPageIsCanvas())
			return;
		MahjongCanvas canvas = (MahjongCanvas) page;
		int index;
		for (index = 0; index < canvas.getInfo().players.length; index++) {
			if (canvas.getInfo().players[index] == player)
				break;
		}
		switch (mentu.type()) {
		case KANTU:
			canvas.startAnimation(index, StateCode.SELECT_KAN);
			break;
		case SYUNTU:
			canvas.startAnimation(index, StateCode.SELECT_CHI);
			break;
		case KOTU:
			canvas.startAnimation(index, StateCode.SELECT_PON);
			break;
		default:
			break;
		}
		canvas.refreshStateCodes();
		canvas.refreshButtonList();
	}

	public void onRonReceived(Map<Player, List<Hai>> playerHaiMap) {
		if (page == null || !isPageIsCanvas())
			return;
		MahjongCanvas canvas = (MahjongCanvas) page;
		canvas.startAnimation(-1, StateCode.SELECT_RON);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		page.movePage("result");
	}

	public void onTsumoHaiReceived(Hai hai) {
		if (page == null || !isPageIsCanvas())
			return;
		MahjongCanvas canvas = (MahjongCanvas) page;
		canvas.getInfo().tsumoHai = hai;
		canvas.addStateCode(StateCode.DISCARD_SELECT);
	}

	public void onDiscardReceived(boolean existTsumo) {
		if (page == null || !isPageIsCanvas())
			return;
		MahjongCanvas canvas = (MahjongCanvas) page;

		canvas.addStateCode(StateCode.DISCARD_SELECT);
	}

	public void onDiscardReceived(Player player, Hai hai, boolean isTsumo) {
		System.out.println(page == null);
		if (page == null || !isPageIsCanvas())
			return;
		MahjongCanvas canvas = (MahjongCanvas) page;

		canvas.getInfo().tsumoHai = null;
	}

	@Override
	public void sendTsumoAgari() {
		if (page == null || !isPageIsCanvas())
			return;
		MahjongCanvas canvas = (MahjongCanvas) page;
		tr.onTsumoAgariReceived();
		canvas.refreshStateCodes();
	}

	@Override
	public void onTsumoAgariReceived() {
		if (page == null || !isPageIsCanvas())
			return;
		MahjongCanvas canvas = (MahjongCanvas) page;
		canvas.startAnimation(-1, StateCode.SELECT_TSUMO);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		page.movePage("result");
	}

	@Override
	public void requestGame(int id) {
		tr.onGameRequested(id);
		// TODO Auto-generated method stub
	}

	@Override
	public void onGameStartReceived(List<Player> playerList, int index,
			int[] score) {
		Map<Kaze, Player> playerMap = new HashMap<Kaze, Player>(4);
		for (int i = 0; i < playerList.size(); i++) {
			playerMap.put(Kaze.valueOf(i), playerList.get(i));
		}

		Kaze playerKaze = Kaze.valueOf(index);

		// TODO insert score
		Player[] players = new Player[playerList.size()];
		for (int i = 0; i < playerList.size(); i++)
			players[i] = playerList.get(i);
		// TODO to be changed
		index = 1;
		ClientInfo tmpInfo = frame.getInfo();
		tmpInfo.setIndex(index);
		tmpInfo.playerNumber = index;
		tmpInfo.players = players;
		tmpInfo.sekiMap = new HashMap<Player, Integer>(4);
		for (int i = 0; i < 4; i++) {
			tmpInfo.sekiMap.put(tmpInfo.players[i], (4 - index + i) % 4);
			tmpInfo.setScore((4 - index + i) % 4, score[i]);
		}
		// tmpInfo.setIndex(index);
		frame.setInfo(tmpInfo);
		frame.setPage("game");
	}

	@Override
	public void onStartKyokuReceived(Kaze bakaze, int kyokusu, int honba,
			int tsumibou) {
		// TODO Auto-generated method stub
		if (page == null)
			return;
		page.movePage("game");
		frame.getInfo().honba = honba;
		frame.getInfo().tsumiBou = tsumibou;
		MahjongCanvas canvas = (MahjongCanvas) page;
		canvas.number = frame.getInfo().playerNumber;
		canvas.setKyokusu(kyokusu);
		canvas.setBakaze(bakaze);
	}

	public void requestNextKyoku() {
		System.out.println("requestNextKyoku");
		tr.onNextKyokuRequested();
	}

	public void onGameResultReceived(int[] score) {
		// TODO to be changed
	}

	@Override
	public void onFieldReceived(List<Hai> tehai,
			Map<Kaze, HurohaiList> nakihai, Map<Kaze, List<Hai>> sutehai,
			Kaze currentTurn, Hai currentSutehai, List<Integer> tehaiSize,
			int yamaSize, int wanpaiSize, List<Hai> doraList) {
		if (page == null || !isPageIsCanvas())
			return;
		MahjongCanvas canvas = (MahjongCanvas) page;
		ClientInfo info = canvas.getInfo();
		info.tehai = tehai;
		info.currentTurn = info.kaze.get(currentTurn);
		for (Kaze k : Kaze.values()) {
			int i = info.kaze.get(k);
			synchronized (info.sutehaiMap) {
				info.sutehaiMap.put(i, sutehai.get(k));
			}
			if (k == currentTurn)
				info.sutehaiMap.get(i).add(currentSutehai);
			info.hurohaiMap.put(i, nakihai.get(k));
		}

		info.tehaiSizeMap.put(info.kaze.get(Kaze.TON), tehaiSize.get(0));
		info.tehaiSizeMap.put(info.kaze.get(Kaze.NAN), tehaiSize.get(1));
		info.tehaiSizeMap.put(info.kaze.get(Kaze.SYA), tehaiSize.get(2));
		info.tehaiSizeMap.put(info.kaze.get(Kaze.PE), tehaiSize.get(3));

		info.yamaSize = yamaSize;
		info.wanpaiSize = wanpaiSize;
		info.doraList = doraList;
	}
=======
>>>>>>> 4bc2f67f4c07fd04e6b951bcdd9956f30c11294c


public abstract class ClientOperator implements Client{
	
	public void setTransporter(Transporter tr){}
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -3438038457328126047L;
//	private Server tr;
//	private BackgroundSystemOfClient background;
//
//	
//	public void setBackground(BackgroundSystemOfClient background) {
//		this.background = background;
//	}
//
//	public ClientOperator() {
//	}
//
//	public void setTransporter(Transporter tr) {
//		this.tr = tr;
//	}
//
//	public ClientOperator(Server tr) {
//		this.tr = tr;
//	}
//
//	public void setTransporter(Server tr) {
//		this.tr = tr;
//	}
//
//	public void sendDiscardIndex(int index) {
//		tr.onDiscardIndexReceived(index);
//		background.setTsumoHai(null);
//		background.refreshStateCodes();
//	}
//
//	public void onTsumoGiriReceived() {
//<<<<<<< HEAD
//		background.setTsumoHai(null);
//		background.refreshStateCodes();
//	}
//
//	public void sendChiIndexList(List<Integer> hais) {
//=======
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//		canvas.getInfo().tsumoHai = null;
//		canvas.refreshStateCodes();
//	}
//
//	public void sendChiIndexList(List<Integer> hais) {
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//		tr.onChiIndexListReceived(hais != null ? new ArrayList<Integer>(hais)
//				: null);
//		background.refreshStateCodes();
//	}
//
//	public void sendPonIndexList(List<Integer> hais) {
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//		tr.onPonIndexListReceived(hais != null ? new ArrayList<Integer>(hais)
//				: null);
//		background.refreshStateCodes();
//	}
//
//	public void sendAnkanIndexList(List<Integer> hais) {
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//		tr.onAnkanIndexListReceived(hais != null ? new ArrayList<Integer>(hais)
//				: null);
//		background.refreshStateCodes();
//	}
//
//	public void sendMinkan(boolean answer) {
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//		tr.onMinkanableIndexReceived(answer);
//		background.refreshStateCodes();
//	}
//
//	public void sendKakanIndex(int index) {
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//		tr.onKakanableIndexReceived(index);
//		background.refreshStateCodes();
//	}
//
//	public void sendReachIndex(int index) {
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//		tr.onReachIndexReceived(index);
//		background.refreshStateCodes();
//		if (index != -1)
//			background.setTsumoHai(null);
//	}
//
//	public void sendRon(boolean answer) {
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//		tr.onRonReceived(answer);
//		background.refreshStateCodes();
//	}
//
//	public void sendKyusyukyuhai(boolean flag) {
//		tr.onKyusyukyuhaiReceived(flag);
//	}
//
//	public void onChiableIndexListsReceived(List<List<Integer>> hais) {
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//		// TODO to be removed
//		background.setTsumoHai(null);
//		background.addButtonList(StateCode.SELECT_CHI);
//		background.addStateCode(StateCode.SELECT_BUTTON);
//		background.getAbleIndexList().put(StateCode.SELECT_CHI_HAI, hais);
//	}
//
//	public void onPonableIndexListsReceived(List<List<Integer>> hais) {
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//		// TODO to be removed
//		background.setTsumoHai(null);
//		
//		background.addButtonList(StateCode.SELECT_PON);
//		background.addStateCode(StateCode.SELECT_BUTTON);
//		background.getAbleIndexList().put(StateCode.SELECT_PON_HAI, hais);
//	}
//
//	public void onMinkanableIndexListReceived(List<Integer> hais) {
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//		// TODO to be removed
//		background.setTsumoHai(null);
//
//		List<List<Integer>> tmpList = new ArrayList<List<Integer>>();
//		tmpList.add(hais);
//		background.addButtonList(StateCode.SELECT_MINKAN);
//		background.addStateCode(StateCode.SELECT_BUTTON);
//		background.getAbleIndexList().put(StateCode.SELECT_MINKAN, tmpList);
//	}
//
//	public void onKyusyukyuhaiRequested() {
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//
//		background.addButtonList(StateCode.KYUSYUKYUHAI);
//		background.addStateCode(StateCode.SELECT_BUTTON);
//	}
//
//	public void onAnkanableIndexListsReceived(List<List<Integer>> hais) {
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//
//		background.addStateCode(StateCode.SELECT_BUTTON);
//		background.addButtonList(StateCode.SELECT_ANKAN);
//		background.getAbleIndexList().put(StateCode.SELECT_ANKAN_HAI, hais);
//	}
//
//	public void onKakanableIndexListReceived(List<Integer> hais) {
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//
//		List<List<Integer>> tmpList = new ArrayList<List<Integer>>();
//		tmpList.add(hais);
//		background.addStateCode(StateCode.SELECT_BUTTON);
//		background.addButtonList(StateCode.SELECT_KAKAN);
//		background.getAbleIndexList().put(StateCode.SELECT_KAKAN_HAI, tmpList);
//	}
//
//	public void onReachableIndexListReceived(List<Integer> hais) {
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//
//		List<List<Integer>> tmpList = new ArrayList<List<Integer>>();
//		for (Integer i : hais) {
//			List<Integer> tmp = new ArrayList<Integer>();
//			tmp.add(i);
//			tmpList.add(tmp);
//		}
//		background.addStateCode(StateCode.SELECT_BUTTON);
//		background.addButtonList(StateCode.SELECT_REACH);
//		background.getAbleIndexList().put(StateCode.SELECT_REACH_HAI, tmpList);
//	}
//
//	public void onReachReceived(Kaze currentTurn, int sutehaiIndex) {
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//		ClientInfo info = canvas.getInfo();
//		canvas.startAnimation(info.kaze.get(currentTurn),
//				StateCode.SELECT_REACH);
//		int currentIndex = info.kaze.get(currentTurn);
//		info.reachPosMap.put(currentIndex, sutehaiIndex + 1);
//	}
//
//	public void onTsumoAgariRequested() {
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//		canvas.addStateCode(StateCode.SELECT_BUTTON);
//		canvas.addButtonList(StateCode.SELECT_TSUMO);
//	}
//
//	public void onRonRequested() {
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//		// TODO to be removed
//		background.setTsumoHai(null);
//
//		background.addStateCode(StateCode.SELECT_BUTTON);
//		background.addButtonList(StateCode.SELECT_RON);
//	}
//
//	public void onNakiReceived(Player player, Mentsu mentu) {
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//		int index;
//		for (index = 0; index < background.getPlayers().length; index++) {
//			if (background.getPlayers()[index] == player)
//				break;
//		}
////		switch (mentu.type()) {
////		case KANTU:
////			background.startAnimation(index, StateCode.SELECT_KAN);
////			break;
////		case SYUNTU:
////			background.startAnimation(index, StateCode.SELECT_CHI);
////			break;
////		case KOTU:
////			background.startAnimation(index, StateCode.SELECT_PON);
////			break;
////		default:
////			break;
////		}
//		background.refreshStateCodes();
//		background.refreshButtonList();
//	}
//
//	public void onRonReceived(Map<Player, List<Hai>> playerHaiMap) {
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//		canvas.startAnimation(-1, StateCode.SELECT_RON);
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//		}
//
//		background.setMode(PackName.Result);
//	}
//
//	public void onTsumoHaiReceived(Hai hai) {
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//		canvas.getInfo().tsumoHai = hai;
//		canvas.addStateCode(StateCode.DISCARD_SELECT);
//	}
//
//	
//	//ON_DISCARD_RECEIVED_0に対応
//	public void onDiscardReceived(boolean existTsumo) {
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//
//		canvas.addStateCode(StateCode.DISCARD_SELECT);
//	}
//	//ON_DISCARD_RECEIVED_1に対応
//	public void onDiscardReceived(Player player, Hai hai, boolean isTsumo) {
//		System.out.println(page == null);
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//
//		canvas.getInfo().tsumoHai = null;
//	}
//
//	@Override
//	public void sendTsumoAgari() {
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//		tr.onTsumoAgariReceived();
//		background.refreshStateCodes();
//	}
//
//	@Override
//	public void onTsumoAgariReceived() {
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//		canvas.startAnimation(-1, StateCode.SELECT_TSUMO);
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//		}
//		background.setMode(PackName.Result);
//	}
//
//	@Override
//	public void requestGame(int id) {
//		tr.onGameRequested(id);
//		// TODO Auto-generated method stub
//	}
//
//	@Override
//	public void onGameStartReceived(List<Player> playerList, int index,
//			int[] score) {
//		Map<Kaze, Player> playerMap = new HashMap<Kaze, Player>(4);
//		for (int i = 0; i < playerList.size(); i++) {
//			playerMap.put(Kaze.valueOf(i), playerList.get(i));
//		}
//
////		Kaze playerKaze = Kaze.valueOf(index);
//
//		// TODO insert score
//		// TODO to be changed
////		index = 1;
//		System.out.println("index:" + index);
//		background.setPlayerNumber(index);
//		background.setPlayerToTheChair(playerList,index);
//		background.setSekiMap(new HashMap<Player, Integer>(4));
//		for (int i = 0; i < 4; i++) {
//			background.getSekiMap().put(background.getPlayers()[i], i);
//			background.setScore((4 - index + i) % 4, score[i]);
//		}
//		// tmpbackground.setIndex(index);
//		background.setMode(PackName.Game);
//	}
//
//	@Override
//	public void onStartKyokuReceived(Kaze bakaze, int kyokusu, int honba,
//			int tsumibou) {
//		// TODO Auto-generated method stub
//		if (page == null)
//			return;
//		page.movePage("game");
//		frame.getInfo().honba = honba;
//		frame.getInfo().tsumiBou = tsumibou;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//		canvas.number = frame.getInfo().playerNumber;
//		canvas.setKyokusu(kyokusu);
//		canvas.setBakaze(bakaze);
//	}
//
//	public void requestNextKyoku() {
//		tr.onNextKyokuRequested();
//	}
//
//	public void onGameResultReceived(int[] score) {
//		// TODO to be changed
//	}
//
//	@Override
//	public void onFieldReceived(List<Hai> tehai,
//			Map<Kaze, HurohaiList> nakihai, Map<Kaze, List<Hai>> sutehai,
//			Kaze currentTurn, Hai currentSutehai, List<Integer> tehaiSize,
//			int yamaSize, int wanpaiSize, List<Hai> doraList) {
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//		ClientInfo info = canvas.getInfo();
//		info.tehai = tehai;
//		info.currentTurn = info.kaze.get(currentTurn);
//		for (Kaze k : Kaze.values()) {
//			int i = background.getKaze().get(k);
//			synchronized (background.getSutehaiMap()) {
//				background.getSutehaiMap().put(i, sutehai.get(k));
//			}
//			if (k == currentTurn)
//				background.getSutehaiMap().get(i).add(currentSutehai);
//			background.getHurohaiMap().put(i, nakihai.get(k));
//		}
//
//		Map<Integer,Integer> tehaiSizeMap = background.getTehaiSizeMap();
//		
//		tehaiSizeMap.put(background.getKaze().get(Kaze.TON), tehaiSize.get(0));
//		tehaiSizeMap.put(background.getKaze().get(Kaze.NAN), tehaiSize.get(1));
//		tehaiSizeMap.put(background.getKaze().get(Kaze.SYA), tehaiSize.get(2));
//		tehaiSizeMap.put(background.getKaze().get(Kaze.PE), tehaiSize.get(3));
//
//		background.setYamaSize(yamaSize);
//		background.setWanpaiSize(wanpaiSize);
//		background.setDoraList(doraList);
//	}
//
//	@Override
//	public void onKyokuResultReceived(KyokuResult result, int[] newScores,
//			int[] oldScores, List<Integer> changeScore, List<Hai> uradoraList) {
//		try {
//			Thread.sleep(100);
//		} catch (InterruptedException e) {
//		}
//		background.setMode(PackName.Result);
//		background.setResult(result, newScores,oldScores,changeScore,uradoraList);
//		// TODO ok?
//	}
//
//	@Override
//	public void onGameOverReceived() {
//		// TODO Auto-generated method stub
//
//	}
//	
//	public void connectServer(){
//		try{
//			Socket s = new Socket("localhost", 5555);
//			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
//			oos.writeObject(this);
//			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
//			this.setTransporter((Transporter)ois.readObject());
//			s.close();
//		}catch(IOException e){
//			System.out.println("ClientSideError:" + e.getMessage());
//		}catch(ClassNotFoundException e){
//			System.out.println("ClientSideError:" + e.getMessage());
//		}
//	}
	

}
