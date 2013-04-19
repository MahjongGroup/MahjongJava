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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import system.AgariMethods;
import system.Hai;
import system.HaiType;
import system.MajanHai;
import system.TehaiList;
import client.ImageLoader;
import client.MajanHaiIDMapper;

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

	public class MajanCanvas extends Canvas implements MouseListener, MouseMotionListener, KeyListener {
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
					
					if(!checked) {
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
				haiImageMap.put(hai, ImageLoader.load(MajanHaiIDMapper.getID(hai)));
			}
			this.scaledHaiImageMap = new HashMap<Hai, Image>();
			for (Hai hai : MajanHai.values()) {
				scaledHaiImageMap.put(hai, ImageLoader.loadScaled(MajanHaiIDMapper.getID(hai)));
			}

			addMouseListener(this);
			addMouseMotionListener(this);
			addKeyListener(this);

			tehai = new ArrayList<Hai>();
			tehai.add(MajanHai.AKA_GO_PIN);
			tehai.add(MajanHai.AKA_GO_PIN);
			tehai.add(MajanHai.AKA_GO_PIN);
			tehai.add(MajanHai.AKA_GO_PIN);
			tehai.add(MajanHai.AKA_GO_PIN);
			tehai.add(MajanHai.AKA_GO_PIN);
			tehai.add(MajanHai.AKA_GO_PIN);
			tehai.add(MajanHai.AKA_GO_PIN);
			tehai.add(MajanHai.AKA_GO_PIN);
			tehai.add(MajanHai.AKA_GO_PIN);
			tehai.add(MajanHai.AKA_GO_PIN);
			tehai.add(MajanHai.AKA_GO_PIN);
			tehai.add(MajanHai.AKA_GO_PIN);

			tsumohai = MajanHai.AKA_GO_MAN;

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

			if(isTenpai) {
				gg.drawString("手牌は聴牌しています。", 200, 200);
			}
			if(isNMentu1Janto){
				gg.drawString("n面子1雀頭です。", 200, 300);
			}
			
			
			g.drawImage(imgBuffer, 0, 0, this);

		}
		
		public void check() {
			TehaiList tlist1 = new TehaiList(tehai);
			isTenpai = AgariMethods.isTenpai(tlist1, false);
			TehaiList tlist2 = new TehaiList(tehai);
			tlist2.add(tsumohai);
			isNMentu1Janto = AgariMethods.isNMentu1Janto(tlist2);
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
			System.out.println(code);

			int y = 0;

			switch (code) {
			// left
			case 37:
				cursorPosition--;
				break;
			//up
			case 38:
				y = 1;
				break;
			// right
			case 39:
				cursorPosition++;
				break;
			//down
			case 40:
				y = -1;				
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
				id += y;
				if(id == 0) id = 36;
				if(id == 37) id = 1;
				if(id == 10 || id == 20) id += y;
				haiType = HaiType.valueOf(id);
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
