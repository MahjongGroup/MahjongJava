package pages;

import java.awt.Dimension;

import client.Client;

public interface Page{
	public String getPageName();
	public void setPage(Page page);
	public void setPreferredSize(Dimension d);
	public void movePage(String order);
	
	public boolean isFinish();
	public void finish();
	public String getNextPageName();
}
