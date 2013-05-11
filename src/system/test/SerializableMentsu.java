package system.test;

import java.io.Serializable;
import java.util.ArrayList;

import system.hai.Hai;
import system.hai.Mentsu;
import system.hai.Mentsu.MentsuHai;

public class SerializableMentsu extends Mentsu implements Serializable {
	public SerializableMentsu(Mentsu m){
		
	}
	
	private class SerializableMentsuHai extends MentsuHai implements Serializable{
		public SerializableMentsuHai(Hai hai, boolean naki) {
			super(hai, naki);
		}
	}
}
