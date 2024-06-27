package entity;

public enum Behavior {
	eat, // move to food
	idle, // do not move
	findMate, // move to potential mate
	mate, // in the process of mating
	random; // move randomly
}
