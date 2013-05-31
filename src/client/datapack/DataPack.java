package client.datapack;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseListener;

import client.Constant;

abstract public class DataPack{
	private Image image;
	private static int width;
	private static int height;
	private MouseListener listener;
	private int version;

	private Graphics g;

	abstract public void createImage();

	static {
		width = Constant.WINDOW_HEIGHT;
		height = Constant.WINDOW_HEIGHT;
	}

	{
		version = 1;
	}

	/**
	 * このDataPackが呼び出されデータが書き換えられる時に呼び出すメソッド DataPackのversionを更新する
	 */
	protected void callPack() {
		version++;
	}
	
	public int getVersion() {
		return version;
	}

	/**
	 * このDataPackが終了する時に呼び出すメソッド DataPackのversionを初期化する
	 */
	protected void finishPack() {
		version = 1;
	}

	/**
	 * イメージを書き込むためのimage,バッファーを提供する
	 * 
	 * @param image
	 */
	public void setImage(Image image) {
		this.image = image;
		this.g = image.getGraphics();
	}

	/**
	 * DataPackの所持するimageを取得する
	 * 
	 * @return
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * DataPackの所持するMouseListenerを提供する
	 * 
	 * @param listener
	 */
	protected void setListener(MouseListener listener) {
		this.listener = listener;
	}

	/**
	 * DataPackの所持するMouseListenerを取得する
	 * @return
	 */
	protected MouseListener getListener() {
		return listener;
	}

	protected static int getWidth() {
		return width;
	}

	protected static int getHeight() {
		return height;
	}

	protected static void setWidth(int width) {
		DataPack.width = width;
	}

	protected static void setHeight(int height) {
		DataPack.height = height;
	}

	protected Graphics getGraphics() {
		return g;
	}

}