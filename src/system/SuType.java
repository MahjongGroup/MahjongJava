package system;

import java.util.ArrayList;
import java.util.List;

/**
 * 数牌の種類を表す列挙型。萬子、筒子、索子の3種類がある。それぞれ0,1,2のIDを持つ。
 * 
 * @author kohei
 * 
 */
public enum SuType {
	MAN("萬子", 0), PIN("筒子", 1), SOU("索子", 2);

	private final String notation;
	private final int id;

	private SuType(String notation, int id) {
		this.notation = notation;
		this.id = id;
	}

	/**
	 * この数牌の種類を表す文字列を日本語で返す。
	 * 
	 * @return この数牌の種類を表す文字列(日本語)
	 */
	public String notation() {
		return notation;
	}

	/**
	 * この数牌の種類のIDを返す。
	 * 
	 * @return この数牌の種類のID
	 */
	public int id() {
		return id;
	}

	@Override
	public String toString() {
		return notation;
	}

	private static final List<SuType> SUHAI_LIST = new ArrayList<SuType>(3);

	static {
		SUHAI_LIST.add(0, MAN);
		SUHAI_LIST.add(1, PIN);
		SUHAI_LIST.add(2, SOU);
	}

	/**
	 * 指定されたIDが不正でないならtrueを返す。
	 * 
	 * @param id 不正でないか確かめるID
	 * @return 指定されたIDが不正でないならtrue
	 */
	public static boolean isValidId(int id) {
		return 0 <= id && id < 3;
	}

	/**
	 * 指定されたIDを持つ数牌の種類を返す。
	 * 
	 * @param id 取得したい数牌の種類が持つID
	 * @return 指定されたIDを持つ数牌の種類
	 * 
	 * @throws ArrayIndexOutOfBoundsException 指定されたIDが不正な場合
	 */
	public static SuType valueOf(int id) {
		return SUHAI_LIST.get(id);
	}

}
