package test.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import system.Player;
import system.hai.Hai;
import system.hai.HurohaiList;
import system.hai.Kaze;
import system.hai.Mentsu;
import system.result.KyokuResult;
import util.MyLogger;

/**
 * コンソール入力を受け付けるクライアント.
 */
public class ConsoleClient implements ClientListener {
	private static MyLogger logger = MyLogger.getLogger();

	private BufferedReader reader;
	private ClientSender sender;

	public ConsoleClient(ClientSender sender) {
		this.setSender(sender);
		this.onConstructed();
	}

	private void onConstructed() {
		reader = new BufferedReader(new InputStreamReader(System.in));
	}

	public void setSender(ClientSender sender) {
		this.sender = sender;
	}

	@Override
	public void onKyusyukyuhaiRequested() {
		logger.debug();
		System.out.print("Kyusyukyuhai>");
		int index = 0;
		try {
			index = getInt(reader);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("onDiscardReceived : IOExceptionが発生したためindexに0を代入.");
			index = 0;
		}

		if (index != 0)
			sender.sendKyusyukyuhai();
	}

	@Override
	public void onTsumoHaiReceived(Hai hai) {
		logger.debug();
		System.out.println("ツモ牌 : " + hai);
	}

	@Override
	public void onDiscardReceived(boolean tumoari) {
		logger.debug();
		System.out.print("Discard>");
		int index = -1;
		try {
			index = getInt(reader);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("onDiscardReceived : IOExceptionが発生したためindexに0を代入.");
			index = -1;
		}
		if (index != -1)
			sender.sendDiscardIndex(index);
	}

	// @Override
	// public void onChiableIndexListsReceived(List<List<Integer>> list) {
	// logger.debug();
	// System.out.println("CHI");
	// System.out.println("-1 : チーしない");
	// for (int i = 0; i < list.size(); i++) {
	// System.out.println(i + " : [" + list.get(i) + "]");
	// }
	//
	// System.out.print("chi>");
	// int index = -1;
	// try {
	// index = getInt(reader);
	// } catch (IOException e) {
	// e.printStackTrace();
	// System.err.println("onDiscardReceived : IOExceptionが発生したためindexに-1を代入.");
	// index = -1;
	// }
	//
	// if(index != -1)
	// sender.sendChiIndexList(list.get(index));
	// }

	// @Override
	// public void onPonableIndexListsReceived(List<List<Integer>> list) {
	// logger.debug();
	// System.out.println("PON");
	// System.out.println("-1 : ポンしない");
	// for (int i = 0; i < list.size(); i++) {
	// System.out.println(i + " : [" + list.get(i) + "]");
	// }
	//
	// System.out.print("pon>");
	// int index = -1;
	// try {
	// index = getInt(reader);
	// } catch (IOException e) {
	// e.printStackTrace();
	// System.err.println("onDiscardReceived : IOExceptionが発生したためindexに-1を代入.");
	// index = -1;
	// }
	//
	// if(index != -1)
	// sender.sendPonIndexList(list.get(index));
	// }

	@Override
	public void onAnkanableIndexListsReceived(List<List<Integer>> list) {
		logger.debug();
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

		if (index != -1)
			sender.sendAnkanIndexList(list.get(index));
	}

	@Override
	public void onKakanableIndexListReceived(List<Integer> list) {
		logger.debug();
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

		if (index != -1)
			sender.sendKakanIndex(list.get(index));
	}

	@Override
	public void onReachableIndexListReceived(List<Integer> list) {
		logger.debug();
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

		if (index != -1)
			sender.sendReachIndex(list.get(index));
	}

	// @Override
	// public void onRonRequested() {
	// logger.debug();
	// System.out.println("RON");
	// System.out.println("-1:ロンしない");
	// System.out.println("-1以外:ロンする");
	// System.out.print("ron>");
	//
	// int index = -1;
	// try {
	// index = getInt(reader);
	// } catch (IOException e) {
	// e.printStackTrace();
	// System.err.println("onDiscardReceived : IOExceptionが発生したためindexに-1を代入.");
	// index = -1;
	// }
	//
	// if(index != -1)
	// sender.sendRon();
	// }

	@Override
	public void onTsumoAgariRequested() {
		logger.debug();
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
			sender.sendTsumoAgari();
	}

	@Override
	public void onDiscardReceived(Player player, Hai hai, boolean tumokiri) {
		logger.debug();
	}

	@Override
	public void onNakiReceived(Player player, Mentsu mentu) {
		logger.debug();
	}

	@Override
	public void onRonReceived(Map<Player, List<Hai>> map) {
		logger.debug();
	}

	@Override
	public void onTsumoAgariReceived() {
		logger.debug();
	}

	public void onFieldReceived(List<Hai> tehai, Map<Kaze, HurohaiList> nakihai, Map<Kaze, List<Hai>> sutehai, Kaze currentTurn) {
		logger.debug();
	}

	@Override
	public void onTsumoGiriReceived() {
		logger.debug();
	}

	@Override
	public void onReachReceived(Kaze currentTurn, int sutehaiIndex) {
		logger.debug();
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
	public void onGameStartReceived(List<Player> playerList, int index, int[] score) {
		logger.debug();
	}

	@Override
	public void onStartKyokuReceived(Kaze bakaze, int kyokusu, int honba, int tsumibou) {
		logger.debug();
	}

	@Override
	public void onKyokuResultReceived(KyokuResult result, int[] newScore, int[] oldScore, List<Integer> changeScore, List<Hai> uradoraList) {
		logger.debug();
	}

	@Override
	public void onFieldReceived(List<Hai> tehai, Map<Kaze, HurohaiList> nakihai, Map<Kaze, List<Hai>> sutehai, Kaze currentTurn,
			Hai currentSutehai, List<Integer> tehaiSize, int yamaSize, int wanpaiSize, List<Hai> doraList) {
		logger.debug();
		sender.requestNextKyoku();
	}

	@Override
	public void onGameOverReceived() {
		logger.debug();
	}

	@Override
	public void onTempaiReceived(Map<Player, List<Hai>> map) {
		logger.debug();
	}

	@Override
	public void onGameResultReceived(int[] score) {
		logger.debug();
	}

	@Override
	public void onNakiRequested(boolean ron, List<Integer> minkan, List<List<Integer>> pon, List<List<Integer>> chi) {
		List<Selection> selections = new ArrayList<Selection>();
		selections.add(new Selection(null, "何もしない"));
		if (ron) {
			selections.add(new Selection(Selection.Type.RON, ""));
		}
		if (minkan != null) {
			selections.add(new Selection(Selection.Type.MINKAN, ""));
		}
		if (pon != null) {
			for (int i = 0; i < pon.size(); i++) {
				selections.add(new Selection(Selection.Type.PON, "", i));
			}
		}
		if (chi != null) {
			for (int i = 0; i < chi.size(); i++) {
				selections.add(new Selection(Selection.Type.CHI, "", i));
			}
		}

		for (int i = 0; i < selections.size(); i++) {
			Selection s = selections.get(i);
			System.out.println("[" + i + "] : " + s.toString());
		}
		System.out.println(">");

		int index = 0;
		try {
			index = getInt(reader);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("onDiscardReceived : IOExceptionが発生したためindexに-1を代入.");
			index = 0;
		}

		if (index < 0 || index >= selections.size()) {
			index = 0;
		}
		Selection s = selections.get(index);
		if (s.getType() == null) {
			sender.sendReject();
			return;
		}
		switch (s.getType()) {
		case RON:
			sender.sendRon();
			break;
		case MINKAN:
			sender.sendMinkan();
			break;
		case PON:
			sender.sendPonIndexList(pon.get(s.getIndex()));
			break;
		case CHI:
			sender.sendChiIndexList(chi.get(s.getIndex()));
			break;
		default:
			break;
		}
	}

	private static class Selection {
		public static enum Type {
			RON, CHI, PON, MINKAN
		}

		private final Type type;
		private final String msg;
		private final int index;

		public Selection(Type type, String msg, int index) {
			this.type = type;
			this.msg = msg;
			this.index = index;
		}

		public Selection(Type type, String msg) {
			this.type = type;
			this.msg = msg;
			this.index = 0;
		}
		
		public int getIndex() {
			return index;
		}

		public Type getType() {
			return type;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			if (type != null) {
				sb.append(type);
				sb.append(" ");
			}
			sb.append(msg);
			return sb.toString();
		}
	}

}