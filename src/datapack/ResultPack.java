package datapack;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import system.hai.Hai;


public class ResultPack extends DataPack {
	private List<List<Hai>> tehais;
	private boolean existWinner;
	private boolean finish;
	{
		tehais = new ArrayList<List<Hai>>();
	}
	@Override
	public void createImage() {
		// TODO Auto-generated method stub
		
	}

	public void setTehais(List<List<Hai>> tehais){
		this.tehais = tehais;
	}
	private class ListenerForResult extends CommunicatableListener{
		@Override
		public void mouseClicked(MouseEvent e) {
			if(finish)
				getBackground().setMode(PackName.Start);
			else
				getBackground().setMode(PackName.Game);
		}
	}
}
