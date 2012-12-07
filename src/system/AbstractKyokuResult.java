package system;

import java.util.HashMap;
import java.util.Map;

/**
 * 局結果の抽象クラス.
 */
public abstract class AbstractKyokuResult implements KyokuResult {
	private final Map<Player, KyokuPlayer> playerMap;
	private final Player oya;

	/**
	 * コンストラクタ.
	 * @param oya 親.
	 * @param map プレイヤー->そのプレイヤー局.
	 * @throws NullPointerException
	 */
	public AbstractKyokuResult(Player oya, Map<Player, KyokuPlayer> map) {
		this.playerMap = new HashMap<Player, KyokuPlayer>(map);
		this.oya = oya;
		if(this.playerMap == null || this.oya == null)
			throw new NullPointerException();
	}

	@Override
	public Player getOya() {
		return oya;
	}

	@Override
	public int getTenpaiSize() {
		int size = 0;
		for(KyokuPlayer kp : playerMap.values()) { 
			if(kp.isTenpai()){
				size++;
			}
		}
		return size;
	}

	@Override
	public boolean isTenpai(Player p) {
		return playerMap.get(p).isTenpai();
	}

	@Override
	public int getReachPlayerSize() {
		int size = 0;
		for(KyokuPlayer kp : playerMap.values()) { 
			if(kp.isReach() || kp.isDoubleReach()){
				size++;
			}
		}
		return size;
	}

	@Override
	public KyokuPlayer getKyokuPlayer(Player p) {
		for (Player player : playerMap.keySet()) {
			if(player.equals(p))
				return playerMap.get(player);
		}
		throw new IllegalArgumentException();
	}

	public boolean isReach(Player p) {
		KyokuPlayer kp = playerMap.get(p);
		return kp.isReach() || kp.isDoubleReach();
	}
}
