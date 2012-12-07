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
import server.Server;

public class MajanFrame extends JFrame{
	private JPanel mainPanel;
	private Page page;
	private Server server;
	
	public void setServer(Server server){
		this.server = server;
	}
	public static void main(String[] args) {
		MajanFrame frame = new MajanFrame();
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
		page = getPageFromString(order);
		if(page == null)
			return;
		page.setPreferredSize(getSize());
		mainPanel.add((Component)page);
		mainPanel.updateUI();
	}	

	public void setPage(String order,Image image){
		setPage(order);
		if(order.toLowerCase().compareTo("result") == 0)
			((ResultPage)mainPanel.getComponent(0)).setImage(image);
		mainPanel.updateUI();
	}
	
	private Page getPageFromString(String str){
		str = str.toLowerCase();
		if(str.compareTo("config") == 0)
			return new ConfigPage(this,page.getOperator());
		if(str.compareTo("enter") == 0)
			return new EnterPage(this,server);
		if(str.compareTo("start") == 0)
		{
			if(page != null)
				return new StartPage(this,page.getOperator());
			else
				return new StartPage(this);
		}
		if(str.compareTo("wait") == 0)
			return new WaitPage(this,page.getOperator());
		if(str.compareTo("game") == 0)
			return new MajanCanvas(this,page.getOperator());
		if(str.compareTo("result") == 0)
			return new ResultPage(this,page.getOperator());
		return null;
	}

}
