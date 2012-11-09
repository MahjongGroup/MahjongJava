package system;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum Yakuman implements Yaku {
	KOKUSIMUSOU("国士無双", false) {

		@Override
		public boolean check(CheckerParam param) {
			List<Hai> haiList = param.getHaiList();
			if (haiList.size() != 14) {
				return false;
			}

			List<HaiType> list = Functions.toHaiTypeListFromHaiCollection(haiList);

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
	KOKUSIMUSOU_13MEN("国士無双13面待ち", false) {
		@Override
		public boolean check(CheckerParam param) {
			List<Hai> haiList = param.getHaiList();
			if (haiList.size() != 14) {
				return false;
			}

			List<HaiType> list = Functions.toHaiTypeListFromHaiCollection(haiList);

			for (HaiType haiType : KOKUSI_SET) {
				if (!list.remove(haiType))
					return false;
			}

			if (list.size() == 1 && KOKUSI_SET.contains(list.get(0)))
				return true;

			return false;

		}

		@Override
		public boolean isDaburu(Rule rule) {
			if (rule.isKokushi13menDaburu())
				return true;
			return false;
		}
	},
	SUANKO("四暗刻", false) {
		@Override
		public boolean check(CheckerParam param) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isDaburu(Rule rule) {
			return false;
		}
	},
	SUANKO_TANKI("四暗刻単騎待ち", false) {
		@Override
		public boolean check(CheckerParam param) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isDaburu(Rule rule) {
			if (rule.isSutanDaburu())
				return true;
			return false;
		}
	},
	DAISANGEN("大三元", false) {
		@Override
		public boolean check(CheckerParam param) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isDaburu(Rule rule) {
			return false;
		}
	},
	TSUISO("字一色", false) {
		@Override
		public boolean check(CheckerParam param) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isDaburu(Rule rule) {
			return false;
		}
	},
	CHINROTO("清老頭", false) {
		@Override
		public boolean check(CheckerParam param) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isDaburu(Rule rule) {
			return false;
		}
	},
	SUKANTSU("四槓子", false) {
		@Override
		public boolean check(CheckerParam param) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isDaburu(Rule rule) {
			return false;
		}
	},

	TYURENPOTO("九蓮宝燈", false) {
		@Override
		public boolean check(CheckerParam param) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isDaburu(Rule rule) {
			return false;
		}
	},
	RYUISO("緑一色", false) {
		@Override
		public boolean check(CheckerParam param) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isDaburu(Rule rule) {
			return false;
		}
	},

	TENHO("天和", true) {

		@Override
		public boolean check(CheckerParam param) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isDaburu(Rule rule) {
			return false;
		}

	},

	CHIHO("地和", true) {

		@Override
		public boolean check(CheckerParam param) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isDaburu(Rule rule) {
			return false;
		}

	},

	SYOSUSHI("小四喜", false) {

		@Override
		public boolean check(CheckerParam param) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isDaburu(Rule rule) {
			return false;
		}

	},

	DAISUSHI("大四喜", false) {

		@Override
		public boolean check(CheckerParam param) {
			// TODO Auto-generated method stub
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

	private Yakuman(String notation, boolean flag) {
		this.notation = notation;
		this.flagCheck = flag;
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
	public boolean flagCheck() {
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

}
