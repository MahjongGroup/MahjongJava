package system.agari;

import java.util.ArrayList;
import java.util.List;

import system.hai.Hai;
import system.hai.HaiType;
import system.hai.Mentsu;

/**
 * 役判定のときに使われるパラメータクラス.
 * 面子を取り出した後に分かる情報で構成される.
 */
public class CheckParam {

	// 牌リスト(面子依存しない役チェックに利用→タンヤオなど)
	private List<Hai> haiList;

	// 面子リスト(赤牌かどうかはここからはチェックしない)
	private List<Mentsu> MentuList;

	// 雀頭
	private HaiType janto;

	// 待ちの形
	private MatiType matiType;

	public CheckParam() {
	}
	
	/**
	 * コピーコンストラクタ．
	 * @param p the param to be copied
	 */
	public CheckParam(CheckParam p) {
		if(p.haiList != null)
			this.haiList = new ArrayList<Hai>(p.haiList);
		if(MentuList != null)
			this.MentuList = new ArrayList<Mentsu>(MentuList);
		this.janto = p.janto;
		this.matiType = p.matiType;
	}
	
	public List<Hai> getHaiList() {
		return new ArrayList<Hai>(haiList);
	}

	public void setHaiList(List<Hai> haiList) {
		this.haiList = haiList;
	}

	public List<Mentsu> getMentuList() {
		return MentuList == null ? null : new ArrayList<Mentsu>(MentuList);
	}

	public void setMentuList(List<Mentsu> mentuList) {
		MentuList = mentuList;
	}

	public void setJanto(HaiType janto) {
		this.janto = janto;
	}

	public MatiType getMatiType() {
		return matiType;
	}
	
	public HaiType getJanto() {
		return janto;
	}

	public void setMatiType(MatiType matiType) {
		this.matiType = matiType;
	}

}
