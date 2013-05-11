package test.server.offline;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import test.server.ServerMessage;
import test.server.ServerMessageType;
import test.server.SingleServerReceiver;

/**
 * スレッドセーフなサーバーレシーバーの実装.
 */
public class SingleServerReceiverImpl implements SingleServerReceiver{
	private final Map<ServerMessageType, ServerMessage> map;
	
	public SingleServerReceiverImpl() {
		map = Collections.synchronizedMap(new HashMap<ServerMessageType, ServerMessage>());
	}
	
	@Override
	public synchronized void onMessageReceived(ServerMessage m, ServerMessageType mType) {
		map.put(mType, m);
	}

	@Override
	public synchronized boolean isMessageReceived(ServerMessageType mType) {
		return map.containsKey(mType);
	}

	@Override
	public synchronized ServerMessage fetchMessage(ServerMessageType mType) {
		return map.remove(mType);
	}
}
