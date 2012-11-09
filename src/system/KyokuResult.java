package system;

import java.util.HashMap;
import java.util.Map;

public class KyokuResult {

	private boolean isTotyuRyukyoku = false;
	private boolean isRyukyoku = true;
	private Player p[];

	/**
	 * 途中流局の場合trueを返す.
	 * 
	 * @return 途中流局の場合true.
	 */
	public boolean isTotyuRyukyoku() {
		return isTotyuRyukyoku;
	}

	public boolean isRyukyoku() {
		return isRyukyoku;
	}

	public Player[] getAgariPlayer() {
		return p;
	}
	
	public int getTenpaiSize() {
		return 1;
	}
	
	public Map<Player, Boolean> getTenpaiMap() {
		return new HashMap<Player, Boolean>();
	}
	
	public Player[] getTenpaiPlayer(){
		return p;
	}
	
	public Player getHojuPlayer(){
		return p[0];
	}
	
	public boolean isOyaAgari() {
		return false;
	}

	public boolean isOyaTempai() {
		return false;
	}

	public int getReachPlayerSize(){
		return 0;
	}

	

}
