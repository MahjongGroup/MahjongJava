package tool;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Integer(牌の並び) -> Byte(フラグ)を表す写像を生成する．
 * 牌の並びは連続している牌の並びを個数で表現したもので、例えば「1,1,1,2,2,3,3,4,4,4,4」のようなときは
 * 3224という整数値となる．
 * フラグは8bitのフラグで1bit目から「刻子からとれるか」、「順子からとれるか」、
 * 「二盃口か」、「一盃口か」、「一気通貫か」を表す．残り3bitは未定である．
 * 
 * ある牌の並びの一盃口フラグ(あるいは二盃口、一気通貫フラグ)がたっているとき、三暗刻や対々和になることは決してない(恐らく)．
 * よって、「刻子からとれる」フラグ、「順子からとれる」フラグの立っているほうを選択すればよい.
 * 両方立っている場合は順子からを選択しなければならない．
 * 
 * また、牌の並び値は、雀頭を含める場合と含めない場合の２つものが考えられる．
 * 
 * @author kohei
 *
 */
public class PatternMethodKeyValueCreator {
	public static final String MAP1_NAME = "map1";
	public static final String MAP2_NAME = "map2";

	/**
	 * 0 -> 0, 1 -> 1, 2 -> 2, 3 -> 3, 4 -> 4, 5 -> 10, 6 -> 11, ...
	 * @return
	 */
	public static int getNum(int num, int m) {
		return (num % (int) Math.pow(5, m + 1)) / (int) Math.pow(5, m);
	}

	public static int getNum10rad(int num, int m) {
		return (num % (int) Math.pow(10, m + 1)) / (int) Math.pow(10, m);
	}

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

	public static byte removeSyuntsu(int num[]) {
		boolean peko = false;
		byte ret = 0;
		int ittsu = 0;
		for (int i = 0; i < num.length - 2; i++) {
			if (num[i] >= 1 && num[i + 1] >= 1 && num[i + 2] >= 1) {
				if (peko) {
					if ((ret & 0x8) != 0) {
						ret |= 0x4;
					} else {
						ret |= 0x8;
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
			ret |= 0x10;
		}
		return ret;
	}

	public static byte isNMentsu(int num[]) {
		byte ret = 0;
		byte flag = 0;

		int copy[] = num.clone();
		removeKotsu(copy);
		flag |= removeSyuntsu(copy);
		if (equalsZero(copy)) {
			ret |= 0x1;
			ret |= flag;
		}

		flag = 0;
		copy = num.clone();
		flag |= removeSyuntsu(copy);
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
				flag |= removeSyuntsu(copy);
				if (equalsZero(copy)) {
					ret |= 0x1;
					ret |= flag;
				}

				copy = num.clone();
				flag = 0;
				copy[j] -= 2;
				flag |= removeSyuntsu(copy);
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
					byte value = 0;
					if (((value = isNMentsu(arr)) & 0x3) != 0) {
						map1.put(num, value);
					}
				} else if (sum % 3 == 2) {
					byte value = 0;
					if (((value = isNMentsu1Janto(arr)) & 0x3) != 0) {
						map2.put(num, value);
					}
				}
			}
		}
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
