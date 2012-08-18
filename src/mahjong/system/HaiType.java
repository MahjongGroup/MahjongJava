package mahjong.system;

/**
 * 牌の種類を表す列挙型。
 * 
 * @author mori
 * 
 */
public enum HaiType {
	ITIMAN(0), RYANMAN(1), SANMAN(2), SUMAN(3), UMAN(4), ROMAN(5), TIMAN(6), PAMAN(7), KUMAN(8), ITIPIN(
			9), RYANPIN(10), SANPIN(11), SUPIN(12), UPIN(13), ROPIN(14), TIPIN(15), PAPIN(16), KUPIN(
			17), ITISOU(18), RYANSOU(19), SANSOU(20), SUSOU(21), USOU(22), ROSOU(23), TISOU(24), PASOU(
			25), KUSOU(26), TON(27), NAN(28), SYA(29), PE(30), HAKU(31), HATSU(32), TYUN(33);

	private final int id;

	private HaiType(int id) {
		this.id = id;
	}

	/**
	 * 数牌ならtrueを返す。
	 * 
	 * @return 数牌ならtrue
	 */
	public boolean isSuhai() {
		if (id >= 0 && id <= 26) {
			return true;
		}
		return false;
	}

	/**
	 * このオブジェクトの数牌の種類を返す
	 * 
	 * @return このオブジェクトの数牌の種類
	 */
	public SuhaiType getSuhaiType() {
		return SuhaiType.valueOf(id / 9);
	}

	/**
	 * このオブジェクトが萬子ならtrueを返す。
	 * 
	 * @return このオブジェクトが萬子ならtrue
	 */
	public boolean isMan() {
		if (id >= 0 && id <= 8)
			return true;
		return false;
	}

	/**
	 * このオブジェクトが筒子ならtrueを返す。
	 * 
	 * @return このオブジェクトが筒子ならtrue
	 */
	public boolean isPin() {
		if (id >= 9 && id <= 17)
			return true;
		return false;
	}

	/**
	 * このオブジェクトが索子ならtrueを返す。
	 * 
	 * @return このオブジェクトが索子ならtrue
	 */
	public boolean isSou() {
		if (id >= 18 && id <= 26)
			return true;
		return false;
	}

	public int getId() {
		return id;
	}

	/**
	 * このオブジェクトが数牌の場合、その番号を返す。
	 * 
	 * @return 数牌の番号
	 */
	public int getNumber() {
		if (isSuhai()) {
			return id % 9 + 1;
		}
		throw new IllegalArgumentException("id : " + id);
	}

	/**
	 * このオブジェクトが字牌ならtrueを返す。
	 * 
	 * @return このオブジェクトが字牌ならtrue
	 */
	public boolean isTsuhai(int x) {
		if (x >= 27 && x <= 33) {
			return true;
		}
		return false;
	}

	/**
	 * このオブジェクトが三元牌ならtrueを返す。
	 * 
	 * @return このオブジェクトが三元牌ならtrue
	 */
	public boolean isSangenhai() {
		if (id >= 31 && id <= 33) {
			return true;
		}
		return false;
	}

	/**
	 * このオブジェクトが風牌ならtrueを返す。
	 * 
	 * @return このオブジェクトが風牌ならtrue
	 */

	public boolean isKazehai() {
		if (id >= 27 && id <= 30) {
			return true;
		}
		return false;
	}

	/**
	 * 種類がtypeで番号がnumberの数牌オブジェクトを返す。
	 * 
	 * @param type 返ってくる数牌オブジェクトの種類
	 * @param number 返ってくる数牌オブジェクトの番号
	 * @return 種類がtypeで番号がnumberの数牌オブジェクト
	 */
	public static HaiType getSuhai(SuhaiType type, int number) {
		for (HaiType result : HaiType.values()) {
			if (result.getSuhaiType() == type && result.getNumber() == number)
				return result;
		}
		throw new IllegalArgumentException();
	}
}
