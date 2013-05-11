package test.system.test;

import java.io.Serializable;

import system.hai.HurohaiList;

public class SerializableHurohaiList extends HurohaiList implements
		Serializable {

	
	public SerializableHurohaiList(HurohaiList h) {
		super();
		for (int i = 0; i < h.size(); i++) {
			add(i, h.get(i));
		}
	}
}
