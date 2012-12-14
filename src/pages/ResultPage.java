package pages;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import system.AgariResult;
import system.KyokuResult;
import system.Player;
import system.ScoreType;
import system.Yaku;
import client.Client;
import client.ClientOperator;
import client.MajanFrame;

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
		public ClearLabel(String str){
			JLabel tmp = new JLabel(str);
			tmp.setOpaque(false);
			tmp.setFont(new Font("sans-serif",Font.BOLD,25));
			tmp.addMouseListener(ResultPage.this);
			tmp.setHorizontalAlignment(SwingConstants.CENTER);
			add(tmp);
			setLayout(new CardLayout());
			setOpaque(false);
			addMouseListener(ResultPage.this);
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
		public ScorePanel(int[] newScore,int[] oldScore){
			GridBagLayout gbl = new GridBagLayout();
			Player[] players = getFrame().getInfo().players;
			for(int i = 0;i < players.length;i++){
				ClearLabel name = new ClearLabel(players[i].getName());
				ClearLabel scoreChange = new ClearLabel(newScore[i] + "->" + oldScore[i]);
				gbl.setConstraints(name, getGrid(0, i, 1, 1));
				gbl.setConstraints(scoreChange, getGrid(1, i, 1, 1));
			}
			setLayout(gbl);
			setOpaque(false);
		}
	}
	
	private class ResultPanel extends JPanel{
		public ResultPanel(Player player,AgariResult result){
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			add(new ClearLabel(player.getName()));
			for(Yaku y:result.getYakuSet()){
				add(new ClearLabel(y.notation()));
			}
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
			return;
		}
		List<Player> winnerList = new ArrayList<Player>();
		for(Player p:getFrame().getInfo().players){
			if(result.isAgari(p)){
				add(new ResultPanel(p, result.getAgariResult(p)));
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