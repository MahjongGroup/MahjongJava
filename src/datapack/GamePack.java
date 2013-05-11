package datapack;

import static client.Constant.BUTTON_CURVE;
import static client.Constant.BUTTON_HEIGHT;
import static client.Constant.BUTTON_WIDTH;
import static client.Constant.HAI_HEIGHT;
import static client.Constant.HAI_WIDTH;
import static client.Constant.PLAYER_BLOCK1_X;
import static client.Constant.PLAYER_BLOCK1_Y;
import static client.Constant.PLAYER_BLOCK2_X;
import static client.Constant.PLAYER_BLOCK2_Y;
import static client.Constant.SCALED_HAI_HEIGHT;
import static client.Constant.SCALED_HAI_WIDTH;
import static client.Constant.TEHAI_INDENT;
import static client.Constant.WINDOW_HEIGHT;
import static client.Constant.WINDOW_WIDTH;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import system.hai.Hai;
import system.hai.Kaze;
import system.hai.MajanHai;
import system.hai.Mentsu;
import system.hai.Mentsu.MentsuHai;
import system.test.Player;
import client.BackgroundSystemOfClient;
import client.ClientPlayer;
import client.StateCode;

public class GamePack extends DataPack {
	
	private class ListenerForGame extends CommunicatableListener {
		public void chooseHai(java.awt.event.MouseEvent e) {
			int mx = e.getX();
			int my = e.getY();
			if (isWait() || animationCount > -1) {
				return;
			}
			if (isSelectTime()) {
				StateCode sc = null;

				// ここから試運用
				boolean scopeSkip = false;
				// ここまで試運用

				int buttonIndex = -1;
				if ((buttonIndex = isInButton(mx, my)) != -1) {
					oneGameData.getStateCodes()
							.remove(StateCode.DISCARD_SELECT);
					sc = getButton(buttonIndex);
				} else if (oneGameData.getButtonList().size() == 1
						&& isInSelectableHai(mx, my)) {
					sc = getButton(0);
				} else {
					for (StateCode subSc : oneGameData.getButtonList()) {
						dispatch(subSc);
					}
					if (oneGameData.getStateCodes().contains(
							StateCode.DISCARD_SELECT)) {
						refreshStateCodes();
						oneGameData.getStateCodes().add(
								StateCode.DISCARD_SELECT);
					} else {
						refreshStateCodes();
					}
					refreshNakiListExclude(null);
					refreshButtonList();
					scopeSkip = true;
				}
				if (!scopeSkip) {
					refreshButtonList();
					refreshStateCodes();
					addStateCode(sc);
					refreshNakiListExclude(sc);
					BackgroundSystemOfClient operator = getBackground();
					switch (sc) {
					case SELECT_RON:
						operator.sendRon(true);
						refreshStateCodes();
						refreshButtonList();
						return;
					case KYUSYUKYUHAI:
						operator.sendKyusyukyuhai(true);
						refreshStateCodes();
						refreshButtonList();
						return;
					case SELECT_TSUMO:
						operator.sendTsumoAgari();
						refreshStateCodes();
						refreshButtonList();
						return;
					case SELECT_MINKAN:
						operator.sendMinkan(true);
						refreshStateCodes();
						refreshButtonList();
						refreshNakiListExclude(null);
						return;
					default:
						break;
					}
				}
			}
			for (StateCode sc : oneGameData.getStateCodes()) {
				if (sc == StateCode.WAIT)
					return;
				if (oneGameData.getAbleIndexList().containsKey(sc)
						&& oneGameData.getAbleIndexList().get(sc).size() == 1) {
					oneGameData.setSelectedIndexes(oneGameData
							.getAbleIndexList().get(sc).get(0));
					dispatch(sc);
					oneGameData.getSelectedIndexes().clear();
					refreshNakiListExclude(null);
					refreshStateCodes();
					return;
				}
				List<Integer> selectedIndexes = oneGameData
						.getSelectedIndexes();
				int size = selectedIndexes.size();
				int i = selectedIndexes.isEmpty() ? -1 : selectedIndexes.get(0);
				int j = -1;
				if (sc == StateCode.DISCARD_SELECT) {
					j = addSelectedIndexes(mx, my, sc.getNum(),
							new ArrayList<List<Integer>>());
				} else {
					j = addSelectedIndexes(mx, my, sc.getNum(), oneGameData
							.getAbleIndexList().get(sc));
				}
				boolean flag = false;
				if (sc.getNum() != 1) {
					if (size == sc.getNum() && j == -1) {
						addSelectedIndexes(mx, my, sc.getNum(), oneGameData
								.getAbleIndexList().get(sc));
					}
					if (selectedIndexes.size() == sc.getNum()) {
						flag = true;
					}
				}
				if (flag || (sc.getNum() == 1 && i == j && j != -1)) {
					dispatch(sc);
					selectedIndexes.clear();
					refreshButtonList();
					refreshStateCodes();
					refreshNakiListExclude(null);
				}
				return;
			}
		}

		private void dispatch(StateCode sc) {
			BackgroundSystemOfClient background = getBackground();
			List<Integer> selectedHais = oneGameData.getSelectedIndexes();
			switch (sc) {
			case SELECT_PON_HAI:
				background.sendPonIndexList(selectedHais);
				break;
			case DISCARD_SELECT:
				background.sendDiscardIndex(selectedHais.get(0));
				break;
			case SELECT_CHI_HAI:
				background.sendChiIndexList(selectedHais);
				break;
			case SELECT_ANKAN_HAI:
				background.sendAnkanIndexList(selectedHais);
				break;
			case SELECT_KAKAN_HAI:
				background.sendKakanIndex(selectedHais.get(0));
				break;
			case SELECT_REACH_HAI:
				background.sendReachIndex(selectedHais.get(0));
				break;
			case SELECT_CHI:
				background.sendChiIndexList(null);
				break;
			case SELECT_PON:
				background.sendPonIndexList(null);
				break;
			case SELECT_MINKAN:
				background.sendMinkan(false);
				break;
			case SELECT_ANKAN:
				background.sendAnkanIndexList(null);
				break;
			case SELECT_REACH:
				background.sendReachIndex(-1);
				break;
			case SELECT_KAKAN:
				background.sendKakanIndex(-1);
				break;
			case KYUSYUKYUHAI:
				background.sendKyusyukyuhai(false);
			case SELECT_RON:
				background.sendRon(false);
			default:
				break;
			}
			refreshStateCodes();
		}

		// クリックされた時の処理
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO クリック実装
			System.out.println("clicked");
			System.out.println("print state-codes:");
			for(StateCode sc:oneGameData.getStateCodes()){
				System.out.println(sc);
			}
			chooseHai(e);
		}

	}
	private class OneGameData {
		private List<Hai> tehai;
		private Map<StateCode, List<List<Integer>>> ableIndexList;
		private Kaze bakaze;
		private List<StateCode> buttonList;
		private int currentTurn;
		private List<Hai> doraList;
		// 棺をしない場合最後の山牌がどの場所にあるか
		private int finish;
		private int honba;
		private Map<Integer, List<Mentsu>> hurohaiMap;
		private Map<Kaze, Integer> kaze;
		private int kyokusu;

		private int nakiPlayer;
		private Map<Integer, Integer> reachPosMap;

		private Map<Integer, Integer> scoreMap;
		private List<Integer> selectedIndexes;
		private EnumSet<StateCode> stateCodes;
		private Hai sutehai;
		private Map<Integer, List<Hai>> sutehaiMap;
		private Map<Integer, Integer> tehaiSizeMap;
		private int tsumiBou;
		private Hai tsumoHai;
		private int wanpaiSize;

		private int yamaSize;

		{
			tehai = new ArrayList<Hai>();
			ableIndexList = new HashMap<StateCode, List<List<Integer>>>();
			for (StateCode sc : StateCode.values()) {
				ableIndexList.put(sc, new ArrayList<List<Integer>>());
			}
			kyokusu = 1;
			sutehaiMap = new HashMap<Integer, List<Hai>>();
			tehaiSizeMap = new HashMap<Integer, Integer>();
			reachPosMap = new HashMap<Integer, Integer>();
			scoreMap = new HashMap<Integer, Integer>();
			hurohaiMap = new HashMap<Integer, List<Mentsu>>();
			for (int i = 0; i < 4; i++) {
				sutehaiMap.put(i, new ArrayList<Hai>());
				tehaiSizeMap.put(i, 13);
				reachPosMap.put(i, null);
				scoreMap.put(i, null);
				hurohaiMap.put(i, new ArrayList<Mentsu>());
			}
			selectedIndexes = new ArrayList<Integer>();
			kaze = new HashMap<Kaze, Integer>();
			for(int j = 0;j < 4;j++){
				kaze.put(Kaze.valueOf(j), j);
			}
			doraList = new ArrayList<Hai>();
			this.stateCodes = EnumSet.of(StateCode.WAIT);
			this.buttonList = new ArrayList<StateCode>();
			setListener(new ListenerForGame());

			// this.testOperator = new Test(this);

		}

		private void beforeSetData() {
			callPack();
		}

		public Map<StateCode, List<List<Integer>>> getAbleIndexList() {
			return ableIndexList;
		}

		public Kaze getBakaze() {
			return bakaze;
		}

		public List<StateCode> getButtonList() {
			return buttonList;
		}

		public int getCurrentTurn() {
			return currentTurn;
		}

		public List<Hai> getDoraList() {
			return doraList;
		}

		public int getFinish() {
			return finish;
		}

		public int getHonba() {
			return honba;
		}

		public Map<Integer, List<Mentsu>> getHurohaiMap() {
			return hurohaiMap;
		}

		public Map<Kaze, Integer> getKaze() {
			return kaze;
		}

		public int getKyokusu() {
			return kyokusu;
		}

		public int getNakiPlayer() {
			return nakiPlayer;
		}

		public Map<Integer, Integer> getReachPosMap() {
			return reachPosMap;
		}

		public Map<Integer, Integer> getScoreMap() {
			return scoreMap;
		}

		public List<Integer> getSelectedIndexes() {
			return selectedIndexes;
		}

		public EnumSet<StateCode> getStateCodes() {
			return stateCodes;
		}

		public Hai getSutehai() {
			return sutehai;
		}

		public Map<Integer, List<Hai>> getSutehaiMap() {
			return sutehaiMap;
		}

		public List<Hai> getTehai() {
			return tehai;
		}

		public Map<Integer, Integer> getTehaiSizeMap() {
			return tehaiSizeMap;
		}

		public int getTsumiBou() {
			return tsumiBou;
		}

		public Hai getTsumoHai() {
			return tsumoHai;
		}

		public int getWanpaiSize() {
			return wanpaiSize;
		}

		public int getYamaSize() {
			return yamaSize;
		}

		public void setAbleIndexList(
				Map<StateCode, List<List<Integer>>> ableIndexList) {
			callPack();
			this.ableIndexList = ableIndexList;
		}

		public void setBakaze(Kaze bakaze) {
			callPack();
			this.bakaze = bakaze;
		}

		public void setButtonList(List<StateCode> buttonList) {
			callPack();
			this.buttonList = buttonList;
		}

		public void setCurrentTurn(int currentTurn) {
			callPack();
			this.currentTurn = currentTurn;
		}

		public void setDoraList(List<Hai> doraList) {
			callPack();
			beforeSetData();
			this.doraList = doraList;
		}

		public void setFinish(int finish) {
			callPack();
			this.finish = finish;
		}

		public void setHonba(int honba) {
			callPack();
			this.honba = honba;
		}

		public void setHurohaiMap(Map<Integer, List<Mentsu>> hurohaiMap) {
			callPack();
			beforeSetData();
			this.hurohaiMap = hurohaiMap;
		}

		public void setKaze(Map<Kaze, Integer> kaze) {
			callPack();
			this.kaze = kaze;
		}

		public void setKyokusu(int kyokusu) {
			callPack();
			this.kyokusu = kyokusu;
		}

		public void setNakiPlayer(int nakiPlayer) {
			callPack();
			this.nakiPlayer = nakiPlayer;
		}

		public void setReachPosMap(Map<Integer, Integer> reachPosMap) {
			callPack();
			beforeSetData();
			this.reachPosMap = reachPosMap;
		}

		public void setScoreMap(Map<Integer, Integer> scoreMap) {
			callPack();
			this.scoreMap = scoreMap;
		}

		public void setSelectedIndexes(List<Integer> selectedIndexes) {
			callPack();
			this.selectedIndexes = selectedIndexes;
		}

		public void setStateCodes(EnumSet<StateCode> stateCodes) {
			callPack();
			this.stateCodes = stateCodes;
		}

		public void setSutehai(Hai sutehai) {
			callPack();
			beforeSetData();
			this.sutehai = sutehai;
		}

		public void setSutehaiMap(Map<Integer, List<Hai>> sutehaiMap) {
			callPack();
			beforeSetData();
			this.sutehaiMap = sutehaiMap;
		}

		public void setTehai(List<Hai> tehai) {
			callPack();
			this.tehai = tehai;
		}

		public void setTehaiSizeMap(Map<Integer, Integer> tehaiSizeMap) {
			callPack();
			beforeSetData();
			this.tehaiSizeMap = tehaiSizeMap;
		}

		public void setTsumiBou(int tsumiBou) {
			callPack();
			this.tsumiBou = tsumiBou;
		}

		public void setTsumoHai(Hai tsumoHai) {
			callPack();
			beforeSetData();
			this.tsumoHai = tsumoHai;
		}

		public void setWanpaiSize(int wanpaiSize) {
			callPack();
			this.wanpaiSize = wanpaiSize;
		}

		public void setYamaSize(int yamaSize) {
			callPack();
			beforeSetData();
			this.yamaSize = yamaSize;
		}

	}
	// 結合テスト
	private static int objSize = 0;
	private static OneGameData oneGameData;
	private static ImageData imageData;
	private static ClientPlayer[] players;

	private static int parentChair;

	public static OneGameData getOneGameData() {
		return oneGameData;
	}

	public static ImageData getWholeGameData() {
		return imageData;
	}

	public static void setOneGameData(OneGameData oneGameData) {
		GamePack.oneGameData = oneGameData;
	}

	private Graphics g;

	// 未使用
	private int animationCount;

	private StateCode animeState;

	static {
		imageData = ImageData.getInstance();
		players = new ClientPlayer[4];
	}

	public static int getObjSize() {
		return objSize;
	}

	public static void setObjSize(int objSize) {
		GamePack.objSize = objSize;
	}

	public static void setWholeGameData(ImageData wholeGameData) {
		GamePack.imageData = wholeGameData;
	}

	int number;

	{
		animationCount = -1;
		// 結合テスト
		this.number = -1;
	}

	public void addButtonList(StateCode sc) {
		callPack();
		oneGameData.getButtonList().add(sc);
	}

	private int addSelectedIndexes(int mx, int my, int max,
			List<List<Integer>> rule) {
		callPack();
		List<Hai> tehai = oneGameData.getTehai();
		List<Integer> selectedIndexes;
		selectedIndexes = oneGameData.getSelectedIndexes();
		int i;
		for (i = 0; i < tehai.size(); i++) {
			if (PLAYER_BLOCK1_X + HAI_WIDTH * i <= mx
					&& mx <= PLAYER_BLOCK1_X + HAI_WIDTH * (i + 1)) {
				int margin = 0;
				if (selectedIndexes.contains(i))
					margin -= 20;
				if (PLAYER_BLOCK1_Y + 270 + margin <= my
						&& my <= PLAYER_BLOCK1_Y + 270 + HAI_HEIGHT) {
					if (selectedIndexes.contains(i)) {
						if (max != 1) {
							selectedIndexes.remove((Integer) i);
							i = -1;
						}
					} else {
						if (rule != null)
							if (rule.size() == 0) {
								if (selectedIndexes.size() == max)
									selectedIndexes.remove(0);
								selectedIndexes.add(i);
							} else {
								for (List<Integer> l : rule) {
									if (l.containsAll(selectedIndexes)
											&& l.contains(i)) {
										if (selectedIndexes.size() == max)
											selectedIndexes.remove(0);
										selectedIndexes.add(i);
										break;
									}
								}
							}
					}
					break;
				}
			}
		}
		if (i >= tehai.size())
			if (oneGameData.getCurrentTurn() == 0) {
				int dx = tehai.size() * HAI_WIDTH + 20 + PLAYER_BLOCK1_X;
				if (mx <= dx + HAI_WIDTH && mx >= dx
						&& my <= PLAYER_BLOCK1_Y + 270 + HAI_HEIGHT
						&& my >= PLAYER_BLOCK1_Y + 270) {
					i = 13;
					if (selectedIndexes.contains(i)) {
						if (max != 1) {
							selectedIndexes.remove((Integer) i);
							i = -1;
						}
					} else {
						if (selectedIndexes.size() == max)
							selectedIndexes.remove(0);
						selectedIndexes.add(i);
					}
				}
			} else {
				return -1;
			}
		return i;
	}

	private void addSelectedIndexesWhenOverHai(int mx, int my, int max,
			List<List<Integer>> rule) {
		callPack();
		List<Hai> tehai = oneGameData.getTehai();
		List<Integer> selectedIndexes = oneGameData.getSelectedIndexes();
		if (PLAYER_BLOCK1_Y + 270 > my
				|| my > PLAYER_BLOCK1_Y + 270 + HAI_HEIGHT + 20) {
			selectedIndexes.clear();
			return;
		}
		for (int i = 0; i < tehai.size(); i++) {
			if (PLAYER_BLOCK1_X + HAI_WIDTH * i <= mx
					&& mx <= PLAYER_BLOCK1_X + HAI_WIDTH * (i + 1)) {
				int margin = 0;
				if (selectedIndexes.contains(i))
					margin -= 20;
				if (PLAYER_BLOCK1_Y + 270 + margin <= my
						&& my <= PLAYER_BLOCK1_Y + 270 + HAI_HEIGHT) {
					if (!selectedIndexes.contains(i)) {
						int count = 0;
						List<Integer> tmpSubList = new ArrayList<Integer>();
						if (selectedIndexes.size() < max
								&& selectedIndexes.size() > 0)
							return;
						selectedIndexes = new ArrayList<Integer>();
						if (rule != null)
							for (List<Integer> subList : rule) {
								if (subList.contains(i)) {
									count++;
									tmpSubList = subList;
									if (count > 1)
										break;
								}
							}
						if (count == 1)
							selectedIndexes.addAll(tmpSubList);
						oneGameData.setSelectedIndexes(selectedIndexes);
					}
				}
				return;
			}
		}
		int dx = tehai.size() * HAI_WIDTH + 20 + PLAYER_BLOCK1_X;
		int margin = 0;
		if (selectedIndexes.contains(13))
			margin -= 20;
		if (mx <= dx + HAI_WIDTH && mx >= dx
				&& my <= PLAYER_BLOCK1_Y + 270 + HAI_HEIGHT
				&& my >= PLAYER_BLOCK1_Y + 270 + margin) {
			if (!selectedIndexes.contains(13)) {
				int count = 0;
				List<Integer> tmpSubList = new ArrayList<Integer>();
				if (selectedIndexes.size() < max && selectedIndexes.size() > 0)
					return;
				selectedIndexes = new ArrayList<Integer>();
				if (rule != null)
					for (List<Integer> subList : rule) {
						if (subList.contains(13)) {
							count++;
							tmpSubList = subList;
							if (count > 1)
								break;
						}
					}
				if (count == 1)
					selectedIndexes.addAll(tmpSubList);
				oneGameData.setSelectedIndexes(selectedIndexes);
			}
			return;
		}
	}

	public void addStateCode(StateCode stateCode) {
		callPack();
		EnumSet<StateCode> stateCodes = oneGameData.getStateCodes();
		while (stateCodes.contains(StateCode.WAIT))
			stateCodes.remove(StateCode.WAIT);
		if (stateCode != null) {
			stateCodes.add(stateCode);
		} else if (stateCodes.size() == 0) {
			stateCodes.add(StateCode.WAIT);
		}
	}

	/**
	 * 親流れの時に呼び出すメソッド プレイヤーと風の対応関係/親を変更する
	 */
	public void changeParent() {
		callPack();
		parentChair = (parentChair + 1) % 4;
		for (int i = 0; i < 4; i++) {
			players[i].setKaze(Kaze
					.valueOf((players[i].getKaze().id() + 3) % 4));
		}
	}

	@Override
	public void createImage() throws NullPointerException {
		// if (number == -1)
		// return;
		// Image imgBuffer = getImage();
		// Graphics gg = getImage().getGraphics();
		// TODO
		if (g == null)
			g = getImage().getGraphics();
		// TODO Auto-generated method stub
		Graphics2D g2 = (Graphics2D) g;
		
		System.out.println("TON:" + getKaze().get(Kaze.TON));

		g2.clearRect(0, 0, getWidth(), getHeight());

		g2.setColor(Color.BLACK);
		g2.setFont(new Font("", Font.BOLD, 20));
		g2.drawString(
				oneGameData.getBakaze().notation() + " "
						+ oneGameData.getKyokusu() + "局", getWidth() / 2 - 50,
				getHeight() / 2 - 10 - 20);
		g2.drawString(oneGameData.getHonba() + "本場", getWidth() / 2 - 50,
				getHeight() / 2 + 10 - 20);
		g2.drawString("積み棒:" + oneGameData.getTsumiBou(), getWidth() / 2 - 50,
				getHeight() / 2 + 30 - 20);

		drawHai(0, PLAYER_BLOCK1_X, PLAYER_BLOCK1_Y, 170, g2);
		drawSuteHai(0, PLAYER_BLOCK1_X, PLAYER_BLOCK1_Y, 170, g2);
		drawJihu(0, PLAYER_BLOCK1_X + 270, PLAYER_BLOCK1_Y + 30, g2);
		if (oneGameData.getReachPosMap().get(0) != null)
			g2.drawImage(imageData.getReachImage(), PLAYER_BLOCK1_X + 200,
					PLAYER_BLOCK1_Y - 30, null);
		g2.rotate(-Math.PI);
		g2.translate(-WINDOW_WIDTH, -WINDOW_HEIGHT);
		drawHai(2, PLAYER_BLOCK1_X, PLAYER_BLOCK1_Y, 170, g2);
		drawSuteHai(2, PLAYER_BLOCK1_X, PLAYER_BLOCK1_Y, 170, g2);
		drawJihu(2, PLAYER_BLOCK1_X + 290, PLAYER_BLOCK1_Y + 60, g2);
		if (oneGameData.getReachPosMap().get(2) != null)
			g2.drawImage(imageData.getReachImage(), PLAYER_BLOCK1_X + 200,
					PLAYER_BLOCK1_Y, null);
		g2.rotate(Math.PI / 2.0);
		g2.translate(-100, -WINDOW_WIDTH + 50);
		g2.rotate(-Math.PI / 20.0);

		drawHai(1, PLAYER_BLOCK2_X, PLAYER_BLOCK2_Y, 250, g2);

		g2.rotate(Math.PI / 20.0);
		g2.translate(100, -50);

		drawSuteHai(1, PLAYER_BLOCK2_X, PLAYER_BLOCK2_Y, 250, g2);
		drawJihu(1, PLAYER_BLOCK2_X + 300, PLAYER_BLOCK2_Y - 80, g2);
		if (oneGameData.getReachPosMap().get(1) != null)
			g2.drawImage(imageData.getReachImage(), PLAYER_BLOCK2_X + 200,
					PLAYER_BLOCK2_Y - 100, null);

		g2.rotate(Math.PI);
		g2.translate(-WINDOW_HEIGHT + 100, -WINDOW_WIDTH - 80);

		g2.rotate(Math.PI / 15.0);

		drawHai(3, PLAYER_BLOCK2_X, PLAYER_BLOCK2_Y, 250, g2);

		g2.rotate(-Math.PI / 15.0);
		g2.translate(-100, 80);

		drawSuteHai(3, PLAYER_BLOCK2_X, PLAYER_BLOCK2_Y, 1250, g2);
		drawJihu(3, PLAYER_BLOCK2_X + 260, PLAYER_BLOCK2_Y - 60, g2);
		if (oneGameData.getReachPosMap().get(3) != null)
			g2.drawImage(imageData.getReachImage(), PLAYER_BLOCK2_X + 200,
					PLAYER_BLOCK2_Y - 100, null);

		g2.rotate(-Math.PI / 2.0);
		g2.translate(-WINDOW_WIDTH, 0);

		drawPartOfYama(g2, 0, 17, PLAYER_BLOCK1_X, PLAYER_BLOCK1_Y);

		g2.rotate(-Math.PI);
		g2.translate(-WINDOW_WIDTH, -WINDOW_HEIGHT);

		drawPartOfYama(g2, 34, 51, PLAYER_BLOCK1_X, PLAYER_BLOCK1_Y);

		g2.rotate(Math.PI / 2.0);
		g2.translate(-100, -WINDOW_WIDTH + 50);
		g2.rotate(-Math.PI / 20.0);

		drawPartOfYama(g2, 17, 34, PLAYER_BLOCK2_X, PLAYER_BLOCK2_Y);

		g2.rotate(Math.PI / 20.0);
		g2.translate(100, -50);
		g2.rotate(Math.PI);
		g2.translate(-WINDOW_HEIGHT + 150, -WINDOW_WIDTH - 100);
		g2.rotate(Math.PI / 25.0);

		drawPartOfYama(g2, 51, 68, PLAYER_BLOCK2_X, PLAYER_BLOCK2_Y);

		g2.rotate(-Math.PI / 25.0);
		g2.translate(-150, 100);
		g2.rotate(-Math.PI / 2.0);
		g2.translate(-WINDOW_WIDTH, 0);

		if (oneGameData.getStateCodes().contains(StateCode.END)) {
			g2.setColor(Color.RED);
			g2.drawString("END", getWidth() / 2, getHeight() / 2);
		}
		if (oneGameData.getStateCodes().contains(StateCode.SELECT_BUTTON)) {
			int size = oneGameData.getButtonList().size();
			int width = getWidth() / 2;
			int height = PLAYER_BLOCK1_Y + 260;
			int half = size / 2;
			for (int i = 0; i < size; i++) {
				g2.setColor(Color.orange);
				int tmpX = width - BUTTON_WIDTH * (half - i) - 10 * (half - i);
				int tmpY = height - BUTTON_HEIGHT / 2 - 10;
				g2.fillRoundRect(tmpX, tmpY, BUTTON_WIDTH, BUTTON_HEIGHT,
						BUTTON_CURVE, BUTTON_CURVE);
				g2.setColor(Color.BLACK);
				g2.setFont(new Font("", Font.BOLD, 15));

				g2.drawString(oneGameData.getButtonList().get(i)
						.getButtonName(), tmpX + BUTTON_WIDTH / 2, tmpY
						+ BUTTON_HEIGHT / 2);
			}
		}
		if (animationCount > -1) {
			int x = (getWidth() - BUTTON_WIDTH) / 2;
			int y = (getHeight() - BUTTON_HEIGHT) / 2;
			g2.setColor(Color.RED);
			g2.fillOval(x, y, BUTTON_WIDTH * 3 / 2, BUTTON_HEIGHT * 3 / 2);
			switch (oneGameData.getNakiPlayer()) {
			case -1:
				break;
			case 0:
				g2.fillPolygon(new Polygon(
						new int[] { x + 100, x + 70, x + 40 }, new int[] {
								y + 20, y + 200, y + 20 }, 3));
				break;
			case 1:
				g2.fillPolygon(new Polygon(new int[] { x + 100, x + 300,
						x + 100 }, new int[] { y + 20, y + 40, y + 60 }, 3));
				break;
			case 2:
				g2.fillPolygon(new Polygon(
						new int[] { x + 100, x + 70, x + 40 }, new int[] {
								y + 20, y - 200, y + 20 }, 3));
				break;
			case 3:
				g2.fillPolygon(new Polygon(new int[] { x + 100, x - 200,
						x + 100 }, new int[] { y + 20, y + 40, y + 60 }, 3));
				break;
			default:
				break;
			}
			g2.setColor(Color.BLACK);
			g2.drawString(animeState.getButtonName() + "!", x + BUTTON_WIDTH
					/ 2 + 20, y + BUTTON_HEIGHT / 2 + 20);
			if (animationCount > 100) {
				animationCount = -100;
				oneGameData.getStateCodes().remove(StateCode.DRAW_ANIME);
			}
			animationCount++;
		}
	}

	private void drawColoredFrame(Graphics2D g2, int index, int x, int y) {
		Map<StateCode, List<List<Integer>>> ableIndexMap = oneGameData
				.getAbleIndexList();
		EnumSet<StateCode> stateCodes = oneGameData.getStateCodes();
		for (StateCode sc : ableIndexMap.keySet()) {
			if (stateCodes.contains(StateCode.SELECT_BUTTON)
					|| stateCodes.contains(sc))
				drawFrameBasedOnRule(g2, index, x, y, ableIndexMap.get(sc));
		}
	}

	private void drawFrameBasedOnRule(Graphics2D g2, int index, int x, int y,
			List<List<Integer>> rule) {
		List<Integer> selectedIndexes = oneGameData.getSelectedIndexes();
		g2.setColor(Color.RED);
		int size = oneGameData.getSelectedIndexes().size();
		if (rule != null)
			for (List<Integer> l : rule) {
				if (l == null)
					continue;
				if ((size == 0 || l.containsAll(selectedIndexes))
						&& l.contains(index))
					g2.drawRoundRect(x, y, HAI_WIDTH, HAI_HEIGHT, BUTTON_CURVE,
							BUTTON_CURVE);
			}
	}

	public void drawHai(int player, int ix, int iy, int sute_x, Graphics2D g2) {
		int tehaiSize = 0;
		if (player != 0) {
			tehaiSize = oneGameData.getTehaiSizeMap().get(player);
		} else {
			tehaiSize = oneGameData.getTehai().size();
		}
		List<Integer> selectedIndexes = oneGameData.getSelectedIndexes();
		int dx = 0;
		for (int i = 0; i < tehaiSize; i++) {
			if (player != 0) {
				g2.drawImage(imageData.getScaledHaiBackImage(), ix + dx
						+ TEHAI_INDENT, iy + 270, SCALED_HAI_WIDTH,
						SCALED_HAI_HEIGHT, null);
				dx += SCALED_HAI_WIDTH;
			} else {
				Image image = imageData.getHaiImageMap().get(
						oneGameData.getTehai().get(i));
				int selectedMargin = selectedIndexes.contains((Integer) i) ? -20
						: 0;
				g2.drawImage(image, ix + dx, iy + 270 + selectedMargin,
						HAI_WIDTH, HAI_HEIGHT, null);
				drawColoredFrame(g2, i, ix + dx, iy + selectedMargin + 270);
				dx += HAI_WIDTH;
			}
		}
		if (oneGameData.getCurrentTurn() == player
				&& oneGameData.getTsumoHai() != null) {
			if (player == 0) {
				dx += HAI_HEIGHT - HAI_WIDTH;
				int selectedMargin = selectedIndexes.contains(13) ? -20 : 0;
				g2.drawImage(
						imageData.getHaiImageMap().get(
								oneGameData.getTsumoHai()), ix + dx, iy + 270
								+ selectedMargin, HAI_WIDTH, HAI_HEIGHT, null);
				drawColoredFrame(g2, 13, ix + dx, iy + selectedMargin + 270);
			} else {
				dx += SCALED_HAI_HEIGHT - SCALED_HAI_WIDTH;
				g2.drawImage(imageData.getScaledHaiBackImage(), ix + dx
						+ TEHAI_INDENT, iy + 270, SCALED_HAI_WIDTH,
						SCALED_HAI_HEIGHT, null);
			}
		}

		List<Mentsu> hurohaiList = oneGameData.getHurohaiMap().get(player);
		int hurohaiListSize = hurohaiList.size();
		int screenWidth;
		int screenHeight;
		if (player % 2 == 0) {
			screenWidth = WINDOW_WIDTH;
			screenHeight = WINDOW_HEIGHT;
		} else {
			screenWidth = WINDOW_HEIGHT;
			screenHeight = WINDOW_HEIGHT;
		}
		int dy = iy + 270;
		// dx = screenWidth - HAI_HEIGHT * 2;
		for (int i = 0; i < hurohaiListSize; i++) {
			Mentsu Mentsu = hurohaiList.get(i);
			int MentsuSize = Mentsu.isKakan() ? Mentsu.size() - 1 : Mentsu.size();
			if (MentsuSize == 0)
				continue;
			// 右から順番に格納
			MentsuHai[] hurohaiArray = new MentsuHai[Mentsu.isKakan() ? MentsuSize + 1
					: MentsuSize];
			dx = screenWidth - SCALED_HAI_HEIGHT * 2;
			if (Mentsu.isNaki()) {
				int fromKaze = (oneGameData.getKaze().get(
						hurohaiList.get(i).getKaze()) + 4 - player) % 4;
				fromKaze = (fromKaze - 1) * (fromKaze - 2) / 2
						* (MentsuSize - 1) - (fromKaze - 1) * (fromKaze - 3);
				int j = 0;
				for (MentsuHai mh : Mentsu.asList()) {
					if (mh.isNaki()) {
						hurohaiArray[fromKaze] = mh;
						continue;
					}
					if (hurohaiArray[j] != null || j == fromKaze) {
						j++;
					}
					hurohaiArray[j++] = mh;
				}
				for (j = 0; j < MentsuSize; j++) {
					Hai tmpHai = MajanHai.valueOf(hurohaiArray[j].type(),
							hurohaiArray[j].aka());
					Image image = imageData.getScaledHaiImageMap().get(tmpHai);
					// TODO
					if (j == fromKaze) {
						g2.rotate(Math.PI / 2.0);
						g2.translate(0, -screenHeight);
						g2.drawImage(image, dy
								+ (SCALED_HAI_HEIGHT - SCALED_HAI_WIDTH),
								screenHeight - dx - SCALED_HAI_WIDTH,
								SCALED_HAI_WIDTH, SCALED_HAI_HEIGHT, null);
						if (Mentsu.isKakan()) {
							tmpHai = MajanHai.valueOf(hurohaiArray[3].type(),
									hurohaiArray[3].aka());
							image = imageData.getHaiImageMap().get(tmpHai);
							g2.drawImage(
									image,
									dy
											+ (SCALED_HAI_HEIGHT - SCALED_HAI_WIDTH * 2),
									screenHeight - dx - SCALED_HAI_WIDTH,
									SCALED_HAI_WIDTH, SCALED_HAI_HEIGHT, null);
						}
						g2.translate(0, screenHeight);
						g2.rotate(-Math.PI / 2.0);
						dx -= SCALED_HAI_HEIGHT;
					} else {
						g2.drawImage(image, dx, dy, SCALED_HAI_WIDTH,
								SCALED_HAI_HEIGHT, null);
						dx -= SCALED_HAI_WIDTH;
					}
				}
			} else {
				int count = 0;
				for (MentsuHai mh : Mentsu.asList()) {
					Hai tmpHai = MajanHai.valueOf(mh.type(), mh.aka());
					Image image;
					if (count == 0 || count == 3)
						image = imageData.getScaledHaiBackImage();
					else
						image = imageData.getScaledHaiImageMap().get(tmpHai);
					// TODO
					g2.drawImage(image, dx, dy, SCALED_HAI_WIDTH,
							SCALED_HAI_HEIGHT, null);
					dx -= SCALED_HAI_WIDTH;
					count++;
				}
			}
			dy -= SCALED_HAI_HEIGHT;
		}

	}

	public void drawJihu(int player, int ix, int iy, Graphics2D g2) {
		Kaze tmp = null;
		g2.setColor(Color.WHITE);
		for (Kaze k : Kaze.values()) {
			if (oneGameData.getKaze().get(k) == player) {
				tmp = k;
				break;
			}
		}
		if (player % 2 != 1)
			g2.drawString(tmp.notation(), ix - 30, iy);
		else
			g2.drawString(tmp.notation(), ix + 60, iy);
		JPanel tmpPanel = new JPanel();
		tmpPanel.add(new JLabel(oneGameData.getScoreMap().get(player) + ""));
		g2.drawString(oneGameData.getScoreMap().get(player) + "", ix, iy);
		g2.setColor(Color.BLACK);
	}

	private void drawPartOfYama(Graphics g2, int start, int end, int ix, int iy) {
		int wanpaiSize = oneGameData.getWanpaiSize();
		int limit = oneGameData.getYamaSize() / 2 + oneGameData.getYamaSize()
				% 2 + oneGameData.getFinish() + 1;
		int finish = oneGameData.getFinish()
				+ 1
				- (wanpaiSize / 2 + wanpaiSize % 2 + (wanpaiSize == 14 ? 1 : 0));
		int indent_y = 200;
		int indent_x = 50;
		if (finish < 0) {
			finish += 68;
			limit += 68;
		}
		int dx = 0;
		int doraStart = finish + 3
				- (8 - (wanpaiSize + 1) % 2 - wanpaiSize == 14 ? 1 : 0);
		for (int i = start; i < end; i++) {
			if ((finish <= i && i < limit)
					|| (finish <= i + 68 && i + 68 < limit))
				if (((finish == i || finish == i + 68) && (wanpaiSize % 2 == 1 || wanpaiSize == 14))
						|| ((finish + 1 == i || finish + 1 == i + 68) && wanpaiSize == 14)
						|| ((limit - 1 == i || limit - 1 == i + 68) && oneGameData
								.getYamaSize() % 2 == 1))
					g2.drawImage(imageData.getScaledDarkHaiBackImage(), ix + dx
							+ indent_x, iy + indent_y, SCALED_HAI_WIDTH,
							SCALED_HAI_HEIGHT, null);
				else if ((doraStart <= i && doraStart
						+ oneGameData.getDoraList().size() > i)
						|| (doraStart <= i + 68 && doraStart
								+ oneGameData.getDoraList().size() > i + 68)) {
					g2.drawImage(
							imageData.getHaiImageMap().get(
									oneGameData.getDoraList().get(
											(i - (finish + 3) + 68) % 68)), ix
									+ dx + indent_x, iy + indent_y,
							SCALED_HAI_WIDTH, SCALED_HAI_HEIGHT, null);
					// TODO ドラ表示
				} else
					g2.drawImage(imageData.getScaledHaiBackImage(), ix + dx
							+ indent_x, iy + indent_y, SCALED_HAI_WIDTH,
							SCALED_HAI_HEIGHT, null);
			dx += SCALED_HAI_WIDTH;
			// TODO current
		}
	}

	public void drawSuteHai(int player, int ix, int iy, int sute_x,
			Graphics2D g2) {
		switch (player) {
		case 0:
			ix += 50;
			iy += 50;
			break;
		case 1:
			ix += 40;
			iy -= 20;
			break;
		case 2:
			ix += 30;
			iy += 80;
			break;
		case 3:
			ix += 40;
			iy -= 20;
			break;
		default:
			break;
		}
		List<Hai> suteHaiList = null;
		synchronized (oneGameData.getSutehaiMap()) {
			suteHaiList = oneGameData.getSutehaiMap().get(player);
		}
		int dy = 0;
		int screenHeight = WINDOW_HEIGHT;
		for (int j = 0; j < 4; j++) {
			int dx = 170 - SCALED_HAI_WIDTH;
			for (int i = 0; j * 6 + i < suteHaiList.size() && i < 6; i++) {
				Image image = imageData.getScaledHaiImageMap().get(
						suteHaiList.get(j * 6 + i));
				if (oneGameData.getReachPosMap().get(player) == null
						|| oneGameData.getReachPosMap().get(player) != j * 6
								+ i) {
					g2.drawImage(image, ix + SCALED_HAI_WIDTH + dx, iy + dy,
							SCALED_HAI_WIDTH, SCALED_HAI_HEIGHT, null);
					dx += SCALED_HAI_WIDTH;
				} else {
					g2.rotate(Math.PI / 2.0);
					g2.translate(0, -screenHeight);
					g2.drawImage(image, iy + dy, screenHeight - dx
							- SCALED_HAI_HEIGHT - SCALED_HAI_WIDTH - ix,
							SCALED_HAI_WIDTH, SCALED_HAI_HEIGHT, null);
					g2.translate(0, screenHeight);
					g2.rotate(-Math.PI / 2.0);
					dx += SCALED_HAI_HEIGHT;
				}
			}
			dy += SCALED_HAI_HEIGHT;
		}
	}

	public Map<StateCode, List<List<Integer>>> getAbleIndexList() {
		return getOneGameData().getAbleIndexList();
	}

	public int getAnimationCount() {
		return animationCount;
	}

	public StateCode getAnimeState() {
		return animeState;
	}

	private StateCode getButton(int buttonIndex) {
		return getSelectHaiFromSelectButton(oneGameData.getButtonList().get(
				buttonIndex));
	}

	public Map<Integer, List<Mentsu>> getHurohaiMap() {
		return getOneGameData().getHurohaiMap();
	}

	/**
	 * あるプレイヤーを識別するためにクライアント側で使っている添字からサーバ側で使っている添字を計算する
	 * 
	 * @param i
	 *            クライアント側の添字
	 * @return サーバ側で使っている添字
	 */
	private int getIndexInServerFromIndexInClient(int i) {
		return (4 + i - parentChair) % 4;
	}

	public Map<Kaze, Integer> getKaze() {
		return getOneGameData().getKaze();
	}

	public int getNumber() {
		return number;
	}

	public Player getPlayerFromClientVer(ClientPlayer cp) {
		if (cp == null)
			return new Player(1, "unknown", false);
		return new Player(cp.getId(), cp.getName(), true);
	}

	public ClientPlayer[] getPlayers() {
		return players;
	}

	public Map<Integer, Integer> getReachPosMap() {
		return oneGameData.getReachPosMap();
	}

	public Map<Player, Integer> getSekiMap() {
		Map<Player,Integer> tmpMap = new HashMap<Player,Integer>();
		for(ClientPlayer cp:players){
			tmpMap.put(cp.getPlayer(), cp.getIndex());
		}
		return tmpMap;
	}

	/**
	 * ボタン選択状態から牌選択状態を得るメソッド 例)input:SELECT_CHI -> output:SELECT_CHI_HAI
	 * 
	 * @param sc
	 *            input
	 * @return output
	 */
	public StateCode getSelectHaiFromSelectButton(StateCode sc) {
		switch (sc) {
		case SELECT_CHI:
			return StateCode.SELECT_CHI_HAI;
		case SELECT_PON:
			return StateCode.SELECT_PON_HAI;
		case SELECT_ANKAN:
			return StateCode.SELECT_ANKAN_HAI;
		case SELECT_REACH:
			return StateCode.SELECT_REACH_HAI;
		case SELECT_KAKAN:
			return StateCode.SELECT_KAKAN_HAI;
		default:
			return sc;
		}
	}

	public Map<Integer, List<Hai>> getSutehaiMap() {
		return getOneGameData().getSutehaiMap();
	}

	public Map<Integer, Integer> getTehaiSizeMap() {
		return getOneGameData().getTehaiSizeMap();
	}

	private int isInButton(int mx, int my) {
		int width = getWidth() / 2;
		int height = PLAYER_BLOCK1_Y + 260;
		if (my <= height + BUTTON_HEIGHT / 2 - 10
				&& my >= height - BUTTON_HEIGHT / 2 - 10) {
			int half = oneGameData.getButtonList().size() / 2;
			// クリック個所がボタンのある範囲にあるかどうか(x方向)
			for (int j = 0; j < oneGameData.getButtonList().size(); j++) {
				if (mx <= width - BUTTON_WIDTH * (half - 1 - j) - 10
						* (half - j)
						&& mx >= width - BUTTON_WIDTH * (half - j) - 10
								* (half - j)) {
					return j;
				}
			}
		}
		return -1;
	}


	private boolean isInSelectableHai(int mx, int my) {
		if (!(PLAYER_BLOCK1_Y + 270 <= my && my <= PLAYER_BLOCK1_Y + 270
				+ HAI_HEIGHT))
			return false;
		StateCode sc = getSelectHaiFromSelectButton(oneGameData.getButtonList()
				.get(0));

		// ここから試運用

		List<StateCode> myTurnsActions = new ArrayList<StateCode>();
		myTurnsActions.add(StateCode.SELECT_REACH);
		myTurnsActions.add(StateCode.SELECT_TSUMO);
		myTurnsActions.add(StateCode.SELECT_KAKAN);

		if (myTurnsActions.contains(sc))
			return false;

		if (StateCode.SELECT_ANKAN == sc && isInTheHai(13, mx))
			return false;

		// ここまで試運用

		if (!oneGameData.getAbleIndexList().containsKey(sc))
			return false;

		List<List<Integer>> tmpList = oneGameData.getAbleIndexList().get(sc);
		if (tmpList != null) {
			for (List<Integer> tmpSubList : tmpList) {
				if (tmpSubList == null)
					break;
				for (Integer integer : tmpSubList) {
					if (isInTheHai(integer, mx)) {
						return true;
					}
				}
				if (tmpSubList.contains(13) && isInTheHai(13, mx)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isInTheHai(int number, int mx) {
		if (number == 13) {
			number = oneGameData.getTehai().size();
			mx -= 20;
		}
		if (mx <= PLAYER_BLOCK1_X + (number + 1) * HAI_WIDTH
				&& mx >= PLAYER_BLOCK1_X + number * HAI_WIDTH)
			return true;
		return false;
	}

	private boolean isSelectTime() {
		return oneGameData.getStateCodes().contains(StateCode.SELECT_BUTTON);
	}

	private boolean isWait() {
		return oneGameData.getStateCodes().contains(StateCode.WAIT);
	}

	public void mouseMoved(MouseEvent e) {
		EnumSet<StateCode> stateCodes = oneGameData.getStateCodes();
		if (stateCodes.contains(StateCode.WAIT))
			return;
		int mx = e.getPoint().x;
		int my = e.getPoint().y;
		for (StateCode sc : stateCodes) {
			addSelectedIndexesWhenOverHai(mx, my, sc.getNum(), oneGameData
					.getAbleIndexList().get(sc));
		}
	}

	private void moveKaze() {
		Map<Kaze, Integer> kaze = oneGameData.getKaze();
		for (Kaze k : kaze.keySet()) {
			kaze.put(k, (kaze.get(k) + 1) % 4);
			getPlayers()[(kaze.get(k) + 1) % 4].setKaze(k);
		}
		oneGameData.setFinish((oneGameData.getFinish() + oneGameData.getKaze()
				.get(Kaze.TON) * 17) % 68);
	}

	public void refreshButtonList() {
		callPack();
		oneGameData.getButtonList().clear();
	}

	private void refreshNakiListExclude(StateCode sc) {
		callPack();
		Map<StateCode, List<List<Integer>>> ableIndexList = oneGameData
				.getAbleIndexList();
		for (StateCode key : ableIndexList.keySet()) {
			if (key == StateCode.SELECT_MINKAN) {
				if (sc != StateCode.SELECT_MINKAN)
					ableIndexList.get(StateCode.SELECT_MINKAN).clear();
				continue;
			}
			if (sc == null)
				ableIndexList.get(key).clear();
			else {
				String tmpString = sc.name();
				if (!sc.name().contains("_HAI"))
					tmpString += "_HAI";
				if (key.name().compareTo(tmpString) != 0)
					ableIndexList.get(key).clear();
			}
		}

	}

	public void refreshStateCodes() {
		callPack();
		oneGameData.getStateCodes().clear();
		oneGameData.getStateCodes().add(StateCode.WAIT);
	}

	public void setAnimationCount(int animationCount) {
		this.animationCount = animationCount;
	}

	public void setAnimeState(StateCode animeState) {
		callPack();
		this.animeState = animeState;
	}

	public void setBakaze(Kaze kaze) {
		callPack();
		oneGameData.setBakaze(kaze);
	}

	public void setCurrentTurn(int currentTurn) {
		callPack();
		getOneGameData().setCurrentTurn(currentTurn);
	}

	public void setDoraList(List<Hai> doraList) {
		callPack();
		getOneGameData().setDoraList(doraList);
	}

	public void setHonba(int honba) {
		callPack();
		getOneGameData().setHonba(honba);
	}

	public void setKyokusu(int kyokusu) {
		callPack();
		if (oneGameData.getKyokusu() != kyokusu)
			moveKaze();
		oneGameData.setKyokusu(kyokusu);
	}

	public void setNumber(int number) {
		callPack();
		this.number = number;
	}

	public void setPlayerNumber(int number) {
		callPack();
		players = new ClientPlayer[number];
	}

	/**
	 * ゲームの最初に呼び出すメソッド プレイヤーと席の関係を決定する
	 * 
	 * @param playerList
	 *            プレイヤーの情報が東,南,西,北(東1局)の順に格納されている
	 * @param index
	 *            自分の風に対応する数字.
	 */
	public void setPlayerToTheChair(List<Player> playerList, int index) {
		callPack();
		players = new ClientPlayer[4];
		parentChair = (4 - index) % 4;
		int delta = getIndexInServerFromIndexInClient(0);
		Map<Kaze, Integer> kaze = getKaze();
		for (int i = 0; i < 4; i++) {
			players[i] = new ClientPlayer(playerList.get((delta + i) % 4));
			players[i].setKaze(Kaze.valueOf((delta + i)%4));
			kaze.put(Kaze.valueOf((delta + i) % 4), i);
		}
	}

	public void setScore(int index, int score) {
		callPack();
		getOneGameData().getScoreMap().put(index, score);
	}

	/**
	 * プレイヤーの点数を変更する
	 * 
	 * @param score
	 *            点数の配列.東,南,西,北の順番で格納されている.
	 */
	public void setScoreToThePlayer(int[] score) {
		callPack();
		int delta = getIndexInServerFromIndexInClient(0);
		for (int i = 0; i < 4; i++) {
			players[i].setScore(score[delta + i]);
		}
	}

	public void setSekiMap(Map<Player, Integer> sekiMap) {
		callPack();
		for(Player p:sekiMap.keySet()){
			players[sekiMap.get(p)] = new ClientPlayer(p);
		}
	}

	public void setTehai(List<Hai> tehai) {
		callPack();
		oneGameData.setTehai(tehai);
	}

	/**
	 * プレイヤーの手牌のサイズを変更する
	 * 
	 * @param tehaiSize
	 *            手牌のサイズが東,南,西,北の順に格納されている.
	 */
	public void setTehaiSize(List<Integer> tehaiSize) {
		callPack();
		int delta = getIndexInServerFromIndexInClient(0);
		for (int i = 0; i < 4; i++) {
			players[i].setTehaiSize(tehaiSize.get(delta + i));
		}
	}

	public void setTsumiBou(int tsumibou) {
		callPack();
		getOneGameData().setTsumiBou(tsumibou);
	}

	public void setTsumoHai(Hai hai) {
		callPack();
		getOneGameData().setTsumoHai(hai);
	}

	public void setWanpaiSize(int wanpaiSize) {
		callPack();
		getOneGameData().setWanpaiSize(wanpaiSize);
	}

	public void setYamaSize(int yamaSize) {
		callPack();
		getOneGameData().setYamaSize(yamaSize);
	}

	public void startAnimation(int player, StateCode sc) {
		oneGameData.setNakiPlayer(player);
		animationCount = 0;
		animeState = sc;
	}

	/**
	 * 局開始時に呼ばれるメソッド
	 */
	public void startGame() {
		oneGameData = new OneGameData();
	}
}
