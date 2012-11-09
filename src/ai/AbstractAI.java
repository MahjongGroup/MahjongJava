package ai;

import system.Kyoku;
import system.Player;


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
	
	/**
	 * 局オブジェクトを更新する。
	 * 局ごとに呼び出されて更新される。
	 * 
	 * @param kyoku 更新する局
	 */
	public void update(Kyoku kyoku) {
		this.kyoku = kyoku;
	}
}