package test.system.result;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import system.agari.AgariResult;
import system.hai.Hai;
import system.test.KyokuPlayer;
import system.test.Player;

/**
 * ロンあがりの場合の局の結果を表すクラス.不変クラスである.
 */
public class KyokuRonAgariResult extends AbstractKyokuResult implements KyokuResult, Serializable {
	private final Map<Player, AgariResult> agariMap;
	private final Player hoju;
	private final Hai agariHai;
	
	/**
	 * このオブジェクトのコンストラクタ.
	 * @param agariHai 
	 * @param hoju
	 * @param agariMap
	 * @param oya
	 * @param map
	 * @throws NullPointerException
	 */
	public KyokuRonAgariResult(Hai agariHai, Player hoju, Map<Player, AgariResult> agariMap, Player oya, Map<Player, KyokuPlayer> map) {
		super(oya, map);
		if(agariHai == null || hoju == null || agariMap == null)
			throw new NullPointerException();
		this.agariHai = agariHai;
		this.hoju = hoju;
		this.agariMap = new HashMap<Player, AgariResult>(agariMap);
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
		return false;
	}

	@Override
	public boolean isRonAgari() {
		return true;
	}

	@Override
	public int getAgariSize() {
		return agariMap.size();
	}

	@Override
	public boolean isAgari(Player p) {
		return agariMap.containsKey(p);
	}

	@Override
	public Player getHojuPlayer() {
		return hoju;
	}

	@Override
	public AgariResult getAgariResult(Player p) {
		return agariMap.get(p);
	}

	@Override
	public Hai getAgariHai() {
		return agariHai;
	}

	@Override
	public Player getTsumoAgariPlayer() {
		throw new UnsupportedOperationException();
	}

	
	// TODO javadoc
	public static class Builder {
		private Map<Player, AgariResult> agariMap;
		
		public Builder() {
			agariMap = new HashMap<Player, AgariResult>();
		}
		
		public void put(Player player, AgariResult result) { 
			agariMap.put(player, result);
		}
		
		public int size() {
			return agariMap.size();
		}
		
		public KyokuResult build(Hai agariHai, Player hoju, Player oya, Map<Player, KyokuPlayer> map) {
			return new KyokuRonAgariResult(agariHai, hoju, agariMap, oya, map);
		}
	}
	
}
