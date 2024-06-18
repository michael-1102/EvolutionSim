package entity;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GlobalData;
import main.Panel;

public class Food extends Entity {
	
	private GlobalData globalData;
	private int age;
	private boolean eaten;
	
	public Food(int x, int y, Panel p) {
		super(x, y, p);
		age = 0;
		globalData = GlobalData.getInstance();
		eaten = false;
	}
	
	/*
	 Update food every frame
	 */
	public void update() {
		age++;
	}
	
	/*
	 Redraw food every frame
	 */
	public void draw(Graphics2D g2) {
		g2.setColor(new Color((int)(255*(0.5*age/globalData.getMaxFoodAge())), (int)(255*(1-1.0*age/globalData.getMaxFoodAge())), 0));
		
		g2.fillRect(posX*p.tileSize+p.tileSize/4, posY*p.tileSize+p.tileSize/4, p.tileSize/2, p.tileSize/2);
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
