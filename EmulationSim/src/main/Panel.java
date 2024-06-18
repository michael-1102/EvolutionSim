package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

import entity.Behavior;
import entity.Creature;

public class Panel extends JPanel implements Runnable {
    private GlobalData globalData;

	final int originalTileSize = 16;
	final int scale = 1;
	
	public final int tileSize = originalTileSize * scale;
	
	public final int maxScreenCol = 48;
	public final int maxScreenRow = 36;
	
	final int screenWidth = tileSize * maxScreenCol;
	final int screenHeight = tileSize * maxScreenRow;
		
	Thread thread;
	
	/*
	 Panel constructor
	 */
	public Panel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.white);
		this.setDoubleBuffered(true);
		globalData = GlobalData.getInstance(this);
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
		double drawInterval = 1000000000/globalData.getFPS();
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		
		while (thread != null) {
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			lastTime = currentTime;
			
			if (delta >= 1) {
				update();
				repaint();
				delta--;
			}
			
		}
	}
	
	/*
	 Set up entities before start
	 */
	public void setUpSim() {
		globalData.getEntities().addCreature(new Creature(4, 4, this, 100, 1000, 20, Behavior.eat));
		globalData.getEntities().addCreature(new Creature(6, 6, this, 100, 1000, 20, Behavior.idle));
		globalData.getEntities().addCreature(new Creature(6, 5, this, 100, 1000, 20, Behavior.idle));
		globalData.getEntities().addCreature(new Creature(6, 4, this, 100, 1000, 20, Behavior.idle));
		globalData.getEntities().addCreature(new Creature(6, 3, this, 100, 1000, 20, Behavior.idle));
		globalData.getEntities().addCreature(new Creature(6, 2, this, 100, 1000, 20, Behavior.idle));
		globalData.getEntities().addCreature(new Creature(6, 1, this, 100, 1000, 20, Behavior.idle));
		globalData.getEntities().addCreature(new Creature(6, 0, this, 100, 1000, 20, Behavior.idle));


	}
	
	/*
	 Update information every frame
	 */
	public void update() {
		globalData.getEntities().update();
		globalData.incrementTime();
	}
	
	
	/*
	 Redraw every frame
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		globalData.getEntities().draw(g2);
		g2.dispose();
	}
	
}
