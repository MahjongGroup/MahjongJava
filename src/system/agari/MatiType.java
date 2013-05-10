package system.agari;

import java.util.ArrayList;
import java.util.List;

import system.hai.HaiType;
import system.hai.Mentsu;
import system.hai.Mentsu.Type;

/**
 * 待ちの種類を表す。例えば,両面待ち,単騎待ちなどがある。
 */
public enum MatiType {
	TANKI(2) {
		@Override
		public boolean check(HaiType agari, Mentsu m, HaiType janto) {
			if (janto == agari)
				return true;
			return false;
		}

		@Override
		public boolean check(List<Mentsu> m, HaiType agari, HaiType janto) {
			if (janto == agari)
				return true;
			return false;
		}
	},
	RYANMEN(0) {
		@Override
		public boolean check(HaiType agari, Mentsu m, HaiType janto) {
			if (m.type() != Type.SYUNTU)
				return false;
			if (!m.contains(agari))
				return false;

			if (agari.number() < 7 && m.get(0).type() == agari)
				return true;
			if (agari.number() > 3 && m.get(2).type() == agari)
				return true;

			return false;
		}

		@Override
		public boolean check(List<Mentsu> mlist, HaiType agari, HaiType janto) {
			if (agari.isTsuhai())
				return false;

			for (Mentsu m : mlist) {
				if (check(agari, m, janto))
					return true;
			}

			return false;
		}
	},
	KANTYAN(2) {
		@Override
		public boolean check(HaiType agari, Mentsu m, HaiType janto) {
			// あがり牌が数牌でない場合
			if (agari.isTsuhai())
				return false;

			if (m.type() == Type.SYUNTU) {
				if (agari.suType() != m.get(0).type().suType())
					return false;

				if (m.get(1).type() == agari)
					return true;
			}
			return false;
		}

		@Override
		public boolean check(List<Mentsu> mlist, HaiType agari, HaiType janto) {
			// あがり牌が数牌でない場合
			if (agari.isTsuhai())
				return false;

			for (Mentsu m : mlist) {
				if (m.type() == Type.SYUNTU) {
					if (agari.suType() != m.get(0).type().suType())
						continue;

					if (m.get(1).type() == agari)
						return true;
				}
			}
			return false;
		}
	},
	PENTYAN(2) {
		@Override
		public boolean check(HaiType agari, Mentsu m, HaiType janto) {
			// あがり牌が数牌でない場合
			if (agari.isTsuhai())
				return false;

			int number = agari.number();
			if (number != 3 && number != 7)
				return false;

			if (m.type() == Type.SYUNTU) {
				if (agari.suType() != m.get(0).type().suType())
					return false;

				if (m.get(0).type().number() == 7)
					return true;
				if (m.get(2).type().number() == 3)
					return true;
			}
			return false;
		}

		@Override
		public boolean check(List<Mentsu> mlist, HaiType agari, HaiType janto) {
			// あがり牌が数牌でない場合
			if (agari.isTsuhai())
				return false;

			int number = agari.number();
			if (number != 3 && number != 7)
				return false;

			for (Mentsu m : mlist) {
				if (m.type() == Type.SYUNTU) {
					if (agari.suType() != m.get(0).type().suType())
						continue;

					if (m.get(0).type().number() == 7)
						return true;
					if (m.get(2).type().number() == 3)
						return true;
				}
			}
			return false;
		}
	},
	SYABO(0) {
		@Override
		public boolean check(HaiType agari, Mentsu m, HaiType janto) {
			if (m.type() != Type.KOTU)
				return false;

			if (m.get(0).type() == agari)
				return true;
			return false;
		}

		@Override
		public boolean check(List<Mentsu> mlist, HaiType agari, HaiType janto) {
			for (Mentsu m : mlist) {
				if (m.type() != Type.KOTU)
					continue;
				if (m.get(0).type() == agari)
					return true;
			}
			return false;
		}
	};

	private int hu;

	private MatiType(int hu) {
		this.hu = hu;
	}

	/**
	 * この待ちタイプの符を返す。
	 * 
	 * @return 符
	 */
	public int hu() {
		return this.hu;
	}

	/**
	 * 指定された面子とあがり牌からこの待ちの種類であるかどうかを判定する．
	 * 
	 * @param agari あがり牌の種類．
	 * @param m 待ちの種類を調べる面子．単騎を判定する場合はnullでよい．
	 * @param janto 雀頭の牌の種類．
	 * @return この待ちタイプであればtrue.
	 */
	public abstract boolean check(HaiType agari, Mentsu m, HaiType janto);

	/**
	 * 指定された面子リストと雀頭の組み合わせがこの待ちの種類であるかどうかを判定する.
	 * 
	 * @param mlist 面子リスト
	 * @param agari あがり牌の牌種
	 * @param janto 雀頭の牌種
	 * @return この待ちタイプであればtrue.
	 */
	public abstract boolean check(List<Mentsu> mlist, HaiType agari, HaiType janto);

	public static MatiType getMatiType(HaiType agari, Mentsu m, HaiType janto) {
		for (MatiType mt : values()) {
			if (mt.check(agari, m, janto)) {
				// exclude the case of TANKI
				if (mt == TANKI)
					continue;
				return mt;
			}
		}
		return null;
	}

	public static boolean isTanki(HaiType agari, HaiType janto) {
		return TANKI.check(null, agari, janto);
	}

	public static List<MatiType> getMatiType(List<Mentsu> mlist, HaiType janto, HaiType agari) {
		List<MatiType> ret = new ArrayList<MatiType>();
		for(MatiType mt : MatiType.values()) {
			if(mt.check(mlist, agari, janto)) {
				ret.add(mt);
			}
		}
		return ret;
	}
}
