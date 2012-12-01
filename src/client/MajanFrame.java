package client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;

import pages.ConfigPage;
import pages.EnterPage;
import pages.Page;
import pages.ResultPage;
import pages.StartPage;
import pages.WaitPage;

public class MajanFrame extends JFrame{
	private JPanel mainPanel;
	
	
	public static void main(String[] args) {
		MajanFrame frame = new MajanFrame();
		frame.setVisible(true);
		frame.setPage("start");
	}
	public MajanFrame() {
		this.setSize(Constant.WINDOW_WIDTH, Constant.WINDOW_HEIGHT);
		mainPanel = new JPanel();
		mainPanel.setPreferredSize(getSize());
		add(mainPanel,BorderLayout.CENTER);
		this.setLocation(Constant.WINDOW_POSITION_X, Constant.WINDOW_POSITION_Y);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPage("start");
		setVisible(true);
	}

	
	public void setPage(String order){
		mainPanel.removeAll();
		Page tmp = getPageFromString(order);
		if(tmp == null)
			return;
		tmp.setPreferredSize(getSize());
		mainPanel.add((Component)tmp);
		mainPanel.updateUI();
	}	

	public void setPage(String order,Image image){
		setPage(order);
		if(order.toLowerCase().compareTo("result") == 0)
			((ResultPage)mainPanel.getComponent(0)).setImage(image);
	}
	
	private Page getPageFromString(String str){
		str = str.toLowerCase();
		if(str.compareTo("config") == 0)
			return new ConfigPage(this);
		if(str.compareTo("enter") == 0)
			return new EnterPage(this);
		if(str.compareTo("start") == 0)
			return new StartPage(this);
		if(str.compareTo("wait") == 0)
			return new WaitPage(this);
		if(str.compareTo("game") == 0)
			return new MajanCanvas(this);
		if(str.compareTo("result") == 0)
			return new ResultPage(this);
		return null;
	}

}
