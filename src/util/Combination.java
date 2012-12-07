package util;

import java.util.ArrayList;
import java.util.List;

/**
 * ペアの組み合わせを生成するスタティックメソッドを提供する.
 */
public class Combination {
	/**
	 * 指定された配列のペアの組み合わせを生成する.例えば,整数のリスト[1,2,3,4]を引数に
	 * 入れると[(1,2),(1,3),(1,4),(2,3),(2,4),(3,4)]というペアのリストを返す.
	 * 
	 * @param c 組み合わせをつくる配列.
	 * @return ペアのリスト.組み合わせがない場合はサイズ0のリスト.
	 */
	public static <T> List<Pair<T>> getCombination(T array[]) {
		List<Pair<T>> ret = new ArrayList<Pair<T>>();
		for (int i = 0; i < array.length; i++) {
			for (int j = i + 1; j < array.length; j++) {
				ret.add(new Pair<T>(array[i], array[j]));
			}
		}
		return ret;
	}
}
