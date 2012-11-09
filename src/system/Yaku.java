package system;

/**
 * 役であることを表すインターフェース。
 * 
 * @author kohei
 *
 */
public interface Yaku {
	/**
	 * 役のタイプ。通常役と役満に分類される。
	 * 
	 * @author kohei
	 *
	 */
	enum Type{
		NORMAL, YAKUMAN;
	}
	
	/**
	 * 役のタイプを返す。
	 * @return　役のイプ
	 */
	public Type type();
	
	/**
	 * フラグでチェックするタイプの役の場合trueを返す。
	 * 
	 * @return フラグでチェックするタイプの役の場合true
	 */
	public boolean flagCheck();
	
	/**
	 * この役を表す文字列を日本語で返す。
	 * 
	 * @return この役を表す日本語
	 */
	public String notation();
	
	/**
	 * 指定された牌がこの役をあがっている場合trueを返す。
	 * 
	 * @param param 役判定に必要な材料
	 * @return この役をあがっている場合true
	 */
	public boolean check(CheckerParam param);
	
}
