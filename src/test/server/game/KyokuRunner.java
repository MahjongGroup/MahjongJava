package test.server.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import server.Server;
import server.Transporter;
import system.Kyoku;
import system.Player;
import system.hai.Hai;
import system.hai.HurohaiList;
import system.hai.Kaze;
import system.hai.Mentsu;
import system.hai.SutehaiList;
import system.hai.TehaiList;
import test.Console;
import test.server.ServerMessage;
import test.server.ServerMessageType;
import test.server.ServerReceiver;
import test.server.ServerSender;
import test.server.SingleServerReceiver;
import test.server.SingleServerSender;
import ai.AI;
import ai.AIType01;
import ai.AIType_Debug;

import static test.server.ServerMessage.*;

/**
 * 1回の局を走らせるクラス.
 */
public class KyokuRunner {
	private final Kyoku kyoku;

	private final Map<Kaze, Player> playerMap;
	private final Map<Kaze, AI> aiMap;
	// private Map<Kaze, Transporter> transporterMap;

	private final ServerReceiver receiver;
	private final ServerSender sender;

	private int stateCode;
	private boolean discardFlag;

	/**
	 * 指定された局を動かす局ランナーのコンストラクタ.
	 * 
	 * @param kyoku 局オブジェクト.
	 */
	public KyokuRunner(Kyoku kyoku, ServerSender sender, ServerReceiver receiver) {
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

		this.receiver = receiver;
		this.sender = sender;
	}

	/**
	 * 局を開始する.
	 */
	public void run() {
		kyoku.init();
		stateCode = STATE_CODE_TSUMO;

		kyokuLoop: while (true) {
			playerLoop: while (true) {
				switch (stateCode) {
				case STATE_CODE_TSUMO:
					STATE_CODE_TSUMO();
					break;
				case STATE_CODE_SEND:
					STATE_CODE_SEND();
					break;
				case STATE_CODE_KYUSYUKYUHAI:
					STATE_CODE_KYUSYUKYUHAI();
					break;
				case STATE_CODE_TSUMOAGARI:
					STATE_CODE_TSUMOAGARI();
					break;
				case STATE_CODE_KAKAN:
					STATE_CODE_KAKAN();
					break;
				case STATE_CODE_CHANKANRON:
					STATE_CODE_TYANKANRON();
					break;
				case STATE_CODE_RINSYANTSUMO:
					STATE_CODE_RINSYANTSUMO();
					break;
				case STATE_CODE_ANKAN:
					STATE_CODE_ANKAN();
					break;
				case STATE_CODE_ISREACH:
					STATE_CODE_ISREACH();
					break;
				case STATE_CODE_DISCARD:
					STATE_CODE_DISCARD();
					break;
				case STATE_CODE_RON:
					STATE_CODE_RON();
					break;
				case STATE_CODE_SUCHA:
					STATE_CODE_SUCHA();
					break;
				case STATE_CODE_NAKI:
					STATE_CODE_NAKI();
					break;
				case STATE_CODE_NEXTTURN:
					STATE_CODE_NEXTTURN();
					break playerLoop;
				case STATE_CODE_ENDOFKYOKU:
					break kyokuLoop;
				default:
					break;
				}
			}
		}
	}

	// 牌をツモる
	private void STATE_CODE_TSUMO() {
		initTransporterFlag();
		initDiscardFlag();
		kyoku.doTsumo();
		SingleServerSender server = sender.get(kyoku.getCurrentPlayer());
		server.sendTsumoHai(kyoku.getCurrentTsumoHai());
		kyoku.sortTehaiList();
		sendNeededInformation();
		stateCode = STATE_CODE_SEND;
	}

	// 九種九牌、ツモあがり、加槓、暗槓、リーチ、どの牌を切るかのリクエストはあらかじめ送信しておく
	public void STATE_CODE_SEND() {
		Player p = kyoku.getCurrentPlayer();
		SingleServerSender server = sender.get(p);
		if (kyoku.isKyusyukyuhai())
			server.requestKyusyukyuhai();
		if (kyoku.isTsumoAgari())
			server.requestTsumoAgari();
		if (kyoku.isKakanable())
			server.sendKakanableIndexList(kyoku.getKakanableHaiList());
		if (kyoku.isAnkanable())
			server.sendAnkanableIndexLists(kyoku.getAnkanableHaiList());
		if (kyoku.isReachable())
			server.sendReachableIndexList(kyoku.getReachableHaiList());
		server.sendDiscard(kyoku.getCurrentTsumoHai() != null);

		SingleServerReceiver rec = receiver.get(p);
		while (true) {
			if (isDiscardPreproccessedMessageReceived(rec)) {
				break;
			}
			sleep();
		}
		stateCode = STATE_CODE_KYUSYUKYUHAI;
	}

	public void STATE_CODE_KYUSYUKYUHAI() {
		if (kyoku.isKyusyukyuhai()) {
			Player p = kyoku.getCurrentPlayer();
			if (p.isMan()) {
				SingleServerReceiver rec = receiver.get(kyoku.getCurrentPlayer());

				boolean answer = false;
				if (rec.isMessageReceived(ServerMessageType.KYUSYUKYUHAI_RECEIVED)) {
					// 九種九牌の返事にbooleanはいらない？返信があればするものとみなせる
					rec.fetchMessage(ServerMessageType.KYUSYUKYUHAI_RECEIVED);
					answer = true;
				}

				if (answer) {
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

	public void STATE_CODE_TSUMOAGARI() {
		if (kyoku.isTsumoAgari()) {
			Player p = kyoku.getCurrentPlayer();
			if (p.isMan()) {
				SingleServerReceiver server = receiver.get(p);
				boolean tsumoagari = false;
				if (server.isMessageReceived(ServerMessageType.TSUMO_AGARI_RECEIVED)) {
					// 九種九牌同様、返事にbooleanはいらない？
					server.fetchMessage(ServerMessageType.TSUMO_AGARI_RECEIVED);
					tsumoagari = true;
				}

				if (tsumoagari) {
					kyoku.doTsumoAgari();
					kyoku.doSyukyoku();
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
					stateCode = STATE_CODE_ENDOFKYOKU;
					return;
				}
			}
		}
		stateCode = STATE_CODE_KAKAN;
	}

	public void STATE_CODE_KAKAN() {
		if (kyoku.isKakanable()) {
			Player p = kyoku.getCurrentPlayer();
			if (p.isMan()) {
				SingleServerReceiver rec = receiver.get(kyoku.getCurrentPlayer());
				ServerMessageType mType = ServerMessageType.KAKANABLE_INDEX_RECEIVED;
				if (rec.isMessageReceived(mType)) {
					int kakanindex = ((IntegerMessage) rec.fetchMessage(mType)).getData();
					Mentsu kakanMentu = kyoku.doKakan(kakanindex);
					sender.notifyNaki(p, kakanMentu);
					stateCode = STATE_CODE_CHANKANRON;
					return;
				}
			} else {
				AI ai = aiMap.get(kyoku.getCurrentTurn());
				int index = -1;
				if ((index = ai.kakan(kyoku.getKakanableHaiList())) != -1) {
					Mentsu kakanMentu = kyoku.doKakan(kyoku.getKakanableHaiList().get(index));
					sender.notifyNaki(p, kakanMentu);
					stateCode = STATE_CODE_CHANKANRON;
					return;
				}
				// AI
			}
		}
		stateCode = STATE_CODE_ANKAN;
		sendNeededInformation();
	}

	private void STATE_CODE_TYANKANRON() {
		List<Player> doRonPlayer = new ArrayList<Player>();
		doRon(doRonPlayer);
		if (doRonPlayer.size() == 0) {
			stateCode = STATE_CODE_RINSYANTSUMO;
			return;
		}
		stateCode = STATE_CODE_ENDOFKYOKU;
	}

	public void STATE_CODE_RINSYANTSUMO() {
		initTransporterFlag();
		onDiscardFlag();
		kyoku.doRinsyanTsumo();
		kyoku.sortTehaiList();

		SingleServerSender server = sender.get(kyoku.getCurrentPlayer());
		if (server != null) {
			server.sendTsumoHai(kyoku.getCurrentTsumoHai());
		}
		sendNeededInformation();
		stateCode = STATE_CODE_SEND;
	}

	public void STATE_CODE_ANKAN() {
		if (kyoku.isAnkanable()) {
			Player p = kyoku.getCurrentPlayer();
			if (p.isMan()) {
				SingleServerReceiver rec = receiver.get(p);
				ServerMessageType mType = ServerMessageType.ANKAN_INDEX_LIST_RECEIVED;

				if (rec.isMessageReceived(mType)) {
					List<Integer> ankanlist = ((IntegerListMessage) rec.fetchMessage(mType)).getData();
					Mentsu ankanMentu = kyoku.doAnkan(ankanlist);
					sender.notifyNaki(p, ankanMentu);
					stateCode = STATE_CODE_RINSYANTSUMO;
					return;
				}
			} else {
				// AI
				AI ai = aiMap.get(kyoku.getCurrentTurn());
				if (ai.ankan(kyoku.getAnkanableHaiList()) != -1) {
					Mentsu ankanMentu = kyoku.doAnkan(kyoku.getAnkanableHaiList().get(0));
					sender.notifyNaki(p, ankanMentu);
					stateCode = STATE_CODE_RINSYANTSUMO;
					return;
				}
			}
		}
		stateCode = STATE_CODE_ISREACH;
		sendNeededInformation();
	}

	private void STATE_CODE_ISREACH() {
		Player p = kyoku.getCurrentPlayer();
		SingleServerSender server = sender.get(p);
		if (p.isMan()) {
			if (kyoku.isReach(kyoku.getCurrentTurn())) {
				sleep();
				kyoku.discardTsumoHai();
				server.sendTsumoGiri();
				stateCode = STATE_CODE_RON;
				return;
			} else {
				if (kyoku.isReachable()) {
					SingleServerReceiver rec = receiver.get(p);
					ServerMessageType mType = ServerMessageType.REACH_INDEX_RECEIVED;
					if (rec.isMessageReceived(mType)) {
						int index = ((IntegerMessage) rec.fetchMessage(mType)).getData();
						kyoku.doReach();
						kyoku.discard(index);

						int reachSutehaiIndex = kyoku.getSutehaiList(kyoku.getCurrentTurn()).size() - 1;
						for (int i = 0; i < kyoku.getSutehaiList(kyoku.getCurrentTurn()).size(); i++) {
							if (kyoku.getSutehaiList(kyoku.getCurrentTurn()).get(i).isNaki()) {
								reachSutehaiIndex--;
							}
						}
						sender.notifyReach(kyoku.getCurrentTurn(), reachSutehaiIndex);
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
				kyoku.discardTsumoHai();
				stateCode = STATE_CODE_RON;
				return;
			}
			AI ai = aiMap.get(kyoku.getCurrentTurn());

			if (kyoku.isReachable() && ai.isReach()) {
				kyoku.doReach();
				int index = ai.discard();
				kyoku.discard(index);
				int reachSutehaiIndex = kyoku.getSutehaiList(kyoku.getCurrentTurn()).size() - 1;
				for (int i = 0; i < kyoku.getSutehaiList(kyoku.getCurrentTurn()).size(); i++) {
					if (kyoku.getSutehaiList(kyoku.getCurrentTurn()).get(i).isNaki()) {
						reachSutehaiIndex--;
					}
				}

				sender.notifyReach(kyoku.getCurrentTurn(), reachSutehaiIndex);
				stateCode = STATE_CODE_RON;
				return;
			} else {
				stateCode = STATE_CODE_DISCARD;
				return;
			}
			// AI
		}
		sendNeededInformation();
	}

	private void STATE_CODE_DISCARD() {
		kyoku.sortTehaiList();
		Player p = kyoku.getCurrentPlayer();
		if (p.isMan()) {
			SingleServerSender server = sender.get(p);
			SingleServerReceiver rec = receiver.get(p);
			
			if (discardFlag)
				server.sendDiscard(kyoku.getCurrentTsumoHai() != null);
			
			ServerMessageType mType = ServerMessageType.DISCARD_INDEX_RECEIVED;
			receiver.wait(p, mType, 0);
			int i = ((IntegerMessage) rec.fetchMessage(mType)).getData();

			if (0 <= i && i <= 12) {
				kyoku.discard(i);
				sender.notifyDiscard(kyoku.getCurrentPlayer(), kyoku.getCurrentSutehai(), false);
			} else if (i == 13) {
				kyoku.discard(13);
				sender.notifyDiscard(kyoku.getCurrentPlayer(), kyoku.getCurrentSutehai(), true);
			}
		} else {
			AI ai = aiMap.get(kyoku.getCurrentTurn());
			kyoku.discard(ai.discard());
			sender.notifyDiscard(kyoku.getCurrentPlayer(), kyoku.getCurrentSutehai(), false);
		}
		
		sendNeededInformation();
		stateCode = STATE_CODE_RON;
	}
	
	private void STATE_CODE_RON() {
		for (Kaze kaze : Kaze.values()) {
			Player p = playerMap.get(kaze);
			if (p.isMan()) {
				SingleServerSender server = sender.get(p);
				if (kyoku.isRonable(kaze))
					server.requestRon();
				
				if(kyoku.isMinkanable(kaze))
					server.sendMinkanableIndexList(kyoku.getMinkanableList(kaze));

				if(kyoku.isPonable(kaze))
					server.sendPonableIndexLists(kyoku.getPonableHaiList(kaze));
				
				if(kyoku.isChiable())
					server.sendChiableIndexLists(kyoku.getChiableHaiList());
			}
		}
		
		doRonAgari();
		sendNeededInformation();
	}

	private void STATE_CODE_SUCHA() {
		isSucha(kyoku);
	}
	
	private void STATE_CODE_NAKI() {
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
	}
	
	private void STATE_CODE_NEXTTURN() {
		if (kyoku.isRyukyoku()) {
			doTempai();// (仮にここにおいているだけ)
			kyoku.doRyukyoku();
			stateCode = STATE_CODE_ENDOFKYOKU;
		} else {
			kyoku.nextTurn();
			stateCode = STATE_CODE_TSUMO;
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

		for (Player p : sender.keySet()) {
			Kaze kaze = kyoku.getKazeOf(p);
			TehaiList tehai = kyoku.getTehaiList(kaze);
			SingleServerSender server = sender.get(p);

			server.sendField(tehai, nakihai, sutehai, kyoku.getCurrentTurn(), kyoku.getCurrentSutehai(), tehaiSize, kyoku.getYamahaiList()
					.size(), kyoku.getWanpaiList().size(), kyoku.getOpenDoraList());
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
							// System.out.println(playerMap.get(ronKaze)
							// + " : ロンだ！");
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
							// System.out.println(playerMap.get(ronKaze)
							// + " : ロンだ！");
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

	// 四家立直,四家連打,四開槓の判定
	private void isSucha(Kyoku k) {
		if (kyoku.isSuchaReach() || kyoku.isSufontsuRenta() || kyoku.isSukaikan()) {
			kyoku.doSuchaReach();
			stateCode = STATE_CODE_ENDOFKYOKU;
		} else {
			stateCode = STATE_CODE_NAKI;
		}
	}

	// 明槓すると返ってきたら明槓する。
	private void doMinkan() {
		for (Kaze kaze : Kaze.values()) {
			if (kyoku.isMinkanable(kaze)) {
				Player p = playerMap.get(kaze);
				if (p.isMan()) {
					Transporter tr = transporterMap.get(kaze);
					boolean isMinkanDo = waitMinkan(tr, kyoku.getMinkanableList(kaze));
					if (isMinkanDo) {
						Mentsu minkanMentu = kyoku.doMinkan(kaze);
						notifyNaki(p, minkanMentu);

						// System.out.println(kyoku.getPlayer(kaze) +
						// " : 明槓します");
						Console.wairEnter();

						stateCode = STATE_CODE_RINSYANTSUMO;
						return;
					}
				} else {
					AIType01 ai = new AIType01(p);
					if (ai.minkan()) {
						Mentsu minkanMentu = kyoku.doMinkan(kaze);
						notifyNaki(p, minkanMentu);

						// System.out.println(kyoku.getPlayer(kaze) +
						// " : 明槓します");
						Console.wairEnter();

						stateCode = STATE_CODE_RINSYANTSUMO;
					}
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

						// System.out.println(kyoku.getPlayer(kaze) + " : ポン！");
						Console.wairEnter();

						stateCode = STATE_CODE_DISCARD;
						return;
					}
				} else {
					// AI
					AI ai = aiMap.get(kaze);
					if (ai.pon(kyoku.getPonableHaiList(kaze)) != -1) {
						Mentsu ponMentu = kyoku.doPon(kaze, kyoku.getPonableHaiList(kaze).get(ai.pon(kyoku.getPonableHaiList(kaze))));
						notifyNaki(p, ponMentu);

						// System.out.println(kyoku.getPlayer(kaze) + " : ポン！");
						Console.wairEnter();

						stateCode = STATE_CODE_DISCARD;
						return;

					}

				}
			}
		}
	}

	// チーすると返ってきたとき,チーする
	private void doChi() {
		if (kyoku.isChiable()) {
			Player p = playerMap.get(kyoku.getCurrentTurn().simo());
			if (p.isMan()) {
				Transporter tr = transporterMap.get(kyoku.getCurrentTurn().simo());
				// System.out.println(tr.isChiReceived());

				List<Integer> chilist = waitChi(tr, kyoku.getChiableHaiList());
				// System.out.println("SystemThreadReceived: ");
				if (chilist != null) {
					Mentsu chiMentu = kyoku.doChi(chilist);
					notifyNaki(p, chiMentu);

					// System.out.println(kyoku.getPlayer(kyoku.getCurrentTurn()
					// .simo()) + " : チーだぜ");
					Console.wairEnter();

					stateCode = STATE_CODE_DISCARD;
					return;
				}

			} else {
				// AI
				AI ai = aiMap.get(kyoku.getCurrentTurn().simo());
				int index = -1;
				if ((index = ai.chi(kyoku.getChiableHaiList())) != -1) {
					Mentsu chiMentu = kyoku.doChi(kyoku.getChiableHaiList().get(index));
					notifyNaki(p, chiMentu);

					// System.out.println(kyoku.getPlayer(kyoku.getCurrentTurn()
					// .simo()) + " : チーだぜ");
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

	// discardFlagを初期化にする
	private void initDiscardFlag() {
		discardFlag = false;
	}

	// discardFlagをonにする
	private void onDiscardFlag() {
		discardFlag = true;
	}

	/*
	 * **********************A()******************************
	 */

	/**
	 * 暗槓、捨てる牌の決定、九種九牌、リーチ、ツモ上がりのどれかのメッセージを受け取っているか確認.
	 */
	private boolean isDiscardPreproccessedMessageReceived(SingleServerReceiver rec) {
		if (rec.isMessageReceived(ServerMessageType.ANKAN_INDEX_LIST_RECEIVED))
			return true;
		if (rec.isMessageReceived(ServerMessageType.KAKANABLE_INDEX_RECEIVED))
			return true;
		if (rec.isMessageReceived(ServerMessageType.DISCARD_INDEX_RECEIVED))
			return true;
		if (rec.isMessageReceived(ServerMessageType.KYUSYUKYUHAI_RECEIVED))
			return true;
		if (rec.isMessageReceived(ServerMessageType.REACH_INDEX_RECEIVED))
			return true;
		if (rec.isMessageReceived(ServerMessageType.TSUMO_AGARI_RECEIVED))
			return true;
		return false;
	}

	// チーできる牌のリストを送る。
	private void sendChiableIndexLists(Server tr, List<List<Integer>> list) {
		tr.sendChiableIndexLists(list);
	}

	// チーできると送った後,その回答が返ってくるまで待つ。
	private List<Integer> waitChi(Transporter tr, List<List<Integer>> sendlist) {
		while (!tr.isChiReceived() && !tr.isPonReceived() && !tr.isMinkanReceived()) {
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
		while (!tr.isPonReceived() && !tr.isChiReceived() && !tr.isMinkanReceived()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return tr.getPonIndexList();
	}

	// 明槓できると送ったあと,その回答を待つ
	private boolean waitMinkan(Transporter tr, List<Integer> sendIndexList) {
		while (!tr.isMinkanReceived() && !tr.isChiReceived() && !tr.isPonReceived()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return tr.isMinkanDo();
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

	// ロンしたプレイヤーのリストとそのあがった手牌を各プレイヤーに送る。
	private void notifyRon(Map<Player, List<Hai>> map, Server tr) {
		tr.notifyRon(map);
	}

	// 聴牌したプレイヤーの手牌を周りに見せる
	private void doTempai() {
		Map<Player, List<Hai>> tempaiMap = new HashMap<Player, List<Hai>>();
		for (Kaze kaze : Kaze.values()) {
			if (kyoku.isTenpai(kaze)) {
				tempaiMap.put(kyoku.getPlayer(kaze), kyoku.getTehaiList(kaze));
			}
		}
		sender.notifyTempai(tempaiMap);
	}

	private void sleep() {
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
