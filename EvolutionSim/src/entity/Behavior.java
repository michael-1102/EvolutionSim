package entity;

public enum Behavior {
	eat, // move to food
	idle, // do not move
	mate, // move to potential mate
	mating, // in the process of mating
	random; // move randomly
}
