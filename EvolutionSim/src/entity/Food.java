package entity;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GlobalData;

public class Food extends Entity {
	
	private GlobalData globalData;
	private int age;
	
	public Food(int x, int y) {
		super(x, y);
		age = 0;
		globalData = GlobalData.getInstance();
	}
	
	/*
	 Update food every frame
	 */
	@Override
	public void update() {
		super.update();
		
		age++;
		
		if (this.isDead()) {
			globalData.getEntities().decrementNumFood();
			globalData.getEntities().setNull(this.getPosX(), this.getPosY());
		}
	}
	
	/*
	 Redraw food every frame
	 */
	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(new Color((int)(255*(0.5*age/globalData.getMaxFoodAge())), (int)(255*(1-1.0*age/globalData.getMaxFoodAge())), 0));
		int tileSize = globalData.getTileSize();
		g2.fillRect(this.getPosX()*tileSize+tileSize/4, this.getPosY()*tileSize+tileSize/4, tileSize/2, tileSize/2);
	}

	/*
	 Return age of food
	 */
	public int getAge() {
		return age;
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
		if (age >= globalData.getMaxFoodAge()) {
			return true;
		}
		return false;
	}
	
	
	
}
