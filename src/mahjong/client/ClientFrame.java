package mahjong.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ClientFrame extends Frame{
	public static void main(String[] argv){
		new ClientFrame();
		
	}
	public ClientFrame(){
		Client client = new Client();
		this.setBounds(100, 100, 700, 500);
		client.setPreferredSize(new Dimension(500,500));
		this.setLayout(new BorderLayout());
		this.add(BorderLayout.CENTER,client);
		client.setBackground(Color.BLACK);
		client.startThread();
		this.setVisible(true);
	}
}
