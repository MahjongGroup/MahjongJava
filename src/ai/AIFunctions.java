package ai;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import system.Functions;
import system.Hai;
import system.HaiType;
import system.TehaiList;

/**
 * AIに用いる様々なお便利メソッドを持つクラス。
 * 
 * @author shio
 * 
 */
public class AIFunctions {

	/**
	 * 手牌リストの中から孤立した牌タイプのリストを返す。
	 * 
	 * @param tlist
	 *            　手牌リスト
	 * @return　孤立した牌のリスト
	 */
	public static List<Hai> isolatedHaiList(TehaiList tlist) {
		List<HaiType> haiTypeList = tlist.toHaiTypeList();
		List<Hai> isolatedHaiList = new ArrayList<Hai>();
		for (Hai hai : tlist) {
			if (hai.isTsuhai()) {
				if (Functions.sizeOfHaiTypeList(hai.type(), haiTypeList) == 1) {
					isolatedHaiList.add(hai);
				}
			} else {
				// 数牌、±2までに牌がなければadd
			}
		}
		return isolatedHaiList;
	}

	/**
	 * 与えられたコレクションの中から最も有効でない牌を返す。
	 * 
	 * @param 牌のコレクション（主に孤立牌リスト）
	 * @return　最も有効でない牌
	 */
	public static Hai getHuyouHai(Collection<? extends Hai> c) {
		Hai tempHai = null;
		for (Hai hai : c) {
			if(hai.isSuhai() && hai.isYaotyuhai())
				return hai;
			if (tempHai == null) {
				tempHai = hai;
				continue;
			}
		}
		return tempHai;
	}
}
