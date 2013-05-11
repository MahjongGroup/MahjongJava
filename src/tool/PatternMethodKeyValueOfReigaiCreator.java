package tool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Integer(牌の並び) -> Byte(フラグ)を表す写像を生成する．
 * 牌の並びは連続している牌の並びを個数で表現したもので、例えば「1,1,1,2,2,3,3,4,4,4,4」のようなときは
 * 3224という整数値となる．
 * フラグは8bitのフラグで1bit目から「刻子からとれるか」、「順子からとれるか」、
 * 「二盃口か」、「順子からとって一盃口か」、「刻子からとって一盃口か」、「一気通貫か」を表す．残り2bitは未定である．
 * 
 * 2012/4/27追記
 * 残り2bitについて
 * 「例外的な牌の並びである」,[例外的な並びで一盃口である]
 * 
 * ある牌の並びの一盃口フラグ(あるいは二盃口、一気通貫フラグ)がたっているとき、三暗刻や対々和になることは決してない(恐らく)．
 * ->2012/4/27追記
 * 二盃口と三暗刻のフラグが同時に立つ場合あり！ 111122223333など
 * よって、「刻子からとれる」フラグ、「順子からとれる」フラグの立っているほうを選択すればよい.
 * 両方立っている場合は順子からを選択しなければならない．
 * 
 * また、牌の並び値は、雀頭を含める場合と含めない場合の２つものが考えられる．
 * 
 * @author kohei
 *
 */
public class PatternMethodKeyValueOfReigaiCreator {
	public static final String MAP1_NAME = "map1";
	public static final String MAP2_NAME = "map2";

	/**
	 * 指定された10進数の値を5進数に変換して、そのm+1桁目を返す？
	 * 0 -> 0, 1 -> 1, 2 -> 2, 3 -> 3, 4 -> 4, 5 -> 10, 6 -> 11, ...
	 * @return 
	 */
	public static int getNum(int num, int m) {
		return (num % (int) Math.pow(5, m + 1)) / (int) Math.pow(5, m);
	}

	/**
	 * 指定された整数のm+1桁目の値をを返す?
	 * @param num
	 * @param m 桁
	 * @return
	 */
	public static int getNum10rad(int num, int m) {
		return (num % (int) Math.pow(10, m + 1)) / (int) Math.pow(10, m);
	}

	/**
	 * 指定された10進数の整数が5進数に変換して何桁あるかを返す.
	 * @param num
	 * @return
	 */
	public static int getKeta(int num) {
		for (int i = 1; i < 20; i++) {
			if (num < (int) Math.pow(5, i)) {
				return i;
			}
		}
		return -1;
	}

	public static boolean isValidNum(int num[]) {
		int sum = 0;
		for (int i = 0; i < num.length; i++) {
			if (num[i] == 0) {
				return false;
			}
			sum += num[i];
		}
		return sum <= 14;
	}

	public static void removeKotsu(int num[]) {
		for (int i = 0; i < num.length; i++) {
			if (num[i] >= 3) {
				num[i] -= 3;
			}
		}
	}

	public static byte removeSyuntsu(int num[], int ipekoflag) {
		boolean peko = false;
		byte ret = 0;
		int ittsu = 0;
		for (int i = 0; i < num.length - 2; i++) {
			if (num[i] >= 1 && num[i + 1] >= 1 && num[i + 2] >= 1) {
				if (peko) {
					if ((ret & ipekoflag) != 0) {
						ret |= 0x4;
					} else {
						ret |= ipekoflag;
						peko = false;
					}
				} else {
					peko = true;
				}

				if (i == 0 && ittsu == 0) {
					ittsu = 1;
				} else if (i == 3 && ittsu == 1) {
					ittsu = 2;
				} else if (i == 6 && ittsu == 2) {
					ittsu = 3;
				}
				num[i] -= 1;
				num[i + 1] -= 1;
				num[i + 2] -= 1;
				i--;
			} else {
				peko = false;
			}
		}
		if (ittsu == 3) {
			ret |= 0x20;
		}
		return ret;
	}

	public static byte isNMentsu(int num[]) {
		byte ret = 0;
		byte flag = 0;

		int copy[] = num.clone();
		removeKotsu(copy);
		flag |= removeSyuntsu(copy, 0x10);
		if (equalsZero(copy)) {
			ret |= 0x1;
			ret |= flag;
		}

		flag = 0;
		copy = num.clone();
		flag |= removeSyuntsu(copy, 0x8);
		removeKotsu(copy);
		if (equalsZero(copy)) {
			ret |= 0x2;
			ret |= flag;
		}
		return ret;
	}

	public static byte isNMentsu1Janto(int num[]) {
		byte ret = 0;

		for (int j = 0; j < num.length; j++) {
			if (num[j] >= 2) {
				int copy[] = num.clone();
				byte flag = 0;
				copy[j] -= 2;
				removeKotsu(copy);
				flag |= removeSyuntsu(copy, 0x10);
				if (equalsZero(copy)) {
					ret |= 0x1;
					ret |= flag;
				}

				copy = num.clone();
				flag = 0;
				copy[j] -= 2;
				flag |= removeSyuntsu(copy, 0x8);
				removeKotsu(copy);
				if (equalsZero(copy)) {
					ret |= 0x2;
					ret |= flag;
				}

				if ((ret & 0x3) != 0) {
					return ret;
				}
				ret = 0;
			}
		}
		return 0;
	}

	public static boolean equalsZero(int array[]) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] != 0) {
				return false;
			}
		}
		return true;
	}

	public static int sumOfArray(int array[]) {
		int size = 0;
		for (int i : array) {
			size += i;
		}
		return size;
	}

	public static int getKeta10rad(int num) {
		for (int i = 1; i < 20; i++) {
			if (num < (int) Math.pow(10, i)) {
				return i;
			}
		}
		return -1;
	}

	public static int to10rad(int num) {
		int ret = 0;
		int keta = getKeta10rad(num);
		for (int i = 0; i < keta; i++) {
			ret += Math.pow(5, i) * getNum10rad(num, i);
		}
		return ret;
	}

	/**
	 * 順子と刻子の取り方を全通り試す
	 * @param arr
	 * @return
	 */
	public static boolean isNMentsu2(int arr[]) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] >= 3) {
				int tmp[] = arr.clone();
				tmp[i] -= 3;
				if (isNMentsu2(tmp)) {
					return true;
				}
			}
			if (i < arr.length - 2 && (arr[i] >= 1 && arr[i + 1] >= 1 && arr[i + 2] >= 1)) {
				int tmp[] = arr.clone();
				tmp[i] -= 1;
				tmp[i + 1] -= 1;
				tmp[i + 2] -= 1;
				if (isNMentsu2(tmp)) {
					return true;
				}
			}
		}
		if (equalsZero(arr))
			return true;
		return false;
	}

	/**
	 * 順子と刻子の取り方を全通り試す
	 * @param arr
	 * @return
	 */
	public static boolean isNMentsu1Janto2(int num[]) {
		for (int j = 0; j < num.length; j++) {
			if (num[j] >= 2) {
				int copy[] = num.clone();
				copy[j] -= 2;
				if (isNMentsu2(copy)) {
					return true;
				}
			}
		}
		return false;
	}

	public static void calc(BufferedWriter out) throws IOException {
		int num = 0;
		int keta = 0;

		Map<Integer, Byte> map1 = new HashMap<Integer, Byte>();
		Map<Integer, Byte> map2 = new HashMap<Integer, Byte>();

		while ((keta = getKeta(num++)) <= 9) {
			int arr[] = new int[keta];
			for (int m = 0; m < keta; m++) {
				arr[m] = getNum(num, m);
			}
			if (isValidNum(arr)) {
				int sum = sumOfArray(arr);
				if (sum % 3 == 0) {
					boolean flag = false;
					byte value = 0;
					if (((value = isNMentsu(arr)) & 0x3) != 0) {
						map1.put(num, value);
						flag = true;
					}
					if (isNMentsu2(arr)) {
						if (!flag) {
							System.out.println(num);
							System.out.println("M : " + Arrays.toString(arr));
						}
					} else {
						if (flag) {
							System.out.println("MN : " + Arrays.toString(arr));
						}
					}
				} else if (sum % 3 == 2) {
					boolean flag = false;
					byte value = 0;
					if (((value = isNMentsu1Janto(arr)) & 0x3) != 0) {
						map2.put(num, value);
						flag = true;
					}
					if (isNMentsu1Janto2(arr)) {
						if (!flag) {
							System.out.println(num);
							System.out.println("MJ : " + Arrays.toString(arr));
						}
					} else {
						if (flag) {
							System.out.println("MJN : " + Arrays.toString(arr));
						}
					}
				}
			}
		}
		
		
		//　例外パターンの挿入
		map1.put(344, (byte)0xc0);
		map1.put(964, (byte)0x40);
		map1.put(1088, (byte)0xc0);
		map1.put(1596, (byte)0xc0);
		map1.put(1708, (byte)0xc0);
		map1.put(4188, (byte)0x40);
		map1.put(4808, (byte)0x40);
		map1.put(7908, (byte)0xc0);
		
		map2.put(974, (byte)0x40);
		map2.put(1098, (byte)0xc0);
		map2.put(1598, (byte)0xc0);
		map2.put(1722, (byte)0xc0);
		map2.put(1846, (byte)0xc0);
		map2.put(2214, (byte)0x40);
		map2.put(2958, (byte)0xc0);
		map2.put(4198, (byte)0xc0);
		map2.put(4238, (byte)0xc0);
		map2.put(4818, (byte)0x40);
		
		map2.put(4822, (byte)0x40);
		map2.put(4858, (byte)0x40);
		map2.put(5442, (byte)0xc0);
		map2.put(6058, (byte)0x40);
		map2.put(7214, (byte)0x40);
		map2.put(7338, (byte)0xc0);
		map2.put(7918, (byte)0xc0);
		map2.put(7958, (byte)0xc0);
		map2.put(7982, (byte)0xc0);
		map2.put(8542, (byte)0xc0);
		
		map2.put(9158, (byte)0xc0);
		map2.put(10438, (byte)0xc0);
		map2.put(11058, (byte)0x40);
		map2.put(14158, (byte)0xc0);
		map2.put(20942, (byte)0x40);
		map2.put(24042, (byte)0x40);
		map2.put(35438, (byte)0xc0);
		map2.put(36058, (byte)0x40);
		map2.put(39542, (byte)0xc0);
		
		System.out.println("map1 : " + map1.size());
		System.out.println("map2 : " + map2.size());

		for (Integer key : map1.keySet()) {
			Byte value = map1.get(key);
			if (value != null) {
				int inpKey = getKey(key);
				System.out.print(inpKey);
				System.out.println(", " + value);
				out.write(String.format("%s.put(%s, (byte)%d);\n", MAP1_NAME, inpKey, value));
			}
		}

		System.out.println("");

		for (Integer key : map2.keySet()) {
			Byte value = map2.get(key);
			if (value != null) {
				int inpKey = getKey(key);
				System.out.print(inpKey);
				System.out.println(", " + value);
				out.write(String.format("%s.put(%s, (byte)%d);\n", MAP2_NAME, inpKey, value));
			}
		}

	}

	public static int getKey(int num) {
		int keta = getKeta(num);
		int ret = 0;
		for (int m = 0; m < keta; m++) {
			ret *= 10;
			ret += getNum(num, m);
		}
		return ret;
	}

	public static void main(String[] args) throws IOException {
		File file = new File("patternMethod.txt");
		if (!file.exists()) {
			file.createNewFile();
		}
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			calc(writer);
		} finally {
			writer.close();
		}
	}
}
