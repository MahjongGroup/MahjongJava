package test.server.game;

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
import system.result.TotyuRyukyokuType;
import test.server.ServerMessage;
import test.server.ServerMessage.BooleanMessage;
import test.server.ServerMessage.IntegerListMessage;
import test.server.ServerMessage.IntegerMessage;
import test.server.ServerMessageType;
import test.server.ServerReceiver;
import test.server.ServerSender;
import test.server.SingleServerReceiver;
import test.server.SingleServerSender;
import util.MyLogger;
import ai.AI;
import ai.AIType01;
import ai.AIType_Debug;

/**
 * 1回の局を走らせるクラス.
 */
public class KyokuRunner {
	private static MyLogger logger = MyLogger.getLogger();

	public static final int STATE_CODE_TSUMO = 0;
	public static final int STATE_CODE_KYUSYUKYUHAI = 1;
	public static final int STATE_CODE_TSUMOAGARI = 2;
	public static final int STATE_CODE_KAKAN = 3;
	public static final int STATE_CODE_CHANKANRON = 4;
	public static final int STATE_CODE_RINSYANTSUMO = 5;
	public static final int STATE_CODE_ANKAN = 6;
	public static final int STATE_CODE_ISREACH = 7;
	public static final int STATE_CODE_DISCARD = 8;
	public static final int STATE_CODE_RYUKYOKU = 9;
	public static final int STATE_CODE_NAKI = 10;
	public static final int STATE_CODE_RON = 11;
	public static final int STATE_CODE_SEND = 12;
	public static final int STATE_CODE_NEXTTURN = 13;
	public static final int STATE_CODE_ENDOFKYOKU = 14;

	private final Kyoku kyoku;

	private final Map<Kaze, Player> playerMap;
	private final Map<Kaze, AI> aiMap;

	private final ServerReceiver receiver;
	private final ServerSender sender;

	private int stateCode;

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
					logger.debug("STATE_CODE_TSUMO");
					STATE_CODE_TSUMO();
					break;
				case STATE_CODE_SEND:
					logger.debug("STATE_CODE_SEND");
					STATE_CODE_SEND();
					break;
				case STATE_CODE_KYUSYUKYUHAI:
					logger.debug("STATE_CODE_KYUSYUKYUHAI");
					STATE_CODE_KYUSYUKYUHAI();
					break;
				case STATE_CODE_TSUMOAGARI:
					logger.debug("STATE_CODE_TSUMOAGARI");
					STATE_CODE_TSUMOAGARI();
					break;
				case STATE_CODE_KAKAN:
					logger.debug("STATE_CODE_KAKAN");
					STATE_CODE_KAKAN();
					break;
				case STATE_CODE_CHANKANRON:
					logger.debug("STATE_CODE_CHANKAN");
					STATE_CODE_TYANKANRON();
					break;
				case STATE_CODE_RINSYANTSUMO:
					logger.debug("STATE_CODE_RINSYANTSUMO");
					STATE_CODE_RINSYANTSUMO();
					break;
				case STATE_CODE_ANKAN:
					logger.debug("STATE_CODE_ANKAN");
					STATE_CODE_ANKAN();
					break;
				case STATE_CODE_ISREACH:
					logger.debug("STATE_CODE_ISREACH");
					STATE_CODE_ISREACH();
					break;
				case STATE_CODE_DISCARD:
					logger.debug("STATE_CODE_DISCARD");
					STATE_CODE_DISCARD();
					break;
				case STATE_CODE_RON:
					logger.debug("STATE_CODE_RON");
					STATE_CODE_RON();
					break;
				case STATE_CODE_RYUKYOKU:
					logger.debug("STATE_CODE_RYUKYOKU");
					STATE_CODE_RYUKYOKU();
					break;
				case STATE_CODE_NAKI:
					logger.debug("STATE_CODE_NAKI");
					STATE_CODE_NAKI();
					break;
				case STATE_CODE_NEXTTURN:
					logger.debug("STATE_CODE_NEXTTURN");
					STATE_CODE_NEXTTURN();
					break playerLoop;
				case STATE_CODE_ENDOFKYOKU:
					logger.debug("STATE_CODE_ENDOFKYOKU");
					break kyokuLoop;
				default:
					break;
				}
			}
		}
	}

	// 牌をツモる
	private void STATE_CODE_TSUMO() {
		clearMessage();
		kyoku.doTsumo();
		SingleServerSender server = sender.get(kyoku.getCurrentPlayer());
		server.sendTsumoHai(kyoku.getCurrentTsumoHai());
		kyoku.sortTehaiList();
		sendNeededInformation();
		stateCode = STATE_CODE_SEND;
	}

	// 九種九牌、ツモあがり、加槓、暗槓、リーチ、どの牌を切るかのリクエストはあらかじめ送信しておく
	private void STATE_CODE_SEND() {
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

	private void STATE_CODE_KYUSYUKYUHAI() {
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
					kyoku.doTotyuRyukyoku(TotyuRyukyokuType.KYUSYUKYUHAI);
					stateCode = STATE_CODE_ENDOFKYOKU;
					return;
				}
				// while loop を抜ける
			} else {
				AI ai = aiMap.get(kyoku.getCurrentTurn());
				if (ai.isKyusyukyuhai()) {
					kyoku.doTotyuRyukyoku(TotyuRyukyokuType.KYUSYUKYUHAI);
					stateCode = STATE_CODE_ENDOFKYOKU;
					return;
				}
				// AI
			}
		}
		stateCode = STATE_CODE_TSUMOAGARI;
	}

	private void STATE_CODE_TSUMOAGARI() {
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

	private void STATE_CODE_KAKAN() {
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
		for (Kaze kaze : Kaze.values()) {
			Player p = playerMap.get(kaze);
			if (p.isMan()) {
				if (kyoku.isRonable(kaze)) {
					SingleServerSender server = sender.get(p);
					server.requestRon();
				}
			}
		}

		if (!doRon()) {
			stateCode = STATE_CODE_RINSYANTSUMO;
			return;
		}
		stateCode = STATE_CODE_ENDOFKYOKU;
	}

	private void STATE_CODE_RINSYANTSUMO() {
		clearMessage();
		kyoku.doRinsyanTsumo();
		kyoku.sortTehaiList();

		SingleServerSender server = sender.get(kyoku.getCurrentPlayer());
		if (server != null) {
			server.sendTsumoHai(kyoku.getCurrentTsumoHai());
		}
		sendNeededInformation();
		stateCode = STATE_CODE_SEND;
	}

	private void STATE_CODE_ANKAN() {
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
						return;
					}
				}
			}
		} else {
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
			}
		}
		stateCode = STATE_CODE_DISCARD;
		sendNeededInformation();
	}

	private void STATE_CODE_DISCARD() {
		kyoku.sortTehaiList();
		Player p = kyoku.getCurrentPlayer();
		if (p.isMan()) {
			SingleServerSender server = sender.get(p);
			SingleServerReceiver rec = receiver.get(p);

			if (kyoku.getCurrentTsumoHai() == null) {
				server.sendDiscard(false);
			}

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

				if (kyoku.isMinkanable(kaze))
					server.sendMinkanableIndexList(kyoku.getMinkanableList(kaze));

				if (kyoku.isPonable(kaze))
					server.sendPonableIndexLists(kyoku.getPonableHaiList(kaze));

				if (kyoku.isChiable())
					server.sendChiableIndexLists(kyoku.getChiableHaiList());
			}
		}

		if (doRon()) {
			stateCode = STATE_CODE_ENDOFKYOKU;
		} else {
			stateCode = STATE_CODE_RYUKYOKU;
		}
		sendNeededInformation();
	}

	private void STATE_CODE_RYUKYOKU() {
		TotyuRyukyokuType type = null;
		if (kyoku.isSuchaReach())
			type = TotyuRyukyokuType.SUCHAREACH;
		if (kyoku.isSufontsuRenta())
			type = TotyuRyukyokuType.SUFONTSURENTA;
		if (kyoku.isSukaikan())
			type = TotyuRyukyokuType.SUKAIKAN;

		if (type != null) {
			kyoku.doTotyuRyukyoku(type);
			stateCode = STATE_CODE_ENDOFKYOKU;
			return;
		}
		
		stateCode = STATE_CODE_NAKI;
	}

	private void STATE_CODE_NAKI() {
		if (!doMinkan()) {
			sendNeededInformation();
			if (!doPon()) {
				sendNeededInformation();
				doChi();
			}
		}
		sendNeededInformation();
		if (stateCode == STATE_CODE_NAKI) {
			stateCode = STATE_CODE_NEXTTURN;
		}
		clearMessage();
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

	private boolean doRon() {
		List<Player> ronPlayer = new ArrayList<Player>(4);
		Map<Player, List<Hai>> ronMap = new HashMap<Player, List<Hai>>();

		for (Kaze kaze : Kaze.values()) {
			if (kyoku.isRonable(kaze)) {
				Player p = playerMap.get(kaze);
				if (p.isMan()) {
					SingleServerReceiver rec = receiver.get(p);
					ServerMessageType mType = ServerMessageType.RON_RECEIVED;

					boolean answer = false;
					while (true) {
						if (rec.isMessageReceived(mType)) {
							ServerMessage m = rec.fetchMessage(mType);
							answer = ((BooleanMessage) m).getData();
							break;
						}
					}

					// kazeがロンする場合
					if (answer) {
						kyoku.doRon(kaze);
						ronPlayer.add(p);

						for (int i = 0; i < kyoku.getTehaiList(kaze).size(); i++) {
							ronMap.put(p, kyoku.getTehaiList(kaze));
						}
					} else {
						kyoku.onRonRejected(kaze);
					}
				} else {
					AI ai = aiMap.get(kaze);
					if (ai.isRon()) {
						kyoku.doRon(kaze);
						for (int i = 0; i < kyoku.getTehaiList(kaze).size(); i++) {
							ronMap.put(p, kyoku.getTehaiList(kaze));
						}
						ronPlayer.add(p);
					} else {
						kyoku.onRonRejected(kaze);
					}
				}
			}
		}

		// ロンしたプレイヤーがいた場合
		if (ronPlayer.size() > 0) {
			sender.notifyRon(ronMap);

			if (kyoku.isSanchaho()) {
				kyoku.doTotyuRyukyoku(TotyuRyukyokuType.SANCHAHO);
			}
			kyoku.doSyukyoku();
			return true;
		}

		return false;
	}

	/**
	 * @return　大明槓をした場合true
	 */
	private boolean doMinkan() {
		for (Kaze kaze : Kaze.values()) {
			if (kyoku.isMinkanable(kaze)) {
				Player p = playerMap.get(kaze);
				if (p.isMan()) {
					ServerMessageType mType = ServerMessageType.MINKAN_RECEIVED;
					receiver.wait(p, mType, 0);
					SingleServerReceiver rec = receiver.get(p);

					boolean answer = ((BooleanMessage) rec.fetchMessage(mType)).getData();
					if (answer) {
						Mentsu minkanMentu = kyoku.doMinkan(kaze);
						sender.notifyNaki(p, minkanMentu);

						stateCode = STATE_CODE_RINSYANTSUMO;
						return true;
					}
				} else {
					AIType01 ai = new AIType01(p);
					if (ai.minkan()) {
						Mentsu minkanMentu = kyoku.doMinkan(kaze);
						sender.notifyNaki(p, minkanMentu);

						stateCode = STATE_CODE_RINSYANTSUMO;
						return true;
					}
				}
				break;
			}
		}
		return false;
	}

	/**
	 * @return　ポンをした場合true
	 */
	private boolean doPon() {
		for (Kaze kaze : Kaze.values()) {
			if (kyoku.isPonable(kaze)) {
				Player p = playerMap.get(kaze);
				if (p.isMan()) {
					ServerMessageType mType = ServerMessageType.PON_INDEX_LIST_RECEIVED;
					receiver.wait(p, mType, 0);
					SingleServerReceiver rec = receiver.get(p);

					List<Integer> ponlist = ((IntegerListMessage) rec.fetchMessage(mType)).getData();
					if (ponlist != null) {
						Mentsu ponMentu = kyoku.doPon(kaze, ponlist);
						sender.notifyNaki(p, ponMentu);

						stateCode = STATE_CODE_DISCARD;
						return true;
					}
				} else {
					AI ai = aiMap.get(kaze);
					if (ai.pon(kyoku.getPonableHaiList(kaze)) != -1) {
						Mentsu ponMentu = kyoku.doPon(kaze, kyoku.getPonableHaiList(kaze).get(ai.pon(kyoku.getPonableHaiList(kaze))));
						sender.notifyNaki(p, ponMentu);

						stateCode = STATE_CODE_DISCARD;
						return true;
					}

				}
				break;
			}
		}
		return false;
	}

	/**
	 * @return　ポンをした場合true
	 */
	private boolean doChi() {
		if (kyoku.isChiable()) {
			Player p = playerMap.get(kyoku.getCurrentTurn().simo());
			if (p.isMan()) {
				ServerMessageType mType = ServerMessageType.CHIINDEX_LIST_RECEIVED;
				receiver.wait(p, mType, 0);
				SingleServerReceiver rec = receiver.get(p);

				List<Integer> chilist = ((IntegerListMessage) rec.fetchMessage(mType)).getData();
				if (chilist != null) {
					Mentsu chiMentu = kyoku.doChi(chilist);
					sender.notifyNaki(p, chiMentu);

					stateCode = STATE_CODE_DISCARD;
					return true;
				}
			} else {
				// AI
				AI ai = aiMap.get(kyoku.getCurrentTurn().simo());
				int index = -1;
				if ((index = ai.chi(kyoku.getChiableHaiList())) != -1) {
					Mentsu chiMentu = kyoku.doChi(kyoku.getChiableHaiList().get(index));
					sender.notifyNaki(p, chiMentu);

					stateCode = STATE_CODE_DISCARD;
					return true;
				}
			}
		}
		return false;
	}

	// 全てのレシーバが受け取っていた特定のメッセージをクリアする.
	private void clearMessage() {
		List<ServerMessageType> mTypeList = new ArrayList<ServerMessageType>();
		mTypeList.add(ServerMessageType.ANKAN_INDEX_LIST_RECEIVED);
		mTypeList.add(ServerMessageType.CHIINDEX_LIST_RECEIVED);
		mTypeList.add(ServerMessageType.DISCARD_INDEX_RECEIVED);
		mTypeList.add(ServerMessageType.KAKANABLE_INDEX_RECEIVED);
		mTypeList.add(ServerMessageType.KYUSYUKYUHAI_RECEIVED);
		mTypeList.add(ServerMessageType.MINKAN_RECEIVED);
		mTypeList.add(ServerMessageType.PON_INDEX_LIST_RECEIVED);
		mTypeList.add(ServerMessageType.REACH_INDEX_RECEIVED);
		mTypeList.add(ServerMessageType.RON_RECEIVED);
		mTypeList.add(ServerMessageType.TSUMO_AGARI_RECEIVED);

		for (Player p : playerMap.values()) {
			SingleServerReceiver rec = receiver.get(p);
			for (ServerMessageType mType : mTypeList) {
				rec.fetchMessage(mType);
			}
		}
	}

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

}
