package mahjong.client;

/**
 * 牌の種類を表す列挙型。
 * 
 * @author mori
 * 
 */
public enum HaiType {
	ITIMAN(0,"一萬"), RYANMAN(1,"二萬"), SANMAN(2,"三萬"), SUMAN(3,"四萬"), UMAN(4,"五萬"), 
	ROMAN(5,"六萬"),TIMAN(6,"七萬"), PAMAN(7,"八萬"), KUMAN(8,"九萬"), 
	ITIPIN(9,"一筒"), RYANPIN(10,"二筒"), SANPIN(11,"三筒"), SUPIN(12,"四筒"), UPIN(13,"五筒"),
	ROPIN(14,"六筒"), TIPIN(15,"七筒"), PAPIN(16,"八筒"), KUPIN(17,"九筒"),
	ITISOU(18,"一索"), RYANSOU(19,"二索"), SANSOU(20,"三索"), SUSOU(21,"四索"), USOU(22,"五索"),
	ROSOU(23,"六索"), TISOU(24,"七索"), PASOU(25,"八索"), KUSOU(26,"九索"),
	TON(27,"東"), NAN(28,"南"), SYA(29,"西"), PE(30,"北"),
	HAKU(31,"白"), HATSU(32,"發"), TYUN(33,"中");

	private final int id;
	private final String notation;

	private HaiType(int id,String notation) {
		this.id = id;
		this.notation = notation;
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
	
	public String toString(){
		return notation;
	}
}	
