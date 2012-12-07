package system;

import java.util.Comparator;

/**
 * 牌を表すインターフェース.すべての牌(通常牌,捨牌,面子構成牌など)がこのインターフェースを実装する. 
 */
public interface Hai extends Comparable<Hai> {
	
	/**
	 * この牌を表す日本語の文字列を返す.
	 * @return この牌を表す日本語の文字列.
	 */
	public String notation();
	
	/**
	 * この牌が赤牌の場合trueを返す.
	 * @return この牌が赤牌の場合true.
	 */
	public boolean aka();
	
	/**
	 * 牌の種類を返す.
	 * @return 牌の種類.
	 */
	public HaiType type();
	
	/**
	 * この牌の順番を表す整数値を返す.
	 * @return この牌の順番を表す整数値.
	 */
	public int ordinal();
	
	/**
	 * 牌をソートするためのコンパレーターオブジェクトの列挙型.
	 */
	public static enum HaiComparator implements Comparator<Hai> {
		/**
		 * 牌を昇順に並び替えるためのコンパレーター.
		 */
		ASCENDING_ORDER {
			@Override
			public int compare(Hai t0, Hai t1) {
				return t0.ordinal() - t1.ordinal();
			}
		},

		/**
		 * 牌を降順に並び替えるためのコンパレーター.
		 */
		DESCENDING_ORDER {
			@Override
			public int compare(Hai t0, Hai t1) {
				return t1.ordinal() - t0.ordinal();
			}
		}
	}
	
	/**
	 * 型ID
	 */
	public static int TYPE_ID = 1;

}
