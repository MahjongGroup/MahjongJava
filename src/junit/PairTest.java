package junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import util.Pair;

public class PairTest {

	@Test
	public void testContains() {
		Pair<Integer> p = new Pair<Integer>(2, 4);
		for (int i = 0; i < 10; i++) {
			switch (i) {
			case 2:
			case 4:
				assertTrue(p.contains(i));
				break;
			default:
				assertFalse(p.contains(i));
				break;
			}
		}
		Pair<Integer> p2 = new Pair<Integer>(4, 2);
		for (int i = 0; i < 10; i++) {
			switch (i) {
			case 2:
			case 4:
				assertTrue(p2.contains(i));
				break;
			default:
				assertFalse(p2.contains(i));
				break;
			}
		}
	}

	@Test
	public void testEqualsObject() {
		Pair<Integer> p1 = new Pair<Integer>(2, 4);
		Pair<Integer> p2 = new Pair<Integer>(2, 4);
		Pair<Integer> p3 = new Pair<Integer>(4, 2);
		Pair<Integer> p4 = new Pair<Integer>(2, 2);
		Pair<Integer> p5 = new Pair<Integer>(4, 4);
		Pair<Integer> p6 = new Pair<Integer>(1, 3);
		
		assertEquals(p1.equals(p1), true);

		assertEquals(p1.equals(p2), true);
		assertEquals(p2.equals(p1), true);
		assertEquals(p1.hashCode(), p2.hashCode());
		
		assertEquals(p1.equals(p3), true);
		assertEquals(p3.equals(p1), true);
		assertEquals(p1.hashCode(), p3.hashCode());
		
		assertEquals(p1.equals(p4), false);
		assertEquals(p4.equals(p1), false);
		
		assertEquals(p1.equals(p5), false);
		assertEquals(p5.equals(p1), false);
		
		assertEquals(p1.equals(p6), false);
		assertEquals(p6.equals(p1), false);
		
		assertEquals(p1.equals(null), false);
	}

	@Test
	public void testIterator() {
		Pair<Integer> p = new Pair<Integer>(2, 4);
		int equalSize = 0;
		for (Integer i : p) {
			if(i == 2)
				equalSize++;
			else if(i == 4)
				equalSize++;
		}
		assertEquals(equalSize, 2);
	}

}
