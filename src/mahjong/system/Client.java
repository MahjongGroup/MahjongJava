package mahjong.system;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;



public class Client extends JPanel implements KeyListener, MouseListener, Runnable{
	private ArrayList<Hai> tehai = new ArrayList<Hai>();
	private int index = 0;
	private static final int Y_MARGIN = 200;
	private static final int X_MARGIN = 10;
	/**
	 * 自分の手牌,ドラなど基本的な情報全部Get
	 */
	public void getStart(){
		
	}
	public void sendRon(){}
	public void getRon(){}
	public void sendDiscard(){}
	public void getDiscard(){}
	public void sendNaki(){}
	public void getNaki(){}
	public void getTehai(){}
	public void sendTehai(){}
	public void getKawa(){}
	public void sendKawa(){}
	
	
	
	public void keyPressed(KeyEvent e){
		int size = tehai.size();
		switch(e.getKeyCode()){
		case KeyEvent.VK_ENTER:
			/*
			 * 何らかの処理
			 */
			break;
		case KeyEvent.VK_LEFT:
			index = (index + size + 1) % size;
			break;
		case KeyEvent.VK_RIGHT:
			index = (index + 1) % size;
			break;
		default:break;
		}
	}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
	public void mouseClicked(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	
	public void run(){
		while (true) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {

			}
		}
	}
	public void paintComponent(Graphics g){
		Image img = new ImageIcon("../images/pin_1.gif").getImage();
		g.drawImage(img, 0, 0, this);
	}
}
