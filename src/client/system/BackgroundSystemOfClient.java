package client.system;

import java.awt.event.MouseListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import client.datapack.CommunicatableListener;
import client.datapack.DataPacks;
import client.datapack.PackName;

import server.Transporter;
import system.Player;
import system.hai.Hai;
import system.hai.Kaze;
import system.hai.Mentsu;
import system.result.KyokuResult;

public class BackgroundSystemOfClient implements Serializable {
	/**
	 * 
	 */
	
	private static final long serialVersionUID = -6055123297792390870L;
	private MahjongFrame frame;
	private PackName mode;
	private DummyClient operator;
	private DataPacks datas;
	private PageManageThread pmt;

	private class PageManageThread extends Thread {
		private boolean isFinish;

		public PageManageThread() {
			isFinish = true;
		}

		public void run() {
			while (isFinish) {
				try {
					if (datas.getVersion(mode) != frame.getVersion()) {
						try{
							datas.createImage(mode);
							frame.setVersion(datas.getVersion(mode));
							frame.setImage(datas.getImage(mode));
						}catch(Exception e){
							
						}
					}
					frame.repaint();
					sleep(10);
				} catch (InterruptedException e) {
					System.out.println("interrupt");
				} catch (NullPointerException e) {
					System.out.println("nullPointer");
				} catch (Exception e) {
					System.out.println("other exception");
				}
			}
			System.out.println("while out");
			System.out.println(isFinish);
		}
	}

	{
		frame = new MahjongFrame();
		operator = new DummyClient();

		datas = new DataPacks();
		// modeの初期値設定
		setMode(PackName.Start);
		// 各DataPackにバッファを提供
		datas.setGraphics(frame.getCanvas());
		// 各MouseListenerにここへのアクセス権を与える
		CommunicatableListener.setBackground(this);
		// 描画用スレッドの作成
		pmt = new PageManageThread();
		pmt.start();
	}

	/**
	 * canvasに現在のmodeのMouseListenerを提供する
	 */
	private void setMouseListener() {
		for (MouseListener ml : frame.getCanvas().getMouseListeners()) {
			frame.getCanvas().removeMouseListener(ml);
		}
		try {
			this.frame.getCanvas().addMouseListener(datas.getListener(mode));
		} catch (Exception e) {
			System.out.println("No such DataPack:call getListener");
		}
	}

	public void setTsumoHai(Hai hai) {
		datas.getGame().setTsumoHai(hai);
	}

	/**
	 * Backgroundのmodeを指定したmodeに設定する
	 * 
	 * @param mode
	 */
	public void setMode(PackName mode) {
		try {
			if (this.mode != null)
				datas.finishPack(this.mode);
		} catch (Exception e) {
			System.out.println("No such mode");
		}
		if(mode == PackName.Game)
			datas.getGame().startGame();
		this.mode = mode;
		frame.setVersion(0);
		setMouseListener();
	}

	public static void main(String[] args) {
		new BackgroundSystemOfClient();
	}

	public void setServer(Transporter tr) {
//		this.operator.setTransporter(tr);
	}

	public BackgroundSystemOfClient() {
	}

	public void sendPonIndexList(List<Integer> hais) {
		operator.sendPonIndexList(hais);
	}

	public void sendDiscardIndex(Integer index) {
		operator.sendDiscardIndex(index);
	}

	public void sendChiIndexList(List<Integer> hais) {
		operator.sendChiIndexList(hais);
	}

	public void sendAnkanIndexList(List<Integer> hais) {
		operator.sendAnkanIndexList(hais);
	}

	public void sendKakanIndex(Integer index) {
		operator.sendKakanIndex(index);
	}

	public void sendReachIndex(Integer index) {
		operator.sendReachIndex(index);
	}

	public void sendMinkan(boolean answer) {
		operator.sendMinkan(answer);
	}

	public void sendKyusyukyuhai(boolean answer) {
		operator.sendKyusyukyuhai(answer);
	}

	public void sendRon(boolean answer) {
		operator.sendRon(answer);
	}

	public void refreshStateCodes() {
	}

	public void sendTsumoAgari() {
		operator.sendTsumoAgari();
	}

	public void addButtonList(StateCode sc) {
		datas.getGame().addButtonList(sc);
	}

	public Map<StateCode, List<List<Integer>>> getAbleIndexList() {
		return datas.getGame().getAbleIndexList();
	}

	public void addStateCode(StateCode sc) {
		datas.getGame().addStateCode(sc);
	}

	public Map<Integer, Integer> getReachPosMap() {
		return datas.getGame().getReachPosMap();
	}

	public Map<Kaze, Integer> getKaze() {
		return datas.getGame().getKaze();
	}

	public void setTehai(List<Hai> tehai) {
		datas.getGame().setTehai(tehai);
	}

	public void setCurrentTurn(int currentTurn) {
		datas.getGame().setCurrentTurn(currentTurn);
	}

	public Map<Integer, List<Hai>> getSutehaiMap() {
		return datas.getGame().getSutehaiMap();
	}

	public Player[] getPlayers() {
		ClientPlayer[] cps = datas.getGame().getPlayers();
		Player[] result = new Player[cps.length];
		for (int i = 0; i < cps.length; i++) {
			ClientPlayer cp = cps[i];
			if(cp != null)
				result[i] = new Player(cp.getId(), cp.getName(), true);
			else
				result[i] = new Player(1,"unknown",false);
		}
		return result;
	}

	public void refreshButtonList() {
		datas.getGame().refreshButtonList();
	}

	public void setPlayerToTheChair(List<Player> playerList, int index) {
		datas.getGame().setPlayerToTheChair(playerList, index);
	}

	public void setPlayerNumber(int number) {
		datas.getGame().setPlayerNumber(number);
	}

	public void setSekiMap(Map<Player, Integer> sekiMap) {
		datas.getGame().setSekiMap(sekiMap);
	}

	public Map<Player, Integer> getSekiMap() {
		return datas.getGame().getSekiMap();
	}

	public void setHonba(int honba) {
		datas.getGame().setHonba(honba);
	}

	public void setTsumiBou(int tsumibou) {
		datas.getGame().setTsumiBou(tsumibou);
	}

	public int getPlayerNumber() {
		return datas.getGame().getPlayers().length;
	}

	public void setNumber(int number) {
		datas.getGame().setNumber(number);
	}

	public void setKyokusu(int kyokusu) {
		datas.getGame().setKyokusu(kyokusu);
	}

	public void setBakaze(Kaze bakaze) {
		datas.getGame().setBakaze(bakaze);
	}

	public Map<Integer, List<Mentsu>> getHurohaiMap() {
		return datas.getGame().getHurohaiMap();
	}

	public void setYamaSize(int yamaSize) {
		datas.getGame().setYamaSize(yamaSize);
	}

	public void setWanpaiSize(int wanpaiSize) {
		datas.getGame().setWanpaiSize(wanpaiSize);
	}

	public void setDoraList(List<Hai> doraList) {
		datas.getGame().setDoraList(doraList);
	}

	public void setResult(KyokuResult result, int[] newScores, int[] oldScores,
			List<Integer> changeScore, List<Hai> uradoraList) {
		// TODO
	}

	public Map<Integer, Integer> getTehaiSizeMap() {
		return datas.getGame().getTehaiSizeMap();
	}

	public void setScore(int index, int score) {
		datas.getGame().setScore(index, score);
	}

	// TODO to be removed
	public ClientOperator getOperator() {
		return operator;
	}
	public void onTempaiReceived(Map<Player,List<Hai>> map){
		List<List<Hai>> tmp = new ArrayList<List<Hai>>();
		for(int i = 0;i < 4;i++){
			tmp.add(map.get(datas.getGame().getPlayers()[i]));
		}
		datas.getResult().setTehais(tmp);
	}

	public void connectServer() {
		operator.connectServer();
		operator.setBackground(this);
		operator.requestGame(22);
		setMode(PackName.Game);
	}
	
	public void requestNextKyoku(){
		operator.requestNextKyoku();
	}
}	
