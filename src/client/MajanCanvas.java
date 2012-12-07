package client;

import static client.Constant.BUTTON_CURVE;
import static client.Constant.BUTTON_HEIGHT;
import static client.Constant.BUTTON_WIDTH;
import static client.Constant.HAI_HEIGHT;
import static client.Constant.HAI_WIDTH;
import static client.Constant.PLAYER_BLOCK1_X;
import static client.Constant.PLAYER_BLOCK1_Y;
import static client.Constant.PLAYER_BLOCK2_X;
import static client.Constant.PLAYER_BLOCK2_Y;
import static client.Constant.SECONDS_PER_FRAME;
import static client.Constant.WINDOW_HEIGHT;
import static client.Constant.WINDOW_WIDTH;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pages.GraphicalPage;
import pages.Page;
import system.Hai;
import system.MajanHai;
import system.Mentu;
import system.Mentu.MentuHai;
import system.Player;
import test.GlobalVar;

public class MajanCanvas extends GraphicalPage implements MouseListener,MouseMotionListener, Page{
	private ClientInfo info;
	private MajanFrame frame;
	private boolean existTsumo;
	private GameThread gthread;
	private OperatorThread opthread;
	private EnumSet<StateCode> stateCodes;
	private Map<Hai, Image> haiImageMap;
	private Image haiBackImage;
	// system?
	private Client operator;
	private List<StateCode> buttonList;
	// graphics
	private Image imgBuffer;
	private Graphics gg;
	private boolean isAlive;

	// 結合テスト
	private static int objSize = 0;
	int number;


	/**
	 * サーバーとの通信を確認するスレッド。何か通信があれば、データを受け取ったり、メソッドを実行したりする。
	 */

	{
		existTsumo = true;
		isAlive = true;
	}

	private class OperatorThread extends Thread {
		@Override
		public void run() {
			//TODO 通信
			while (isAlive) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	private class GameThread extends Thread {
		@Override
		public void run() {
			long preTime = System.currentTimeMillis();

			while (isAlive) {
				repaint();
				try {
					long dt = System.currentTimeMillis() - preTime;
					if (dt < SECONDS_PER_FRAME) {
						Thread.sleep(SECONDS_PER_FRAME - dt);
					}
				} catch (InterruptedException e) {
					// nothing to do
				}
				preTime = System.currentTimeMillis();
			}

		}
	}

	public MajanCanvas(MajanFrame frame,Client operator){
		this(frame);
		System.out.println(operator == null);
		this.operator = operator;
		System.out.println(operator == null);
		setOperator(operator);
		System.out.println(operator == null);
		if(operator != null)
			((ClientOperator)operator).setPage(this);
	}
	@Override
	public String getPageName(){
		return "Canvas";
	}

	public MajanCanvas(MajanFrame frame) {
		this.frame = frame;
		this.haiImageMap = new HashMap<Hai, Image>();
		for (Hai hai : MajanHai.values()) {
			haiImageMap.put(hai, ImageLoader.load(MajanHaiIDMapper.getID(hai)));
		}
		this.haiBackImage = ImageLoader.load(ImageID.hai_back);
		this.stateCodes = EnumSet.of(StateCode.WAIT);

//		this.operator = new ClientOperator(this);

		// 結合テスト
		this.number = (objSize++)%4;
		this.info = new ClientInfo(number);
		info.sekiMap = new HashMap<Player, Integer>(4);
		for (int i = 0; i < 4; i++) {
			info.sekiMap.put(GlobalVar.players[(number + i) % 4], i);
		}
		System.out.println("add operator");
		
		this.buttonList = new ArrayList<StateCode>();

		// this.testOperator = new Test(this);

		this.opthread = new OperatorThread();
		this.opthread.start();

		this.addMouseListener(this);
		addMouseMotionListener(this);

		this.gthread = new GameThread();
		this.gthread.start();
	}

	public ClientInfo getInfo() {
		return info;
	}

	@Override
	public void paint(Graphics g) {
		if (imgBuffer == null)
			imgBuffer = createImage(getWidth(), getHeight());
		if (gg == null)
			gg = imgBuffer.getGraphics();
		Graphics2D gBody = (Graphics2D) g;
		Graphics2D g2 = (Graphics2D) gg;
		
		g2.clearRect(0, 0, getWidth(), getHeight());
		
		drawHai(0, PLAYER_BLOCK1_X, PLAYER_BLOCK1_Y, 170, g2);

		g2.rotate(-Math.PI);
		g2.translate(-WINDOW_WIDTH, -WINDOW_HEIGHT);

		drawHai(2, PLAYER_BLOCK1_X, PLAYER_BLOCK1_Y, 170, g2);

		g2.rotate(Math.PI / 2.0);
		g2.translate(0, -WINDOW_WIDTH);

		drawHai(1, PLAYER_BLOCK2_X, PLAYER_BLOCK2_Y, 250, g2);

		g2.rotate(Math.PI);
		g2.translate(-WINDOW_HEIGHT, -WINDOW_WIDTH);

		drawHai(3, PLAYER_BLOCK2_X, PLAYER_BLOCK2_Y, 250, g2);

		g2.rotate(-Math.PI / 2.0);
		g2.translate(-WINDOW_WIDTH, 0);

		if (stateCodes.contains(StateCode.END)) {
			g2.setColor(Color.RED);
			g2.drawString("END", getWidth() / 2, getHeight() / 2);
		}
		if (stateCodes.contains(StateCode.SELECT_BUTTON)) {
			int size = buttonList.size();
			int width = getWidth() / 2;
			int height = getHeight() / 2;
			int half = size / 2;
			for (int i = 0; i < size; i++) {
				g2.setColor(Color.orange);
				int tmpX = width - BUTTON_WIDTH * (half - i) - 10 * (half - i);
				int tmpY = height - BUTTON_HEIGHT / 2 - 10;
				g2.fillRoundRect(tmpX, tmpY, BUTTON_WIDTH, BUTTON_HEIGHT,
						BUTTON_CURVE, BUTTON_CURVE);
				g2.setColor(Color.BLACK);
				g2.setFont(new Font("", Font.BOLD, 15));
				int margin = buttonList.get(i).name().compareTo("KYUSYUKYUHAI") == 0 ? 0
						: 7;

				g2.drawString(buttonList.get(i).name().substring(margin), tmpX
						+ BUTTON_WIDTH / 2, tmpY + BUTTON_HEIGHT / 2);
			}
		}
		gBody.drawImage(imgBuffer, 0, 0, this);

	}

	public void drawHai(int player, int ix, int iy, int sute_x, Graphics2D g2) {
		if (info == null)
			return;
		int tehaiSize = 0;
		if (player != 0) {
			tehaiSize = info.tehaiSizeMap.get(player);
		} else {
			tehaiSize = info.tehai.size();
		}
		List<Integer> selectedIndexes = info.selectedIndexes;
		int dx = 0;
		for (int i = 0; i < tehaiSize; i++) {
			if (player != 0) {
				g2.drawImage(haiBackImage, ix + dx, iy + 270, HAI_WIDTH,
						HAI_HEIGHT, null);
			} else {
				Image image = haiImageMap.get(info.tehai.get(i));
				int selectedMargin = selectedIndexes.contains((Integer) i) ? -20
						: 0;
				g2.drawImage(image, ix + dx, iy + 270 + selectedMargin,
						HAI_WIDTH, HAI_HEIGHT, null);
				drawColoredFrame(g2, i, ix + dx, iy + selectedMargin + 270);
			}
			dx += HAI_WIDTH;
		}
		if (info.currentTurn == player && info.tsumoHai != null) {
			if (player == 0) {
				dx += 20;
				int selectedMargin = selectedIndexes.contains(13) ? -20 : 0;
				g2.drawImage(haiImageMap.get(info.tsumoHai), ix + dx, iy + 270
						+ selectedMargin, HAI_WIDTH, HAI_HEIGHT, null);
				drawColoredFrame(g2, 13, ix + dx, iy + selectedMargin + 270);
			} else {
				dx += 20;
				g2.drawImage(haiBackImage, ix + dx, iy + 270, HAI_WIDTH,
						HAI_HEIGHT, null);
			}
		}

		List<Mentu> hurohaiList = info.hurohaiMap.get(player);
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
			Mentu mentu = hurohaiList.get(i);
			int mentuSize = mentu.isKakan() ? mentu.size() - 1 : mentu.size();
			if (mentuSize == 0)
				continue;
			// 右から順番に格納
			MentuHai[] hurohaiArray = new MentuHai[mentu.isKakan() ? mentuSize + 1
					: mentuSize];
			dx = screenWidth - HAI_HEIGHT * 2;
			if (mentu.isNaki()) {
				int fromKaze = (info.kaze.get(hurohaiList.get(i).getKaze()) + 4 - player) % 4;
				fromKaze = (fromKaze - 1) * (fromKaze - 2) / 2
						* (mentuSize - 1) - (fromKaze - 1) * (fromKaze - 3);
				int j = 0;
				for (MentuHai mh : mentu.asList()) {
					if (mh.isNaki()) {
						hurohaiArray[fromKaze] = mh;
						continue;
					}
					if (hurohaiArray[j] != null || j == fromKaze) {
						j++;
					}
					hurohaiArray[j++] = mh;
				}
				for (j = 0; j < mentuSize; j++) {
					Hai tmpHai = MajanHai.valueOf(hurohaiArray[j].type(),
							hurohaiArray[j].aka());
					Image image = haiImageMap.get(tmpHai);
					// TODO
					if (j == fromKaze) {
						g2.rotate(Math.PI / 2.0);
						g2.translate(0, -screenHeight);
						g2.drawImage(image, dy + (HAI_HEIGHT - HAI_WIDTH),
								screenHeight - dx - HAI_WIDTH, HAI_WIDTH,
								HAI_HEIGHT, null);
						if (mentu.isKakan()) {
							tmpHai = MajanHai.valueOf(hurohaiArray[3].type(),
									hurohaiArray[3].aka());
							image = haiImageMap.get(tmpHai);
							g2.drawImage(image, dy
									+ (HAI_HEIGHT - HAI_WIDTH * 2),
									screenHeight - dx - HAI_WIDTH, HAI_WIDTH,
									HAI_HEIGHT, null);
						}
						g2.translate(0, screenHeight);
						g2.rotate(-Math.PI / 2.0);
						dx -= HAI_HEIGHT;
					} else {
						g2.drawImage(image, dx, dy, HAI_WIDTH, HAI_HEIGHT, null);
						dx -= HAI_WIDTH;
					}
				}
			} else {
				int count = 0;
				for (MentuHai mh : mentu.asList()) {
					Hai tmpHai = MajanHai.valueOf(mh.type(), mh.aka());
					Image image;
					if (count == 0 || count == 3)
						image = haiBackImage;
					else
						image = haiImageMap.get(tmpHai);
					// TODO
					g2.drawImage(image, dx, dy, HAI_WIDTH, HAI_HEIGHT, null);
					dx -= HAI_WIDTH;
					count++;
				}
			}
			dy -= HAI_HEIGHT;
		}

		List<Hai> suteHaiList = null;
		synchronized (info.sutehaiMap) {
			suteHaiList = info.sutehaiMap.get(player);
		}
		dy = 0;
		for (int j = 0; j < 4; j++) {
			dx = 170 - HAI_WIDTH;
			for (int i = 0; j * 6 + i < suteHaiList.size() && i < 6; i++) {
				Image image = haiImageMap.get(suteHaiList.get(j * 6 + i));
				if (info.reachPosMap.get(player) == null
						|| info.reachPosMap.get(player) != j * 6 + i) {
					g2.drawImage(image, ix + HAI_WIDTH + dx, iy + dy,
							HAI_WIDTH, HAI_HEIGHT, null);
					dx += HAI_WIDTH;
				} else {
					g2.rotate(Math.PI / 2.0);
					g2.translate(0, -screenHeight);
					g2.drawImage(image, iy + dy, screenHeight - dx - HAI_HEIGHT - HAI_WIDTH
							- ix, HAI_WIDTH, HAI_HEIGHT, null);
					g2.translate(0, screenHeight);
					g2.rotate(-Math.PI / 2.0);
					dx += HAI_HEIGHT;
				}
			}
			dy += HAI_HEIGHT;
		}
	}

	@Override
	public void update(Graphics g) {
		this.paint(g);
	}

	private int addSelectedIndexes(int mx, int my, int max,
			List<List<Integer>> rule) {
		List<Hai> tehai = getInfo().tehai;
		List<Integer> selectedIndexes;
		selectedIndexes = getInfo().selectedIndexes;
		int i;
		for (i = 0; i < tehai.size(); i++) {
			if (PLAYER_BLOCK1_X + HAI_WIDTH * i <= mx
					&& mx <= PLAYER_BLOCK1_X + HAI_WIDTH * (i + 1)) {
				int margin = 0;
				if (selectedIndexes.contains(i))
					margin -= 20;
				if (PLAYER_BLOCK1_Y + 270 + margin <= my
						&& my <= PLAYER_BLOCK1_Y + 270 + HAI_HEIGHT + margin) {
					if (selectedIndexes.contains(i)) {
						if (max != 1) {
							selectedIndexes.remove((Integer) i);
							i = -1;
						}
					} else {
						if(rule != null)
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
			if (getInfo().currentTurn == 0) {
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

	private void dispatch(StateCode sc) {
		switch (sc) {
		case SELECT_PON_HAI:
			operator.sendPonIndexList(info.selectedIndexes);
			break;
		case DISCARD_SELECT:
			operator.sendDiscardIndex(info.selectedIndexes.get(0));
			break;
		case SELECT_CHI_HAI:
			operator.sendChiIndexList(info.selectedIndexes);
			break;
		case SELECT_ANKAN_HAI:
			operator.sendAnkanIndexList(info.selectedIndexes);
			break;
		case SELECT_KAKAN_HAI:
			operator.sendKakanIndex(info.selectedIndexes.get(0));
			break;
		case SELECT_REACH_HAI:
			operator.sendReachIndex(info.selectedIndexes.get(0));
			break;
		case SELECT_CHI:
			operator.sendChiIndexList(null);
			break;
		case SELECT_PON:
			operator.sendPonIndexList(null);
			break;
		case SELECT_MINKAN:
			operator.sendMinkan(false);
			break;
		case SELECT_ANKAN:
			operator.sendAnkanIndexList(null);
			break;
		case SELECT_REACH:
			operator.sendReachIndex(-1);
			break;
		case SELECT_KAKAN:
			operator.sendKakanIndex(-1);
			break;
		case KYUSYUKYUHAI:
			operator.sendKyusyukyuhai(false);
		case SELECT_RON:
			operator.sendRon(false);
		default:
			break;
		}
		refreshStateCodes();
	}

	@Override
	public void mouseClicked(java.awt.event.MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();
		hideFocus();
		if (stateCodes.contains(StateCode.WAIT)){
			refreshNakiListExclude(null);
			refreshStateCodes();
			refreshButtonList();
			return;
		}
		System.out.println(number);
		boolean hasOnlyElem = false;
		if (stateCodes.contains(StateCode.SELECT_BUTTON)) {
			int width = getWidth() / 2;
			int height = getHeight() / 2;
			int i = buttonList.size() + 1;
			//クリック個所がボタンのある範囲にあるかどうか(y方向のみ判定)
			if (my <= height + BUTTON_HEIGHT / 2 - 10
					&& my >= height - BUTTON_HEIGHT / 2 - 10) {
				int half = buttonList.size() / 2;
				//クリック個所がボタンのある範囲にあるかどうか(x方向)
				for (int j = 0; j < buttonList.size(); j++) {
					if (mx <= width - BUTTON_WIDTH * (half - 1 - j) - 10
							* (half - j)
							&& mx >= width - BUTTON_WIDTH * (half - j) - 10
									* (half - j)) {
						i = j;
						break;
					}
				}
			} else {
				//行動の選択肢が1つしかない場合は牌を直接選ぶことを許す
				//ここではそれ以外を除外し,ここで処理を終了している
				if(buttonList.size() != 1){
					refreshStateCodes();
					refreshNakiListExclude(null);
					for(StateCode sc:buttonList){
						dispatch(sc);
					}
					refreshButtonList();
					return;
				}
			}
			//クリック個所がボタンのある範囲にあったか
			StateCode sc;
			boolean tmpFlag = false;
			if (i < buttonList.size()
			// 選択肢が1つで
					|| (buttonList.size() == 1
					// クリック個所が手牌のある範囲にあり(y方向の判定)
							&& (my <= PLAYER_BLOCK1_Y + HAI_HEIGHT + 270
							&& my >= PLAYER_BLOCK1_Y + 270)
					// ツモ牌以外の牌がある範囲にあるか(x方向の判定)
					&& ((mx <= PLAYER_BLOCK1_X + info.tehai.size() * HAI_WIDTH 
					&& mx >= PLAYER_BLOCK1_X)
					// ツモ牌のある範囲にある(x方向の判定)
					|| (mx <= PLAYER_BLOCK1_X + (info.tehai.size() + 1) * HAI_WIDTH + 20 
					&& mx >= PLAYER_BLOCK1_X + info.tehai.size() * HAI_WIDTH + 20)))) {
				//複数の選択肢から1つを選んだか
				//もしくは1つの選択肢の指定した箇所をクリックしたか
				if(my <= height + BUTTON_HEIGHT / 2 - 10
						&& my >= height - BUTTON_HEIGHT / 2 - 10){
					sc = getSelectHaiFromSelect(buttonList.get(i));

					refreshStateCodes();
					refreshNakiListExclude(sc);
					for(StateCode subSc:buttonList){
						dispatch(subSc);
					}
					refreshButtonList();
					return;
				}
				else{
					sc = getSelectHaiFromSelect(buttonList.get(0));
					List<List<Integer>> tmpList = getInfo().ableIndexList.get(sc);
					if(tmpList != null){
						for(List<Integer> tmpSubList:tmpList){
							if(tmpSubList == null)
								break;
							for(Integer integer:tmpSubList){
								if(mx <= PLAYER_BLOCK1_X + (integer + 1) * HAI_WIDTH
										&& mx >= PLAYER_BLOCK1_X + integer * HAI_WIDTH){
											tmpFlag = true;
											break;
								}
							}
							if(tmpSubList.contains(13) 
									&& mx <= PLAYER_BLOCK1_X + (info.tehai.size() + 1) * HAI_WIDTH + 20
									&& mx >= PLAYER_BLOCK1_X + info.tehai.size() * HAI_WIDTH + 20){
								tmpFlag = true;
							}
							if(tmpFlag)
								break;
						}
					}
				}
			}
			else{
				refreshStateCodes();
				refreshNakiListExclude(null);
				for(StateCode subSc:buttonList){
					dispatch(subSc);
				}
				refreshButtonList();
				return;				
			}
			//今回選択された動作以外に関係するデータは消去

			refreshStateCodes();
			refreshNakiListExclude(sc);
			refreshButtonList();
				
			// 今回選択された動作に対する動作
			if(!tmpFlag){
				refreshNakiListExclude(null);
				dispatch(sc);
				return;
			}
			switch (sc) {
			case SELECT_CHI_HAI:
				if (info.ableIndexList.get(sc).size() != 1) {
					addStateCode(sc);
				} else {
					operator.sendChiIndexList(info.ableIndexList.get(sc).get(0));
					refreshButtonList();
					refreshNakiListExclude(null);
					refreshStateCodes();
					return;
				}
				break;
			case SELECT_PON_HAI:
				if (info.ableIndexList.get(sc).size() != 1) {
					addStateCode(sc);
				} else {
					operator.sendPonIndexList(info.ableIndexList.get(sc).get(0));
					refreshButtonList();
					refreshNakiListExclude(null);
					refreshStateCodes();
					return;
				}
				break;
			case SELECT_MINKAN:
				operator.sendMinkan(true);
				info.selectedIndexes.clear();
				addStateCode(StateCode.DISCARD_SELECT);
				break;
			case KYUSYUKYUHAI:
				operator.sendKyusyukyuhai(true);
				break;
			case SELECT_ANKAN_HAI:
				if (info.ableIndexList.get(sc).size() != 1) {
					addStateCode(sc);
				} else {
					operator.sendAnkanIndexList(info.ableIndexList.get(sc).get(
							0));
					refreshButtonList();
					refreshNakiListExclude(null);
					refreshStateCodes();
					return;
				}
				break;
			case SELECT_REACH_HAI:
				if (!(info.ableIndexList.get(sc).size() == 1 && info.ableIndexList
						.get(sc).get(0).size() == 1)) {
					addStateCode(sc);
				} else {
					frame.setTitle(frame.getTitle() + "*");
					operator.sendReachIndex(info.ableIndexList.get(sc).get(0)
							.get(0));
					refreshButtonList();
					refreshNakiListExclude(null);
					refreshStateCodes();
					return;
				}
				break;
			case SELECT_KAKAN_HAI:
				if (!(info.ableIndexList.get(sc).size() == 1 && info.ableIndexList
						.get(sc).get(0).size() == 1)) {
					addStateCode(sc);
				} else {
					operator.sendKakanIndex(info.ableIndexList.get(sc).get(0)
							.get(0));
					refreshButtonList();
					refreshNakiListExclude(null);
					refreshStateCodes();
					return;
				}
				break;
			case SELECT_TSUMO:
				operator.sendTsumoAgari();
				break;
			case SELECT_RON:
				operator.sendRon(true);
			default:
				break;
			}
			hasOnlyElem = true;
		}
		for (StateCode sc : stateCodes) {
			if(hasOnlyElem){
				addSelectedIndexesWhenOverHai(mx, my, sc.getNum(), info.ableIndexList.get(sc));
				refreshNakiListExclude(null);
				return;
			}
			List<Integer> selectedIndexes = getInfo().selectedIndexes;
			int size = selectedIndexes.size();
			int i = selectedIndexes.isEmpty() ? -1 : selectedIndexes.get(0);
			int j = -1;
			if (sc == StateCode.DISCARD_SELECT) {
				j = addSelectedIndexes(mx, my, sc.getNum(),
						new ArrayList<List<Integer>>());
			} else {
				j = addSelectedIndexes(mx, my, sc.getNum(),
						info.ableIndexList.get(sc));
			}
			boolean flag = false;
			if (sc.getNum() != 1) {
				if (size == sc.getNum() && j == -1) {
					addSelectedIndexes(mx, my, sc.getNum(),
							info.ableIndexList.get(sc));
				}
				if (selectedIndexes.size() == sc.getNum()) {
					flag = true;
				}
			}
			if (flag || (sc.getNum() == 1 && i == j && j != -1)) {
				System.out.println(selectedIndexes);
				dispatch(sc);
				selectedIndexes.clear();
				refreshButtonList();
				refreshStateCodes();
				refreshNakiListExclude(null);
			}
			return;
		}
	}

	@Override
	public void mouseEntered(java.awt.event.MouseEvent e) {}
	@Override
	public void mouseExited(java.awt.event.MouseEvent e) {}
	@Override
	public void mousePressed(java.awt.event.MouseEvent e) {}
	@Override
	public void mouseReleased(java.awt.event.MouseEvent e) {}

	public void addStateCode(StateCode stateCode) {
		while (stateCodes.contains(StateCode.WAIT))
			stateCodes.remove(StateCode.WAIT);
		stateCodes.add(stateCode);
	}
	public void refreshStateCodes() {
		stateCodes.clear();
		stateCodes.add(StateCode.WAIT);
	}
	public void refreshButtonList() {
		buttonList.clear();
	}
	public void addButtonList(StateCode sc) {
		buttonList.add(sc);
	}

	public void setExistTsumo(boolean existTsumo) {
		this.existTsumo = existTsumo;
	}

	private void drawFrameBasedOnRule(Graphics2D g2, int index, int x, int y,
			List<List<Integer>> rule) {
		List<Integer> selectedIndexes = info.selectedIndexes;
		g2.setColor(Color.RED);
		int size = info.selectedIndexes.size();
		if(rule != null)
			for (List<Integer> l : rule) {
				if (l == null)
					continue;
				if ((size == 0 || l.containsAll(selectedIndexes))
						&& l.contains(index))
					g2.drawRoundRect(x, y, HAI_WIDTH, HAI_HEIGHT, BUTTON_CURVE,
							BUTTON_CURVE);
			}
	}

	private void drawColoredFrame(Graphics2D g2, int index, int x, int y) {
		Map<StateCode, List<List<Integer>>> ableIndexMap = getInfo().ableIndexList;
		for (StateCode sc : ableIndexMap.keySet()) {
			if (stateCodes.contains(StateCode.SELECT_BUTTON)
					|| stateCodes.contains(sc))
				drawFrameBasedOnRule(g2, index, x, y, ableIndexMap.get(sc));
		}
	}

	private void refreshNakiListExclude(StateCode sc) {
		for (StateCode key : getInfo().ableIndexList.keySet()) {
			if (key == StateCode.SELECT_MINKAN)
				continue;
			if (sc == null)
				getInfo().ableIndexList.get(key).clear();
			else {
				String tmpString = sc.name();
				if (!sc.name().contains("_HAI"))
					tmpString += "_HAI";
				if (key.name().compareTo(tmpString) != 0)
					getInfo().ableIndexList.get(key).clear();
			}
		}

	}

	/**
	 * ボタン選択状態から牌選択状態を得るメソッド
	 * 例)input:SELECT_CHI -> output:SELECT_CHI_HAI
	 * @param sc input
	 * @return output
	 */
	public StateCode getSelectHaiFromSelect(StateCode sc){
		switch(sc){
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
	
	@Override
	public void mouseDragged(MouseEvent e) {}
	private void addSelectedIndexesWhenOverHai(int mx, int my, int max,
			List<List<Integer>> rule) {
		List<Hai> tehai = getInfo().tehai;
		List<Integer> selectedIndexes = getInfo().selectedIndexes;
		if(PLAYER_BLOCK1_Y + 270 > my
				|| my > PLAYER_BLOCK1_Y + 270 + HAI_HEIGHT + 20){
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
						&& my <= PLAYER_BLOCK1_Y + 270 + HAI_HEIGHT + margin) {
					if (!selectedIndexes.contains(i)) {
						int count = 0;
						List<Integer> tmpSubList = new ArrayList<Integer>();
						if (selectedIndexes.size() < max
								&& selectedIndexes.size() > 0)
							return;
						selectedIndexes = new ArrayList<Integer>();
						if(rule != null)
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
						getInfo().selectedIndexes = selectedIndexes;
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
				&& my <= PLAYER_BLOCK1_Y + 270 + HAI_HEIGHT + margin
				&& my >= PLAYER_BLOCK1_Y + 270 + margin) {
			if (!selectedIndexes.contains(13)) {
				int count = 0;
				List<Integer> tmpSubList = new ArrayList<Integer>();
				if (selectedIndexes.size() < max
						&& selectedIndexes.size() > 0)
					return;
				selectedIndexes = new ArrayList<Integer>();
				if(rule != null)
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
				getInfo().selectedIndexes = selectedIndexes;
			}
			return;
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		if(stateCodes.contains(StateCode.WAIT))
			return;
		int mx = e.getPoint().x;
		int my = e.getPoint().y;
		for(StateCode sc:stateCodes){
			addSelectedIndexesWhenOverHai(mx, my, sc.getNum(), info.ableIndexList.get(sc));
		}
	}
	
	public Image getImage(){
		return imgBuffer;
	}
	public void kill(){
		isAlive = false;
	}
	public void movePage(String order){
		frame.setPage(order,imgBuffer);
		kill();
	}
	public void setFocus(){
		frame.setLocation(0, 0);
		frame.setAlwaysOnTop(true);
	}
	public void hideFocus(){
		frame.setAlwaysOnTop(false);
	}

}
