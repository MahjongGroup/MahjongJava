package mahjong.client;


import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;



public class Client extends Canvas implements KeyListener, MouseListener, Runnable{
	private ArrayList<Hai> tehai = new ArrayList<Hai>();
	private int index = 0;
	private static final int Y_MARGIN = 20;
	private static final int X_MARGIN = 10;
	private ArrayList<Image> images = new ArrayList<Image>();
	/**
	 * 自分の手牌,ドラなど基本的な情報全部Get
	 */
	
	public Client(){
		tehai.add(new Hai(HaiType.HAKU,false));
		tehai.add(new Hai(HaiType.HATSU,false));
		tehai.add(new Hai(HaiType.KUPIN,false));
		tehai.add(new Hai(HaiType.KUMAN,false));
		tehai.add(new Hai(HaiType.NAN,false));
		tehai.add(new Hai(HaiType.ITIPIN,false));
		tehai.add(new Hai(HaiType.HAKU,false));
		tehai.add(new Hai(HaiType.HATSU,false));
		tehai.add(new Hai(HaiType.KUPIN,false));
		tehai.add(new Hai(HaiType.KUMAN,false));
		tehai.add(new Hai(HaiType.NAN,false));
		tehai.add(new Hai(HaiType.ITIPIN,false));
		tehai.add(new Hai(HaiType.ITIPIN,false));
		
	}
	
	public void getStart(){}
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

	static Image image = null;
	
	public void run(){
		image = Toolkit.getDefaultToolkit().createImage("res\\白.gif");
		
		while (true) {
//			images.clear();
//			for(Hai h:tehai){
//				images.add(getImageObject(h));
//			}
			repaint();
			try{
				Thread.sleep(1);
			}
			catch(InterruptedException e){}
		}
	}
	public Image getImageObject(Hai hai){
		String s = "res\\" + hai.getType().toString().replace('\t', '\0') + ".gif";
		System.out.println(s);
		return Toolkit.getDefaultToolkit().createImage(s);
	}

	
	@Override
	public void update(Graphics g) {
		paint(g);
	}
	
	@Override public void paint(Graphics g){
		System.out.println("a");
//		g.setColor(Color.white);
//		g.fillRect(0, 0, 100, 100);
//		image = Toolkit.getDefaultToolkit().createImage("C:\\Users\\USER\\Desktop\\workspace\\MahjongJava\\bin\\mahjong\\images\\test.gif");
//		image = Toolkit.getDefaultToolkit().createImage("../images/test.gif");
//		g.setColor(Color.red);
//		g.drawLine(10, 10, 20, 20);
//		g.drawImage(image, 10, 10, this);
		for(int i = 0;i < tehai.size();i++){
			Hai h = tehai.get(i);
			if(g.drawImage(getImageObject(h),
					X_MARGIN + i * 30, Y_MARGIN, this))
				System.out.println("a");
		}
//		g.drawImage(new ImageIcon("../images/sonic-animated.gif").getImage(), 0, 0, this);
	}
	

	
	public void startThread(){
		Thread thread = new Thread();
		thread.start();
	}
}
