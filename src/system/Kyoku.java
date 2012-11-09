package system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import ai.AI;
import ai.AIType01;

/**
 * 1局を表すクラス。
 * 
 * @author kohei
 * 
 */
public class Kyoku {
	// ルール(赤ありなど)
	private final Rule rule;

	// 場風
	private final Kaze bakaze;

	// 各プレイヤーが持つ牌
	private final Map<Kaze, TehaiList> tehaiMap;
	private final Map<Kaze, HurohaiList> hurohaiMap;
	private final Map<Kaze, SutehaiList> sutehaiMap;

	// プレイヤー
	private final Map<Kaze, Player> playerMap;

	// 各プレイヤーが鳴いているかどうか
	private final Map<Kaze, Boolean> nakiMap;

	// 一発判定フラグ
	private final Map<Kaze, Boolean> ippatuMap;

	// 立直フラグ
	private final Map<Kaze, Boolean> reachMap;

	// ダブル立直フラグ
	private final Map<Kaze, Boolean> daburuRitiMap;

	// 山牌を表すリスト
	private final List<Hai> yamahai;

	// 王牌を表すリスト
	private final List<Hai> wanpai;

	// 総ツモ枚数。これが70に達したら局終了
	private int tumoSize;

	// 現在ターン
	private Kaze currentTurn;

	// 現在ツモ牌
	private Hai currentTumohai;

	// 現在捨て牌
	private Hai currentSutehai;

	// 搶槓フラグ
	private boolean tyankanFlag;

	// 嶺上ツモフラグ
	private boolean rinsyanFlag;

	// 天和フラグ
	private boolean tenhoFlag;

	// 地和フラグマップ
	private Map<Kaze, Boolean> tihoMap;

	// 新ドラ枚数
	private int newDoraSize;

	// 槓回数
	private int kanSize;

	//後めくり槓フラグ
	private boolean atomekuriKanFlag;

	// 暗槓フラグ
	private boolean ankanFlag;

	// 初順フラグ 鳴かれてもfalseとなる
	private boolean firstTurn;

	// 槓リスト
	private Map<Kaze, Boolean> kanMap;
	
	private Random rand;

	/**
	 * 局を生成するコンストラクタ
	 * 
	 * @param rule 採用するルール
	 * @param player 風->プレイヤーを表すマップ
	 * @param bakaze 場風
	 */
	public Kyoku(Rule rule, Map<Kaze, Player> player, Kaze bakaze) {
		this.rule = rule;
		this.bakaze = bakaze;

		this.ankanFlag = false;
		this.atomekuriKanFlag = false;

		this.reachMap = new HashMap<Kaze, Boolean>(4);
		this.daburuRitiMap = new HashMap<Kaze, Boolean>(4);
		this.ippatuMap = new HashMap<Kaze, Boolean>(4);
		this.tihoMap = new HashMap<Kaze, Boolean>(4);

		this.kanMap = new HashMap<Kaze, Boolean>();
		
		this.tehaiMap = new HashMap<Kaze, TehaiList>(4);
		for (Kaze kaze : Kaze.values()) {
			this.tehaiMap.put(kaze, new TehaiList(13));
		}
		this.hurohaiMap = new HashMap<Kaze, HurohaiList>(4);
		for (Kaze kaze : Kaze.values()) {
			this.hurohaiMap.put(kaze, new HurohaiList(14));
		}
		this.sutehaiMap = new HashMap<Kaze, SutehaiList>(4);
		for (Kaze kaze : Kaze.values()) {
			this.sutehaiMap.put(kaze, new SutehaiList(14));
		}

		this.playerMap = new HashMap<Kaze, Player>(player);
		this.nakiMap = new HashMap<Kaze, Boolean>();

		this.yamahai = new ArrayList<Hai>(136);
		this.wanpai = new ArrayList<Hai>(14);
		this.rand = new Random();
	}

	/**
	 * 局を初期化する
	 */
	public void init() {
		this.tumoSize = 0;
		this.kanSize = 0;
		this.newDoraSize = 0;
		this.currentTurn = Kaze.TON;
		this.firstTurn = true;

		reachMap.clear();
		for (Kaze kaze : Kaze.values()) {
			reachMap.put(kaze, false);
		}

		daburuRitiMap.clear();
		for (Kaze kaze : Kaze.values()) {
			daburuRitiMap.put(kaze, false);
		}

		ippatuMap.clear();
		for (Kaze kaze : Kaze.values()) {
			ippatuMap.put(kaze, false);
		}

		this.tyankanFlag = false;
		this.rinsyanFlag = false;
		this.tenhoFlag = true;

		this.tihoMap.clear();
		for (Kaze kaze : Kaze.values()) {
			this.tihoMap.put(kaze, true);
		}

		this.nakiMap.clear();
		for (Kaze kaze : Kaze.values()) {
			this.nakiMap.put(kaze, false);
		}

		this.yamahai.clear();

		// [分岐]赤ありか？
		// 赤ありルール
		if (rule.isAkaAri()) {
			for (HaiType type : HaiType.values()) {
				// 数牌5は赤がある
				if (type.group3() == HaiGroup3.SU && type.number() == 5) {
					// 赤は1枚ずつ
					this.yamahai.add(MajanHai.valueOf(type, true));

					// 赤じゃない数牌5は3枚ずつ
					this.yamahai.add(MajanHai.valueOf(type, false));
					this.yamahai.add(MajanHai.valueOf(type, false));
					this.yamahai.add(MajanHai.valueOf(type, false));
				}
				// それ以外は4枚
				else {
					this.yamahai.add(MajanHai.valueOf(type, false));
					this.yamahai.add(MajanHai.valueOf(type, false));
					this.yamahai.add(MajanHai.valueOf(type, false));
					this.yamahai.add(MajanHai.valueOf(type, false));
				}
			}
		}
		// 赤なしルール
		else {
			for (HaiType type : HaiType.values()) {
				// すべて4枚ずつ
				this.yamahai.add(MajanHai.valueOf(type, false));
				this.yamahai.add(MajanHai.valueOf(type, false));
				this.yamahai.add(MajanHai.valueOf(type, false));
				this.yamahai.add(MajanHai.valueOf(type, false));
			}
		}

		this.wanpai.clear();

		// 王牌を山牌からランダムに14枚選ぶ
		for (int i = 0; i < 14; i++) {
			this.wanpai.add(fetchRandomHai(this.yamahai));
		}

		// 各プレイヤーに13枚ずつ配る
		for (Kaze kaze : Kaze.values()) {
			List<Hai> tehaiList = tehaiMap.get(kaze);
			tehaiList.clear();
			for (int i = 0; i < 13; i++) {
				tehaiList.add(fetchRandomHai(yamahai));
			}
		}

		// 副露牌リストを初期化
		for (Kaze kaze : Kaze.values()) {
			List<Mentu> hurohaiList = hurohaiMap.get(kaze);
			hurohaiList.clear();
		}

		// 副露牌リストを初期化
		for (Kaze kaze : Kaze.values()) {
			List<Sutehai> sutehaiList = sutehaiMap.get(kaze);
			sutehaiList.clear();
		}
	}

	/**
	 * ランダムな牌を山からツモる。
	 * 
	 * @throws IllegalStateException 連続でこのメソッドを呼び出した場合
	 */
	public void doTsumo() {
		if (this.currentTumohai != null)
			throw new IllegalStateException("不正なメソッド呼び出し");
		this.currentTumohai = fetchRandomHai(this.yamahai);
		this.tumoSize++;
	}

	/**
	 * 九種九牌ならtrueを返す。
	 * 
	 * @return 九種九牌ならtrue
	 */
	public boolean isKyusyukyuhai() {
		TehaiList tehai = tehaiMap.get(currentTurn);
		Set<HaiType> set = Functions.getHaiTypeSetFrom(tehai);
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
	 * 現在のツモ牌でツモ上がっているならtrueを返す。
	 * 
	 * @return 現在のツモ牌でツモ上がっているならtrue
	 */
	public boolean isTsumoAgari() {
		Set<Yaku> yaku = this.getYakuSetByFlag(currentTurn, true);
		return isAgari(true, this.currentTumohai, currentTurn, yaku);
	}

	/**
	 * 現在ターンの人がツモ上がりする。
	 */
	public void doTsumoAgari() {
		// TODO insert
	}

	/**
	 * 現在ターンの人が加槓できるならtrueを返す。
	 * 
	 * @return 加槓できるならtrue
	 */
	public boolean isKakanable() {
		if(this.kanSize == 4) {
			return false;
		}
		HurohaiList huro = this.hurohaiMap.get(currentTurn);
		TehaiList tehai = this.tehaiMap.get(currentTurn);
		for (Hai hai : tehai) {
			if (huro.isKakan(hai.type())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 現在ターンの人が加槓する。
	 * 
	 * @param index 牌のインデックス。ツモ牌を指定する場合は13を渡す。
	 * @return 加槓して出来た面子。
	 */
	public Mentu doKakan(int index) {
		TehaiList tehai = tehaiMap.get(currentTurn);
		Hai hai = null;
		if (index == 13) {
			hai = currentTumohai;
		} else {
			hai = tehai.get(index);
		}

		HurohaiList huro = this.hurohaiMap.get(currentTurn);
		assert huro.isKakan(hai.type());

		Mentu m = huro.doKakan(hai);
		if (index == 13) {
			this.currentSutehai = this.currentTumohai;
		} else {
			this.currentSutehai = tehai.remove(index);
			tehai.add(currentTumohai);
		}
		this.currentTumohai = null;

		this.tyankanFlag = true;
		this.ippatuMap.put(currentTurn, false);

		this.atomekuriKanFlag = true;
		return m;
	}

	/**
	 * 現在ターンの人が嶺上牌をツモる。
	 */
	public void doRinsyanTsumo() {
		this.currentTumohai = this.wanpai.get(this.kanSize++);
		this.currentSutehai = null;

		this.tumoSize++;
		this.tyankanFlag = false;
		this.rinsyanFlag = true;
	}

	/**
	 * 現在ターンの人が加槓できる手牌リストの位置のリストを返す。
	 * 
	 * @return ないならnullを返す。
	 */
	public List<Integer> getKakanableHaiList() {
		List<Integer> result = new ArrayList<Integer>();

		HurohaiList huro = this.hurohaiMap.get(currentTurn);
		TehaiList tehai = this.tehaiMap.get(currentTurn);

		for (int i = 0; i < tehai.size(); i++) {
			Hai hai = tehai.get(i);
			if (huro.isKakan(hai.type())) {
				result.add(i);
			}
		}
		return result;
	}

	/**
	 * 現在ターンの人が暗槓できるならtrueを返す。
	 * 
	 * @return　暗槓できるならtrue
	 */
	public boolean isAnkanable() {
		if(this.kanSize == 4) {
			return false;
		}
		TehaiList haiList = tehaiMap.get(currentTurn);
		return haiList.isAnkanable();
	}

	/**
	 * 現在暗槓できる手牌リストの位置のリストのリストを返す。
	 * 
	 * @return　ない場合はnullを返す。
	 */
	public List<List<Integer>> getAnkanableHaiList() {
		TehaiList haiList = tehaiMap.get(currentTurn);
		return haiList.getAnkanableIndexList();
	}

	/**
	 * 指定されたインデックスで暗槓する。indexが13のときはツモ牌
	 * 
	 * @param type 暗槓する牌のインデックス
	 * @return 暗槓して出来た面子。
	 */
	public Mentu doAnkan(int index) {
		TehaiList haiList = tehaiMap.get(currentTurn);
		HaiType type = haiList.get(index).type();

		assert haiList.isAnkanable(type);
		HurohaiList huroList = hurohaiMap.get(currentTurn);

		Hai haiArray[] = new Hai[4];
		int i = 0;
		for (Iterator<Hai> itr = haiList.iterator(); itr.hasNext();) {
			Hai hai = itr.next();
			if (hai.type() == type) {
				itr.remove();
				haiArray[i++] = hai;
			}
		}
		Mentu m = new Mentu(haiArray);
		huroList.add(m);
		this.newDoraSize++;
		this.tenhoFlag = false;
		this.firstTurn = false;
		this.tihoMap.put(currentTurn, false);
		this.currentTumohai = null;
		this.ankanFlag = true;

		return m;
	}

	/**
	 * 指定された風の人がロン出来るならtrueを返す。
	 * 
	 * @param kaze プレイヤーの風
	 * @return　指定された風の人がロン出来るならtrue
	 */
	public boolean isRonable(Kaze kaze) {
		if (kaze == currentTurn)
			return false;

		Set<Yaku> yaku = this.getYakuSetByFlag(kaze, false);
		return isAgari(true, this.currentSutehai, kaze, yaku);
	}

	/**
	 * 指定された風の人が指定された牌がフリテンとなるならtrueを返す。
	 * 
	 * @param kaze フリテンかどうか調べるプレイヤーの風
	 * @param agariHai フリテンかどうか調べる牌
	 * @return フリテンとなるならtrue
	 */
	public boolean isFuriten(Kaze kaze, HaiType type) {
		for (Hai hai : sutehaiMap.get(kaze)) {
			if (hai.type() == type) {
				return true;
			}
		}
		return false;
	}

	/**
	 * ロンする。
	 * 
	 * @param kaze
	 */
	public void doRon(Kaze kaze) {
		// TODO insert
	}

	/**
	 * 指定された風の人が立直しているならtrueを返す。
	 * 
	 * @param kaze 　立直しているか調べる人の風
	 * @return　立直しているならtrue
	 */
	public boolean isReach(Kaze kaze) {
		return this.reachMap.get(kaze);
	}

	/**
	 * 立直時、不要牌の位置リストを返す。
	 * 
	 * @return 不要牌の位置リスト
	 */
	public List<Integer> getReachableHaiList() {
		Kaze kaze = getCurrentTurn();
		CheckerParam param = new CheckerParam();

		param.setNaki(this.nakiMap.get(kaze));
		param.setBakaze(bakaze);
		param.setJikaze(kaze);
		param.setRule(rule);

		TehaiList tehaiList = tehaiMap.get(kaze);
		Hai tsumohai = currentTumohai;

		return AgariFunctions.getReachableIndexList(tehaiList, tsumohai, param);
	}

	/**
	 * 指定された風のプレイヤーがテンパイしているならtrueを返す。
	 * 
	 * @param kaze テンパイしているか確かめる
	 * @return
	 */
	public boolean isTenpai(Kaze kaze) {
		CheckerParam param = new CheckerParam();

		param.setNaki(this.nakiMap.get(kaze));
		param.setBakaze(bakaze);
		param.setJikaze(kaze);
		param.setRule(rule);

		return AgariFunctions.isTenpai(tehaiMap.get(kaze), hurohaiMap.get(kaze), param);
	}

	/**
	 * 立直できるならtrueを返す。
	 * 
	 * @return 立直できるならtrue
	 */
	public boolean isReachable() {
		return !nakiMap.get(currentTurn) && isTenpai(currentTurn);
	}

	/**
	 * 立直する。初順であった場合はダブル立直する。
	 */
	public void doReach() {
		if (this.tihoMap.get(currentTurn)) {
			this.daburuRitiMap.put(currentTurn, true);
		} else {
			this.reachMap.put(currentTurn, true);
		}
	}

	/**
	 * 手牌から指定されたインデックスの牌を切って、捨牌リストに加える。
	 * 
	 * @param index 手牌リストの切りたい牌のインデックス
	 * @throws ArrayIndexOutOfBoundsException 指定されたインデックスが手牌リストの範囲を越えていた場合
	 */
	public void discard(int index) {
		List<Hai> tehaiList = tehaiMap.get(currentTurn);

		this.currentSutehai = tehaiList.remove(index);

		if (currentTumohai != null)
			tehaiList.add(currentTumohai);

		this.kiru();
	}

	/**
	 * ツモ切りする。
	 */
	public void discardTsumoHai() {
		this.currentSutehai = currentTumohai;
		this.kiru();
	}

	/**
	 * 牌を切る際に起こる副作用。
	 */
	private void kiru() {
		this.currentTumohai = null;

		List<Sutehai> sutehaiList = sutehaiMap.get(currentTurn);
		sutehaiList.add(new Sutehai(this.currentSutehai));

		if (this.tumoSize == 1)
			this.tenhoFlag = false;

		this.tihoMap.put(currentTurn, false);
		this.ippatuMap.put(currentTurn, false);

		this.rinsyanFlag = false;
	}

	/**
	 * ドラをめくれる場合めくる。
	 * 
	 * @return ドラをめくった枚数。
	 */
	public int openDora() {
		int result = 0;

		if (atomekuriKanFlag) {
			this.newDoraSize++;
			result++;
			this.atomekuriKanFlag = false;
		}
		if (ankanFlag) {
			this.newDoraSize++;
			result++;
			this.ankanFlag = false;
		}

		return result;
	}

	/**
	 * 指定された風のプレイヤーが明槓できるならtrueを返す。
	 * 
	 * @param kaze 明槓できるか調べるプレイヤーの風
	 * @return　明槓できるならtrue
	 */
	public boolean isMinkanable(Kaze kaze) {
		if (kaze == currentTurn)
			return false;
		if(this.kanSize == 4) {
			return false;
		}
		return tehaiMap.get(kaze).isMinkanable(currentSutehai.type());
	}

	public List<Integer> getMinkanableList() {
		return new ArrayList<Integer>(0);
	}

	/**
	 * 指定された風の人が明槓をする。
	 * 
	 * @param kaze 明槓をするプレイヤーの風
	 * @return 明槓して出来た面子。
	 */
	public Mentu doMinkan(Kaze kaze) {
		assert isMinkanable(kaze);

		TehaiList tehai = this.tehaiMap.get(kaze);
		HurohaiList huro = this.hurohaiMap.get(kaze);
		HaiType type = this.currentSutehai.type();

		Mentu m = new Mentu(this.currentSutehai, currentTurn, tehai.remove(type),
				tehai.remove(type), tehai.remove(type));
		huro.add(m);

		SutehaiList sute = sutehaiMap.get(currentTurn);
		Sutehai sh = sute.remove(sute.size() - 1);
		sute.add(sh.naku(kaze));

		this.naku();
		this.currentTurn = kaze;
		this.atomekuriKanFlag = true;

		return m;
	}

	/**
	 * 指定された風のプレイヤーがポンできるならtrueを返す。
	 * 
	 * @param kaze 　ポンできるか調べるプレイヤーの風
	 * @return　ポンできるならtrue
	 */
	public boolean isPonable(Kaze kaze) {
		if (kaze == currentTurn)
			return false;
		return tehaiMap.get(kaze).isPonable(currentSutehai.type());
	}

	/**
	 * ポンする。
	 * 
	 * @param kaze
	 * @param ponList
	 * @return ポンして出来た面子。
	 */
	public Mentu doPon(Kaze kaze, List<Integer> ponList) {
		assert isPonable(kaze);
		assert ponList.size() == 2;

		int index0 = ponList.get(0);
		int index1 = ponList.get(1);

		TehaiList tehai = this.tehaiMap.get(kaze);
		HurohaiList huro = this.hurohaiMap.get(kaze);

		Hai hai0 = tehai.get(index0);
		Hai hai1 = tehai.get(index1);

		Mentu m = new Mentu(this.currentSutehai, currentTurn, hai0, hai1);
		huro.add(m);

		tehai.remove(index0);
		tehai.remove(index1);

		SutehaiList sute = sutehaiMap.get(currentTurn);

		Sutehai sh = sute.remove(sute.size() - 1);
		sute.add(sh.naku(kaze));

		this.naku();
		this.currentTurn = kaze;
		return m;
	}

	/**
	 * 指定された風のプレイヤーがポンできる牌インデックスのリストを返す。
	 * 
	 * @param kaze ポンするプレイヤーの風
	 * @return 牌インデックスのリスト
	 */
	public List<List<Integer>> getPonableHaiList(Kaze kaze) {
		return tehaiMap.get(kaze).getPonableIndexList(currentSutehai.type());
	}

	/**
	 * 下家がチーできるならtrueを返す。
	 * 
	 * @return　下家がチーできるならtrue
	 */
	public boolean isChiable() {
		return tehaiMap.get(currentTurn.simo()).isChiable(currentSutehai.type());
	}

	/**
	 * チーできる牌リストを返す。
	 * 
	 * @return　チーできる牌リスト
	 */
	public List<List<Integer>> getChiableHaiList() {
		return tehaiMap.get(currentTurn.simo()).getChiableHaiList(currentSutehai.type());
	}

	/**
	 * チーする。
	 * 
	 * @param tiList チーする牌インデックスのリスト
	 * @return チーして出来た面子。
	 */
	public Mentu doChi(List<Integer> tiList) {
		assert isChiable();
		assert tiList.size() == 2;

		Kaze nextTurn = currentTurn.simo();

		TehaiList tehai = tehaiMap.get(nextTurn);
		SutehaiList sute = sutehaiMap.get(currentTurn);

		Sutehai sh = sute.remove(sute.size() - 1);
		sute.add(sh.naku(nextTurn));

		int index0 = tiList.get(0);
		int index1 = tiList.get(1);

		Mentu m = new Mentu(this.currentSutehai, currentTurn, tehai.get(index0), tehai.get(index1));

		TehaiList tehaiList = tehaiMap.get(nextTurn);
		tehaiList.remove(index0);
		tehaiList.remove(index1);

		HurohaiList hrList = hurohaiMap.get(nextTurn);
		hrList.add(m);

		this.naku();
		this.currentTurn = nextTurn;

		return m;
	}

	/**
	 * 鳴いたときの副作用。
	 * 
	 */
	private void naku() {
		this.currentSutehai = null;
		this.firstTurn = false;
		for (Kaze kaze : Kaze.values()) {
			this.ippatuMap.put(kaze, false);
		}
		for (Kaze kaze : Kaze.values()) {
			this.tihoMap.put(kaze, false);
		}
	}

	public boolean isSanchaho() {
		// TODO insert
		return false;
	}

	/**
	 * 四風連打の場合trueを返す。
	 * @return 四風連打の場合true
	 */
	public boolean isSufontsuRenta() {
		if (this.firstTurn == false)
			return false;
		if(this.tumoSize != 4)
			return false;
		
		HaiType haiType = null;
		for (Kaze kaze : Kaze.values()) {
			if (haiType == null) {
				HaiType type = sutehaiMap.get(kaze).get(0).type();
				if (type.group3() != HaiGroup3.KAZE)
					return false;
				haiType = type;
			} else {
				if (haiType != sutehaiMap.get(kaze).get(0).type()) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 四開槓の場合trueを返す。しかし、1人が4回槓をしている場合にはfalseを返す。
	 * @return　四開槓の場合true
	 */
	public boolean isSukaikan() {
		if(this.kanSize != 4) {
			return false;
		}
		return false;
	}
	
	/**
	 * 四家立直の場合trueを返す。
	 * @return 四家立直の場合true。
	 */
	public boolean isSuchaReach() {
		for (Kaze kaze : Kaze.values()) {
			if(!this.reachMap.get(kaze)) {
				return false;
			}
		}
		return true;
	}

	public boolean isRyukyoku() {
		// TODO inserted
		return false;
	}

	public void doRyukyoku() {
		// TODO inserted
	}

	public boolean isRenchan() {
		// TODO inserted
		return false;
	}

	public boolean isTotyuRyukyoku() {
		// TODO inserted
		return false;
	}

	public void doTotyuRyukyoku() {
		// TODO inserted
		return;
	}

	public boolean isSyukyoku() {
		// TODO inserted
		return false;
	}

	public void doSyukyoku() {
		// TODO inserted
	}

	public Rule getRule() {
		return this.rule;
	}

	public Map<Kaze, Player> getPlayerMap() {
		return this.playerMap;
	}

	public Kaze getBakaze() {
		return this.bakaze;
	}

	/**
	 * 現在、表になっているドラのリストを返す。
	 * 
	 * @return 現在、表になっているドラのリスト
	 */
	public List<Hai> getDoraList() {
		List<Hai> list = new ArrayList<Hai>();
		for (int i = 0; i < this.kanSize + 1; i++) {
			list.add(this.wanpai.get(4 + 2 * i));
		}
		return list;
	}

	/**
	 * 裏ドラのリストを返す。
	 * 
	 * @return 裏ドラのリスト。
	 */
	public List<Hai> getUradoraList() {
		List<Hai> list = new ArrayList<Hai>();
		for (int i = 0; i < this.kanSize + 1; i++) {
			list.add(this.wanpai.get(4 + 2 * i + 1));
		}
		return list;
	}

	public List<Mentu> getFuroHaiList(Kaze kaze) {
		return this.hurohaiMap.get(kaze);
	}

	public Map<Player, List<Yaku>> getYakuMap() {
		// TODO changed
		return new HashMap<Player, List<Yaku>>(0);
	}

	public List<Player> getTenpaiPlayerList() {
		// TODO changed
		return new ArrayList<Player>(0);
	}

	private Hai fetchRandomHai(List<Hai> list) {
		return list.remove(rand.nextInt(list.size()));
	}

	public void sortTehaiList() {
		for (Kaze kaze : Kaze.values()) {
			Collections.sort(tehaiMap.get(kaze));
		}
	}

	public void sortTehaiList(Kaze kaze) {
		Collections.sort(tehaiMap.get(kaze));
	}

	/**
	 * 現在のターンを終了して、次のプレイヤーのターンにする。
	 * 
	 */
	public void nextTurn() {
		if (this.firstTurn) {
			if (this.tumoSize >= 4)
				this.firstTurn = false;
		}
		this.currentSutehai = null;
		this.currentTurn = this.currentTurn.simo();
	}

	public Kaze getCurrentTurn() {
		return this.currentTurn;
	}

	public List<Hai> getTehaiList(Kaze kaze) {
		return new ArrayList<Hai>(this.tehaiMap.get(kaze));
	}
	
	/**
	 * 現在の捨て牌マップを返す。捨て牌マップとは
	 * 	風->その風の人の捨て牌リスト
	 * を表すマップである。
	 * @return 現在の捨て牌マップ
	 */
	public Map<Kaze, SutehaiList> getSutehaiMap() {
		return new HashMap<Kaze, SutehaiList>(this.sutehaiMap);
	}
	
	/**
	 * 現在の副露牌マップを返す。副露牌マップとは
	 * 	風->その風の人の副露牌リスト
	 * を表すマップである。
	 * @return 現在の副露牌マップ
	 */
	public Map<Kaze, HurohaiList> getHurohaiMap() {
		return new HashMap<Kaze,HurohaiList>(this.hurohaiMap);
	}
	

	public Kaze getKazeOf(Player p) {
		for (Kaze kaze : playerMap.keySet()) {
		}
		return null;
	}

	public void disp() {
		System.out.println("場風：" + this.bakaze);
		System.out.println(this.tumoSize + "順目");
		System.out.println("");
		System.out.println("山牌(" + this.yamahai.size() + ")：" + this.yamahai);
		System.out.println("王牌(" + this.wanpai.size() + ")：" + this.wanpai);
		System.out.println("");
		for (Kaze kaze : Kaze.values()) {
			List<Hai> tehaiList = this.tehaiMap.get(kaze);
			List<Mentu> hurohaiList = this.hurohaiMap.get(kaze);
			List<Sutehai> sutehaiList = this.sutehaiMap.get(kaze);
			System.out.println(kaze + "の人：");
			System.out.println("\t手牌(" + tehaiList.size() + ")：" + tehaiList);
			System.out.println("\t副露牌(" + hurohaiList.size() + ")：" + hurohaiList);
			System.out.println("\t捨牌(" + sutehaiList.size() + ")：" + sutehaiList);
		}
		System.out.println(String.format("現在ターン：%s, ツモ牌：%s", currentTurn, currentTumohai));
	}

	/**
	 * 現在のツモ牌を返す。
	 * 
	 * @return 現在のツモ牌
	 */
	public Hai getCurrentTsumoHai() {
		return this.currentTumohai;
	}

	/**
	 * 現在の捨て牌を返す。
	 * 
	 * @return 今捨てられた捨て牌
	 */
	public Hai getCurrentSutehai() {
		return this.currentSutehai;
	}

	/**
	 * 現在の槓の回数を返す。
	 * 
	 * @return　槓の回数
	 */
	public int getKanSize() {
		return this.kanSize;
	}

	/**
	 * あがれるかどうかを判定
	 * 
	 * @param tumo ツモかどうか
	 * @param agariHai あがり牌
	 * @param kaze 判定するプレイヤの風
	 * @param flagCheckYakuSet フラグによる役判定
	 * @return あがれるならtrue
	 */
	private boolean isAgari(boolean tumo, Hai agariHai, Kaze kaze, Set<Yaku> flagCheckYakuSet) {
		CheckerParam param = new CheckerParam();
		if (isFuriten(kaze, agariHai.type())) {
			return false;
		}

		// フラグによる役セット
		param.setFlagCheckYakuSet(flagCheckYakuSet);

		// 　ツモかロンか
		param.setTsumo(tumo);

		// 鳴いているか
		param.setNaki(this.nakiMap.get(kaze));

		// あがり牌
		param.setAgariHai(agariHai);

		// 場風
		param.setBakaze(bakaze);

		// 自風
		param.setJikaze(kaze);

		param.setRule(rule);

		return AgariFunctions.isAgari(tehaiMap.get(kaze), hurohaiMap.get(kaze), param);
	}

	/**
	 * フラグによりチェックする役セットを返す。
	 * 
	 * @param kaze 風
	 * @param tumo ツモの場合true
	 * @return フラグによりチェックする役セット
	 */
	public Set<Yaku> getYakuSetByFlag(Kaze kaze, boolean tumo) {
		Set<Yaku> set = new HashSet<Yaku>();

		// 立直
		if (this.reachMap.get(kaze)) {
			set.add(NormalYaku.RICHI);
		}

		// 門前自摸
		if (tumo && !this.nakiMap.get(kaze)) {
			set.add(NormalYaku.TSUMO);
		}

		// 一発
		if (this.ippatuMap.get(kaze)) {
			set.add(NormalYaku.IPPATSU);
		}

		// ほうてい
		if (!tumo && this.tumoSize == 70) {
			set.add(NormalYaku.HOTEI);
		}

		// はいてい
		if (tumo && tumoSize == 70) {
			set.add(NormalYaku.HAITEI);
		}

		// 嶺上開花
		if (tumo && rinsyanFlag) {
			set.add(NormalYaku.RINSYANKAIHO);
		}

		// 搶槓
		if (!tumo && tyankanFlag) {
			set.add(NormalYaku.TYANKAN);
		}

		// ダブル立直
		if (daburuRitiMap.get(kaze)) {
			set.add(NormalYaku.DABURURICHI);
		}

		// 天和
		if (tumo && tenhoFlag) {
			set.add(Yakuman.TENHO);
		}

		// 　地和
		if (tumo && tihoMap.get(kaze)) {
			set.add(Yakuman.CHIHO);
		}

		return set;
	}

	public static void main(String[] args) throws IOException {
		Rule rule = new Rule();
		Map<Kaze, Player> players = new HashMap<Kaze, Player>(4);
		players.put(Kaze.TON, new Player(0, "A", true));
		players.put(Kaze.NAN, new Player(1, "B", true));
		players.put(Kaze.SYA, new Player(2, "C", true));
		players.put(Kaze.PE, new Player(3, "D", true));

		Map<Kaze, AI> ais = new HashMap<Kaze, AI>(4);

		Kyoku kyoku = new Kyoku(rule, players, Kaze.TON);
		kyoku.init();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String buf = null;
		kyoku.doTsumo();
		kyoku.sortTehaiList();
		kyoku.disp();

		AIType01 ai0 = new AIType01(players.get(Kaze.NAN));
		ai0.update(kyoku);
		AIType01 ai1 = new AIType01(players.get(Kaze.SYA));
		ai1.update(kyoku);
		AIType01 ai2 = new AIType01(players.get(Kaze.PE));
		ai2.update(kyoku);

		ais.put(Kaze.NAN, ai0);
		ais.put(Kaze.SYA, ai1);
		ais.put(Kaze.PE, ai2);

		Random rand = new Random();

		while (true) {
			boolean naki = false;
			int num = -1;

			// 人間の場合
			if (players.get(kyoku.getCurrentTurn()).isMan()) {
				buf = reader.readLine();
				num = Integer.parseInt(buf);
			}
			// 　コンピューターの場合
			else {
				AI ai = ais.get(kyoku.getCurrentTurn());
				num = ai.discard();
			}
			if (num == 13) {
				if (kyoku.getCurrentTsumoHai() == null) {
					System.exit(1);
				}
				kyoku.discardTsumoHai();
			} else {
				kyoku.discard(num);
			}

			for (Kaze kaze : Kaze.values()) {
				if (kyoku.isRonable(kaze)) {
					System.out.println(kaze + ":ロンだぜ！");
				}
			}

			if (!naki) {
				for (Kaze kaze : Kaze.values()) {
					if (kyoku.isMinkanable(kaze)) {

						int input = -1;

						// 人間の場合
						if (players.get(kaze).isMan()) {
							System.out.println(kaze);
							System.out.println(-1 + ":明槓しない");
							System.out.println(0 + ":明槓する");
							input = getIntFromIn(reader);
						}
						// 　コンピューターの場合
						else {
							AI ai = ais.get(kaze);
							input = ai.minkan() ? 0 : -1;
						}

						if (input == 0) {
							kyoku.doMinkan(kaze);
							naki = true;
							break;
						}
					}
				}
			}
			if (!naki) {
				for (Kaze kaze : Kaze.values()) {
					if (kyoku.isPonable(kaze)) {
						List<List<Integer>> list = kyoku.getPonableHaiList(kaze);
						int i = 0;
						int input = -1;
						List<Integer> inputList = null;

						// 人間の場合
						if (players.get(kaze).isMan()) {
							System.out.println(-1 + ":ポンしない");
							for (List<Integer> list2 : list) {
								System.out.println(i + ":" + list2);
								i++;
							}
							System.out.println(kaze);
							input = getIntFromIn(reader);
							if (input != -1)
								inputList = list.get(input);
						}
						// 　コンピューターの場合
						else {
							AI ai = ais.get(kaze);
							inputList = ai.pon();
							input = (inputList == null) ? -1 : 0;
						}

						if (input != -1) {
							kyoku.doPon(kaze, inputList);
							naki = true;
							break;
						}
					}
				}
			}
			if (!naki && kyoku.isChiable()) {
				List<List<Integer>> list = kyoku.getChiableHaiList();
				List<Integer> inputList = null;
				int i = 0;
				int input = -1;

				// 人間の場合
				if (players.get(kyoku.getCurrentTurn().simo()).isMan()) {
					System.out.println(-1 + ":チーしない");
					for (List<Integer> list2 : list) {
						System.out.println(i + ":" + list2);
						i++;
					}

					input = getIntFromIn(reader);
					if (input != -1)
						inputList = list.get(input);
				}
				// 　コンピューターの場合
				else {
					AI ai = ais.get(kyoku.getCurrentTurn().simo());
					inputList = ai.chi();
					input = (inputList == null) ? -1 : 0;
				}

				if (input != -1) {
					kyoku.doChi(inputList);
					naki = true;
				}
			}

			if (!naki) {
				System.out.println(kyoku.isSufontsuRenta());
				
				kyoku.nextTurn();
				kyoku.doTsumo();

				if (kyoku.isTsumoAgari()) {
					System.out.println("つもあがりぃ！");
				}

				while (true) {
					while (true) {
						if (kyoku.isKakanable()) {
							List<Integer> list = kyoku.getKakanableHaiList();
							int i = 0;
							System.out.println(-1 + ":加槓しない");
							for (int j : list) {
								System.out.println(i + ":" + j);
								i++;
							}
							int input = -1;
							if (kyoku.currentTurn == Kaze.TON) {
								input = getIntFromIn(reader);
							}
							// 　コンピューターの場合
							else {
								input = rand.nextInt(list.size() + 1);
								if (input == list.size()) {
									input = -1;
								}
							}
							if (input == 0) {
								kyoku.doKakan(input);
								for (Kaze kaze : Kaze.values()) {
									if (kyoku.isRonable(kaze)) {
										System.out.println(kaze + ":ロンだぜ！");
									}
								}
								kyoku.doRinsyanTsumo();
								kyoku.sortTehaiList();
								kyoku.disp();
								continue;
							} else {
								break;
							}
						} else {
							break;
						}
					}
					if (kyoku.isAnkanable()) {
						List<List<Integer>> list = kyoku.getAnkanableHaiList();
						List<Integer> inputList = null;
						int i = 0;
						int input = -1;

						// 人間の場合
						if (players.get(kyoku.getCurrentTurn()).isMan()) {
							System.out.println(-1 + "：暗槓しない");
							for (List<Integer> t : list) {
								System.out.println(i + ":" + t);
								i++;
							}

							input = getIntFromIn(reader);
							if (input != -1)
								inputList = list.get(input);
						}
						// 　コンピューターの場合
						else {
							AI ai = ais.get(kyoku.getCurrentTurn());
							inputList = ai.chi();
							input = (inputList == null) ? -1 : 0;
						}

						if (input != -1) {
							kyoku.doAnkan(inputList.get(0));
							kyoku.doRinsyanTsumo();
							kyoku.sortTehaiList();
							kyoku.disp();
							continue;
						} else {
							break;
						}
					} else {
						break;
					}
				}

				if (kyoku.isReach(kyoku.getCurrentTurn())) {
					kyoku.discardTsumoHai();
				} else {
					if (kyoku.isReachable()) {
						System.out.println(-1 + ":立直しない");
						System.out.println(0 + ":立直する");
						int input = getIntFromIn(reader);
						if (input == 0) {
							kyoku.doReach();
						}
					}
				}
			}

			kyoku.sortTehaiList();
			kyoku.disp();
			
		}
	}

	public static int getIntFromIn(BufferedReader b) throws IOException {
		String buf = b.readLine();
		return Integer.parseInt(buf);
	}
}
