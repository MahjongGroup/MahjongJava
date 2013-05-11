package test.system.hai;

import java.util.Comparator;


/**
 * 牌を表すインターフェース.すべての牌(通常牌,捨牌,面子構成牌など)がこのインターフェースを実装する. 
 */
public interface Hai extends Comparable<Hai> {
	
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
	
	/**
	 * この牌が赤牌の場合trueを返す.
	 * @return この牌が赤牌の場合true.
	 */
	public boolean aka();

	/**
	 * この牌タイプが三元牌の場合trueを返す.
	 * @return 三元牌の場合true.
	 */
	public boolean isSangenhai();

	/**
	 * この牌タイプが数牌の場合trueを返す.
	 * このメソッドとisTsuhai()メソッドは対称なものであり,isTsuhai() == !isSuhai() が常に成り立つ.
	 * 
	 * @return 数牌の場合true.
	 */
	public boolean isSuhai();

	/**
	 * この牌タイプが字牌の場合trueを返す.
	 * このメソッドとisSuhai()メソッドは対称なものであり,isTsuhai() == !isSuhai() が常に成り立つ.
	 * 
	 * @return 字牌の場合true.
	 */
	public boolean isTsuhai();

	/**
	 * チュンチャン牌の場合falseを返す.
	 * @return チュンチャン牌の場合false.
	 */
	public boolean isTyuntyanhai();

	/**
	 * ヤオチュー牌の場合trueを返す.
	 * @return ヤオチュー牌の場合true.
	 */
	public boolean isYaotyuhai();
	
	/**
	 * この牌タイプが風牌の場合,その風を返す.風牌でない場合は例外を投げる.
	 * @return この風牌の風.
	 * @throws UnsupportedOperationException 風牌でない牌タイプに対してこのメソッドを呼び出した場合.
	 */
	public Kaze kaze();
	
	/**
	 * この牌を表す日本語の文字列を返す.
	 * @return この牌を表す日本語の文字列.
	 */
	public String notation();

	/**
	 * この牌タイプが数牌の場合,その数字を返す.数牌でない場合は例外を投げる.
	 * @return この数牌の数字.
	 * @throws UnsupportedOperationException 数牌ではない牌タイプに対してこのメソッドを呼び出した場合.
	 */
	public int number();

	/**
	 * この牌の順番を表す整数値を返す.
	 * @return この牌の順番を表す整数値.
	 */
	public int ordinal();
	
	/**
	 * この牌タイプが三元牌の場合,三元牌の種類(白,撥,中)を返す.三元牌でない場合は例外を投げる.
	 * @return 三元牌の種類(白,撥,中).
	 * @throws 三元牌でない牌タイプに対してこのメソッドを呼び出した場合.
	 */
	public SangenType sangenType();
	
	/**
	 * この牌タイプが数牌の場合,その種類(萬子,筒子,索子)を返す.この牌タイプが数牌でない場合は例外を投げる.
	 * @return 数牌の種類(萬子,筒子,索子).
	 * @throws UnsupportedOperationException この牌タイプが数牌でない場合.
	 */
	public SuType suType();
	
	/**
	 * この牌タイプのドラの牌タイプを表す．
	 * @return この牌タイプのドラの牌タイプを表す．
	 */
	public HaiType nextOfDora();

	/**
	 * 牌の種類を返す.
	 * @return 牌の種類.
	 */
	public HaiType type();

}
