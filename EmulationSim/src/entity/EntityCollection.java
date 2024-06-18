package entity;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import main.GlobalData;
import main.Panel;

public class EntityCollection {
	private ArrayList<Entity> entities;
	private GlobalData globalData;
	private Panel p;
	static private int dirX[] = { -1, 0, 1, 0 };
	static private int dirY[] = { 0, 1, 0, -1 };
	
	/*
	 FoodList constructor
	 */
	public EntityCollection(Panel p) {
		entities = new ArrayList<Entity>();
		globalData = GlobalData.getInstance();
		this.p = p;
	}
	
	/*
	 Get ArrayList of entities
	 */
	public ArrayList<Entity> getEntities() {
		return entities;
	}
	
	/*
	 Get entity at index
	 */
	public Entity getEntity(int i) {
		return entities.get(i);
	}
	
	/*
	 Add creature to ArrayList
	 */
	public void addCreature(Creature creature) {
		entities.add(creature);
	}
	
	
	/*
	 Add food to ArrayList
	 */
	public void addFood(Food food) {
		entities.add(food);
	}
	
	
	
	/*
	 Update food every frame
	 */
	public void update() {
		for (Iterator<Entity> iterator = entities.iterator(); iterator.hasNext();) {
		    Entity entity = iterator.next();
		    entity.update();
		}
		
		// remove dead entities
		entities.removeIf(x -> x.isDead());
		
		// respawn food
		if (globalData.getTime() % globalData.getFoodRespawnTime() == 0) {
			int newX, newY;
			for (int i = 0; i < globalData.getNumFoodSpawn(); i++) {
				if (entities.size() >= globalData.getMaxNumFood()) {
					return;
				}
				newX = (int)(Math.random() * p.maxScreenCol); 
				newY = (int)(Math.random() * p.maxScreenRow);
				if (!this.posEmpty(newX, newY)) {
					int[] newPos = emptyBFS(new boolean[p.maxScreenCol][p.maxScreenRow], newX, newY);
					if (newPos.length == 2) {
						newX = newPos[0];
						newY = newPos[1];
					} else {
						return;
					}
				}
				addFood(new Food(newX, newY, p));
			}
		}
	}
	
	private boolean isValid(boolean visited[][], int x, int y) {
		if (x < 0 || y < 0 || x >= p.maxScreenCol || y>= p.maxScreenRow) {
			return false;
		}
		if (visited[x][y]) {
			return false;
		}
		return true;
	}
	
	/*
	 Search for nearest empty space
	 */
	private int[] emptyBFS(boolean visited[][], int x, int y) {
		Queue<int[]> q = new LinkedList<>();
		q.add(new int[] {x,y});
		visited[x][y] = true;
		int[] pos = new int[2]; 
		while (!q.isEmpty()) {
			pos = q.peek();
			if (this.posEmpty(pos[0], pos[1])) {
				return pos;
			}
			
			q.remove();
			
			ArrayList<int[]> list = new ArrayList<int[]>();
			for (int i = 0; i < 4; i++) {
				int adjx = pos[0] + dirX[i];
				int adjy = pos[1] + dirY[i];
				if (isValid(visited, adjx, adjy)) {
					list.add(new int[] {adjx, adjy});
					visited[adjx][adjy] = true;
				}
			}
			Collections.shuffle(list, new Random());
			for (int i = 0; i < list.size(); i++) {
				q.add(list.get(i));
			}
		}
		return new int[0];
	}
	
	/*
	 Draw foods every frame
	 */
	public void draw(Graphics2D g2) {
		for (Entity entity : entities) {
			entity.draw(g2);
		}
	}
	
	/*
	 Return index of food if there is food at x and y position, return -1 otherwise
	 */
	public int posHasFood(int x, int y) {
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i) != null) {
				if (entities.get(i) instanceof Food && entities.get(i).posX == x && entities.get(i).posY == y) {
					return i;
				}
			}
		}
		return -1;
	}
	
	/*
	 Return true if x and y position is empty
	 */
	public boolean posEmpty(int x, int y) {
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i) != null) {
				if (entities.get(i).posX == x && entities.get(i).posY == y) {
					return false;
				}
			}
		}
		return true;
	}
	
	/*
	 Return true if position is not solid
	 */
	public boolean posNotSolid(int x, int y) {
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i) != null) {
				if (entities.get(i).posX == x && entities.get(i).posY == y && entities.get(i).hasCollision()) {
					return false;
				}
			}
		}
		return true;
	}
	
	
	/*
	 Return true if creature at x and y position
	 */
	public boolean posHasCreature(int x, int y) {
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i) != null) {
				if (entities.get(i) instanceof Creature && entities.get(i).posX == x && entities.get(i).posY == y) {
					return true;
				}
			}
		}
		return false;
	}
	
	/*
	 Remove entity from ArrayList using index
	 */
	public boolean removeEntity(int i) {
		if (i >= 0) {
			entities.remove(i);
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 Remove food from ArrayList using Food object
	 */
	public boolean removeFood(Food food) {
		return entities.remove(food);
	}
}
