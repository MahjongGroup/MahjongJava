package system;


/**
 * ルール,場風を持つクラス
 */
public class Field {
	private final Rule rule;
	private final Kaze bakaze;

	public Field(Rule rule, Kaze bakaze) {
		this.rule = rule;
		this.bakaze = bakaze;
	}

	public Rule getRule() {
		return rule;
	}

	public Kaze getBakaze() {
		return bakaze;
	}
	
	@Override
	public String toString() {
		return "rule:" + rule + ", 場風:" + bakaze;
	}
}
