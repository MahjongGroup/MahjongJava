package mahjong.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
	private List<Hai> tehai = new ArrayList<Hai>();
	private List<Hai> nakihai = new ArrayList<Hai>();
	private List<Hai> sutehai = new ArrayList<Hai>();
	private List<Hai> matihaiOfRon = new ArrayList<Hai>();
	private List<Hai> matihaiOfPon = new ArrayList<Hai>();
	private List<Hai> matihaiOfChi = new ArrayList<Hai>();
	private List<Hai> matihaiOfKan = new ArrayList<Hai>();

	private int score = 25000;
	private boolean isReach = false;
	private int KanCount = 0;
	
	private Hai getTehai(int index) {
		return tehai.get(index);
	}
	public List<Hai> getTehai() {
		return new ArrayList<Hai>(tehai);
	}
	public List<Hai> getNakihai(){
		return new ArrayList<Hai>(nakihai);
	}
	public List<Hai> getSutehai(){
		return new ArrayList<Hai>(sutehai);
	}
	public List<Hai> getMatihaiOfRon(){
		return new ArrayList<Hai>(matihaiOfRon);
	}
	public List<Hai> getMatihaiOfPon(){
		return new ArrayList<Hai>(matihaiOfPon);
	}	
	public List<Hai> getMatihaiOfChi(){
		return new ArrayList<Hai>(matihaiOfChi);
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
		if(index1 > index2)
			tehai.remove(index2);
		else
			tehai.remove(index2 - 1);
	}
	public Hai dahai(int index) {
		Hai hai = getTehai(index);
		sutehai.add(hai);
		tehai.remove(index);
		return hai;
	}
	/**
	 * 牌に関連するリストを空にする
	 * 局の始めに使用
	 */
	public void clearHais(){
		tehai.clear();
		nakihai.clear();
		sutehai.clear();
		matihaiOfRon.clear();
		matihaiOfPon.clear();
		matihaiOfKan.clear();
		matihaiOfChi.clear();
	}
	public void sortTehai(){
		Collections.sort(tehai);
		culcMati();
	}
	/**
	 * 聴牌しているかどうかを判別する
	 * clacMati()の後に使用しないと誤った判定となる
	 * @return 聴牌しているならtrue
	 */	
	public boolean isTempai(){
		return matihaiOfRon.size() != 0;
	}
	/**
	 * 待ち牌を計算する
	 * 具体的には槓,ロン,チー,ポンの待ちを計算し保存する
	 */
	private void culcMati(){
		/*
		 * ロンの待ちを計算
		 */
		matihaiOfRon.clear();
		for(HaiType hai:HaiType.values()){
			Hai tmp = new Hai(hai,false);
			if(AgariChecker.isAgari(getTehai(), tmp)){
				matihaiOfRon.add(tmp);
			}
		}
		/*
		 * ポンの待ちを計算
		 */
		matihaiOfPon.clear();
		List<HaiType> singleList = Hais.getSingleHaiList(tehai);
		for(HaiType type : singleList){
			int size = Hais.getHaiSize(tehai, type);
			if(size >= 2){
				matihaiOfPon.add(new Hai(type,false));
			}
		}
		/*
		 * 暗槓,明槓の待ちを計算
		 */
		matihaiOfKan.clear();
		for(HaiType type : singleList){
			int size = Hais.getHaiSize(tehai, type);
			if(size == 3){
				matihaiOfKan.add(new Hai(type,false));
			}
		}
	}
	
	/**
	 * リーチできるかどうかを判定する
	 * @return リーチできるならtrue
	 */
	public boolean isReachable(){
		return !isReach && nakihai.size() == 0 && isTempai();
	}
	
	public boolean isReach(){
		return isReach;
	}

	public void doReach(){
		isReach = true;
	}
	
	public void clearReach(){
		isReach = false;
	}
	
	public boolean isKanable(Hai hai){
		return matihaiOfKan.contains(hai);
	}
	
	
	public void doAnkan(int index){
		Hai Kampai = tehai.get(index);
		for(int i = 0;i<4;i++){
			nakihai.add(Kampai);
			tehai.remove(Kampai);
		}
		KanCount++;
	}
	
	public boolean isPonable(Hai hai){
		return matihaiOfPon.contains(hai);
/*
 		boolean result = false;
		List<HaiType> singleList = Hais.getSingleHaiList(tehai);
		for(HaiType type : singleList){
			int size = Hais.getHaiSize(tehai, type);
			if(size == 2){
				matihaiOfPon.add(new Hai(type,false));
				result = true;
			}
		}
		return result;
		*/
	}
	public boolean isMinkanable(){
		List<HaiType> singleList = Hais.getSingleHaiList(tehai);
		for(HaiType type : singleList){
			int size = Hais.getHaiSize(tehai, type);
			if(size == 3) return true;
		}
		return false;
	}
	public int getKanCount(){
		return KanCount;
	}
}
