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
import server.MahjongServer;
import server.Server;
import server.Transporter;

public class MahjongFrame extends JFrame{
	private JPanel mainPanel;
	private Client operator;
	private Page page;
	private Server server;
	private ClientInfo info;
	
	public void setInfo(ClientInfo info){
		this.info = info;
	}
	
	public ClientInfo getInfo(){
		if(info == null){
			info = new ClientInfo();
		}
		return info;
	}
	
	public Client getOperator(){
		if(operator == null){
			operator = new ClientOperator(server);
			((ClientOperator)operator).setFrame(this);
			((Transporter)server).setClient(operator);
		}
		return operator;
	}
	
	public void setServer(Server server){
		this.server = server;
	}
	public static void main(String[] args) {
		MahjongServer.main(args);
//		MajanFrame frame = new MajanFrame();
//		frame.setPage("start");
	}
	public MahjongFrame() {
		this.setSize(Constant.WINDOW_WIDTH, Constant.WINDOW_HEIGHT);
		mainPanel = new JPanel();
		mainPanel.setPreferredSize(getSize());
		add(mainPanel,BorderLayout.CENTER);
		this.setLocation(Constant.WINDOW_POSITION_X, Constant.WINDOW_POSITION_Y);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setPage("start");
		setVisible(true);
	}

	
	public void setPage(String order){
		mainPanel.removeAll();
		page = getPageFromString(order);
		if(page == null)
			return;
		page.setPreferredSize(getSize());
		((ClientOperator)getOperator()).setFrame(this);
		((ClientOperator)getOperator()).setPage(page);
		mainPanel.add((Component)page);
		mainPanel.updateUI();
	}

	public void setPage(String order,Image image){
		setPage(order);
		mainPanel.updateUI();
	}
	
	private Page getPageFromString(String str){
		str = str.toLowerCase();
		System.out.println(str);
		if(str.compareTo("config") == 0)
			return new ConfigPage(this);
		if(str.compareTo("enter") == 0)
			return new EnterPage(this,server);
		if(str.compareTo("start") == 0)
		{
			if(page != null)
				return new StartPage(this,operator);
			else
				return new StartPage(this);
		}
		if(str.compareTo("wait") == 0)
			return new WaitPage(this);
		if(str.compareTo("game") == 0)
			return new MajanCanvas(this);
		if(str.compareTo("result") == 0)
			return new ResultPage(this);
		return null;
	}

}
