package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import server.MahjongGame;
import server.Server;
import server.Transporter;
import system.Player;
import system.Rule;

/**
 * コンソール用の麻雀サーバー
 */
public class MahjongServerTest {
	public static Player p = new Player(10, "imatom", true);
	public static Server server;
	public static Map<Player, Transporter> transMap = new HashMap<Player, Transporter>(4);

	// DEBUG
	public static void main(String[] args) {
		List<Player> plist = new ArrayList<Player>(4);
		plist.add(p);
		plist.add(new Player(21, "moseshi", false));
		plist.add(new Player(34, "fillshion", false));
		plist.add(new Player(47, "morimitsu", false));

		
		Transporter trs = new Transporter();
		transMap.put(plist.get(0), trs);
//		transMap.put(plist.get(1), new Transporter());
//		transMap.put(plist.get(2), new Transporter());
//		transMap.put(plist.get(3), new Transporter());

		// 結合用
		for (Transporter tr : transMap.values()) {
			ConsoleClient2 client = new ConsoleClient2(p, tr);
			tr.setClient(client);
//			tr.setWait(false);
			Thread th = new Thread(client);
			th.start();
		}

		Rule rule = new Rule();
		MahjongGame game = new MahjongGame(plist, rule, transMap);
		game.run();
	}

}
