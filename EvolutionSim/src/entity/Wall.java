package entity;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GlobalData;

public class Wall extends Entity {
	
	GlobalData globalData;
	
	public Wall(int x, int y) {
		super(x, y);
		globalData = GlobalData.getInstance();
	}
	
	/*
	 Draw wall
	 */
	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(Color.orange);
		int tileSize = globalData.getTileSize();
		g2.fillRect(this.getPosX()*tileSize, this.getPosY()*tileSize, tileSize, tileSize);
	}
	
	/*
	 Food does have collision
	 */
	public boolean hasCollision() {
		return true;
	}
}
