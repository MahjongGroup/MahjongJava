package system;

import system.hai.Kaze;

/**
 * Fieldクラスはルール,場風を持つ.
 * このオブジェクトは1局ごとに生成される.
 */
public class Field {
	private final Rule rule;
	private final Kaze bakaze;

	/**
	 * コンストラクタ.
	 * @param rule この麻雀で使用されるルール.
	 * @param bakaze この局の場風.
	 */
	public Field(Rule rule, Kaze bakaze) {
		this.rule = rule;
		this.bakaze = bakaze;
	}

	/**
	 * この麻雀で使用されているルールを返す.
	 * @return この麻雀で使用されているルール
	 */
	public Rule getRule() {
		return rule;
	}

	/**
	 * この局の場風を返す.
	 * @return この局の場風
	 */
	public Kaze getBakaze() {
		return bakaze;
	}

	@Override
	public String toString() {
		return "rule:" + rule + ", 場風:" + bakaze;
	}
}
