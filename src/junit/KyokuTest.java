package junit;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import system.Hai;
import system.HaiType;
import system.Kaze;
import system.Kyoku;
import system.KyokuPlayer;
import system.KyokuResult;
import system.MajanHai;
import system.Player;
import system.Rule;

import static system.MajanHai.*;

public class KyokuTest {

	Player p1 = new Player(0, "a", false);
	Player p2 = new Player(1, "b", false);
	Player p3 = new Player(2, "c", false);
	Player p4 = new Player(3, "d", false);
	Kyoku kyoku = init();

	public Kyoku init() {
		Map<Kaze, Player> player = new HashMap<Kaze, Player>();
		player.put(Kaze.TON, p1);
		player.put(Kaze.NAN, p2);
		player.put(Kaze.SYA, p3);
		player.put(Kaze.PE, p4);
		Kyoku kyoku = new Kyoku(new Rule(), player, Kaze.TON);
		return kyoku;
	}

	@Test
	public void testDoTsumo() {
		kyoku.init();
		assertEquals(kyoku.sizeOfYamahai(), 70);
		kyoku.doTsumo();
		assertEquals(kyoku.sizeOfYamahai(), 69);
	}

	@Test
	public void testIsKyusyukyuhai() {
		kyoku.init();
		kyoku.doTsumo();
		kyoku.isKyusyukyuhai();
	}

	@Test
	public void testDoKyusyukyuhai() {
		kyoku.init();
		KyokuPlayer kp = new KyokuPlayer();
		List<Hai> list = new ArrayList<Hai>(Arrays.asList(new Hai[] { ITI_MAN, ITI_SOU, ITI_SOU, ITI_PIN, TYUN, TON, TON, NAN, PE, SYA, HATU, HAKU, NI_MAN }));
		assertEquals(list.size(), 13);
		kp.setTehai(list);
		kyoku.setKyokuPlayer(Kaze.TON, kp);
		kyoku.doTsumo();
		assertTrue(kyoku.isKyusyukyuhai());
		kyoku.doKyusyukyuhai();
		KyokuResult kr = kyoku.createKyokuResult();
		assertTrue(kr.isTotyuRyukyoku());
		assertFalse(kr.isRonAgari());
		assertFalse(kr.isRyukyoku());
		assertFalse(kr.isTsumoAgari());
	}

	@Test
	public void testIsTsumoAgari() {
		kyoku.init();
		kyoku.doTsumo();
		kyoku.isTsumoAgari();
	}

	@Test
	public void testDoTsumoAgari() {
		kyoku.init();
		KyokuPlayer kp = new KyokuPlayer();
		List<Hai> list = new ArrayList<Hai>(Arrays.asList(new Hai[] { ITI_MAN, NI_MAN, SAN_MAN, YO_MAN, GO_MAN, ROKU_MAN, NANA_MAN, HATI_MAN, TON, TON, TON, NAN, NAN }));
		assertEquals(list.size(), 13);
		kp.setTehai(list);
		kyoku.setKyokuPlayer(Kaze.TON, kp);
		kyoku.doTsumo(KYU_MAN);
		assertTrue(kyoku.isTsumoAgari());
		kyoku.doTsumoAgari();
		KyokuResult kr = kyoku.createKyokuResult();
		assertFalse(kr.isTotyuRyukyoku());
		assertFalse(kr.isRyukyoku());
		assertFalse(kr.isRonAgari());
		assertTrue(kr.isTsumoAgari());

		for (Kaze kaze : Kaze.values()) {
			if (kaze == Kaze.TON) {
				assertTrue(kr.isAgari(kyoku.getPlayer(kaze)));
			} else {
				assertFalse(kr.isAgari(kyoku.getPlayer(kaze)));
			}
		}
	}

	@Test
	public void testIsKakanable() {
		kyoku.init();
		kyoku.doTsumo();
		kyoku.isKakanable();
	}

	@Test
	public void testGetKakanableHaiList() {
		fail("Not yet implemented");
	}

	@Test
	public void testDoKakan() {
		fail("Not yet implemented");
	}

	@Test
	public void testDoRinsyanTsumo() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsAnkanable() {
		kyoku.init();
		kyoku.doTsumo();
		kyoku.isAnkanable();
	}

	@Test
	public void testGetAnkanableHaiList() {
		kyoku.init();
		KyokuPlayer kp = new KyokuPlayer();
		List<Hai> list = new ArrayList<Hai>(Arrays.asList(new Hai[] {ITI_MAN,ITI_MAN,ITI_MAN,ITI_MAN,NI_MAN,NI_MAN,NI_MAN,NI_MAN,NAN,NAN,NAN,NAN,PE}));
		assertEquals(list.size(),13);
		kp.setTehai(list);
		kyoku.setKyokuPlayer(Kaze.TON, kp);
		kyoku.doTsumo();
		assertTrue(kyoku.isAnkanable());
		kyoku.getAnkanableHaiList();
	}

	@Test
	public void testDoAnkan() {
		kyoku.init();
		KyokuPlayer kp = new KyokuPlayer();
		List<Hai> list = new ArrayList<Hai>(Arrays.asList(new Hai[] { ITI_MAN, NI_MAN, SAN_MAN, YO_MAN, GO_MAN, ROKU_MAN, NANA_MAN, HATI_MAN, TON, TON, TON, TON, NAN }));
		assertEquals(list.size(), 13);
		kp.setTehai(list);
		kyoku.setKyokuPlayer(Kaze.TON, kp);
		kyoku.doTsumo(KYU_MAN);
		assertTrue(kyoku.isAnkanable());
		kyoku.doAnkan(kyoku.getAnkanableHaiList().get(0));
		kyoku.doRinsyanTsumo();
		assertFalse(kyoku.isTsumoAgari());
	}

	@Test
	public void testIsRonable() {
		kyoku.init();
		kyoku.doTsumo();
		kyoku.discard(0);
		for (Kaze kaze : Kaze.values()) {
			kyoku.isRonable(kaze);
		}
	}

	@Test
	public void testDoRon() {
		kyoku.init();
		KyokuPlayer kp = new KyokuPlayer();
		KyokuPlayer kp2 = new KyokuPlayer();
		List<Hai> list = new ArrayList<Hai>(Arrays.asList(new Hai[] { ITI_MAN,ITI_MAN,ITI_MAN, NI_MAN, SAN_MAN, YO_MAN, GO_MAN, ROKU_MAN, NANA_MAN, HATI_MAN, KYU_MAN, KYU_MAN, KYU_MAN }));
		List<Hai> list2 = new ArrayList<Hai>(Arrays.asList(new Hai[] {HAKU,HAKU,HAKU,HATU,HATU,HATU,TYUN,TYUN,TYUN,TON,TON,TON,NAN}));
		assertEquals(list.size(), 13);
		assertEquals(list2.size(), 13);
		kp.setTehai(list);
		kp2.setTehai(list2);
		kyoku.setKyokuPlayer(Kaze.TON, kp);
		kyoku.setKyokuPlayer(Kaze.NAN,kp2);
		
		kyoku.doTsumo(NAN);
		assertFalse(kyoku.isTsumoAgari());
		kyoku.discard(13);
		assertTrue(kyoku.isRonable(Kaze.NAN));
		kyoku.doRon(Kaze.NAN);
	}

	@Test
	public void testIsReach() {
		kyoku.init();
		KyokuPlayer kp = new KyokuPlayer();
		List<Hai> list = new ArrayList<Hai>(Arrays.asList(new Hai[] { ITI_MAN,ITI_MAN,ITI_MAN, NI_MAN, SAN_MAN, YO_MAN, GO_MAN, ROKU_MAN, NANA_MAN, HATI_MAN, KYU_MAN, KYU_MAN, NI_PIN }));
		assertEquals(list.size(),13);
		kp.setTehai(list);
		kyoku.setKyokuPlayer(Kaze.TON, kp);
		kyoku.doTsumo(KYU_MAN);
		assertFalse(kyoku.isTsumoAgari());
		kyoku.doReach();
		kyoku.discard(kyoku.getReachableHaiList().get(0));
		assertTrue(kyoku.isReach(kyoku.getCurrentTurn()));
		
	}
	

	@Test
	public void testIsReachable() {
		kyoku.init();
		kyoku.doTsumo();
		kyoku.isReachable();
	}
	
	public void testIsReachable_and_Tsumoagari_Kokushi(){
		kyoku.init();
		KyokuPlayer kp = new KyokuPlayer();
		List<Hai> list = new ArrayList<Hai>(Arrays.asList(new Hai[] { ITI_MAN,KYU_MAN,ITI_PIN, KYU_PIN, ITI_SOU, KYU_SOU, TON, NAN, SYA, PE, HATU, HAKU, TYUN }));
		assertEquals(list.size(),13);
		kp.setTehai(list);
		kyoku.setKyokuPlayer(Kaze.TON, kp);
		kyoku.doTsumo(KYU_MAN);
		assertTrue(kyoku.isReachable());
		assertTrue(kyoku.isTsumoAgari());
		kyoku.doTsumoAgari();
	}
	

	@Test
	public void testGetReachableHaiList() {
		kyoku.init();
		KyokuPlayer kp = new KyokuPlayer();
		List<Hai> list = new ArrayList<Hai>(Arrays.asList(new Hai[] { ITI_MAN,ITI_MAN,ITI_MAN, NI_MAN, SAN_MAN, YO_MAN, GO_MAN, ROKU_MAN, NANA_MAN, HATI_MAN, KYU_MAN, KYU_MAN, KYU_MAN}));
		assertEquals(list.size() ,13);
		kp.setTehai(list);
		kyoku.setKyokuPlayer(Kaze.TON, kp);
		kyoku.doTsumo(ITI_MAN);
		assertTrue(kyoku.isReachable());
		assertTrue(kyoku.getReachableHaiList().size() > 0);
	}

	@Test
	public void testDoReach() {
		fail("Not yet implemented");
	}

	@Test
	public void testDiscard() {
		kyoku.init();
		kyoku.doTsumo();
		kyoku.discard(0);
	}

	@Test
	public void testDiscardTsumoHai() {
		kyoku.init();
		kyoku.doTsumo();
		kyoku.discardTsumoHai();
	}

	@Test
	public void testIsMinkanable() {
		kyoku.init();
		kyoku.doTsumo();
		kyoku.discard(0);

		for (Kaze kaze : Kaze.values()) {
			kyoku.isMinkanable(kaze);
		}
	}

	@Test
	public void testGetMinkanableList() {
		fail("Not yet implemented");
	}

	@Test
	public void testDoMinkan() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsPonable() {
		kyoku.init();
		kyoku.doTsumo();
		kyoku.discard(0);

		for (Kaze kaze : Kaze.values()) {
			kyoku.isPonable(kaze);
		}
	}

	@Test
	public void testGetPonableHaiList() {
		fail("Not yet implemented");
	}

	@Test
	public void testDoPon() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsChiable() {
		kyoku.init();
		kyoku.doTsumo();
		kyoku.discard(0);

		kyoku.isChiable();
	}

	@Test
	public void testGetChiableHaiList() {
		fail("Not yet implemented");
	}

	@Test
	public void testDoChi() {
		fail("Not yet implemented");
	}

	@Test
	public void testNextTurn() {
		kyoku.init();
		kyoku.doTsumo();
		kyoku.discard(0);
		kyoku.nextTurn();
		
	}

	@Test
	public void testIsFuriten() {
		kyoku.init();
		kyoku.doTsumo();
		kyoku.discard(0);
		kyoku.isFuriten(Kaze.TON, HaiType.ITI_MAN);
	}

	@Test
	public void testIsTenpai() {
		kyoku.init();
		kyoku.doTsumo();

		kyoku.isTenpai();
		kyoku.discard(0);
		
		for (Kaze kaze : Kaze.values()) {
			kyoku.isTenpai(kaze);
		}
	}

	@Test
	public void testOpenDora() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsSanchaho() {
		kyoku.init();
		kyoku.isSanchaho();
	}

	@Test
	public void testIsSufontsuRenta() {
		kyoku.init();
		kyoku.isSufontsuRenta();
	}

	@Test
	public void testIsSukaikan() {
		kyoku.init();
		kyoku.isSukaikan();
	}

	@Test
	public void testIsSuchaReach() {
		kyoku.init();
		kyoku.isSuchaReach();
	}

	@Test
	public void testDoSuchaReach() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsRyukyoku() {
		kyoku.init();
		kyoku.isRyukyoku();
	}

	@Test
	public void testDoRyukyoku() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsTotyuRyukyoku() {
		kyoku.init();
		kyoku.isTotyuRyukyoku();
	}

	@Test
	public void testDoTotyuRyukyokuSanchaho() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsSyukyoku() {
		kyoku.init();
		kyoku.isSyukyoku();
	}

	@Test
	public void testDoSyukyoku() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetRule() {
		Rule rule = kyoku.getRule();
		assertTrue(rule != null);
	}

	@Test
	public void testGetPlayerMap() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPlayer() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetBakaze() {
		assertEquals(kyoku.getBakaze(), Kaze.TON);
	}

	@Test
	public void testGetOpenDoraList() {
		kyoku.init();
		kyoku.getOpenDoraList();
	}

	@Test
	public void testGetDoraList() {
		kyoku.init();
		kyoku.getDoraList();
	}

	@Test
	public void testGetUradoraList() {
		kyoku.init();
		kyoku.getUradoraList();
	}

	@Test
	public void testGetCurrentTurn() {
		kyoku.init();
		assertEquals(kyoku.getCurrentTurn(), Kaze.TON);
		kyoku.doTsumo();
		kyoku.discard(0);
		kyoku.nextTurn();
		assertEquals(kyoku.getCurrentTurn(), Kaze.NAN);
		kyoku.doTsumo();
		kyoku.discard(0);
		kyoku.nextTurn();
		assertEquals(kyoku.getCurrentTurn(), Kaze.SYA);
		kyoku.doTsumo();
		kyoku.discard(0);
		kyoku.nextTurn();
		assertEquals(kyoku.getCurrentTurn(), Kaze.PE);
		kyoku.doTsumo();
		kyoku.discard(0);
		kyoku.nextTurn();
		assertEquals(kyoku.getCurrentTurn(), Kaze.TON);
	}

	@Test
	public void testGetKazeOf() {
		kyoku.init();
		assertEquals(kyoku.getKazeOf(p1), Kaze.TON);
		assertEquals(kyoku.getKazeOf(p2), Kaze.NAN);
		assertEquals(kyoku.getKazeOf(p3), Kaze.SYA);
		assertEquals(kyoku.getKazeOf(p4), Kaze.PE);
	}

}
