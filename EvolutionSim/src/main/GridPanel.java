package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class GridPanel extends JPanel implements Runnable {
    private GlobalData globalData;
		
	Thread thread;
	
	/*
	 Panel constructor
	 */
	public GridPanel() {
		globalData = GlobalData.getInstance();
		this.setPreferredSize(new Dimension(globalData.screenWidth, globalData.screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
	}

	
	/*
	 Starts thread
	 */
	public void startThread() {
		thread = new Thread(this);
		thread.start();
	}
	
	/*
	 Runs at start of thread
	 */
	@Override
	public void run() {
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		
		while (thread != null) {
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / (1000000000.0/globalData.getFPS());
			lastTime = currentTime;
			
			if (delta >= 1) {
				update();
				repaint();
				delta--;
			}
			
		}
	}
	
	
	
	/*
	 Update information every frame
	 */
	public void update() {
		globalData.getEntities().update();
		globalData.getTimerPanel().incrementTime();
	}
	
	
	/*
	 Redraw entities
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		globalData.getEntities().draw(g2);
		//g2.dispose();
		
	}
	
}
