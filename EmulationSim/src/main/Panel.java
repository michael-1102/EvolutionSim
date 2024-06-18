package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

import entity.Creature;
import entity.Food;

public class Panel extends JPanel implements Runnable {
    private GlobalData globalData;

	final int originalTileSize = 16;
	final int scale = 1;
	
	public final int tileSize = originalTileSize * scale;
	
	public final int maxScreenCol = 48;
	public final int maxScreenRow = 36;
	
	final int screenWidth = tileSize * maxScreenCol;
	final int screenHeight = tileSize * maxScreenRow;
	
	public int foodTimer;
	
	Thread thread;
	
	/*
	 Panel constructor
	 */
	public Panel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.white);
		this.setDoubleBuffered(true);
		globalData = GlobalData.getInstance(this);
		foodTimer = 0;
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
		globalData.getCreatures().addCreature(new Creature(10, 25, this, 100, 1000, 20));
		globalData.getCreatures().addCreature(new Creature(47, 1, this, 100, 1000, 20));
		
		globalData.getFoods().addFood(new Food(2, 3, this));
		globalData.getFoods().addFood(new Food(20, 43, this));
		globalData.getFoods().addFood(new Food(14, 23, this));
		globalData.getFoods().addFood(new Food(2, 33, this));
		globalData.getFoods().addFood(new Food(23, 6, this));
		globalData.getFoods().addFood(new Food(40, 30, this));
		globalData.getFoods().addFood(new Food(1, 34, this));
		globalData.getFoods().addFood(new Food(30, 30, this));
		globalData.getFoods().addFood(new Food(40, 13, this));
	}
	
	/*
	 Update information every frame
	 */
	public void update() {
		globalData.getCreatures().update();
		globalData.getFoods().update();
		foodTimer++;
	}
	
	/*
	 Redraw every frame
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		globalData.getCreatures().draw(g2);
		globalData.getFoods().draw(g2);
		g2.dispose();
	}
	
}
