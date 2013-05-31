package test;

import system.hai.Hai;
import system.hai.MajanHai;

/**
 * a
 * @author shio
 *
 */
public class Test {
	public static void main(String[] args) {
		for (Hai hai : MajanHai.values()) {
			System.out.println(hai);
		}
		
		byte x = (byte) 0xc0;
		byte y = (byte) 0x40;
		System.out.println("0xc0 = " + x);
		System.out.println("0x40 = " + y);
		
		System.out.println(x & 0x43);
		
		
	}
}
