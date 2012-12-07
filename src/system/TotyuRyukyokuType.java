package system;

/**
 * 途中流局の種類を表す列挙型.
 */
public enum TotyuRyukyokuType {
	SANCHAHO("三家放"), KYUSYUKYUHAI("九種九牌"), SUCHAREACH("四家立直");
	private final String name;
	
	private TotyuRyukyokuType(String name) {
		this.name = name;
	}
	
	/**
	 * このオブジェクトの日本語名を取得する.
	 * @return 日本語名.
	 */
	public String getName() {
		return name;
	}
	
}
