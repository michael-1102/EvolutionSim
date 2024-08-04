package entity;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GlobalData;

public class Food extends Entity {
	
	private GlobalData globalData;
	private int age;
	private boolean eaten;
	
	public Food(int x, int y) {
		super(x, y);
		age = 0;
		globalData = GlobalData.getInstance();
		eaten = false;
	}
	
	/*
	 Update food every frame
	 */
	@Override
	public void update() {
		age++;
	}
	
	/*
	 Redraw food every frame
	 */
	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(new Color((int)(255*(0.5*age/globalData.getMaxFoodAge())), (int)(255*(1-1.0*age/globalData.getMaxFoodAge())), 0));
		int tileSize = globalData.getTileSize();
		g2.fillRect(posX*tileSize+tileSize/4, posY*tileSize+tileSize/4, tileSize/2, tileSize/2);
	}

	/*
	 Return age of food
	 */
	public int getAge() {
		return age;
	}
	
	/*
	 Asset eaten
	 */
	public void assertEaten() {
		eaten = true;
	}
	
	/*
	 Food does not have collision
	 */
	public boolean hasCollision() {
		return false;
	}
	
	/*
	 Returns true if food is too old or has been eaten
	 */
	public boolean isDead() {
		if (age >= globalData.getMaxFoodAge() || eaten) {
			return true;
		}
		return false;
	}
	
	
	
}
