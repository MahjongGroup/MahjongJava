package datapack;

import java.awt.Canvas;
import java.awt.Image;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import client.Constant;

public class DataPacks implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 679615104432261399L;
	private static ConfigPack config;
	private static EnterPack enter;
	private static GamePack game;
	private static ResultPack result;
	private static StartPack start;
	private static WaitPack wait;
	private static List<DataPack> packsList;

	/**
	 * DataPacks(DataPackの集合)に各DataPackを格納する
	 */
	static {
		config = new ConfigPack();
		enter = new EnterPack();
		game = new GamePack();
		result = new ResultPack();
		start = new StartPack();
		wait = new WaitPack();
		packsList = new ArrayList<DataPack>();
		packsList.add(config);
		packsList.add(enter);
		packsList.add(game);
		packsList.add(start);
		packsList.add(result);
		packsList.add(wait);
	}

	/**
	 * 指定されたキャンバスから各DataPackにイメージ書き込み用のバッファを提供する
	 * 
	 * @param canvas
	 */
	public void setGraphics(Canvas canvas) {
		for (DataPack d : packsList) {
			d.setImage(canvas.createImage(Constant.WINDOW_WIDTH,
					Constant.WINDOW_HEIGHT));
		}
	}

	/**
	 * 指定されたモードのDataPackのデータ更新を終了する
	 * 
	 * @param mode
	 */
	public void finishPack(PackName mode) throws Exception{
		getDataPack(mode).finishPack();
	}

	/**
	 * 指定されたモードのイメージを生成する
	 * 
	 * @param mode
	 * @exception Exception
	 * 指定されたmodeのDataPackが存在しない場合
	 */
	public void createImage(PackName mode) throws Exception{
		getDataPack(mode).createImage();
	}

	public int getVersion(PackName mode) throws Exception{
		return getDataPack(mode).getVersion();
	}
	
	/**
	 * 指定されたモードからイメージを取得する
	 * 
	 * @param mode
	 * @return
	 * @throws Exception
	 *             指定されたモードが存在しない
	 */
	public Image getImage(PackName mode) throws Exception {
		return getDataPack(mode).getImage();
	}

	public ConfigPack getConfig() {
		return config;
	}

	public EnterPack getEnter() {
		return enter;
	}

	public GamePack getGame() {
		return game;
	}

	public ResultPack getResult() {
		return result;
	}

	public StartPack getStart() {
		return start;
	}

	public WaitPack getWait() {
		return wait;
	}

	/**
	 * 指定されたmodeに対応するDataPackを取得する
	 * 
	 * @param mode
	 * @return
	 * @throws Exception
	 *             DataPackが存在しないmodeを指定すると発生
	 */

	public DataPack getDataPack(PackName mode) throws Exception {
		switch (mode) {
		case Config:
			return config;
		case Enter:
			return enter;
		case Game:
			return game;
		case Result:
			return result;
		case Start:
			return start;
		case Wait:
			return wait;
		default:
			throw new Exception();
		}
	}

	/**
	 * 指定されたmodeのMouseListenerを取得する
	 * 
	 * @param mode
	 * @return
	 * @throws Exception
	 */
	public MouseListener getListener(PackName mode) throws Exception {
		return getDataPack(mode).getListener();
	}

}
