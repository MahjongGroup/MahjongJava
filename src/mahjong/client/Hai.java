package mahjong.client;

/**
 * 牌を表すクラス。牌の種類と赤牌かどうかのフラグを持っている。
 * @author mori
 *
 */
public class Hai implements Comparable<Hai> {

	private final boolean aka;
	private final HaiType type;

	/**
	 * 牌オブジェクトを生成するコンストラクタ。
	 * @param type 牌の種類
	 * @param aka 赤牌ならtrue
	 */
	public Hai(HaiType type, boolean aka) {
		this.type = type;
		this.aka = aka;
	}
	
	/**
	 * 牌の種類を表すHaiTypeオブジェクトを返す。
	 * @return 牌の種類を表すHaiTypeオブジェクト
	 */
	public HaiType getType() {
		return type;
	}

	/**
	 * この牌が赤牌ならtrueを返す。
	 * @return この牌が赤牌ならtrue
	 */
	public boolean isRed() {
		return aka;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (aka ? 1231 : 1237);
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Hai other = (Hai) obj;
		if (aka != other.aka)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		if(aka)
			return "赤" + type.toString();
		return type.toString();
	}

	@Override
	public int compareTo(Hai o) {
		int result = type.getId() - o.getType().getId();
		if(result == 0)
			return (aka ? 1 : 0) - (o.isRed() ? 1 : 0);
		return result;
	}
}
