package entity;

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
}
