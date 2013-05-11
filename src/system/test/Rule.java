package system.test;

import system.hai.Kaze;

/**
 * 麻雀のルールを表すクラス。
 * @author kohei
 */
public class Rule {
	/**
	 * 赤ありの場合trueを返す。
	 * @return
	 */
	public boolean isAkaAri() {
		return true;
	}
	
	/**
	 * 国士13面待ちがダブル役満の場合trueを返す。
	 * @return
	 */
	public boolean isKokushi13menDaburu() {
		return true;
	}

	/**
	 * 四暗刻単騎がダブル役満の場合trueを返す。
	 * @return
	 */
	public boolean isSutanDaburu() {
		return true;
	}

	/**
	 * 純正九蓮宝燈がダブル役満の場合trueを返す。
	 * @return
	 */
	public boolean isJuntyanDaburu() {
		return true;
	}

	
	/**
	 * 大四喜がダブル役満の場合trueを返す。
	 * @return
	 */
	public boolean isDaisushiDaburu() {
		return true;
	}

	/**
	 * ゲームが終了する風を返す.例えば,東風なら東を半荘なら南を返す.
	 * @return ゲームが終了する風.
	 */
	public Kaze getEndKaze () {
		return Kaze.NAN;
	}
	public boolean isSyanyu(){
		return true;
	}
	public boolean isHakoAri(){
		return true;
	}
	
}
