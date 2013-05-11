package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import system.test.Player;
import system.test.Rule;
import test.ConsoleClient;
import client.Client;

/**
 * 麻雀サーバーを表すクラス. とりあえずテスト用にメイン関数のみ実装.
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
		transMap.put(plist.get(0), new DummyTransporter());
		// transMap.put(plist.get(1), new Transporter());
		// transMap.put(plist.get(2), new Transporter());
		// transMap.put(plist.get(3), new Transporter());
		try {
			ServerSocket serverS = new ServerSocket(5555);
			System.out.println("server launched");
			// 結合用
			int count = 0;
			Socket s = null;
			for (Transporter tr : transMap.values()) {
				if (count == 0) {
					DummyTransporter dtr = (DummyTransporter) tr;
					while (s == null) {
						try {
							s = serverS.accept();
							ObjectOutputStream oos = new ObjectOutputStream(
									s.getOutputStream());
							oos.flush();
							ObjectInputStream ois = new ObjectInputStream(
									s.getInputStream());
							dtr.connectClient(ois, oos);
						} catch (Exception e) {
							System.out.println("ServerSideError:"
									+ e.getMessage());
						}
					}
					// (new Runnable(){
					// private Server tr;
					//
					// public Runnable setTransporter(Server tr){
					// this.tr = tr;
					// return this;
					// }
					//
					// @Override
					// public void run() {
					//
					// MahjongFrame frame = new MahjongFrame();
					// frame.setServer(tr);
					// }
					// }).setTransporter(tr).run();
				} else {
					Client client = new ConsoleClient(tr);
					tr.setClient(client);
					tr.setWait(false);
				}
				count++;
			}
			boolean isWait = true;
			System.out.println(s);
			while (isWait) {
				isWait = false;
				for (Transporter tr : transMap.values()) {
					if (tr.isWait()) {
						isWait = true;
						break;
					}
				}
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}

			Rule rule = new Rule();
			MahjongGame game = new MahjongGame(plist, rule, transMap);
			game.run();
			serverS.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return;
		}

	}


	public MahjongServer() {
	}
}
