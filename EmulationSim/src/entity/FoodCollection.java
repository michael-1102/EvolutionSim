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

public class FoodCollection {
	private ArrayList<Food> foods;
	private GlobalData globalData;
	private Panel p;
	static private int dirX[] = { -1, 0, 1, 0 };
	static private int dirY[] = { 0, 1, 0, -1 };
	
	/*
	 FoodList constructor
	 */
	public FoodCollection(Panel p) {
		foods = new ArrayList<Food>();
		globalData = GlobalData.getInstance();
		this.p = p;
	}
	
	/*
	 Get ArrayList of food
	 */
	public ArrayList<Food> getFoods() {
		return foods;
	}
	
	/*
	 Add food to ArrayList
	 */
	public void addFood(Food food) {
		foods.add(food);
	}
	
	/*
	 Update food every frame
	 */
	public void update() {
		for (Iterator<Food> iterator = foods.iterator(); iterator.hasNext();) {
		    Food food = iterator.next();
		    food.update();
		    if (food.getAge() >= globalData.getMaxFoodAge()) {
		    	iterator.remove();
		    }
		}
		
		if (p.foodTimer == globalData.getFoodRespawnTime()) {
			p.foodTimer = 0;
			int newX, newY;
			for (int i = 0; i < globalData.getNumFoodSpawn(); i++) {
				if (foods.size() >= globalData.getMaxNumFood()) {
					return;
				}
				newX = (int)(Math.random() * p.maxScreenCol); 
				newY = (int)(Math.random() * p.maxScreenRow);
				if (this.posHasFood(newX, newY) >= 0) {
					int[] newPos = BFS(new boolean[p.maxScreenCol][p.maxScreenRow], newX, newY);
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
	
	private int[] BFS(boolean visited[][], int x, int y) {
		Queue<int[]> q = new LinkedList<>();
		q.add(new int[] {x,y});
		visited[x][y] = true;
		int[] foodPos = new int[2]; 
		while (!q.isEmpty()) {
			foodPos = q.peek();
			if (globalData.getFoods().posHasFood(foodPos[0], foodPos[1]) < 0) {
				return foodPos;
			}
			
			q.remove();
			
			ArrayList<int[]> list = new ArrayList<int[]>();
			for (int i = 0; i < 4; i++) {
				int adjx = foodPos[0] + dirX[i];
				int adjy = foodPos[1] + dirY[i];
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
		for (Food food : foods) {
			food.draw(g2);
		}
	}
	
	/*
	 Return index of food if there is food at x and y position, return -1 otherwise
	 */
	public int posHasFood(int x, int y) {
		for (int i = 0; i < foods.size(); i++) {
			if (foods.get(i).posX == x && foods.get(i).posY == y) {
				return i;
			}
		}
		return -1;
	}
	
	/*
	 Remove food from ArrayList using index
	 */
	public boolean removeFood(int i) {
		if (i >= 0) {
			foods.remove(i);
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 Remove food from ArrayList using Food object
	 */
	public boolean removeFood(Food food) {
		return foods.remove(food);
	}
}
