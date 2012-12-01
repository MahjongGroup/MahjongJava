package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static system.MajanHai.GO_MAN;
import static system.MajanHai.NI_MAN;
import static system.MajanHai.ROKU_MAN;
import static system.MajanHai.SAN_MAN;
import static system.MajanHai.TON;
import static system.MajanHai.YO_MAN;

import java.util.List;
import java.util.Set;

import org.junit.Test;

import system.HaiType;
import system.MajanHai;
import system.TehaiList;

public class TehaiListTest {

	/**
	 * 2,3,3,3,4,5,6,東,東,東,東というサンプルリストを生成して返す.
	 * 
	 * @return サンプルリスト.
	 */
	public TehaiList getSampleTehaiList() {
		TehaiList tlist = new TehaiList();
		tlist.add(NI_MAN);
		tlist.add(SAN_MAN);
		tlist.add(SAN_MAN);
		tlist.add(SAN_MAN);
		tlist.add(YO_MAN);
		tlist.add(GO_MAN);
		tlist.add(ROKU_MAN);
		tlist.add(TON);
		tlist.add(TON);
		tlist.add(TON);
		tlist.add(TON);
		return tlist;
	}

	@Test
	public void testSwap() {
		TehaiList tlist = new TehaiList();
		tlist.add(MajanHai.ITI_MAN);
		tlist.add(MajanHai.NI_MAN);
		tlist.add(MajanHai.SAN_MAN);

		tlist.swap(1, MajanHai.NI_PIN);
		assertEquals(tlist.size(), 3);
		assertEquals(tlist.get(0), MajanHai.ITI_MAN);
		assertEquals(tlist.get(1), MajanHai.NI_PIN);
		assertEquals(tlist.get(2), MajanHai.SAN_MAN);
	}

	@Test
	public void testIsPonable() {
		//2,3,3,3,4,5,6,東,東,東,東
		TehaiList tlist = getSampleTehaiList();
		assertEquals(tlist.isPonable(HaiType.ITI_MAN), false);
		assertEquals(tlist.isPonable(HaiType.NI_MAN), false);
		assertEquals(tlist.isPonable(HaiType.SAN_MAN), true);
		assertEquals(tlist.isPonable(HaiType.TON), true);
	}

	@Test
	public void testGetPonableIndexList() {
		//2,3,3,3,4,5,6,東,東,東,東
		TehaiList tlist = getSampleTehaiList();
		for (HaiType haiType : HaiType.values()) {
			List<List<Integer>> ponList = tlist.getPonableIndexList(haiType);
			int trueSize = 0;
			switch (haiType) {
			case SAN_MAN:
				assertEquals(ponList.size(), 3);
				for (int i = 0; i < ponList.size(); i++) {
					assertEquals(ponList.get(i).size(), 2);
					if (ponList.get(i).contains(1)) {
						if (ponList.get(i).contains(2)) {
							trueSize++;
						}
						if (ponList.get(i).contains(3)) {
							trueSize++;
						}
					} else {
						if (ponList.get(i).contains(2)) {
							if (ponList.get(i).contains(3)) {
								trueSize++;
							}
						}
					}
				}
				assertEquals(trueSize, 3);
				break;
			case TON:
				assertEquals(ponList.size(), 6);
				for (int i = 0; i < ponList.size(); i++) {
					assertEquals(ponList.get(i).size(), 2);
					if (ponList.get(i).contains(7)) {
						if (ponList.get(i).contains(8))
							trueSize++;
						if (ponList.get(i).contains(9))
							trueSize++;
						if (ponList.get(i).contains(10))
							trueSize++;
					} else if(ponList.get(i).contains(8)) {
						if (ponList.get(i).contains(9))
							trueSize++;
						if (ponList.get(i).contains(10))
							trueSize++;
					} else if(ponList.get(i).contains(9)) {
						if (ponList.get(i).contains(10))
							trueSize++;
					}
				}
				assertEquals(trueSize, 6);
				break;
			default:
				assertEquals(ponList.size(), 0);
				break;
			}
		}
	}

	@Test
	public void testIsMinkanable() {
		//2,3,3,3,4,5,6,東,東,東,東
		TehaiList tlist = getSampleTehaiList();
		assertTrue(tlist.isMinkanable(HaiType.SAN_MAN));
		assertTrue(tlist.isMinkanable(HaiType.TON));
		assertFalse(tlist.isMinkanable(HaiType.NI_MAN));
	}

	@Test
	public void testGetMinkanableIndexList() {
		//2,3,3,3,4,5,6,東,東,東,東
		TehaiList tlist = getSampleTehaiList();
		fail("Not yet implemented");
	}

	@Test
	public void testIsAnkanableHaiHaiType() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsAnkanableHai() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAnkanableIndexList() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetChiableHaiList() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsChiable() {
		fail("Not yet implemented");
	}

	@Test
	public void testToHaiTypeList() {
		fail("Not yet implemented");
	}

	@Test
	public void testToHaiTypeSet() {
		//2,3,3,3,4,5,6,東,東,東,東
		TehaiList tlist = getSampleTehaiList();
		Set<HaiType> set = tlist.toHaiTypeSet();
		assertEquals(set.size(), 6);
		for (HaiType haiType : HaiType.values()) {
			switch (haiType) {
			case NI_MAN:
			case SAN_MAN:
			case YO_MAN:
			case GO_MAN:
			case ROKU_MAN:
			case TON:
				assertTrue(set.contains(haiType));
				break;
			default:
				assertFalse(set.contains(haiType));
				break;
			}
		}
	}

	@Test
	public void testSizeOf() {
		//2,3,3,3,4,5,6,東,東,東,東
		TehaiList tlist = getSampleTehaiList();
		for (HaiType haiType : HaiType.values()) {
			int size = tlist.sizeOf(haiType);
			switch (haiType) {
			case NI_MAN:
				assertEquals(size, 1);
				break;
			case SAN_MAN:
				assertEquals(size, 3);
				break;
			case YO_MAN:
				assertEquals(size, 1);
				break;
			case GO_MAN:
				assertEquals(size, 1);
				break;
			case ROKU_MAN:
				assertEquals(size, 1);
				break;
			case TON:
				assertEquals(size, 4);
				break;
			default:
				assertEquals(size, 0);
				break;
			}
		}
	}

}
