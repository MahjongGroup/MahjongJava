package test;

import java.util.Set;

import system.HaiType;
import system.Yakuman;

/**
 * a
 * @author shio
 *
 */
public class Test {
	public static void main(String[] args) {
		Set<HaiType> set = Yakuman.getKokusiSet();
		System.out.println(set);
		set.remove(HaiType.PE);
		System.out.println(set);
	}
}
