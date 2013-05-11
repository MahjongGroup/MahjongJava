package system.test;

import java.io.Serializable;

public class OrderPacket implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 721807507627329119L;
	private Order order;
	private int index;
	private Object arg;
	
	/**
	 * 命令送信用のデータ保存媒体
	 * @param order 送信する命令の種類
	 * @param index 送信する命令の第何引数を指定するか
	 * @param arg 引数の実態
	 */
	public OrderPacket(Order order,int index,Object arg){
		this.order = order;
		this.index = index;
		this.arg = arg;
	}
	
	public OrderPacket(Order order){
		this(order,0,null);
	}
	
	public Order getOrder(){
		return order;
	}

	public int getIndex() {
		return index;
	}

	public Object getArg() {
		return arg;
	}
	
}
