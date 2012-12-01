package system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import system.Mentu.Type;

/**
 * 待ちの種類を表す。例えば、両面待ち、単騎待ちなどがある。
 */
public enum MatiType {
	TANKI(2) {
		@Override
		public boolean check(Param param) {
			if(param.getJanto() == param.getAgariHai().type())
				return true;
			return false;
		}
	},
	RYANMEN(0) {
		@Override
		public boolean check(Param param) {
			HaiType agariType = param.getAgariHai().type();
			
			// あがり牌が数牌でない場合
			if(agariType.group2() != HaiGroup2.SU)
				return false;
			
			for (Mentu mentu : param.getMentuList()) {
				if(mentu.isNaki())
					continue;

				if(mentu.type() == Type.SYUNTU) {
					if(agariType.suType() != mentu.get(0).type().suType())
						continue;
					
					Mentu m = new Mentu(mentu);
					Collections.sort(m.asList(), Hai.HaiComparator.ASCENDING_ORDER);
					
					if(m.get(0).type().number() != 7 && m.get(0).type() == agariType)
						return true;
					if(m.get(2).type().number() != 3 && m.get(2).type() == agariType)
						return true;
				}
			}
			return false;
		}
	},
	KANTYAN(2) {
		@Override
		public boolean check(Param param) {
			HaiType agariType = param.getAgariHai().type();
			
			// あがり牌が数牌でない場合
			if(agariType.group2() != HaiGroup2.SU)
				return false;
			
			for (Mentu mentu : param.getMentuList()) {
				if(mentu.isNaki())
					continue;

				if(mentu.type() == Type.SYUNTU) {
					if(agariType.suType() != mentu.get(0).type().suType())
						continue;
					
					Mentu m = new Mentu(mentu);
					Collections.sort(m.asList(), Hai.HaiComparator.ASCENDING_ORDER);
					
					if(m.get(1).type() == agariType)
						return true;
				}
			}
			return false;
		}
	},
	PENTYAN(2) {
		@Override
		public boolean check(Param param) {
			HaiType agariType = param.getAgariHai().type();
			
			// あがり牌が数牌でない場合
			if(agariType.group2() != HaiGroup2.SU)
				return false;
			
			int number = agariType.number();
			if(number != 3 && number != 7)
				return false;
			
			for (Mentu mentu : param.getMentuList()) {
				if(mentu.isNaki())
					continue;
				
				if(mentu.type() == Type.SYUNTU) {
					
					if(agariType.suType() != mentu.get(0).type().suType())
						continue;
					
					Mentu m = new Mentu(mentu);
					Collections.sort(m.asList(), Hai.HaiComparator.ASCENDING_ORDER);
					
					if(m.get(0).type().number() == 7)
						return true;
					if(m.get(2).type().number() == 3)
						return true;
				}
			}
			return false;
		}
	},
	SYABO(0) {
		@Override
		public boolean check(Param param) {
			for (Mentu m : param.getMentuList()) {
				if(m.isNaki())
					continue;
				
				if(m.type() != Type.KOTU)
					continue;
				
				if(m.get(0).type() == param.getAgariHai().type())
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
	 * @return 符
	 */
	public int hu() {
		return this.hu;
	}
	
	public abstract boolean check(Param param);
	
	/**
	 * 上がり形から考えられる全ての待ちタイプのリストを返す。
	 * 
	 * @param param　チェッカーパラム
	 * @return　考えられる全ての待ちタイプのリスト
	 */
	public static List<MatiType> getMatiTypeList(Param param) {
		List<MatiType> result = new ArrayList<MatiType>();
		
		for (MatiType mt : values()) {
			if(mt.check(param)) {
				result.add(mt);
			}
		}
		return result;
	}
	
}
