package tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SuhaiAlgo {
	public static void main(String[] args) {
		Map<Integer, Byte> map = new TreeMap<Integer, Byte>();

		for (int i0 = 0; i0 < 5; i0++) {
			for (int i1 = 0; i1 < 5; i1++) {
				for (int i2 = 0; i2 < 5; i2++) {
					for (int i3 = 0; i3 < 5; i3++) {
						for (int i4 = 0; i4 < 5; i4++) {
							for (int i5 = 0; i5 < 5; i5++) {
								for (int i6 = 0; i6 < 5; i6++) {
									for (int i7 = 0; i7 < 5; i7++) {
										for (int i8 = 0; i8 < 5; i8++) {
											if ((i0 + i1 + i2 + i3 + i4 + i5 + i6 + i7 + i8) <= 14) {
												int pos[] = { i0, i1, i2, i3, i4, i5, i6, i7, i8 };
												map.put(getInt(pos), getMax(pos).getByte());
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		//		System.out.println(map);
		System.out.println(map.size());

		int arr[] = { 1, 1, 2, 3, 4, 5, 5, 5, 7, 7, 8, 9, 9, 9 };
		System.out.println(arr.length);
		System.out.println(Arrays.toString(getPos(arr)));
		int pos[] = getPos(arr);

		long time = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			map.get(getInt(pos));
		}
		System.out.println(System.currentTimeMillis() - time);

		byte b = map.get(getInt(pos));
		Data data = getDataFromByte(b);
		System.out.println("mentu : " + data.mentu);
		System.out.println("tatu : " + data.tatu);
		System.out.println("janto : " + data.janto);

		time = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			getMax(pos);
		}
		System.out.println(System.currentTimeMillis() - time);
	}
	
	static int getInt(int arr[]) {
		int ret = 0;
		for (int i = 0; i < 9; i++) {
			ret += (int) (Math.pow(10, 8 - i)) * arr[i];
		}
		return ret;
	}

	public static Data getDataFromByte(byte b) {
		Data data = new Data();
		data.mentu = (b >> 4);
		data.tatu = b - ((b >> 2) << 2);
		data.janto = b - ((b >> 1) << 1);
		return data;
	}

	static class Data {
		public int mentu;
		public int tatu;
		public int janto;

		public byte getByte() {
			return (byte) ((mentu << 4) + (tatu << 1) + janto);
		}

		public int cal() {
			if (janto == 1) {
				if (mentu + tatu > 4) {
					return mentu * 2 + 2 + 1;
				}
				return mentu * 2 + tatu + 1;
			} else {
				return mentu * 2 + tatu;
			}
		}
	}

	public static int[] getPos(int[] hai) {
		int ret[] = new int[9];
		for (int i : hai) {
			ret[i - 1]++;
		}
		return ret;
	}

	public static void remKotu(int temp[], Data data) {
		for (int i = 0; i < 9; i++) {
			if (temp[i] >= 3) {
				temp[i] -= 3;
				data.mentu += 1;
			}
		}
	}

	public static void remSyuntu(int temp[], Data data) {
		for (int i = 0; i < 7; i++) {
			if (temp[i] >= 1 && temp[i + 1] >= 1 && temp[i + 2] >= 1) {
				temp[i] -= 1;
				temp[i + 1] -= 1;
				temp[i + 2] -= 1;
				data.mentu += 1;
				i--;
			}
		}
	}

	public static void remTatu1(int temp[], Data data) {
		for (int i = 0; i < 7; i++) {
			if (temp[i] >= 1) {
				if (temp[i + 1] >= 1) {
					temp[i] -= 1;
					temp[i + 1] -= 1;
					data.tatu += 1;
					i--;
				} else if (temp[i + 2] >= 1) {
					temp[i] -= 1;
					temp[i + 2] -= 1;
					data.tatu += 1;
					i--;
				}
			}
		}
	}

	public static void remTatu2(int temp[], Data data) {
		for (int i = 0; i < 9; i++) {
			if (temp[i] >= 2) {
				temp[i] -= 2;
				data.tatu += 1;
			}
		}
	}

	public static Data getMax(int pos[]) {
		List<Data> list = new ArrayList<Data>();

		for (int i = 0; i < 9; i++) {
			Data data1 = new Data();
			Data data2 = new Data();
			
			if (pos[i] >= 2) {
				int temp[] = pos.clone();
				temp[i] -= 2;
				data1.janto = 1;

				int temp2[] = temp.clone();

				remKotu(temp, data1);
				remSyuntu(temp, data1);
				remTatu1(temp, data1);
				remTatu2(temp, data1);

				list.add(data1);
				
				remSyuntu(temp2, data2);
				remKotu(temp2, data2);
				remTatu1(temp2, data2);
				remTatu2(temp2, data2);

				list.add(data2);
			}
		}

		Data ret = null;
		if (list.size() == 0) {
			ret = new Data();
			return ret;
		}

		int max = 0;
		for (Data data : list) {
			if (data.cal() > max) {
				max = data.cal();
				ret = data;
			}
		}
		return ret;
	}

}
