package test.server.game;

import java.util.ArrayList;
import java.util.List;

import system.Kyoku;
import system.Mahjong;
import system.Player;
import system.Rule;
import system.agari.AgariResult;
import system.result.KyokuResult;
import test.server.ServerMessageType;
import test.server.ServerReceiver;
import test.server.ServerSender;
import test.server.SingleServerSender;
import util.MyLogger;

/**
 * 1半荘(東風)を実行するクラス.
 */
public class MahjongGame {
	private static MyLogger logger = MyLogger.getLogger();
	
	private final Mahjong mahjong;
	private final List<Player> playerList;
	private final ServerSender sender;
	private final ServerReceiver receiver;

	public MahjongGame(List<Player> playerList, Rule rule, ServerSender sender, ServerReceiver receiver) {
		this.playerList = new ArrayList<Player>(playerList);
		this.mahjong = new Mahjong(this.playerList, rule);
		this.sender = sender;
		this.receiver = receiver;
	}
	
	/**
	 * 麻雀ゲームを実行する.
	 */
	public void run() {
		mahjong.init();
		
		for (Player p : sender.keySet()) {
			SingleServerSender server = sender.get(p);
			server.sendGameStart(playerList, playerList.indexOf(p), mahjong.getScores());
		}

		while (!mahjong.isEnd()) {
			Kyoku kyoku = mahjong.startKyoku();
			mahjong.disp();
			
			logger.debug("notifyStartKyoku");
			sender.notifyStartKyoku(kyoku.getBakaze(), mahjong.getKyokusu(),mahjong.getHonba(),mahjong.getTsumibo());
			

			logger.debug("kyoku runner start");
			KyokuRunner runner = new KyokuRunner(kyoku, sender, receiver);
			runner.run();

			int oldScores[] = mahjong.getScores();
			
			logger.debug("kyoku end");
			mahjong.endKyoku();
			mahjong.disp2();
			
			int newScores[] = mahjong.getScores();
			KyokuResult kr = kyoku.createKyokuResult();
			List<Integer> soten = new ArrayList<Integer>();
			for(Player p :playerList){
				int karisoten = 0;
				if(kr.isAgari(p)){
					AgariResult ar = kr.getAgariResult(p);
					if(playerList.indexOf(p)==0){
						karisoten = ar.getParentScore();
					}else{
						karisoten = ar.getChildScore();
					}
				}
				soten.add(karisoten);
			}
			
			logger.debug("notifyKyokuResult");
			sender.notifyKyokuResult(kr,newScores,oldScores,soten,kyoku.getUraDoraList());

			logger.debug("wait for next kyoku requested");
			receiver.wait(ServerMessageType.NEXT_KYOKU_REQUESTED, 0);
		}
		
		logger.debug("notifyGameResult");
		sender.notifyGameResult(mahjong.getScores());
	}
	
}
