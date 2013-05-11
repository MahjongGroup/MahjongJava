package test.server.offline;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import system.Player;
import system.hai.Hai;
import system.hai.Kaze;
import system.hai.Mentsu;
import system.result.KyokuResult;
import test.server.ServerSender;
import test.server.SingleServerSender;

public class ServerSenderImpl implements ServerSender{
	private final Map<Player, SingleServerSender> map;

	public ServerSenderImpl(Map<Player, SingleServerSender> map) {
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
	public Set<java.util.Map.Entry<Player, SingleServerSender>> entrySet() {
		return map.entrySet();
	}

	@Override
	public SingleServerSender get(Object key) {
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
	public SingleServerSender put(Player key, SingleServerSender value) {
		return map.put(key, value);
	}

	@Override
	public void putAll(Map<? extends Player, ? extends SingleServerSender> m) {
		map.putAll(m);
	}

	@Override
	public SingleServerSender remove(Object key) {
		return map.remove(key);
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public Collection<SingleServerSender> values() {
		return map.values();
	}


	@Override
	public void notifyStartKyoku(Kaze bakaze, int kyokusu, int honba,
			int tsumibou) {
		for (SingleServerSender sender : map.values()) {
			sender.notifyStartKyoku(bakaze, kyokusu, honba, tsumibou);
		}
	}

	@Override
	public void notifyDiscard(Player p, Hai hai, boolean tumokiri) {
		for (SingleServerSender sender : map.values()) {
			sender.notifyDiscard(p, hai, tumokiri);
		}
	}

	@Override
	public void notifyNaki(Player p, Mentsu m) {
		for (SingleServerSender sender : map.values()) {
			sender.notifyNaki(p, m);
		}
	}

	@Override
	public void notifyRon(Map<Player, List<Hai>> map) {
		for (SingleServerSender sender : this.map.values()) {
			sender.notifyRon(map);
		}
	}

	@Override
	public void notifyReach(Kaze currentTurn, int sutehaiIndex) {
		for (SingleServerSender sender : map.values()) {
			sender.notifyReach(currentTurn, sutehaiIndex);
		}
	}

	@Override
	public void notifyKyokuResult(KyokuResult kr, int[] newScore,
			int[] oldScore, List<Integer> soten, List<Hai> uradoraList) {
		for (SingleServerSender sender : map.values()) {
			sender.notifyKyokuResult(kr, newScore, oldScore, soten, uradoraList);
		}
	}

	@Override
	public void notifyTempai(Map<Player, List<Hai>> map) {
		for (SingleServerSender sender : this.map.values()) {
			sender.notifyTempai(map);
		}
	}

	@Override
	public void notifyGameResult(int[] Score) {
		for (SingleServerSender sender : map.values()) {
			sender.notifyGameResult(Score);
		}
	}
}
