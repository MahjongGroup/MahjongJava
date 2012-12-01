package system;

public class ScoreCalculator {
	/**
	 * 指定されたスコアをscale倍したものの2桁目の値を切り上げたスコア.
	 * @param score 基本点.
	 * @param scale 基本点を何倍するか.
	 * @return 計算されたスコア.
	 */
	public static int getCeilScore(int score, int scale) {
		return (int) (Math.ceil(score * scale / 100.0) * 100.0); 
	}
}
