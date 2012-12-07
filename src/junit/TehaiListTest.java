package junit;

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
import static system.MajanHai.NAN;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import system.Hai;
import system.HaiType;
import system.MajanHai;
import system.TehaiList;

public class TehaiListTest {

	/**
	 * 2,3,3,3,4,5,6,東,東,東,東,南,南というサンプルリストを生成して返す.
	 * 
	 * @return サンプルリスト.
	 */
	public TehaiList getSampleTehaiList() {
		TehaiList tlist = new TehaiList(Arrays.asList(new Hai[] { NI_MAN, SAN_MAN, SAN_MAN, SAN_MAN, YO_MAN, GO_MAN, ROKU_MAN, TON, TON, TON, TON, NAN, NAN }));
		return tlist;
	}

	@Test
	public void testTehaiList() {
		//2,3,3,3,4,5,6,東,東,東,東,南,南
		TehaiList tlist = getSampleTehaiList();
		assertEquals(tlist.size(), 13);
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
		//2,3,3,3,4,5,6,東,東,東,東,南,南
		TehaiList tlist = getSampleTehaiList();
		assertEquals(tlist.isPonable(HaiType.ITI_MAN), false);
		assertEquals(tlist.isPonable(HaiType.NI_MAN), false);
		assertEquals(tlist.isPonable(HaiType.SAN_MAN), true);
		assertEquals(tlist.isPonable(HaiType.TON), true);
	}

	@Test
	public void testGetPonableIndexList() {
		//2,3,3,3,4,5,6,東,東,東,東,南,南
		TehaiList tlist = getSampleTehaiList();
		for (HaiType haiType : HaiType.values()) {
			List<List<Integer>> ponList = tlist.getPonableIndexList(haiType);
			int trueSize = 0;
			if (haiType == HaiType.SAN_MAN) {
				assertTrue(tlist.isPonable(haiType));
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
			} else if (haiType == HaiType.SAN_MAN) {

			} else {
				assertFalse(tlist.isPonable(haiType));
				assertEquals(ponList.size(), 0);
			}
		}
	}

	@Test
	/**
	 * 明槓できるためには指定された牌種を３枚持つ
	 */
	public void testIsMinkanable() {
		//2,3,3,3,4,5,6,東,東,東,東,南,南
		TehaiList tlist = getSampleTehaiList();
		assertTrue(tlist.isMinkanable(HaiType.SAN_MAN));
		assertFalse(tlist.isMinkanable(HaiType.TON));
		assertFalse(tlist.isMinkanable(HaiType.NI_MAN));
	}

	@Test
	/**
	 * 3萬以外の牌では明槓できない
	 * 3萬で明槓した場合、[1,2,3]のリストが返ってくる．
	 */
	public void testGetMinkanableIndexList() {
		// 2,3,3,3,4,5,6,東,東,東,東,南,南
		TehaiList tlist = getSampleTehaiList();
		for (HaiType type : HaiType.values()) {
			if (type == HaiType.SAN_MAN) {
				assertTrue(tlist.isMinkanable(type));
				List<Integer> list = tlist.getMinkanableIndexList(type);
				assertTrue(list.contains(1));
				assertTrue(list.contains(2));
				assertTrue(list.contains(3));
			} else {
				assertFalse(tlist.isMinkanable(type));
			}
		}
	}

	@Test
	public void testIsAnkanableHaiHaiType() {

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
		//2,3,3,3,4,5,6,東,東,東,東,南,南
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
		//2,3,3,3,4,5,6,東,東,東,東,南,南
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
