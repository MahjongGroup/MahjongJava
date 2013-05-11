package system.hai;


/**
 * 捨て牌。鳴かれた牌の場合,どこから鳴かれたかの情報も持つ。
 */
public class Sutehai extends AbstractMajanHai {
	private final boolean naki;
	private final Kaze nakiKaze;

	/**
	 * 指定された牌の捨て牌を生成するコンストラクタ。
	 * 
	 * @param hai
	 */
	public Sutehai(Hai hai) {
		super(hai);
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
		super(hai);
		this.naki = true;
		this.nakiKaze = kaze;
	}

	/**
	 * この捨て牌が鳴かれている場合trueを返す.
	 * @return この捨て牌が鳴かれている場合はtrue.
	 */
	public boolean isNaki() {
		return naki;
	}

	/**
	 * この捨て牌が鳴かれている場合,どこの鳴かれたプレイヤーの風を返す.この捨て牌が鳴かれていない場合は
	 * nullを返す.
	 * @return この牌が鳴かれた風.鳴かれていない場合はnull.
	 */
	public Kaze getNakiKaze() {
		return nakiKaze;
	}

	/**
	 * この捨て牌を指定された風のプレイヤーから鳴かれた牌とし,そのオブジェクトを返す.
	 * この捨て牌オブジェクト自体は不変クラスなので内部状態は変化しない.
	 * 
	 * @param kaze 鳴いたプレイヤーの風.
	 * @return 新しく生成された捨て牌.
	 */
	public Sutehai naku(Kaze kaze) {
		return new Sutehai(this.hai, kaze);
	}
	
	@Override
	public String toString() {
		if(this.isNaki()) {
			return hai + "("  + this.nakiKaze + ")";
		}
		return hai.toString();
	}
}
