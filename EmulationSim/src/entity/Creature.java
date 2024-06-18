package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import main.GlobalData;
import main.Panel;

public class Creature extends Entity {
	
	private GlobalData globalData;
	private int energy;
	private int maxEnergy;
	private Behavior behavior;
	private int sight;
	
	static private int dirX[] = { -1, 0, 1, 0 };
	static private int dirY[] = { 0, 1, 0, -1 };
	/*
	 Creature constructor
	 */
	public Creature(int x, int y, Panel p, int energy, int maxEnergy, int sight) {
		super(x, y, p);
		this.energy = energy;
		this.maxEnergy = maxEnergy;
		this.sight = sight;
		behavior = Behavior.eat;
		globalData = GlobalData.getInstance();
	}
	
	/*
	 Redraw creature every frame
	 */
	public void draw(Graphics2D g2) {
		g2.setColor(Color.red);
		g2.fillRect(posX*p.tileSize, posY*p.tileSize, p.tileSize, p.tileSize);
	}
	
	/*
	 Update creature every frame
	 */
	public void update() {		
		// movement behavior
		switch(behavior) {
		case eat:
			this.moveToFood();
			break;
		case idle:
			break;
		case mate:
			
			break;
		case random:
			this.moveRandom();
			break;
		default:
			break;
			
		}
		
		// check maxScreenColfor food
		FoodCollection foods = globalData.getFoods();
		int foodIndex = foods.posHasFood(posX, posY);
		if (foodIndex >= 0) {
			this.eatFood(foods, foodIndex);
		}
	}
	
	
	/*
	 Eat food at FoodCollection index
	 */
	private void eatFood(FoodCollection foods, int i) {
		foods.removeFood(i);
		energy += globalData.getFoodEnergy();
		if (energy > maxEnergy)
			energy = maxEnergy;
	}
	
	/*
	 Move creature towards food
	 */
	private void moveToFood() {
		int[] foodPos = BFS(new boolean[p.maxScreenCol][p.maxScreenRow], posX, posY);
		if (foodPos.length == 2) {
			int difX = foodPos[0] - posX;
			int difY = foodPos[1] - posY;
			if (Math.abs(difX) > Math.abs(difY)) {
				if (difX > 0) {
					posX++;
				} else {
					posX--;
				}
			} else {
				if (difY > 0) {
					posY++;
				} else {
					posY--;
				}
			}
		} else {
			this.moveRandom();
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
			if (Math.abs(foodPos[0] - x) + Math.abs(foodPos[1] - y) > sight) return new int[0];
			if (globalData.getFoods().posHasFood(foodPos[0], foodPos[1]) >= 0) {
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
	 Move creature randomly
	 */
	private void moveRandom() {
		int dir = (int)(Math.random() * 4); 
		switch(dir) {
			case(0): // 0 = up
				if (posY == 0) {
					moveRandom();
					return;
				} else {
					posY--;
					energy--;
				}
				break;
			case(1): // 1 = right
				if (posX == p.maxScreenCol - 1) {
					moveRandom();
					return;
				} else {
					posX++;
					energy--;
				}
				break;
			case(2): // 2 = down
				if (posY == p.maxScreenRow - 1) {
					moveRandom();
					return;
				} else {
					posY++;
					energy--;
				}
				break;
			case(3): // 3 = left
				if (posX == 0) {
					moveRandom();
					return;
				} else {
					posX--;
					energy--;
				}
				break;
			default:
				break;
		}
	}

	/*
	 Return energy of creature
	 */
	public int getEnergy() {
		return energy;
	}
}
