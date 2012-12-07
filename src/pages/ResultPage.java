package pages;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import system.KyokuResult;

import client.Client;
import client.ClientOperator;
import client.MajanFrame;

public class ResultPage extends InputPage implements Page{
	private MajanFrame frame;
	private Image imgBuffer;
	private Graphics g2;
	private KyokuResult result;
	public ResultPage(MajanFrame frame,Client operator){
		this(frame);
		setOperator(operator);
		if(operator != null)
			((ClientOperator)getOperator()).setPage(this);
	}
	
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
				//TODO 次への処理
			}
		});
	}
	public void paint(Graphics g){
		if(imgBuffer == null)
			imgBuffer = createImage(getWidth(),getHeight());
		if(g2 == null)
			g2 = imgBuffer.getGraphics();
		super.paint(g2);
		g2.drawString("Result", getWidth()/2, getHeight()/2);
		g2.setColor(new Color(0,0,0,100));
		g2.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(imgBuffer, 0, 0,this);
	}

	@Override
	public String getPageName() {
		// TODO Auto-generated method stub
		return "Result";
	}
	
	public void setResult(KyokuResult result){
		this.result = result;
	}
}