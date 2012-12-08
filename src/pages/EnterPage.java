package pages;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import server.Server;
import server.Transporter;
import client.Client;
import client.ClientOperator;
import client.MajanFrame;

public class EnterPage extends InputPage implements Page{
	private List<Information> informations;
	private boolean isFinish;
	private JButton enter;
	private Client operator;

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
	
	private class Information extends JPanel{
		private State state;
		private JLabel tag;
		private JTextField content;
		public Information(State state){
			tag = new JLabel(state.name());
			content = new JTextField(state.name() + "を入力してください");
			content.addFocusListener(new FocusListener() {
				@Override
				public void focusLost(FocusEvent e) {
					if(Information.this.getContent().length() == 0)
						Information.this.setContent(Information.this.state.name() + "を入力してください");
				}
				@Override
				public void focusGained(FocusEvent e) {
					if (Information.this.getContent().compareTo(
							Information.this.state.name() + "を入力してください") == 0)
						Information.this.setContent("");
				}
			});
			content.addKeyListener(new KeyListener() {
				@Override public void keyTyped(KeyEvent e) {}
				@Override public void keyReleased(KeyEvent e) {}
				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode() == KeyEvent.VK_ENTER)
						movePage("wait");
				}
			});
			add(tag);
			add(content);
			this.state = state;
			setOpaque(false);
		}
		public String getTag(){
			return state.name();
		}
		public String getContent(){
			return content.getText();
		}
		public void setContent(String str){
			content.setText(str);
		}
	}
	
	private enum State{
		ID,ACCOUNT;
	}
	{
		informations = new ArrayList<Information>();
		for(State st:State.values()){
			informations.add(new Information(st));
		}
		isFinish = false;
		enter = new JButton("ENTER");
		for(Information i:informations){
			add(i);
		}
		add(enter);
		enter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(EnterPage.this.getFrame() != null)
					movePage("wait");
			}
		});
		new PaintThread().start();
	}
	@Override
	public void movePage(String order){
//		try{
//			GlobalGame.setPlayer(Integer.parseInt(informations.get(0).getContent()), informations.get(1).getContent());
//		}catch(NumberFormatException e){
//			JOptionPane.showMessageDialog(frame, "IDは半角数字のみで入力してください!!");
//			informations.get(0).setContent("IDを入力してください");
//			return;
//		}
		getFrame().setTitle("No." + informations.get(0).getContent() + ":"
				+ informations.get(1).getContent());
		operator.requestGame(Integer.parseInt(informations.get(0).getContent()));
		getFrame().setPage(order);
	}
	public EnterPage(MajanFrame frame){
		setFrame(frame);
	}
	public EnterPage(MajanFrame frame,Server tr){
		this(frame);
		operator = new ClientOperator(tr);
		((ClientOperator)operator).setFrame(frame);
		((Transporter)tr).setClient(operator);
		setOperator(operator);
		if(operator != null)
			((ClientOperator)getOperator()).setPage(this);
	}
	@Override
	public String getPageName() {
		// TODO Auto-generated method stub
		return "Enter";
	}
	
}
