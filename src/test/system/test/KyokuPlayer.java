package test.system.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import system.agari.AgariMethods;
import system.hai.Hai;
import system.hai.Hai.HaiComparator;
import system.hai.HaiType;
import system.hai.HurohaiList;
import system.hai.Kaze;
import system.hai.Mentsu;
import system.hai.Sutehai;
import system.hai.SutehaiList;
import system.hai.TehaiList;

/**
 * ある一つの局の中のプレイヤーを表す.手牌,捨牌,副露牌などを持つ.
 */
public class KyokuPlayer {
	private final TehaiList tehaiList;
	private final HurohaiList hurohaiList;
	private final SutehaiList sutehaiList;

	private boolean reachFlag;
	private boolean dreachFlag;
	private boolean nakiFlag;
	private boolean furitenFlag;
	private boolean tatyaFuritenFlag;

	private int kanSize;

	// キャッシュ変数
	// テンパイ情報などを記憶しておく.checkフラグがtrueの場合はキャッシュされている
	// 値を参照する.
	private boolean cachedTenpaiCheckFlag;
	private boolean cachedTenpaiFlag;

	/**
	 * コンストラクタ.
	 */
	public KyokuPlayer() {
		this.tehaiList = new TehaiList();
		this.hurohaiList = new HurohaiList();
		this.sutehaiList = new SutehaiList();
		this.init();
	}

	/**
	 * コピーコンストラクタ.
	 */
	public KyokuPlayer(KyokuPlayer kp) {
		this.tehaiList = kp.getTehaiList();
		this.hurohaiList = kp.getHurohaiList();
		this.sutehaiList = kp.getSutehaiList();
		this.dreachFlag = kp.isDoubleReach();
		this.reachFlag = kp.isReach();
		this.kanSize = kp.getKanSize();
		this.nakiFlag = kp.isNaki();
		this.furitenFlag = kp.isFuriten();
	}

	/**
	 * このオブジェクトを初期化する.
	 */
	public void init() {
		this.tehaiList.clear();
		this.hurohaiList.clear();
		this.sutehaiList.clear();

		this.reachFlag = false;
		this.nakiFlag = false;
		this.dreachFlag = false;
		this.furitenFlag = false;

		this.cachedTenpaiCheckFlag = false;

		this.kanSize = 0;
	}

	/**
	 * 指定された牌を手牌リストに加える.
	 * 
	 * @return addに成功した場合trueを返す.
	 */
	public boolean distribute(Hai hai) {
		onStateChanged();
		return this.tehaiList.add(hai);
	}

	/**
	 * このプレイヤーの手牌が指定された牌(ツモ牌)を含めて九種九牌となっている場合, trueを返す.
	 * 
	 * @return 九種九牌の場合true.
	 */
	public boolean isKyusyukyuhai(Hai tsumohai) {
		Set<HaiType> set = tehaiList.toHaiTypeSet();
		set.add(tsumohai.type());

		int size = 0;
		for (HaiType type : set) {
			if (type.isYaotyuhai()) {
				size++;
			}
			if (size >= 9) {
				return true;
			}
		}
		return false;
	}

	/**
	 * このプレイヤーが加槓可能の場合trueを返す.
	 * 
	 * @param ツモ牌．ない場合はnull．
	 * @return このプレイヤーが加槓可能の場合true.
	 */
	public boolean isKakanable(Hai tsumo) {
		if (this.kanSize >= 4) {
			return false;
		}
		for (Hai hai : tehaiList) {
			if (hurohaiList.isKakan(hai.type())) {
				return true;
			}
		}

		if (tsumo != null && hurohaiList.isKakan(tsumo.type())) {
			return true;
		}
		return false;
	}

	/**
	 * 加槓可能な手牌のインデックスリストを返す.
	 * 
	 * @param ツモ牌．
	 * @return 加槓可能な手牌のインデックスリスト.加槓できる牌がない場合は空のリストを返す.
	 */
	public List<Integer> getKakanableHaiList(Hai tsumo) {
		List<Integer> result = new ArrayList<Integer>();

		for (int i = 0; i < tehaiList.size(); i++) {
			Hai hai = tehaiList.get(i);
			if (hurohaiList.isKakan(hai.type())) {
				result.add(i);
			}
		}
		if (hurohaiList.isKakan(tsumo.type())) {
			result.add(13);
		}
		return result;
	}

	/**
	 * 指定された牌(ツモ牌)で加槓する.
	 * 
	 * @param hai 加槓する牌.
	 * @return 加槓して出来た面子.
	 * @throws IllegalArgumentException
	 *             指定された牌で加槓出来ない場合.
	 */
	public Mentsu doKakan(Hai hai) {
		if (!hurohaiList.isKakan(hai.type()))
			throw new IllegalArgumentException("指定された牌で加槓出来ません : " + hai);
		onStateChanged();
		Mentsu m = hurohaiList.doKakan(hai);
		return m;
	}

	/**
	 * 指定された手牌のインデックスで加槓する.
	 * 
	 * @param index 手牌インデックス.
	 * @param tsumohai 現在のツモ牌.
	 * @return 加槓して出来た面子.
	 * @throws IllegalArgumentException
	 *             指定された牌で加槓出来ない場合.
	 */
	public Mentsu doKakan(int index, Hai tsumohai) {
		Hai hai = tehaiList.get(index);
		if (!hurohaiList.isKakan(hai.type()))
			throw new IllegalArgumentException("指定された牌で加槓出来ません : " + hai);
		onStateChanged();
		Mentsu m = hurohaiList.doKakan(hai);
		tehaiList.remove(index);
		tehaiList.add(tsumohai);
		return m;
	}

	/**
	 * このプレイヤーが暗槓可能な場合はtrueを返す.
	 * 
	 * @param tsumohai ツモ牌.
	 * @return このプレイヤーが暗槓可能な場合はtrue.
	 */
	public boolean isAnkanable(Hai tsumohai) {
		if (this.kanSize == 4) {
			return false;
		}
		// おくりカン判定
		if (reachFlag) {
			List<List<Integer>> indexes = tehaiList.getAnkanableIndexList(tsumohai);
			for (List<Integer> list : indexes) {
				if (list.contains(13)) {
					TehaiList tempList = new TehaiList(tehaiList);
					for (Integer i : list) {
						tempList.remove(i);
					}
					if (!AgariMethods.isTenpai(tempList, nakiFlag))
						return false;
					List<Hai> machi = AgariMethods.getMachiHaiList(tempList, nakiFlag);
					if (machi.equals(this.getMachihaiList())) {
						return true;
					}
					return false;
				}
			}
			return false;
		}
		return tehaiList.isAnkanable(tsumohai);
	}

	/**
	 * 暗槓できる牌のインデックスのリストのリストを返す.
	 * 
	 * @param tsumohai ツモ牌.
	 * @return 暗槓できる牌のインデックスのリストのリスト.
	 */
	public List<List<Integer>> getAnkanableHaiList(Hai tsumohai) {
		return tehaiList.getAnkanableIndexList(tsumohai);
	}

	/**
	 * 指定されたインデックス(13はツモ牌)のリストの牌で暗槓する.
	 * 
	 * @param tsumohai ツモ牌.
	 * @param index 暗槓する手牌のインデックス(13はツモ牌)のリスト.
	 * @return 暗槓して出来た暗槓の面子.
	 * @throws IllegalArgumentException
	 *             指定された牌で暗槓できない場合.
	 */
	public Mentsu doAnkan(Hai tsumohai, List<Integer> list) {
		onStateChanged();

		int index = -1;
		if ((index = list.get(0)) == 13)
			index = list.get(1);

		HaiType type = tehaiList.get(index).type();

		if (!tehaiList.isAnkanable(tsumohai, type)) {
			throw new IllegalArgumentException("指定された牌では暗槓できません : " + type);
		}

		Hai haiArray[] = new Hai[4];
		int i = 0;
		for (Iterator<Hai> itr = tehaiList.iterator(); itr.hasNext();) {
			Hai hai = itr.next();
			if (hai.type() == type) {
				itr.remove();
				haiArray[i++] = hai;
			}
		}
		// 暗槓インデックスにツモ牌が含まれている場合
		if (type == tsumohai.type()) {
			haiArray[i] = tsumohai;
		}
		// 暗槓インデックスにツモ牌が含まれていない場合
		else {
			tehaiList.add(tsumohai);
		}

		Mentsu m = new Mentsu(haiArray);
		hurohaiList.add(m);
		kanSize++;

		return m;
	}

	/**
	 * リーチしている場合trueを返す.ダブルリーチしていても,このメソッドはfalseを返すことに注意しなければならない.
	 * 
	 * @return　リーチしている場合true.
	 */
	public boolean isReach() {
		return reachFlag;
	}

	/**
	 * ダブルリーチしている場合trueを返す.
	 * 
	 * @return ダブルリーチしている場合true.
	 */
	public boolean isDoubleReach() {
		return dreachFlag;
	}

	/**
	 * リーチフラグをtrueにする。指定されたフラグがtrueの場合,ダブルリーチする.
	 * 
	 * @param db trueの場合,ダブルリーチする.
	 * @throws IllegalStateException 既にリーチまたはダブルリーチしている場合.
	 */
	public void doReach(boolean db) {
		if (reachFlag || dreachFlag)
			throw new IllegalStateException("既にリーチしている");
		if (db) {
			dreachFlag = true;
		} else {
			reachFlag = true;
		}
	}

	/**
	 * このプレイヤーの手牌の指定されたインデックス(13はツモ牌)の牌を削除する.
	 * 
	 * @param tsumohai ツモ牌.
	 * @param index 削除する手牌の牌のインデックス(13はツモ牌).
	 * @return 削除された牌.
	 * @throws IndexOutOfBoundsException
	 *             指定されたインデックスが範囲外の場合.
	 */
	public Hai discard(int index, Hai tsumohai) {
		Hai ret = null;
		if (index == 13) {
			ret = tsumohai;
		} else {
			onStateChanged();
			ret = tehaiList.remove(index);
			if (tsumohai != null) {
				tehaiList.add(tsumohai);
			}
		}
		return ret;
	}

	/**
	 * 指定された牌で明槓を出来る場合trueを返す.
	 * 
	 * @param hai 明槓できるか確かめる牌タイプ.
	 * @return 明槓を出来る場合true.
	 */
	public boolean isMinkanable(HaiType type) {
		if (reachFlag || dreachFlag)
			return false;
		return tehaiList.isMinkanable(type);
	}

	/**
	 * このプレイヤーの明槓できる手牌のインデックスのリストを返す.
	 * 
	 * @param type 牌タイプ.
	 * @return 明槓できる手牌のインデックスのリスト.
	 */
	public List<Integer> getMinkanableList(HaiType type) {
		return tehaiList.getMinkanableIndexList(type);
	}

	/**
	 * 指定された牌で明槓をする.
	 * 
	 * @param minkanhai 明槓する牌.
	 * @param kaze 鳴かれたプレイヤーの風.
	 * @return 明槓して出来た面子.
	 */
	public Mentsu doMinkan(Hai minkanhai, Kaze kaze) {
		onStateChanged();
		HaiType type = minkanhai.type();

		Mentsu m = new Mentsu(minkanhai, kaze, tehaiList.remove(type), tehaiList.remove(type), tehaiList.remove(type));
		hurohaiList.add(m);
		nakiFlag = true;
		return m;
	}

	/**
	 * 指定された牌でポンできる場合trueを返す.
	 * 
	 * @param hai 牌タイプ.
	 * @return ポンできる場合true.
	 */
	public boolean isPonable(HaiType type) {
		if (reachFlag || dreachFlag)
			return false;

		return tehaiList.isPonable(type);
	}

	/**
	 * このプレイヤーのポン出来る牌インデックスのリストのリストを返す.
	 * 
	 * @param type 牌タイプ.
	 * @return 牌インデックスのリストのリスト.
	 */
	public List<List<Integer>> getPonableHaiList(HaiType type) {
		return tehaiList.getPonableIndexList(type);
	}

	/**
	 * ポンする.
	 * 
	 * @param ponList ポンするインデックスリスト.
	 * @param sutehai 捨て牌
	 * @param kaze 鳴かれた人の風.
	 * @return ポンして出来た面子.
	 */
	public Mentsu doPon(List<Integer> ponList, Hai sutehai, Kaze kaze) {
		onStateChanged();
		int index0 = ponList.get(0);
		int index1 = ponList.get(1);

		Hai hai0 = tehaiList.get(index0);
		Hai hai1 = tehaiList.get(index1);

		Mentsu m = new Mentsu(sutehai, kaze, hai0, hai1);
		hurohaiList.add(m);

		if (index0 < index1) {
			tehaiList.remove(index1);
			tehaiList.remove(index0);
		} else {
			tehaiList.remove(index0);
			tehaiList.remove(index1);
		}
		nakiFlag = true;

		return m;
	}

	/**
	 * チーできる場合trueを返す.
	 * 
	 * @param type 牌タイプ.
	 * @return チーできる場合true.
	 */
	public boolean isChiable(HaiType type) {
		if (reachFlag || dreachFlag)
			return false;

		return tehaiList.isChiable(type);
	}

	/**
	 * チー出来る牌インデックスのリストのリストを返す.
	 * 
	 * @param type 牌タイプ.
	 * @return チー出来る牌インデックスのリストのリスト.
	 */
	public List<List<Integer>> getChiableHaiList(HaiType type) {
		return tehaiList.getChiableHaiList(type);
	}

	/**
	 * チーする.
	 * 
	 * @param tiList チーする牌インデックスのリスト.
	 * @param chihai チーする牌.
	 * @param kaze 鳴かれた人の風.
	 * @return チーして出来た面子.
	 */
	public Mentsu doChi(List<Integer> tiList, Hai chihai, Kaze kaze) {
		onStateChanged();
		int index0 = tiList.get(0);
		int index1 = tiList.get(1);

		Mentsu m = new Mentsu(chihai, kaze, tehaiList.get(index0), tehaiList.get(index1));
		if (index0 < index1) {
			tehaiList.remove(index1);
			tehaiList.remove(index0);
		} else {
			tehaiList.remove(index0);
			tehaiList.remove(index1);
		}
		hurohaiList.add(m);
		nakiFlag = true;
		return m;
	}

	/**
	 * フリテンの場合trueを返す.
	 * 
	 * @return フリテンの場合true.
	 */
	public boolean isFuriten() {
		furitenFlag = AgariMethods.isFuriten(getMachihaiList(), sutehaiList);
		return (furitenFlag || tatyaFuritenFlag);
	}

	/**
	 *　他家振聴フラグをセットする。
	 * @param tf
	 */
	public void setTatyaFuritenFlag(boolean tf) {
		this.tatyaFuritenFlag = tf;
	}

	/**
	 * この局プレイヤーがテンパイしている場合、その待ち牌リストを返す.
	 * テンパイしていない場合は空のリストを返す.
	 * 
	 * このメソッドから得られる待ち牌を合わせた手牌は役がない場合もある.
	 * 
	 * @return 待ち牌リスト.
	 */
	public List<Hai> getMachihaiList() {
		return AgariMethods.getMachiHaiList(tehaiList, nakiFlag);
	}

	/**
	 * このプレイヤーが鳴いている場合trueを返す.
	 * 
	 * @return このプレイヤーが鳴いている場合true.
	 */
	public boolean isNaki() {
		return nakiFlag;
	}

	/**
	 * テンパイしている場合trueを返す.
	 * 
	 * @return テンパイしている場合true.
	 */
	public boolean isTenpai() {
		if (cachedTenpaiCheckFlag) {
			return cachedTenpaiFlag;
		}
		cachedTenpaiCheckFlag = true;
		return cachedTenpaiFlag = AgariMethods.isTenpai(tehaiList, nakiFlag);
	}

	/**
	 * このプレイヤーの槓サイズを返す.
	 * 
	 * @return このプレイヤーの槓サイズ.
	 */
	public int getKanSize() {
		return this.kanSize;
	}

	/**
	 * 指定された牌コンパレータで手牌をソートする.
	 * 
	 * @param c 牌コンパレータ.
	 */
	public void sortTehai(HaiComparator c) {
		Collections.sort(tehaiList, c);
	}

	/**
	 * 指定された牌を手牌に追加する.
	 * 
	 * @param hai 手牌に追加する牌.
	 * @return addに成功した場合trueを返す.
	 */
	public boolean addTehai(Hai hai) {
		onStateChanged();
		return tehaiList.add(hai);
	}

	/**
	 * 指定された手牌のインデックスの牌を返す.
	 * 
	 * @param index
	 *            手牌のインデックス.
	 * @return 指定されたインデックスの牌.
	 */
	public Hai getTehai(int index) {
		return tehaiList.get(index);
	}

	/**
	 * このプレイヤーの手牌の指定されたインデックスの牌を削除する.
	 * 
	 * @param index 削除する手牌の牌のインデックス.
	 * @return 削除された牌.
	 * @throws IndexOutOfBoundsException 指定されたインデックスが範囲外の場合.
	 */
	public Hai removeTehai(int index) {
		onStateChanged();
		return tehaiList.remove(index);
	}

	public int sizeOfTehai() {
		return tehaiList.size();
	}

	/**
	 * 捨牌リストから指定されたインデックスの捨牌を取得する.
	 * 
	 * @param index インデックス.
	 * @return 捨牌.
	 */
	public Sutehai getSutehai(int index) {
		return sutehaiList.get(index);
	}

	/**
	 * 指定された牌を捨牌に追加する.
	 * 
	 * @param hai 捨てる牌.
	 * @return addに成功した場合trueを返す.
	 */
	public boolean addSutehai(Sutehai hai) {
		return sutehaiList.add(hai);
	}

	public int sizeOfSutehai() {
		return sutehaiList.size();
	}

	/**
	 * このプレイヤーの手牌リストを返す.
	 * 
	 * @return このプレイヤーの手牌リスト.
	 */
	public TehaiList getTehaiList() {
		return new TehaiList(this.tehaiList);
	}

	/**
	 * このプレイヤーの捨牌リストを返す.
	 * 
	 * @return このプレイヤーの捨牌リスト.
	 */
	public SutehaiList getSutehaiList() {
		return new SutehaiList(this.sutehaiList);
	}

	/**
	 * このプレイヤーの副露牌リストを返す.
	 * 
	 * @return このプレイヤーの副露牌リスト.
	 */
	public HurohaiList getHurohaiList() {
		return new HurohaiList(this.hurohaiList);
	}

	/**
	 * 内部状態が変化した場合はすべてのキャッシュフラグをfalseにする．
	 */
	private void onStateChanged() {
		cachedTenpaiCheckFlag = false;
	}

	// DEBUG
	public void disp() {
		if (reachFlag || dreachFlag)
			System.out.println("\tリーチ");
		System.out.print("\t手牌(" + tehaiList.size() + ")：");
		System.out.print("[");
		for (int i = 0; i < tehaiList.size(); i++) {
			System.out.print(tehaiList.get(i) + "(" + i + "),");
		}
		System.out.print("]\n");
		System.out.println("\t副露牌(" + hurohaiList.size() + ")：" + hurohaiList);
		System.out.println("\t捨牌(" + sutehaiList.size() + ")：" + sutehaiList);
	}

	// DEBUG
	public void setTehai(List<Hai> list) {
		onStateChanged();
		this.tehaiList.clear();
		this.tehaiList.addAll(list);
	}

}