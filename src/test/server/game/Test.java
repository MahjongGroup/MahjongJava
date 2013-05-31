package test.server.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import system.Player;
import system.Rule;
import test.client.AIClient;
import test.client.ClientListener;
import test.client.ClientSenderImpl;
import test.client.ConsoleClient;
import test.client.MahjongClient;
import test.server.ServerReceiver;
import test.server.ServerSender;
import test.server.SingleServerReceiver;
import test.server.SingleServerSender;
import test.server.offline.ServerReceiverImpl;
import test.server.offline.ServerSenderImpl;
import test.server.offline.SingleServerReceiverImpl;
import test.server.offline.SingleServerSenderImpl;
import ai.AI;
import ai.AIType01;

public class Test {
	public static void main(String[] args) {
		Player p1 = new Player(100, "imatom", true);
		Player p2 = new Player(101, "moseshi", false);
		Player p3 = new Player(102, "morimitsu", false);
		Player p4 = new Player(103, "fillsion", false);

		AI ai2 = new AIType01(p2);
		AI ai3 = new AIType01(p3);
		AI ai4 = new AIType01(p4);
		
		Map<Player, AI> aiMap = new HashMap<Player, AI>();
		aiMap.put(p2, ai2);
		aiMap.put(p3, ai3);
		aiMap.put(p4, ai4);
		
		SingleServerReceiver rec1 = new SingleServerReceiverImpl();
		SingleServerReceiver rec2 = new SingleServerReceiverImpl();
		SingleServerReceiver rec3 = new SingleServerReceiverImpl();
		SingleServerReceiver rec4 = new SingleServerReceiverImpl();

		Map<Player, SingleServerReceiver> map = new HashMap<Player, SingleServerReceiver>();
		map.put(p1, rec1);
		map.put(p2, rec2);
		map.put(p3, rec3);
		map.put(p4, rec4);

		ServerReceiver receivers = new ServerReceiverImpl(map);
		
		SingleServerSenderImpl send1 = new SingleServerSenderImpl();
		SingleServerSenderImpl send2 = new SingleServerSenderImpl();
		SingleServerSenderImpl send3 = new SingleServerSenderImpl();
		SingleServerSenderImpl send4 = new SingleServerSenderImpl();
		
		Map<Player, SingleServerSender> map2 = new HashMap<Player, SingleServerSender>();
		map2.put(p1, send1);
		map2.put(p2, send2);
		map2.put(p3, send3);
		map2.put(p4, send4);
		
		ServerSender sender = new ServerSenderImpl(map2);
		
		ClientSenderImpl csend1 = new ClientSenderImpl();
		ClientSenderImpl csend2 = new ClientSenderImpl();
		ClientSenderImpl csend3 = new ClientSenderImpl();
		ClientSenderImpl csend4 = new ClientSenderImpl();
		
		csend1.setServerReceiver(rec1);
		csend2.setServerReceiver(rec2);
		csend3.setServerReceiver(rec3);
		csend4.setServerReceiver(rec4);
		
		ClientListener listener1 = new ConsoleClient(csend1);
		ClientListener listener2 = new MahjongClient(new AIClient(csend2, ai2));
		ClientListener listener3 = new MahjongClient(new AIClient(csend3, ai3));
		ClientListener listener4 = new MahjongClient(new AIClient(csend4, ai4));
		
		send1.setListener(listener1);
		send2.setListener(listener2);
		send3.setListener(listener3);
		send4.setListener(listener4);
		
		Rule rule = new Rule();
		
		List<Player> playerList = new ArrayList<Player>(4);
		playerList.add(p1);
		playerList.add(p2);
		playerList.add(p3);
		playerList.add(p4);
		MahjongGame game = new MahjongGame(playerList, rule, sender, receivers, aiMap);
		game.run();
		
	}
}
