package pages;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import system.AgariResult;
import system.KyokuResult;
import system.Player;
import system.ScoreType;
import system.Yaku;
import client.Client;
import client.ClientOperator;
import client.MajanFrame;

public class ResultPage extends InputPage implements Page{
	private Image imgBuffer;
	private Graphics g2;
	private KyokuResult result;
	private int[] newScore;
	private int[] oldScore;
	private JPanel[] pointsPanel;
	
	{
		newScore = new int[4];
		oldScore = new int[4];
		addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {
				getOperator().requestNextKyoku();
			}
		});
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
		g2.drawString("Result", getWidth()/2, getHeight()/2);
		g2.setColor(new Color(0,0,0,100));
		g2.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(imgBuffer, 0, 0,this);
	}

	@Override
	public String getPageName() {
		// TODO Auto-generated method stub
		return "Result";
	}
	
	private class ResultPanel extends JPanel{
		public ResultPanel(Player player,AgariResult result){
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			JLabel playerName = new JLabel(player.getName());
			playerName.setOpaque(false);
			add(playerName);
			for(Yaku y:result.getYakuSet()){
				JLabel tmpLabel = new JLabel(y.notation());
				tmpLabel.setOpaque(false);
				add(tmpLabel);
			}
			JLabel han = new JLabel(result.getHan() + "翻");
			han.setOpaque(false);
			add(han);
			JLabel hu = new JLabel(result.getHu() + "符");
			hu.setOpaque(false);
			add(hu);
			if(result.getScoreType() != ScoreType.NORMAL){
				JLabel scoreType = new JLabel(result.getScoreType().notation());
				scoreType.setOpaque(false);
				add(scoreType);
			}
		}
	}
	
	
	public void setResult(KyokuResult result,int[] beforeScore,int[] afterScore){
		this.result = result;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.newScore = newScore;
		this.oldScore = oldScore;
		if(result.isRyukyoku() || result.isTotyuRyukyoku()){
			add(new JTextField("流局"));
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
}