package pages;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.Client;
import client.ClientOperator;
import client.MajanCanvas;
import client.MajanFrame;

public abstract class InputPage extends JPanel implements Page{
	private JLabel back;
	private Image image;
	private boolean isFinish;
	private MajanFrame frame;
	private Page page;
	private Client operator;
	
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

	protected void setFrame(MajanFrame frame){
		this.frame = frame;
	}
	
	@Override
	public void movePage(String order) {
		frame.setPage(order);
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
	@Override
	public Client getOperator() {
		return operator;
	}
	public void setPage(Page page){
		((ClientOperator)operator).setPage(page);
	}
	protected void setOperator(Client operator){
		this.operator = operator;
	}
	
	protected MajanFrame getFrame(){
		return frame;
	}
}
