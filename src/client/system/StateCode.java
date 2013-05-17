package client.system;

public enum StateCode {
	ERROR(-1,""),
	WAIT(0,""),
	SELECT_PON(102,"ポン"),
	SELECT_CHI(103,"チー"),
	SELECT_ANKAN(104,"暗槓"),
	SELECT_MINKAN(105,"明槓"),
	SELECT_KAKAN(106,"加槓"),
	SELECT_KAN(1000,"槓"),
	DISCARD_HAI(1,""),
 
	
	//numは選択枚数
	DISCARD_SELECT(1,""),
	SELECT_KAKAN_HAI(1,""),
	SELECT_REACH_HAI(1,""),
	SELECT_PON_HAI(2,""),
	SELECT_CHI_HAI(2,""),
	SELECT_ANKAN_HAI(4,""),

	
	KYUSYUKYUHAI(107,"九種九牌"),
	SELECT_REACH(108,"リーチ"),
	SELECT_RON(109,"ロン"),
	SELECT_TSUMO(110,"ツモ"),
	SELECT_BUTTON(112,""),			//pon,kan,chi
	END(200,""),
	DRAW_ANIME(300,"");
	private int num;
	private String name;
	private StateCode(int num,String name){
		this.num = num;
		this.name = name;
	}
	public String getButtonName(){
		return name;
	}
	public int getNum(){
		return num;
	}
}
