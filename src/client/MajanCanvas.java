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
import static client.Constant.SCALED_HAI_HEIGHT;
import static client.Constant.SCALED_HAI_WIDTH;
import static client.Constant.SECONDS_PER_FRAME;
import static client.Constant.TEHAI_INDENT;
import static client.Constant.WINDOW_HEIGHT;
import static client.Constant.WINDOW_WIDTH;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import pages.GraphicalPage;
import pages.Page;
import system.Hai;
import system.Kaze;
import system.MajanHai;
import system.Mentu;
import system.Mentu.MentuHai;
import system.Player;

public class MajanCanvas extends GraphicalPage implements MouseListener,
		MouseMotionListener, Page {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ClientInfo info;
	private GameThread gthread;
	private OperatorThread opthread;
	private EnumSet<StateCode> stateCodes;
	private Map<Hai, Image> haiImageMap;
	private Map<Hai, Image> scaledHaiImageMap;
	private Image haiBackImage;
	private Image scaledHaiBackImage;
	private Image scaledDarkHaiBackImage;
	private Image reachImage;
	private int animationCount;
	private StateCode animeState;
	private int nakiPlayer;
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
		isAlive = true;
		animationCount = -1;
	}

	public void startAnimation(int player, StateCode sc) {
		nakiPlayer = player;
		animationCount = 0;
		animeState = sc;
	}

	private class OperatorThread extends Thread {
		@Override
		public void run() {
			// TODO 通信
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

	public MajanCanvas(MajanFrame frame, Client operator) {
		this(frame);
		System.out.println(operator == null);
		this.operator = operator;
		System.out.println(operator == null);
		setOperator(operator);
		System.out.println(operator == null);
		if (operator != null)
			((ClientOperator) operator).setPage(this);
	}

	@Override
	public String getPageName() {
		return "Canvas";
	}

	public MajanCanvas(MajanFrame frame) {
		setFrame(frame);
		operator = getFrame().getOperator();
		this.info = frame.getInfo();
		info.resetBeforeKyoku();
		this.haiImageMap = new HashMap<Hai, Image>();
		for (Hai hai : MajanHai.values()) {
			haiImageMap.put(hai, ImageLoader.load(MajanHaiIDMapper.getID(hai)));
		}
		this.scaledHaiImageMap = new HashMap<Hai, Image>();
		for (Hai hai : MajanHai.values()) {
			scaledHaiImageMap.put(hai,
					ImageLoader.loadScaled(MajanHaiIDMapper.getID(hai)));
		}
		this.haiBackImage = ImageLoader.load(ImageID.hai_back);
		this.scaledHaiBackImage = ImageLoader.loadScaled(ImageID.hai_back);
		this.scaledDarkHaiBackImage = ImageLoader
				.loadScaled(ImageID.hai_darkback);
		this.stateCodes = EnumSet.of(StateCode.WAIT);
		this.reachImage = Toolkit.getDefaultToolkit().createImage(
				"image/reach.png");

		// 結合テスト
		this.number = -1;
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
		if (info == null)
			info = getFrame().getInfo();
		return info;
	}

	private void drawPartOfYama(Graphics g2, int start, int end, int ix, int iy) {
		int wanpaiSize = info.wanpaiSize;
		int limit = info.yamaSize / 2 + info.yamaSize % 2 + info.finish + 1;
		int finish = info.finish
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
						|| ((limit - 1 == i || limit - 1 == i + 68) && info.yamaSize % 2 == 1))
					g2.drawImage(scaledDarkHaiBackImage, ix + dx + indent_x, iy
							+ indent_y, SCALED_HAI_WIDTH, SCALED_HAI_HEIGHT,
							null);
				else if ((doraStart <= i && doraStart + info.doraList.size() > i)
						|| (doraStart <= i + 68 && doraStart
								+ info.doraList.size() > i + 68)) {
					g2.drawImage(haiImageMap.get(info.doraList.get((i
							- (finish + 3) + 68) % 68)), ix + dx + indent_x, iy
							+ indent_y, SCALED_HAI_WIDTH, SCALED_HAI_HEIGHT,
							null);
					// TODO ドラ表示
				} else
					g2.drawImage(scaledHaiBackImage, ix + dx + indent_x, iy
							+ indent_y, SCALED_HAI_WIDTH, SCALED_HAI_HEIGHT,
							null);
			dx += SCALED_HAI_WIDTH;
			// TODO current
		}
	}

	@Override
	public void paint(Graphics g) {
		if (number == -1)
			return;
		if (imgBuffer == null)
			imgBuffer = createImage(getWidth(), getHeight());
		if (gg == null)
			gg = imgBuffer.getGraphics();
		Graphics2D gBody = (Graphics2D) g;
		Graphics2D g2 = (Graphics2D) gg;

		g2.clearRect(0, 0, getWidth(), getHeight());
		super.paint(g2);

		g2.setColor(Color.BLACK);
		g2.setFont(new Font("", Font.BOLD, 20));
		g2.drawString(info.bakaze.notation() + " " + info.kyokusu + "局",
				getWidth() / 2 - 50, getHeight() / 2 - 10 - 20);
		g2.drawString(info.honba + "本場", getWidth() / 2 - 50,
				getHeight() / 2 + 10 - 20);
		g2.drawString("積み棒:" + info.tsumiBou, getWidth() / 2 - 50,
				getHeight() / 2 + 30 - 20);

		drawHai(0, PLAYER_BLOCK1_X, PLAYER_BLOCK1_Y, 170, g2);
		drawSuteHai(0, PLAYER_BLOCK1_X, PLAYER_BLOCK1_Y, 170, g2);
		drawJihu(0, PLAYER_BLOCK1_X + 270, PLAYER_BLOCK1_Y + 30, g2);
		if (info.reachPosMap.get(0) != null)
			g2.drawImage(reachImage, PLAYER_BLOCK1_X + 200,
					PLAYER_BLOCK1_Y - 30, null);
		g2.rotate(-Math.PI);
		g2.translate(-WINDOW_WIDTH, -WINDOW_HEIGHT);

		drawHai(2, PLAYER_BLOCK1_X, PLAYER_BLOCK1_Y, 170, g2);
		drawSuteHai(2, PLAYER_BLOCK1_X, PLAYER_BLOCK1_Y, 170, g2);
		drawJihu(2, PLAYER_BLOCK1_X + 290, PLAYER_BLOCK1_Y + 60, g2);
		if (info.reachPosMap.get(2) != null)
			g2.drawImage(reachImage, PLAYER_BLOCK1_X + 200, PLAYER_BLOCK1_Y,
					null);

		g2.rotate(Math.PI / 2.0);
		g2.translate(-100, -WINDOW_WIDTH + 50);
		g2.rotate(-Math.PI / 20.0);

		drawHai(1, PLAYER_BLOCK2_X, PLAYER_BLOCK2_Y, 250, g2);

		g2.rotate(Math.PI / 20.0);
		g2.translate(100, -50);

		drawSuteHai(1, PLAYER_BLOCK2_X, PLAYER_BLOCK2_Y, 250, g2);
		drawJihu(1, PLAYER_BLOCK2_X + 300, PLAYER_BLOCK2_Y - 80, g2);
		if (info.reachPosMap.get(1) != null)
			g2.drawImage(reachImage, PLAYER_BLOCK2_X + 200,
					PLAYER_BLOCK2_Y - 100, null);

		g2.rotate(Math.PI);
		g2.translate(-WINDOW_HEIGHT + 100, -WINDOW_WIDTH - 80);

		g2.rotate(Math.PI / 15.0);

		drawHai(3, PLAYER_BLOCK2_X, PLAYER_BLOCK2_Y, 250, g2);

		g2.rotate(-Math.PI / 15.0);
		g2.translate(-100, 80);

		drawSuteHai(3, PLAYER_BLOCK2_X, PLAYER_BLOCK2_Y, 1250, g2);
		drawJihu(3, PLAYER_BLOCK2_X + 260, PLAYER_BLOCK2_Y - 60, g2);
		if (info.reachPosMap.get(3) != null)
			g2.drawImage(reachImage, PLAYER_BLOCK2_X + 200,
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

		if (stateCodes.contains(StateCode.END)) {
			g2.setColor(Color.RED);
			g2.drawString("END", getWidth() / 2, getHeight() / 2);
		}
		if (stateCodes.contains(StateCode.SELECT_BUTTON)) {
			int size = buttonList.size();
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

				g2.drawString(buttonList.get(i).getButtonName(), tmpX
						+ BUTTON_WIDTH / 2, tmpY + BUTTON_HEIGHT / 2);
			}
		}
		if (animationCount > -1) {
			int x = (getWidth() - BUTTON_WIDTH) / 2;
			int y = (getHeight() - BUTTON_HEIGHT) / 2;
			g2.setColor(Color.RED);
			g2.fillOval(x, y, BUTTON_WIDTH * 3 / 2, BUTTON_HEIGHT * 3 / 2);
			switch (nakiPlayer) {
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
				stateCodes.remove(StateCode.DRAW_ANIME);
			}
			animationCount++;
		}
		gBody.drawImage(imgBuffer, 0, 0, this);
	}

	public void drawJihu(int player, int ix, int iy, Graphics2D g2) {
		Kaze tmp = null;
		g2.setColor(Color.WHITE);
		for (Kaze k : Kaze.values()) {
			if (getInfo().kaze.get(k) == player) {
				tmp = k;
				break;
			}
		}
		if (player % 2 != 1)
			g2.drawString(tmp.notation(), ix - 30, iy);
		else
			g2.drawString(tmp.notation(), ix + 60, iy);
		JPanel tmpPanel = new JPanel();
		tmpPanel.add(new JLabel(getInfo().scoreMap.get(player) + ""));
		g2.drawString(getInfo().scoreMap.get(player) + "", ix, iy);
		g2.setColor(Color.BLACK);
	}

	public void drawSuteHai(int player, int ix, int iy, int sute_x,
			Graphics2D g2) {
		if (info == null)
			return;
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
		synchronized (info.sutehaiMap) {
			suteHaiList = info.sutehaiMap.get(player);
		}
		int dy = 0;
		int screenHeight = WINDOW_HEIGHT;
		for (int j = 0; j < 4; j++) {
			int dx = 170 - SCALED_HAI_WIDTH;
			for (int i = 0; j * 6 + i < suteHaiList.size() && i < 6; i++) {
				Image image = scaledHaiImageMap.get(suteHaiList.get(j * 6 + i));
				if (info.reachPosMap.get(player) == null
						|| info.reachPosMap.get(player) != j * 6 + i) {
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
				g2.drawImage(scaledHaiBackImage, ix + dx + TEHAI_INDENT,
						iy + 270, SCALED_HAI_WIDTH, SCALED_HAI_HEIGHT, null);
				dx += SCALED_HAI_WIDTH;
			} else {
				Image image = haiImageMap.get(info.tehai.get(i));
				int selectedMargin = selectedIndexes.contains((Integer) i) ? -20
						: 0;
				g2.drawImage(image, ix + dx, iy + 270 + selectedMargin,
						HAI_WIDTH, HAI_HEIGHT, null);
				drawColoredFrame(g2, i, ix + dx, iy + selectedMargin + 270);
				dx += HAI_WIDTH;
			}
		}
		if (info.currentTurn == player && info.tsumoHai != null) {
			if (player == 0) {
				dx += HAI_HEIGHT - HAI_WIDTH;
				int selectedMargin = selectedIndexes.contains(13) ? -20 : 0;
				g2.drawImage(haiImageMap.get(info.tsumoHai), ix + dx, iy + 270
						+ selectedMargin, HAI_WIDTH, HAI_HEIGHT, null);
				drawColoredFrame(g2, 13, ix + dx, iy + selectedMargin + 270);
			} else {
				dx += SCALED_HAI_HEIGHT - SCALED_HAI_WIDTH;
				g2.drawImage(scaledHaiBackImage, ix + dx + TEHAI_INDENT,
						iy + 270, SCALED_HAI_WIDTH, SCALED_HAI_HEIGHT, null);
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
			dx = screenWidth - SCALED_HAI_HEIGHT * 2;
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
					Image image = scaledHaiImageMap.get(tmpHai);
					// TODO
					if (j == fromKaze) {
						g2.rotate(Math.PI / 2.0);
						g2.translate(0, -screenHeight);
						g2.drawImage(image, dy
								+ (SCALED_HAI_HEIGHT - SCALED_HAI_WIDTH),
								screenHeight - dx - SCALED_HAI_WIDTH,
								SCALED_HAI_WIDTH, SCALED_HAI_HEIGHT, null);
						if (mentu.isKakan()) {
							tmpHai = MajanHai.valueOf(hurohaiArray[3].type(),
									hurohaiArray[3].aka());
							image = haiImageMap.get(tmpHai);
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
				for (MentuHai mh : mentu.asList()) {
					Hai tmpHai = MajanHai.valueOf(mh.type(), mh.aka());
					Image image;
					if (count == 0 || count == 3)
						image = scaledHaiBackImage;
					else
						image = scaledHaiImageMap.get(tmpHai);
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

	@Override
	public void update(Graphics g) {
		this.paint(g);
	}

	private void addSelectedIndexesWhenOverHai(int mx, int my, int max,
			List<List<Integer>> rule) {
		List<Hai> tehai = getInfo().tehai;
		List<Integer> selectedIndexes = getInfo().selectedIndexes;
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
				getInfo().selectedIndexes = selectedIndexes;
			}
			return;
		}
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

	private int isInButton(int mx, int my) {
		int width = getWidth() / 2;
		int height = PLAYER_BLOCK1_Y + 260;
		if (my <= height + BUTTON_HEIGHT / 2 - 10
				&& my >= height - BUTTON_HEIGHT / 2 - 10) {
			int half = buttonList.size() / 2;
			// クリック個所がボタンのある範囲にあるかどうか(x方向)
			for (int j = 0; j < buttonList.size(); j++) {
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
		StateCode sc = getSelectHaiFromSelect(buttonList.get(0));

		// ここから試運用

		List<StateCode> myTurnsActions = new ArrayList<StateCode>();
		myTurnsActions.add(StateCode.SELECT_REACH);
		myTurnsActions.add(StateCode.SELECT_ANKAN);
		myTurnsActions.add(StateCode.SELECT_TSUMO);

		if (myTurnsActions.contains(sc))
			return false;

		if (StateCode.SELECT_ANKAN == sc && isInTheHai(13, mx))
			return false;

		// ここまで試運用

		if (!getInfo().ableIndexList.containsKey(sc))
			return false;

		List<List<Integer>> tmpList = getInfo().ableIndexList.get(sc);
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
			number = info.tehai.size();
			mx -= 20;
		}
		if (mx <= PLAYER_BLOCK1_X + (number + 1) * HAI_WIDTH
				&& mx >= PLAYER_BLOCK1_X + number * HAI_WIDTH)
			return true;
		return false;
	}

	@Override
	public void mouseClicked(java.awt.event.MouseEvent e) {
		// choiseHai(e);
	}

	public void choiseHai(java.awt.event.MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();
		hideFocus();
		if (stateCodes.contains(StateCode.WAIT) || animationCount > -1) {
			return;
		}
		if (stateCodes.contains(StateCode.SELECT_BUTTON)) {
			StateCode sc = null;
			// ここから試運用
			boolean scopeSkip = false;
			// ここまで試運用
			if (isInButton(mx, my) != -1) {
				stateCodes.remove(StateCode.DISCARD_SELECT);
				sc = getSelectHaiFromSelect(buttonList.get(isInButton(mx, my)));
			} else if (buttonList.size() == 1 && isInSelectableHai(mx, my)) {
				sc = getSelectHaiFromSelect(buttonList.get(0));
			} else {
				for (StateCode subSc : buttonList) {
					dispatch(subSc);
				}
				refreshStateCodes();
				refreshNakiListExclude(null);
				refreshButtonList();
				scopeSkip = true;
			}
			if (!scopeSkip) {
				refreshButtonList();
				refreshStateCodes();
				addStateCode(sc);
				refreshNakiListExclude(sc);
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
		for (StateCode sc : stateCodes) {
			if (sc == StateCode.WAIT)
				return;
			if (info.ableIndexList.containsKey(sc)
					&& info.ableIndexList.get(sc).size() == 1) {
				info.selectedIndexes = info.ableIndexList.get(sc).get(0);
				dispatch(sc);
				info.selectedIndexes.clear();
				refreshNakiListExclude(null);
				refreshStateCodes();
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
	public void mouseEntered(java.awt.event.MouseEvent e) {
	}

	@Override
	public void mouseExited(java.awt.event.MouseEvent e) {
	}

	@Override
	public void mousePressed(java.awt.event.MouseEvent e) {
		choiseHai(e);
	}

	@Override
	public void mouseReleased(java.awt.event.MouseEvent e) {
	}

	public void addStateCode(StateCode stateCode) {
		while (stateCodes.contains(StateCode.WAIT))
			stateCodes.remove(StateCode.WAIT);
		if (stateCode != null) {
			stateCodes.add(stateCode);
		} else if (stateCodes.size() == 0) {
			stateCodes.add(StateCode.WAIT);
		}
	}

	public void refreshStateCodes() {
		if (stateCodes.contains(StateCode.DISCARD_SELECT)) {
			stateCodes.clear();
			stateCodes.add(StateCode.DISCARD_SELECT);
		} else {
			// ここからが試運用でない部分

			stateCodes.clear();
			stateCodes.add(StateCode.WAIT);

			// ここまでが試運用でない部分
		}
	}

	public void refreshButtonList() {
		buttonList.clear();
	}

	public void addButtonList(StateCode sc) {
		buttonList.add(sc);
	}

	private void drawFrameBasedOnRule(Graphics2D g2, int index, int x, int y,
			List<List<Integer>> rule) {
		List<Integer> selectedIndexes = info.selectedIndexes;
		g2.setColor(Color.RED);
		int size = info.selectedIndexes.size();
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
			if (key == StateCode.SELECT_MINKAN) {
				if (sc != StateCode.SELECT_MINKAN)
					getInfo().ableIndexList.get(StateCode.SELECT_MINKAN)
							.clear();
				continue;
			}
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
	 * ボタン選択状態から牌選択状態を得るメソッド 例)input:SELECT_CHI -> output:SELECT_CHI_HAI
	 * 
	 * @param sc
	 *            input
	 * @return output
	 */
	public StateCode getSelectHaiFromSelect(StateCode sc) {
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

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (stateCodes.contains(StateCode.WAIT))
			return;
		int mx = e.getPoint().x;
		int my = e.getPoint().y;
		for (StateCode sc : stateCodes) {
			addSelectedIndexesWhenOverHai(mx, my, sc.getNum(),
					info.ableIndexList.get(sc));
		}
	}

	public Image getImage() {
		return imgBuffer;
	}

	public void kill() {
		isAlive = false;
	}

	public void movePage(String order) {
		kill();
		repaint();
		getFrame().setPage(order);
	}

	public void setFocus() {
		// getFrame().setLocation(0, 0);
		// getFrame().setAlwaysOnTop(true);
	}

	public void hideFocus() {
		getFrame().setAlwaysOnTop(false);
	}

	public void setPlayers(Player[] players, int index) {
		number = index;
		ClientInfo info = getFrame().getInfo();
		if (info != null)
			info = new ClientInfo(number);
		this.info = info;
		info.sekiMap = new HashMap<Player, Integer>(4);
		for (int i = 0; i < 4; i++) {
			info.sekiMap.put(info.players[(4 - number) % 4], i);
		}
	}

	public void setBakaze(Kaze kaze) {
		info.bakaze = kaze;
	}

	public void setKyokusu(int kyokusu) {
		if (info.kyokusu != kyokusu)
			moveKaze();
		info.kyokusu = kyokusu;
	}

	private void moveKaze() {
		Map<Kaze, Integer> kaze = info.kaze;
		for (Kaze k : kaze.keySet()) {
			kaze.put(k, (kaze.get(k) + 1) % 4);
		}
		info.finish = (info.finish + kaze.get(Kaze.TON) * 17) % 68;
	}
}
