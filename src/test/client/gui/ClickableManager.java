package test.client.gui;

import static test.client.gui.Constant.HAI_H;
import static test.client.gui.Constant.HAI_W;
import static test.client.gui.Constant.TEHAI_X;
import static test.client.gui.Constant.TEHAI_Y;
import static test.client.gui.Constant.TSUMO_W;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ClickableManager implements MouseListener {
	private GameManager manager;
	
	public void setGameManager(GameManager m) {
		this.manager = m;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();

		if (TEHAI_Y <= my && my <= TEHAI_Y + HAI_H && mx >= TEHAI_X) {
			final int index = (mx - TEHAI_X) / HAI_W;
			if (index < 13) {
				manager.onHaiPressed(index);
			}

			final int tsumox = (mx - TEHAI_X) - HAI_W * manager.tehai().size() - TSUMO_W;
			if (0 <= tsumox && tsumox <= HAI_W) {
				manager.onHaiPressed(13);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
