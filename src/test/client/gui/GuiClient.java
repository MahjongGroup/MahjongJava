package test.client.gui;

import static test.client.gui.Constant.HAI_H;
import static test.client.gui.Constant.HAI_W;
import static test.client.gui.Constant.TEHAI_X;
import static test.client.gui.Constant.TEHAI_Y;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import system.hai.Hai;

public class GuiClient extends Canvas {
	private Image imgBuffer;
	private Graphics gg;

	private GameThread gthread;

	private GameManager manager;

	private class GameThread extends Thread {
		@Override
		public void run() {
			long preTime = System.currentTimeMillis();

			while (true) {
				repaint();
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

	public GuiClient() {
		this.gthread = new GameThread();
		this.gthread.start();
	}

	public void setGameManager(GameManager m) {
		this.manager = m;
	}

	@Override
	public void paint(Graphics g) {
		if (imgBuffer == null)
			imgBuffer = createImage(getWidth(), getHeight());
		if (gg == null) {
			gg = imgBuffer.getGraphics();
		}

		gg.clearRect(0, 0, getWidth(), getHeight());

		gg.setColor(Color.WHITE);
		gg.fillRect(0, 0, 900, 500);

		if (manager.tehai() != null) {
			int i = 0;
			for (Hai hai : manager.tehai()) {
				drawHai(TEHAI_X + i * HAI_W, TEHAI_Y, hai);
				i++;
			}
			if (manager.tehai() != null)
				drawHai(TEHAI_X + i * HAI_W, TEHAI_Y, manager.tsumohai());
		}

		g.drawImage(imgBuffer, 0, 0, this);

	}

	public void drawHai(int x, int y, Hai hai) {
		gg.drawImage(ImageLoader.getHaiFront(), x, y, HAI_W, HAI_H, this);
		gg.drawImage(ImageLoader.getImage(hai), x, y, HAI_W, HAI_H, this);
	}

	@Override
	public void update(Graphics g) {
		this.paint(g);
	}

}
