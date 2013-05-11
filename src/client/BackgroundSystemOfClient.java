package client;

import pages.Page;
import server.MahjongServer;

public class BackgroundSystemOfClient {
	private Page currentPage;
	private ClientInfo info;
	private MahjongFrame frame;

	private class pageManageThread extends Thread {
		private boolean isFinish;

		public pageManageThread() {
			isFinish = true;
			while (isFinish) {
				if (currentPage.isFinish()) {
					currentPage.finish();
					currentPage = getNextPage();
				}
				try {
					sleep(10000);
				} catch (InterruptedException e) {

				}
			}
		}
	}

	private Page getNextPage() {
		Page p = null;
		return p;
	}

	public static void main(String[] args) {
		MahjongServer.main(args);
		// MajanFrame frame = new MajanFrame();
		// frame.setPage("start");
	}

}
