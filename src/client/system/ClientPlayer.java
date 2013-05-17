package client.system;

import system.Player;
import system.hai.HurohaiList;
import system.hai.Kaze;
import system.hai.SutehaiList;

public class ClientPlayer {
	private final int id;
	private final String name;
	private HurohaiList hurohaiList;
	private Kaze kaze;
	private int score;
	private int reachPos;
	private SutehaiList sutehaiList;
	private int tehaiSize;

	
	
	private int index;
	
	public HurohaiList getHurohaiList() {
		return hurohaiList;
	}

	public int getReachPos() {
		return reachPos;
	}

	public SutehaiList getSutehaiList() {
		return sutehaiList;
	}

	public int getTehaiSize() {
		return tehaiSize;
	}

	public void setHurohaiList(HurohaiList hurohaiList) {
		this.hurohaiList = hurohaiList;
	}

	public void setReachPos(int reachPos) {
		this.reachPos = reachPos;
	}

	public void setSutehaiList(SutehaiList sutehaiList) {
		this.sutehaiList = sutehaiList;
	}

	public void setTehaiSize(int tehaiSize) {
		this.tehaiSize = tehaiSize;
	}



	public Player getPlayer(){
		return new Player(id,name,false);
	}
		
	public ClientPlayer(Player player){
		this(player.getId(),player.getName());
	}
	
	public ClientPlayer(int id,String name,int score,int index){
		this(id,name,score);
		this.index = index;
	}
	
	
	public ClientPlayer(int id,String name,int score,Kaze kaze){
		this(id,name,score);
		this.kaze = kaze;
	}
	
	public ClientPlayer(int id,String name,int score){
		this(id,name);
		this.score = score;
	}
	
	public ClientPlayer(int id,String name){
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}

	public int getIndex() {
		return index;
	}

	public Kaze getKaze() {
		return kaze;
	}

	public String getName() {
		return name;
	}

	public int getScore() {
		return score;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setKaze(Kaze kaze) {
		this.kaze = kaze;
	}


	public void setScore(int score) {
		this.score = score;
	}
}
