package entity;

import java.awt.Graphics2D;

public class Entity {

	private int posX;
	private int posY;
	private boolean upToDate; // whether this entity has already been updated this frame
	/*
	 Entity constructor
	 */
	public Entity(int x, int y) {
		posX = x;
		posY = y;
		upToDate = false;
	}
	
	public int getPosX() {
		return posX;
	}
	
	public int getPosY() {
		return posY;
	}
	
	public void setPosX(int x) {
		posX = x;
	}
	
	public void setPosY(int y) {
		posY = y;
	}
	

	public void draw(Graphics2D g2) {
	}

	public void update() {
		upToDate = true;
	}
	
	public boolean isDead() {
		return false;
	}
	
	public boolean hasCollision() {
		return false;
	}
	
	public boolean isUpToDate() {
		return upToDate;
	}
	
	public void setUpToDate(boolean newVal) {
		upToDate = newVal;
	}
}
