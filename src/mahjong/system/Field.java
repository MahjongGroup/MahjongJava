package mahjong.system;
import java.util.ArrayList;
import java.util.Random;

public class Field {
	private ArrayList<Hai> yama = new ArrayList<Hai>();
	private ArrayList<Hai> wanpai = new ArrayList<Hai>();
	private ArrayList<Player> players = new ArrayList<Player>();
	private ArrayList<Hai> doras = new ArrayList<Hai>();
	private HaiType fieldWind = HaiType.TON;
	private int kyoku = 0;
	private int honba = 0;
	private Random rand;
	private int reach = 0;
	
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
		doras.add(wanpai.get(3));
		for(int i = 0;i < 4;i++){
			for(Player player:players){
				if(i != 3)
					for(int j = 0;j < 3;j++)
						player.tsumo(getHai());
				player.tsumo(getHai());
			}
		}
	}
	public void renchan(){
		honba++;
	}
	public void ryukyoku(){
		if(kyoku == 4)kyoku = 0;
//		fieldWind.getNextKaze();
		kyoku++;
		honba = 0;
	}
	public Hai getHai(){
		Hai hai = yama.get(rand.nextInt(yama.size()));
		yama.remove(hai);
		return hai;
	}
}