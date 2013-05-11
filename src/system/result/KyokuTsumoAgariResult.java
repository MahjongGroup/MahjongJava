package system.result;

import java.util.Map;

import system.agari.AgariResult;
import system.hai.Hai;
import system.test.KyokuPlayer;
import system.test.Player;

/**
 * ツモ上がった場合の局の結果を表すクラス.不変クラス.
 */
public class KyokuTsumoAgariResult extends AbstractKyokuResult implements KyokuResult {
	private final Player agariPlayer;
	private final AgariResult result;
	private final Hai agariHai;
	
	/**
	 * コンストラクタ.
	 * @param agariHai
	 * @param agariPlayer
	 * @param result
	 * @param oya
	 * @param map
	 * @throws NullPointerException
	 */
	public KyokuTsumoAgariResult(Hai agariHai, Player agariPlayer, AgariResult result, Player oya, Map<Player, KyokuPlayer> map) {
		super(oya, map);
		if(agariHai == null || agariPlayer == null || result == null)
			throw new NullPointerException();
		this.agariHai = agariHai;
		this.agariPlayer = agariPlayer;
		this.result = result;
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
		return false;
	}

	@Override
	public boolean isTsumoAgari() {
		return true;
	}

	@Override
	public boolean isRonAgari() {
		return false;
	}

	@Override
	public int getAgariSize() {
		return 1;
	}

	@Override
	public boolean isAgari(Player p) {
		return p.equals(agariPlayer);
	}

	@Override
	public Player getHojuPlayer() {
		throw new UnsupportedOperationException();
	}

	@Override
	public AgariResult getAgariResult(Player p) {
		if(!p.equals(agariPlayer)) {
			throw new IllegalArgumentException();
		}
		return result;
	}

	@Override
	public Hai getAgariHai() {
		return agariHai;
	}

	@Override
	public Player getTsumoAgariPlayer() {
		return agariPlayer;
	}

}
