package system;

import java.util.ArrayList;
import java.util.List;

/**
 * 四人麻雀用,赤ありの麻雀牌を表すクラス。
 */
public class MajanHai implements Hai {
	public static Hai ITI_MAN = new MajanHai("一萬", HaiType.ITI_MAN, false, 0);
	public static Hai NI_MAN = new MajanHai("二萬", HaiType.NI_MAN, false, 1);
	public static Hai SAN_MAN = new MajanHai("三萬", HaiType.SAN_MAN, false, 2);
	public static Hai YO_MAN = new MajanHai("四萬", HaiType.YO_MAN, false, 3);
	public static Hai GO_MAN = new MajanHai("五萬", HaiType.GO_MAN, false, 4);
	public static Hai AKA_GO_MAN = new MajanHai("赤五萬", HaiType.GO_MAN, true, 5);
	public static Hai ROKU_MAN = new MajanHai("六萬", HaiType.ROKU_MAN, false, 6);
	public static Hai NANA_MAN = new MajanHai("七萬", HaiType.NANA_MAN, false, 7);
	public static Hai HATI_MAN = new MajanHai("八萬", HaiType.HATI_MAN, false, 8);
	public static Hai KYU_MAN = new MajanHai("九萬", HaiType.KYU_MAN, false, 9);

	public static Hai ITI_PIN = new MajanHai("一筒", HaiType.ITI_PIN, false, 10);
	public static Hai NI_PIN = new MajanHai("二筒", HaiType.NI_PIN, false, 11);
	public static Hai SAN_PIN = new MajanHai("三筒", HaiType.SAN_PIN, false, 12);
	public static Hai YO_PIN = new MajanHai("四筒", HaiType.YO_PIN, false, 13);
	public static Hai GO_PIN = new MajanHai("五筒", HaiType.GO_PIN, false, 14);
	public static Hai AKA_GO_PIN = new MajanHai("赤五筒", HaiType.GO_PIN, true, 15);
	public static Hai ROKU_PIN = new MajanHai("六筒", HaiType.ROKU_PIN, false, 16);
	public static Hai NANA_PIN = new MajanHai("七筒", HaiType.NANA_PIN, false, 17);
	public static Hai HATI_PIN = new MajanHai("八筒", HaiType.HATI_PIN, false, 18);
	public static Hai KYU_PIN = new MajanHai("九筒", HaiType.KYU_PIN, false, 19);

	public static Hai ITI_SOU = new MajanHai("一索", HaiType.ITI_SOU, false, 20);
	public static Hai NI_SOU = new MajanHai("二索", HaiType.NI_SOU, false, 21);
	public static Hai SAN_SOU = new MajanHai("三索", HaiType.SAN_SOU, false, 22);
	public static Hai YO_SOU = new MajanHai("四索", HaiType.YO_SOU, false, 23);
	public static Hai GO_SOU = new MajanHai("五索", HaiType.GO_SOU, false, 24);
	public static Hai AKA_GO_SOU = new MajanHai("赤五索", HaiType.GO_SOU, true, 25);
	public static Hai ROKU_SOU = new MajanHai("六索", HaiType.ROKU_SOU, false, 26);
	public static Hai NANA_SOU = new MajanHai("七索", HaiType.NANA_SOU, false, 27);
	public static Hai HATI_SOU = new MajanHai("八索", HaiType.HATI_SOU, false, 28);
	public static Hai KYU_SOU = new MajanHai("九索", HaiType.KYU_SOU, false, 29);

	public static Hai TON = new MajanHai("東", HaiType.TON, false, 30);
	public static Hai NAN = new MajanHai("南", HaiType.NAN, false, 31);
	public static Hai SYA = new MajanHai("西", HaiType.SYA, false, 32);
	public static Hai PE = new MajanHai("北", HaiType.PE, false, 33);
	public static Hai HAKU = new MajanHai("白", HaiType.HAKU, false, 34);
	public static Hai HATU = new MajanHai("撥", HaiType.HATU, false, 35);
	public static Hai TYUN = new MajanHai("中", HaiType.TYUN, false, 36);

	private static List<Hai> HAI_LIST = new ArrayList<Hai>(37);
	static {
		HAI_LIST.add(AKA_GO_MAN);
		HAI_LIST.add(ITI_MAN);
		HAI_LIST.add(NI_MAN);
		HAI_LIST.add(SAN_MAN);
		HAI_LIST.add(YO_MAN);
		HAI_LIST.add(GO_MAN);
		HAI_LIST.add(ROKU_MAN);
		HAI_LIST.add(NANA_MAN);
		HAI_LIST.add(HATI_MAN);
		HAI_LIST.add(KYU_MAN);

		HAI_LIST.add(AKA_GO_SOU);
		HAI_LIST.add(ITI_SOU);
		HAI_LIST.add(NI_SOU);
		HAI_LIST.add(SAN_SOU);
		HAI_LIST.add(YO_SOU);
		HAI_LIST.add(GO_SOU);
		HAI_LIST.add(ROKU_SOU);
		HAI_LIST.add(NANA_SOU);
		HAI_LIST.add(HATI_SOU);
		HAI_LIST.add(KYU_SOU);

		HAI_LIST.add(AKA_GO_PIN);
		HAI_LIST.add(ITI_PIN);
		HAI_LIST.add(NI_PIN);
		HAI_LIST.add(SAN_PIN);
		HAI_LIST.add(YO_PIN);
		HAI_LIST.add(GO_PIN);
		HAI_LIST.add(ROKU_PIN);
		HAI_LIST.add(NANA_PIN);
		HAI_LIST.add(HATI_PIN);
		HAI_LIST.add(KYU_PIN);

		HAI_LIST.add(TON);
		HAI_LIST.add(NAN);
		HAI_LIST.add(SYA);
		HAI_LIST.add(PE);
		HAI_LIST.add(HAKU);
		HAI_LIST.add(HATU);
		HAI_LIST.add(TYUN);
	}

	private final String notation;
	private final HaiType type;
	private final boolean aka;
	private final int ordinary;

	/**
	 * @throws NullPointerException typeがnullの場合．
	 */
	private MajanHai(String notation, HaiType type, boolean aka, int ordinary) {
		this.notation = notation;
		this.type = type;
		this.aka = aka;
		this.ordinary = ordinary;
		if(this.type == null) {
			throw new NullPointerException();
		}
	}

	@Override
	public String notation() {
		return notation;
	}

	@Override
	public HaiType type() {
		return type;
	}

	@Override
	public boolean aka() {
		return aka;
	}

	@Override
	public String toString() {
		return notation;
	}

	public static Hai valueOf(HaiType type, boolean aka) {
		for (Hai hai : values()) {
			if (hai.type() == type && hai.aka() == aka) {
				return hai;
			}
		}
		throw new IllegalArgumentException();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (aka ? 1231 : 1237);
		result = prime * result + type.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Hai))
			return false;
		Hai other = (Hai) obj;
		if (aka != other.aka())
			return false;
		if (type != other.type())
			return false;
		return true;
	}

	@Override
	public int compareTo(Hai hai) {
		return ordinary - hai.ordinal();
	}

	public static Hai[] values() {
		return HAI_LIST.toArray(new Hai[0]);
	}

	@Override
	public int ordinal() {
		return ordinary;
	}

	@Override
	public boolean isYaotyuhai() {
		return type.isYaotyuhai();
	}

	@Override
	public boolean isTyuntyanhai() {
		return type.isTyuntyanhai();
	}

	@Override
	public boolean isSuhai() {
		return type.isSuhai();
	}

	@Override
	public boolean isTsuhai() {
		return type.isTsuhai();
	}

	@Override
	public boolean isSangenhai() {
		return type.isSangenhai();
	}

	@Override
	public int number() {
		return type.number();
	}

	@Override
	public Kaze kaze() {
		return type.kaze();
	}

	@Override
	public SangenType sangenType() {
		return type.sangenType();
	}

	@Override
	public SuType suType() {
		return type.suType();
	}
	
	@Override
	public HaiType nextOfDora() {
		return type.nextOfDora();
	}

}
