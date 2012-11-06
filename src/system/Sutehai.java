package system;

/**
 * 捨て牌。鳴かれた牌の場合、どこから鳴かれたかの情報も持つ。
 * 
 * @author kohei
 * 
 */
public class Sutehai implements Hai {
	private final Hai hai;
	private final boolean naki;
	private final Kaze nakiKaze;

	/**
	 * 指定された牌の捨て牌を生成するコンストラクタ。
	 * 
	 * @param hai
	 */
	public Sutehai(Hai hai) {
		// TODO change
		this.hai = MajanHai.valueOf(hai.type(), hai.aka());
		this.naki = false;
		this.nakiKaze = null;
	}

	/**
	 * 指定された牌の鳴き捨て牌を生成するコンストラクタ。
	 * 
	 * @param hai 牌
	 * @param kaze 鳴かれた場所(風)
	 * 
	 */
	public Sutehai(Hai hai, Kaze kaze) {
		// TODO change
		this.hai = MajanHai.valueOf(hai.type(), hai.aka());
		this.naki = true;
		this.nakiKaze = kaze;
	}

	public boolean isNaki() {
		return naki;
	}

	public Kaze getNakiKaze() {
		return nakiKaze;
	}

	public Sutehai naku(Kaze kaze) {
		return new Sutehai(this.hai, kaze);
	}
	
	@Override
	public String notation() {
		return hai.notation();
	}

	@Override
	public boolean aka() {
		return hai.aka();
	}

	@Override
	public HaiType type() {
		return hai.type();
	}

	@Override
	public int compareTo(Hai hai) {
		return hai.compareTo(hai);
	}

	@Override
	public int ordinal() {
		return hai.ordinal();
	}

	@Override
	public String toString() {
		if(this.isNaki()) {
			return hai + "("  + this.nakiKaze + ")";
		}
		return hai.toString();
	}
}
