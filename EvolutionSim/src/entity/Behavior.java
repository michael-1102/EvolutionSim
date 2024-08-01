package entity;

import java.awt.Color;

public enum Behavior {
	eat(true, true, "Find Food", Color.yellow), // move to food
	idle(true, false, "Rest", Color.black), // do not move
	findMate(true, true, "Mate", Color.blue), // move to potential mate
	mate(false, false, null, null), // in the process of mating
	random(true, true, "Move Randomly", Color.red); // move randomly
	
	private boolean schedulable; // whether or not this behavior can be placed in the schedule
	private boolean moving; // whether or not this behavior requires moving
	private String name;
	private Color color;

	
	private Behavior(boolean schedulable, boolean moving, String name, Color color) {
		this.schedulable = schedulable;
		this.moving = moving;		
		this.name = name;
		this.color = color;
	}
	
	public boolean isSchedulable() {
		return schedulable;
	}
	
	public boolean isMoving() {
		return moving;
	}
	
	public String getName() {
		return name;
	}
	
	public Color getColor() {
		return color;
	}
	
}
