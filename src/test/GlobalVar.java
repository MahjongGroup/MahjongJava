package test;

import server.Server;
import system.test.Player;
import client.ClientOperator;

/**
 * 結合テスト用クラス
 * @author kotaro
 *
 */
public class GlobalVar {
	public static Server trs[];
	public static ClientOperator ops[];
	public static Player players[];
	
	/**
	 * すべての変数が設定されるまで待機する。
	 */
	public static void waitSettings() {
		while(checkNull()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static boolean checkNull() {
		for (Server tr: trs) {
			if(tr == null)
				return true;
		}
		for (ClientOperator op: ops) {
			if(op == null)
				return true;
		}
		return false;
	}
}
