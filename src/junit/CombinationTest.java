package junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import util.Combination;

public class CombinationTest {

	@Test
	public void testValue() {
		assertEquals(Combination.calc(1, 1), 1);
		assertEquals(Combination.calc(1, 0), 1);
		assertEquals(Combination.calc(2, 0), 1);
		assertEquals(Combination.calc(100, 0), 1);
		assertEquals(Combination.calc(100, 99), 100);
		assertEquals(Combination.calc(3, 2), 3);
		assertEquals(Combination.calc(34, 2), 561);
		assertEquals(Combination.calc(211,3), 1543465);
	}
	
	@Test
	public void test() {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 1; i < 10; i++) {
			list.add(1);
			for (int j = 1; j < i + 1; j++) {
				Combination<Integer> c = new Combination<Integer>(list, j);
				assertTrue(c.size() == Combination.calc(i, j));
			}
		}
		list.clear();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		Combination<Integer> c = new Combination<Integer>(list, 3);
		List<List<Integer>> test = new ArrayList<List<Integer>>();
		test.add(Arrays.asList(new Integer[]{1,2,3}));
		test.add(Arrays.asList(new Integer[]{1,2,4}));
		test.add(Arrays.asList(new Integer[]{1,2,5}));
		test.add(Arrays.asList(new Integer[]{1,3,4}));
		test.add(Arrays.asList(new Integer[]{1,3,5}));
		test.add(Arrays.asList(new Integer[]{1,4,5}));
		test.add(Arrays.asList(new Integer[]{2,3,4}));
		test.add(Arrays.asList(new Integer[]{2,3,5}));
		test.add(Arrays.asList(new Integer[]{2,4,5}));
		test.add(Arrays.asList(new Integer[]{3,4,5}));
		for (List<Integer> l : c) {
			for (Iterator<List<Integer>> itr = test.iterator(); itr.hasNext();) {
				List<Integer> elem = itr.next();
				if(elem.containsAll(l)) {
					itr.remove();
				}
			}
		}
		assertEquals(test.size(), 0);
	}

}
