package system;

/**
 * 点数のタイプの列挙型。満貫、跳満などがある。
 */
public enum ScoreType {
	NORMAL("通常点数"),
	MANGAN("満貫"),
	HANEMAN("跳満"),
	BAIMAN("倍満"),
	SANBAIMAN("三倍満"),
	
	YAKUMAN("役満"),
	DOUBLE_YAKUMAN("ダブル役満"),
	TRIPLE_YAKUMAN("トリプル役満"),
	;
	
	private String notation;
	
	private ScoreType(String notation) {
		this.notation = notation;
	}
	
	public String notation() {
		return this.notation;
	}
}
