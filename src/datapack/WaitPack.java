package datapack;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import client.BackgroundSystemOfClient;

public class WaitPack extends DataPack {

	{
		setListener(new ListenerForWait());
	}
	
	private class ListenerForWait extends CommunicatableListener {
		// クリックされた時の処理
		@Override
		public void mouseClicked(MouseEvent e) {
			BackgroundSystemOfClient background = getBackground();
			background.connectServer();
			background.setMode(PackName.Game);
		}
	}
	
	@Override
	public void createImage() {
		// TODO Auto-generated method stub
		Graphics g = getGraphics();
		g.clearRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.red);
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		g.drawString("Please Wait", getWidth()/2, getHeight()/2);
	}
}
