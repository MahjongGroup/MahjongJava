package system;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 役判定のときに使われるパラメータクラス。
 */
public class Param {
	private boolean tsumo; // ツモかロンか
	private Hai agariHai; // あがり牌
	private boolean naki; // 鳴いているかどうか
	private Kaze jikaze; // 自風
	private Set<Yaku> flagCheckYakuSet; // フラグ役Set

	// 判定関数の中で設定される変数群---------------------------------------------
	// 牌リスト(面子依存しない役チェックに利用→タンヤオなど)
	private List<Hai> haiList;

	// 面子リスト(赤牌かどうかはここからはチェックしない)
	private List<Mentu> MentuList;

	// 雀頭
	private HaiType janto;

	// 待ちの形
	private MatiType matiType;

	public List<Hai> getHaiList() {
		return new ArrayList<Hai>(haiList);
	}

	public void setHaiList(List<Hai> haiList) {
		this.haiList = haiList;
	}

	public void setNaki(boolean naki) {
		this.naki = naki;
	}

	public boolean isNaki() {
		return naki;
	}

	public boolean isTsumo() {
		return tsumo;
	}

	public void setTsumo(boolean tsumo) {
		this.tsumo = tsumo;
	}

	public Hai getAgariHai() {
		return agariHai;
	}

	public void setAgariHai(Hai agariHai) {
		this.agariHai = agariHai;
	}

	public List<Mentu> getMentuList() {
		return MentuList == null ? null : new ArrayList<Mentu>(MentuList);
	}

	public void setMentuList(List<Mentu> mentuList) {
		MentuList = mentuList;
	}

	public HaiType getJanto() {
		return janto;
	}

	public void setJanto(HaiType janto) {
		this.janto = janto;
	}

	public MatiType getMatiType() {
		return matiType;
	}

	public void setMatiType(MatiType matiType) {
		this.matiType = matiType;
	}

	public Kaze getJikaze() {
		return jikaze;
	}

	public void setJikaze(Kaze jikaze) {
		this.jikaze = jikaze;
	}

	public Set<Yaku> getFlagCheckYakuSet() {
		return new HashSet<Yaku>(flagCheckYakuSet);
	}

	public void setFlagCheckYakuSet(Set<Yaku> flagCheckYakuSet) {
		this.flagCheckYakuSet = flagCheckYakuSet;
	}
}
