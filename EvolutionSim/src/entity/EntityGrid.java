package entity;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import main.GlobalData;

public class EntityGrid {
	private Entity[][] entities;
	private GlobalData globalData;
	private int numFood;
	private ArrayList<Integer> xVals;
	private ArrayList<Integer> yVals;
	static private int dirX[] = { -1, 0, 1, 0 };
	static private int dirY[] = { 0, 1, 0, -1 };
	
	
	/*
	 EntityCollection constructor
	 */
	public EntityGrid() {
		numFood = 0;
		globalData = GlobalData.getInstance();
		entities = new Entity[globalData.getMaxScreenCol()][globalData.getMaxScreenRow()];
		xVals = new ArrayList<Integer>();
		yVals = new ArrayList<Integer>();
		for (int i = 0; i < globalData.getMaxScreenCol(); i++) {
			xVals.add(i);
		}
		for (int i = 0; i < globalData.getMaxScreenRow(); i++) {
			yVals.add(i);
		}
	}

	/*
	 Add entity at its posX, posY
	 */
	public void addEntity(Entity entity) {
		entities[entity.getPosX()][entity.getPosY()] = entity;
		if (entity instanceof Food)
			numFood++;
	}
	
	/*
	 Set x,y to null
	 */
	public void setNull(int x, int y) {
		entities[x][y] = null;
	}
	
	/*
	 Move entity from previous location to new x, y
	 */
	public void moveEntity(Entity entity, int newX, int newY) {
		entities[entity.getPosX()][entity.getPosY()] = null;
		entities[newX][newY] = entity;
	}
	
	/*
	 Get entity at x, y
	 */
	public Entity getEntity(int x, int y) {
		if (entities[x][y] != null)
			return entities[x][y];
		else
			return new Entity(x, y);
	}
	
	/*
	 Lower number of food by 1
	 */
	public void decrementNumFood() {
		numFood--;
	}

	/*
	 Update entities every frame
	 */
	public void update() {
		Collections.shuffle(xVals);
		for (int i = 0; i < entities.length; i++) {
			Collections.shuffle(yVals);
			for (int j = 0; j < entities[i].length; j++) {
				if (entities[xVals.get(i)][yVals.get(j)] != null)
					if (!entities[xVals.get(i)][yVals.get(j)].isUpToDate())
						entities[xVals.get(i)][yVals.get(j)].update();
			}
		}
		
		for (int i = 0; i < entities.length; i++) {
			for (int j = 0; j < entities[i].length; j++) {
				if (entities[i][j] != null)
					entities[i][j].setUpToDate(false);
			}
		}
				
		respawnFood();
	}
	
	public void respawnFood() {
		if (globalData.getTimerPanel().getTime() % globalData.getFoodRespawnTime() == 0 && globalData.doesRandomFoodSpawn()) {
			int newX, newY;
			for (int i = 0; i < globalData.getNumFoodSpawn(); i++) {
				if (numFood >= globalData.getMaxNumFood()) {
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
				this.addEntity(new Food(newX, newY));
			}
		}
	}
	
	public boolean isValid(boolean visited[][], int x, int y) {
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
	 Draw entities every frame
	 */
	public void draw(Graphics2D g2) {
		for (int i = 0; i < entities.length; i++) {
			for (int j = 0; j < entities[i].length; j++) {
				Entity entity = entities[i][j];
				if (entity != null) {
					entity.draw(g2);
				}
			}
		}
	}
	
	
	/*
	 Return true if x and y position is empty
	 */
	public boolean posEmpty(int x, int y) {
		return (entities[x][y] == null);
	}
	
	/*
	 Return true if position is not solid
	 */
	public boolean posNotSolid(int x, int y) {
		if (entities[x][y] != null) {
			return !entities[x][y].hasCollision();
		}
		return true;
	}
	
	
}
