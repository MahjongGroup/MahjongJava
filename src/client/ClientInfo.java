package client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import system.Hai;
import system.Kaze;
import system.MajanHai;
import system.Mentu;
import system.Player;

public class ClientInfo {
	List<Hai> tehai;

	// 　自分->0, 上家->1,,,,
	Map<Player, Integer> sekiMap;
	Map<Integer, List<Hai>> sutehaiMap;
	Map<Integer,Integer> scoreMap;
	Map<Integer,Integer> IDMap;
	Map<Integer,String> nameMap;
	Map<Integer, List<Mentu>> hurohaiMap;
	Map<Integer, Integer> tehaiSizeMap;
	Map<StateCode, List<List<Integer>>> ableIndexList;
	Map<Integer, Integer> reachPosMap;
	List<List<Integer>> chiableIndexLists;
	List<List<Integer>> ponableIndexLists;
	List<List<Integer>> ankanableIndexList;
	List<List<Integer>> kakanableIndexList;
	List<Integer> reachableIndexList;
	List<Integer> selectedIndexes;
	Hai sutehai;
	int tsumiBou;
	public Player[] players;
	int[] scores;

	public List<Hai> doraList;
	int honba;
	Kaze bakaze;
	int playerNumber;
	int finish;
	
	int yamaSize;
	int wanpaiSize;

	Hai tsumoHai;
	int currentTurn;
	int kyokusu;
	public Map<Kaze, Integer> kaze;
	
	{
		scoreMap = Collections.synchronizedMap(new HashMap<Integer, Integer>());
	}

	public void resetBeforeKyoku(){
		sutehai = null;
		tsumiBou = 0;
		doraList = new ArrayList<Hai>();
		honba = 0;
		bakaze = null;
		finish = 0;
		yamaSize = 0;
		wanpaiSize = 0;
		tsumoHai = null;
		currentTurn = 0;
		this.scores = new int[4];
		this.tehai = Collections.synchronizedList(new ArrayList<Hai>());
		this.sutehaiMap = Collections
				.synchronizedMap(new HashMap<Integer, List<Hai>>());
		this.hurohaiMap = Collections
				.synchronizedMap(new HashMap<Integer, List<Mentu>>());
		this.tehaiSizeMap = Collections
				.synchronizedMap(new HashMap<Integer, Integer>());
		this.tsumoHai = MajanHai.ITI_MAN;
		this.selectedIndexes = new ArrayList<Integer>();
		this.chiableIndexLists = new ArrayList<List<Integer>>();
		this.ponableIndexLists = new ArrayList<List<Integer>>();
		this.ankanableIndexList = new ArrayList<List<Integer>>();
		this.kakanableIndexList = new ArrayList<List<Integer>>();
		int dice = 2 + (int)(Math.random() * 10);
		finish = (17 * (dice % 4) + (7 - dice) + 67) % 68;
		//東の左手側が0
		//yama:反時計周りに描画
		//dora:時計周りに描画
		
		reachPosMap = Collections
				.synchronizedMap(new HashMap<Integer, Integer>());

		reachPosMap.put(0, null);
		reachPosMap.put(1, null);
		reachPosMap.put(2, null);
		reachPosMap.put(3, null);
		
		ableIndexList = Collections
				.synchronizedMap(new HashMap<StateCode, List<List<Integer>>>());
		ableIndexList.put(StateCode.SELECT_PON_HAI, ponableIndexLists);
		ableIndexList.put(StateCode.SELECT_CHI_HAI, chiableIndexLists);
		ableIndexList.put(StateCode.SELECT_ANKAN_HAI, ankanableIndexList);
		ableIndexList.put(StateCode.SELECT_KAKAN_HAI, kakanableIndexList);
		List<List<Integer>> tmpList = new ArrayList<List<Integer>>();
		tmpList.add(reachableIndexList);
		ableIndexList.put(StateCode.SELECT_REACH_HAI, tmpList);
		ableIndexList.put(StateCode.SELECT_MINKAN,
				new ArrayList<List<Integer>>());

		this.tehai.add(MajanHai.ITI_MAN);
		this.tehai.add(MajanHai.ITI_MAN);
		this.tehai.add(MajanHai.ITI_MAN);
		this.tehai.add(MajanHai.NI_MAN);
		this.tehai.add(MajanHai.SAN_MAN);
		this.tehai.add(MajanHai.YO_MAN);
		this.tehai.add(MajanHai.GO_MAN);
		this.tehai.add(MajanHai.ROKU_MAN);
		this.tehai.add(MajanHai.NANA_MAN);
		this.tehai.add(MajanHai.HATI_MAN);
		this.tehai.add(MajanHai.KYU_MAN);
		this.tehai.add(MajanHai.KYU_MAN);
		this.tehai.add(MajanHai.HAKU);
		for (int i = 0; i < 4; i++)
			this.hurohaiMap.put(i, new ArrayList<Mentu>());

		this.reachableIndexList = new ArrayList<Integer>();
		this.tehaiSizeMap.put(0, 13);
		this.tehaiSizeMap.put(1, 13);
		this.tehaiSizeMap.put(2, 13);
		this.tehaiSizeMap.put(3, 13);
		for (int i = 0; i < 4; i++)
			this.sutehaiMap.put(i, new ArrayList<Hai>());
	}
	
	public ClientInfo(){
		resetBeforeKyoku();
		this.kaze = Collections.synchronizedMap(new HashMap<Kaze, Integer>());
		this.scoreMap = Collections.synchronizedMap(new HashMap<Integer, Integer>());
		this.nameMap = Collections.synchronizedMap(new HashMap<Integer, String>());
		this.IDMap = Collections.synchronizedMap(new HashMap<Integer, Integer>());
		this.players = new Player[4];
		for(int i = 0;i < 4;i++){
			scoreMap.put(i, 0);
		}
		for(int i = 0;i < 4;i++){
			nameMap.put(i, "");
		}
		for(int i = 0;i < 4;i++){
			IDMap.put(i, 0);
		}
	}
	
	public ClientInfo(int index) {
		this();
		setIndex(index);
	}
	
	public void setIndex(int index){
		this.currentTurn = (4 - index) % 4;
		this.kaze.put(Kaze.TON, (4 - index) % 4);
		this.kaze.put(Kaze.NAN, (5 - index) % 4);
		this.kaze.put(Kaze.SYA, (6 - index) % 4);
		this.kaze.put(Kaze.PE, (7 - index) % 4);
		this.finish = (finish + kaze.get(Kaze.TON)) % 68;
	}
	
	public void setID(int key,int id){
		IDMap.put(key,id);
	}
	public void setID(Kaze kaze,int id){
		IDMap.put(this.kaze.get(kaze),id);
	}
	public void setName(int key,String accountName){
		nameMap.put(key,accountName);
	}
	public void setName(Kaze kaze,String accountName){
		nameMap.put(this.kaze.get(kaze),accountName);
	}
	public void setScore(int key,int score){
		scoreMap.put(key,score);
	}
	public void setScore(Kaze kaze,int score){
		scoreMap.put(this.kaze.get(kaze),score);
	}
}
