package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class GridPanel extends JPanel implements Runnable {
	
    private GlobalData globalData;
	private volatile double delta;	
	/*
	 Panel constructor
	 */
	public GridPanel() {
		globalData = GlobalData.getInstance();
		this.setBackground(Color.white);
		this.setDoubleBuffered(true);
	}
	
	@Override
	public Dimension getPreferredSize() {
	    return new Dimension(globalData.getScreenWidth(), globalData.getScreenHeight());
	}
	
	@Override
	public Dimension getMinimumSize() {
	    return new Dimension(globalData.getScreenWidth(), globalData.getScreenHeight());
	}
	
	/*
	 Runs at start of thread
	 */
	@Override
	public void run() {
		delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		
		while (true) {
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / (1000000000.0/globalData.getFPS());
			lastTime = currentTime;
			if (delta >= 1) {
				update();
				repaint();
				delta = 0;
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
		g2.setColor(new Color(170, 200, 160));
		g2.fillRect(0, 0, globalData.getScreenWidth(), globalData.getScreenHeight());
		globalData.getEntities().draw(g2);
		g2.setColor(new Color(0, 0, 0,  globalData.getTimerPanel().getSkyAlpha()));
		System.out.println(globalData.getTimerPanel().getSkyAlpha());
		g2.fillRect(0, 0, globalData.getScreenWidth(), globalData.getScreenHeight());
		//g2.dispose();
	}
	
}
