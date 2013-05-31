package client.datapack;

import static client.Constant.BUTTON_CURVE;
import static client.Constant.BUTTON_HEIGHT;
import static client.Constant.BUTTON_WIDTH;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import system.test.Area;
import system.test.Point;
import client.BackgroundSystemOfClient;

public class StartPack extends DataPack {
	private List<ButtonInfo> buttons;

	{
		buttons = new ArrayList<ButtonInfo>();
		int x = (getWidth() - BUTTON_WIDTH) / 2;
		int y = (getHeight() - BUTTON_HEIGHT) / 2;

		// ボタンの表示エリアの定義
		for (State st : State.values()) {
			buttons.add(new ButtonInfo(st, new Area(x, y, BUTTON_WIDTH * 2,
					BUTTON_HEIGHT * 2)));
			y += BUTTON_HEIGHT * 2 + 20;
		}
		// リスナーの決定
		setListener(new ListenerForStart());
	}

	private class ListenerForStart extends CommunicatableListener {
		private void dispatch(State st) {
			BackgroundSystemOfClient background = getBackground();
			switch (st) {
			case CONFIG:
				background.setMode(PackName.Config);
				break;
			case ENTER:
				background.setMode(PackName.Wait);
				break;
			default:

				break;
			}
		}

		// クリックされた時の処理
		@Override
		public void mouseClicked(MouseEvent e) {
			System.out.println("mouse clicked:Start");
			int mx = e.getX();
			int my = e.getY();
			for (ButtonInfo bi : buttons) {
				if (bi.getArea().hasPoint(mx, my)) {
					dispatch(bi.getState());
					break;
				}
			}
		}

	}

	private enum State {
		ENTER, CONFIG;
	}

	private class ButtonInfo {
		private State state;
		private Area area;

		public State getState() {
			return state;
		}

		public ButtonInfo(State state, Area area) {
			this.state = state;
			this.area = area;
		}

		public Area getArea() {
			return area;
		}

	}

	@Override
	public void createImage() {
		Graphics g = getGraphics();

		for (ButtonInfo bi : buttons) {
			g.setColor(Color.ORANGE);
			Area area = bi.getArea();
			Point start = area.getStartPoint();
			g.fillRoundRect(start.getX(), start.getY(), area.getWidth(),
					area.getHeight(), BUTTON_CURVE, BUTTON_CURVE);
			g.setColor(Color.BLACK);
			g.setFont(new Font("", Font.BOLD, 50));
			g.drawString(bi.getState().name(), start.getX(), start.getY()
					+ BUTTON_HEIGHT);
		}
	}

}
