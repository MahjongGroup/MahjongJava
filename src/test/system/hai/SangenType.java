package test.system.hai;

import java.util.ArrayList;
import java.util.List;

/**
 * 三元牌の種類を表す列挙型。白,撥,中の3種類がある。それぞれ0,1,2のIDを持つ。
 * 
 * @author kohei
 * 
 */
public enum SangenType {
	HAKU("白", 0), HATU("撥", 1), TYUN("中", 2);

	private final String notation;
	private final int id;

	private SangenType(String notation, int id) {
		this.notation = notation;
		this.id = id;
	}

	/**
	 * この三元牌の種類を表す文字列を日本語で返す。
	 * 
	 * @return この三元牌の種類を表す文字列(日本語)
	 */
	public String notation() {
		return notation;
	}

	/**
	 * この三元牌の種類のIDを返す。
	 * 
	 * @return この三元牌の種類のID
	 */
	public int id() {
		return id;
	}

	@Override
	public String toString() {
		return notation;
	}

	private static final List<SangenType> SANGEN_LIST = new ArrayList<SangenType>();
	static {
		SANGEN_LIST.add(0, HAKU);
		SANGEN_LIST.add(1, HATU);
		SANGEN_LIST.add(2, TYUN);
	}

	/**
	 * 指定されたIDが不正でないならtrueを返す。
	 * 
	 * @param id 不正でないか調べるID
	 * @return 指定されたIDが不正でないならtrue
	 */
	public static boolean isValidId(int id) {
		return 0 <= id && id < 3;
	}

	/**
	 * 指定されたIDを持つ三元牌の種類を返す。
	 * 
	 * @param id 取得したい三元牌の種類が持つID
	 * @return 指定されたIDを持つ三元牌の種類
	 * 
	 * @throws ArrayIndexOutOfBoundsException 指定されたIDが不正な場合
	 */
	public static SangenType valueOf(int id) {
		return SANGEN_LIST.get(id);
	}

}
