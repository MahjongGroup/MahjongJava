package pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

import client.MajanFrame;

public class WaitPage extends GraphicalPage implements Page{
	private MajanFrame frame;
	private Image imgBuffer;
	private Graphics g2;
	private int count;
	private static int circleCount;
	private static double omega;
	private static int smallCircleRect;
	private static int largeCircleRect;
	private static double e;
	private static Color selectedColor;
	
	
	static{
		circleCount = 10;
		smallCircleRect = 10;
		largeCircleRect = 20;
		omega = Math.PI / (circleCount / 2);
		selectedColor = new Color(255,150,67);
		e = Math.E * 0.8;
	}
	{
		count = 0;
	}
	public WaitPage(MajanFrame frame){
		this.frame = frame;
	}
	
	public void moveGame(){
		kill();
		frame.setPage("game");
	}
	
	public void paint(Graphics g){
		if(imgBuffer == null)
			imgBuffer = createImage(getWidth(),getHeight());
		if(g2 == null)
			g2 = imgBuffer.getGraphics();
		super.paint(g2);
		int restRed = 255 - selectedColor.getRed();
		int restGreen = 255 - selectedColor.getGreen();
		int restBlue = 255 - selectedColor.getBlue();
		for(int i = 0;i < circleCount;i++){
			double rate = (double)((i - count / 10 % circleCount + circleCount)
					% circleCount)
					/ circleCount;
			int red = (int)(255 - restRed * rate);
			int green = (int)(255 - restGreen * rate);
			int blue = (int)(255 - restBlue * rate);

			float[] hsb = Color.RGBtoHSB(red,green,blue, null);
			Color tmpColor = new Color(red,green,blue);
			tmpColor = new Color(red,
					green,
					blue,
					(int) (tmpColor.getAlpha() * ((Math.log(e * hsb[1]) < 0)?0:Math.log(e * hsb[1])/Math.log(e))));
			g2.setColor(tmpColor);
			g2.fillOval(
					getWidth()/2 - smallCircleRect/2 - (int)(largeCircleRect * Math.sin(omega * i)),
					getHeight()/2 - smallCircleRect/2 - 40 + (int)(largeCircleRect * Math.cos(omega * i)),
					smallCircleRect,
					smallCircleRect);
		}
		
		g2.setFont(new Font("",Font.PLAIN,20));
		g2.drawString(
				"Please Wait...",
				getWidth()/2 - 60,
				getHeight()/2 - 40);
		count++;
		g.drawImage(imgBuffer, 0, 0, this);
	}
}
