package entity;

import java.awt.Graphics2D;

public class Entity {

	protected int posX;
	protected int posY;
	/*
	 Entity constructor
	 */
	public Entity(int x, int y) {
		posX = x;
		posY = y;
	}
	
	/*
	 Overridden
	 */
	public void draw(Graphics2D g2) {
		
	}

	/*
	 Overridden
	 */
	public void update() {
		
	}
	
	/*
	 Overridden
	 */
	public boolean isDead() {
		return false;
	}
	
	/*
	 Overridden
	 */
	public boolean hasCollision() {
		return false;
	}
}
