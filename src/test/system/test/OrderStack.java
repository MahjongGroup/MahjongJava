package test.system.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderStack {
	private Map<Order,List<OrderPacket>> stack;
		
	{
		stack = new HashMap<Order, List<OrderPacket>>();
		for(Order o:Order.values()){
			stack.put(o, new ArrayList<OrderPacket>());
		}
	}
	public void pushPacket(OrderPacket packet){
		System.out.println(packet);
		List<OrderPacket> list = stack.get(packet.getOrder());
		OrderPacket delete = null;
		for(OrderPacket op:list){
			if(op.getIndex() == packet.getIndex())
				delete = op;
		}
		if(delete != null)
			list.remove(delete);
		list.add(packet);
	}
	
	public List<OrderPacket> popPackets(Order order){
		return stack.get(order);
	}
		
	public boolean isFullStack(Order order){
		System.out.println(order.name() + " stack size:" + stack.get(order).size() + " / " + order.getCount());
		return stack.get(order).size() >= order.getCount();
	}
	
	public void clearStack(Order order){
		stack.get(order).clear();
	}
}
