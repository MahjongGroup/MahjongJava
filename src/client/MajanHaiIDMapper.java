package client;

import java.util.HashMap;
import java.util.Map;

import system.hai.Hai;
import system.hai.MajanHai;


public class MajanHaiIDMapper {
	private static final Map<Hai, Integer> ID_MAP = new HashMap<Hai, Integer>(1000);

	static {
		ID_MAP.put(MajanHai.AKA_GO_MAN, ImageID.aka_man_5);
		ID_MAP.put(MajanHai.ITI_MAN, ImageID.man_1);
		ID_MAP.put(MajanHai.NI_MAN, ImageID.man_2);
		ID_MAP.put(MajanHai.SAN_MAN, ImageID.man_3);
		ID_MAP.put(MajanHai.YO_MAN, ImageID.man_4);
		ID_MAP.put(MajanHai.GO_MAN, ImageID.man_5);
		ID_MAP.put(MajanHai.ROKU_MAN, ImageID.man_6);
		ID_MAP.put(MajanHai.NANA_MAN, ImageID.man_7);
		ID_MAP.put(MajanHai.HATI_MAN, ImageID.man_8);
		ID_MAP.put(MajanHai.KYU_MAN, ImageID.man_9);
 
		ID_MAP.put(MajanHai.AKA_GO_PIN, ImageID.aka_pin_5);
		ID_MAP.put(MajanHai.ITI_PIN, ImageID.pin_1);
		ID_MAP.put(MajanHai.NI_PIN, ImageID.pin_2);
		ID_MAP.put(MajanHai.SAN_PIN, ImageID.pin_3);
		ID_MAP.put(MajanHai.YO_PIN, ImageID.pin_4);
		ID_MAP.put(MajanHai.GO_PIN, ImageID.pin_5);
		ID_MAP.put(MajanHai.ROKU_PIN, ImageID.pin_6);
		ID_MAP.put(MajanHai.NANA_PIN, ImageID.pin_7);
		ID_MAP.put(MajanHai.HATI_PIN, ImageID.pin_8);
		ID_MAP.put(MajanHai.KYU_PIN, ImageID.pin_9);

		ID_MAP.put(MajanHai.AKA_GO_SOU, ImageID.aka_sou_5);
		ID_MAP.put(MajanHai.ITI_SOU, ImageID.sou_1);
		ID_MAP.put(MajanHai.NI_SOU, ImageID.sou_2);
		ID_MAP.put(MajanHai.SAN_SOU, ImageID.sou_3);
		ID_MAP.put(MajanHai.YO_SOU, ImageID.sou_4);
		ID_MAP.put(MajanHai.GO_SOU, ImageID.sou_5);
		ID_MAP.put(MajanHai.ROKU_SOU, ImageID.sou_6);
		ID_MAP.put(MajanHai.NANA_SOU, ImageID.sou_7);
		ID_MAP.put(MajanHai.HATI_SOU, ImageID.sou_8);
		ID_MAP.put(MajanHai.KYU_SOU, ImageID.sou_9);

		ID_MAP.put(MajanHai.TON, ImageID.kaze_1);
		ID_MAP.put(MajanHai.NAN, ImageID.kaze_2);
		ID_MAP.put(MajanHai.SYA, ImageID.kaze_3);
		ID_MAP.put(MajanHai.PE, ImageID.kaze_4);

		ID_MAP.put(MajanHai.HAKU, ImageID.sangen_1);
		ID_MAP.put(MajanHai.HATU, ImageID.sangen_2);
		ID_MAP.put(MajanHai.TYUN, ImageID.sangen_3);

	}
	
	public static int getID(Hai hai) {
		return ID_MAP.get(hai);
	}
}
