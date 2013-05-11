package test.client;

import java.util.List;

import test.server.ServerMessage;
import test.server.ServerMessageType;
import test.server.SingleServerReceiver;
import util.MyLogger;
import static test.server.ServerMessage.*;

public class ClientSenderImpl implements ClientSender{
	private static MyLogger logger = MyLogger.getLogger();
	private SingleServerReceiver receiver;
	
	public ClientSenderImpl() {
		
	}
	
	public void setServerReceiver(SingleServerReceiver rec) {
		this.receiver = rec;
	}

	@Override
	public void requestGame(int id) {
		logger.debug();
		ServerMessageType mType = ServerMessageType.GAME_REQUESTED;
		ServerMessage m = new IntegerMessage(id);
		receiver.onMessageReceived(m, mType);
	}

	@Override
	public void requestNextKyoku() {
		logger.debug();
		ServerMessageType mType = ServerMessageType.NEXT_KYOKU_REQUESTED;
		receiver.onMessageReceived(EmptyMessage.getInstance(), mType);
	}

	@Override
	public void sendAnkanIndexList(List<Integer> list) {
		logger.debug();
		ServerMessageType mType = ServerMessageType.ANKAN_INDEX_LIST_RECEIVED;
		ServerMessage m = new IntegerListMessage(list);
		receiver.onMessageReceived(m, mType);
	}

	@Override
	public void sendChiIndexList(List<Integer> list) {
		logger.debug();
		ServerMessageType mType = ServerMessageType.CHIINDEX_LIST_RECEIVED;
		ServerMessage m = new IntegerListMessage(list);
		receiver.onMessageReceived(m, mType);
	}

	@Override
	public void sendDiscardIndex(int index) {
		logger.debug();
		ServerMessageType mType = ServerMessageType.DISCARD_INDEX_RECEIVED;
		ServerMessage m = new IntegerMessage(index);
		receiver.onMessageReceived(m, mType);
	}

	@Override
	public void sendKakanIndex(int index) {
		logger.debug();
		ServerMessageType mType = ServerMessageType.KAKANABLE_INDEX_RECEIVED;
		ServerMessage m = new IntegerMessage(index);
		receiver.onMessageReceived(m, mType);
	}

	@Override
	public void sendKyusyukyuhai() {
		logger.debug();
		ServerMessageType mType = ServerMessageType.KYUSYUKYUHAI_RECEIVED;
		receiver.onMessageReceived(EmptyMessage.getInstance(), mType);
	}

	@Override
	public void sendMinkan() {
		logger.debug();
		ServerMessageType mType = ServerMessageType.MINKAN_RECEIVED;
		receiver.onMessageReceived(EmptyMessage.getInstance(), mType);
	}

	@Override
	public void sendPonIndexList(List<Integer> list) {
		logger.debug();
		ServerMessageType mType = ServerMessageType.PON_INDEX_LIST_RECEIVED;
		ServerMessage m = new IntegerListMessage(list);
		receiver.onMessageReceived(m, mType);
	}

	@Override
	public void sendReachIndex(int index) {
		logger.debug();
		ServerMessageType mType = ServerMessageType.REACH_INDEX_RECEIVED;
		ServerMessage m = new IntegerMessage(index);
		receiver.onMessageReceived(m, mType);
	}

	@Override
	public void sendRon() {
		logger.debug();
		ServerMessageType mType = ServerMessageType.RON_RECEIVED;
		receiver.onMessageReceived(EmptyMessage.getInstance(), mType);
	}

	@Override
	public void sendTsumoAgari() {
		logger.debug();
		ServerMessageType mType = ServerMessageType.TSUMO_AGARI_RECEIVED;
		receiver.onMessageReceived(EmptyMessage.getInstance(), mType);
	}

	@Override
	public void sendReject() {
		logger.debug();
		ServerMessageType mType = ServerMessageType.REJECT_RECEIVED;
		receiver.onMessageReceived(EmptyMessage.getInstance(), mType);
	}
	
	
}
