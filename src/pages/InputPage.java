package pages;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

class InputPage extends JPanel implements Page{
	private JLabel back;
	private Image image;
	private boolean isFinish;
	{
//		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		ImageIcon tmpIcon = new ImageIcon("image/background.jpg");
		image = new ImageIcon("image/background.jpg").getImage();
		back = new JLabel(tmpIcon);
		isFinish = false;
		startThread();
	}
	public void startThread(){
		new PaintThread().start();
	}
	public void kill(){
		isFinish = true;
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

	protected JLabel getBack(){
		return back;
	}
	public void setPreferredSize(Dimension d){
		super.setPreferredSize(d);
	}
	public void paintComponent(Graphics g){
		g.drawImage(image,0,0,getWidth(),getHeight(),this);
	}
	public void setImage(Image image){
		this.image = image;
	}
	
}
