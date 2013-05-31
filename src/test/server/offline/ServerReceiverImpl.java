package test.server.offline;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import system.Player;
import test.server.ServerMessageType;
import test.server.ServerReceiver;
import test.server.SingleServerReceiver;

public class ServerReceiverImpl implements ServerReceiver {
	private final Map<Player, SingleServerReceiver> map;

	public ServerReceiverImpl(Map<Player, SingleServerReceiver> map) {
		this.map = map;
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<Player, SingleServerReceiver>> entrySet() {
		return map.entrySet();
	}

	@Override
	public SingleServerReceiver get(Object key) {
		return map.get(key);
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public Set<Player> keySet() {
		return map.keySet();
	}

	@Override
	public SingleServerReceiver put(Player key, SingleServerReceiver value) {
		return map.put(key, value);
	}

	@Override
	public void putAll(Map<? extends Player, ? extends SingleServerReceiver> m) {
		map.putAll(m);
	}

	@Override
	public SingleServerReceiver remove(Object key) {
		return map.remove(key);
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public Collection<SingleServerReceiver> values() {
		return map.values();
	}

	@Override
	public boolean wait(ServerMessageType mType, long timeout) {
		Set<Player> pset = new HashSet<Player>(map.keySet());
		
		long startTime = System.currentTimeMillis();

		while (true) {
			if(timeout != 0) {
				if(System.currentTimeMillis() - startTime > timeout)
					return true;
			}
			
			Player removedPlayer = null;
			for (Player p : pset) {
				SingleServerReceiver rec = map.get(p);

				if (rec.isMessageReceived(mType)) {
					removedPlayer = p;
					break;
				}
			}
			if (removedPlayer != null) {
				pset.remove(removedPlayer);
			}
			if (pset.size() == 0)
				return false;
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean wait(Player p, ServerMessageType mType, long timeout) {
		long startTime = System.currentTimeMillis();
		while (true) {
			if(timeout != 0) {
				if(System.currentTimeMillis() - startTime > timeout)
					return true;
			}
			
			SingleServerReceiver rec = map.get(p);

			if (rec.isMessageReceived(mType)) {
				return false;
			}
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
