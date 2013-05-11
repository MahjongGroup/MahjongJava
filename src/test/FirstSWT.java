package test;

import java.awt.Canvas;
import java.awt.event.PaintEvent;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


public class FirstSWT {

	Canvas canvas;
	
	public static void main(String[] args) {
		FirstSWT obj = new FirstSWT();
		obj.start();
	}
	
	public void start() {
		Display display = new Display();
		Shell shell = new Shell(display);
		
		shell.setLocation(100, 50);
		shell.setSize(500, 700);
		shell.setLayout(new FillLayout());
		
//		canvas = new Canvas(shell, SWT.NONE);
//		
//		canvas.addPaintListener(new PaintListener(){
//			int x = 10;
//
//			@Override
//			public void paintControl(PaintEvent e) {
//				e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_BLUE));
//				e.gc.drawText("Hello World!", 10 + x++, 10);
//			}
//			
//		});
//
//		Executor executor = Executors.newSingleThreadExecutor();
//		
//		/*
//		executor.execute(new Runnable(){
//
//			@Override
//			public void run() {
//				while(true){
//					FirstSWT.this.canvas.redraw();
//					try {
//						Thread.sleep(100);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//			
//		});
//		*/
//		
//		shell.open();
//		
//		while (!shell.isDisposed()) {
//			if (!display.readAndDispatch()) {
//				try {
//					Thread.sleep(10);
//				} catch (InterruptedException e1) {
//					e1.printStackTrace();
//				}
//				canvas.redraw();
//				
//			}
//		}
//		
//		display.dispose();
	}
}