package pages;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

import client.Client;
import client.ClientOperator;
import client.MahjongFrame;

public abstract class GraphicalPage extends Canvas implements Page{
	private Image image;
	private boolean isFinish;
	private Client operator;
	private MahjongFrame frame;
	{
		image = new ImageIcon("image/background.jpg").getImage();
		isFinish = false;
		startThread();
	}
	
	public void setFrame(MahjongFrame frame){
		this.frame = frame;
	}
	
	public MahjongFrame getFrame(){
		return frame;
	}
	
	public Client getOperator(){
		return operator;
	}
	public void setPage(Page page){
		((ClientOperator)operator).setPage(page);
	}
	protected void setOperator(Client operator){
		this.operator = operator;
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
	public void finish(){
		isFinish = true;
	}
	public void setPreferredSize(Dimension d){
		super.setPreferredSize(d);
	}
	public void paint(Graphics g){
		g.drawImage(image,0,0,getWidth(),getHeight(),this);
	}
	public void startThread(){
		new PaintThread().start();
	}
}
