package junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static system.hai.MajanHai.GO_MAN;
import static system.hai.MajanHai.GO_PIN;
import static system.hai.MajanHai.GO_SOU;
import static system.hai.MajanHai.HAKU;
import static system.hai.MajanHai.HATI_MAN;
import static system.hai.MajanHai.HATI_PIN;
import static system.hai.MajanHai.HATU;
import static system.hai.MajanHai.ITI_MAN;
import static system.hai.MajanHai.ITI_PIN;
import static system.hai.MajanHai.ITI_SOU;
import static system.hai.MajanHai.KYU_MAN;
import static system.hai.MajanHai.KYU_PIN;
import static system.hai.MajanHai.KYU_SOU;
import static system.hai.MajanHai.NAN;
import static system.hai.MajanHai.NANA_MAN;
import static system.hai.MajanHai.NANA_PIN;
import static system.hai.MajanHai.NI_MAN;
import static system.hai.MajanHai.NI_PIN;
import static system.hai.MajanHai.NI_SOU;
import static system.hai.MajanHai.PE;
import static system.hai.MajanHai.ROKU_MAN;
import static system.hai.MajanHai.ROKU_PIN;
import static system.hai.MajanHai.SAN_MAN;
import static system.hai.MajanHai.SAN_PIN;
import static system.hai.MajanHai.SAN_SOU;
import static system.hai.MajanHai.SYA;
import static system.hai.MajanHai.TON;
import static system.hai.MajanHai.TYUN;
import static system.hai.MajanHai.YO_MAN;
import static system.hai.MajanHai.YO_PIN;
import static system.hai.MajanHai.YO_SOU;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import system.Kyoku;
import system.KyokuPlayer;
import system.Player;
import system.Rule;
import system.hai.Hai;
import system.hai.HaiType;
import system.hai.Kaze;
import system.result.KyokuResult;

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
	public void test_chinitsu_tamenchan1(){
		kyoku.init();
		KyokuPlayer kp = new KyokuPlayer();
		List<Hai> list = new ArrayList<Hai>(Arrays.asList(new Hai[] { NI_PIN,SAN_MAN,SAN_PIN,YO_PIN,YO_PIN,GO_PIN,GO_PIN,ROKU_PIN,ROKU_PIN,NANA_PIN,NANA_PIN,HATI_PIN,HATI_PIN }));
		assertEquals(list.size(), 13);
		kp.setTehai(list);
		kyoku.setKyokuPlayer(Kaze.TON, kp);
		kyoku.doTsumo(SAN_PIN);
		assertTrue(kyoku.isReachable());
		//System.out.println(kyoku.getReachableHaiList());
		kyoku.doReach();
		kyoku.discard(kyoku.getReachableHaiList().get(1));
		kyoku.doTsumo(HATI_PIN);//2筒、5筒、8筒でアガリ
		//System.out.println(kyoku.getTehaiList(kyoku.getCurrentTurn()));
		assertTrue(kyoku.isTsumoAgari());
		kyoku.doTsumoAgari();
	}
	
	@Test
	public void test_chinitsu_tamenchan2(){
		kyoku.init();
		KyokuPlayer kp = new KyokuPlayer();
		List<Hai> list = new ArrayList<Hai>(Arrays.asList(new Hai[] { NI_PIN,NI_PIN,NI_PIN,SAN_PIN,YO_PIN,GO_PIN,ROKU_PIN,NANA_PIN,NANA_PIN,NANA_PIN,HATI_PIN,HATI_PIN,TON }));
		assertEquals(list.size(), 13);
		kp.setTehai(list);
		kyoku.setKyokuPlayer(Kaze.TON, kp);
		kyoku.doTsumo(HATI_PIN);
		//System.out.println(kyoku.getTehaiList(kyoku.getCurrentTurn())+":"+kyoku.getCurrentTsumoHai());
		assertTrue(kyoku.isReachable());
	//	System.out.println(kyoku.getReachableHaiList());
		kyoku.doReach();
		kyoku.discard(kyoku.getReachableHaiList().get(2));//東切り
		kyoku.doTsumo(HATI_PIN);//1~8筒でアガリ
		//System.out.println(kyoku.getTehaiList(kyoku.getCurrentTurn()));
		assertTrue(kyoku.isTsumoAgari());
		kyoku.doTsumoAgari();
	}
	
	
	@Test
	public void test_chinitsu_tamenchan3(){
		kyoku.init();
		KyokuPlayer kp = new KyokuPlayer();
		List<Hai> list = new ArrayList<Hai>(Arrays.asList(new Hai[] { GO_PIN,ROKU_PIN,ROKU_PIN,ROKU_PIN,NANA_PIN,NANA_PIN,NANA_PIN,HATI_PIN,HATI_PIN,HATI_PIN,KYU_PIN,KYU_PIN,TON }));
		assertEquals(list.size(), 13);
		kp.setTehai(list);
		kyoku.setKyokuPlayer(Kaze.TON, kp);
		kyoku.doTsumo(KYU_PIN);
		//System.out.println(kyoku.getTehaiList(kyoku.getCurrentTurn())+":"+kyoku.getCurrentTsumoHai());
		assertTrue(kyoku.isReachable());
		//System.out.println(kyoku.getReachableHaiList());
		kyoku.doReach();
		kyoku.discard(kyoku.getReachableHaiList().get(1));//東切り
		//System.out.println(kyoku.getTehaiList(kyoku.getCurrentTurn()));
		kyoku.doTsumo(HATI_PIN);//4~8筒でアガリ
		//System.out.println(kyoku.getTehaiList(kyoku.getCurrentTurn())+" : "+kyoku.getCurrentTsumoHai());
		//System.out.println(kyoku.isTsumoAgari());
		assertTrue(kyoku.isTsumoAgari());
		kyoku.doTsumoAgari();
	}
	
	
	@Test
	public void testIsKakanable() {
		kyoku.init();
		kyoku.doTsumo();
		kyoku.isKakanable();
	}

	@Test
	public void testGetKakanableHaiList() {
		kyoku.init();
		KyokuPlayer kp = new KyokuPlayer();
		KyokuPlayer kp2 = new KyokuPlayer();
		KyokuPlayer kp3 = new KyokuPlayer();
		KyokuPlayer kp4 = new KyokuPlayer();
		List<Hai> list = new ArrayList<Hai>(Arrays.asList(new Hai[] {ITI_MAN,ITI_MAN,ITI_MAN,ITI_MAN,NI_MAN,NI_MAN,NI_MAN,NI_MAN,NAN,NAN,NAN,NAN,PE}));
		List<Hai> list2 = new ArrayList<Hai>(Arrays.asList(new Hai[] {ITI_SOU,ITI_SOU,ITI_SOU,NI_SOU,NI_SOU,NI_SOU,SAN_SOU,SAN_SOU,SAN_SOU,YO_SOU,YO_SOU,YO_SOU,GO_SOU}));
		List<Hai> list3 = new ArrayList<Hai>(Arrays.asList(new Hai[] {ITI_PIN,ITI_PIN,ITI_PIN,NI_PIN,NI_PIN,NI_PIN,SAN_PIN,SAN_PIN,SAN_PIN,YO_PIN,YO_PIN,YO_PIN,GO_PIN}));
		List<Hai> list4 = new ArrayList<Hai>(Arrays.asList(new Hai[] {HAKU,HAKU,HAKU,TYUN,TYUN,TYUN,HATU,HATU,HATU,PE,PE,PE,SYA}));
		assertEquals(list.size(),13);
		assertEquals(list2.size(),13);
		assertEquals(list3.size(),13);
		assertEquals(list4.size(),13);
		kp.setTehai(list);
		kp2.setTehai(list2);
		kp3.setTehai(list3);
		kp4.setTehai(list4);
		kyoku.setKyokuPlayer(Kaze.TON, kp);
		kyoku.setKyokuPlayer(Kaze.NAN, kp2);
		kyoku.setKyokuPlayer(Kaze.SYA, kp3);
		kyoku.setKyokuPlayer(Kaze.PE, kp4);
		kyoku.doTsumo(ITI_SOU);
		kyoku.discard(13);
		assertTrue(kyoku.isMinkanable(Kaze.NAN));
		assertTrue(kyoku.isPonable(Kaze.NAN));
		kyoku.doPon(Kaze.NAN, kyoku.getPonableHaiList(Kaze.NAN).get(0));
		kyoku.discard(10);
		kyoku.nextTurn();
		kyoku.doTsumo();
		kyoku.discard(13);
		kyoku.nextTurn();
		kyoku.doTsumo(NI_SOU);
		kyoku.discard(13);
		assertTrue(kyoku.isMinkanable(Kaze.NAN));
		assertTrue(kyoku.isPonable(Kaze.NAN));
		kyoku.doPon(Kaze.NAN, kyoku.getPonableHaiList(Kaze.NAN).get(0));
		kyoku.discard(7);
		kyoku.nextTurn();
		kyoku.discard(0);
		kyoku.nextTurn();
		kyoku.discard(0);
		kyoku.nextTurn();
		kyoku.discard(0);
		kyoku.nextTurn();
		kyoku.doTsumo();
		assertTrue(kyoku.isKakanable());
		//System.out.println(kyoku.getCurrentTurn()+" : "+kyoku.getTehaiList(kyoku.getCurrentTurn()));
		//System.out.println(kyoku.getFuroHaiList(kyoku.getCurrentTurn()));
		//System.out.println(kyoku.getKakanableHaiList());
		assertTrue(kyoku.getKakanableHaiList().size() == 2);
		kyoku.doKakan(1);
		kyoku.doRinsyanTsumo();
		kyoku.doKakan(0);
		
	}

	@Test
	public void testDoKakan() {
		fail("Not yet implemented");
	}

	@Test
	public void testDoRinsyanTsumo() {
		kyoku.init();
		KyokuPlayer kp = new KyokuPlayer();
		List<Hai> list = new ArrayList<Hai>(Arrays.asList(new Hai[] {ITI_MAN,ITI_MAN,ITI_MAN,ITI_MAN,NI_MAN,NI_MAN,NI_MAN,NI_MAN,SAN_MAN,SAN_MAN,SAN_MAN,SAN_MAN,YO_MAN}));
		kyoku.removeYamahai(list);
		kyoku.removeWanpai(list);
		assertEquals(list.size(),13);
		kp.setTehai(list);
		kyoku.setKyokuPlayer(Kaze.TON, kp);
		kyoku.doTsumo();
		assertTrue(kyoku.isAnkanable());
		//System.out.println(kyoku.getTehaiList(kyoku.getCurrentTurn()));
		//System.out.println(kyoku.getAnkanableHaiList());
		kyoku.doAnkan(kyoku.getAnkanableHaiList().get(kyoku.getAnkanableHaiList().size()-1));
		kyoku.doRinsyanTsumo();
		//System.out.println(kyoku.getTehaiList(kyoku.getCurrentTurn()));
		kyoku.doAnkan(kyoku.getAnkanableHaiList().get(kyoku.getAnkanableHaiList().size()-1));
		kyoku.doRinsyanTsumo();
		//System.out.println(kyoku.getTehaiList(kyoku.getCurrentTurn()));
		kyoku.doAnkan(kyoku.getAnkanableHaiList().get(kyoku.getAnkanableHaiList().size()-1));
		kyoku.doRinsyanTsumo();
		//System.out.println(kyoku.getTehaiList(kyoku.getCurrentTurn()));
	
	}
	
	
	@Test
	public void testFuritenJudge(){
		kyoku.init();
		KyokuPlayer kp = new KyokuPlayer();
		KyokuPlayer kp2 = new KyokuPlayer();
		List<Hai> list = new ArrayList<Hai>(Arrays.asList(new Hai[] {ITI_MAN,ITI_MAN,ITI_MAN,KYU_MAN,NI_MAN,SAN_MAN,YO_MAN,GO_MAN,ROKU_MAN,NANA_MAN,HATI_MAN,KYU_MAN,KYU_MAN}));
		List<Hai> list2 = new ArrayList<Hai>(Arrays.asList(new Hai[] {ITI_PIN,ITI_PIN,ITI_PIN,KYU_PIN,NI_PIN,SAN_PIN,YO_PIN,GO_PIN,ROKU_PIN,NANA_PIN,HATI_PIN,KYU_PIN,KYU_PIN}));
		kyoku.removeYamahai(list);
		kyoku.removeWanpai(list);
		kyoku.removeYamahai(list2);
		kyoku.removeWanpai(list2);
		
		assertEquals(list.size(),13);
		assertEquals(list2.size(),13);
		kp.setTehai(list);
		kp2.setTehai(list2);
		kyoku.setKyokuPlayer(Kaze.TON, kp);
		kyoku.setKyokuPlayer(Kaze.NAN, kp2);
		kyoku.doTsumo(GO_MAN);
		//System.out.println(kyoku.getCurrentTurn());
		//System.out.println(kyoku.getTehaiList(kyoku.getCurrentTurn())+" : "+kyoku.getCurrentTsumoHai());
		assertTrue(kyoku.isTsumoAgari());
		kyoku.discard(13);
		kyoku.nextTurn();
		kyoku.doTsumo(ROKU_MAN);
		kyoku.discard(13);
		assertTrue(kyoku.isTenpai(Kaze.TON));
		//System.out.println(kyoku.getSutehaiList(Kaze.TON));
		assertFalse(kyoku.isRonable(Kaze.TON));
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
	
	@Test
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
		KyokuResult kr = kyoku.createKyokuResult();
		//System.out.println(kr.getAgariResult(kyoku.getCurrentPlayer()));
	}
	
	@Test
	public void testIsChitoi(){
		kyoku.init();
		KyokuPlayer kp = new KyokuPlayer();
		List<Hai> list = new ArrayList<Hai>(Arrays.asList(new Hai[] {ITI_MAN,ITI_MAN,ITI_MAN,ITI_MAN,NI_MAN,NI_MAN,YO_MAN,YO_MAN,ROKU_MAN,ROKU_MAN,HATI_MAN,HATI_MAN,KYU_MAN	}));
		assertEquals(list.size() ,13);
		kp.setTehai(list);
		kyoku.setKyokuPlayer(Kaze.TON, kp);
		kyoku.doTsumo(KYU_MAN);
		assertFalse(kyoku.isReachable());
		assertFalse(kyoku.isTsumoAgari());
	}
	
	@Test
	public void testIsReachable_andTsumoagari_chitoi(){
		kyoku.init();
		KyokuPlayer kp = new KyokuPlayer();
		List<Hai> list = new ArrayList<Hai>(Arrays.asList(new Hai[] { ITI_MAN,ITI_MAN,ITI_PIN,ITI_PIN, ITI_SOU, ITI_SOU, TON, NAN, NAN, PE, PE, HAKU, TYUN }));
		assertEquals(list.size(),13);
		kp.setTehai(list);
		kyoku.setKyokuPlayer(Kaze.TON, kp);
		kyoku.doTsumo(TON);
		assertTrue(kyoku.isReachable());
		kyoku.doReach();
		kyoku.discard(kyoku.getReachableHaiList().get(0));
		kyoku.nextTurn();
		kyoku.doTsumo();
		kyoku.discard(13);
		kyoku.nextTurn();
		kyoku.doTsumo();
		kyoku.discard(13);
		kyoku.nextTurn();	
		kyoku.doTsumo();
		kyoku.discard(13);
		kyoku.nextTurn();
		kyoku.doTsumo(TYUN);
		assertTrue(kyoku.isTsumoAgari());
		
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
		kyoku.init();
		KyokuPlayer kp = new KyokuPlayer();
		KyokuPlayer kp2 = new KyokuPlayer();
		List<Hai> list = new ArrayList<Hai>(Arrays.asList(new Hai[] { ITI_MAN,ITI_MAN,ITI_MAN, NI_MAN, SAN_MAN, YO_MAN, GO_MAN, ROKU_MAN, NANA_MAN, HATI_MAN, KYU_MAN, KYU_MAN, KYU_MAN}));
		List<Hai> list2 = new ArrayList<Hai>(Arrays.asList(new Hai[] {NI_PIN,NI_PIN,NI_PIN,SAN_PIN,SAN_PIN,SAN_PIN,YO_PIN,YO_PIN,YO_PIN,GO_PIN,GO_PIN,ROKU_PIN,NANA_PIN}));
		assertEquals(list.size(),13);
		assertEquals(list2.size() ,13);
		kp.setTehai(list);
		kp2.setTehai(list2);
		kyoku.setKyokuPlayer(Kaze.TON, kp);
		kyoku.setKyokuPlayer(Kaze.NAN, kp2);
		kyoku.doTsumo(NI_PIN);
		kyoku.discard(13);
		assertTrue(kyoku.isMinkanable(Kaze.NAN));
		kyoku.doMinkan(Kaze.NAN);
		
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
		kyoku.init();
		KyokuPlayer kp = new KyokuPlayer();
		List<Hai> list = new ArrayList<Hai>(Arrays.asList(new Hai[] {ITI_MAN,ITI_MAN,ITI_MAN,ITI_MAN,NI_MAN,NI_MAN,NI_MAN,HAKU,HAKU,HAKU,HAKU,HATU,HATU }));
		kyoku.removeYamahai(list);
		kyoku.removeWanpai(list);
		assertEquals(list.size(),13);
		kp.setTehai(list);
		kyoku.setKyokuPlayer(Kaze.TON, kp);
		kyoku.doTsumo(TYUN);
		kyoku.discard(13);
		kyoku.nextTurn();
		kyoku.doTsumo(NI_MAN);
		kyoku.discard(13);
		assertTrue(kyoku.isPonable(Kaze.TON));
		assertTrue(kyoku.getPonableHaiList(Kaze.TON).size() == 3);
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
		kyoku.isFuriten(Kaze.TON);
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
		kyoku.doTsumo(TON);
		kyoku.discard(13);
		kyoku.nextTurn();
		kyoku.doTsumo(TON);
		kyoku.discard(13);
		kyoku.nextTurn();
		kyoku.doTsumo(TON);
		kyoku.discard(13);
		kyoku.nextTurn();
		assertFalse(kyoku.isSufontsuRenta());
		kyoku.doTsumo(TON);
		kyoku.discard(13);
		assertTrue(kyoku.isSufontsuRenta());
	}

	@Test
	public void testIsSukaikan() {
		kyoku.init();
		KyokuPlayer kp0 = new KyokuPlayer();
		KyokuPlayer kp1 = new KyokuPlayer();
		List<Hai> list0 = new ArrayList<Hai>(Arrays.asList(new Hai[] {TON,TON,TON,TON,NAN,NAN,NAN,NAN,KYU_MAN,ITI_PIN,NI_PIN,HATU,HATU}));
		List<Hai> list1 = new ArrayList<Hai>(Arrays.asList(new Hai[] {SYA,SYA,SYA,SYA,PE,PE,PE,PE,KYU_MAN,ITI_PIN,NI_PIN,HATU,HATU }));
		kyoku.removeYamahai(list0);
		kyoku.removeWanpai(list0);
		kyoku.removeYamahai(list1);
		kyoku.removeWanpai(list1);
		assertEquals(list0.size(),13);
		assertEquals(list1.size(),13);
		kp0.setTehai(list0);
		kp1.setTehai(list1);
		kyoku.setKyokuPlayer(Kaze.TON, kp0);
		kyoku.setKyokuPlayer(Kaze.NAN, kp1);
		kyoku.doTsumo();
		kyoku.doAnkan(kyoku.getAnkanableHaiList().get(0));
		kyoku.doRinsyanTsumo();
		kyoku.doAnkan(kyoku.getAnkanableHaiList().get(0));
		kyoku.doRinsyanTsumo();
		kyoku.discard(13);
		kyoku.nextTurn();
		kyoku.doTsumo();
		kyoku.doAnkan(kyoku.getAnkanableHaiList().get(0));
		kyoku.doRinsyanTsumo();
		assertFalse(kyoku.isSukaikan());
		kyoku.doAnkan(kyoku.getAnkanableHaiList().get(0));
		kyoku.doRinsyanTsumo();
		kyoku.discard(13);
		assertTrue(kyoku.isSukaikan());
		assertEquals(list0.size(),13);
		

	}
	
	
	
	public void testSukantsu(){
		kyoku.init();
		KyokuPlayer kp = new KyokuPlayer();
		List<Hai> list = new ArrayList<Hai>(Arrays.asList(new Hai[] {TON,TON,TON,NAN,NAN,NAN,SYA,SYA,SYA,PE,PE,PE,HATU}));
		kyoku.removeYamahai(list);
		kyoku.removeWanpai(list);
		assertEquals(list.size(),13);
		kyoku.setKyokuPlayer(Kaze.TON, kp);
		kyoku.doTsumo(ITI_PIN);
		kyoku.discard(13);
		kyoku.nextTurn();
		kyoku.doTsumo(TON);
		kyoku.discard(13);
		assertTrue(kyoku.isMinkanable(Kaze.TON));
		kyoku.doMinkan(Kaze.TON);
		kyoku.doRinsyanTsumo();
		kyoku.discard(13);
		kyoku.nextTurn();
		kyoku.doTsumo(NAN);
		kyoku.discard(13);
		assertTrue(kyoku.isMinkanable(Kaze.TON));
		kyoku.doMinkan(Kaze.TON);
		kyoku.doRinsyanTsumo();
		kyoku.discard(13);
		kyoku.nextTurn();
		kyoku.doTsumo(SYA);
		kyoku.discard(13);
		assertTrue(kyoku.isMinkanable(Kaze.TON));
		kyoku.doMinkan(Kaze.TON);
		kyoku.doRinsyanTsumo();
		kyoku.discard(13);
		kyoku.nextTurn();
		kyoku.doTsumo(PE);
		kyoku.discard(13);
		assertTrue(kyoku.isMinkanable(Kaze.TON));
		kyoku.doMinkan(Kaze.TON);
		kyoku.doRinsyanTsumo();
		kyoku.discard(13);
		assertFalse(kyoku.isSukaikan());
		kyoku.nextTurn();
		
		
		
		
		
	}
	
	
	

	@Test
	public void testIsSuchaReach() {
		kyoku.init();
		KyokuPlayer kp0 = new KyokuPlayer();
		KyokuPlayer kp1 = new KyokuPlayer();
		KyokuPlayer kp2 = new KyokuPlayer();
		KyokuPlayer kp3 = new KyokuPlayer();
		List<Hai> list0 = new ArrayList<Hai>(Arrays.asList(new Hai[] {ITI_MAN,NI_MAN,SAN_MAN,YO_MAN,GO_MAN,ROKU_MAN,NANA_MAN,HATI_MAN,KYU_MAN,ITI_PIN,NI_PIN,HATU,HATU}));
		List<Hai> list1 = new ArrayList<Hai>(Arrays.asList(new Hai[] {ITI_MAN,NI_MAN,SAN_MAN,YO_MAN,GO_MAN,ROKU_MAN,NANA_MAN,HATI_MAN,KYU_MAN,ITI_PIN,NI_PIN,HATU,HATU }));
		List<Hai> list2 = new ArrayList<Hai>(Arrays.asList(new Hai[] {ITI_MAN,NI_MAN,SAN_MAN,YO_MAN,GO_MAN,ROKU_MAN,NANA_MAN,HATI_MAN,KYU_MAN,ITI_PIN,NI_PIN,HAKU,HAKU }));
		List<Hai> list3 = new ArrayList<Hai>(Arrays.asList(new Hai[] {ITI_MAN,NI_MAN,SAN_MAN,YO_MAN,GO_MAN,ROKU_MAN,NANA_MAN,HATI_MAN,KYU_MAN,ITI_PIN,NI_PIN,TYUN,TYUN }));
		kyoku.removeYamahai(list0);
		kyoku.removeWanpai(list0);
		kyoku.removeYamahai(list1);
		kyoku.removeWanpai(list1);
		kyoku.removeYamahai(list2);
		kyoku.removeWanpai(list2);
		kyoku.removeYamahai(list3);
		kyoku.removeWanpai(list3);
		assertEquals(list0.size(),13);
		assertEquals(list1.size(),13);
		assertEquals(list2.size(),13);
		assertEquals(list3.size(),13);
		kp0.setTehai(list0);
		kp1.setTehai(list1);
		kp2.setTehai(list2);
		kp3.setTehai(list3);
		kyoku.setKyokuPlayer(Kaze.TON, kp0);
		kyoku.setKyokuPlayer(Kaze.NAN, kp1);
		kyoku.setKyokuPlayer(Kaze.SYA, kp2);
		kyoku.setKyokuPlayer(Kaze.PE, kp3);
		
		kyoku.doTsumo(KYU_PIN);
		assertTrue(kyoku.isReachable());
		kyoku.doReach();
		kyoku.discard(13);
		kyoku.nextTurn();
		kyoku.doTsumo(KYU_PIN);
		assertTrue(kyoku.isReachable());
		kyoku.doReach();
		kyoku.discard(13);
		kyoku.nextTurn();
		kyoku.doTsumo(KYU_PIN);
		assertTrue(kyoku.isReachable());
		kyoku.doReach();
		kyoku.discard(13);
		kyoku.nextTurn();
		kyoku.doTsumo(KYU_PIN);
		assertFalse(kyoku.isSuchaReach());
		assertTrue(kyoku.isReachable());
		kyoku.doReach();
		kyoku.discard(13);
		kyoku.nextTurn();
		assertTrue(kyoku.isSuchaReach());
	
	
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
		kyoku.init();
		KyokuPlayer kp1 = new KyokuPlayer();
		KyokuPlayer kp2 = new KyokuPlayer();
		KyokuPlayer kp3 = new KyokuPlayer();
		List<Hai> list1 = new ArrayList<Hai>(Arrays.asList(new Hai[] {ITI_MAN,NI_MAN,SAN_MAN,YO_MAN,GO_MAN,ROKU_MAN,NANA_MAN,HATI_MAN,KYU_MAN,ITI_PIN,NI_PIN,HATU,HATU }));
		List<Hai> list2 = new ArrayList<Hai>(Arrays.asList(new Hai[] {ITI_MAN,NI_MAN,SAN_MAN,YO_MAN,GO_MAN,ROKU_MAN,NANA_MAN,HATI_MAN,KYU_MAN,ITI_PIN,NI_PIN,HAKU,HAKU }));
		List<Hai> list3 = new ArrayList<Hai>(Arrays.asList(new Hai[] {ITI_MAN,NI_MAN,SAN_MAN,YO_MAN,GO_MAN,ROKU_MAN,NANA_MAN,HATI_MAN,KYU_MAN,ITI_PIN,NI_PIN,TYUN,TYUN }));
		kyoku.removeYamahai(list1);
		kyoku.removeWanpai(list1);
		kyoku.removeYamahai(list2);
		kyoku.removeWanpai(list2);
		kyoku.removeYamahai(list3);
		kyoku.removeWanpai(list3);
		assertEquals(list1.size(),13);
		assertEquals(list2.size(),13);
		assertEquals(list3.size(),13);
		kp1.setTehai(list1);
		kp2.setTehai(list2);
		kp3.setTehai(list3);
		kyoku.setKyokuPlayer(Kaze.NAN, kp1);
		kyoku.setKyokuPlayer(Kaze.SYA, kp2);
		kyoku.setKyokuPlayer(Kaze.PE, kp3);
		kyoku.doTsumo(SAN_PIN);
		kyoku.discard(13);
		assertTrue(kyoku.isRonable(Kaze.NAN));
		assertTrue(kyoku.isRonable(Kaze.SYA));
		assertTrue(kyoku.isRonable(Kaze.PE));
		kyoku.doRon(Kaze.NAN);
		kyoku.doRon(Kaze.SYA);
		kyoku.doRon(Kaze.PE);
		assertTrue(kyoku.isSanchaho());
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
		kyoku.init();
		//System.out.println(kyoku.getPlayerMap());
	}

	@Test
	public void testGetPlayer() {
		kyoku.init();
		//System.out.println(kyoku.getPlayer(kyoku.getCurrentTurn()));
	}

	@Test
	public void testGetBakaze() {
		assertEquals(kyoku.getBakaze(), Kaze.TON);
	}

	@Test
	public void testGetOpenDoraList() {
		kyoku.init();
		//System.out.println(kyoku.getOpenDoraList());
		kyoku.getOpenDoraList();
	}
	
	@Test
	public void testGetOpenDoraList_after_kan(){
		kyoku.init();
		KyokuPlayer kp = new KyokuPlayer();
		List<Hai> list = new ArrayList<Hai>(Arrays.asList(new Hai[] { ITI_MAN,ITI_MAN,ITI_MAN, ITI_MAN, SAN_MAN, SAN_MAN, SAN_MAN, SAN_MAN, NANA_MAN, KYU_MAN, KYU_MAN, KYU_MAN, KYU_MAN}));
		kyoku.removeYamahai(list);
		kyoku.removeWanpai(list);
		assertEquals(list.size() ,13);
		kp.setTehai(list);
		kyoku.setKyokuPlayer(Kaze.TON, kp);
		kyoku.doTsumo();
		assertTrue(kyoku.isAnkanable());
		//System.out.println(kyoku.getAnkanableHaiList());
		kyoku.doAnkan(kyoku.getAnkanableHaiList().get(0));
		kyoku.doRinsyanTsumo();
		kyoku.doAnkan(kyoku.getAnkanableHaiList().get(0));
		kyoku.doRinsyanTsumo();
		kyoku.doAnkan(kyoku.getAnkanableHaiList().get(0));
		kyoku.doRinsyanTsumo();
		//System.out.println(kyoku.getOpenDoraList());	
	}
	
	

	@Test
	public void testGetDoraList() {
		kyoku.init();
	}

	@Test
	public void testGetUradoraList() {
		kyoku.init();
		
	}
	
	
	@Test
	public void yakuHanteiPinfu(){
		kyoku.init();
		KyokuPlayer kp1 = new KyokuPlayer();
		KyokuPlayer kp2 = new KyokuPlayer();
		List<Hai> list1 = new ArrayList<Hai>(Arrays.asList(new Hai[] { SAN_MAN,SAN_MAN,YO_MAN, YO_MAN, GO_MAN, ROKU_MAN, ROKU_MAN,  NI_SOU, SAN_SOU, SAN_SOU, YO_SOU, YO_SOU, GO_SOU}));
		List<Hai> list2 = new ArrayList<Hai>(Arrays.asList(new Hai[]  { SAN_MAN,SAN_MAN,YO_MAN, YO_MAN, GO_MAN, ROKU_MAN, ROKU_MAN,  NI_SOU, SAN_SOU, SAN_SOU, YO_SOU, YO_SOU, GO_SOU}));
//		kyoku.removeYamahai(list1);
//		kyoku.removeWanpai(list1);
//		kyoku.removeYamahai(list2);
//		kyoku.removeWanpai(list2);
		assertEquals(list1.size() ,13);
		kp1.setTehai(list1);
		assertEquals(list2.size() ,13);
		kp2.setTehai(list2);
		kyoku.setKyokuPlayer(Kaze.TON, kp1);
		kyoku.setKyokuPlayer(Kaze.NAN, kp2);
		kyoku.doTsumo(ROKU_PIN);
		assertTrue(kyoku.isReachable());
		kyoku.doReach();
		kyoku.discard(13);
		kyoku.nextTurn();
		kyoku.doTsumo(GO_MAN);
		kyoku.discard(13);
		kyoku.doRon(Kaze.TON);
		KyokuResult kr = kyoku.createKyokuResult();
		//System.out.println(kr.getAgariResult(p1));
	}
	
	@Test
	public void yakuHanteiSananko(){
		kyoku.init();
		KyokuPlayer kp1 = new KyokuPlayer();
		KyokuPlayer kp2 = new KyokuPlayer();
		List<Hai> list1 = new ArrayList<Hai>(Arrays.asList(new Hai[] { SAN_MAN,SAN_MAN,SAN_MAN, GO_MAN, GO_MAN, ROKU_MAN, ROKU_MAN,  ITI_SOU, ITI_SOU, ITI_SOU, SAN_SOU, YO_SOU, GO_SOU}));
		List<Hai> list2 = new ArrayList<Hai>(Arrays.asList(new Hai[]  { ITI_MAN,ITI_MAN,YO_MAN, YO_MAN, GO_MAN, ROKU_MAN, ROKU_MAN,  NI_SOU, SAN_SOU, SAN_SOU, YO_SOU, YO_SOU, GO_SOU}));
//		kyoku.removeYamahai(list1);
//		kyoku.removeWanpai(list1);
//		kyoku.removeYamahai(list2);
//		kyoku.removeWanpai(list2);
		assertEquals(list1.size() ,13);
		kp1.setTehai(list1);
		assertEquals(list2.size() ,13);
		kp2.setTehai(list2);
		kyoku.setKyokuPlayer(Kaze.TON, kp1);
		kyoku.setKyokuPlayer(Kaze.NAN, kp2);
		kyoku.doTsumo(ROKU_PIN);
		assertTrue(kyoku.isReachable());
		kyoku.doReach();
		kyoku.discard(13);
		kyoku.nextTurn();
		kyoku.doTsumo(GO_MAN);
		kyoku.discard(13);
		kyoku.doRon(Kaze.TON);
		KyokuResult kr = kyoku.createKyokuResult();
		//System.out.println(kr.getAgariResult(p1));
	}
	
	@Test
	public void yakuHanteiKantyan(){
		kyoku.init();
		KyokuPlayer kp1 = new KyokuPlayer();
		KyokuPlayer kp2 = new KyokuPlayer();
		List<Hai> list1 = new ArrayList<Hai>(Arrays.asList(new Hai[] { SAN_MAN,SAN_MAN,YO_MAN, YO_MAN, GO_MAN, ROKU_MAN, ROKU_MAN,  ITI_SOU, NI_SOU, SAN_SOU, YO_SOU, YO_SOU, YO_SOU}));
		List<Hai> list2 = new ArrayList<Hai>(Arrays.asList(new Hai[]  { SAN_MAN,SAN_MAN,YO_MAN, YO_MAN, GO_MAN, ROKU_MAN, ROKU_MAN,  NI_SOU, SAN_SOU, SAN_SOU, YO_SOU, YO_SOU, GO_SOU}));
		kyoku.removeYamahai(list1);
		kyoku.removeWanpai(list1);
		kyoku.removeYamahai(list2);
		kyoku.removeWanpai(list2);
		assertEquals(list1.size() ,13);
		kp1.setTehai(list1);
		assertEquals(list2.size() ,13);
		kp1.setTehai(list2);
		kyoku.setKyokuPlayer(Kaze.TON, kp1);
		kyoku.setKyokuPlayer(Kaze.NAN, kp2);
		kyoku.doTsumo(ROKU_PIN);
		assertTrue(kyoku.isReachable());
		kyoku.doReach();
		kyoku.discard(13);
		kyoku.nextTurn();
		kyoku.doTsumo(GO_MAN);
		kyoku.discard(13);
		kyoku.doRon(Kaze.TON);
		KyokuResult kr = kyoku.createKyokuResult();
		//System.out.println(kr.getAgariResult(p1));
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
