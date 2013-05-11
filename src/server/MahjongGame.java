package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import system.agari.AgariResult;
import system.result.KyokuResult;
import system.test.Kyoku;
import system.test.Mahjong;
import system.test.Player;
import system.test.Rule;
import test.Console;

/**
 * 1半荘(東風)を実行するクラス.
 */
public class MahjongGame {
	private final Mahjong mahjong;
	private final List<Player> playerList;
	private final Map<Player, Transporter> transMap;

	public MahjongGame(List<Player> playerList, Rule rule, Map<Player, Transporter> tmap) {
		this.playerList = new ArrayList<Player>(playerList);
		this.mahjong = new Mahjong(this.playerList, rule);
		this.transMap = new HashMap<Player, Transporter>(tmap);
	}
	
	/**
	 * 麻雀ゲームを実行する.
	 */
	public void run() {
		mahjong.init();
		
		for (Player p : transMap.keySet()) {
			Server server = transMap.get(p);
			server.sendGameStart(playerList, playerList.indexOf(p), mahjong.getScores());
		}

		while (!mahjong.isEnd()) {
			
			Kyoku kyoku = mahjong.startKyoku();
			mahjong.disp();
			Console.wairEnter();
			
			for(Server server:transMap.values()){
				server.notifyStartKyoku(kyoku.getBakaze(), mahjong.getKyokusu(),mahjong.getHonba(),mahjong.getTsumibo());
			}
			
			KyokuRunner runner = new KyokuRunner(kyoku, transMap);
			runner.run();

			int oldScores[] = mahjong.getScores();
			
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
			
			
			for(Server server:transMap.values()){
				server.notifyKyokuResult(kr,newScores,oldScores,soten,kyoku.getUraDoraList());
			}
			waitKyokuResult();
			
			if(kr.isRonAgari()) {
				for (Player player : playerList) {
					if(kr.isAgari(player)) {
//						System.out.println(kr.getAgariResult(player));
					}
				}
			}else if(kr.isTsumoAgari()) {
//				System.out.println(kr.getAgariResult(kr.getTsumoAgariPlayer()));
			}
		}
		
		for(Server server:transMap.values()){
			server.notifyGameResult(mahjong.getScores());
		}
		
	}
	
	private void waitKyokuResult(){
		for (Transporter tr : transMap.values()) {
			while(!tr.isEndResultPage()){
				try{
					Thread.sleep(100);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
}
