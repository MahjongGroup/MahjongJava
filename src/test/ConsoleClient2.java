package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
	private BufferedReader reader;
	Server server;
	Player p;
	int playerindex;
	List<Player> playerlist;
	int scores[];

	Kaze bakaze;
	List<Hai> tehai;
	Hai tsumohai;
	
	
	public ConsoleClient2(Player p, Server server) {
		reader = new BufferedReader(new InputStreamReader(System.in));
		this.server = server;
		this.p = p;
	}

	public void run() {
		requestGame(p.getId());
	}

	@Override
	public void requestGame(int id) {
		System.out.println("Client : requestGame");
		server.onGameRequested(id);
	}

	@Override
	public void onGameStartReceived(List<Player> playerList, int index, int[] scores) {
		System.out.println("Client : onGameStartReceived");
		this.playerlist = playerList;
		this.playerindex = index;
		this.scores = scores.clone();
	}

	@Override
	public void onStartKyokuReceived(Kaze bakaze, int kyokusu, int honba, int tsumibou) {
		System.out.println("Client : onStartKyokuReceived");
		this.bakaze = bakaze;
	}

	@Override
	public void onKyusyukyuhaiRequested() {
		System.out.println("Client : onKyusyukyuhaiRequested");
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
		System.out.println("Client : onTsumoHaiReceived");
		this.tsumohai = hai;
	}

	@Override
	public void onDiscardReceived(boolean tumoari) {
		DisplayConsole dc = new DisplayConsole();
		getDispTehai(tehai, dc);
		if(tsumohai != null) 
			getDispTsumoHai(tsumohai, dc);
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
		// TODO Auto-generated method stub

	}

	@Override
	public void onNakiReceived(Player p, Mentsu m) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRonReceived(Map<Player, List<Hai>> map) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTsumoAgariReceived() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFieldReceived(List<Hai> tehai, Map<Kaze, HurohaiList> nakihai, Map<Kaze, List<Hai>> sutehai, Kaze currentTurn, Hai currentSutehai, List<Integer> tehaiSize, int yamaSize, int wanpaiSize, List<Hai> doraList) {
		this.tehai = tehai;
	}

	@Override
	public void onTsumoGiriReceived() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGameResultReceived(int[] score) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReachReceived(Kaze currentTurn, int sutehaiIndex) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onKyokuResultReceived(KyokuResult result, int[] newScores, int[] oldScores, List<Integer> soten, List<Hai> uradoraList) {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestNextKyoku() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGameOverReceived() {
		// TODO Auto-generated method stub

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

	public static DisplayConsole getDispTehai(List<Hai> tehai, DisplayConsole dc) {
		dc.add("　\n｜\n｜\n　");

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
					dc.add(String.format("赤　\n%s｜\n%s｜\nー　\n　%d", notation.charAt(0), notation.charAt(1), i));
				else
					dc.add(String.format("赤　\n%s｜\n%s｜\nー　\n　%s", notation.charAt(0), notation.charAt(1), getStringZenkaku(i)));
			} else {
				if (i >= 10)
					dc.add(String.format("ー　\n%s｜\n%s｜\nー　\n　%d", notation.charAt(0), notation.charAt(1), i));
				else
					dc.add(String.format("ー　\n%s｜\n%s｜\nー　\n　%s", notation.charAt(0), notation.charAt(1), getStringZenkaku(i)));
			}
		}
		return dc;
	}

	public static DisplayConsole getDispTsumoHai(Hai hai, DisplayConsole dc) {
		dc.add("　\n｜\n｜\n　");
		String notation = hai.notation();
		if (notation.length() == 1) {
			notation = notation + "　";
		} else if (notation.length() == 3) {
			notation = notation.substring(1);
		}
		if (hai.aka()) {
			dc.add(String.format("赤　\n%s｜\n%s｜\nー　\n　13", notation.charAt(0), notation.charAt(1)));
		} else {
			dc.add(String.format("ー　\n%s｜\n%s｜\nー　\n　13", notation.charAt(0), notation.charAt(1)));
		}
		return dc;
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

	public void disp() {
		for (StringBuilder sb : buffer) {
			System.out.println(sb.toString());
		}
	}
}