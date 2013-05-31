package test.client.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MyFrame extends JFrame {
	private JPanel mainPanel;
	private GuiClient canvas;

	public MyFrame(GuiClient gui) {
		this.setSize(900, 500);
		this.setLocation(100, 100);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		add(gui);
		setVisible(true);
	}


}
