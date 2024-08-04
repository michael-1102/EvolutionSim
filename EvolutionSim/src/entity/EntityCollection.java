package entity;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import main.GlobalData;

public class EntityCollection {
	private ArrayList<Entity> entities;
	private GlobalData globalData;
	static private int dirX[] = { -1, 0, 1, 0 };
	static private int dirY[] = { 0, 1, 0, -1 };
	
	/*
	 FoodList constructor
	 */
	public EntityCollection() {
		entities = new ArrayList<Entity>();
		globalData = GlobalData.getInstance();
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
	 Update entities every frame
	 */
	public void update() {
		Collections.shuffle(entities);
		for (Iterator<Entity> iterator = entities.iterator(); iterator.hasNext();) {
		    Entity entity = iterator.next();
		    entity.update();
		}
		
		// remove dead entities
		entities.removeIf(x -> x.isDead());
		
		// add new entities
		for (Entity entity : globalData.getNewEntities()) {
			entities.add(entity);
		}
		globalData.getNewEntities().clear();
		
		// respawn food
		if (globalData.getTimerPanel().getTime() % globalData.getFoodRespawnTime() == 0 && globalData.doesRandomFoodSpawn()) {
			int newX, newY;
			for (int i = 0; i < globalData.getNumFoodSpawn(); i++) {
				if (entities.size() >= globalData.getMaxNumFood()) {
					return;
				}
				int maxScreenCol = globalData.getMaxScreenCol();
				int maxScreenRow = globalData.getMaxScreenRow();

				newX = (int)(Math.random() * maxScreenCol); 
				newY = (int)(Math.random() * maxScreenRow);
				if (!this.posEmpty(newX, newY)) {
					int[] newPos = emptyBFS(new boolean[maxScreenCol][maxScreenRow], newX, newY);
					if (newPos.length == 2) {
						newX = newPos[0];
						newY = newPos[1];
					} else {
						return;
					}
				}
				addFood(new Food(newX, newY));
			}
		}
	}
	
	private boolean isValid(boolean visited[][], int x, int y) {
		if (x < 0 || y < 0 || x >= globalData.getMaxScreenCol() || y>= globalData.getMaxScreenRow()) {
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
		Object[] arr = entities.toArray();
		for (Object entity : arr) {
			if (entity != null)
				((Entity)entity).draw(g2);
		}
	}
	
	/*
	 Return index of food if there is food at x and y position, return -1 otherwise
	 */
	public int posHasFood(int x, int y) {
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i) != null) {
				if (entities.get(i) instanceof Food && entities.get(i).posX == x && entities.get(i).posY == y) {
					if (!((Food)entities.get(i)).isDead())
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
	 Return creature if there is one at x and y value
	 */
	public Entity getCreature(int x, int y) {
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i) != null) {
				if (entities.get(i) instanceof Creature && entities.get(i).posX == x && entities.get(i).posY == y) {
					return entities.get(i);
				}
			}
		}
		return new Entity(x, y);
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
