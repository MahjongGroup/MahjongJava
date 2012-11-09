package system;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 役判定のときに使われるパラメータクラス。
 * @author kohei
 *
 */
public class CheckerParam {
	// ツモかロンか
	private boolean tsumo;
	
	// あがり牌
	private Hai agariHai;
	
	// 牌リスト(面子依存しない役チェックに利用→タンヤオなど)
	private List<Hai> haiList;

	// 面子リスト(赤牌かどうかはここからはチェックしない)
	private List<Mentu> MentuList;
	
	// 雀頭
	private HaiType janto;
	
	// ルール
	private Rule rule;
	
	// 鳴いているかどうか
	private boolean naki;
	
	// 待ちの形
	private MatiType matiType;
	
	// 自風
	private Kaze jikaze;
	
	// 場風
	private Kaze bakaze;

	// フラグ役Set
	private Set<Yaku> flagCheckYakuSet;
	
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
		return new ArrayList<Mentu>(MentuList);
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

	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
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

	public Kaze getBakaze() {
		return bakaze;
	}

	public void setBakaze(Kaze bakaze) {
		this.bakaze = bakaze;
	}

	public Set<Yaku> getFlagCheckYakuSet() {
		return new HashSet<Yaku>(flagCheckYakuSet);
	}

	public void setFlagCheckYakuSet(Set<Yaku> flagCheckYakuSet) {
		this.flagCheckYakuSet = flagCheckYakuSet;
	}
}
