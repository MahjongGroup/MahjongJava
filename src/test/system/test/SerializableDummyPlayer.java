package test.system.test;

import java.io.Serializable;

import system.Player;

public class SerializableDummyPlayer extends Player{
	public SerializableDummyPlayer(int id, String name, boolean isMan) {
		super(id, name, isMan);
		// TODO Auto-generated constructor stub
	}
	public SerializableDummyPlayer(){
		super(-1,"",false);
	}
}
