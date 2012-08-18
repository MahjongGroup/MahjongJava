package mahjong.system;

enum HaiType {
	ITIMAN(0), RYANMAN(1), SANMAN(2), SUMAN(3), UMAN(4), ROMAN(5), TIMAN(6), PAMAN(7), KUMAN(8),
	ITIPIN(9), RYANPIN(10), SANPIN(11), SUPIN(12), UPIN(13), ROPIN(14), TIPIN(15), PAPIN(16), KUPIN(17),
	ITISOU(18), RYANSOU(19), SANSOU(20), SUSOU(21), USOU(22), ROSOU(23), TISOU(24), PASOU(25), KUSOU(26),
	TON(27), NAN(28), SYA(29), PE(30),
	HAKU(31), HATSU(32), TYUN(33);
	
	private final int type;
	
	private HaiType(int type){
		this.type = type;
	}
	
	public boolean isSuhai(int x){
		
		if(x >= 0 && x <= 26){
			return true;
		}
		return false;
	}
	
	
	
	public int getNumber(int x){
		if(x >= 0 && x <= 26){
			return x%9+1;
		}
		throw new IllegalArgumentException(Integer.toString(x));
	}
	
	public boolean isTsuhai(int x){
		if(x >= 27 && x <= 33){
		return true;
		}	
	return false;
	}
	
	public boolean isSangenhai(int x){
		if(x >= 31 && x <= 33){
			return true;
		}
		return false;
	}
	
	public boolean isKazehai(int x){
		if(x >= 27 && x <= 30){
			return true;
		}
		return false;
	}
}
