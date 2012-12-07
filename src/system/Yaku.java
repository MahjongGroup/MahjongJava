package system;

/**
 * 役であることを表すインターフェース。
 */
public interface Yaku {
	/**
	 * 役のタイプ。通常役と役満に分類される。
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
	 * 4面子1雀頭で構成されている必要がある場合はtrueを返す.ただし,このメソッドがfalseを
	 * 返すからといって4面子1雀頭で構成されていないとは限らない.
	 * 
	 * @return 4面子1雀頭で構成されている必要がある場合はtrue.
	 */
	public boolean is4Mentu1Janto();
	
	/**
	 * フラグでチェックするタイプの役の場合trueを返す。
	 * 
	 * @return フラグでチェックするタイプの役の場合true
	 */
	public boolean isFlagCheck();
	
	/**
	 * この役を表す文字列を日本語で返す。
	 * 
	 * @return この役を表す日本語
	 */
	public String notation();
	
	/**
	 * 指定された牌がこの役をあがっている場合trueを返す。
	 * 
	 * @param param 役判定に必要な材料.
	 * @param field 局のルール,場風など.
	 * @return この役をあがっている場合true
	 */
	public boolean check(Param param, Field field);
	
}
