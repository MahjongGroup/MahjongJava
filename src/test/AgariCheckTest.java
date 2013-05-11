package test;

import java.util.Arrays;

import mahjong.system.AgariChecker;
import mahjong.system.Hai;
import mahjong.system.HaiType;

public class AgariCheckTest {
	public static void main(String[] args) {
		Hai hais[] = {
				new Hai(HaiType.UPIN, true),
				new Hai(HaiType.UPIN, false),
				new Hai(HaiType.ITIMAN, false),
				new Hai(HaiType.ITIMAN, false),
				new Hai(HaiType.ITIMAN, false),
				new Hai(HaiType.ITIPIN, false),
				new Hai(HaiType.RYANPIN, false),
				new Hai(HaiType.SANPIN, false),
				new Hai(HaiType.KUMAN, false),
				new Hai(HaiType.KUMAN, false),
				new Hai(HaiType.NAN, false),
				new Hai(HaiType.NAN, false),
				new Hai(HaiType.NAN, false),
		};
		System.out.println(AgariChecker.isAgari(Arrays.asList(hais), new Hai(HaiType.NAN, false)));
	}
}
	