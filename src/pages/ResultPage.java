package pages;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import system.AgariResult;
import system.Hai;
import system.Kaze;
import system.KyokuPlayer;
import system.KyokuResult;
import system.MajanHai;
import system.Mentu;
import system.Player;
import system.ScoreType;
import system.Yaku;
import client.ImageLoader;
import client.MahjongFrame;
import client.MajanHaiIDMapper;

import static client.Constant.*;


public class ResultPage extends InputPage implements Page,MouseListener{
	private Image imgBuffer;
	private Graphics g2;
	private int[] newScore;
	private int[] oldScore;
	private int winnerCount;
	private List<ResultPanel> resultPanels;
	
	{
		newScore = new int[4];
		oldScore = new int[4];
		addMouseListener(this);
		winnerCount = 0;
		resultPanels = new ArrayList<ResultPanel>();
	}
	
	
	public ResultPage(MahjongFrame frame){
		setFrame(frame);
	}
	public void paint(Graphics g){
		if(imgBuffer == null)
			imgBuffer = createImage(getWidth(),getHeight());
		if(g2 == null)
			g2 = imgBuffer.getGraphics();
		super.paint(g2);
		g.drawImage(imgBuffer, 0, 0,this);
	}

	@Override
	public String getPageName() {
		// TODO Auto-generated method stub
		return "Result";
	}
	
	private class ClearLabel extends JPanel{
		{
			setOpaque(false);
			addMouseListener(ResultPage.this);
		}
		public ClearLabel(){}
		public ClearLabel(String str){
			JLabel tmp = new JLabel(str);
			tmp.setOpaque(false);
			tmp.setFont(new Font("sans-serif",Font.BOLD,25));
			tmp.addMouseListener(ResultPage.this);
			tmp.setHorizontalAlignment(SwingConstants.CENTER);
			add(tmp);
		}
		public ClearLabel(Hai hai){
//			setLayout(new CardLayout());
			JLabel tmp = new JLabel(new ImageIcon(ImageLoader.loadScaled(MajanHaiIDMapper.getID(MajanHai.valueOf(hai.type(),hai.aka())))));
			add(tmp);
			tmp.setOpaque(false);
			tmp.addMouseListener(ResultPage.this);
		}
	}
	
	private class ScorePanel extends JPanel{
		private GridBagConstraints getGrid(int x,int y,int width,int height){
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = x;
			gbc.gridy = y;
			gbc.gridwidth = width;
			gbc.gridheight = height;
			return gbc;
		}
		{
//			setPreferredSize(new Dimension(10, getHeight()));
		}
		public ScorePanel(int[] newScore,int[] oldScore){
			GridBagLayout gbl = new GridBagLayout();
			Player[] players = getFrame().getInfo().players;
			for(int i = 0;i < players.length;i++){
				ClearLabel name = null;
				if(players[i] == null)
					name = new ClearLabel("");
				else
					name = new ClearLabel(players[i].getName());
				ClearLabel newPlayerScore = new ClearLabel(newScore[i] + "");
				ClearLabel flow = new ClearLabel("->");
				ClearLabel oldPlayerScore = new ClearLabel(oldScore[i] + "");
				gbl.setConstraints(name, getGrid(0, i, 1, 1));
				gbl.setConstraints(oldPlayerScore, getGrid(1, i, 1, 1));
				gbl.setConstraints(flow, getGrid(2, i, 1, 1));
				gbl.setConstraints(newPlayerScore, getGrid(3, i, 1, 1));
				add(name);
				add(oldPlayerScore);
				add(flow);
				add(newPlayerScore);
			}
			setLayout(gbl);
			addMouseListener(ResultPage.this);
			setOpaque(false);
		}
	}
	
	
	private class ResultPanel extends JPanel{
		private class StringHaisPanel extends JPanel{
			public StringHaisPanel(String beforeStr,List<Hai> tehai,String afterStr){
				setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
				add(new ClearLabel(beforeStr));
				for(Hai h:tehai){
					add(new ClearLabel(h));
				}
				add(new ClearLabel(afterStr));
				setOpaque(false);
				updateUI();
			}			
		}
		private class TehaiPanel extends JPanel{
			public TehaiPanel(List<Hai> tehai){
				setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
				Dimension d = new Dimension(SCALED_HAI_WIDTH,SCALED_HAI_HEIGHT);
				for(Hai h:tehai){
					ClearLabel tmpClearLabel = new ClearLabel(h);
					tmpClearLabel.setSize(d);
					tmpClearLabel.setPreferredSize(d);
					add(new ClearLabel(h));
				}
				setOpaque(false);
				updateUI();
			}
		}
		
		private class DoraPanel extends ClearLabel{
			public DoraPanel(int doraCount){
				setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
				add(new ClearLabel("ドラ"));
				List<Hai> tmpList = new ArrayList<Hai>();
				for(Hai h:getFrame().getInfo().doraList){
					tmpList.add(MajanHai.valueOf(h.nextOfDora(), false));
				}
				add(new TehaiPanel(tmpList));
				add(new ClearLabel("" + doraCount));
			}
		}
		
		public ResultPanel(Player player, AgariResult result,
				KyokuPlayer kplayer, Hai agariHai, List<Hai> uradoraList,int getScore) {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			add(new ClearLabel(player.getName()));
			List<Hai> tmpList = new ArrayList<Hai>(kplayer.getTehaiList());
			for(Mentu m:kplayer.getHurohaiList()){
				tmpList.addAll(m.asList());
			}
			add(new StringHaisPanel("",new ArrayList<Hai>(tmpList),""));
			tmpList = new ArrayList<Hai>();
			tmpList.add(agariHai);
			add(new StringHaisPanel("上がり牌",new ArrayList<Hai>(tmpList),""));
			for(Yaku y:result.getYakuSet()){
				add(new ClearLabel(y.notation()));
			}
			tmpList = new ArrayList<Hai>();
			for(Hai h:getFrame().getInfo().doraList){
				tmpList.add(MajanHai.valueOf(h.nextOfDora(), false));
			}
			if(kplayer.isReach()){
				for(Hai h:uradoraList){
					tmpList.add(MajanHai.valueOf(h.nextOfDora(), false));
				}
			}
			add(new StringHaisPanel("ドラ",new ArrayList<Hai>(tmpList),"" + result.getDoraSize()));
			add(new ClearLabel(result.getHan() + "翻"));
			add(new ClearLabel(result.getHu() + "符"));
			if(result.getScoreType() != ScoreType.NORMAL){
				add(new ClearLabel(result.getScoreType().notation()));
			}
			add(new ClearLabel(getScore + ""));
			setOpaque(false);
		}
	}
	
	
	public void setResult(KyokuResult result, int[] newScore, int[] oldScore,
			List<Integer> changeScore, List<Hai> uradoraList) {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.newScore = newScore;
		this.oldScore = oldScore;
		for(int i = 0;i < 4;i++){
			int index = getFrame().getInfo().kaze.get(Kaze.valueOf(i));
			getFrame().getInfo().setScore(index, newScore[i]);
		}
		if(result.isRyukyoku() || result.isTotyuRyukyoku()){
			add(new ClearLabel("流局"));
			updateUI();
			return;
		}
		resultPanels = new ArrayList<ResultPanel>();
		for(Player p:getFrame().getInfo().players){
			if(result.isAgari(p)){
				int index = -1;
				for(Kaze k:Kaze.values()){
					index = getFrame().getInfo().kaze.get(k);
					if(getFrame().getInfo().players[index]==p)
						break;
				}
				resultPanels.add(new ResultPanel(p, result.getAgariResult(p),
						result.getKyokuPlayer(p), result.getAgariHai(),
						uradoraList, changeScore.get(index)));
			}
		}
		mouseClicked(null);
		updateUI();
	}
	

	@Override
	public void mouseClicked(MouseEvent e) {
		if(winnerCount < resultPanels.size()){
			removeAll();
			add(resultPanels.get(winnerCount));
			winnerCount++;
			updateUI();
		}else if(winnerCount == resultPanels.size()){
			removeAll();
			add(new ScorePanel(newScore, oldScore));
			winnerCount++;
			updateUI();
		}else{
			getFrame().getOperator().requestNextKyoku();			
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
}