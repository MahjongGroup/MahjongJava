package mahjong.client;

/**
 * 数牌の種類を表す列挙型。数牌の種類には萬子、筒子、索子がある。
 * 
 * @author kohei
 * 
 */
public enum SuhaiType {
	MAN(0, "萬子"), PIN(1, "筒子"), SOU(2, "索子");

	private final int id;
	private final String notation;

	private SuhaiType(int id, String notation) {
		this.id = id;
		this.notation = notation;
	}

	public int getId() {
		return id;
	}

	public String getNotation() {
		return notation;
	}

	/**
	 * 引数と同じidを持つSuhaiTypeオブジェクトを返す。
	 * 
	 * @param id
	 * @return 引数と同じidを持つSuhaiTypeオブジェクト
	 */
	public static SuhaiType valueOf(int id) {
		for (SuhaiType type : values()) {
			if (type.id == id)
				return type;
		}
		throw new IllegalArgumentException("id : " + id);
	}

	@Override
	public String toString() {
		return notation;
	}

}
