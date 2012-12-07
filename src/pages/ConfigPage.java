package pages;

import static client.Constant.BUTTON_HEIGHT;
import static client.Constant.BUTTON_WIDTH;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import system.Rule;
import client.Client;
import client.ClientOperator;
import client.MajanFrame;

public class ConfigPage extends GraphicalPage implements MouseListener, Page{
	private Rule rule;
	private List<Config> configs;
	private boolean isFinish;
	private MajanFrame frame;

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
	
	private class Config{
		private State state;
		private boolean flag;
		public Config(State state){
			this.state = state;
			flag = false;
		}
		public void toggleFlag(){
			flag = !flag;
		}
		public String getExplain(){
			return state.explain;
		}
		public String getSwitch(){
			if(flag)
				return "ON";
			else
				return "OFF";
		}
	}
	
	private enum State{
		AKA("赤ドラ有り"),
		KOKUSI("国士13面待ちはダブル役満"),
		SUAN("四暗刻単騎待ちはダブル役満"),
		SUSI("大四喜はダブル役満"),
		KAZE("半荘戦まで行う"),
		SHANYU("西入する"),
		HAKO("とびで終了する");
		private String explain;
		private State(String exp){
			explain = exp;
		}
	}
	{
		configs = new ArrayList<Config>();
		for(State st:State.values()){
			configs.add(new Config(st));
		}
		isFinish = false;
	}
	
	public ConfigPage(MajanFrame frame){
		this.frame = frame;
		addMouseListener(this);
		new PaintThread().start();
	}
	public ConfigPage(MajanFrame frame,Client operator){
		this(frame);
		setOperator(operator);
		if(operator != null)
			((ClientOperator)getOperator()).setPage(this);
	}
	
	public void paint(Graphics g){
		g.clearRect(0, 0, getWidth(), getHeight());
		int x = getWidth() * 2 / 5;
		int y = getHeight()/5;

		g.setFont(new Font("",Font.BOLD,40));
		g.drawString("C O N F I G", x, y - BUTTON_HEIGHT * 3 / 2);
		
		int maxLength = 0;
		for(Config c:configs){
			int tmp = c.getExplain().length();
			if(tmp > maxLength)
				maxLength = tmp;
		}
		for(Config c:configs){
			g.setFont(new Font("",Font.BOLD,20));
			if(c.flag)
				g.setColor(Color.RED);
			else
				g.setColor(Color.BLUE);
			g.fillRect(x + maxLength * 22 + 14, y, BUTTON_WIDTH, BUTTON_HEIGHT);
			g.setColor(Color.BLACK);
			g.drawString(c.getSwitch(), x + maxLength * 22 + 45, y + BUTTON_HEIGHT/2 + 7);
			g.setColor(Color.BLACK);
			g.drawString(c.getExplain(), x, y + BUTTON_HEIGHT/2 + 7);
			y += BUTTON_HEIGHT + 20;
		}
		g.setColor(Color.GRAY);
		g.fillRect((getWidth() - BUTTON_WIDTH)/2, y, BUTTON_WIDTH, BUTTON_HEIGHT);
		g.setColor(Color.BLACK);
		g.drawString("SAVE", (getWidth() - BUTTON_WIDTH)/2 + 20, y + BUTTON_HEIGHT/2);
	}

	private boolean isInArea(int targetX,int targetY,int x,int y,int width,int height){
		return targetX <= x + width
				&& targetX >= x
				&& targetY >= y
				&& targetY <= y + height;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		int my = e.getY();
		int mx = e.getX();
		int y = getHeight() / 5;
		int x = getWidth() * 2 / 5;
		int maxLength = 0;
		for(Config c:configs){
			int tmp = c.getExplain().length();
			if(tmp > maxLength)
				maxLength = tmp;
		}
		for(Config c:configs){
			if(isInArea(mx, my, x - 5, y,
					maxLength * 22 + 19 + BUTTON_WIDTH, BUTTON_HEIGHT)){
				c.toggleFlag();
				return;
			}
			y += BUTTON_HEIGHT + 20;
		}
		if(isInArea(mx,my,(getWidth() - BUTTON_WIDTH)/2,y,
				BUTTON_WIDTH,BUTTON_HEIGHT))
			frame.setPage("start");
	}

	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	private void saveConfig(){}
	@Override
	public void setPreferredSize(Dimension d) {
		super.setPreferredSize(d);
	}

	@Override
	public void movePage(String order) {
		frame.setPage(order);
	}

	@Override
	public Client getOperator() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getPageName(){
		return "Config";
	}
}
