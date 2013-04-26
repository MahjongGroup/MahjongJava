package system.hai;


/**
 * 麻雀牌を実装した抽象クラス．例えば捨て牌や面子構成牌など基本的な動作が
 * 麻雀牌と変わらない牌クラスがこのクラスを継承する．
 */
public abstract class AbstractMajanHai implements Hai{
	protected final Hai hai;
	
	/**
	 * コンストラクタ．
	 * @param hai コピーする麻雀牌オブジェクト．
	 * @throws NullPointerException 指定された牌がnullの場合．
	 */
	public AbstractMajanHai(Hai hai) {
		this.hai = MajanHai.valueOf(hai.type(), hai.aka());
		if(this.hai == null) {
			throw new NullPointerException();
		}
	}

	@Override
	public boolean aka() {
		return hai.aka();
	}

	@Override
	public int compareTo(Hai o) {
		return hai.compareTo(o);
	}

	@Override
	public boolean equals(Object obj) {
		return hai.equals(obj);
	}

	@Override
	public int hashCode() {
		return hai.hashCode();
	}

	@Override
	public boolean isSangenhai() {
		return hai.isSangenhai();
	}

	@Override
	public boolean isSuhai() {
		return hai.isSuhai();
	}

	@Override
	public boolean isTsuhai() {
		return hai.isTsuhai();
	}

	@Override
	public boolean isTyuntyanhai() {
		return hai.isTyuntyanhai();
	}

	@Override
	public boolean isYaotyuhai() {
		return hai.isYaotyuhai();
	}

	@Override
	public Kaze kaze() {
		return hai.kaze();
	}

	@Override
	public String notation() {
		return hai.notation();
	}

	@Override
	public int number() {
		return hai.number();
	}

	@Override
	public int ordinal() {
		return hai.ordinal();
	}

	@Override
	public SangenType sangenType() {
		return hai.sangenType();
	}


	@Override
	public SuType suType() {
		return hai.suType();
	}

	@Override
	public String toString() {
		return hai.toString();
	}
	
	@Override
	public HaiType type() {
		return hai.type();
	}

	@Override
	public HaiType nextOfDora() {
		return hai.nextOfDora();
	}


}
