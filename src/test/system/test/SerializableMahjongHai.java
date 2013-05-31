package test.system.test;

import java.io.Serializable;

import system.hai.Hai;
import system.hai.HaiType;
import system.hai.MajanHai;

public class SerializableMahjongHai implements Serializable {
	private int haiTypeNumber;
	private boolean akaFlag;

	public SerializableMahjongHai(MajanHai hai) {
		this(hai.type().id(), hai.aka());
	}

	public SerializableMahjongHai() {
		this(-1, false);
	}

	public SerializableMahjongHai(int haiTypeNumber, boolean akaFlag) {
		setAkaFlag(akaFlag);
		setHaiTypeNumber(haiTypeNumber);
	}

	public int getHaiTypeNumber() {
		return haiTypeNumber;
	}

	public boolean isAkaFlag() {
		return akaFlag;
	}

	public void setHaiTypeNumber(int haiTypeNumber) {
		this.haiTypeNumber = haiTypeNumber;
	}

	public void setAkaFlag(boolean akaFlag) {
		this.akaFlag = akaFlag;
	}
	
	public Hai getMajanHai(){
		HaiType type = null;
		for(HaiType ht:HaiType.values()){
			if(ht.id() == haiTypeNumber){
				type = ht;
				break;
			}
		}
		return MajanHai.valueOf(type, akaFlag);
	}
}
