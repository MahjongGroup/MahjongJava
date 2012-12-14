package pages;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import static client.Constant.SCALED_HAI_WIDTH;
import static client.Constant.SCALED_HAI_HEIGHT;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import system.AgariResult;
import system.Hai;
import system.KyokuPlayer;
import system.KyokuResult;
import system.Player;
import system.ScoreType;
import system.Yaku;
import client.Client;
import client.ClientOperator;
import client.ImageLoader;
import client.MajanFrame;
import client.MajanHaiIDMapper;

public class ResultPage extends InputPage implements Page,MouseListener{
	private Image imgBuffer;
	private Graphics g2;
	private KyokuResult result;
	private int[] newScore;
	private int[] oldScore;
	private JPanel[] pointsPanel;
	
	{
		newScore = new int[4];
		oldScore = new int[4];
		addMouseListener(this);
	}
	
	public ResultPage(MajanFrame frame,Client operator){
		this(frame);
		setOperator(operator);
		if(operator != null)
			((ClientOperator)getOperator()).setPage(this);
	}
	
	public ResultPage(MajanFrame frame){
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
		{			setLayout(new CardLayout());
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
			JLabel tmp = new JLabel(new ImageIcon(ImageLoader.loadScaled(MajanHaiIDMapper.getID(hai))));
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
				ClearLabel name = new ClearLabel(players[i].getName());
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
		private class TehaiPanel extends JPanel{
			public TehaiPanel(List<Hai> tehai){
				setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
				for(Hai h:tehai){
					add(new ClearLabel(h));
				}
				setOpaque(false);
				updateUI();
			}
		}
		
		private class DoraPanel extends ClearLabel{
			public DoraPanel(int doraCount,List<Hai> doraList){
			}
		}
		
		public ResultPanel(Player player,AgariResult result,KyokuPlayer kplayer){
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			add(new ClearLabel(player.getName()));
			List<Hai> tmpHaiList = new ArrayList<Hai>(kplayer.getTehaiList());
			add(new TehaiPanel(kplayer.getTehaiList()));
			for(Yaku y:result.getYakuSet()){
				add(new ClearLabel(y.notation()));
			}
			if(result.getDoraSize() != 0)
				add(new ClearLabel("ドラ" + result.getDoraSize()));
			add(new ClearLabel(result.getHan() + "翻"));
			add(new ClearLabel(result.getHu() + "符"));
			if(result.getScoreType() != ScoreType.NORMAL){
				add(new ClearLabel(result.getScoreType().notation()));
			}
			setOpaque(false);
		}
	}
	
	
	public void setResult(KyokuResult result,int[] newScore,int[] oldScore){
		this.result = result;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.newScore = newScore;
		this.oldScore = oldScore;
		add(new ScorePanel(newScore, oldScore));
		if(result.isRyukyoku() || result.isTotyuRyukyoku()){
			add(new ClearLabel("流局"));
			updateUI();
			return;
		}
		List<Player> winnerList = new ArrayList<Player>();
		for(Player p:getFrame().getInfo().players){
			if(result.isAgari(p)){
				add(new ResultPanel(p, result.getAgariResult(p),result.getKyokuPlayer(p)));
			}
		}
		updateUI();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		getOperator().requestNextKyoku();
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