package test.server;

import java.util.ArrayList;
import java.util.List;


/**
 * サーバーがクライアントから受け取るメッセージ.
 */
public interface ServerMessage {
	public static class IntegerMessage implements ServerMessage {
		private final int data;

		public IntegerMessage(int i) {
			this.data = i;
		}

		public int getData() {
			return data;
		}
	}

	public static class IntegerListMessage implements ServerMessage {
		private final List<Integer> data;

		public IntegerListMessage(List<Integer> list) {
			this.data = list;
		}

		public List<Integer> getData() {
			return new ArrayList<Integer>(data);
		}
	}

	public static class BooleanMessage implements ServerMessage {
		private final boolean data;

		public BooleanMessage(boolean b) {
			this.data = b;
		}

		public boolean getData() {
			return data;
		}
	}

	public static class EmptyMessage implements ServerMessage {
		private static EmptyMessage instance;

		public static ServerMessage getInstance() {
			if (instance == null)
				instance = new EmptyMessage();
			return instance;
		}
	}
}
