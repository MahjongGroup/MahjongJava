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
 * ある一人のプレイヤーのあがったときの結果を表すクラス．役セットと点数などを持つ.
 * このクラスのオブジェクトを作成するには内部クラスのBuilderオブジェクトを生成する.
 * 
 */
public class AgariResult {
	private final Set<Yaku> yakuSet;
	private final int han;
	private final int hu;
	private final boolean yakuman;
	private final int yakumanSize;
	private final int doraSize;
	private final int baseScore;
	private final ScoreType scoreType;

	private AgariResult(Set<Yaku> yaku, int han, int hu, boolean yakuman, int yakumanSize, int doraSize, int baseScore, ScoreType scoreType) {
		this.yakuSet = yaku;
		this.han = han;
		this.hu = hu;
		this.yakuman = yakuman;
		this.yakumanSize = yakumanSize;
		this.doraSize = doraSize;
		this.baseScore = baseScore;
		this.scoreType = scoreType;
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

	@Override
	public String toString() {
		return "AgariResult [yakuSet=" + yakuSet + ", han=" + han + ", hu=" + hu + ", yakuman=" + yakuman + ", yakumanSize=" + yakumanSize + ", doraSize=" + doraSize + ", score=" + baseScore + ", scoreType=" + scoreType + "]";
	}

	/**
	 * AgariResultオブジェクトを生成するビルダークラス.
	 * このオブジェクトを生成するには、手牌リスト、副露牌リストなどが必要である.
	 * このオブジェクトを生成するとき、役判定、翻数計算、点数計算をする.
	 * また天和や一発などの役は事前に判定して、そのフラグリストを引数として加える.
	 * 
	 */
	public static class Builder {
		private TehaiList tehai;
		private HurohaiList huro;
		private Field field;
		private Kaze hoju;
		private List<HaiType> odora;
		private List<HaiType> udora;
		private CheckParam chParam;
		private AgariParam agariParam;
		private Set<Yaku> yakuFlag;

		private int han;
		private int hu;
		private boolean yakuman;
		private Set<Yaku> yakuSet;
		private int yakumanSize;
		private int doraSize;
		private int baseScore;
		private ScoreType scoreType;

		public Builder() {
			this.chParam = new CheckParam();
		}

		public void setTehaiList(TehaiList tehai) {
			this.tehai = tehai;
		}

		public void setHurohaiList(HurohaiList huro) {
			this.huro = huro;
		}

		public void setField(Field f) {
			this.field = f;
		}

		public void setHojuKaze(Kaze hoju) {
			this.hoju = hoju;
		}

		public void setOpenDoraList(List<HaiType> dora) {
			this.odora = dora;
		}
		
		public void setUraDoraList(List<HaiType> dora) {
			this.udora = dora;
		}

		public void setAgariParam(AgariParam ap) {
			this.agariParam = ap;
		}
		
		public void setCheckParam(CheckParam p) {
			this.chParam = p;
		}
		
		public void setYakuFlag(Set<Yaku> yaku) {
			this.yakuFlag = yaku;
		}

		public boolean isValidState() {
			if (tehai == null || huro == null || field == null || odora == null || udora == null || yakuFlag == null || agariParam == null) {
				return false;
			}
			return true;
		}

		public AgariResult build() {
			if(!isValidState())
				throw new IllegalStateException();
			if(chParam == null)
				chParam = new CheckParam();
			
			if(!testOrder(tehai, huro)) {
				checkYaku();
				// 役満の場合
				if (yakuman) {
					calcYakumanSize();
				}
				// 通常役の場合
				else {
					calcHanAndHu(odora, udora);
				}
				calcBaseScore();
			}

			AgariResult result = new AgariResult(yakuSet, han, hu, yakuman, yakumanSize, doraSize, baseScore, scoreType);
			return result;
		}
		
		
		/**
		 * すでに面子が確定しているとき.
		 * @return
		 */
		public AgariResult buildTest() {
			if(!isValidState() )
				throw new IllegalStateException();
			if(chParam == null)
				chParam = new CheckParam();
			
			checkYaku();

			// 役満の場合
			if (yakuman) {
				calcYakumanSize();
			}
			// 通常役の場合
			else {
				calcHanAndHu(odora, udora);
			}
			calcBaseScore();
			AgariResult result = new AgariResult(yakuSet, han, hu, yakuman, yakumanSize, doraSize, baseScore, scoreType);
			return result;
		}

		/**
		 * 刻子->順子、順子->刻子の２つの面子の取り出し方を試して点数の高い方を採用する.
		 * @param tehaiList
		 * @param hurohaiList
		 * @return 点数などが既に決まった場合true.
		 */
		private boolean testOrder(TehaiList tehaiList, HurohaiList hurohaiList) {
			// チェックパラムに上がり牌,副露牌を含めた牌リストをセット
			List<Hai> haiList = new ArrayList<Hai>(tehaiList);
			haiList.add(agariParam.getAgarihai());
			for (Mentu m : hurohaiList) {
				haiList.addAll(m.asList());
			}
			chParam.setHaiList(haiList);

			List<Hai> tehaiPlusAgariHai = new ArrayList<Hai>(tehaiList);
			tehaiPlusAgariHai.add(agariParam.getAgarihai());

			// n面子1雀頭かどうかをパターン法で判定する
			PatternMethod.Value pvalue = PatternMethod.getValue(tehaiPlusAgariHai);

			// n面子1雀頭の場合
			if (pvalue.isSuccessful()) {
				if (pvalue.isIkkiTsukan()) {
					agariParam.addFlagCheckYaku(NormalYaku.IKKI);
				}
				// 一盃口は順子からとるか刻子からとるかで変わるのでここでは判定しない.
				else if (pvalue.isRyanpeko()) {
					agariParam.addFlagCheckYaku(NormalYaku.RYANPEKO);
				}

				List<AgariResult> agarilist = new ArrayList<AgariResult>(2);

				// 刻子からとれるかどうか
				if (pvalue.isKotsuRm()) {
					System.out.println("刻子");
					NMentsu1Janto nmj = NMentsu1Janto.newInstanceFromKotsu(tehaiPlusAgariHai);
					CheckParam p = new CheckParam(chParam);
					AgariParam agp = new AgariParam(agariParam);
					
					p.setJanto(nmj.getJanto());

					if (pvalue.isIpekoByKotsuRm()) {
						System.out.println("IPEKO");
						agp.addFlagCheckYaku(NormalYaku.IPEKO);
					}

					agarilist.add(checkYakuOnMentsuFetchedEnd(hurohaiList, nmj, p, agp));
				}
				// 順子からとれるかどうか
				if (pvalue.isSyuntsuRm()) {
					System.out.println("順子");
					NMentsu1Janto nmj = NMentsu1Janto.newInstanceFromSyuntsu(tehaiPlusAgariHai);
					CheckParam p = new CheckParam(chParam);
					AgariParam agp = new AgariParam(agariParam);

					p.setJanto(nmj.getJanto());

					if (pvalue.isIpekoBySyuntsuRm()) {
						System.out.println("IPEKO");
						agp.addFlagCheckYaku(NormalYaku.IPEKO);
					}

					agarilist.add(checkYakuOnMentsuFetchedEnd(hurohaiList, nmj, p, agp));
				}

				AgariResult agari = null;
				int max = 0;
				int max_han = 0;
				
				for (AgariResult ar : agarilist) {
					int sc = ar.getBaseScore();
					int han = ar.getHan();
					
					if (sc > max || (sc == max && han > max_han)) {
						max = sc;
						max_han = han;
						agari = ar;
					}
				}

				this.yakuSet = agari.yakuSet;
				this.yakuman = agari.yakuman;
				this.yakumanSize = agari.yakumanSize;
				this.baseScore = agari.baseScore;
				this.doraSize = agari.doraSize;
				this.han = agari.han;
				this.hu = agari.hu;
				this.scoreType = agari.scoreType;
				
				return true;
			}
			// n面子1雀頭でない場合
			else {
				chParam.setJanto(null);
				checkYaku();
			}
			return false;
		}

		/**
		 * 面子が既に取り出されてる時点での役判定.
		 * 待ちの種類(両面、単騎待ちなど)で点数が高くなるほうを採用する.
		 */
		private AgariResult checkYakuOnMentsuFetchedEnd(HurohaiList hlist, List<Mentu> fetchedMentu, CheckParam param, AgariParam agp) {
			// この時点では面子の取り出しは終了している
			Map<Mentu, CheckParam> mentumap = new HashMap<Mentu, CheckParam>();

			// あがり牌を含むすべての面子を確認する
			for (Mentu mentu : fetchedMentu) {
				if (mentu.contains(agp.getAgarihai().type())) {
					List<Mentu> mentulist = new ArrayList<Mentu>(fetchedMentu);
					mentulist.addAll(hlist);

					// ロンあがりの場合はその面子を明順子(または明刻子)にする.
					if (!agp.isTsumo()) {
						mentulist.remove(mentu);
						mentulist.add(new Mentu(mentu, hoju, agp.getAgarihai()));
					}

					CheckParam p = new CheckParam(param);
					p.setMentuList(mentulist);
					p.setMatiType(MatiType.getMatiType(agp.getAgarihai().type(), mentu, param.getJanto()));
					mentumap.put(mentu, p);
				}
			}
			
			// 単騎待ちの場合
			if (MatiType.isTanki(agp.getAgarihai().type(), param.getJanto())) {
				List<Mentu> mentulist = new ArrayList<Mentu>(fetchedMentu);
				mentulist.addAll(hlist);
				CheckParam p = new CheckParam(param);
				p.setMentuList(mentulist);
				p.setMatiType(MatiType.TANKI);

				// 単騎のキーはnullをつかう.
				mentumap.put(null, p);
			}

			Mentu mentu = getMaxScoreMentu(mentumap);
			param = mentumap.get(mentu);

			Builder builder = new Builder();
			builder.setCheckParam(param);
			builder.setAgariParam(agp);
			builder.setField(field);
			builder.setHojuKaze(hoju);
			builder.setHurohaiList(huro);
			builder.setOpenDoraList(odora);
			builder.setUraDoraList(udora);
			builder.setTehaiList(tehai);
			builder.setYakuFlag(yakuFlag);
			
			return builder.buildTest();
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
		private int checkMatiType(CheckParam param, AgariParam agp) {
			// この時点で待ちの種類(両面、単騎など)や面子は確定している
			int han = 0;
			int hu = 0;
			boolean pinhu = false;

			if (NormalYaku.PINHU.check(agp, param, field)) {
				han += NormalYaku.PINHU.getHansu();
				pinhu = true;
			}
			if (NormalYaku.SANNANKO.check(agp, param, field)) {
				han += NormalYaku.SANNANKO.getHansu();
			}

			// 平和の場合は20符固定
			if (pinhu) {
				hu = 20;
				if (!agp.isTsumo()) {
					hu += 10;
				}
			} else {
				hu = 20;

				if (agp.isTsumo()) {
					hu += 2;
				} else if (!agp.isNaki()) {
					hu += 10;
				}

				for (Mentu m : param.getMentuList()) {
					hu += m.calcHu();
				}
				hu += param.getMatiType().hu();

				HaiType jantoType = param.getJanto();
				if (jantoType.group3() == HaiGroup3.KAZE) {
					Kaze jantoKaze = jantoType.kaze();
					if (jantoKaze == field.getBakaze() || jantoKaze == agp.getJikaze()) {
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
		 * @param mentumap
		 * @return the mentu whose score is max, null if janto is max score.
		 */
		private Mentu getMaxScoreMentu(Map<Mentu, CheckParam> mentumap) {
			int max = 0;
			Mentu ret = null;
			for (Mentu m : mentumap.keySet()) {
				int sc = checkMatiType(mentumap.get(m), agariParam);
				if (sc > max) {
					max = sc;
					ret = m;
				}
			}
			return ret;
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
		public void calcHanAndHu(List<HaiType> odora, List<HaiType> udora) {
			// 鳴いている場合は食い下がりを考慮する．
			if (agariParam.isNaki()) {
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
				for (HaiType haiType : odora) {
					if (hai.type() == haiType)
						this.doraSize += 1;
				}
			}

			// 立直している場合は裏ドラ確認
			if (yakuSet.contains(NormalYaku.RICHI)) {
				for (Hai hai : haiList) {
					for (HaiType haiType : udora) {
						if (hai.type() == haiType)
							this.doraSize += 1;
					}
				}
			}
			this.han += this.doraSize;

			// 平和の場合は20符固定
			if (yakuSet.contains(NormalYaku.PINHU)) {
				this.hu = 20;
				if (!agariParam.isTsumo()) {
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

				if (agariParam.isTsumo()) {
					this.hu += 2;
				} else if (!agariParam.isNaki()) {
					this.hu += 10;
				}

				for (Mentu m : chParam.getMentuList()) {
					this.hu += m.calcHu();
				}
				this.hu += chParam.getMatiType().hu();

				HaiType jantoType = chParam.getJanto();
				if (jantoType.group3() == HaiGroup3.KAZE) {
					Kaze jantoKaze = jantoType.kaze();
					if (jantoKaze == field.getBakaze() || jantoKaze == agariParam.getJikaze()) {
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

		// 面子などから役を判定する.
		private boolean checkYaku() {
			this.yakuSet = new HashSet<Yaku>();
			
			// 役満をチェックする
			for (Yakuman yaku : Yakuman.values()) {
				if (chParam.getMentuList() != null) {
					if (yaku.check(agariParam, chParam, field)) {
						yakuSet.add(yaku);
					}
				} else {
					if (!yaku.is4Mentu1Janto() && yaku.check(agariParam, chParam, field)) {
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
					if (yaku.check(agariParam, chParam, field))
						yakuSet.add(yaku);
				}
			}
			// 4面子1雀頭でない
			else {
				if (NormalYaku.CHITOI.check(agariParam, chParam, field)) {
					yakuSet.add(NormalYaku.CHITOI);

					for (Yaku yaku : NormalYaku.values()) {
						if (yaku == NormalYaku.CHITOI)
							continue;
						if (!yaku.is4Mentu1Janto() && yaku.check(agariParam, chParam, field))
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

	}

}
