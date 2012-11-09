package ai;

import java.util.List;

import system.HaiType;


public interface AI {
	public boolean isKyusyukyuhai();
	public boolean isTumoAgari();
	public int kakan();
	public HaiType ankan();
	public boolean isReach();
	public int discard();
	public boolean isRon();
	public List<Integer> pon();
	public List<Integer> chi();
	public boolean minkan();
}
