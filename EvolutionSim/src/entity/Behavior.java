package entity;

public enum Behavior {
	eat(true), // move to food
	idle(true), // do not move
	findMate(true), // move to potential mate
	mate(false), // in the process of mating
	random(true); // move randomly
	
	boolean schedulable;
	
	private Behavior(boolean schedulable) {
		this.schedulable = schedulable;
	}
	
	public boolean isSchedulable() {
		return schedulable;
	}
}
