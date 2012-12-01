package system;

/**
 * 局の結果を表すインターフェース.
 */
public interface KyokuResult {

	/**
	 * 途中流局の場合trueを返す.
	 * 
	 * @return 途中流局の場合true.
	 */
	public abstract boolean isTotyuRyukyoku();

	/**
	 * 途中流局の種類を返す．
	 * @return 途中流局の種類．
	 * @throws UnsupportedOperationException 途中流局でないオブジェクトの場合．
	 */
	public abstract TotyuRyukyokuType getTotyuryukyokuType();

	/**
	 * 流局の場合trueを返す.
	 * @return 流局の場合true.
	 */
	public abstract boolean isRyukyoku();

	/**
	 * ツモ上がりの場合はtrueを返す.
	 * @return ツモ上がりの場合true.
	 */
	public abstract boolean isTsumoAgari();

	/**
	 * ロン上がりの場合はtrueを返す.
	 * @return ロン上がりの場合true.
	 */
	public abstract boolean isRonAgari();

	/**
	 * この局の親を返す．
	 * @return この局の親．
	 */
	public abstract Player getOya();

	/**
	 * テンパイしている人の人数を返す．
	 * @return テンパイしている人の人数.
	 */
	public abstract int getTenpaiSize();

	/**
	 * 指定されたプレイヤーがテンパイしている場合trueを返す．
	 * @param p テンパイしているかどうか確かめるプレイヤー．
	 * @return テンパイしている場合true．
	 */
	public abstract boolean isTenpai(Player p);

	/**
	 * あがっている人の人数を返す．
	 * @return あがっている人の人数．
	 */
	public abstract int getAgariSize();

	/**
	 * 指定されたプレイヤーがあがっている場合trueを返す．
	 * @param p あがっているかどうか確かめるプレイヤー．
	 * @return あがっている場合はtrue．
	 */
	public abstract boolean isAgari(Player p);

	/**
	 * 放銃したプレイヤーを返す.
	 * @return 放銃したプレイヤー.
	 */
	public abstract Player getHojuPlayer();

	/**
	 * リーチしているプレイヤーのサイズを返す.
	 * @return
	 */
	public abstract int getReachPlayerSize();

	/**
	 * 指定されたプレイヤーの上がり結果を返す.
	 * @param p 上がったプレイヤー.
	 * @return 上がり結果(基本点、役など).
	 * @throws UnsupportedOperationException ツモあがりでない局Resultがこのメソッドを呼び出した場合．
	 */
	public abstract AgariResult getAgariResult(Player p);

	/**
	 * 上がり牌を返す.流局の場合はnullを返す.
	 * @return 上がり牌.流局の場合はnull.
	 */
	public abstract Hai getAgariHai();

	/**
	 * 指定されたプレイヤーの局情報(手牌、副露牌、捨て牌など)を返す.指定されたプレイヤーが存在しない
	 * 場合はnullを返す.
	 * @param p プレイヤー.
	 * @return 局情報(手牌、副露牌、捨て牌など).
	 */
	public abstract KyokuPlayer getKyokuPlayer(Player p);

	/**
	 * ツモあがったプレイヤーを返す．
	 * @return ツモあがったプレイヤー．
	 * @throws UnsupportedOperationException ツモあがりでない局Resultがこのメソッドを呼び出した場合．
	 */
	public abstract Player getTsumoAgariPlayer();
	
	public boolean isReach(Player p);
}