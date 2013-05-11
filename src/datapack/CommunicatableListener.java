package datapack;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import client.BackgroundSystemOfClient;

abstract public class CommunicatableListener implements MouseListener {
	private static BackgroundSystemOfClient background;

	public static void setBackground(BackgroundSystemOfClient background) {
		CommunicatableListener.background = background;
	}

	protected BackgroundSystemOfClient getBackground(){
		return background;
	}
	
	
	
	@Override
	abstract public void mouseClicked(MouseEvent e);

	@Override
	public void mousePressed(MouseEvent e) {
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}
