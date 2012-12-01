package pages;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import client.MajanFrame;

public class ResultPage extends InputPage implements Page{
	private MajanFrame frame;
	public ResultPage(MajanFrame frame){
		this.frame = frame;
		addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {
				ResultPage.this.frame.setPage("game");
			}
		});
	}

}
