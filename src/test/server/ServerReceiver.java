package test.server;

import java.util.Map;

import system.Player;

public interface ServerReceiver extends Map<Player, SingleServerReceiver>{
	/**
	 * 全てのクライアントから指定された種類のメッセージを受け取るまで待機する.
	 * @param mType メッセージの種類.
	 * @param timeout 待つ最大の時間.0の場合、無限に待つ.
	 * @return タイムアウトした場合true.
	 */
	public boolean wait(ServerMessageType mType, long timeout);

	/**
	 * 指定されたプレイヤーから指定された種類のメッセージを受け取るまで待機する.
	 * @param p プレイヤー.
	 * @param mType メッセージの種類.
	 * @param timeout 待つ最大の時間.0の場合、無限に待つ.
	 * @return タイムアウトした場合true.
	 */
	public boolean wait(Player p, ServerMessageType mType, long timeout);
	
}
