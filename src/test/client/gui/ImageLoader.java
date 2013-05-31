package test.client.gui;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

import system.hai.Hai;
import system.hai.MajanHai;
import static test.client.gui.Constant.HAI_H;
import static test.client.gui.Constant.HAI_W;
import static test.client.gui.Constant.TEHAI_X;
import static test.client.gui.Constant.TEHAI_Y;

public class ImageLoader {
	private static final Map<Hai, Image> map;
	private static final Image HAI_FRONT;

	static {
		map = new HashMap<Hai, Image>();
		
		for (Hai hai : MajanHai.values()) {
			map.put(hai, load(hai.notation()));
		}
		
		HAI_FRONT = load("HAI_FRONT");
	}

	public static Image load(String name) {
		return Toolkit.getDefaultToolkit().createImage("res/" + name + ".png").getScaledInstance(HAI_W, HAI_H, Image.SCALE_SMOOTH);
	}

	public static Image getImage(Hai hai) {
		return map.get(hai);
	}
	
	public static Image getHaiFront() {
		return HAI_FRONT;	
	}
}
