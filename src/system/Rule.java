package system;

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
	 * 大四喜がダブル役満の場合trueを返す。
	 * @return
	 */
	public boolean isDaisushiDaburu() {
		return true;
	}

}
