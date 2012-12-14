package system;

import static system.MajanHai.GO_MAN;
import static system.MajanHai.GO_PIN;
import static system.MajanHai.ITI_MAN;
import static system.MajanHai.ITI_PIN;
import static system.MajanHai.KYU_MAN;
import static system.MajanHai.KYU_PIN;
import static system.MajanHai.NAN;
import static system.MajanHai.NANA_MAN;
import static system.MajanHai.NANA_PIN;
import static system.MajanHai.NI_MAN;
import static system.MajanHai.PE;
import static system.MajanHai.SAN_MAN;
import static system.MajanHai.SAN_PIN;
import static system.MajanHai.SYA;
import static system.MajanHai.TON;
import static system.MajanHai.YO_MAN;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class IndexMethod {
	private static final int zeroarr[];

	static {
		zeroarr = new int[37];
		Arrays.fill(zeroarr, 0);
	}

	public static int[] toIntArray(TehaiList list) {
		int n[] = zeroarr.clone();
		for (Hai hai : list) {
			n[hai.type().id()]++;
		}
		return n;
	}
	
	public static Integer[] calkKey2(int n[]) {
		List<Integer> ret = new ArrayList<Integer>();
		boolean flag = false;
		Integer elem = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 1; j <= 9; j++) {
				if (n[i * 10 + j] == 0) {
					if (flag) {
						ret.add(elem);
						elem = 0;
						flag = false;
					}
				} else {
					elem *= 10;
					elem += n[i * 10 + j];
					flag = true;
				}
			}
			if (flag) {
				ret.add(elem);
				elem = 0;
				flag = false;
			}
		}
		if (flag) {
			ret.add(elem);
			elem = 0;
			flag = false;
		}
		
		for (int i = 30; i <= 36; i++) {
			if (n[i] != 0) {
				ret.add(n[i]);
			}
		}
		return ret.toArray(new Integer[0]);
	}

	public static int calkKey(int n[]) {
		int ret = 1;
		boolean flag = true; // 0を置かないときtrueとなる.最初は置かないので初期値true

		for (int i = 0; i < 3; i++) {
			for (int j = 1; j <= 9; j++) {
				if (n[i * 10 + j] == 0) {
					if (flag)
						continue;
					flag = true;
					ret <<= 2;
					ret |= 0x2;
				} else {
					if (!flag) {
						ret <<= 1;
					} else {
						flag = false;
					}
					switch (n[i * 10 + j]) {
					case 2:
						ret <<= 2;
						ret |= 0x3;
						break;
					case 3:
						ret <<= 4;
						ret |= 0xF;
						break;
					case 4:
						ret <<= 6;
						ret |= 0x3F;
						break;
					}
				}
			}
		}

		if (!flag) {
			ret <<= 2;
			ret |= 0x2;
		}

		flag = false; // 一度でも1以上があればtrue
		for (int i = 30; i <= 36; i++) {
			if (n[i] != 0) {
				flag = true;
				switch (n[i]) {
				case 2:
					ret <<= 2;
					ret |= 0x3;
					break;
				case 3:
					ret <<= 4;
					ret |= 0xF;
					break;
				case 4:
					ret <<= 6;
					ret |= 0x3F;
					break;
				}
				ret <<= 2;
				ret |= 0x2;
			}
		}
		if (flag) {
			ret >>= 2;
			ret <<= 1;
		}

		return ret;
	}

	public static boolean equalsArrayAllZero(int a[]) {
		for (int i = 0; i < a.length; i++) {
			if(a[i] != 0) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isNMentsu1Janto(int pos[]) {
		for (int i = 1; i <= 36; i++) {
			if (pos[i] >= 2) {
				int temp[] = pos.clone();
				temp[i] -= 2;
				int temp2[] = temp.clone();

				remKotsu(temp);
				remSyuntsu(temp);

				if(equalsArrayAllZero(temp)) {
					return true;
				}
				
				remSyuntsu(temp2);
				remKotsu(temp2);

				if(equalsArrayAllZero(temp2)) {
					return true;
				}
			}
		}
		return false;
	}

	public static void remKotsu(int temp[]) {
		for (int i = 1; i <= 36; i++) {
			if (temp[i] >= 3) {
				temp[i] -= 3;
			}
		}
	}

	public static void remSyuntsu(int temp[]) {
		for (int j = 0; j < 2; j++) {
			for (int i = 0; i < 7; i++) {
				if (temp[(j * 10 + i)] >= 1 && temp[(j * 10 + i) + 1] >= 1 && temp[(j * 10 + i) + 2] >= 1) {
					temp[(j * 10 + i)] -= 1;
					temp[(j * 10 + i) + 1] -= 1;
					temp[(j * 10 + i) + 2] -= 1;
					i--;
				}
			}
		}
	}

	public static void main(String[] args) {
		// 1,2,2,2,3,3,3,4,4, TON,TON,TON, NAN,NAN
		TehaiList list = new TehaiList(Arrays.asList(new Hai[] { ITI_MAN, NI_MAN, NI_MAN, NI_MAN, SAN_MAN, SAN_MAN, SAN_MAN, YO_MAN, YO_MAN, TON, TON, TON, NAN, NAN }));
		// 
		TehaiList list2 = new TehaiList(Arrays.asList(new Hai[] { ITI_MAN, SAN_MAN, GO_MAN, NANA_MAN, KYU_MAN, ITI_PIN, SAN_PIN, GO_PIN, NANA_PIN, KYU_PIN, TON, NAN, SYA, PE }));
		
		System.out.println(Arrays.toString(calkKey2(toIntArray(list))));
		System.out.println(Arrays.toString(calkKey2(toIntArray(list2))));
		
//		System.out.println(list.size());
//		int n[] = toIntArray(list);
//		System.out.println(Arrays.toString(n));
//		int key = calkKey(n);
//		String bi = Integer.toBinaryString(key);
//		System.out.println(bi);
//		System.out.println(Integer.toBinaryString(calkKey(toIntArray(list2))));
//		int t = 0x3;
//		t >>= 1;
//		System.out.println(Integer.toBinaryString(t));
//		t <<= 1;
//		System.out.println(Integer.toBinaryString(t));
//		
//		System.out.println(isNMentsu1Janto(n));
//		System.out.println(isNMentsu1Janto(toIntArray(list2)));
//		
//		Map<Integer, Boolean> map = new TreeMap<Integer, Boolean>();
//
//		
//		System.out.println(Integer.toBinaryString(0x3) + " & " + Integer.toBinaryString(0x5));
//		System.out.println(16 & 17);
	}
}
