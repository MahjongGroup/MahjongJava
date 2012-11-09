package system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 1回(1東風、1半荘)の麻雀を表すクラス。
 */
public class Mahjong {
	private final Player players[];
	private final int scores[];
	private final Rule rule;
	private Kaze bakaze;
	private int kyokusu; int tsumibo;
	private int honba;
	private boolean lastKyoku;
	
	/**
	 * 麻雀ゲームのコンストラクタ.プレイヤーのリストは親から順番に入っている必要がある.
	 * @param players 親から席順にはいったプレイヤーのリスト.
	 * @param rule ルール.
	 */
	public Mahjong(List<Player> players, Rule rule) {
		this.players = players.toArray(new Player[0]);
		this.scores = new int[4];
		this.rule = rule;
	}
	
	/**
	 * 初期化する.ここでいう初期化は点数などの初期化である.
	 */
	public void init() {
		for (int i = 0; i < scores.length; i++) {
			scores[i] = 0;
		}
		bakaze = Kaze.TON;
		kyokusu = 1;
		tsumibo = 0;
		honba = 0;
		lastKyoku = false;
	}

	/**
	 * 次の局をスタートする.
	 * @return 次の局を表すオブジェクト.
	 */
	public Kyoku startKyoku() {
		// オーラスの場合、フラグを立てる.
		if(kyokusu == 4 && rule.getEndKaze() == bakaze) {
			lastKyoku = true;
		}
		
		Map<Kaze, Player> player = new HashMap<Kaze, Player>(4);
		player.put(Kaze.TON, players[0]);
		player.put(Kaze.NAN, players[1]);
		player.put(Kaze.SYA, players[2]);
		player.put(Kaze.PE, players[3]);
		return new Kyoku(rule, player, bakaze);
	}
	
	/**
	 * このゲームに参加しているプレイヤーの配列を返す.この配列のインデックス0は立ち親.
	 * @return プレイヤーの配列.
	 */
	public Player[] getPlayers() {
		return players.clone();
	}

	/**
	 * 指定されたインデックスのプレイヤーを返す.指定するインデックスは立ち親を0として、後は
	 * 順番に1,2,3とした値である.
	 * @param index インデックス.立ち親の席を0とした席順の番号.
	 * @return 指定されたインデックスのプレイヤー.
	 */
	public Player getPlayer(int index) {
		return players[index];
	}
	/**
	 * このゲームの現在のスコアの配列を返す.この配列の順番はgetPlayersで返されるプレイヤーの
	 * 配列の順番と対応している.
	 * @return 現在のスコアの配列.
	 */
	public int[] getScores() {
		return scores.clone();
	}
	
	/**
	 * 指定されたインデックスのプレイヤーのスコアを返す.指定するインデックスは立ち親を0として、後は
	 * 順番に1,2,3とした値である.
	 * @param index インデックス.立ち親の席を0とした席順の番号.
	 * @return 指定されたインデックスのプレイヤーのスコア.
	 */
	public int getScore(int index) {
		return scores[index];
	}

	/**
	 * このゲームで採用されているルールを返す.
	 * @return このゲームで採用されているルール.
	 */
	public Rule getRule() {
		return rule;
	}

	/**
	 * 現在の場風を返す.
	 * @return 現在の場風.
	 */
	public Kaze getBakaze() {
		return bakaze;
	}

	/**
	 * 現在の局数を返す.例えば現在、東2局の場合2を、南4局の場合4を返す.
	 * @return 現在の局数.
	 */
	public int getKyokusu() {
		return kyokusu;
	}

	/**
	 * 現在の積み棒の数を返す.
	 * @return 現在の積み棒の数.
	 */
	public int getTsumibo() {
		return tsumibo;
	}

	/**
	 * 現在の本場を返す.
	 * @return 現在の本場.
	 */
	public int getHonba() {
		return honba;
	}
	
	
}
