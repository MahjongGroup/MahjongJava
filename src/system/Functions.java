package system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import system.Mentu.Type;


public class Functions {
	
	public static int max(int a, int ...array) {
		int max = a;
		
		for (int i : array) {
			if(i > max)
				max = i;
		}
		return max;
	}

	public static int min(int a, int ...array) {
		int min = a;
		
		for (int i : array) {
			if(i < min)
				min = i;
		}
		return min;
	}
	
	/**
	 * 指定されたドラ表示牌から実際のドラ牌の種類を取得する.
	 * @return 実際のドラ牌の種類．
	 */
	public HaiType getDora(HaiType dora) {
		return null;
	}

	/**
	 * 指定された牌コレクションの中に含まれる指定された種類の牌の数を返す。
	 * 
	 * @param type 数が知りたい牌の種類
	 * @param c 牌コレクション
	 * @return 指定された種類の牌の数
	 */
	public static int sizeOf(HaiType type, Collection<? extends Hai> c) {
		int count = 0;

		for (Hai h : c) {
			HaiType t = h.type();
			if (type == t)
				count++;
		}
		return count;
	}
	
	public static Hai remove(HaiType type, List<? extends Hai> list){
		for (Iterator<? extends Hai> itr = list.iterator(); itr.hasNext();) {
			Hai hai = itr.next();
			if(hai.type() == type) {
				itr.remove();
				return hai;
			}
		}
		return null;
	}

	public static int sizeOfHaiTypeList(HaiType type, List<HaiType> list) {
		int count = 0;

		for (HaiType t : list) {
			if (type == t)
				count++;
		}
		return count;
	}
	
	public static boolean isSyuntu(Hai ...hais) {
		if (hais.length != 3)
			return false;
		if (!isAllSuHai(hais))
			return false;
		if (!isAllSameSuType(hais))
			return false;

		Hai haiArray[] = hais.clone();
		Arrays.sort(haiArray);

		int pre = 0;
		for (int i = 0; i < haiArray.length; i++) {
			if (pre == 0) {
				pre = haiArray[i].type().number();
			} else {
				if (haiArray[i].type().number() != pre + 1)
					return false;
				pre = haiArray[i].type().number();
			}
		}
		return true;
	}
	
	public static List<Hai> extract(List<? extends Hai> list, SuType suType) {
		List<Hai> result = new ArrayList<Hai>();
		for (Hai hai : list) {
			if(hai.type().isSuhai()){
				if(hai.type().suType() == suType) {
					result.add(hai);
				}
			}
		}
		return result;
	}

	public static boolean isAllSameSuType(Hai ...hais) {
		if (hais.length == 0)
			return true;
		if(!isAllSuHai(hais)) 
			return false;
		SuType type = hais[0].type().suType();
		for (int i = 1; i < hais.length; i++) {
			if (type != hais[i].type().suType())
				return false;
		}
		return true;
	}

	public static boolean isAllHayType(Hai ...hais) {
		if(hais.length == 0) 
			return true;
		HaiType type = hais[0].type();
		for (int i = 1; i < hais.length; i++) {
			if(hais[i].type() != type)
				return false;
		}
		return true;
	}
	
	public static boolean isAllSuHai(Hai ...hais) {
		for (Hai hai : hais) {
			if (hai.type().isTsuhai())
				return false;
		}
		return true;
	}

	public static boolean isAllTuHai(Hai ...hais) {
		for (Hai hai : hais) {
			if (hai.type().isSuhai())
				return false;
		}
		return true;
	}

	public static Mentu.Type getMentuType(Hai ...hais) {
		if(hais.length != 3 && hais.length != 4)
			throw new IllegalArgumentException();

		if(hais.length == 3) {
			if(Functions.isSyuntu(hais)){
				return Type.SYUNTU;
			}
			else if(Functions.isAllHayType(hais)){
				return Type.KOTU;
			}
			return null;
		}

		if(Functions.isAllHayType(hais)){
			return Type.KANTU;
		}

		return null;
	}
	
}
