package mahjong.client;


public enum Kaze {
	TON(0),NAN(1),SHA(2),PE(3);
	
	private final int kaze;

	Kaze(int kaze){
		this.kaze = kaze;
	}
	
	public static Kaze valueOf(int kaz) {
		for (Kaze kaze : values()) {
			if (kaze.kaze == kaz)
				return kaze;
		}
		throw new IllegalArgumentException("kaze : " + kaz);
	}

	
	
	Kaze getNextKaze(){
		if(0 <= kaze && kaze <= 2){
			return Kaze.valueOf(kaze+1);
		}
		return Kaze.valueOf(0);
	}
	
	
	int getKazeType(){
		return kaze;
	}
	
}
