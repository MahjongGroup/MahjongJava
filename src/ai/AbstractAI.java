package ai;

import system.test.Kyoku;
import system.test.Player;


public abstract class AbstractAI implements AI{
	protected Kyoku kyoku;
	protected final Player player;
	
	/**
	 * コンストラクタ。
	 * 
	 * @param p このAIを表すプレイヤーオブジェクト
	 */
	public AbstractAI(Player p) {
		this.player = p;
	}
	
	@Override
	public void update(Kyoku kyoku) {
		this.kyoku = kyoku;
	}
}