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
	
	
}
