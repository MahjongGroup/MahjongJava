package system.yaku;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import system.Field;
import system.Rule;
import system.agari.AgariParam;
import system.agari.CheckParam;
import system.agari.MatiType;
import system.hai.Hai;
import system.hai.HaiType;
import system.hai.Mentsu;
import system.hai.SuType;

public enum Yakuman implements Yaku {
	KOKUSIMUSOU("国士無双", false, false) {

		@Override
		public boolean check(AgariParam agParam, CheckParam chParam, Field field) {
			List<Hai> haiList = chParam.getHaiList();
			if (haiList.size() != 14) {
				return false;
			}

			// List<HaiType> list =
			// Functions.toHaiTypeListFromHaiCollection(haiList);
			List<HaiType> list = HaiType.toHaiTypeList(haiList);

			for (HaiType haiType : KOKUSI_SET) {
				if (!list.remove(haiType))
					return false;
			}

			if (list.size() != 1)
				return false;

			if (!KOKUSI_SET.contains(list.get(0)))
				return false;
			return true;
		}

		@Override
		public boolean isDaburu(Rule rule) {
			return false;
		}
	},
	KOKUSIMUSOU_13MEN("国士無双13面待ち", false, false) {
		@Override
		public boolean check(AgariParam agParam, CheckParam chParam, Field field) {
			Hai agariHai = agParam.getAgarihai();
			if (agariHai.type().isTyuntyanhai())
				return false;
			List<Hai> haiList = chParam.getHaiList();
			List<HaiType> list = HaiType.toHaiTypeList(haiList);
			list.remove(agariHai.type());

			for (HaiType haiType : KOKUSI_SET) {
				if (!list.remove(haiType))
					return false;
			}
			return true;
		}

		@Override
		public boolean isDaburu(Rule rule) {
			if (rule.isKokushi13menDaburu())
				return true;
			return false;
		}
	},
	SUANKO("四暗刻", false, true) {
		@Override
		public boolean check(AgariParam agParam, CheckParam chParam, Field field) {
			List<Mentsu> mlist = chParam.getMentuList();
			int anko = 0;
			for (Mentsu mentu : mlist) {
				if (mentu.type() != Mentsu.Type.SYUNTU && !mentu.isNaki()) {
					anko++;
				}
			}
			if (anko == 4)
				return true;
			return false;
		}

		@Override
		public boolean isDaburu(Rule rule) {
			return false;
		}
	},
	SUANKO_TANKI("四暗刻単騎待ち", false, true) {
		@Override
		public boolean check(AgariParam agParam, CheckParam chParam, Field field) {
			List<Mentsu> mlist = chParam.getMentuList();
			int anko = 0;
			for (Mentsu mentu : mlist) {
				if (mentu.type() != Mentsu.Type.SYUNTU && !mentu.isNaki()) {
					anko++;
				}
			}
			if (anko == 4 && chParam.getMatiType() == MatiType.TANKI)
				return true;
			return false;
		}

		@Override
		public boolean isDaburu(Rule rule) {
			if (rule.isSutanDaburu())
				return true;
			return false;
		}
	},
	DAISANGEN("大三元", false, true) {
		@Override
		public boolean check(AgariParam agParam, CheckParam chParam, Field field) {
			List<Mentsu> mlist = chParam.getMentuList();
			int sangenSize = 0;
			for (Mentsu mentu : mlist) {
				if (mentu.get(0).type().isSangenhai()) {
					sangenSize++;
				}
			}
			if (sangenSize == 3)
				return true;
			return false;
		}

		@Override
		public boolean isDaburu(Rule rule) {
			return false;
		}
	},
	TSUISO("字一色", false, false) {
		@Override
		public boolean check(AgariParam agParam, CheckParam chParam, Field field) {
			for (Hai hai : chParam.getHaiList()) {
				HaiType haiType = hai.type();
				if (!haiType.isTsuhai())
					return false;
			}
			return true;
		}

		@Override
		public boolean isDaburu(Rule rule) {
			return false;
		}
	},
	CHINROTO("清老頭", false, true) {
		@Override
		public boolean check(AgariParam agParam, CheckParam chParam, Field field) {
			HaiType janto = chParam.getJanto();
			if (janto.isTyuntyanhai() || janto.isTsuhai())
				return false;
			List<Mentsu> mlist = chParam.getMentuList();
			for (Mentsu mentu : mlist) {
				if (mentu.type() == Mentsu.Type.SYUNTU || mentu.get(0).type().isTyuntyanhai() || mentu.get(0).type().isTsuhai())
					return false;
			}
			return true;
		}

		@Override
		public boolean isDaburu(Rule rule) {
			return false;
		}
	},
	SUKANTSU("四槓子", false, true) {
		@Override
		public boolean check(AgariParam agParam, CheckParam chParam, Field field) {
			List<Mentsu> mlist = chParam.getMentuList();
			int kanSize = 0;
			for (Mentsu mentu : mlist) {
				if (mentu.type() == Mentsu.Type.KANTU)
					kanSize++;
			}
			if (kanSize == 4)
				return true;
			return false;
		}

		@Override
		public boolean isDaburu(Rule rule) {
			return false;
		}
	},

	TYURENPOTO("九蓮宝燈", false, true) {
		@Override
		public boolean check(AgariParam agParam, CheckParam chParam, Field field) {
			Hai hai = agParam.getAgarihai();
			if (hai.type().isTsuhai())
				return false;
			SuType suType = hai.type().suType();
			List<Hai> haiList = chParam.getHaiList();
			List<HaiType> haiTypeList = HaiType.toHaiTypeList(haiList);
			for (int i = 1; i < 10; i++) {
				HaiType TyurenHaiType = HaiType.valueOf(suType, i);
				if (i == 1 || i == 9) {
					for (int j = 0; j < 3; j++) {
						if (!haiTypeList.contains(TyurenHaiType))
							return false;
						haiTypeList.remove(TyurenHaiType);
					}
				} else if (!haiTypeList.contains(TyurenHaiType))
					return false;
				haiTypeList.remove(TyurenHaiType);
			}
			return true;
		}

		@Override
		public boolean isDaburu(Rule rule) {
			return false;
		}
	},

	JUNTYANTYUREN("純正九蓮宝燈", false, true) {
		@Override
		public boolean check(AgariParam agParam, CheckParam chParam, Field field) {
			Hai hai = agParam.getAgarihai();
			if (hai.type().isTsuhai())
				return false;
			SuType suType = hai.type().suType();
			List<Hai> haiList = chParam.getHaiList();
			List<HaiType> haiTypeList = HaiType.toHaiTypeList(haiList);
			haiTypeList.remove(hai.type());

			for (int i = 1; i < 10; i++) {
				HaiType TyurenHaiType = HaiType.valueOf(suType, i);
				if (i == 1 || i == 9) {
					for (int j = 0; j < 3; j++) {
						if (!haiTypeList.remove(TyurenHaiType))
							return false;
					}
				} else if (!haiTypeList.remove(TyurenHaiType))
					return false;
			}
			return true;
		}

		@Override
		public boolean isDaburu(Rule rule) {
			if (rule.isJuntyanDaburu())
				return true;
			return false;
		}
	},

	RYUISO("緑一色", false, true) {
		@Override
		public boolean check(AgariParam agParam, CheckParam chParam, Field field) {
			List<Hai> haiList = chParam.getHaiList();
			List<HaiType> haiTypeList = HaiType.toHaiTypeList(haiList);
			return !haiTypeList.retainAll(RYUISO_SET);
		}

		@Override
		public boolean isDaburu(Rule rule) {
			return false;
		}
	},

	TENHO("天和", true, false) {

		@Override
		public boolean check(AgariParam agParam, CheckParam chParam, Field field) {
			return agParam.getFlagCheckYakuSet().contains(TENHO);
		}

		@Override
		public boolean isDaburu(Rule rule) {
			return false;
		}

	},

	CHIHO("地和", true, false) {

		@Override
		public boolean check(AgariParam agParam, CheckParam chParam, Field field) {
			return agParam.getFlagCheckYakuSet().contains(CHIHO);
		}

		@Override
		public boolean isDaburu(Rule rule) {
			return false;
		}

	},

	SYOSUSHI("小四喜", false, true) {

		@Override
		public boolean check(AgariParam agParam, CheckParam chParam, Field field) {
			HaiType janto = chParam.getJanto();
			List<Mentsu> mlist = chParam.getMentuList();
			if (!janto.isKazehai()) {
				return false;
			}
			int kazeSize = 0;
			for (Mentsu mentu : mlist) {
				if (mentu.get(0).type().isKazehai()) {
					kazeSize++;
				}
			}
			if (kazeSize == 3)
				return true;
			return false;
		}

		@Override
		public boolean isDaburu(Rule rule) {
			return false;
		}

	},

	DAISUSHI("大四喜", false, true) {

		@Override
		public boolean check(AgariParam agParam, CheckParam chParam, Field field) {
			List<Mentsu> mlist = chParam.getMentuList();
			int kazeSize = 0;
			for (Mentsu mentu : mlist) {
				if (mentu.get(0).type().isKazehai()) {
					kazeSize++;
				}
			}
			if (kazeSize == 4)
				return true;
			return false;
		}

		@Override
		public boolean isDaburu(Rule rule) {
			if (rule.isDaisushiDaburu())
				return true;
			return false;
		}

	},

	;

	private final String notation;
	private final boolean flagCheck;
	private final boolean kouseiFlag;

	private Yakuman(String notation, boolean flag, boolean kousei) {
		this.notation = notation;
		this.flagCheck = flag;
		this.kouseiFlag = kousei;
	}

	@Override
	public boolean is4Mentu1Janto() {
		return kouseiFlag;
	}

	@Override
	public String notation() {
		return notation;
	}

	@Override
	public Type type() {
		return Type.YAKUMAN;
	}

	@Override
	public boolean isFlagCheck() {
		return flagCheck;
	}

	public abstract boolean isDaburu(Rule rule);

	@Override
	public String toString() {
		return notation;
	}

	/**
	 * 国士無双に必要な牌セット
	 */
	private static final Set<HaiType> KOKUSI_SET;

	static {
		Set<HaiType> set = new HashSet<HaiType>(6);
		set.add(HaiType.ITI_MAN);
		set.add(HaiType.KYU_MAN);
		set.add(HaiType.ITI_PIN);
		set.add(HaiType.KYU_PIN);
		set.add(HaiType.ITI_SOU);
		set.add(HaiType.KYU_SOU);
		set.add(HaiType.TON);
		set.add(HaiType.NAN);
		set.add(HaiType.SYA);
		set.add(HaiType.PE);
		set.add(HaiType.HAKU);
		set.add(HaiType.HATU);
		set.add(HaiType.TYUN);
		KOKUSI_SET = Collections.unmodifiableSet(set);
	}

	/**
	 * 国士無双の牌セットを返す．この牌セットは変更することが出来ない．
	 * @return 国士無双の牌セット.
	 */
	public static final Set<HaiType> getKokusiSet() {
		return KOKUSI_SET;
	}

	/**
	 * 緑一色に必要な牌セット
	 */
	private static final Set<HaiType> RYUISO_SET;

	static {
		Set<HaiType> set = new HashSet<HaiType>(6);
		set.add(HaiType.NI_SOU);
		set.add(HaiType.SAN_SOU);
		set.add(HaiType.YO_SOU);
		set.add(HaiType.ROKU_SOU);
		set.add(HaiType.HATI_SOU);
		set.add(HaiType.HATU);
		RYUISO_SET = Collections.unmodifiableSet(set);
	}

	/**
	 * 緑一色の牌セットを返す．この牌セットは変更することが出来ない．
	 * @return 緑一色の牌セット.
	 */
	public static final Set<HaiType> getRyuisoSet() {
		return RYUISO_SET;
	}
}
