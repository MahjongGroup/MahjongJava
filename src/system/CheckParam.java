package system;

import java.util.ArrayList;
import java.util.List;

/**
 * 役判定のときに使われるパラメータクラス.
 * 役判定の中で取得される.
 */
public class CheckParam {

	// 牌リスト(面子依存しない役チェックに利用→タンヤオなど)
	private List<Hai> haiList;

	// 面子リスト(赤牌かどうかはここからはチェックしない)
	private List<Mentu> MentuList;

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
			this.MentuList = new ArrayList<Mentu>(MentuList);
		this.janto = p.janto;
		this.matiType = p.matiType;
	}
	
	public List<Hai> getHaiList() {
		return new ArrayList<Hai>(haiList);
	}

	public void setHaiList(List<Hai> haiList) {
		this.haiList = haiList;
	}

	public List<Mentu> getMentuList() {
		return MentuList == null ? null : new ArrayList<Mentu>(MentuList);
	}

	public void setMentuList(List<Mentu> mentuList) {
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
