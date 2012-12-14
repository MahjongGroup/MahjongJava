package pages;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
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
	
	
	public void setResult(KyokuResult result,int[] beforeScore,int[] afterScore){
		this.result = result;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.newScore = newScore;
		this.oldScore = oldScore;
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