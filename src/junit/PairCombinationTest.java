package junit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import util.PairCombination;
import util.Pair;

public class PairCombinationTest {

	@Test
	public void testInt() {
		List<Integer> list1 = new ArrayList<Integer>();
		list1.add(1);
		list1.add(2);
		list1.add(3);
		list1.add(4);
		List<Pair<Integer>> comb1 = PairCombination.getCombination(list1.toArray(new Integer[0]));
		assertEquals(comb1.size(), 6);
		assertTrue(comb1.contains(new Pair<Integer>(1,2)));
		assertTrue(comb1.contains(new Pair<Integer>(2,1)));
		assertTrue(comb1.contains(new Pair<Integer>(1,3)));
		assertTrue(comb1.contains(new Pair<Integer>(1,4)));
		assertTrue(comb1.contains(new Pair<Integer>(2,3)));
		assertTrue(comb1.contains(new Pair<Integer>(2,4)));
		assertTrue(comb1.contains(new Pair<Integer>(3,4)));
		assertFalse(comb1.contains(new Pair<Integer>(1,1)));
		assertFalse(comb1.contains(new Pair<Integer>(2,2)));
		assertFalse(comb1.contains(new Pair<Integer>(3,3)));
		assertFalse(comb1.contains(new Pair<Integer>(4,4)));
	}

	@Test
	public void testBigInt() {
		for (int i = 0; i < 100; i++) {
			List<Integer> list = new ArrayList<Integer>();
			for (int j = 0; j < i; j++) {
				list.add(1);
			}
			
			List<Pair<Integer>> comb = PairCombination.getCombination(list.toArray(new Integer[0]));
			assertEquals(comb.size(), i*(i-1)/2);
		}
	}

	@Test
	public void testString() {
		List<String> list1 = new ArrayList<String>();
		list1.add("a");
		list1.add("b");
		list1.add("c");
		list1.add("d");
		List<Pair<String>> comb1 = PairCombination.getCombination(list1.toArray(new String[0]));
		assertEquals(comb1.size(), 6);
		assertTrue(comb1.contains(new Pair<String>("a","b")));
		assertTrue(comb1.contains(new Pair<String>("b","a")));
		assertTrue(comb1.contains(new Pair<String>("a","c")));
		assertTrue(comb1.contains(new Pair<String>("a","d")));
		assertTrue(comb1.contains(new Pair<String>("b","c")));
		assertTrue(comb1.contains(new Pair<String>("b","d")));
		assertTrue(comb1.contains(new Pair<String>("c","d")));
		assertFalse(comb1.contains(new Pair<String>("a","a")));
		assertFalse(comb1.contains(new Pair<String>("b","b")));
		assertFalse(comb1.contains(new Pair<String>("c","c")));
		assertFalse(comb1.contains(new Pair<String>("d","d")));
	}

}
