package system.test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

abstract public class OrderCommunicator {
	private ObjectInputStream input;
	private OrderStack stack;
	private OrderPacketReceiver opr;
	private ObjectOutputStream output;
	
	{
		stack = new OrderStack();
	}
	
	public void setInput(ObjectInputStream input){
		this.input = input;
	}
	
	public void setOutput(ObjectOutputStream output){
		this.output = output;
	}

	private class OrderPacketReceiver extends Thread {
		private boolean isActive;
		{
			isActive = true;
		}

		public void run() {
			try {
				while (isActive) {
					System.out.println("before read");
					OrderPacket op = (OrderPacket) input.readObject();
					if(op == null)
						continue;
					stack.pushPacket(op);
					if (stack.isFullStack(op.getOrder())) {
						dispatch(op.getOrder());
						stack.clearStack(op.getOrder());
					}
				}
			} catch (IOException e) {
				System.out.println("IOException:" + e.getMessage());
			} catch (ClassNotFoundException e) {
				System.out.println("ClassNotFoundException:" + e.getMessage());
			}
		}

		public void finish() {
			isActive = false;
		}
	}

	abstract public void dispatch(Order order);

	protected List<OrderPacket> getPackets(Order order){
		List<OrderPacket> tmpList = stack.popPackets(order);
		OrderPacket[] tmpArray = new OrderPacket[stack.popPackets(order).size()];
		for(OrderPacket op:stack.popPackets(order)){
			if(op.getIndex() != 0)
				tmpArray[op.getIndex() - 1] = op;
		}
		tmpList.clear();
		for(int i = 0;i < tmpArray.length;i++){
			tmpList.add(tmpArray[i]);
		}
		return tmpList;
	}
	
	public void sendPacket(OrderPacket op) {
		try {
			output.writeObject(op);
		} catch (IOException e) {
			System.out.println("IOException:" + e.getMessage());
		}
	}
	
	public void launch(ObjectInputStream ois,ObjectOutputStream oos){
		System.out.println("launch");
		this.input = ois;
		this.output = oos;
		opr = new OrderPacketReceiver();
		opr.start();
	}
	
	public void finish(){
		opr.finish();
	}
	
}
