package client;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;

public class MahjongCanvas extends Canvas {
	private int version;
	private Image image;

	{
		version = 0;
	}
	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	public void setImage(Image image){
		this.image = image;
	}
	
	public void paint(Graphics g){
		super.paint(g);
		g.clearRect(0, 0, getWidth(), getHeight());
		g.drawImage(image,0,0,getWidth(),getHeight(),this);
	}
	
}
