package entity;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;

public class CreatureCollection {
	private ArrayList<Creature> creatures;
	
	/*
	 CreatureCollection constructor
	 */
	public CreatureCollection() {
		creatures = new ArrayList<Creature>();
	}
	
	/*
	 Get total number of creatures
	 */
	public int getNumCreatures() {
		return creatures.size();
	}
	
	/*
	 Get ArrayList of creatures
	 */
	public ArrayList<Creature> getCreatures() {
		return creatures;
	}
	
	/*
	 Add creature to ArrayList
	 */
	public void addCreature(Creature creature) {
		creatures.add(creature);
	}
	
	/*
	 Update creatures every frame
	 */
	public void update() {
		for (Iterator<Creature> iterator = creatures.iterator(); iterator.hasNext();) {
			Creature creature = iterator.next();
			creature.update();
		    if (creature.getEnergy() <= 0) {
		    	iterator.remove();
		    }
		}
	}
	
	/*
	 Draw creatures every frame
	 */
	public void draw(Graphics2D g2) {
		for (Creature creature : creatures) {
			creature.draw(g2);
		}
	}
	
}
