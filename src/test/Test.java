package test;

import java.util.Set;

import system.hai.HaiType;
import system.yaku.Yakuman;

/**
 * a
 * @author shio
 *
 */
public class Test {
	public static void main(String[] args) {
		byte x = (byte) 0xc0;
		byte y = (byte) 0x40;
		System.out.println("0xc0 = " + x);
		System.out.println("0x40 = " + y);
		
		System.out.println(x & 0x43);
		
	}
}




