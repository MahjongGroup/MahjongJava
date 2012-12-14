package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import client.Client;

import server.Server;
import system.Hai;
import system.HurohaiList;
import system.Kaze;
import system.KyokuResult;
import system.Mentu;
import system.Player;

/**
 * コンソール入力を受け付けるクライアント.
 */
public class ConsoleClient implements Client {
	private static class Kyoku {
		// 各プレイヤーの手牌サイズ. 0から起家
		int tehaiSizes[] = new int[4];
		
		// 山牌数
		int yamahaiSize;
		
		// プレイヤーのインデックス
		int playerIndex;
		
		
	}

	private BufferedReader reader;
	
	// DEBUG 結合用フィールド
	private Server server;

	/**
	 * 指定されたサーバーを持つコンソールクライアントを生成する.
	 * @param server サーバ
	 */
	public ConsoleClient(Server server) {
		this.server = server;
		this.onConstructed();
	}

	public ConsoleClient() {
		this.server = server;
		this.onConstructed();
	}
	
	private void onConstructed() {
		reader = new BufferedReader(new InputStreamReader(System.in));
	}

	// DEBUG 結合用メソッド
	public void setServer(Server tr) {
		this.server = tr;
	}

	@Override
	public void onKyusyukyuhaiRequested() {
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
		System.out.println("ツモ牌 : " + hai);
	}

	@Override
	public void onDiscardReceived(boolean tumoari) {
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
	public void onChiableIndexListsReceived(List<List<Integer>> list) {
		System.out.println("CHI");
		System.out.println("-1 : チーしない");
		for (int i = 0; i < list.size(); i++) {
			System.out.println(i + " : [" + list.get(i) + "]");
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

		sendChiIndexList(index == -1 ? null : list.get(index));
	}

	@Override
	public void sendChiIndexList(List<Integer> list) {
		server.onChiIndexListReceived(list);
	}

	@Override
	public void onPonableIndexListsReceived(List<List<Integer>> list) {
		System.out.println("PON");
		System.out.println("-1 : ポンしない");
		for (int i = 0; i < list.size(); i++) {
			System.out.println(i + " : [" + list.get(i) + "]");
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

		sendPonIndexList(index == -1 ? null : list.get(index));
	}

	@Override
	public void sendPonIndexList(List<Integer> list) {
		server.onPonIndexListReceived(list);
	}

	@Override
	public void onAnkanableIndexListsReceived(List<List<Integer>> list) {
		System.out.println("ANKAN");
		System.out.println("-1 : 暗槓しない");
		for (int i = 0; i < list.size(); i++) {
			System.out.println(i + " : [" + list.get(i) + "]");
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

		sendAnkanIndexList(index == -1 ? null : list.get(index));
	}

	@Override
	public void sendAnkanIndexList(List<Integer> list) {
		server.onAnkanIndexListReceived(list);
	}

	@Override
	// TODO 引数リストでなくてよくない？
	public void onMinkanableIndexListReceived(List<Integer> list) {
		System.out.println("MINKAN");
		System.out.println("-1 : 明槓しない");
		for (int i = 0; i < list.size(); i++) {
			System.out.println(i + " : [" + list.get(i) + "]");
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

	public void onGameResultReceived(int[] score){
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
	// 引数にbooleanはいらないんだっけ？
	public void sendTsumoAgari() {
		server.onTsumoAgariReceived();
	}

	@Override
	public void onDiscardReceived(Player player, Hai hai, boolean tumokiri) {
		// 何もしない
	}

	@Override
	public void onNakiReceived(Player player, Mentu mentu) {
		// 何もしない
	}

	@Override
	public void onRonReceived(Map<Player, List<Hai>> map) {
		// 何もしない
	}

	@Override
	public void onTsumoAgariReceived() {
		// 何もしない
	}


	public void onFieldReceived(List<Hai> tehai, Map<Kaze, HurohaiList> nakihai,
			Map<Kaze, List<Hai>> sutehai, Kaze currentTurn) {
		// 何もしない
	}

	@Override
	public void onTsumoGiriReceived() {
		// 何もしない
	}

	@Override
	public void onReachReceived(Kaze currentTurn, int sutehaiIndex) {
		// 何もしない
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
	public void requestGame(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameStartReceived(List<Player> playerList,int index,int[] score) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStartKyokuReceived(Kaze bakaze, int kyokusu) {
		// TODO Auto-generated method stub
		
	}

	public void requestNextKyoku(){
		server.onNextKyokuRequested();
	}
	
	@Override
	public void onKyokuResultReceived(KyokuResult result,int[] newScore,int[] oldScore) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFieldReceived(List<Hai> tehai,
			Map<Kaze, HurohaiList> nakihai, Map<Kaze, List<Hai>> sutehai,
			Kaze currentTurn, Hai currentSutehai, List<Integer> tehaiSize,
			int yamaSize, int wanpaiSize, List<Hai> doraList) {
		// TODO Auto-generated method stub
		
	}


}
