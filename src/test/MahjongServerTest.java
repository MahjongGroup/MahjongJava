package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import server.MahjongGame;
import server.Transporter;
import system.Player;
import system.Rule;
import client.Client;

/**
 * コンソール用の麻雀サーバー
 */
public class MahjongServerTest {

	// DEBUG
	public static void main(String[] args) {
		List<Player> plist = new ArrayList<Player>(4);
		plist.add(new Player(10, "imatom", true));
		plist.add(new Player(21, "moseshi", true));
		plist.add(new Player(34, "fillshion", true));
		plist.add(new Player(47, "morimitsu", true));

		Map<Player, Transporter> transMap = new HashMap<Player, Transporter>(4);
		transMap.put(plist.get(0), new Transporter());
				transMap.put(plist.get(1), new Transporter());
				transMap.put(plist.get(2), new Transporter());
				transMap.put(plist.get(3), new Transporter());

		// 結合用
		for (Transporter tr : transMap.values()) {
			Client client = new ConsoleClient(tr);
			tr.setClient(client);
			tr.setWait(false);
		}

		Rule rule = new Rule();

		MahjongGame game = new MahjongGame(plist, rule, transMap);
		game.run();
	}

	public MahjongServerTest() {
	}

}
