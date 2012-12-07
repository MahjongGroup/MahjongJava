package pages;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import client.Client;
import client.ClientOperator;
import client.MajanFrame;
import static client.Constant.BUTTON_WIDTH;
import static client.Constant.BUTTON_HEIGHT;
import static client.Constant.BUTTON_CURVE;

public class StartPage extends GraphicalPage implements MouseListener{
	private boolean isFinish;
	private MajanFrame frame;
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
		this.frame = frame;
		addMouseListener(this);
//		new PaintThread().start();
	}
	public void paint(Graphics g){
		int x = (getWidth() - BUTTON_WIDTH) / 2;
		int y = (getHeight() - BUTTON_HEIGHT) / 2;
		for (State st : State.values()) {
			g.setColor(Color.ORANGE);
			g.fillRoundRect(x,
					y, BUTTON_WIDTH * 2,
					BUTTON_HEIGHT * 2, BUTTON_CURVE, BUTTON_CURVE);
			g.setColor(Color.BLACK);
			g.setFont(new Font("", Font.BOLD, 50));
			g.drawString(st.name(), x, y + BUTTON_HEIGHT);
			y += BUTTON_HEIGHT * 2 + 20;
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();
		int x = (getWidth() - BUTTON_WIDTH)/2;
		int y = (getHeight() - BUTTON_HEIGHT)/2;
		for(State st:State.values()){
			System.out.println(mx + ":" + my + ":" + x + ":" + y);
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
		frame.setPage(st.name());
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
		frame.setPage(order);
	}
	@Override
	public String getPageName(){
		return "Start";
	}
	
}
