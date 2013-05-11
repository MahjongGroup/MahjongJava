package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import system.hai.Kaze;
import system.test.Kyoku;
import system.test.Mahjong;
import system.test.Player;
import system.test.Rule;
import ai.AI;
import ai.AIType01;
import ai.AIType_Debug;
import ai.AIType_Ex1;
import ai.AbstractAI;

// DEBUG class
public class Main {

	public static void main(String[] args) throws IOException {
		ServerMessageType.ANKAN_INDEX_LIST_RECEIVED.newInstance(0);
		Class<Integer> c = Integer.class;
		Object obj = 100;
		System.out.println(c.cast(obj));
//		Main.run();
	}
	// DEBUG
	public static void run() throws IOException {

		List<Player> playerList = new ArrayList<Player>();
		playerList.add(new Player(0, "imatom", false));
		playerList.add(new Player(1, "morimitsu", false));
		playerList.add(new Player(2, "moseshi", false));
		playerList.add(new Player(3, "filshion", false));
		
		
		Map<Player, AI> aiMap = new HashMap<Player, AI>(4);
		for (Player p : playerList) {
			//AIの設定
			aiMap.put(p, new AIType_Debug(p, false, true));
		}

		Mahjong mahjong = new Mahjong(playerList, new Rule());
		mahjong.init();

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		mahjong: while (!mahjong.isEnd()) {
			Kyoku kyoku = mahjong.startKyoku();
			
			Map<Kaze, Player> players = new HashMap<Kaze, Player>(4);
			for (Kaze kaze : Kaze.values()) {
				players.put(kaze, kyoku.getPlayer(kaze));
			}
			
			Map<Kaze, AI> ais = new HashMap<Kaze, AI>(4);
			for (Kaze kaze : Kaze.values()) {
				ais.put(kaze, aiMap.get(kyoku.getPlayer(kaze)));
			}
			
			mahjong.disp();
			
			for (AI ai : ais.values()) {
				((AbstractAI) ai).update(kyoku);
			}

			kyoku.doTsumo();

			kyoku.sortTehaiList();
			kyoku.disp();

			boolean naki = false;

			kyoku: while (true) {

				if (!naki) {
					if (kyoku.isTsumoAgari()) {
						System.out.println("つもあがりぃ！");
						kyoku.doTsumoAgari();
						break kyoku;
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
								if (players.get(kyoku.getCurrentTurn()).isMan()) {
									input = getIntFromIn(reader, -1, list.size());
								}
								// 　コンピューターの場合
								else {
									AI ai = ais.get(kyoku.getCurrentTurn());
									input = ai.kakan(kyoku.getKakanableHaiList());
								}
								if (input != -1) {
									kyoku.doKakan(list.get(input));
									for (Kaze kaze : Kaze.values()) {
										if (kyoku.isRonable(kaze)) {
											System.out.println(kaze + ":ロンだぜ！");
											kyoku.doRon(kaze);
											break kyoku;
										}
									}
									kyoku.doRinsyanTsumo();
									kyoku.sortTehaiList();
									kyoku.disp();
									continue kyoku;
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

								input = getIntFromIn(reader, -1, list.size() - 1);
							}
							// 　コンピューターの場合
							else {
								AI ai = ais.get(kyoku.getCurrentTurn());
								input = ai.ankan(list);
							}

							if (input != -1) {
								inputList = list.get(input);
								kyoku.doAnkan(inputList);
								kyoku.doRinsyanTsumo();
								kyoku.sortTehaiList();
								kyoku.disp();
								continue kyoku;
							} else {
								break;
							}
						} else {
							break;
						}
					}

					aaa: if (!kyoku.isReach(kyoku.getCurrentTurn())) {
						if (kyoku.isReachable()) {
							int input = -1;
							kyoku.disp();
							if (players.get(kyoku.getCurrentTurn()).isMan()) {
								System.out.println(-1 + ":立直しない");
								System.out.println(0 + ":立直する");
								input = getIntFromIn(reader, -1, 0);
							} else {
								AI ai = ais.get(kyoku.getCurrentTurn());
								if (ai.reach(null))
									input = 0;
							}
							if (input == 0) {
								System.out.println("リ～～～チ！！");
								kyoku.doReach();
								int num = -1;
								if (players.get(kyoku.getCurrentTurn()).isMan()) {
									num = getIntFromIn(reader, 0,
											kyoku.sizeOfTehai(kyoku.getCurrentTurn()), 13);
								} else {
									AI ai = ais.get(kyoku.getCurrentTurn());
									num = ai.discard();
								}
								if (num == 13) {
									if (kyoku.getCurrentTsumoHai() == null) {
										System.exit(1);
									}
									kyoku.discard(num);
								} else {
									kyoku.discard(num);
								}
								break aaa;
							}
						}
						int num = -1;

						// 人間の場合
						if (!kyoku.isReach(kyoku.getCurrentTurn())) {
							if (players.get(kyoku.getCurrentTurn()).isMan()) {
								num = getIntFromIn(reader, 0,
										kyoku.sizeOfTehai(kyoku.getCurrentTurn()), 13);
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
								kyoku.discard(num);
							} else {
								kyoku.discard(num);
							}
						}
					} else {
						System.out.println("ツモ切りやし");
						kyoku.discardTsumoHai();
					}
				} else {
					naki = false;

					int num = -1;

					// 人間の場合
					if (!kyoku.isReach(kyoku.getCurrentTurn())) {
						if (players.get(kyoku.getCurrentTurn()).isMan()) {
							num = getIntFromIn(reader, 0,
									kyoku.sizeOfTehai(kyoku.getCurrentTurn()), 13);
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
						}
						kyoku.discard(num);
					}
				}
				for (Kaze kaze : Kaze.values()) {
					if (kyoku.isRonable(kaze)) {
						System.out.println(kaze + ":ロンだぜ！");
						kyoku.doRon(kaze);
						break kyoku;
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
								input = getIntFromIn(reader, -1, 0);
							}
							// 　コンピューターの場合
							else {
								AI ai = ais.get(kaze);
								input = ai.minkan() ? 0 : -1;
							}

							if (input == 0) {
								System.out.println("明槓ですね");
								kyoku.doMinkan(kaze);
								kyoku.doRinsyanTsumo();
								kyoku.sortTehaiList();
								kyoku.disp();
								continue kyoku;
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
								input = getIntFromIn(reader, -1, list.size() - 1);
							}
							// 　コンピューターの場合
							else {
								AI ai = ais.get(kaze);
								input = ai.pon(list);
							}

							if (input != -1) {
								inputList = list.get(input);
								System.out.println("ポンしたる");
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

						input = getIntFromIn(reader, -1, list.size() - 1);
					}
					// 　コンピューターの場合
					else {
						AI ai = ais.get(kyoku.getCurrentTurn().simo());
						input = ai.chi(list);
					}

					if (input != -1) {
						inputList = list.get(input);
						System.out.println("チーだな");
						kyoku.doChi(inputList);
						naki = true;
					}
				}

				if (!naki) {
					kyoku.nextTurn();
					
					if (kyoku.isRyukyoku()) {
						System.out.println("糸冬　局");
						kyoku.doRyukyoku();
						break kyoku;
					}
					kyoku.doTsumo();

				}

				kyoku.sortTehaiList();
				kyoku.disp();

			}
			
			mahjong.endKyoku();
			mahjong.disp2();
		}
	}

	// DEBUG
	public static int getIntFromIn(BufferedReader b, int min, int max, int... permit)
			throws IOException {
		int ret = 0;
		Pattern p = Pattern.compile("[0-9]+|-[0-9]+");

		while (true) {
			String buf = b.readLine();
			Matcher m = p.matcher(buf);

			if (!m.matches()) {
				System.out.print("不正な入力です。：");
				continue;
			}
			ret = Integer.parseInt(buf);
			if (ret < min || max < ret) {
				for (int i : permit) {
					if (i == ret) {
						break;
					}
				}
				System.out.println("範囲外です:" + ret);
				continue;
			}
			break;
		}

		return ret;
	}
}