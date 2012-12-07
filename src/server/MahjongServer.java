package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import system.Player;
import system.Rule;
import test.ConsoleClient;
import client.Client;
import client.ClientOperator;
import client.MajanFrame;

/**
 * 麻雀サーバーを表すクラス.
 * とりあえずテスト用にメイン関数のみ実装.
 */
public class MahjongServer {

	// DEBUG
	public static void main(String[] args) {
		List<Player> plist = new ArrayList<Player>(4);
		plist.add(new Player(10, "imatom", true));
		plist.add(new Player(21, "moseshi", false));
		plist.add(new Player(34, "fillshion", false));
		plist.add(new Player(47, "morimitsu", false));

		Map<Player, Transporter> transMap = new HashMap<Player, Transporter>(4);
		transMap.put(plist.get(0), new Transporter());
//		transMap.put(plist.get(1), new Transporter());
//		transMap.put(plist.get(2), new Transporter());
//		transMap.put(plist.get(3), new Transporter());
		
		// 結合用
		int count = 0;
		for (Transporter tr : transMap.values()) {
			if(count == 0){
				(new Runnable(){
					private Server tr;
					
					public Runnable setTransporter(Server tr){
						this.tr = tr;
						return this;
					}
					
					@Override
					public void run() {
						MajanFrame frame = new MajanFrame();
						frame.setServer(tr);
					}
				}).setTransporter(tr).run();
			}else{
				Client client = new ConsoleClient(tr);
				tr.setClient(client);
				tr.setWait(false);
			}
			count++;
		}
		boolean isWait = true;
		System.out.println("before wait");
		while(isWait){
			isWait = false;
			for(Transporter tr:transMap.values()){
				if(tr.isWait()){
					isWait = true;
					break;
				}
			}
		}

		try{
			Thread.sleep(3000);
		}catch(InterruptedException e){}

		for(Transporter tr:transMap.values()){
			tr.sendGameStart(plist);
		}
		
		Rule rule = new Rule();
		
		MahjongGame game = new MahjongGame(plist, rule, transMap);
		game.run();
	}

	public MahjongServer() {
	}

}
