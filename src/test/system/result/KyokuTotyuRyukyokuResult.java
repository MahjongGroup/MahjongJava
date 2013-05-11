package test.system.result;

import java.util.Map;

import system.agari.AgariResult;
import system.hai.Hai;
import system.test.KyokuPlayer;
import system.test.Player;


/**
 * 途中流局した場合の局の結果を表すクラス.
 */
public class KyokuTotyuRyukyokuResult extends AbstractKyokuResult implements KyokuResult {
	private final TotyuRyukyokuType type;
	
	/**
	 * コンストラクタ.
	 * @param type 途中流局の種類.
	 * @param oya 親.
	 * @param map プレイヤー->そのプレイヤー局.
	 * @throws NullPointerException
	 */
	public KyokuTotyuRyukyokuResult(TotyuRyukyokuType type, Player oya, Map<Player, KyokuPlayer> map) {
		super(oya, map);
		if(type == null)
			throw new NullPointerException();
		this.type = type;
	}

	@Override
	public boolean isTotyuRyukyoku() {
		return true;
	}

	@Override
	public TotyuRyukyokuType getTotyuryukyokuType() {
		return type;
	}

	@Override
	public boolean isRyukyoku() {
		return false;
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