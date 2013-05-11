package client;

import server.Transporter;


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
//>>>>>>> 59048d61396abfaf3fc8e051137796334e3876bb
//		tr.onChiIndexListReceived(hais != null ? new ArrayList<Integer>(hais)
//				: null);
//		background.refreshStateCodes();
//	}
//
//	public void sendPonIndexList(List<Integer> hais) {
//<<<<<<< HEAD
//=======
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//>>>>>>> 59048d61396abfaf3fc8e051137796334e3876bb
//		tr.onPonIndexListReceived(hais != null ? new ArrayList<Integer>(hais)
//				: null);
//		background.refreshStateCodes();
//	}
//
//	public void sendAnkanIndexList(List<Integer> hais) {
//<<<<<<< HEAD
//=======
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//>>>>>>> 59048d61396abfaf3fc8e051137796334e3876bb
//		tr.onAnkanIndexListReceived(hais != null ? new ArrayList<Integer>(hais)
//				: null);
//		background.refreshStateCodes();
//	}
//
//	public void sendMinkan(boolean answer) {
//<<<<<<< HEAD
//=======
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//>>>>>>> 59048d61396abfaf3fc8e051137796334e3876bb
//		tr.onMinkanableIndexReceived(answer);
//		background.refreshStateCodes();
//	}
//
//	public void sendKakanIndex(int index) {
//<<<<<<< HEAD
//=======
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//>>>>>>> 59048d61396abfaf3fc8e051137796334e3876bb
//		tr.onKakanableIndexReceived(index);
//		background.refreshStateCodes();
//	}
//
//	public void sendReachIndex(int index) {
//<<<<<<< HEAD
//=======
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//>>>>>>> 59048d61396abfaf3fc8e051137796334e3876bb
//		tr.onReachIndexReceived(index);
//		background.refreshStateCodes();
//		if (index != -1)
//			background.setTsumoHai(null);
//	}
//
//	public void sendRon(boolean answer) {
//<<<<<<< HEAD
//=======
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//>>>>>>> 59048d61396abfaf3fc8e051137796334e3876bb
//		tr.onRonReceived(answer);
//		background.refreshStateCodes();
//	}
//
//	public void sendKyusyukyuhai(boolean flag) {
//		tr.onKyusyukyuhaiReceived(flag);
//	}
//
//	public void onChiableIndexListsReceived(List<List<Integer>> hais) {
//<<<<<<< HEAD
//=======
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//>>>>>>> 59048d61396abfaf3fc8e051137796334e3876bb
//		// TODO to be removed
//		background.setTsumoHai(null);
//		background.addButtonList(StateCode.SELECT_CHI);
//		background.addStateCode(StateCode.SELECT_BUTTON);
//		background.getAbleIndexList().put(StateCode.SELECT_CHI_HAI, hais);
//	}
//
//	public void onPonableIndexListsReceived(List<List<Integer>> hais) {
//<<<<<<< HEAD
//=======
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//>>>>>>> 59048d61396abfaf3fc8e051137796334e3876bb
//		// TODO to be removed
//		background.setTsumoHai(null);
//		
//		background.addButtonList(StateCode.SELECT_PON);
//		background.addStateCode(StateCode.SELECT_BUTTON);
//		background.getAbleIndexList().put(StateCode.SELECT_PON_HAI, hais);
//	}
//
//	public void onMinkanableIndexListReceived(List<Integer> hais) {
//<<<<<<< HEAD
//=======
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//>>>>>>> 59048d61396abfaf3fc8e051137796334e3876bb
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
//<<<<<<< HEAD
//=======
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//>>>>>>> 59048d61396abfaf3fc8e051137796334e3876bb
//
//		background.addButtonList(StateCode.KYUSYUKYUHAI);
//		background.addStateCode(StateCode.SELECT_BUTTON);
//	}
//
//	public void onAnkanableIndexListsReceived(List<List<Integer>> hais) {
//<<<<<<< HEAD
//=======
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//>>>>>>> 59048d61396abfaf3fc8e051137796334e3876bb
//
//		background.addStateCode(StateCode.SELECT_BUTTON);
//		background.addButtonList(StateCode.SELECT_ANKAN);
//		background.getAbleIndexList().put(StateCode.SELECT_ANKAN_HAI, hais);
//	}
//
//	public void onKakanableIndexListReceived(List<Integer> hais) {
//<<<<<<< HEAD
//=======
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//
//>>>>>>> 59048d61396abfaf3fc8e051137796334e3876bb
//		List<List<Integer>> tmpList = new ArrayList<List<Integer>>();
//		tmpList.add(hais);
//		background.addStateCode(StateCode.SELECT_BUTTON);
//		background.addButtonList(StateCode.SELECT_KAKAN);
//		background.getAbleIndexList().put(StateCode.SELECT_KAKAN_HAI, tmpList);
//	}
//
//	public void onReachableIndexListReceived(List<Integer> hais) {
//<<<<<<< HEAD
//=======
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//
//>>>>>>> 59048d61396abfaf3fc8e051137796334e3876bb
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
//<<<<<<< HEAD
////		background.startAnimation(background.kaze.get(currentTurn),
////				StateCode.SELECT_REACH);
//		int currentIndex = background.getKaze().get(currentTurn);
//		background.getReachPosMap().put(currentIndex, sutehaiIndex + 1);
//	}
//
//	public void onTsumoAgariRequested() {
//		background.addStateCode(StateCode.SELECT_BUTTON);
//		background.addButtonList(StateCode.SELECT_TSUMO);
//	}
//
//	public void onRonRequested() {
//=======
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
//>>>>>>> 59048d61396abfaf3fc8e051137796334e3876bb
//		// TODO to be removed
//		background.setTsumoHai(null);
//
//		background.addStateCode(StateCode.SELECT_BUTTON);
//		background.addButtonList(StateCode.SELECT_RON);
//	}
//
//<<<<<<< HEAD
//	public void onNakiReceived(Player player, Mentu mentu) {
//
//=======
//	public void onNakiReceived(Player player, Mentsu mentu) {
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//>>>>>>> 59048d61396abfaf3fc8e051137796334e3876bb
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
//<<<<<<< HEAD
////		background.startAnimation(-1, StateCode.SELECT_RON);
//=======
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//		canvas.startAnimation(-1, StateCode.SELECT_RON);
//>>>>>>> 59048d61396abfaf3fc8e051137796334e3876bb
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//		}
//
//		background.setMode(PackName.Result);
//	}
//
//	public void onTsumoHaiReceived(Hai hai) {
//<<<<<<< HEAD
//		background.setTsumoHai(hai);
//		background.addStateCode(StateCode.DISCARD_SELECT);
//=======
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//		canvas.getInfo().tsumoHai = hai;
//		canvas.addStateCode(StateCode.DISCARD_SELECT);
//>>>>>>> 59048d61396abfaf3fc8e051137796334e3876bb
//	}
//
//	
//	//ON_DISCARD_RECEIVED_0に対応
//	public void onDiscardReceived(boolean existTsumo) {
//<<<<<<< HEAD
//		background.addStateCode(StateCode.DISCARD_SELECT);
//=======
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//
//		canvas.addStateCode(StateCode.DISCARD_SELECT);
//>>>>>>> 59048d61396abfaf3fc8e051137796334e3876bb
//	}
//	//ON_DISCARD_RECEIVED_1に対応
//	public void onDiscardReceived(Player player, Hai hai, boolean isTsumo) {
//<<<<<<< HEAD
//		background.setTsumoHai(null);
//=======
//		System.out.println(page == null);
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//
//		canvas.getInfo().tsumoHai = null;
//>>>>>>> 59048d61396abfaf3fc8e051137796334e3876bb
//	}
//
//	@Override
//	public void sendTsumoAgari() {
//<<<<<<< HEAD
//=======
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//>>>>>>> 59048d61396abfaf3fc8e051137796334e3876bb
//		tr.onTsumoAgariReceived();
//		background.refreshStateCodes();
//	}
//
//	@Override
//	public void onTsumoAgariReceived() {
//<<<<<<< HEAD
//		//TODO animation
////		background.startAnimation(-1, StateCode.SELECT_TSUMO);
//=======
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//		canvas.startAnimation(-1, StateCode.SELECT_TSUMO);
//>>>>>>> 59048d61396abfaf3fc8e051137796334e3876bb
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
//<<<<<<< HEAD
//		background.setMode(PackName.Game);
//		background.setHonba(honba);
//		background.setTsumiBou(tsumibou);
//		background.setNumber(background.getPlayerNumber());
//		background.setKyokusu(kyokusu);
//		background.setBakaze(bakaze);
//=======
//		if (page == null)
//			return;
//		page.movePage("game");
//		frame.getInfo().honba = honba;
//		frame.getInfo().tsumiBou = tsumibou;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//		canvas.number = frame.getInfo().playerNumber;
//		canvas.setKyokusu(kyokusu);
//		canvas.setBakaze(bakaze);
//>>>>>>> 59048d61396abfaf3fc8e051137796334e3876bb
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
//<<<<<<< HEAD
//		background.setTehai(tehai);
//		background.setCurrentTurn(background.getKaze().get(currentTurn));
//=======
//		if (page == null || !isPageIsCanvas())
//			return;
//		MahjongCanvas canvas = (MahjongCanvas) page;
//		ClientInfo info = canvas.getInfo();
//		info.tehai = tehai;
//		info.currentTurn = info.kaze.get(currentTurn);
//>>>>>>> 59048d61396abfaf3fc8e051137796334e3876bb
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
