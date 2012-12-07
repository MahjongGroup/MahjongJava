package system;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 1回(1東風,1半荘)の麻雀を表すクラス。
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
	private Map<Player, Integer> sekiMap;

	private static Player winner;

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
		this.sekiMap = new HashMap<Player, Integer>(4);
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
			sekiMap.put(players[i], i);
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
		// オーラスの場合,フラグを立てる.
		if (kyokusu == 4 && rule.getEndKaze() == bakaze) {
			lastKyoku = true;
		}

		Map<Kaze, Player> player = new HashMap<Kaze, Player>(4);

		int oya = getOyaIndex();

		player.put(Kaze.TON, players[oya]);
		player.put(Kaze.NAN, players[(oya + 1) % 4]);
		player.put(Kaze.SYA, players[(oya + 2) % 4]);
		player.put(Kaze.PE, players[(oya + 3) % 4]);
		kyoku = new Kyoku(rule, player, bakaze);
		kyoku.init();
		return kyoku;
	}

	public boolean isEnd() {
		return endFlag;
	}

	/**
	 * 終局後の処理を行う.
	 */
	public void endKyoku() {
		this.kyokuResult = kyoku.createKyokuResult();

		tsumibo += kyokuResult.getReachPlayerSize();

		Player oya = kyokuResult.getOya();

		for (Player p : players) {
			if (kyokuResult.isReach(p)) {
				scores[sekiMap.get(p)] -= 1000;
			}
		}

		if (kyokuResult.isTotyuRyukyoku()) {
			honba += 1;
			return;
		}
		if (kyokuResult.isRyukyoku()) {
			if (isOyaTenpai()) {
				honba += 1;
			} else {
				kyokusu += 1;
				honba += 1;
			}
			// 　罰符支払い
			int tenpaiSize = kyokuResult.getTenpaiSize();
			if (tenpaiSize != 0 && tenpaiSize != 4) {
				for (Player player : players) {
					if (kyokuResult.isTenpai(player)) {
						scores[sekiMap.get(player)] += 3000 / tenpaiSize;
					} else {
						scores[sekiMap.get(player)] -= 3000 / (4 - tenpaiSize);
					}
				}
			}
		}
		// 流局ではない場合
		else {
			int agariSize = kyokuResult.getAgariSize();
			// 和了あり
			if (kyokuResult.isRonAgari()) {
				Player p2 = kyokuResult.getHojuPlayer();
				// 親(含む)のロン
				if (isOyaAgari()) {
					for (Player p : players) {
						if (!kyokuResult.isAgari(p))
							continue;
						// 親の場合
						if (p.equals(oya)) {
							int tmpScore = getCeilScore(kyokuResult
									.getAgariResult(p).getScore(), 6)
									+ 300 * honba;
							scores[sekiMap.get(p)] += tmpScore + 1000 * tsumibo;
							scores[sekiMap.get(p2)] -= tmpScore;
							honba += 1;
							tsumibo = 0;
						} else {
							int tmpScore = getCeilScore(kyokuResult
									.getAgariResult(p).getScore(), 4)
									+ 300 * honba;
							scores[sekiMap.get(p)] += tmpScore;
							scores[sekiMap.get(p2)] -= tmpScore;
						}
					}
				}
				// 子のロン
				else {
					if (agariSize == 1) {
						for (Player p : players) {
							if (!kyokuResult.isAgari(p))
								continue;
							int tmpScore = getCeilScore(kyokuResult
									.getAgariResult(p).getScore(), 4)
									+ 300 * honba;
							scores[sekiMap.get(p)] += tmpScore + 1000 * tsumibo;
							scores[sekiMap.get(p2)] -= tmpScore;
							tsumibo = 0;
						}
					} else {
						if (tsumibo != 0) {
							// 頭ハネ積み棒
							int seki[] = new int[2];
							for (int i : seki) {
								if (i < getOyaIndex())
									i += 4;
							}
							if (seki[0] < seki[1])
								scores[seki[0]] += 1000 * tsumibo;
							else
								scores[seki[1]] += 1000 * tsumibo;
							tsumibo = 0;
						}
						for (Player p : players) {
							if (!kyokuResult.isAgari(p))
								continue;
							int tmpScore = getCeilScore(kyokuResult
									.getAgariResult(p).getScore(), 4)
									+ 300 * honba;
							scores[sekiMap.get(p)] += tmpScore;
							scores[sekiMap.get(p2)] -= tmpScore;
						}
					}
					honba = 0;
					kyokusu += 1;
				}
			}
			// ツモあがりの場合
			if (kyokuResult.isTsumoAgari()) {
				Player tsumoPlayer = kyokuResult.getTsumoAgariPlayer();
				// 親のツモ
				if (isOyaAgari()) {
					int totalScore = 0;
					for (Player player : players) {
						if (player.equals(oya))
							continue;
						int tmpScore = getCeilScore(
								kyokuResult.getAgariResult(tsumoPlayer)
										.getScore(), 2)
								+ 100 * honba;
						scores[sekiMap.get(player)] -= tmpScore;
						totalScore += tmpScore;
					}
					scores[getOyaIndex()] += totalScore + 1000 * tsumibo;
					tsumibo = 0;
					honba += 1;
				}
				// 子のツモ
				else {
					int totalScore = 0;
					for (Player p : players) {
						if (p.equals(tsumoPlayer))
							continue;
						int tmpScore = 0;
						// 　親の場合,子の２倍支払う.
						if (p.equals(oya)) {
							tmpScore = getCeilScore(
									kyokuResult.getAgariResult(tsumoPlayer)
											.getScore(), 2)
									+ 100 * honba;
							scores[sekiMap.get(p)] -= tmpScore;
							totalScore += tmpScore;
						}
						// 子の場合
						else {
							tmpScore = getCeilScore(
									kyokuResult.getAgariResult(tsumoPlayer)
											.getScore(), 1)
									+ 100 * honba;
							scores[sekiMap.get(p)] -= tmpScore;
							totalScore += tmpScore;
						}
					}
					scores[sekiMap.get(tsumoPlayer)] += totalScore + 1000
							* tsumibo;
					honba = 0;
					tsumibo = 0;
					kyokusu += 1;
				}
			}
		}

		if (lastKyoku
				&& !(rule.isSyanyu() && isSyanyuScore())
				&& ((getOyaIndex() == getMaxScorePlayerIndex())
						|| !(isOyaTenpai() || isOyaAgari()))) {
			endFlag = true;
		}
		if (kyokusu == 5) {
			if (lastKyoku) {
				if (rule.isSyanyu()) {
					if (isSyanyuScore()) {
						bakaze = bakaze.simo();
						kyokusu = 1;
					} else
						endFlag = true;
				} else
					endFlag = true;
			} else {
				bakaze = bakaze.simo();
				kyokusu = 1;
			}
		}
		if (rule.isHakoAri() && isHako())
			endFlag = true;
		if (endFlag && tsumibo != 0)
			scores[getMaxScorePlayerIndex()] += 1000 * tsumibo;
	}

	private boolean isOyaTenpai() {
		if (!kyokuResult.isRyukyoku())
			return false;
		return kyokuResult.isTenpai(kyokuResult.getOya());
	}

	private boolean isOyaAgari() {
		return kyokuResult.isAgari(kyokuResult.getOya());
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
	 * 指定されたインデックスのプレイヤーを返す.指定するインデックスは立ち親を0として,後は 順番に1,2,3とした値である.
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
	 * 指定されたインデックスのプレイヤーのスコアを返す.指定するインデックスは立ち親を0として,後は 順番に1,2,3とした値である.
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
	 * 現在の局数を返す.例えば現在,東2局の場合2を,南4局の場合4を返す.
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

	public int getMaxScorePlayerIndex() {
		int maxScore = 0;
		int index = 0;
		for (Player p : players) {
			int seki = sekiMap.get(p);
			int score = scores[seki];
			if (score > maxScore) {
				maxScore = score;
				index = seki;
			}
		}
		return index;
	}

	public boolean isSyanyuScore() {
		if (getScore(getMaxScorePlayerIndex()) < 30000)
			return true;
		return false;
	}

	public boolean isHako() {
		for (int score : scores) {
			if (score < 0)
				return true;
		}
		return false;
	}

	private int getOyaIndex() {
		return (kyokusu - 1) % 4;
	}

	/**
	 * 指定されたスコアをscale倍したものの2桁目の値を切り上げたスコア.
	 * 
	 * @param score
	 *            基本点.
	 * @param scale
	 *            基本点を何倍するか.
	 * @return 計算されたスコア.
	 */
	public static int getCeilScore(int score, int scale) {
		return (int) (Math.ceil(score * scale / 100.0) * 100.0);
	}

	public static void main(String[] args) {
		Player players[] = { new Player(11, "Kohei", true),
				new Player(22, "Shishido", true),
				new Player(33, "Moseshi", true), new Player(44, "Mori", true), };
		Mahjong game = new Mahjong(Arrays.asList(players), new Rule());
		game.init();

		do {
			game.startKyoku();
			// kyoku procedure
			game.disp();
			game.endKyoku();
			game.disp2();
		} while (!game.isEnd());
	}

	public void disp() {
		System.out.println(bakaze + "" + kyokusu + "局　" + honba + "本場　"
				+ "積み棒：" + tsumibo);
		System.out.println("親 :" + players[getOyaIndex()].getName());
		// System.out.println("endFlag: " +endFlag);
		if (lastKyoku)
			System.out.println("オーラス");
		System.out.println();
	}

	@Override
	public String toString() {
		return "players:" + Arrays.toString(players) + " scores:"
				+ Arrays.toString(scores) + " bakaze:" + bakaze + " kyokusu:"
				+ kyokusu + " tsumibo:" + tsumibo + " honba:" + honba;
	}

	public void disp2() {
		if (kyokuResult.isTotyuRyukyoku())
			System.out.println("途中流局");
		if (kyokuResult.isRyukyoku()) {
			System.out.println("流局");
			for (Player p : players) {
				if (!kyokuResult.isTenpai(p))
					continue;
				System.out.println(p.getName() + ": " + true);
			}
			System.out.println("聴牌数 :" + kyokuResult.getTenpaiSize());
			if (kyokuResult.getTenpaiSize() > 0) {
				for (Player p : players) {
					if (!kyokuResult.isTenpai(p))
						continue;
					System.out.println("聴牌プレイヤー :" + p.getName());
				}
			}
		} else if (kyokuResult.getAgariSize() > 0) {
			for (Player p : players) {
				if (kyokuResult.isAgari(p)) {
					if (kyokuResult.isRonAgari())
						System.out.println("和了 :" + p.getName() + "  放銃 :"
								+ kyokuResult.getHojuPlayer().getName() + "  "
								+ kyokuResult.getAgariResult(p).getScore()
								+ "点");
					if (kyokuResult.isTsumoAgari())
						System.out.println("和了 :" + p.getName() + "  自獏 "
								+ kyokuResult.getAgariResult(p).getScore()
								+ "点");
				}
			}
		}
		if (isEnd()) {
			System.out.println("Game is Over.");
			winner = players[getMaxScorePlayerIndex()];
			System.out.println(winner.getName() + " WON!!!");
		}
		for (Player p : sekiMap.keySet()) {
			System.out.println(p.getName() + ": " + scores[sekiMap.get(p)]);
		}
		System.out.println();
	}

	public static Player getWinner() {
		return winner;
	}

}
