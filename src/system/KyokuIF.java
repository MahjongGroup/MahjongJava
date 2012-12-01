package system;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 局インターフェース.局は1回の局に関するすべての情報を持つ.
 * 
 * @author kohei
 * 
 */
public interface KyokuIF {

	/**
	 * 局を初期化する
	 */
	public abstract void init();

	/**
	 * ランダムな牌を山からツモる。
	 * 
	 * @throws IllegalStateException 連続でこのメソッドを呼び出した場合
	 */
	public abstract void doTsumo();

	/**
	 * 九種九牌ならtrueを返す。
	 * 
	 * @return 九種九牌ならtrue
	 */
	public abstract boolean isKyusyukyuhai();

	/**
	 * 現在のツモ牌でツモ上がっているならtrueを返す。
	 * 
	 * @return 現在のツモ牌でツモ上がっているならtrue
	 */
	public abstract boolean isTsumoAgari();

	/**
	 * 現在ターンの人がツモ上がりする。
	 */
	public abstract void doTsumoAgari();

	/**
	 * 指定された風のプレイヤーを返す.
	 * 
	 * @param kaze プレイヤーの風.
	 * @return 指定された風のプレイヤー.
	 */
	public abstract Player getPlayer(Kaze kaze);

	/**
	 * 現在ターンの人が加槓できるならtrueを返す。
	 * 
	 * @return 加槓できるならtrue
	 */
	public abstract boolean isKakanable();

	/**
	 * 現在ターンの人が加槓する。
	 * 
	 * @param index 牌のインデックス。ツモ牌を指定する場合は13を渡す。
	 * @return 加槓して出来た面子。
	 */
	public abstract Mentu doKakan(int index);

	/**
	 * 現在ターンの人が嶺上牌をツモる。
	 */
	public abstract void doRinsyanTsumo();

	/**
	 * 現在ターンの人が加槓できる手牌リストの位置のリストを返す。
	 * 
	 * @return ないならnullを返す。
	 */
	public abstract List<Integer> getKakanableHaiList();

	/**
	 * 現在ターンの人が暗槓できるならtrueを返す。
	 * 
	 * @return　暗槓できるならtrue
	 */
	public abstract boolean isAnkanable();

	/**
	 * 現在暗槓できる手牌リストの位置のリストのリストを返す。
	 * 
	 * @return　ない場合はnullを返す。
	 */
	public abstract List<List<Integer>> getAnkanableHaiList();

	/**
	 * 指定されたインデックスで暗槓する。indexが13のときはツモ牌
	 * 
	 * @param type 暗槓する牌のインデックス
	 * @return 暗槓して出来た面子。
	 */
	public abstract Mentu doAnkan(int index);

	/**
	 * 指定された風の人がロン出来るならtrueを返す。
	 * 
	 * @param kaze プレイヤーの風
	 * @return　指定された風の人がロン出来るならtrue
	 */
	public abstract boolean isRonable(Kaze kaze);

	/**
	 * 指定された風の人が指定された牌がフリテンとなるならtrueを返す。
	 * 
	 * @param kaze フリテンかどうか調べるプレイヤーの風
	 * @param agariHai フリテンかどうか調べる牌
	 * @return フリテンとなるならtrue
	 */
	public abstract boolean isFuriten(Kaze kaze, HaiType type);

	/**
	 * ロンする。
	 * 
	 * @param kaze
	 */
	public abstract void doRon(Kaze kaze);

	/**
	 * 指定された風の人が立直しているならtrueを返す。
	 * 
	 * @param kaze 　立直しているか調べる人の風
	 * @return　立直しているならtrue
	 */
	public abstract boolean isReach(Kaze kaze);

	/**
	 * 立直時、不要牌の位置リストを返す。
	 * 
	 * @return 不要牌の位置リスト
	 */
	public abstract List<Integer> getReachableHaiList();

	/**
	 * 指定された風のプレイヤーがテンパイしているならtrueを返す。
	 * 
	 * @param kaze テンパイしているか確かめるプレイヤーの風.
	 * @return テンパイしている場合true.
	 */
	public abstract boolean isTenpai(Kaze kaze);

	/**
	 * 立直できるならtrueを返す.
	 * 
	 * @return 立直できるならtrue.
	 */
	public abstract boolean isReachable();

	/**
	 * 立直する。初順であった場合はダブル立直する。
	 */
	public abstract void doReach();

	/**
	 * 手牌から指定されたインデックスの牌を切って、捨牌リストに加える。
	 * 
	 * @param index 手牌リストの切りたい牌のインデックス
	 * @throws ArrayIndexOutOfBoundsException 指定されたインデックスが手牌リストの範囲を越えていた場合
	 */
	public abstract void discard(int index);

	/**
	 * ツモ切りする。
	 */
	public abstract void discardTsumoHai();

	/**
	 * ドラをめくれる場合めくる。
	 * 
	 * @return ドラをめくった枚数。
	 */
	public abstract int openDora();

	/**
	 * 指定された風のプレイヤーが明槓できるならtrueを返す。
	 * 
	 * @param kaze 明槓できるか調べるプレイヤーの風
	 * @return　明槓できるならtrue
	 */
	public abstract boolean isMinkanable(Kaze kaze);

	public abstract List<Integer> getMinkanableList();

	/**
	 * 指定された風の人が明槓をする。
	 * 
	 * @param kaze 明槓をするプレイヤーの風
	 * @return 明槓して出来た面子。
	 */
	public abstract Mentu doMinkan(Kaze kaze);

	/**
	 * 指定された風のプレイヤーがポンできるならtrueを返す。
	 * 
	 * @param kaze 　ポンできるか調べるプレイヤーの風
	 * @return　ポンできるならtrue
	 */
	public abstract boolean isPonable(Kaze kaze);

	/**
	 * ポンする。
	 * 
	 * @param kaze
	 * @param ponList
	 * @return ポンして出来た面子。
	 */
	public abstract Mentu doPon(Kaze kaze, List<Integer> ponList);

	/**
	 * 指定された風のプレイヤーがポンできる牌インデックスのリストを返す。
	 * 
	 * @param kaze ポンするプレイヤーの風
	 * @return 牌インデックスのリスト
	 */
	public abstract List<List<Integer>> getPonableHaiList(Kaze kaze);

	/**
	 * 下家がチーできるならtrueを返す。
	 * 
	 * @return　下家がチーできるならtrue
	 */
	public abstract boolean isChiable();

	/**
	 * チーできる牌リストを返す。
	 * 
	 * @return　チーできる牌リスト
	 */
	public abstract List<List<Integer>> getChiableHaiList();

	/**
	 * チーする。
	 * 
	 * @param tiList チーする牌インデックスのリスト
	 * @return チーして出来た面子。
	 */
	public abstract Mentu doChi(List<Integer> tiList);

	public abstract boolean isSanchaho();

	/**
	 * 四風連打の場合trueを返す。
	 * 
	 * @return 四風連打の場合true
	 */
	public abstract boolean isSufontsuRenta();

	/**
	 * 四開槓の場合trueを返す。しかし、1人が4回槓をしている場合にはfalseを返す。
	 * 
	 * @return　四開槓の場合true
	 */
	public abstract boolean isSukaikan();

	/**
	 * 四家立直の場合trueを返す。
	 * 
	 * @return 四家立直の場合true。
	 */
	public abstract boolean isSuchaReach();

	public abstract boolean isRyukyoku();

	public abstract void doRyukyoku();

	public abstract boolean isRenchan();

	public abstract boolean isTotyuRyukyoku();

	public abstract void doTotyuRyukyoku();

	public abstract boolean isSyukyoku();

	public abstract void doSyukyoku();

	public abstract Rule getRule();

	public abstract Map<Kaze, Player> getPlayerMap();

	public abstract Kaze getBakaze();

	public abstract KyokuResult createKyokuResult();

	/**
	 * 現在、表になっているドラのリストを返す。
	 * 
	 * @return 現在、表になっているドラのリスト
	 */
	public abstract List<Hai> getDoraList();

	/**
	 * 裏ドラのリストを返す。
	 * 
	 * @return 裏ドラのリスト。
	 */
	public abstract List<Hai> getUradoraList();

	public abstract List<Mentu> getFuroHaiList(Kaze kaze);

	public abstract Map<Player, List<Yaku>> getYakuMap();

	public abstract List<Player> getTenpaiPlayerList();

	public abstract void sortTehaiList();

	public abstract void sortTehaiList(Kaze kaze);

	/**
	 * 現在のターンを終了して、次のプレイヤーのターンにする。
	 * 
	 */
	public abstract void nextTurn();

	public abstract Kaze getCurrentTurn();

	public abstract List<Hai> getTehaiList(Kaze kaze);

	/**
	 * 現在の捨て牌マップを返す。捨て牌マップとは 風->その風の人の捨て牌リスト を表すマップである。
	 * 
	 * @return 現在の捨て牌マップ
	 */
	public abstract Map<Kaze, SutehaiList> getSutehaiMap();

	/**
	 * 現在の副露牌マップを返す。副露牌マップとは 風->その風の人の副露牌リスト を表すマップである。
	 * 
	 * @return 現在の副露牌マップ
	 */
	public abstract Map<Kaze, HurohaiList> getHurohaiMap();

	public abstract Kaze getKazeOf(Player p);

	public abstract void disp();

	/**
	 * 現在のツモ牌を返す。
	 * 
	 * @return 現在のツモ牌
	 */
	public abstract Hai getCurrentTsumoHai();

	/**
	 * 現在の捨て牌を返す。
	 * 
	 * @return 今捨てられた捨て牌
	 */
	public abstract Hai getCurrentSutehai();

	/**
	 * 現在の槓の回数を返す。
	 * 
	 * @return　槓の回数
	 */
	public abstract int getKanSize();

	/**
	 * 指定された引数からチェッカーパラム(ツモorロン、あがり牌、ルール、鳴き、自風、場風、フラグ役セット)を生成する.
	 * 
	 * @param tumo ツモの場合true
	 * @param agariHai 上がり牌
	 * @param kaze 自分の風
	 * @return 生成したチェッカーパラム
	 */
	public abstract Param newCheckerParam(boolean tumo, Hai agariHai, Kaze kaze);

	/**
	 * フラグによりチェックする役セットを返す。
	 * 
	 * @param kaze 風
	 * @param tumo ツモの場合true
	 * @return フラグによりチェックする役セット
	 */
	public abstract Set<Yaku> getYakuSetByFlag(Kaze kaze, boolean tumo);

}