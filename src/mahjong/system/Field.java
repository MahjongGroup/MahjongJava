package mahjong.system;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Field {
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
		for(int i = 0;i < 4;i++)
			players.add(new Player());
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
		for(int i = 0;i < 4;i++){
			for(Player player:players){
				if(i != 3)
					for(int j = 0;j < 3;j++)
						player.tsumo(getHai());
				player.tsumo(getHai());
			}
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
		reach++;
	}

	public void doReach(){
		currentPlayer.doReach();
		reach++;
	}

	public boolean isAnkanable(){
		return currentPlayer.isAnkanable();
	}
	
	public void doAnkan(){
		Hai hai;
		currentPlayer.doAnkan();
		currentPlayer.tsumo(hai = wanpai.get(2 * (doras.size() - 1)));
		wanpai.set(wanpai.indexOf(hai), null);
		doras.add(wanpai.get(4 + 2 * doras.size()));
	}
	
	public void ryukyoku(){
		if(kyoku == 4)kyoku = 0;
		fieldWind.getNextKaze();
		kyoku++;
		honba = 0;
	}
	public Hai getHai(){
		Hai hai = yama.get(rand.nextInt(yama.size()));
		yama.remove(hai);
		return hai;
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
		return yama.size() + wanpai.size() == 14;
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
