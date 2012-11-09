package test;

import static system.MajanHai.AKA_GO_MAN;
import static system.MajanHai.NI_PIN;
import static system.MajanHai.SAN_MAN;
import static system.MajanHai.SAN_PIN;
import static system.MajanHai.TON;
import static system.MajanHai.YO_MAN;
import static system.MajanHai.YO_PIN;

import java.util.ArrayList;
import java.util.List;

import system.CheckerParam;
import system.HaiType;
import system.MajanHai;
import system.MatiType;
import system.Mentu;


public class Main {
	public static void main(String[] args) {
		
		Mentu m0 = new Mentu(MajanHai.ITI_MAN, SAN_MAN, MajanHai.NI_MAN);
		Mentu m1 = new Mentu(TON, TON, TON);
		Mentu m2 = new Mentu(NI_PIN, SAN_PIN, YO_PIN);
		Mentu m3 = new Mentu(MajanHai.ITI_PIN, SAN_PIN, NI_PIN);
//		Mentu m4 = new Mentu(MajanHai.YO_PIN, MajanHai.SAN_PIN, MajanHai.GO_PIN);
		Mentu m4 = new Mentu(MajanHai.NI_SOU, MajanHai.SAN_SOU, MajanHai.YO_SOU);
		Mentu m5 = new Mentu(MajanHai.NI_SOU, MajanHai.NI_SOU, MajanHai.NI_SOU);
		
		List<Mentu> mentuList = new ArrayList<Mentu>(4);
		mentuList.add(m0);
		mentuList.add(m1);
		mentuList.add(m2);
		mentuList.add(m3);
		mentuList.add(m4);
		mentuList.add(m5);
		
		CheckerParam param = new CheckerParam();
		param.setTsumo(true);
		param.setMentuList(mentuList);
		param.setJanto(HaiType.NAN);
		param.setAgariHai(MajanHai.NI_SOU);

		for (MatiType m : MatiType.values()) {
			System.out.println(m + " : " + m.check(param));
		}
		
	}
}
