package pages;

import static client.Constant.BUTTON_CURVE;
import static client.Constant.BUTTON_HEIGHT;
import static client.Constant.BUTTON_WIDTH;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import client.Client;
import client.ClientOperator;
import client.MajanFrame;

public class StartPage extends GraphicalPage implements MouseListener{
	private boolean isFinish;
	private Image imgBuffer;
	private Graphics g2;
	{
		isFinish = false;
	}
	private enum State{
		ENTER,CONFIG;
	}
	private class PaintThread extends Thread{
		public void run(){
			while(!isFinish){
				repaint();
				try{
					sleep(10);
				}catch(InterruptedException e){}
			}
		}
	}
	public StartPage(MajanFrame frame,Client operator){
		this(frame);
		setOperator(operator);
		if(operator != null)
			((ClientOperator)getOperator()).setPage(this);
	}
	
	public StartPage(MajanFrame frame){
		setFrame(frame);
		addMouseListener(this);
//		new PaintThread().start();
	}
	public void paint(Graphics g){
		if(imgBuffer == null)
			imgBuffer = createImage(getWidth(),getHeight());
		if(g2 == null)
			g2 = imgBuffer.getGraphics();
		int x = (getWidth() - BUTTON_WIDTH) / 2;
		int y = (getHeight() - BUTTON_HEIGHT) / 2;
		for (State st : State.values()) {
			g2.setColor(Color.ORANGE);
			g2.fillRoundRect(x,
					y, BUTTON_WIDTH * 2,
					BUTTON_HEIGHT * 2, BUTTON_CURVE, BUTTON_CURVE);
			g2.setColor(Color.BLACK);
			g2.setFont(new Font("", Font.BOLD, 50));
			g2.drawString(st.name(), x, y + BUTTON_HEIGHT);
			y += BUTTON_HEIGHT * 2 + 20;
		}
		g.drawImage(imgBuffer, 0, 0, getWidth(), getHeight(), this);
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();
		int x = (getWidth() - BUTTON_WIDTH)/2;
		int y = (getHeight() - BUTTON_HEIGHT)/2;
		for(State st:State.values()){
//			System.out.println(mx + ":" + my + ":" + x + ":" + y);
			if(mx >= x
					&& mx <= x + BUTTON_WIDTH * 2
					&& my <= y + BUTTON_HEIGHT * 2
					&& my >= y){
				System.out.println("in area");
				dispatch(st);
				break;
			}
			y += BUTTON_HEIGHT * 2 + 20;
		}
		// TODO Auto-generated method stub
	}
	
	private void dispatch(State st){
		isFinish = true;
		if(st == State.ENTER)
			getFrame().setPage("WAIT");
		else
			getFrame().setPage(st.name());
//		switch(st){
//		case CONFIG:
//			frame.setPage("Config");
//			break;
//		case ENTER:
//			frame.setPage("Enter");
//			break;
//		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
	@Override
	public void movePage(String order) {
		getFrame().setPage(order);
	}
	@Override
	public String getPageName(){
		return "Start";
	}
	
}
