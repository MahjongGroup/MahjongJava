package test.system.test;

import java.io.Serializable;
import system.Player;

public class SerializablePlayer extends Player implements Serializable {
	public SerializablePlayer(Player p){
		super(p.getId(), p.getName(), false);
	}
	
	public SerializablePlayer(int id, String name, boolean isMan) {
		super(id, name, isMan);
	}

}
