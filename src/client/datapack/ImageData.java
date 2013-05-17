package client.datapack;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

import system.hai.Hai;
import system.hai.MajanHai;
import client.system.ImageID;
import client.system.ImageLoader;
import client.system.MajanHaiIDMapper;

public class ImageData {
	private static final Image haiBackImage;
	private static final Map<Hai, Image> haiImageMap;
	private static final Image reachImage;
	private static final Image scaledDarkHaiBackImage;
	private static final Image scaledHaiBackImage;
	private static final Map<Hai, Image> scaledHaiImageMap;
	private static final ImageData instance;

	static{
		instance = new ImageData();
		haiImageMap = new HashMap<Hai, Image>();

		for (Hai hai : MajanHai.values()) {
			haiImageMap.put(hai, ImageLoader.load(MajanHaiIDMapper.getID(hai)));
		}
		scaledHaiImageMap = new HashMap<Hai, Image>();
		for (Hai hai : MajanHai.values()) {
			scaledHaiImageMap.put(hai,
					ImageLoader.loadScaled(MajanHaiIDMapper.getID(hai)));
		}
		haiBackImage = ImageLoader.load(ImageID.hai_back);
		scaledHaiBackImage = ImageLoader.loadScaled(ImageID.hai_back);
		scaledDarkHaiBackImage = ImageLoader
				.loadScaled(ImageID.hai_darkback);
		reachImage = Toolkit.getDefaultToolkit().createImage(
				"image/reach.png");
	}
		
	
	private ImageData(){
	}
	public static ImageData getInstance(){
		return instance;
	}

	public Image getHaiBackImage() {
		return haiBackImage;
	}

	public Map<Hai, Image> getHaiImageMap() {
		return haiImageMap;
	}

	public Image getReachImage() {
		return reachImage;
	}

	public Image getScaledDarkHaiBackImage() {
		return scaledDarkHaiBackImage;
	}

	public Image getScaledHaiBackImage() {
		return scaledHaiBackImage;
	}

	public Map<Hai, Image> getScaledHaiImageMap() {
		return scaledHaiImageMap;
	}

}
