package system;

import java.util.HashSet;
import java.util.Set;

/**
 * あがり判定、役判定に必要な情報.
 * 計算をせずに事前にわかっている情報.
 * ツモかどうか、自風、鳴いているか、あがり牌がある.
 */
public class AgariParam {
	private final boolean tsumo;
	private final boolean naki;
	private final Hai agarihai;
	private final Kaze jikaze;
	private Set<Yaku> flagCheckYakuSet;

	/**
	 * コンストラクタ.
	 * 
	 * @param tsumo ツモの場合はtrue.
	 * @param naki 鳴き.
	 * @param hai あがり牌.
	 * @param kaze 自分の風.
	 */
	public AgariParam(boolean tsumo, boolean naki, Hai hai, Kaze kaze, Set<Yaku> yakuSet) {
		this.tsumo = tsumo;
		this.naki = naki;
		this.agarihai = hai;
		this.jikaze = kaze;
		if(yakuSet != null) {
			this.flagCheckYakuSet = new HashSet<Yaku>(yakuSet);
		}else {
			this.flagCheckYakuSet = new HashSet<Yaku>(0);
		}
	}
	
	public AgariParam(AgariParam agp) {
		this.tsumo = agp.tsumo;
		this.naki = agp.naki;
		this.agarihai = agp.agarihai;
		this.jikaze = agp.jikaze;
		
		if(agp.getFlagCheckYakuSet() != null) {
			this.flagCheckYakuSet = new HashSet<Yaku>(agp.flagCheckYakuSet);
		}else {
			this.flagCheckYakuSet = new HashSet<Yaku>(0);
		}
	}

	/**
	 * ツモの場合はtrueを返す.
	 * @return true if this agari is tsumo agari.
	 */
	public boolean isTsumo() {
		return tsumo;
	}

	/**
	 * 鳴きの場合はtrueを返す.
	 * @return true if this agari is naki.
	 */
	public boolean isNaki() {
		return naki;
	}

	/**
	 * あがり牌を返す.
	 * @return the agari hai.
	 */
	public Hai getAgarihai() {
		return agarihai;
	}

	/**
	 * 自風を返す.
	 * @return the jikaze of this agari.
	 */
	public Kaze getJikaze() {
		return jikaze;
	}

	/**
	 * 役フラグセットを返す.
	 * @return the flag check yaku set.
	 */
	public Set<Yaku> getFlagCheckYakuSet() {
		return new HashSet<Yaku>(flagCheckYakuSet);
	}

	public boolean addFlagCheckYaku(Yaku addedYaku) {
		return flagCheckYakuSet.add(addedYaku);
	}
}
