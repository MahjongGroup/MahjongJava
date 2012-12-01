package client;

public enum StateCode {
	ERROR(-1),
	WAIT(0),
	SELECT_PON(102),
	SELECT_CHI(103),
	SELECT_ANKAN(104),
	SELECT_MINKAN(105),
	SELECT_KAKAN(106),
	DISCARD_HAI(1),

	
	//numは選択枚数
	DISCARD_SELECT(1),
	SELECT_KAKAN_HAI(1),
	SELECT_REACH_HAI(1),
	SELECT_PON_HAI(2),
	SELECT_CHI_HAI(2),
	SELECT_ANKAN_HAI(4),

	
	KYUSYUKYUHAI(107),
	SELECT_REACH(108),
	SELECT_RON(109),
	SELECT_TSUMO(110),
	SELECT_BUTTON(112),			//pon,kan,chi
	END(200);
	private int num;
	private StateCode(int num){
		this.num = num;
	}
	public int getNum(){
		return num;
	}
}
