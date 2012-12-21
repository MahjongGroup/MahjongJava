package system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import system.algo.PatternMethod;
import util.MyMath;

/**
 * ある一人のプレイヤーのあがったときの結果を表すクラス．役セットと点数などを持つ。
 */
public class AgariResult {
	private Set<Yaku> yakuSet;
	private int han;
	private int hu;
	private boolean yakuman;
	private int yakumanSize;
	private int doraSize;
	private int baseScore;
	private ScoreType scoreType;
	private Param chParam;
	private Field field;
	private Kaze hoju;

	private AgariResult(Param param, Field f, Kaze hoju) {
		this.chParam = param;
		this.field = f;
		this.yakuman = false;
		this.yakuSet = new HashSet<Yaku>();
		this.hoju = hoju;
	}

	/**
	 * @return the yakuSet
	 */
	public Set<Yaku> getYakuSet() {
		return yakuSet;
	}

	/**
	 * @return the han
	 */
	public int getHan() {
		return han;
	}

	/**
	 * @return the hu
	 */
	public int getHu() {
		return hu;
	}

	/**
	 * @return the chParam
	 */
	public Param getChParam() {
		return chParam;
	}

	/**
	 * @return the yakuman
	 */
	public boolean isYakuman() {
		return yakuman;
	}

	/**
	 * @return the yakumanSize
	 */
	public int getYakumanSize() {
		return yakumanSize;
	}

	/**
	 * 基本点を返す．子のロンあがりの場合の点数はこの基本点の4倍(親の場合は6倍)となる.
	 * @return the base score.
	 */
	public int getBaseScore() {
		return baseScore;
	}

	/**
	 * 子のロンあがりしたときの点数、つまり基本点を4倍して切り上げした点数を返す．
	 * @return the child score.
	 */
	public int getChildScore() {
		return MyMath.ceil(baseScore * 4, 2);
	}

	/**
	 * 親のロンあがりしたときの点数、つまり基本点を6倍して切り上げした点数を返す．
	 * @return the parent score.
	 */
	public int getParentScore() {
		return MyMath.ceil(baseScore * 6, 2);
	}

	/**
	 * @return the doraSize
	 */
	public int getDoraSize() {
		return doraSize;
	}

	/**
	 * @return the scoreType
	 */
	public ScoreType getScoreType() {
		return scoreType;
	}
	
	/**
	 * @return the mati type of this agari.
	 */
	public MatiType getMatiTye() {
		return chParam.getMatiType();
	}
	
	
	public Kaze getHojuKaze() {
		return hoju;
	}

	/**
	 * 面子の取り出しかたや、待ちの種類(両面待ちなど)などが既に決まっている場合に
	 * このメソッドを呼び出す．
	 * 
	 * @param p
	 * @param f
	 * @param doralist
	 * @param uralist
	 * @return
	 */
	public static AgariResult createTestAgariResult(Param p, Field f, List<HaiType> doralist, List<HaiType> uralist) {
		AgariResult result = new AgariResult(p, f, null);
		result.checkYaku();

		// 役満の場合
		if (result.isYakuman()) {
			result.calcYakumanSize();
		}
		// 通常役の場合
		else {
			result.calcHanAndHu(doralist, uralist);
		}
		result.calcBaseScore();
		return result;
	}

	/**
	 * あがり結果オブジェクトを生成し,それを返す。あがっていないときはnullを返す。
	 * 
	 * @param tehaiList 手牌リスト
	 * @param hurohaiList 副露牌リスト
	 * @param param チェッカーパラム
	 * @param f フィールドインスタンス．
	 * @param hoju 放銃したプレイヤーの風．
	 * @return あがり結果。あがっていない場合はnull。
	 */
	public static AgariResult createAgariResult(TehaiList tehaiList, HurohaiList hurohaiList, Param param, Field f, Kaze hoju, List<HaiType> doraList, List<HaiType> uraDoraList) {
		AgariResult result = new AgariResult(param, f, hoju);
		result.testOrder(tehaiList, hurohaiList);

		// 役満の場合
		if (result.isYakuman()) {
			result.calcYakumanSize();
		}
		// 通常役の場合
		else {
			result.calcHanAndHu(doraList, uraDoraList);
		}
		result.calcBaseScore();
		return result;
	}

	/**
	 * 役満サイズを計算する。
	 */
	public void calcYakumanSize() {
		for (Yaku yaku : this.yakuSet) {
			if (yaku instanceof Yakuman) {
				Yakuman yakuman = (Yakuman) yaku;
				if (yakuman.isDaburu(field.getRule())) {
					this.yakumanSize += 2;
				} else {
					this.yakumanSize += 1;
				}
			}
		}

	}

	/**
	 * 翻数と符数を計算する。
	 */
	public void calcHanAndHu(List<HaiType> doraList, List<HaiType> uraDoraList) {
		// 鳴いている場合は食い下がりを考慮する．
		if (chParam.isNaki()) {
			for (Yaku yaku : this.yakuSet) {
				if (yaku instanceof NormalYaku) {
					NormalYaku nYaku = (NormalYaku) yaku;
					this.han += nYaku.getHansu();
					if (nYaku.isKuisagari()) {
						this.han -= 1;
					}
				}
			}
		} else {
			for (Yaku yaku : this.yakuSet) {
				if (yaku instanceof NormalYaku) {
					NormalYaku nYaku = (NormalYaku) yaku;
					this.han += nYaku.getHansu();
				}
			}
		}

		// 赤ドラ確認
		for (Hai hai : chParam.getHaiList()) {
			if (hai.aka()) {
				this.doraSize += 1;
			}
		}

		List<Hai> haiList = chParam.getHaiList();

		// 表ドラ確認
		for (Hai hai : haiList) {
			for (HaiType haiType : doraList) {
				if (hai.type() == haiType)
					this.doraSize += 1;
			}
		}

		// 立直している場合は裏ドラ確認
		if (yakuSet.contains(NormalYaku.RICHI)) {
			for (Hai hai : haiList) {
				for (HaiType haiType : uraDoraList) {
					if (hai.type() == haiType)
						this.doraSize += 1;
				}
			}
		}
		this.han += this.doraSize;

		// 平和の場合は20符固定
		if (yakuSet.contains(NormalYaku.PINHU)) {
			this.hu = 20;
			if (!chParam.isTsumo()) {
				this.hu += 10;
			}
		}
		// 七対子の場合は25符固定
		else if (yakuSet.contains(NormalYaku.CHITOI)) {
			this.hu = 25;
		}
		// それ以外の場合
		else {
			this.hu = 20;

			if (chParam.isTsumo()) {
				this.hu += 2;
			} else if (!chParam.isNaki()) {
				this.hu += 10;
			}

			for (Mentu m : chParam.getMentuList()) {
				this.hu += m.calcHu();
			}
			this.hu += chParam.getMatiType().hu();

			HaiType jantoType = chParam.getJanto();
			if (jantoType.group3() == HaiGroup3.KAZE) {
				Kaze jantoKaze = jantoType.kaze();
				if (jantoKaze == field.getBakaze() || jantoKaze == chParam.getJikaze()) {
					this.hu += 2;
				}
			} else if (jantoType.group3() == HaiGroup3.SANGEN) {
				this.hu += 2;
			}

			if (this.hu % 10 > 0) {
				this.hu = (this.hu / 10) * 10 + 10;
			}
		}
	}

	/**
	 * 基本点を計算する。
	 */
	public void calcBaseScore() {
		// 役満の場合
		if (this.yakuman) {
			this.baseScore = this.yakumanSize * 8000;
			this.scoreType = ScoreType.YAKUMAN;
		}
		// 通常役の場合
		else {
			this.baseScore = this.hu * (int) Math.pow(2, this.han + 2);

			if (baseScore >= 2000) {
				if (this.han <= 5) {
					this.baseScore = 2000;
					this.scoreType = ScoreType.MANGAN;
				} else if (this.han <= 7) {
					this.baseScore = 3000;
					this.scoreType = ScoreType.HANEMAN;
				} else if (this.han <= 10) {
					this.baseScore = 4000;
					this.scoreType = ScoreType.BAIMAN;
				} else if (this.han <= 12) {
					this.baseScore = 6000;
					this.scoreType = ScoreType.SANBAIMAN;
				} else {
					this.baseScore = 8000;
					this.scoreType = ScoreType.YAKUMAN;
				}
			} else {
				this.scoreType = ScoreType.NORMAL;
			}
		}
	}

	private void testOrder(TehaiList tehaiList, HurohaiList hurohaiList) {
		// チェックパラムに上がり牌,副露牌を含めた牌リストをセット
		List<Hai> haiList = new ArrayList<Hai>(tehaiList);
		haiList.add(chParam.getAgariHai());
		for (Mentu m : hurohaiList) {
			haiList.addAll(m.asList());
		}
		chParam.setHaiList(haiList);

		List<Hai> tehaiPlusAgariHai = new ArrayList<Hai>(tehaiList);
		tehaiPlusAgariHai.add(chParam.getAgariHai());

		PatternMethod.Value pvalue = PatternMethod.getValue(tehaiPlusAgariHai);
		if (pvalue.isSuccessful()) {
			if (pvalue.isIkkiTsukan()) {
				chParam.addFlagCheckYaku(NormalYaku.IKKI);
			}
			else if (pvalue.isRyanpeko()) {
				chParam.addFlagCheckYaku(NormalYaku.RYANPEKO);
			}

			List<AgariResult> agarilist = new ArrayList<AgariResult>(2);
			if (pvalue.isKotsuRm()) {
				NMentsu1Janto nmj = NMentsu1Janto.newInstanceFromKotsu(tehaiPlusAgariHai);
				Param p = new Param(chParam);
				p.setJanto(nmj.getJanto());
				
				if (pvalue.isIpekoByKotsuRm()) {
					p.addFlagCheckYaku(NormalYaku.IPEKO);
				}
				
				agarilist.add(checkYakuOnMentsuFetchedEnd(hurohaiList, nmj, p));
			}
			if (pvalue.isSyuntsuRm()) {
				NMentsu1Janto nmj = NMentsu1Janto.newInstanceFromSyuntsu(tehaiPlusAgariHai);
				Param p = new Param(chParam);
				p.setJanto(nmj.getJanto());
				
				if (pvalue.isIpekoBySyuntsuRm()) {
					p.addFlagCheckYaku(NormalYaku.IPEKO);
				}

				agarilist.add(checkYakuOnMentsuFetchedEnd(hurohaiList, nmj, p));
			}

			AgariResult agari = null;
			int max = 0;
			for (AgariResult ar : agarilist) {
				int sc = ar.getBaseScore();
				if(sc > max) {
					max = sc;
					agari = ar;
				}
			}
			
			this.yakuSet = agari.yakuSet;
			this.yakuman = agari.yakuman;
			this.chParam = agari.chParam;
		} else {
			chParam.setJanto(null);
			checkYaku();
		}
	}

	/**
	 * あがり牌をどこの面子、あるいは雀頭に含めるものかを考えるために、あがり牌の場所で変わる
	 * 平和、三暗刻、符のみから基本点を計算して、それを返す．
	 * 
	 * 様々な待ちの種類(両面、単騎など)をとれる場合はこの関数が最も高い点数を返す場合を採用しなければならない．
	 * 
	 * @param param チェックパラム．
	 * @return 平和、三暗刻、符のみから基本点.
	 */
	private int checkMatiType(Param param) {
		// この時点で待ちの種類(両面、単騎など)や面子は確定している
		int han = 0;
		int hu = 0;
		boolean pinhu = false;

		if (NormalYaku.PINHU.check(param, field)) {
			han += NormalYaku.PINHU.getHansu();
			pinhu = true;
		}
		if (NormalYaku.SANNANKO.check(param, field)) {
			han += NormalYaku.SANNANKO.getHansu();
		}

		// 平和の場合は20符固定
		if (pinhu) {
			hu = 20;
			if (!param.isTsumo()) {
				hu += 10;
			}
		} else {
			hu = 20;

			if (param.isTsumo()) {
				hu += 2;
			} else if (!param.isNaki()) {
				hu += 10;
			}

			for (Mentu m : param.getMentuList()) {
				hu += m.calcHu();
			}
			hu += param.getMatiType().hu();

			HaiType jantoType = param.getJanto();
			if (jantoType.group3() == HaiGroup3.KAZE) {
				Kaze jantoKaze = jantoType.kaze();
				if (jantoKaze == field.getBakaze() || jantoKaze == param.getJikaze()) {
					hu += 2;
				}
			} else if (jantoType.group3() == HaiGroup3.SANGEN) {
				hu += 2;
			}

			if (hu % 10 > 0) {
				hu = (hu / 10) * 10 + 10;
			}
		}

		return hu * (int) Math.pow(2, han + 2);
	}

	/**
	 * 
	 * @param mentumap
	 * @return the mentu whose score is max, null if janto is max score.
	 */
	private Mentu getMaxScoreMentu(Map<Mentu, Param> mentumap) {
		int max = 0;
		Mentu ret = null;
		for (Mentu m : mentumap.keySet()) {
			int sc = checkMatiType(mentumap.get(m));
			if (sc > max) {
				max = sc;
				ret = m;
			}
		}
		return ret;
	}

	private boolean checkYaku() {
		// 役満をチェックする
		for (Yakuman yaku : Yakuman.values()) {
			if (chParam.getMentuList() != null) {
				if (yaku.check(chParam, field)) {
					yakuSet.add(yaku);
				}
			} else {
				if (!yaku.is4Mentu1Janto() && yaku.check(chParam, field)) {
					yakuSet.add(yaku);
				}
			}
		}

		// 役満である
		if (yakuSet.size() > 0) {

			// かぶっている役満(国士無双と国士無双13面待ちなど)を削除する。
			if (yakuSet.contains(Yakuman.KOKUSIMUSOU) && yakuSet.contains(Yakuman.KOKUSIMUSOU_13MEN)) {
				yakuSet.remove(Yakuman.KOKUSIMUSOU);
			}
			if (yakuSet.contains(Yakuman.SUANKO) && yakuSet.contains(Yakuman.SUANKO_TANKI)) {
				yakuSet.remove(Yakuman.SUANKO);
			}
			if (yakuSet.contains(Yakuman.SYOSUSHI) && yakuSet.contains(Yakuman.DAISUSHI)) {
				yakuSet.remove(Yakuman.SYOSUSHI);
			}
			if (yakuSet.contains(Yakuman.TYURENPOTO) && yakuSet.contains(Yakuman.JUNTYANTYUREN)) {
				yakuSet.remove(Yakuman.TYURENPOTO);
			}

			this.yakuman = true;
			return true;
		}

		// 4面子1雀頭である
		if (chParam.getMentuList() != null) {
			for (Yaku yaku : NormalYaku.values()) {
				if (yaku == NormalYaku.CHITOI)
					continue;
				if (yaku.check(chParam, field))
					yakuSet.add(yaku);
			}
		}
		// 4面子1雀頭でない
		else {
			if (NormalYaku.CHITOI.check(chParam, field)) {
				yakuSet.add(NormalYaku.CHITOI);

				for (Yaku yaku : NormalYaku.values()) {
					if (yaku == NormalYaku.CHITOI)
						continue;
					if (!yaku.is4Mentu1Janto() && yaku.check(chParam, field))
						yakuSet.add(yaku);
				}
			}
		}

		if (yakuSet.size() == 0)
			return false;

		// かぶっている役(混一色と清一色など)を削除する。
		if (yakuSet.contains(NormalYaku.HONNITSU) && yakuSet.contains(NormalYaku.CHINNITSU)) {
			yakuSet.remove(NormalYaku.HONNITSU);
		}
		if (yakuSet.contains(NormalYaku.IPEKO) && yakuSet.contains(NormalYaku.RYANPEKO)) {
			yakuSet.remove(NormalYaku.IPEKO);
		}
		if (yakuSet.contains(NormalYaku.RICHI) && yakuSet.contains(NormalYaku.DABURURICHI)) {
			yakuSet.remove(NormalYaku.RICHI);
		}
		if (yakuSet.contains(NormalYaku.TYANTA) && yakuSet.contains(NormalYaku.JUNTYAN)) {
			yakuSet.remove(NormalYaku.TYANTA);
		}
		if (yakuSet.contains(NormalYaku.TYANTA) && yakuSet.contains(NormalYaku.HONROTO)) {
			yakuSet.remove(NormalYaku.TYANTA);
		}

		return true;
	}

	private AgariResult checkYakuOnMentsuFetchedEnd(HurohaiList hlist, List<Mentu> fetchedMentu, Param param) {
		// この時点では面子の取り出しは終了している
		Map<Mentu, Param> mentumap = new HashMap<Mentu, Param>();
		for (Mentu mentu : fetchedMentu) {
			if (mentu.contains(param.getAgariHai().type())) {
				List<Mentu> mentulist = new ArrayList<Mentu>(fetchedMentu);
				mentulist.addAll(hlist);
				if(!param.isTsumo()) {
					mentulist.remove(mentu);
					mentulist.add(new Mentu(mentu, hoju, param.getAgariHai()));
				}
				Param p = new Param(param);
				p.setMentuList(mentulist);
				p.setMatiType(MatiType.getMatiType(param.getAgariHai().type(), mentu, param.getJanto()));
				mentumap.put(mentu, p);
			}
		}
		if (MatiType.isTanki(param.getAgariHai().type(), param.getJanto())) {
			List<Mentu> mentulist = new ArrayList<Mentu>(fetchedMentu);
			mentulist.addAll(hlist);
			Param p = new Param(param);
			p.setMentuList(mentulist);
			p.setMatiType(MatiType.TANKI);
			mentumap.put(null, p);
		}

		Mentu mentu = getMaxScoreMentu(mentumap);
		param = mentumap.get(mentu);
		
		return createTestAgariResult(param, field, new ArrayList<HaiType>(0), new ArrayList<HaiType>(0));
	}

	@Override
	public String toString() {
		return "AgariResult [yakuSet=" + yakuSet + ", han=" + han + ", hu=" + hu + ", yakuman=" + yakuman + ", yakumanSize=" + yakumanSize + ", doraSize=" + doraSize + ", score=" + baseScore + ", scoreType=" + scoreType + ", chParam=" + chParam + "]";
	}

}
