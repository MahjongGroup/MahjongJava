package mahjong.system;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Field {
	static public Scanner stdIn = new Scanner(System.in);
	private ArrayList<Hai> yama = new ArrayList<Hai>();
	private ArrayList<Hai> wanpai = new ArrayList<Hai>();
	private ArrayList<Player> players = new ArrayList<Player>();
	private ArrayList<Hai> doras = new ArrayList<Hai>();
	private Kaze fieldWind = Kaze.TON;
	private int kyoku = 0;
	private int honba = 0;
	private Random rand;
	private int reach = 0;
	private Player currentPlayer;

	public Field(){
		rand = new Random();
		for(int i = 0;i < 4;i++)
			players.add(new Player());
		currentPlayer = players.get(0);
	}
	public static void main(String str[]){
		Field field = new Field();
		field.chipai();
		field.sortTehai();
		while(field.isRyukyoku()){
			field.printField();
			System.out.println(field.tsumo());
			field.dahai(stdIn.nextInt());
			field.nextPlayer();
		}
	}
	
	public void printField(){
		for(Player player:players){
			if(player == currentPlayer)System.out.print("*");
			for(int i = 0;i < 13;i++){
				System.out.print(" " + player.getTehai(i));
			}
			System.out.println();
		}
	}
	
	public void nextPlayer(){
		currentPlayer = players.get((players.indexOf(currentPlayer) + 1)%4);
	}
	
	public void chipai(){
		for(HaiType haiType:HaiType.values()){
			if(haiType == HaiType.UMAN || haiType == HaiType.UPIN || haiType == HaiType.USOU)
				yama.add(new Hai(haiType,true));
			else
				yama.add(new Hai(haiType,false));
			for(int i = 0;i < 3;i++)
				yama.add(new Hai(haiType,false));
		}
		for(int i = 0;i < 14;i++){
			wanpai.add(getHai());
		}
		doras.add(wanpai.get(4));
		for(Player player:players){
			for(int j = 0;j < 13;j++)
				player.tsumo(getHai());
			player.clearReach();
		}
	}

	public void sortTehai(){
		for(Player player:players){
			player.sortTehai();
		}
	}

	public Hai tsumo(){
		Hai hai = getHai();
		currentPlayer.tsumo(hai);
		return hai;
	}
	
	public List<Hai> getTehai(){
		return currentPlayer.makeTehai();
	}
	
	public void renchan(){
		honba++;
	}
	
	public boolean isReach(){
		return currentPlayer.isReach();
	}

	public void dahai(int index){
		currentPlayer.dahai(index);
		currentPlayer.sortTehai();
	}

	public void doReach(){
		currentPlayer.doReach();
		reach++;
	}

	public boolean isAnkanable(){
		return currentPlayer.isAnkanable();
	}
	
	public void doAnkan(int index){
		Hai hai;
		currentPlayer.doAnkan(index);
		currentPlayer.tsumo(hai = wanpai.get(2 * (doras.size() - 1)));
		wanpai.set(wanpai.indexOf(hai), null);
		doras.add(wanpai.get(4 + 2 * doras.size()));
	}
	
	public void oyaNagare(){
		if(kyoku == 4){
			kyoku = 0;
			fieldWind = fieldWind.getNextKaze();
		}
		kyoku++;
		honba = 0;
	}
	
	public Hai getHai(){
		Hai hai = yama.get(rand.nextInt(yama.size()));
		yama.remove(hai);
		return hai;
	}
	
	public boolean isSuhurenda(){
		HaiType previous = null;
		HaiType current = null;
		for(Player player:players){
			current = player.getSutehai(0).getType();
			if(previous == null)previous = current;
			if(previous != current)return false;
			previous = current;
		}
		if(current != HaiType.TON || current != HaiType.NAN ||
				current != HaiType.SYA || current != HaiType.PE)return false;
		return true;
	}
	
	public boolean isSukaikan(){
		int kanCount = 0;
		int numberOfPerson = 0;
		for(Player player:players){
			if(player.getKanCount() != 0){
				kanCount+=player.getKanCount();
				numberOfPerson++;
			}
		}
		return (kanCount > 3 && numberOfPerson > 1);
	}
	
	public int getReachStick(){
		int reach_point = reach;
		reach = 0;
		return reach_point;
	}

	public int getHonba(){
		return honba;
	}
	
	public Hai RinsyanTsumo(){
		Hai hai = wanpai.get((doras.size() - 1) * 2);
		wanpai.set(wanpai.indexOf(hai), null);
		return hai;
	}

	public boolean isRyukyoku(){
		return !(yama.size() + wanpai.size() == 14);
	}

//	public boolean isSyuukyoku(){
//		boolean syuukyoku = true;
//		Hai card;
//		for(Player player:players){
//			player.tsumo(card = getHai());
//			if(player.checkAgari()){
//				winner = player;
//				syuukyoku = false;
//				break;
//			}
//			if(player.isTempai())
//				player.doReach();
//			if(player.isAnkanable()){
//				player.doAnkan();
//				player.tsumo(card = wanpai.get(2 * (doras.size() - 1)));
//				wanpai.set(wanpai.indexOf(card), null);
//				doras.add(wanpai.get(4 + 2 * doras.size()));
//			}
//			if(player.isKakanable()){
//				
//			}
//			player.isReach();
//		}
//		return syuukyoku;
//	}
}
