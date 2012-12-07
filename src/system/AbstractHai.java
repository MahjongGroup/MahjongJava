package system;

/**
 * 麻雀牌を実装した抽象クラス．例えば捨て牌や面子構成牌など基本的な動作が
 * 麻雀牌と変わらない牌クラスがこのクラスを継承する．
 */
public abstract class AbstractHai implements Hai{
	protected final Hai hai;
	
	/**
	 * コンストラクタ．
	 * @param hai コピーする麻雀牌オブジェクト．
	 * @throws NullPointerException 指定された牌がnullの場合．
	 */
	public AbstractHai(Hai hai) {
		this.hai = MajanHai.valueOf(hai.type(), hai.aka());
		if(this.hai == null) {
			throw new NullPointerException();
		}
	}

	@Override
	public int compareTo(Hai o) {
		return hai.compareTo(o);
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
	public int ordinal() {
		return hai.ordinal();
	}

	@Override
	public int hashCode() {
		return hai.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return hai.equals(obj);
	}
	
	@Override
	public String toString() {
		return hai.toString();
	}

}
