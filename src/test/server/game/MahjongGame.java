package test.server.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import system.Kyoku;
import system.Mahjong;
import system.Player;
import system.Rule;
import system.hai.Hai;
import system.hai.Kaze;
import system.result.KyokuResult;
import test.server.ServerMessageType;
import test.server.ServerReceiver;
import test.server.ServerSender;
import test.server.SingleServerReceiver;
import test.server.SingleServerSender;
import util.MyLogger;
import ai.AI;

/**
 * 1半荘(東風)を実行するクラス.
 */
public class MahjongGame {
	private static MyLogger logger = MyLogger.getLogger();

	private final Mahjong mahjong;
	private final List<Player> playerList;
	private final ServerSender sender;
	private final ServerReceiver receiver;
	private final Map<Player, AI> aiMap;

	public MahjongGame(List<Player> playerList, Rule rule, ServerSender sender, ServerReceiver receiver, Map<Player, AI> aiMap) {
		this.playerList = new ArrayList<Player>(playerList);
		this.mahjong = new Mahjong(this.playerList, rule);
		this.sender = sender;
		this.receiver = receiver;
		this.aiMap = aiMap;
	}

	/**
	 * 麻雀ゲームを実行する.
	 */
	public void run() {
		mahjong.init();

		Map<Kaze, Player> pMap = new HashMap<Kaze, Player>();
		for (int i = 0; i < playerList.size(); i++) {
			Player p = playerList.get(i);
			pMap.put(Kaze.valueOf(i), p);
		}

		for (Player p : sender.keySet()) {
			SingleServerSender server = sender.get(p);
			server.sendGameStart(pMap, Kaze.valueOf(playerList.indexOf(p)), mahjong.getScores()[0]);
		}

		while (!mahjong.isEnd()) {
			Kyoku kyoku = mahjong.startKyoku();
			for (AI ai : aiMap.values()) {
				ai.update(kyoku);
			}

			mahjong.disp();

			kyoku.init();

			logger.debug("notifyStartKyoku");

			Map<Kaze, List<Hai>> haipai = new HashMap<Kaze, List<Hai>>();

			for (Kaze kaze : Kaze.values()) {
				List<Hai> tehai = kyoku.getTehaiList(kaze);
				Collections.sort(tehai, Hai.HaiComparator.ASCENDING_ORDER);
				haipai.put(kaze, tehai);
			}

			for (Kaze kaze : Kaze.values()) {
				SingleServerSender ssender = sender.get(kyoku.getPlayer(kaze));
				ssender.notifyStartKyoku(kyoku.getBakaze(), kaze,  mahjong.getKyokusu(), mahjong.getHonba(), mahjong.getTsumibo(), haipai.get(kaze), kyoku.getOpenDoraList().get(0));
			}

			logger.debug("kyoku runner start");
			KyokuRunner runner = new KyokuRunner(kyoku, sender, receiver);
			runner.run();

			int oldScores[] = mahjong.getScores();

			logger.debug("kyoku end");
			mahjong.endKyoku();
			mahjong.disp2();

			Map<Kaze, Integer> scoreDiff = new HashMap<Kaze, Integer>();

			int newScores[] = mahjong.getScores();
			for (int i = 0; i < oldScores.length; i++) {
				Kaze kaze = Kaze.valueOf(i);
				scoreDiff.put(kaze, newScores[i] - oldScores[i]);
			}
			KyokuResult kr = kyoku.createKyokuResult();

			logger.debug("notifyKyokuResult");
			sender.notifyKyokuResult(kr, scoreDiff, kyoku.getUraDoraList());

			logger.debug("wait for next kyoku requested");

			receiver.wait(ServerMessageType.NEXT_KYOKU_REQUESTED, 1000);
			for (SingleServerReceiver rec : receiver.values()) {
				rec.fetchMessage(ServerMessageType.NEXT_KYOKU_REQUESTED);
			}
		}

		logger.debug("notifyGameResult");
		
		Map<Kaze, Integer> scoreMap = new HashMap<Kaze, Integer>();
		for (int i = 0; i < playerList.size(); i++) {
			Kaze kaze = Kaze.valueOf(i);
			scoreMap.put(kaze, mahjong.getScore(i));
		}
		sender.notifyGameResult(scoreMap);
	}

}
