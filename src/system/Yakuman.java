package system;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum Yakuman implements Yaku {
	KOKUSIMUSOU("国士無双", false, false) {

		@Override
		public boolean check(Param param, Field field) {
			List<Hai> haiList = param.getHaiList();
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
		public boolean check(Param param, Field field) {
			Hai agariHai = param.getAgariHai();
			if (agariHai.type().isTyuntyanhai())
				return false;
			List<Hai> haiList = param.getHaiList();
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
		public boolean check(Param param, Field field) {
			List<Mentu> mlist = param.getMentuList();
			int anko = 0;
			for (Mentu mentu : mlist) {
				if (mentu.type() != Mentu.Type.SYUNTU && !mentu.isNaki()) {
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
		public boolean check(Param param, Field field) {
			List<Mentu> mlist = param.getMentuList();
			int anko = 0;
			for (Mentu mentu : mlist) {
				if (mentu.type() != Mentu.Type.SYUNTU && !mentu.isNaki()) {
					anko++;
				}
			}
			if (anko == 4 && param.getMatiType() == MatiType.TANKI)
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
		public boolean check(Param param, Field field) {
			List<Mentu> mlist = param.getMentuList();
			int sangenSize = 0;
			for (Mentu mentu : mlist) {
				if (mentu.get(0).type().group3() == HaiGroup3.SANGEN) {
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
		public boolean check(Param param, Field field) {
			for (Hai hai : param.getHaiList()) {
				HaiType haiType = hai.type();
				if (haiType.group2() != HaiGroup2.TU)
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
		public boolean check(Param param, Field field) {
			HaiType janto = param.getJanto();
			if (janto.isTyuntyanhai() || janto.group2() == HaiGroup2.TU)
				return false;
			List<Mentu> mlist = param.getMentuList();
			for (Mentu mentu : mlist) {
				if (mentu.type() == Mentu.Type.SYUNTU
						|| mentu.get(0).type().isTyuntyanhai()
						|| mentu.get(0).type().group2() == HaiGroup2.TU)
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
		public boolean check(Param param, Field field) {
			List<Mentu> mlist = param.getMentuList();
			int kanSize = 0;
			for (Mentu mentu : mlist) {
				if (mentu.type() == Mentu.Type.KANTU)
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
		public boolean check(Param param, Field field) {
			Hai hai = param.getAgariHai();
			if (hai.type().group2() == HaiGroup2.TU)
				return false;
			SuType suType = hai.type().suType();
			List<Hai> haiList = param.getHaiList();
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
			if (haiTypeList.get(0).suType() != suType)
				return false;
			return true;
		}

		@Override
		public boolean isDaburu(Rule rule) {
			return false;
		}
	},

	JUNTYANTYUREN("純正九蓮宝燈", false, true) {
		@Override
		public boolean check(Param param, Field field) {
			Hai hai = param.getAgariHai();
			if (hai.type().group2() == HaiGroup2.TU)
				return false;
			SuType suType = hai.type().suType();
			List<Hai> haiList = param.getHaiList();
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
		public boolean check(Param param, Field field) {
			List<Hai> haiList = param.getHaiList();
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
		public boolean check(Param param, Field field) {
			return param.getFlagCheckYakuSet().contains(TENHO);
		}

		@Override
		public boolean isDaburu(Rule rule) {
			return false;
		}

	},

	CHIHO("地和", true, false) {

		@Override
		public boolean check(Param param, Field field) {
			return param.getFlagCheckYakuSet().contains(CHIHO);
		}

		@Override
		public boolean isDaburu(Rule rule) {
			return false;
		}

	},

	SYOSUSHI("小四喜", false, true) {

		@Override
		public boolean check(Param param, Field field) {
			HaiType janto = param.getJanto();
			List<Mentu> mlist = param.getMentuList();
			if (janto.group3() != HaiGroup3.KAZE) {
				return false;
			}
			int kazeSize = 0;
			for (Mentu mentu : mlist) {
				if (mentu.get(0).type().group3() == HaiGroup3.KAZE) {
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
		public boolean check(Param param, Field field) {
			List<Mentu> mlist = param.getMentuList();
			int kazeSize = 0;
			for (Mentu mentu : mlist) {
				if (mentu.get(0).type().group3() == HaiGroup3.KAZE) {
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
	private static final Set<HaiType> KOKUSI_SET = new HashSet<HaiType>(13);

	static {
		KOKUSI_SET.add(HaiType.ITI_MAN);
		KOKUSI_SET.add(HaiType.KYU_MAN);
		KOKUSI_SET.add(HaiType.ITI_PIN);
		KOKUSI_SET.add(HaiType.KYU_PIN);
		KOKUSI_SET.add(HaiType.ITI_SOU);
		KOKUSI_SET.add(HaiType.KYU_SOU);
		KOKUSI_SET.add(HaiType.TON);
		KOKUSI_SET.add(HaiType.NAN);
		KOKUSI_SET.add(HaiType.SYA);
		KOKUSI_SET.add(HaiType.PE);
		KOKUSI_SET.add(HaiType.HAKU);
		KOKUSI_SET.add(HaiType.HATU);
		KOKUSI_SET.add(HaiType.TYUN);
	}

	public static final Set<HaiType> getKokusiSet() {
		return new HashSet<HaiType>(KOKUSI_SET);
	}

	/**
	 * 緑一色に必要な牌セット
	 */
	private static final Set<HaiType> RYUISO_SET = new HashSet<HaiType>(13);

	static {
		RYUISO_SET.add(HaiType.NI_SOU);
		RYUISO_SET.add(HaiType.SAN_SOU);
		RYUISO_SET.add(HaiType.YO_SOU);
		RYUISO_SET.add(HaiType.ROKU_SOU);
		RYUISO_SET.add(HaiType.HATI_SOU);
		RYUISO_SET.add(HaiType.HATU);
	}

	public static final Set<HaiType> getRyuisoSet() {
		return new HashSet<HaiType>(RYUISO_SET);
	}
}
