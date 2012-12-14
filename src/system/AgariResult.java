package system;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import system.algo.PatternMethod;
import util.MyMath;

/**
 * 役セットと点数などをもつクラス。
 * 
 * @author kohei
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

	public AgariResult(Param param, Field f) {
		this.chParam = param;
		this.field = f;
		this.yakuman = false;
		this.yakuSet = new HashSet<Yaku>();
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
	 * あがり結果オブジェクトを生成し,それを返す。あがっていないときはnullを返す。
	 * 
	 * @param tehaiList 手牌リスト
	 * @param hurohaiList 副露牌リスト
	 * @param param チェッカーパラム
	 * @return あがり結果。あがっていない場合はnull。
	 */
	public static AgariResult createAgariResult(TehaiList tehaiList, HurohaiList hurohaiList, Param param, Field f, List<HaiType> doraList, List<HaiType> uraDoraList) {
		AgariResult result = new AgariResult(param, f);
		result.checkYaku(tehaiList, hurohaiList);

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

	/**
	 * 役をチェックする。
	 * 
	 * @param tehaiList 手牌リスト
	 * @param hurohaiList 副露牌リスト
	 * @return　成功した場合true。
	 */
	public boolean checkYaku(TehaiList tehaiList, HurohaiList hurohaiList) {
		// チェックパラムに上がり牌,副露牌を含めた牌リストをセット
		List<Hai> haiList = new ArrayList<Hai>(tehaiList);
		haiList.add(chParam.getAgariHai());
		for (Mentu m : hurohaiList) {
			haiList.addAll(m.asList());
		}
		chParam.setHaiList(haiList);

		List<Hai> tehaiPlusAgariHai = new ArrayList<Hai>(tehaiList);
		tehaiPlusAgariHai.add(chParam.getAgariHai());

		// 4面子1雀頭である
		PatternMethod.Value pvalue = PatternMethod.getValue(tehaiPlusAgariHai);
		if (pvalue.isSuccessful()) {
			if (pvalue.isIkkiTsukan()) {
				chParam.addFlagCheckYaku(NormalYaku.IKKI);
			}
			if (pvalue.isIpeko()) {
				chParam.addFlagCheckYaku(NormalYaku.IPEKO);
			} else if (pvalue.isRyanpeko()) {
				chParam.addFlagCheckYaku(NormalYaku.RYANPEKO);
			}
			AgariMethods.setMentuListAndJanto(tehaiList, chParam.getAgariHai(), hurohaiList, chParam);
			List<MatiType> matiTypeList = MatiType.getMatiTypeList(chParam);
			chParam.setMatiType(matiTypeList.get(0));
		} else {
			chParam.setMentuList(null);
			chParam.setJanto(null);
		}

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
			List<MatiType> matiTypeList = MatiType.getMatiTypeList(chParam);

			assert matiTypeList.size() > 0;

			boolean pinhu = false;

			// 両面待ちととれる場合			
			if (matiTypeList.contains(MatiType.RYANMEN)) {
				chParam.setMatiType(MatiType.RYANMEN);

				if (NormalYaku.PINHU.check(chParam, field)) {
					yakuSet.add(NormalYaku.PINHU);
					pinhu = true;

					for (Yaku yaku : NormalYaku.values()) {
						if (yaku == NormalYaku.PINHU)
							continue;
						if (yaku == NormalYaku.CHITOI)
							continue;
						if (yaku.check(chParam, field))
							yakuSet.add(yaku);
					}

				} else if (matiTypeList.size() > 1) {
					if (!NormalYaku.PINHU.check(chParam, field)) {
						matiTypeList.remove(MatiType.RYANMEN);
					}
				}
			}
			if (!pinhu) {
				chParam.setMatiType(matiTypeList.get(0));

				for (Yaku yaku : NormalYaku.values()) {
					if (yaku == NormalYaku.PINHU)
						continue;
					if (yaku == NormalYaku.CHITOI)
						continue;
					if (yaku.check(chParam, field))
						yakuSet.add(yaku);
				}
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

	@Override
	public String toString() {
		return "AgariResult [yakuSet=" + yakuSet + ", han=" + han + ", hu=" + hu + ", yakuman=" + yakuman + ", yakumanSize=" + yakumanSize + ", doraSize=" + doraSize + ", score=" + baseScore + ", scoreType=" + scoreType + ", chParam=" + chParam + "]";
	}

}
