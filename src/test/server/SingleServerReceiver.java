package test.server;


/**
 * クライアントからメッセージを受け取るレシーバークラス．
 */
public interface SingleServerReceiver {
	
	/**
	 * クライアントからメッセージを受け取ったときに呼び出される．
	 * 既に同じタイプのメッセージが届いている場合は上書きする.
	 * 
	 * @param m クライアントから受け取ったメッセージ。
	 * @param mType そのメッセージのタイプ.
	 */
	public void onMessageReceived(ServerMessage m, ServerMessageType mType);

	/**
	 * 指定されたタイプのメッセージを受け取っている場合trueを返す.
	 * @param mType 受け取ってるか確認するメッセージ.
	 * @return　指定されたタイプのメッセージを受け取っている場合true.
	 */
	public boolean isMessageReceived(ServerMessageType mType);
	
	/**
	 * 指定されたタイプのメッセージを受け取る.存在しない場合はnullを返す.
	 * レシーバーからメッセージを受け取った後はそのメッセージタイプに対するisMessageReceivedは
	 * falseを返す.
	 * 
	 * @param mType 取得したいメッセージのタイプ.
	 * @return 指定されたタイプのメッセージ.存在しない場合、null.
	 */
	public ServerMessage fetchMessage(ServerMessageType mType);
	
}
