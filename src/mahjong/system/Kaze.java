package mahjong.system;

public enum Kaze {
	TON(0),NAN(1),SHA(2),PE(3);
	
	private final int kaze;

	Kaze(int kaze){
		this.kaze = kaze;
	}
	
	int getNextKaze(){
		if(0 <= kaze && kaze <= 2){
			return kaze+1;
		}
		return 0;
	}
	
	int getKazeType(){
		return kaze;
	}
	
}
