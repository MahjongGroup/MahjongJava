package system;

import java.util.Arrays;
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
	private int kyokusu;
	int tsumibo;
	private int honba;
	private boolean lastKyoku;

	private Kyoku kyoku;
	private KyokuResult kyokuResult;
	private boolean endFlag;
	private Map<Player, Integer> sekimap;

	/**
	 * 麻雀ゲームのコンストラクタ.プレイヤーのリストは親から順番に入っている必要がある.
	 * 
	 * @param players
	 *            親から席順にはいったプレイヤーのリスト.
	 * @param rule
	 *            ルール.
	 */
	public Mahjong(List<Player> players, Rule rule) {
		this.players = players.toArray(new Player[0]);
		this.sekimap = new HashMap<Player, Integer>(4);
		this.scores = new int[4];
		this.rule = rule;
	}

	/**
	 * 初期化する.ここでいう初期化は点数などの初期化である.
	 */
	public void init() {
		for (int i = 0; i < scores.length; i++) {
			scores[i] = 25000;
		}
		for (int i = 0; i < players.length; i++) {
			sekimap.put(players[i], i);
		}
		bakaze = Kaze.TON;
		kyokusu = 1;
		tsumibo = 0;
		honba = 0;
		lastKyoku = false;
		endFlag = false;
	}

	/**
	 * 次の局をスタートする.
	 * 
	 * @return 次の局を表すオブジェクト.
	 */
	public Kyoku startKyoku() {
		// オーラスの場合、フラグを立てる.
		if (kyokusu == 4 && rule.getEndKaze() == bakaze) {
			lastKyoku = true;
		}

		Map<Kaze, Player> player = new HashMap<Kaze, Player>(4);

		player.put(Kaze.TON, players[(kyokusu - 1) % 4]);
		player.put(Kaze.NAN, players[(kyokusu) % 4]);
		player.put(Kaze.SYA, players[(kyokusu + 1) % 4]);
		player.put(Kaze.PE, players[(kyokusu + 2) % 4]);
		kyoku = new Kyoku(rule, player, bakaze);
		return kyoku;
	}

	public static void main(String[] args) {
		Player players[] = { new Player(11, "Kohei", true),
				new Player(22, "Shishido", true),
				new Player(33, "Shikatani", true),
				new Player(44, "Mori", true), };
		Mahjong game = new Mahjong(Arrays.asList(players), new Rule());
		game.init();
		do {
			Kyoku kyoku = game.startKyoku();
			// kyoku procedure
			game.disp();
			game.endKyoku();
		} while (!game.isEnd());
	}

	public void disp() {
		System.out.println(bakaze + "" + kyokusu + "局" + honba + "本場");
		// System.out.println("endFlag: " +endFlag);
		if (lastKyoku)
			System.out.println("オーラス");

	}

	public boolean isEnd() {
		return endFlag;
	}

	public void endKyoku() {
		this.kyokuResult = kyoku.createKyokuResult();
		tsumibo += kyokuResult.getReachPlayerSize();

		if (kyokuResult.isTotyuRyukyoku()) {
			honba += 1;
			return;
		}
		if (kyokuResult.isRyukyoku()) {
			Map<Player, Boolean> tenpaiMap = kyokuResult.getTenpaiMap();
			Player p[] = kyokuResult.getTenpaiPlayer();
			
			if (kyokuResult.isOyaTempai()) {
				honba += 1;
			} else {
				kyokusu += 1;
				honba += 1;
			}
			// 　罰符支払い
			if (p != null) {
				int tenpaiSize = kyokuResult.getTenpaiSize();
				for (Player player : tenpaiMap.keySet()) {
					if(tenpaiMap.get(player)) {
						scores[this.sekimap.get(player)] += 3000 / tenpaiSize;
					}else {
						scores[this.sekimap.get(player)] -= 3000 / (4-tenpaiSize);
					}
				}
//				int i = p.length;
//				for (Player player : players) {
//					boolean flag = false;
//					for (int j = 0; j < i; j++) {
//						if (p[i].getId() == player.getId()){
//							scores[sekimap.get(player)] += 3000 / i;
//							flag = true;
//						}
//					}
//					if(!flag)scores[sekimap.get(player)] -= 3000 / (4 - i);
//				}
			}
		} else {
			Player p1[] = kyokuResult.getAgariPlayer();
			
			
			if(kyokuResult.isOyaAgari()){
				honba += 1;
				scores[(kyokusu - 1) % 4] += 1000 * tsumibo;
			}
			Player p2 = kyokuResult.getHojuPlayer();
			for(Player player: players){
				for(int i =0;i<p1.length;i++){
					if(player.equals(p1)){
						
					}
				}
			}
		}
		
		if (kyokusu == 4 && bakaze == rule.getEndKaze())
			lastKyoku = true;
		if (kyokusu == 5) {
			if (bakaze == rule.getEndKaze()) {
				if (rule.isSyanyu()) {
					if (isSyanyuScore()) {
						bakaze = bakaze.simo();
						kyokusu = 1;
					}
				} else
					endFlag = true;
			} else {
				bakaze = bakaze.simo();
				kyokusu = 1;
			}
		}
	}

	/**
	 * このゲームに参加しているプレイヤーの配列を返す.この配列のインデックス0は立ち親.
	 * 
	 * @return プレイヤーの配列.
	 */
	public Player[] getPlayers() {
		return players.clone();
	}

	/**
	 * 指定されたインデックスのプレイヤーを返す.指定するインデックスは立ち親を0として、後は 順番に1,2,3とした値である.
	 * 
	 * @param index
	 *            インデックス.立ち親の席を0とした席順の番号.
	 * @return 指定されたインデックスのプレイヤー.
	 */
	public Player getPlayer(int index) {
		return players[index];
	}

	/**
	 * このゲームの現在のスコアの配列を返す.この配列の順番はgetPlayersで返されるプレイヤーの 配列の順番と対応している.
	 * 
	 * @return 現在のスコアの配列.
	 */
	public int[] getScores() {
		return scores.clone();
	}

	/**
	 * 指定されたインデックスのプレイヤーのスコアを返す.指定するインデックスは立ち親を0として、後は 順番に1,2,3とした値である.
	 * 
	 * @param index
	 *            インデックス.立ち親の席を0とした席順の番号.
	 * @return 指定されたインデックスのプレイヤーのスコア.
	 */
	public int getScore(int index) {
		return scores[index];
	}

	/**
	 * このゲームで採用されているルールを返す.
	 * 
	 * @return このゲームで採用されているルール.
	 */
	public Rule getRule() {
		return rule;
	}

	/**
	 * 現在の場風を返す.
	 * 
	 * @return 現在の場風.
	 */
	public Kaze getBakaze() {
		return bakaze;
	}

	/**
	 * 現在の局数を返す.例えば現在、東2局の場合2を、南4局の場合4を返す.
	 * 
	 * @return 現在の局数.
	 */
	public int getKyokusu() {
		return kyokusu;
	}

	/**
	 * 現在の積み棒の数を返す.
	 * 
	 * @return 現在の積み棒の数.
	 */
	public int getTsumibo() {
		return tsumibo;
	}

	/**
	 * 現在の本場を返す.
	 * 
	 * @return 現在の本場.
	 */
	public int getHonba() {
		return honba;
	}

	public boolean isSyanyuScore() {
		for (int score : scores) {
			if (score < 30000)
				return true;
		}
		return false;
	}

}
