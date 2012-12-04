package system;

import java.util.ArrayList;
import java.util.List;

/**
 * 風を表す列挙体。東、南、西、北の4つのオブジェクトを持つ。それぞれ0,1,2,3のIDを持つ。
 * 
 * @author kohei
 * 
 */
public enum Kaze {
	TON("東", 0), NAN("南", 1), SYA("西", 2), PE("北", 3);

	private final String notation;
	private final int id;

	private Kaze(String notation, int id) {
		this.notation = notation;
		this.id = id;
	}

	/**
	 * この風を表す文字列を日本語で返す。
	 * 
	 * @return この風を表す文字列(日本語)
	 */
	public String notation() {
		return notation;
	}

	/**
	 * この風のIDを返す
	 * 
	 * @return この風のID
	 */
	public int id() {
		return id;
	}

	/**
	 * この風の上風を返す。例えば、南の場合は東を返す。
	 * 
	 * @return この風の上風
	 */
	public Kaze kami() {
		return KAZE_LIST.get((id + 3) % 4);
	}

	/**
	 * この風の対面の風を返す。例えば、東の場合は西を返す。
	 * 
	 * @return この風の対面の風
	 */
	public Kaze toimen() {
		return KAZE_LIST.get((id + 2) % 4);
	}

	/**
	 * この風の下風を返す。例えば、南の場合は西を返す。
	 * 
	 * @return この風の下風
	 */
	public Kaze simo() {
		return KAZE_LIST.get((id + 1) % 4);
	}

	@Override
	public String toString() {
		return notation;
	}


	private static final List<Kaze> KAZE_LIST = new ArrayList<Kaze>(4);

	static {
		KAZE_LIST.add(0, TON);
		KAZE_LIST.add(1, NAN);
		KAZE_LIST.add(2, SYA);
		KAZE_LIST.add(3, PE);
	}

	/**
	 * 指定されたIDが不正でないならtrueを返す。
	 * 
	 * @param id 不正でないか調べるID
	 * @return 指定されたIDが不正でないならtrue
	 */
	public static boolean isValidId(int id) {
		return 0 <= id && id < 4;
	}

	/**
	 * 指定されたIDを持つ風を返す。
	 * 
	 * @param id 取得したい風のID
	 * @return 指定されたIDを持つ風
	 * 
	 * @throws ArrayIndexOutOfBoundsException 指定されたIDが不正な場合
	 */
	public static Kaze valueOf(int id) {
		return KAZE_LIST.get(id);
	}

	/**
	 * 型ID
	 */
	public static int TYPE_ID = 2;

	public static void main(String[] args) {
		for (int i = 0; i < KAZE_LIST.size(); i++) {
			System.out.println(KAZE_LIST.get(i));
		}
	}
}
