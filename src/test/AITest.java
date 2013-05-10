package test;

import ai.AIMethods;
import system.agari.AgariMethods;
import system.hai.Hai;
import system.hai.HaiType;
import system.hai.MajanHai;
import system.hai.TehaiList;

public class AITest {
	public static void main(String[] args) {
		TehaiList tlist = new TehaiList();
		tlist.add(MajanHai.ITI_MAN);
		tlist.add(MajanHai.NI_MAN);
		tlist.add(MajanHai.SAN_MAN);
		tlist.add(MajanHai.YO_MAN);
		tlist.add(MajanHai.AKA_GO_MAN);
		tlist.add(MajanHai.ROKU_MAN);
		tlist.add(MajanHai.NANA_MAN);
		tlist.add(MajanHai.HATI_MAN);
		tlist.add(MajanHai.KYU_MAN);
		tlist.add(MajanHai.ITI_MAN);
		tlist.add(MajanHai.ITI_MAN);
		tlist.add(MajanHai.KYU_MAN);
	//	tlist.add(MajanHai.ITI_MAN);
		//ツモ牌
		tlist.add(MajanHai.KYU_MAN);
		
		System.out.println(tlist.toString());
		System.out.println(AIMethods.getInvalidHaiList(tlist));
		System.out.println(AIMethods.getExtendedValidHaiTypeSet(tlist));
		System.out.println(AgariMethods.getMachiHaiList(tlist, false));
	}
}
