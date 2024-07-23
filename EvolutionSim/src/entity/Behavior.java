package entity;

import java.util.ArrayList;
import java.util.Random;

public enum Behavior {
	eat(true, true), // move to food
	idle(true, false), // do not move
	findMate(true, true), // move to potential mate
	mate(false, false), // in the process of mating
	random(true, true); // move randomly
	
	private boolean schedulable; // whether or not this behavior can be placed in the schedule
	private boolean moving; // whether or not this behavior requires moving

	
	private Behavior(boolean schedulable, boolean moving) {
		this.schedulable = schedulable;
		this.moving = moving;		
	}
	
	public boolean isSchedulable() {
		return schedulable;
	}
	
	public boolean isMoving() {
		return moving;
	}
	
}
