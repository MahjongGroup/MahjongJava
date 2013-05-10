package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import server.Server;
import system.Player;
import system.hai.Hai;
import system.hai.HurohaiList;
import system.hai.Kaze;
import system.hai.Mentsu;
import system.result.KyokuResult;
import client.Client;

public class ConsoleClient2 implements Client, Runnable {
	static final MyLogger logger = MyLogger.getLogger();
	BufferedReader reader;
	Server server;
	Player p;
	int playerindex;
	List<Player> playerlist;
	

	Kaze bakaze;
	List<Hai> tehai;
	Hai tsumohai;
	Map<Kaze, List<Hai>> sutehai;
	Map<Kaze, List<Mentsu>> hurohai;
	Map<Kaze, Integer> score;
	Map<Integer, Kaze> kazemap; // playerのid -> 風
	
	Kaze pkaze;
	
	public ConsoleClient2(Player p, Server server) {
		this.reader = new BufferedReader(new InputStreamReader(System.in));
		this.server = server;
		this.p = p;
		
		this.sutehai = new HashMap<Kaze, List<Hai>>(4);
		this.hurohai = new HashMap<Kaze, List<Mentsu>>(4);

		this.score = new HashMap<Kaze, Integer>(4);
		this.score.put(Kaze.TON, 0);
		this.score.put(Kaze.NAN, 0);
		this.score.put(Kaze.SYA, 0);
		this.score.put(Kaze.PE, 0);
		
		this.kazemap = new HashMap<Integer, Kaze>(4);
		
		initKyoku();
}

	public void run() {
		requestGame(p.getId());
	}
	
	public void initKyoku() {
		this.sutehai.put(Kaze.TON, new ArrayList<Hai>());
		this.sutehai.put(Kaze.NAN, new ArrayList<Hai>());
		this.sutehai.put(Kaze.SYA, new ArrayList<Hai>());
		this.sutehai.put(Kaze.PE, new ArrayList<Hai>());
		
		this.hurohai.put(Kaze.TON, new ArrayList<Mentsu>());
		this.hurohai.put(Kaze.NAN, new ArrayList<Mentsu>());
		this.hurohai.put(Kaze.SYA, new ArrayList<Mentsu>());
		this.hurohai.put(Kaze.PE, new ArrayList<Mentsu>());
		
	}

	@Override
	public void requestGame(int id) {
		System.out.println("Client : requestGame");
		server.onGameRequested(id);
	}

	@Override
	public void onGameStartReceived(List<Player> playerList, int index, int[] scores) {
		logger.debug("Server -> Client");
		this.playerlist = playerList;
		this.playerindex = index;
		
		score.put(Kaze.TON, scores[0]);
		score.put(Kaze.NAN, scores[1]);
		score.put(Kaze.SYA, scores[2]);
		score.put(Kaze.PE, scores[3]);
		
		pkaze = Kaze.valueOf(index);
		
		for(int i = 0; i < playerList.size(); i++) {
			kazemap.put(playerList.get(i).getId(), Kaze.valueOf(i));
		}
	}

	@Override
	public void onStartKyokuReceived(Kaze bakaze, int kyokusu, int honba, int tsumibou) {
		logger.debug("Server -> Client");
		this.bakaze = bakaze;
	}

	@Override
	public void onKyusyukyuhaiRequested() {
		logger.debug("Server -> Client");
		System.out.print("Kyusyukyuhai>");
		int index = 0;
		try {
			index = getInt(reader);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("onDiscardReceived : IOExceptionが発生したためindexに0を代入.");
			index = 0;
		}
		sendKyusyukyuhai(index == 1 ? true : false);
	}

	@Override
	public void sendKyusyukyuhai(boolean flag) {
		server.onKyusyukyuhaiReceived(flag);
	}

	@Override
	public void onTsumoHaiReceived(Hai hai) {
		logger.debug("Server -> Client");
		this.tsumohai = hai;
	}

	@Override
	public void onDiscardReceived(boolean tumoari) {
		logger.debug("Server -> Client");
		DisplayConsole dc = new DisplayConsole();
		dc.addTehai(tehai);
		if(tsumohai != null) 
			dc.addTsumoHai(tsumohai);
		dc.disp();
		
		System.out.print("Discard>");
		int index = 0;
		try {
			index = getInt(reader);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("onDiscardReceived : IOExceptionが発生したためindexに0を代入.");
			index = 0;
		}
		sendDiscardIndex(index);
	}

	@Override
	public void sendDiscardIndex(int index) {
		server.onDiscardIndexReceived(index);
	}

	@Override
	public void onChiableIndexListsReceived(List<List<Integer>> lists) {
		logger.debug("Server -> Client");
		System.out.println("CHI");
		System.out.println("-1 : チーしない");
		for (int i = 0; i < lists.size(); i++) {
			System.out.println(i + " : [" + lists.get(i) + "]");
		}

		System.out.print("chi>");
		int index = -1;
		try {
			index = getInt(reader);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("onDiscardReceived : IOExceptionが発生したためindexに-1を代入.");
			index = -1;
		}

		sendChiIndexList(index == -1 ? null : lists.get(index));
	}

	@Override
	public void sendChiIndexList(List<Integer> list) {
		server.onChiIndexListReceived(list);
	}

	@Override
	public void onPonableIndexListsReceived(List<List<Integer>> lists) {
		logger.debug("Server -> Client");
		System.out.println("PON");
		System.out.println("-1 : ポンしない");
		for (int i = 0; i < lists.size(); i++) {
			System.out.println(i + " : [" + lists.get(i) + "]");
		}

		System.out.print("pon>");
		int index = -1;
		try {
			index = getInt(reader);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("onDiscardReceived : IOExceptionが発生したためindexに-1を代入.");
			index = -1;
		}

		sendPonIndexList(index == -1 ? null : lists.get(index));
	}

	@Override
	public void sendPonIndexList(List<Integer> list) {
		server.onPonIndexListReceived(list);
	}

	@Override
	public void onAnkanableIndexListsReceived(List<List<Integer>> lists) {
		logger.debug("Server -> Client");
		System.out.println("ANKAN");
		System.out.println("-1 : 暗槓しない");
		for (int i = 0; i < lists.size(); i++) {
			System.out.println(i + " : [" + lists.get(i) + "]");
		}

		System.out.print("ankan>");
		int index = -1;
		try {
			index = getInt(reader);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("onDiscardReceived : IOExceptionが発生したためindexに-1を代入.");
			index = -1;
		}

		sendAnkanIndexList(index == -1 ? null : lists.get(index));
	}

	@Override
	public void sendAnkanIndexList(List<Integer> list) {
		server.onAnkanIndexListReceived(list);
	}

	@Override
	public void onMinkanableIndexListReceived(List<Integer> hais) {
		logger.debug("Server -> Client");
		System.out.println("MINKAN");
		System.out.println("-1 : 明槓しない");
		for (int i = 0; i < hais.size(); i++) {
			System.out.println(i + " : [" + hais.get(i) + "]");
		}

		System.out.print("minkan>");
		int index = -1;
		try {
			index = getInt(reader);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("onDiscardReceived : IOExceptionが発生したためindexに-1を代入.");
			index = -1;
		}

		sendMinkan(index != -1);
	}

	@Override
	public void sendMinkan(boolean result) {
		server.onMinkanableIndexReceived(result);
	}

	@Override
	public void onKakanableIndexListReceived(List<Integer> list) {
		logger.debug("Server -> Client");
		System.out.println("KAKAN");
		System.out.println("-1 : 加槓しない");
		for (int i = 0; i < list.size(); i++) {
			System.out.println(i + " : [" + list.get(i) + "]");
		}

		System.out.print("kakan>");
		int index = -1;
		try {
			index = getInt(reader);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("onDiscardReceived : IOExceptionが発生したためindexに-1を代入.");
			index = -1;
		}

		sendKakanIndex(index == -1 ? -1 : list.get(index));
	}

	@Override
	public void sendKakanIndex(int index) {
		server.onKakanableIndexReceived(index);
	}

	@Override
	public void onReachableIndexListReceived(List<Integer> list) {
		logger.debug("Server -> Client");
		System.out.println("REACH");
		System.out.println("-1 : 立直しない");
		for (int i = 0; i < list.size(); i++) {
			System.out.println(i + " : [" + list.get(i) + "]");
		}

		System.out.print("reach>");
		int index = -1;
		try {
			index = getInt(reader);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("onDiscardReceived : IOExceptionが発生したためindexに-1を代入.");
			index = -1;
		}

		sendReachIndex(index == -1 ? -1 : list.get(index));
	}

	@Override
	public void sendReachIndex(int index) {
		server.onReachIndexReceived(index);
	}

	@Override
	public void onRonRequested() {
		logger.debug("Server -> Client");
		System.out.println("RON");
		System.out.println("-1:ロンしない");
		System.out.println("-1以外:ロンする");
		System.out.print("ron>");

		int index = -1;
		try {
			index = getInt(reader);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("onDiscardReceived : IOExceptionが発生したためindexに-1を代入.");
			index = -1;
		}

		sendRon(index != -1);
	}

	@Override
	public void sendRon(boolean result) {
		server.onRonReceived(result);
	}

	@Override
	public void onTsumoAgariRequested() {
		logger.debug("Server -> Client");
		System.out.println("TSUMO");
		System.out.println("-1:ツモあがりしない");
		System.out.println("-1以外:ツモあがりする");
		System.out.print("tsumo>");

		int index = -1;
		try {
			index = getInt(reader);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("onDiscardReceived : IOExceptionが発生したためindexに-1を代入.");
			index = -1;
		}

		if (index != -1)
			sendTsumoAgari();
	}

	@Override
	public void sendTsumoAgari() {
		server.onTsumoAgariReceived();
	}

	@Override
	public void onDiscardReceived(Player p, Hai hai, boolean tumokiri) {
		logger.debug("Server -> Client");
		Kaze kaze = kazemap.get(p.getId());
		sutehai.get(kaze).add(hai);
		
		DisplayConsole dc = new DisplayConsole();
		dc.addDiscardhai(hai, kaze, tumokiri);
		dc.disp();
	}

	@Override
	public void onNakiReceived(Player p, Mentsu m) {
		logger.debug("Server -> Client");
	}

	@Override
	public void onRonReceived(Map<Player, List<Hai>> map) {
		logger.debug("Server -> Client");
	}

	@Override
	public void onTsumoAgariReceived() {
		logger.debug("Server -> Client");
	}

	@Override
	public void onFieldReceived(List<Hai> tehai, Map<Kaze, HurohaiList> nakihai, Map<Kaze, List<Hai>> sutehai, Kaze currentTurn, Hai currentSutehai, List<Integer> tehaiSize, int yamaSize, int wanpaiSize, List<Hai> doraList) {
		logger.debug("Server -> Client");
		this.tehai = tehai;
	}

	@Override
	public void onTsumoGiriReceived() {
		logger.debug("Server -> Client");
	}

	@Override
	public void onGameResultReceived(int[] score) {
		logger.debug("Server -> Client");
	}

	@Override
	public void onReachReceived(Kaze currentTurn, int sutehaiIndex) {
		logger.debug("Server -> Client");

	}

	@Override
	public void onKyokuResultReceived(KyokuResult result, int[] newScores, int[] oldScores, List<Integer> soten, List<Hai> uradoraList) {
		logger.debug("Server -> Client");
	}

	@Override
	public void requestNextKyoku() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGameOverReceived() {
		logger.debug("Server -> Client");
	}

	public static int getInt(BufferedReader b) throws IOException {
		int ret = 0;
		Pattern p = Pattern.compile("[0-9]+|-[0-9]+");

		while (true) {
			String buf = b.readLine();
			Matcher m = p.matcher(buf);

			if (!m.matches()) {
				System.out.print("不正な入力です。：");
				continue;
			}
			ret = Integer.parseInt(buf);
			break;
		}

		return ret;
	}


	@Override
	public void onTempaiReceived(Map<Player, List<Hai>> map) {
				logger.debug("Server -> Client");
		
	}

}

class DisplayConsole {
	List<StringBuilder> buffer;

	public DisplayConsole() {
		buffer = new ArrayList<StringBuilder>();
	}

	public void add(String str) {
		String array[] = str.split("\n");
		for (int i = 0; i < array.length; i++) {
			StringBuilder sb = null;
			if (i + 1 > buffer.size()) {
				sb = new StringBuilder();
				buffer.add(sb);
			} else {
				sb = buffer.get(i);
			}
			sb.append(array[i]);
		}
	}

	public void addTehai(List<Hai> tehai) {
		add("　\n｜\n｜\n　");

		for (int i = 0; i < tehai.size(); i++) {
			Hai hai = tehai.get(i);
			String notation = hai.notation();
			if (notation.length() == 1) {
				notation = notation + "　";
			} else if (notation.length() == 3) {
				notation = notation.substring(1);
			}
			if (hai.aka()) {
				if (i >= 10)
					add(String.format("赤　\n%s｜\n%s｜\nー　\n　%d", notation.charAt(0), notation.charAt(1), i));
				else
					add(String.format("赤　\n%s｜\n%s｜\nー　\n　%s", notation.charAt(0), notation.charAt(1), getStringZenkaku(i)));
			} else {
				if (i >= 10)
					add(String.format("ー　\n%s｜\n%s｜\nー　\n　%d", notation.charAt(0), notation.charAt(1), i));
				else
					add(String.format("ー　\n%s｜\n%s｜\nー　\n　%s", notation.charAt(0), notation.charAt(1), getStringZenkaku(i)));
			}
		}
	}

	public void addHai(Hai hai) {
		String notation = hai.notation();
		if (notation.length() == 1) {
			notation = notation + "　";
		} else if (notation.length() == 3) {
			notation = notation.substring(1);
		}
		if (hai.aka()) {
			add(String.format("赤　\n%s｜\n%s｜\nー　", notation.charAt(0), notation.charAt(1)));
		} else {
			add(String.format("ー　\n%s｜\n%s｜\nー　", notation.charAt(0), notation.charAt(1)));
		}
	}

	public void addYokoHai(Hai hai) {
		String notation = hai.notation();
		if (notation.length() == 1) {
			notation = notation + "　";
		} else if (notation.length() == 3) {
			notation = notation.substring(1);
		}
		add(String.format("赤　\n%s｜\n%s｜\nー　", notation.charAt(0), notation.charAt(1)));
		if (hai.aka()) {
			add(String.format("赤　\n%s｜\n%s｜\nー　", notation.charAt(0), notation.charAt(1)));
		} else {
			add(String.format("ー　\n%s｜\n%s｜\nー　", notation.charAt(0), notation.charAt(1)));
		}
	}

	public void addTsumoHai(Hai hai) {
		add("　\n｜\n｜\n　");
		String notation = hai.notation();
		if (notation.length() == 1) {
			notation = notation + "　";
		} else if (notation.length() == 3) {
			notation = notation.substring(1);
		}
		if (hai.aka()) {
			add(String.format("赤　\n%s｜\n%s｜\nー　\n　13", notation.charAt(0), notation.charAt(1)));
		} else {
			add(String.format("ー　\n%s｜\n%s｜\nー　\n　13", notation.charAt(0), notation.charAt(1)));
		}
	}

	public void addDiscardhai(Hai hai, Kaze k, boolean tsumokiri) {
		String notation = hai.notation();
		if (notation.length() == 1) {
			notation = notation + "　";
		} else if (notation.length() == 3) {
			notation = notation.substring(1);
		}
		
		if(tsumokiri) {
			add(String.format("　　　　　　　\n%s（ツモ切り）\n　　　　　　　\n　　　　　　　", k.notation()));
		}else {
			add(String.format("　　\n%s　\n　　\n　　", k.notation()));
		}
		add("　\n｜\n｜\n　");
		if (hai.aka()) {
			add(String.format("赤　\n%s｜\n%s｜\nー　", notation.charAt(0), notation.charAt(1)));
		} else {
			add(String.format("ー　\n%s｜\n%s｜\nー　", notation.charAt(0), notation.charAt(1)));
		}
	}

	/**
	 * 
	 * @param m
	 * @param place 鳴いた家.南->下家、西->対面、北->上家に対応する.
	 */
	public void addNaki(Mentsu m, Kaze place) {
		if(m.type() == Mentsu.Type.KANTU) {
			
		}else {
			switch(place) {
			case NAN:
				
			}
		}
	}
	
	public static String getStringZenkaku(int i) {
		switch (i) {
		case 0:
			return "０";
		case 1:
			return "１";
		case 2:
			return "２";
		case 3:
			return "３";
		case 4:
			return "４";
		case 5:
			return "５";
		case 6:
			return "６";
		case 7:
			return "７";
		case 8:
			return "８";
		case 9:
			return "９";
		}
		return "　";
	}

	
	public void disp() {
		for (StringBuilder sb : buffer) {
			System.out.println(sb.toString());
		}
	}
}