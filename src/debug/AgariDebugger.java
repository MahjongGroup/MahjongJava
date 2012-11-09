package debug;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import system.CheckerParam;
import system.Kaze;
import system.MajanHai;
import system.Rule;
import system.Yaku;

public class AgariDebugger {
	public static void main(String[] args) throws IOException {
		CheckerParam cp = new CheckerParam();
		cp.setAgariHai(MajanHai.GO_SOU);
		cp.setBakaze(Kaze.TON);
		cp.setFlagCheckYakuSet(new HashSet<Yaku>(0));
		cp.setJikaze(Kaze.NAN);
		cp.setNaki(false);
		cp.setTsumo(true);
		cp.setRule(new Rule());
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		Pattern p = Pattern.compile("(.+)\\s(.*)\\s(.*)");
		Matcher m = p.matcher(reader.readLine());
		if(m.matches()) {
			System.out.println(m.group(1));
			System.out.println(m.group(2));
		}
		
//		Set<Yaku> yakuSet = AgariFunctions.checkYaku(tehai0, new HurohaiList(0), cp);
//		System.out.println(yakuSet);

	}
}
