package client;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseListener;
import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MahjongFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2590847503467626884L;
	private MahjongCanvas canvas;
	
	private JPanel mainPanel;

	{
		this.setSize(Constant.WINDOW_WIDTH, Constant.WINDOW_HEIGHT);
		mainPanel = new JPanel();
		mainPanel.setSize(Constant.WINDOW_WIDTH, Constant.WINDOW_HEIGHT);
		canvas = new MahjongCanvas();
		canvas.setSize(Constant.WINDOW_WIDTH, Constant.WINDOW_HEIGHT);
		this.setLocation(Constant.WINDOW_POSITION_X, Constant.WINDOW_POSITION_Y);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainPanel.add(canvas);
		this.add(mainPanel);
		setVisible(true);
	}


	/**
	 * 持っているcanvasを取得する
	 * 
	 * @return
	 */
	public MahjongCanvas getCanvas() {
		return canvas;
	}

	/**
	 * 指定されたcanvasでcanvasを上書きする
	 * 
	 * @param canvas
	 */
	public void setCanvas(MahjongCanvas canvas) {
		this.canvas = canvas;
	}

	/**
	 * 空のコンストラクタ
	 */
	public MahjongFrame() {
	}

	/**
	 * canvasのバッファを取得する
	 */
	public Graphics getGraphics() {
		return canvas.getGraphics();
	}

	/**
	 * canvasの描画するimageを設定する
	 * @param image
	 */
	public void setImage(Image image) {
		canvas.setImage(image);
	}

	/**
	 * canvasで使用するMouseListenerを指定する
	 * @param mouseListener
	 */
	public void addListener(MouseListener mouseListener) {
		canvas.addMouseListener(mouseListener);
	}

	public void repaint() {
//		super.repaint();
		canvas.repaint();
	}
	
	public void setVersion(int version){
		canvas.setVersion(version);
	}
	
	public int getVersion(){
		return getCanvas().getVersion();
	}
	
}
