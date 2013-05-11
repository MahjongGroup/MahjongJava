package test.system.result;

import java.util.Map;

import system.agari.AgariResult;
import system.hai.Hai;
import system.test.KyokuPlayer;
import system.test.Player;

/**
 * 途中流局を表す結果.
 */
public class KyokuRyukyokuResult extends AbstractKyokuResult implements KyokuResult{
	/**
	 * コンストラクタ.
	 * 
	 * @param oya 親.
	 * @param map プレイヤー->そのプレイヤー局.
	 */
	public KyokuRyukyokuResult(Player oya, Map<Player, KyokuPlayer> map) {
		super(oya, map);
	}

	@Override
	public boolean isTotyuRyukyoku() {
		return false;
	}

	@Override
	public TotyuRyukyokuType getTotyuryukyokuType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isRyukyoku() {
		return true;
	}

	@Override
	public boolean isTsumoAgari() {
		return false;
	}

	@Override
	public boolean isRonAgari() {
		return false;
	}

	@Override
	public int getAgariSize() {
		return 0;
	}

	@Override
	public boolean isAgari(Player p) {
		return false;
	}

	@Override
	public Player getHojuPlayer() {
		throw new UnsupportedOperationException();
	}

	@Override
	public AgariResult getAgariResult(Player p) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Hai getAgariHai() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Player getTsumoAgariPlayer() {
		throw new UnsupportedOperationException();
	}

}