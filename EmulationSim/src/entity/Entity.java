package entity;

import java.awt.Graphics2D;

import main.Panel;

public class Entity {

	protected int posX;
	protected int posY;
	protected Panel p;

	/*
	 Entity constructor
	 */
	public Entity(int x, int y, Panel p) {
		posX = x;
		posY = y;
		this.p = p;
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
