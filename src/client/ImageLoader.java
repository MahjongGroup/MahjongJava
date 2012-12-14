package client;

import static client.Constant.SCALED_HAI_HEIGHT;
import static client.Constant.SCALED_HAI_WIDTH;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

public class ImageLoader {
	private static final Map<Integer, String> IMAGE_MAP = new HashMap<Integer, String>(1000);
	static {
		IMAGE_MAP.put(ImageID.aka_man_5, "aka_man_5");
		IMAGE_MAP.put(ImageID.man_1, "man_1");
		IMAGE_MAP.put(ImageID.man_2, "man_2");
		IMAGE_MAP.put(ImageID.man_3, "man_3");
		IMAGE_MAP.put(ImageID.man_4, "man_4");
		IMAGE_MAP.put(ImageID.man_5, "man_5");
		IMAGE_MAP.put(ImageID.man_6, "man_6");
		IMAGE_MAP.put(ImageID.man_7, "man_7");
		IMAGE_MAP.put(ImageID.man_8, "man_8");
		IMAGE_MAP.put(ImageID.man_9, "man_9");

		IMAGE_MAP.put(ImageID.aka_pin_5, "aka_pin_5");
		IMAGE_MAP.put(ImageID.pin_1, "pin_1");
		IMAGE_MAP.put(ImageID.pin_2, "pin_2");
		IMAGE_MAP.put(ImageID.pin_3, "pin_3");
		IMAGE_MAP.put(ImageID.pin_4, "pin_4");
		IMAGE_MAP.put(ImageID.pin_5, "pin_5");
		IMAGE_MAP.put(ImageID.pin_6, "pin_6");
		IMAGE_MAP.put(ImageID.pin_7, "pin_7");
		IMAGE_MAP.put(ImageID.pin_8, "pin_8");
		IMAGE_MAP.put(ImageID.pin_9, "pin_9");

		IMAGE_MAP.put(ImageID.aka_sou_5, "aka_sou_5");
		IMAGE_MAP.put(ImageID.sou_1, "sou_1");
		IMAGE_MAP.put(ImageID.sou_2, "sou_2");
		IMAGE_MAP.put(ImageID.sou_3, "sou_3");
		IMAGE_MAP.put(ImageID.sou_4, "sou_4");
		IMAGE_MAP.put(ImageID.sou_5, "sou_5");
		IMAGE_MAP.put(ImageID.sou_6, "sou_6");
		IMAGE_MAP.put(ImageID.sou_7, "sou_7");
		IMAGE_MAP.put(ImageID.sou_8, "sou_8");
		IMAGE_MAP.put(ImageID.sou_9, "sou_9");

		IMAGE_MAP.put(ImageID.kaze_1, "kaze_1");
		IMAGE_MAP.put(ImageID.kaze_2, "kaze_2");
		IMAGE_MAP.put(ImageID.kaze_3, "kaze_3");
		IMAGE_MAP.put(ImageID.kaze_4, "kaze_4");

		IMAGE_MAP.put(ImageID.sangen_1, "sangen_1");
		IMAGE_MAP.put(ImageID.sangen_2, "sangen_2");
		IMAGE_MAP.put(ImageID.sangen_3, "sangen_3");

		IMAGE_MAP.put(ImageID.hai_back, "back");
		IMAGE_MAP.put(ImageID.hai_darkback, "darkback");

	}
	
	public static ImageIcon loadIcon(int id){
		ImageIcon tmpIcon = new ImageIcon("res/" + IMAGE_MAP.get(id) + ".gif");
		return new ImageIcon("res/" + IMAGE_MAP.get(id) + ".gif");
	}
	
	public static Image load(int id) {
		return Toolkit.getDefaultToolkit().createImage("res/"+IMAGE_MAP.get(id) + ".gif");
	}
	
	public static Image loadScaled(int id){
		return load(id).getScaledInstance(SCALED_HAI_WIDTH, SCALED_HAI_HEIGHT, Image.SCALE_DEFAULT);
	}
	
}
