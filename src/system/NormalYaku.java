package system;

import java.util.List;
import java.util.Set;

public enum NormalYaku implements Yaku {
	RICHI("立直", 1, false, false, true) {
		@Override
		public boolean check(CheckerParam param) {
			return param.getFlagCheckYakuSet().contains(RICHI);
		}

	},
	YAKUHAI_HAKU("役牌　白", 1, true, false, false) {
		@Override
		public boolean check(CheckerParam param) {
			for (Mentu m : param.getMentuList()) {
				if (m.get(0).type() == HaiType.HAKU) {
					return true;
				}
			}
			return false;
		}
	},
	YAKUHAI_HATSU("役牌　撥", 1, true, false, false) {
		@Override
		public boolean check(CheckerParam param) {
			for (Mentu m : param.getMentuList()) {
				if (m.get(0).type() == HaiType.HATU) {
					return true;
				}
			}
			return false;
		}
	},
	YAKUHAI_TYUN("役牌　中", 1, true, false, false) {
		@Override
		public boolean check(CheckerParam param) {
			for (Mentu m : param.getMentuList()) {
				if (m.get(0).type() == HaiType.TYUN) {
					return true;
				}
			}
			return false;
		}
	},
	TANNYAO("断么九", 1, true, false, false) {
		@Override
		public boolean check(CheckerParam param) {
			for (Hai hai : param.getHaiList()) {
				if (!hai.type().isTyuntyanhai())
					return false;
			}
			return true;
		}
	},
	PINHU("平和", 1, false, false, false) {

		@Override
		public boolean check(CheckerParam param) {
			if (param.isNaki())
				return false;
			HaiType jantohai = param.getJanto();
			if (jantohai.group3() == HaiGroup3.SANGEN) {
				return false;
			}
			else if (jantohai.group3() == HaiGroup3.KAZE) {
				Kaze kaze = jantohai.kaze();
				if(kaze == param.getBakaze() || kaze == param.getJikaze())
					return false;
			}

			for (Mentu mentu : param.getMentuList()) {
				if (mentu.type() != Mentu.Type.SYUNTU) {
					return false;
				}
			}
			if (param.getMatiType() == MatiType.RYANMEN)
				return true;
			return false;
		}

	},
	TSUMO("門前清自摸和", 1, false, false, false) {
		@Override
		public boolean check(CheckerParam param) {
			if (param.isTsumo() && !param.isNaki())
				return true;
			return false;
		}
	},
	IPPATSU("一発", 1, false, false, true) {

		@Override
		public boolean check(CheckerParam param) {
			return param.getFlagCheckYakuSet().contains(IPPATSU);
		}

	},

	IPEKO("一盃口", 1, false, false, false) {

		@Override
		public boolean check(CheckerParam param) {
			// TODO Auto-generated method stub
			return false;
		}

	},
	HOTEI("河底撈魚", 1, true, false, true) {

		@Override
		public boolean check(CheckerParam param) {
			return param.getFlagCheckYakuSet().contains(HOTEI);
		}

	},
	HAITEI("海底摸月", 1, true, false, true) {

		@Override
		public boolean check(CheckerParam param) {
			return param.getFlagCheckYakuSet().contains(HAITEI);
		}

	},
	RINSYANKAIHO("嶺上開花", 1, true, false, true) {

		@Override
		public boolean check(CheckerParam param) {
			return param.getFlagCheckYakuSet().contains(RINSYANKAIHO);
		}

	},
	TYANKAN("搶槓", 1, true, false, true) {

		@Override
		public boolean check(CheckerParam param) {
			return param.getFlagCheckYakuSet().contains(TYANKAN);
		}

	},
	DABURURICHI("ダブル立直", 2, false, false, true) {

		@Override
		public boolean check(CheckerParam param) {
			return param.getFlagCheckYakuSet().contains(DABURURICHI);
		}

	},
	TOITOI("対々和", 2, true, false, false) {

		@Override
		public boolean check(CheckerParam param) {
			// TODO Auto-generated method stub
			return false;
		}

	},
	SANSYOKUDOJUN("三色同順", 2, true, true, false) {

		@Override
		public boolean check(CheckerParam param) {
			// TODO Auto-generated method stub
			return false;
		}

	},
	SANSYOKUDOKO("三色同刻", 2, true, false, false) {

		@Override
		public boolean check(CheckerParam param) {
			// TODO Auto-generated method stub
			return false;
		}

	},
	CHITOI("七対子", 2, false, false, false) {

		@Override
		public boolean check(CheckerParam param) {
			List<Hai> haiList = param.getHaiList();

			if (haiList.size() != 14) {
				return false;
			}

			List<HaiType> list = Functions.toHaiTypeListFromHaiCollection(haiList);

			Set<HaiType> haiSet = Functions.getHaiTypeSetFromHaiType(list);
			for (HaiType haiType : haiSet) {
				if (Functions.sizeOfHaiTypeList(haiType, list) != 2)
					return false;
			}
			return true;
		}

	},
	IKKI("一気通貫", 2, true, true, false) {

		@Override
		public boolean check(CheckerParam param) {
			// TODO Auto-generated method stub
			return false;
		}

	},
	TYANTA("チャンタ", 2, true, true, false) {

		@Override
		public boolean check(CheckerParam param) {
			// TODO Auto-generated method stub
			return false;
		}

	},
	SANNANKO("三暗刻", 2, true, false, false) {

		@Override
		public boolean check(CheckerParam param) {
			// TODO Auto-generated method stub
			return false;
		}

	},
	SYOSANGEN("小三元", 2, true, false, false) {

		@Override
		public boolean check(CheckerParam param) {
			// TODO Auto-generated method stub
			return false;
		}

	},
	HONROTO("混老頭", 2, true, false, false) {

		@Override
		public boolean check(CheckerParam param) {
			// TODO Auto-generated method stub
			return false;
		}

	},
	SANKANTSU("三槓子", 2, true, false, false) {

		@Override
		public boolean check(CheckerParam param) {
			// TODO Auto-generated method stub
			return false;
		}

	},
	JUNTYAN("順チャンタ", 3, true, true, false) {

		@Override
		public boolean check(CheckerParam param) {
			// TODO Auto-generated method stub
			return false;
		}

	},
	HONNITSU("混一色", 3, true, true, false) {

		@Override
		public boolean check(CheckerParam param) {
			// TODO Auto-generated method stub
			return false;
		}

	},
	RYANPEKO("二盃口", 3, false, false, false) {

		@Override
		public boolean check(CheckerParam param) {
			// TODO Auto-generated method stub
			return false;
		}

	},
	CHINNITSU("清一色", 6, true, false, false) {

		@Override
		public boolean check(CheckerParam param) {
			// TODO Auto-generated method stub
			return false;
		}

	},
	;

	private final String notation;
	private final int hansu;
	private final boolean naki;
	private final boolean kuisagari;
	private final boolean flagCheck;

	private NormalYaku(String notation, int hansu, boolean naki, boolean kuisagari, boolean flag) {
		this.notation = notation;
		this.hansu = hansu;
		this.naki = naki;
		this.kuisagari = kuisagari;
		this.flagCheck = flag;
	}

	@Override
	public String notation() {
		return notation;
	}

	@Override
	public Type type() {
		return Type.NORMAL;
	}

	@Override
	public boolean flagCheck() {
		return flagCheck;
	}

	public int getHansu() {
		return hansu;
	}

	public boolean isNaki() {
		return naki;
	}

	public boolean isKuisagari() {
		return kuisagari;
	}

	@Override
	public String toString() {
		return notation;
	}

}
