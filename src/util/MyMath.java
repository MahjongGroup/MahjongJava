package util;

/**
 * @author kohei
 *
 */
public class MyMath {
	/**
	 * 指定された整数値を指定された桁数で切り上げた整数値を返す．
	 * 例えばceil(1280, 2)は1300を返す．
	 * @param num 整数．
	 * @param keta 切り上げする桁数．
	 * @return 切り上げされた整数値．
	 */
	public static int ceil(int num, int keta) {
		int scale = (int) Math.pow(10, keta);
		if(num % scale == 0) {
			return num;
		}
		return (num / scale) * scale + scale;
	}
	
	/**
	 * 指定された整数の中で最大の値を返す.
	 * 引数の整数は少なくともひとつは入れなければならない.
	 * 
	 * @param a 第1の整数.
	 * @param array	 2番目からの整数.
	 * @return 最大の整数.
	 */
	public static int max(int a, int ...array) {
		int max = a;
		
		for (int i : array) {
			if(i > max)
				max = i;
		}
		return max;
	}

	/**
	 * 指定された整数の中で最小の値を返す.
	 * 引数の整数は少なくともひとつは入れなければならない.
	 * 
	 * @param a 第1の整数.
	 * @param array	 2番目からの整数.
	 * @return 最小の整数.
	 */
	public static int min(int a, int ...array) {
		int min = a;
		
		for (int i : array) {
			if(i < min)
				min = i;
		}
		return min;
	}
	
}
