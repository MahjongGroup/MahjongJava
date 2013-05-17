package test;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import system.Field;
import system.Functions;
import system.Rule;
import system.agari.AgariMethods;
import system.agari.AgariParam;
import system.agari.AgariResult;
import system.hai.Hai;
import system.hai.HaiType;
import system.hai.HurohaiList;
import system.hai.Kaze;
import system.hai.MajanHai;
import system.hai.TehaiList;
import system.yaku.Yaku;
import client.system.ImageLoader;
import client.system.MajanHaiIDMapper;

public class TestAgari extends JFrame {
	private JPanel mainPanel;
	private MajanCanvas canvas;

	public static void main(String[] args) {
		TestAgari frame = new TestAgari();
	}

	public TestAgari() {
		this.setSize(900, 500);
		this.setLocation(100, 100);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		canvas = new MajanCanvas(this);
		add(canvas);
		setVisible(true);
	}

	public class MajanCanvas extends Canvas implements MouseListener,
			MouseMotionListener, KeyListener {
		private Map<Hai, Image> haiImageMap;
		private Map<Hai, Image> scaledHaiImageMap;
		private Image haiBackImage;

		private Image scaledHaiBackImage;
		private Image scaledDarkHaiBackImage;
		private Image reachImage;

		private Image imgBuffer;
		private Graphics gg;
		private GameThread gthread;

		private List<Hai> tehai;
		private Hai tsumohai;

		private List<HaiType> odora;
		private List<HaiType> udora;

		private Rule rule;
		private Field field;
		private Kaze jikaze;

		private int cursorPosition;

		private boolean checked;
		private boolean isTenpai;
		private boolean isNMentu1Janto;

		private class GameThread extends Thread {
			@Override
			public void run() {
				long preTime = System.currentTimeMillis();

				while (true) {
					repaint();

					if (!checked) {
						checked = true;
						check();
					}

					try {
						long dt = System.currentTimeMillis() - preTime;
						if (dt < 33) {
							Thread.sleep(33 - dt);
						}
					} catch (InterruptedException e) {
						// nothing to do
					}
					preTime = System.currentTimeMillis();
				}	
			}
		}

		public MajanCanvas(TestAgari frame) {
			this.haiImageMap = new HashMap<Hai, Image>();
			for (Hai hai : MajanHai.values()) {
				haiImageMap.put(hai,
						ImageLoader.load(MajanHaiIDMapper.getID(hai)));
			}
			this.scaledHaiImageMap = new HashMap<Hai, Image>();
			for (Hai hai : MajanHai.values()) {
				scaledHaiImageMap.put(hai,
						ImageLoader.loadScaled(MajanHaiIDMapper.getID(hai)));
			}

			addMouseListener(this);
			addMouseMotionListener(this);
			addKeyListener(this);

			rule = new Rule();
			field = new Field(rule, Kaze.TON);
			jikaze = Kaze.TON;

			tehai = new ArrayList<Hai>();
			tehai.add(MajanHai.ITI_MAN);
			tehai.add(MajanHai.YO_MAN);
			tehai.add(MajanHai.NANA_MAN);
			tehai.add(MajanHai.NI_PIN);
			tehai.add(MajanHai.GO_PIN);
			tehai.add(MajanHai.HATI_PIN);
			tehai.add(MajanHai.SAN_SOU);
			tehai.add(MajanHai.ROKU_SOU);
			tehai.add(MajanHai.KYU_SOU);
			tehai.add(MajanHai.NAN);
			tehai.add(MajanHai.SYA);
			tehai.add(MajanHai.PE);
			tehai.add(MajanHai.HAKU);

			tsumohai = MajanHai.AKA_GO_PIN;

			odora = new ArrayList<HaiType>(0);
			udora = new ArrayList<HaiType>(0);

			this.gthread = new GameThread();
			this.gthread.start();
		}

		@Override
		public void paint(Graphics g) {
			if (imgBuffer == null)
				imgBuffer = createImage(getWidth(), getHeight());
			if (gg == null)
				gg = imgBuffer.getGraphics();

			gg.clearRect(0, 0, getWidth(), getHeight());

			gg.setColor(Color.WHITE);
			gg.fillRect(0, 0, 900, 500);
			gg.setColor(Color.RED);

			gg.drawString("", 200, 200);

			int x = 40;
			int y = 40;

			for (int i = 0; i < 13; i++) {
				Image img = this.haiImageMap.get(tehai.get(i));
				gg.drawImage(img, x, y, this);
				x += 50;
			}

			x += 20;
			Image img = this.haiImageMap.get(tsumohai);
			gg.drawImage(img, x, y, this);

			int cx = 40 + cursorPosition * 50;
			int cy = y;
			if (cursorPosition == 13)
				cx += 20;
			gg.drawRect(cx, cy, 50, 70);
			gg.drawRect(cx + 1, cy + 1, 49, 69);
			gg.drawRect(cx + 2, cy + 2, 48, 68);

			if (isTenpai) {
				gg.drawString("手牌は聴牌しています。", 100, 200);
				TehaiList tlist = new TehaiList(tehai);
				List<Hai> list = AgariMethods.getMachiHaiList(tlist, false);
				int i = 0;
				for (Hai hai : list) {
					Image image = this.haiImageMap.get(hai);
					gg.drawImage(image, 100 + i, 300, this);
					i += 50;
				}
			}
			if (isNMentu1Janto) {
				gg.drawString("n面子1雀頭です。", 100, 250);
			}

			g.drawImage(imgBuffer, 0, 0, this);

		}

		public void check() {
			TehaiList tlist1 = new TehaiList(tehai);
			isTenpai = AgariMethods.isTenpai(tlist1, false);
			TehaiList tlist2 = new TehaiList(tehai);
			tlist2.add(tsumohai);
			isNMentu1Janto = AgariMethods.isNMentu1Janto(tlist2);

			if (AgariMethods.isAgari(tlist1, new HurohaiList(0), tsumohai,
					null, true, false, jikaze, field)) {
				AgariResult.Builder b = new AgariResult.Builder();
				AgariParam ag = new AgariParam(true, false, tsumohai, Kaze.TON,
						null);
				b.setAgariParam(ag);
				Rule rule = new Rule();
				Field f = new Field(rule, Kaze.TON);
				b.setField(f);
				b.setHojuKaze(null);
				b.setHurohaiList(new HurohaiList());
				b.setOpenDoraList(odora);
				b.setTehaiList(tlist1);
				b.setUraDoraList(udora);
				b.setYakuFlag(new HashSet<Yaku>(0));
				AgariResult result = b.build();
				System.out.println(result.toString());
			}
		}

		@Override
		public void update(Graphics g) {
			this.paint(g);
		}

		@Override
		public void mouseClicked(java.awt.event.MouseEvent e) {
		}

		@Override
		public void mouseEntered(java.awt.event.MouseEvent e) {
		}

		@Override
		public void mouseExited(java.awt.event.MouseEvent e) {
		}

		@Override
		public void mousePressed(java.awt.event.MouseEvent e) {
			int mx = e.getX();
			int my = e.getY();
		}

		@Override
		public void mouseReleased(java.awt.event.MouseEvent e) {
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyPressed(KeyEvent e) {
			int code = e.getKeyCode();

			int y = 0;

			switch (code) {
			// left
			case 37:
				cursorPosition--;
				break;
			// up
			case 38:
				y = 1;
				break;
			// right
			case 39:
				cursorPosition++;
				break;
			// down
			case 40:
				y = -1;
				break;
			// s
			case 83:
				Collections.sort(tehai);
				break;
			}

			if (y != 0) {
				checked = false;
				HaiType haiType = null;
				if (cursorPosition == 13) {
					haiType = tsumohai.type();
				} else {
					haiType = tehai.get(cursorPosition).type();
				}
				int id = haiType.id();
				while (true) {
					id += y;
					if (id == 0)
						id = 36;
					if (id == 37)
						id = 1;
					if (id == 10 || id == 20)
						id += y;
					haiType = HaiType.valueOf(id);
					if(Functions.sizeOf(haiType, tehai) == 4) continue;
					break;
				}
				Hai hai = MajanHai.valueOf(haiType, false);
				if (cursorPosition == 13) {
					tsumohai = hai;
				} else {
					tehai.set(cursorPosition, hai);
				}
			}

			if (cursorPosition < 0)
				cursorPosition = 13;

			if (cursorPosition > 13)
				cursorPosition = 0;
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
		}

	}

}
