package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import system.Kyoku;
import system.Player;
import system.hai.Hai;
import system.hai.HurohaiList;
import system.hai.Kaze;
import system.hai.Mentsu;
import system.hai.SutehaiList;
import system.hai.TehaiList;
import test.Console;
import ai.AI;
import ai.AIType01;
import ai.AIType_Debug;

/**
 * 1回の局を走らせるクラス.
 */
public class KyokuRunner {
	private final Kyoku kyoku;

	private final Map<Kaze, Player> playerMap;
	private final Map<Kaze, AI> aiMap;
	private final Map<Kaze, Transporter> transporterMap;

	private int stateCode;
	private boolean discardFlag;

	/**
	 * 指定された局を動かす局ランナーのコンストラクタ.
	 * 
	 * @param kyoku
	 *            局オブジェクト.
	 */
	public KyokuRunner(Kyoku kyoku, Map<Player, Transporter> trMap) {
		this.kyoku = kyoku;
		this.playerMap = kyoku.getPlayerMap();
		this.aiMap = new HashMap<Kaze, AI>(4);
		for (Player p : playerMap.values()) {
			if (p.isMan())
				continue;
			AI ai = new AIType_Debug(p, false, true);
			ai.update(kyoku);
			this.aiMap.put(kyoku.getKazeOf(p), ai);
		}

		this.transporterMap = new HashMap<Kaze, Transporter>(4);
		for (Player p : trMap.keySet()) {
			this.transporterMap.put(kyoku.getKazeOf(p), trMap.get(p));
		}
	}

	/**
	 * 局を開始する.
	 */
	public void run() {
		kyoku.init();

		stateCode = STATE_CODE_TSUMO;

		kyokuLoop: while (true) {
//			System.out.println("ターンチェンジ");
			playerLoop: while (true) {
				switch (stateCode) {
				case STATE_CODE_TSUMO:
					initTransporterFlag();
					initDiscardFlag();
					doTsumo();
					kyoku.sortTehaiList();
//					kyoku.disp();
					sendNeededInformation();
					stateCode = STATE_CODE_SEND;
					break;

				case STATE_CODE_SEND:
					sendBeforeDiscard();
					stateCode = STATE_CODE_KYUSYUKYUHAI;
					break;

				case STATE_CODE_KYUSYUKYUHAI:
					doKyusyuKyuhai();
					break;

				case STATE_CODE_TSUMOAGARI:
					doTsumoAgari();
					break;

				case STATE_CODE_KAKAN:
					doKakan();
					sendNeededInformation();
					break;

				case STATE_CODE_CHANKANRON:
					doChankanRonAgari();
					break;

				case STATE_CODE_RINSYANTSUMO:
					initTransporterFlag();
					onDiscardFlag();
					doRinsyanTsumo();
					sendNeededInformation();
					stateCode = STATE_CODE_SEND;
					break;

				case STATE_CODE_ANKAN:
					doAnkan();
					sendNeededInformation();
					break;

				case STATE_CODE_ISREACH:
					reach();
					sendNeededInformation();
					break;

				case STATE_CODE_DISCARD:
					kyoku.sortTehaiList();
//					kyoku.disp();
					discard();
					sendNeededInformation();
					stateCode = STATE_CODE_RON;
					break;
				case STATE_CODE_RON:
					sendAfterDiscard();
					doRonAgari();
					sendNeededInformation();
					break;

				case STATE_CODE_SUCHA:
					isSucha(kyoku);
					break;

				case STATE_CODE_NAKI:
					doMinkan();
					sendNeededInformation();
					doPon();
					sendNeededInformation();
					doChi();
					sendNeededInformation();
					onDiscardFlag();
					if (stateCode == STATE_CODE_NAKI) {
						stateCode = STATE_CODE_NEXTTURN;
					}
					initTransporterFlag();
					break;

				case STATE_CODE_NEXTTURN:
					if (kyoku.isRyukyoku()) {
						doTempai();//(仮にここにおいているだけ)
						kyoku.doRyukyoku();
						stateCode = STATE_CODE_ENDOFKYOKU;
					} else {
						kyoku.nextTurn();
						stateCode = STATE_CODE_TSUMO;
					}
					break playerLoop;
				case STATE_CODE_ENDOFKYOKU:
					break kyokuLoop;

				default:
					break;
				}
			}
		}
	}

	/**
	 * 各クライアントに場(捨て牌,副露牌,手牌など)の情報を送る.
	 */
	private void sendNeededInformation() {
		Map<Kaze, HurohaiList> nakihai = kyoku.getHurohaiMap();

		Map<Kaze, List<Hai>> sutehai = new HashMap<Kaze, List<Hai>>();
		for (Kaze kaze : Kaze.values()) {
			SutehaiList sutehailist = kyoku.getSutehaiList(kaze);
			sutehai.put(kaze, sutehailist.toNakiExcludedHaiList());
		}

		List<Integer> tehaiSize = new ArrayList<Integer>();
		for (Kaze kaze : Kaze.values()) {
			tehaiSize.add(kyoku.getTehaiList(kaze).size());
		}

		for (Kaze kaze : transporterMap.keySet()) {
			TehaiList tehai = kyoku.getTehaiList(kaze);
			Server tr = transporterMap.get(kaze);

			tr.sendField(tehai, nakihai, sutehai, kyoku.getCurrentTurn(), kyoku
					.getCurrentSutehai(), tehaiSize, kyoku.getYamahaiList()
					.size(), kyoku.getWanpaiList().size(), kyoku
					.getOpenDoraList());
		}
	}

	/**
	 * ツモを行い,ツモを行ったことをクライアントに知らせる.
	 */
	private void doTsumo() {
		kyoku.doTsumo();
		Server tr = transporterMap.get(kyoku.getCurrentTurn());
		if (tr != null) {
			tr.sendTsumoHai(kyoku.getCurrentTsumoHai());
		}
	}

	// 嶺上ツモするメソッド
	private void doRinsyanTsumo() {
		kyoku.doRinsyanTsumo();
		kyoku.sortTehaiList();
//		kyoku.disp();

		Server tr = transporterMap.get(kyoku.getCurrentTurn());
		if (tr != null) {
			tr.sendTsumoHai(kyoku.getCurrentTsumoHai());
		}
	}

	/**
	 * 九種九牌,ツモ上がり,暗槓,加槓,リーチができるときに,それを送信する.
	 */
	private void sendBeforeDiscard() {
		Player p = kyoku.getCurrentPlayer();
		if (p.isMan()) {
			Server trserver = transporterMap.get(kyoku.getCurrentTurn());
			if (kyoku.isKyusyukyuhai())
				trserver.requestKyusyukyuhai();
			if (kyoku.isTsumoAgari())
				trserver.requestTsumoAgari();
			if (kyoku.isKakanable())
				trserver.sendKakanableIndexList(kyoku.getKakanableHaiList());
			if (kyoku.isAnkanable())
				trserver.sendAnkanableIndexLists(kyoku.getAnkanableHaiList());
			if (kyoku.isReachable())
				trserver.sendReachableIndexList(kyoku.getReachableHaiList());
			Transporter tr = transporterMap.get(kyoku.getCurrentTurn());
			trserver.sendDiscard(isThereTsumohai(tr));
		} else {
			// TODO AIの場合は何もしない？
		}
	}

	// ロン,チー,ポン,明槓を送る
	private void sendAfterDiscard() {
		sendRonAgari();
		sendMinkan();
		sendPon();
		sendChi();
	}

	/**
	 * 九種九牌ができるとき,waitを呼び出す
	 */
	private void doKyusyuKyuhai() {
		if (kyoku.isKyusyukyuhai()) {
			Player p = kyoku.getCurrentPlayer();
			if (p.isMan()) {
				Transporter tr = transporterMap.get(kyoku.getCurrentTurn());
				if (waitKyusyukyuhai(tr)) {
					kyoku.doKyusyukyuhai();
					stateCode = STATE_CODE_ENDOFKYOKU;
					return;
				}
				// while loop を抜ける
			} else {
				AI ai = aiMap.get(kyoku.getCurrentTurn());
				if (ai.isKyusyukyuhai()) {
					kyoku.doKyusyukyuhai();
					stateCode = STATE_CODE_ENDOFKYOKU;
					return;
				}
				// AI
			}
		}
		stateCode = STATE_CODE_TSUMOAGARI;
	}

	// ツモあがりができるとき,waitを呼び出す
	private void doTsumoAgari() {
		if (kyoku.isTsumoAgari()) {
			Player p = kyoku.getCurrentPlayer();
			if (p.isMan()) {
				Transporter tr = transporterMap.get(kyoku.getCurrentTurn());
				boolean tsumoagari = waitTsumoagari(tr);
				if (tsumoagari) {
					kyoku.doTsumoAgari();
					kyoku.doSyukyoku();
					//System.out.println(kyoku.getCurrentPlayer() + " : ツモちゃった！");
					Console.wairEnter();
					stateCode = STATE_CODE_ENDOFKYOKU;
					return;
				} else {
					stateCode = STATE_CODE_KAKAN;
					return;
				}
			} else {
				AI ai = aiMap.get(kyoku.getCurrentTurn());
				if (ai.isTumoAgari()) {
					kyoku.doTsumoAgari();
					kyoku.doSyukyoku();
					//System.out.println(kyoku.getCurrentPlayer() + " : ツモちゃった！");
					Console.wairEnter();
					stateCode = STATE_CODE_ENDOFKYOKU;
					return;
				}
			}
		}
		stateCode = STATE_CODE_KAKAN;
	}

	// 加槓できるとき,waitを呼び出す
	private void doKakan() {
		if (kyoku.isKakanable()) {
			Player p = kyoku.getCurrentPlayer();
			if (p.isMan()) {
				Transporter tr = transporterMap.get(kyoku.getCurrentTurn());
				int kakanindex = waitKakan(tr, kyoku.getKakanableHaiList());
				if (kakanindex != -1) {
					Mentsu kakanMentu = kyoku.doKakan(kakanindex);
					notifyNaki(p, kakanMentu);
					stateCode = STATE_CODE_CHANKANRON;
					return;
				}
			} else {
				AI ai = aiMap.get(kyoku.getCurrentTurn());
				int index = -1;
				if ((index = ai.kakan(kyoku.getKakanableHaiList())) != -1) {
					Mentsu kakanMentu = kyoku.doKakan(kyoku
							.getKakanableHaiList().get(index));
					notifyNaki(p, kakanMentu);
					stateCode = STATE_CODE_CHANKANRON;
					return;
				}
				// AI
			}
		}
		stateCode = STATE_CODE_ANKAN;
	}

	// 鳴いたこととその牌を各プレイヤーに伝える
	private void notifyNaki(Player player, Mentsu mentu) {
		for (Kaze kaze : transporterMap.keySet()) {
			transporterMap.get(kaze).notifyNaki(player, mentu);
		}
	}

	// 暗槓したプレイヤーとその牌を各プレイヤーに伝える
	private void doAnkan() {

		if (kyoku.isAnkanable()) {
			Player p = kyoku.getCurrentPlayer();
			if (p.isMan()) {
				Transporter tr = transporterMap.get(kyoku.getCurrentTurn());
				List<Integer> ankanlist = waitAnkan(tr,
						kyoku.getAnkanableHaiList());
				if (ankanlist != null) {
					Mentsu ankanMentu = kyoku.doAnkan(ankanlist);
					//System.out.println(ankanMentu);
					notifyNaki(p, ankanMentu);

					stateCode = STATE_CODE_RINSYANTSUMO;
					return;
				}
			} else {
				// AI
				AI ai = aiMap.get(kyoku.getCurrentTurn());
				if (ai.ankan(kyoku.getAnkanableHaiList()) != -1) {
					Mentsu ankanMentu = kyoku.doAnkan(kyoku
							.getAnkanableHaiList().get(0));
					notifyNaki(p, ankanMentu);
					stateCode = STATE_CODE_RINSYANTSUMO;
					return;
				}
			}
		}
		stateCode = STATE_CODE_ISREACH;
		// to be defined
	}

	// リーチしているときはツモ切り,リーチできるときはwaitを呼び出す。
	private void reach() {
		Player p = kyoku.getCurrentPlayer();
		Transporter tr = transporterMap.get(kyoku.getCurrentTurn());
		if (p.isMan()) {
			if (kyoku.isReach(kyoku.getCurrentTurn())) {
				try {
					Thread.sleep(1000);
					kyoku.discardTsumoHai();
					sendTsumoGiri(tr);

					//System.out.println("現在捨て牌：" + kyoku.getCurrentSutehai());
					Console.wairEnter();

					stateCode = STATE_CODE_RON;
					return;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				if (kyoku.isReachable()) {
					int reach = waitReach(tr, kyoku.getReachableHaiList());
					if (reach != -1) {
						kyoku.doReach();
						kyoku.discard(reach);

						//System.out
						//		.println("現在捨て牌：" + kyoku.getCurrentSutehai());
						Console.wairEnter();

						for (Kaze kaze : transporterMap.keySet()) {
							Server treach = transporterMap.get(kaze);

							int reachSutehaiIndex = kyoku.getSutehaiList(
									kyoku.getCurrentTurn()).size() - 1;
							for (int i = 0; i < kyoku.getSutehaiList(
									kyoku.getCurrentTurn()).size(); i++) {
								if (kyoku
										.getSutehaiList(kyoku.getCurrentTurn())
										.get(i).isNaki()) {
									reachSutehaiIndex--;
								}
							}
							notifyReach(kyoku.getCurrentTurn(),
									reachSutehaiIndex, treach);
						}
						stateCode = STATE_CODE_RON;
					} else {
						stateCode = STATE_CODE_DISCARD;
					}
				} else {
					stateCode = STATE_CODE_DISCARD;
				}
			}
		} else {
			// AIの処理
			if (kyoku.isReach(kyoku.getCurrentTurn())) {
				// try {
				// Thread.sleep(1000);
				kyoku.discardTsumoHai();
				//System.out.println("現在捨て牌：" + kyoku.getCurrentSutehai());
				Console.wairEnter();

				stateCode = STATE_CODE_RON;
				return;
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
			}
			AI ai = aiMap.get(kyoku.getCurrentTurn());

			if (kyoku.isReachable() && ai.isReach()) {
				kyoku.doReach();
				int index = ai.discard();
				kyoku.discard(index);
				int reachSutehaiIndex = kyoku.getSutehaiList(
						kyoku.getCurrentTurn()).size() - 1;
				for (int i = 0; i < kyoku
						.getSutehaiList(kyoku.getCurrentTurn()).size(); i++) {
					if (kyoku.getSutehaiList(kyoku.getCurrentTurn()).get(i)
							.isNaki()) {
						reachSutehaiIndex--;
					}
				}
				for (Transporter t : transporterMap.values()) {
					t.notifyReach(kyoku.getCurrentTurn(), reachSutehaiIndex);
				}
				//System.out.println("現在捨て牌：" + kyoku.getCurrentSutehai());
				Console.wairEnter();
				stateCode = STATE_CODE_RON;
				return;
			} else {
				stateCode = STATE_CODE_DISCARD;
				return;
			}
			// AI
		}
	}

	// 捨てる牌がクライアントから送られてくるのを待つwaitを呼び出し,ツモ切りか手出しかを区別して捨てる。
	private void discard() {
		Player p = kyoku.getCurrentPlayer();
		if (p.isMan()) {
			Transporter tr = transporterMap.get(kyoku.getCurrentTurn());
			int i = waitDiscarded(tr, isThereTsumohai(tr));

			if (0 <= i && i <= 12) {
				kyoku.discard(i);
				afterDiscard();
			} else if (i == 13) {
				kyoku.discard(13);
				afterDiscard();

			} else {
				//System.out.println("不正な入力です");
			}
		} else {
			AI ai = aiMap.get(kyoku.getCurrentTurn());
			kyoku.discard(ai.discard());
			afterDiscard();
			// AI
		}
	}

	// 捨てられた牌とそのプレイヤーを各プレイヤーに伝える。
	private void afterDiscard() {
		//System.out.println("現在捨て牌：" + kyoku.getCurrentSutehai());
		Console.wairEnter();
		for (Kaze kaze : transporterMap.keySet()) {
			Server tnotify = transporterMap.get(kaze);
			notifyDiscard(kyoku.getCurrentPlayer(), kyoku.getCurrentSutehai(),
					false, tnotify);
		}
	}

	// ロンあがりできるときにプレイヤーにロンするかどうかを聞く
	private void sendRonAgari() {
		for (Kaze kaze : Kaze.values()) {
			Player p = playerMap.get(kaze);
			if (p.isMan()) {
				Server tr = transporterMap.get(kaze);
				if (kyoku.isRonable(kaze))
					tr.requestRon();
			}
		}
	}

	// ロン上がりできると送った後,その回答が送られてくるのを待つ
	private void doRon(List<Player> doRonPlayer) {

		for (Kaze kaze : Kaze.values()) {
			if (kyoku.isRonable(kaze)) {
				Player p = playerMap.get(kaze);
				if (p.isMan()) {
					Transporter tr = transporterMap.get(kaze);
					boolean doRon = waitRon(tr);
					if (doRon) {
						kyoku.doRon(kaze);
						doRonPlayer.add(p);

						Map<Player, List<Hai>> map = new HashMap<Player, List<Hai>>();
						for (int i = 0; i < kyoku.getTehaiList(kaze).size(); i++) {
							map.put(p, kyoku.getTehaiList(kaze));
						}

						for (Kaze ronKaze : transporterMap.keySet()) {
							Server tron = transporterMap.get(ronKaze);
							notifyRon(map, tron);
							//System.out.println(playerMap.get(ronKaze)
							//		+ " : ロンだ！");
							Console.wairEnter();
						}

						if (kyoku.isSanchaho()) {
							kyoku.doTotyuRyukyokuSanchaho();
						}
						kyoku.doSyukyoku();
					} else {
						kyoku.onRonRejected(kaze);
					}
				} else {
					// AI
					AI ai = aiMap.get(kaze);
					if (ai.isRon()) {
						kyoku.doRon(kaze);
						Map<Player, List<Hai>> map = new HashMap<Player, List<Hai>>();
						for (int i = 0; i < kyoku.getTehaiList(kaze).size(); i++) {
							map.put(p, kyoku.getTehaiList(kaze));
						}
						doRonPlayer.add(p);

						for (Kaze ronKaze : transporterMap.keySet()) {
							Server tron = transporterMap.get(ronKaze);
							notifyRon(map, tron);
							//System.out.println(playerMap.get(ronKaze)
							//		+ " : ロンだ！");
							Console.wairEnter();
						}

						if (kyoku.isSanchaho()) {
							kyoku.doTotyuRyukyokuSanchaho();
						}
						kyoku.doSyukyoku();
					} else {
						kyoku.onRonRejected(kaze);
					}
				}
			}

		}

	}

	// 捨て牌からロンあがりする。
	private void doRonAgari() {
		List<Player> doRonPlayer = new ArrayList<Player>();
		doRon(doRonPlayer);
		if (doRonPlayer.size() == 0) {
			stateCode = STATE_CODE_SUCHA;
		} else {
			stateCode = STATE_CODE_ENDOFKYOKU;
		}
	}

	// 加槓した牌からロンあがりする。
	private void doChankanRonAgari() {
		List<Player> doRonPlayer = new ArrayList<Player>();
		doRon(doRonPlayer);
		if (doRonPlayer.size() == 0) {
			stateCode = STATE_CODE_RINSYANTSUMO;
		} else {
			stateCode = STATE_CODE_ENDOFKYOKU;
		}
	}

	// 四家立直,四家連打,四開槓の判定
	private void isSucha(Kyoku k) {
		if (kyoku.isSuchaReach() || kyoku.isSufontsuRenta()
				|| kyoku.isSukaikan()) {
			kyoku.doSuchaReach();
			stateCode = STATE_CODE_ENDOFKYOKU;
		} else {
			stateCode = STATE_CODE_NAKI;
		}
	}

	// 明槓できるときに,そのプレイヤーに明槓できるということを伝える。
	private void sendMinkan() {
		for (Kaze kaze : Kaze.values()) {
			if (kyoku.isMinkanable(kaze)) {
				Player p = playerMap.get(kaze);
				if (p.isMan()) {
					Server tr = transporterMap.get(kaze);
					sendMinkanableIndexList(tr, kyoku.getMinkanableList(kaze));
				} else {
					// AI
				}
			}
		}
	}

	// 明槓すると返ってきたら明槓する。
	private void doMinkan() {
		for (Kaze kaze : Kaze.values()) {
			if (kyoku.isMinkanable(kaze)) {
				Player p = playerMap.get(kaze);
				if (p.isMan()) {
					Transporter tr = transporterMap.get(kaze);
					boolean isMinkanDo = waitMinkan(tr,
							kyoku.getMinkanableList(kaze));
					if (isMinkanDo) {
						Mentsu minkanMentu = kyoku.doMinkan(kaze);
						notifyNaki(p, minkanMentu);

						//System.out.println(kyoku.getPlayer(kaze) + " : 明槓します");
						Console.wairEnter();

						stateCode = STATE_CODE_RINSYANTSUMO;
						return;
					}
				} else {
					AIType01 ai = new AIType01(p);
					if (ai.minkan()) {
						Mentsu minkanMentu = kyoku.doMinkan(kaze);
						notifyNaki(p, minkanMentu);

						//System.out.println(kyoku.getPlayer(kaze) + " : 明槓します");
						Console.wairEnter();

						stateCode = STATE_CODE_RINSYANTSUMO;
					}
				}
			}
		}

	}

	// ポンできるときにポンできるということをそのプレイヤーに伝える。
	private void sendPon() {
		for (Kaze kaze : Kaze.values()) {
			if (kyoku.isPonable(kaze)) {
				Player p = playerMap.get(kaze);
				if (p.isMan()) {
					Server tr = transporterMap.get(kaze);
					sendPonanbleIndexLists(tr, kyoku.getPonableHaiList(kaze));
				} else {

				}
			}
		}
	}

	// ポンすると返ってきたら,ポンする。
	private void doPon() {
		for (Kaze kaze : Kaze.values()) {
			if (kyoku.isPonable(kaze)) {
				Player p = playerMap.get(kaze);
				if (p.isMan()) {
					Transporter tr = transporterMap.get(kaze);
					List<Integer> ponlist = waitPon(tr);
					if (ponlist != null) {
						Mentsu ponMentu = kyoku.doPon(kaze, ponlist);
						notifyNaki(p, ponMentu);

						//System.out.println(kyoku.getPlayer(kaze) + " : ポン！");
						Console.wairEnter();

						stateCode = STATE_CODE_DISCARD;
						return;
					}
				} else {
					// AI
					AI ai = aiMap.get(kaze);
					if (ai.pon(kyoku.getPonableHaiList(kaze)) != -1) {
						Mentsu ponMentu = kyoku.doPon(
								kaze,
								kyoku.getPonableHaiList(kaze).get(
										ai.pon(kyoku.getPonableHaiList(kaze))));
						notifyNaki(p, ponMentu);

						//System.out.println(kyoku.getPlayer(kaze) + " : ポン！");
						Console.wairEnter();

						stateCode = STATE_CODE_DISCARD;
						return;

					}

				}
			}
		}
	}

	// チーできるプレイヤーにチーできるということを伝える。
	private void sendChi() {
		if (kyoku.isChiable()) {
			Player p = playerMap.get(kyoku.getCurrentTurn().simo());
			if (p.isMan()) {
				Server tr = transporterMap.get(kyoku.getCurrentTurn().simo());
				sendChiableIndexLists(tr, kyoku.getChiableHaiList());
			} else {
				// AI
			}
		}
	}

	// チーすると返ってきたとき,チーする
	private void doChi() {
		if (kyoku.isChiable()) {
			Player p = playerMap.get(kyoku.getCurrentTurn().simo());
			if (p.isMan()) {
				Transporter tr = transporterMap.get(kyoku.getCurrentTurn()
						.simo());
				//System.out.println(tr.isChiReceived());

				List<Integer> chilist = waitChi(tr, kyoku.getChiableHaiList());
				//System.out.println("SystemThreadReceived: ");
				if (chilist != null) {
					Mentsu chiMentu = kyoku.doChi(chilist);
					notifyNaki(p, chiMentu);

					//System.out.println(kyoku.getPlayer(kyoku.getCurrentTurn()
					//		.simo()) + " : チーだぜ");
					Console.wairEnter();

					stateCode = STATE_CODE_DISCARD;
					return;
				}

			} else {
				// AI
				AI ai = aiMap.get(kyoku.getCurrentTurn().simo());
				int index = -1;
				if ((index = ai.chi(kyoku.getChiableHaiList())) != -1) {
					Mentsu chiMentu = kyoku.doChi(kyoku.getChiableHaiList().get(
							index));
					notifyNaki(p, chiMentu);

					//System.out.println(kyoku.getPlayer(kyoku.getCurrentTurn()
					//		.simo()) + " : チーだぜ");
					Console.wairEnter();

					stateCode = STATE_CODE_DISCARD;
					return;
				}
			}

		}
	}

	// 全てのプレイヤーのトランスポータのフラグ等を元に戻す。
	private void initTransporterFlag() {
		for (Transporter tr : transporterMap.values()) {
			tr.init();
		}
	}
	
	//discardFlagを初期化にする
	private void initDiscardFlag(){
		discardFlag =false;
	}

	//discardFlagをonにする
	private void onDiscardFlag(){
		discardFlag = true;
	}
	
	/*
	 * **********************A()******************************
	 */

	// requestKyusyukyuhai()が送られたとき,その返答が返ってくるまで待つ
	private boolean waitKyusyukyuhai(Transporter tr) {
		// tr.requestKyusyukyuhai();
		while (!tr.isKyusyukyuhaiReceived() && !tr.getGrandFlag()
				&& !tr.isDiscardedReceived()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return tr.getKyusyukyuhaiResult();
	}

	// ツモしてから牌が切られるのか,ツモせずに牌が切られる(ポン,チー)のか
	private boolean isThereTsumohai(Transporter tr) {
		return tr.isThereTsumohai();
	}

	// 捨て牌を選べという命令を送って,待つ
	private int waitDiscarded(Transporter tr, boolean tumoari) {
		if(discardFlag)
			tr.sendDiscard(tumoari);
		
		while (!tr.isDiscardedReceived()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return tr.getDiscardedIndex();
	}

	private void sendTsumoGiri(Server tr) {
		tr.sendTsumoGiri();
	}

	// チーできる牌のリストを送る。
	private void sendChiableIndexLists(Server tr, List<List<Integer>> list) {
		tr.sendChiableIndexLists(list);
	}

	// チーできると送った後,その回答が返ってくるまで待つ。
	private List<Integer> waitChi(Transporter tr, List<List<Integer>> sendlist) {
		while (!tr.isChiReceived() && !tr.isPonReceived()
				&& !tr.isMinkanReceived()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return tr.getChiIndexList();
	}

	// ポンできると送った後,その回答が返ってくるまで待つ。
	private List<Integer> waitPon(Transporter tr) {
		while (!tr.isPonReceived() && !tr.isChiReceived()
				&& !tr.isMinkanReceived()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return tr.getPonIndexList();
	}

	// ポンできるとき,そのポンできる牌のリストを送る
	private void sendPonanbleIndexLists(Server tr, List<List<Integer>> list) {
		tr.sendPonableIndexLists(list);
	}

	// 暗槓できるということを送った後,その回答を待つ
	private List<Integer> waitAnkan(Transporter tr, List<List<Integer>> sendlist) {
		while (!tr.isAnkanReceived() && !tr.getGrandFlag()
				&& !tr.isDiscardedReceived()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return tr.getAnkanIndexList();

	}

	// 明槓できると送ったあと,その回答を待つ
	private boolean waitMinkan(Transporter tr, List<Integer> sendIndexList) {
		while (!tr.isMinkanReceived() && !tr.isChiReceived()
				&& !tr.isPonReceived()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return tr.isMinkanDo();
	}

	// 明槓できる牌のリストを送る
	private void sendMinkanableIndexList(Server tr, List<Integer> sendIndexList) {
		tr.sendMinkanableIndexList(sendIndexList);
	}

	// 加槓できると送った後,その回答が返ってくるのを待つ。
	private int waitKakan(Transporter tr, List<Integer> sendKakanList) {
		while (!tr.isKakanReceived() && !tr.getGrandFlag()
				&& !tr.isDiscardedReceived()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return tr.getKakanindex();
	}

	// リーチできるとき,リーチするかどうかの回答が返ってくるまで待つ。
	private int waitReach(Transporter tr, List<Integer> sendReachableList) {
		while (!tr.isReachReceived() && !tr.getGrandFlag()
				&& !tr.isDiscardedReceived()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return tr.getReachHaiIndex();
	}

	// ロン上がりできると送った後,その回答が返ってくるのを待つ
	private boolean waitRon(Transporter tr) {
		while (!tr.isRonReceived()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return tr.isRonDo();
	}

	// ツモ上がりできると送った後,その回答が返ってくるのを待つ
	private boolean waitTsumoagari(Transporter tr) {
		while (!tr.isTsumoagariDo() && !tr.isDiscardedReceived()
				&& !tr.getGrandFlag()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return tr.isTsumoagariDo();
	}

	// 切った牌とそのプレイヤーを各プレイヤーに伝える
	private void notifyDiscard(Player player, Hai hai, boolean tumokiri,
			Server tr) {
		tr.notifyDiscard(player, hai, tumokiri);
	}

	// ロンしたプレイヤーのリストとそのあがった手牌を各プレイヤーに送る。
	private void notifyRon(Map<Player, List<Hai>> map, Server tr) {
		tr.notifyRon(map);
	}

	//リーチしたプレイヤーと何の牌でリーチしたかを各プレイヤーに送る。
	private void notifyReach(Kaze currentTurn, int sutehaiIndex, Server tr) {
		tr.notifyReach(currentTurn, sutehaiIndex);
	}

	//聴牌したプレイヤーの手牌を周りに見せる
	@SuppressWarnings("null")
	private void doTempai(){
		Map<Player,List<Hai>> tempaiMap = null;
		Server ttempai = null;
		for (Kaze kaze : Kaze.values()){
			if(kyoku.isTenpai(kaze)){
				tempaiMap.put(kyoku.getPlayer(kaze),kyoku.getTehaiList(kaze));
			}
			ttempai = transporterMap.get(kaze);
		}
		notifyTempai(tempaiMap,ttempai);
	}
	
	//聴牌したことを周りに知らせる、
	private void notifyTempai(Map<Player,List<Hai>> tehaimap,Server tr){
		tr.notifyTempai(tehaimap);		
	}
	
	public static final int STATE_CODE_TSUMO = 0;
	public static final int STATE_CODE_KYUSYUKYUHAI = 1;
	public static final int STATE_CODE_TSUMOAGARI = 2;
	public static final int STATE_CODE_KAKAN = 3;
	public static final int STATE_CODE_CHANKANRON = 4;
	public static final int STATE_CODE_RINSYANTSUMO = 5;
	public static final int STATE_CODE_ANKAN = 6;
	public static final int STATE_CODE_ISREACH = 7;
	public static final int STATE_CODE_DISCARD = 8;
	public static final int STATE_CODE_SUCHA = 9;
	public static final int STATE_CODE_NAKI = 10;
	public static final int STATE_CODE_RON = 11;
	public static final int STATE_CODE_SEND = 12;
	public static final int STATE_CODE_NEXTTURN = 13;
	public static final int STATE_CODE_ENDOFKYOKU = 14;

}
