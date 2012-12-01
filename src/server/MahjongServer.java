package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import system.Player;
import system.Rule;
import test.ConsoleClient;
import client.Client;

/**
 * 麻雀サーバーを表すクラス．
 * とりあえずテスト用にメイン関数のみ実装．
 */
public class MahjongServer {

	// DEBUG
	public static void main(String[] args) {
		List<Player> plist = new ArrayList<Player>(4);
		plist.add(new Player(10, "morimitsu", false));
		plist.add(new Player(21, "moseshi", false));
		plist.add(new Player(34, "fillshion", false));
		plist.add(new Player(47, "imatom", true));

		Map<Player, Transporter> transMap = new HashMap<Player, Transporter>(4);
//		transMap.put(plist.get(0), new Transporter());
//		transMap.put(plist.get(1), new Transporter());
//		transMap.put(plist.get(2), new Transporter());
		transMap.put(plist.get(3), new Transporter());
		
		// 結合用
		for (Transporter tr : transMap.values()) {
			Client client = new ConsoleClient(tr);
			tr.setClient(client);
		}
		
		Rule rule = new Rule();
		
		MahjongGame game = new MahjongGame(plist, rule, transMap);
		game.run();
	}

	public MahjongServer() {
	}

}
