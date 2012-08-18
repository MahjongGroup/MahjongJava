package mahjong.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
	private List<Hai> tehai = new ArrayList<Hai>();
	private List<Hai> nakihai = new ArrayList<Hai>();
	private List<Hai> sutehai = new ArrayList<Hai>();

	private int score = 25000;
	private boolean isReach = false;
	
	public Hai getTehai(int index) {
		return tehai.get(index);
	}
	
	public List<Hai> makeTehai() {
		return new ArrayList<Hai>(tehai);
	}


	public Hai getNakihai(int index) {
		return nakihai.get(index);
	}

	public Hai getSutehai(int index) {
		return sutehai.get(index);
	}

	public int getScore() {
		return score;
	}

	public void culcScore(int index){
		score = score+index;
	}
	
	public void tsumo(Hai hai) {
		tehai.add(hai);
	}

	public void naki(Hai hai, int index1, int index2) {

		nakihai.add(hai);
		nakihai.add(getTehai(index1));
		nakihai.add(getTehai(index2));
		tehai.remove(index1);
		tehai.remove(index2);
	}

	public void dahai(int index) {
		sutehai.add(getTehai(index));
		tehai.remove(index);
	}
	public void tehaiclear(){
		tehai.clear();
	}
	public void nakihaiclear(){
		nakihai.clear();
	}
	public void sutehaiclear(){
		sutehai.clear();
	}
	public void sortTehai(){
		Collections.sort(tehai);
	}
	
	public boolean isReach(){
		return this.isReach;
	}
	public void doReach(){
		isReach = true;
	}
	
}
