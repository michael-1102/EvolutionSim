package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import main.GlobalData;

public class Creature extends Entity {
	
	Node[][] nodes;
	Node startNode, goalNode, currentNode;
	ArrayList<Node> openList;
	ArrayList<Node> checkedList;
	boolean goalReached;
	
	private GlobalData globalData;
	
	// attributes
	private int energy;
	private int maxEnergy;
	private Behavior behavior;
	private int daySight;
	private int nightSight;
	private int maxSteps; // number of steps that creature is willing to travel for mate/food
	private int maxEnergyDuringMating; // amount of energy that creature is willing to give to mate
	private Color color;
	// end of attributes
	
	Creature mate;
	
	static private int dirX[] = { -1, 0, 1, 0 };
	static private int dirY[] = { 0, 1, 0, -1 };
	static private List<Integer> dirOptions = Arrays.asList(0, 1, 2, 3);
	
	private int sight;
	private boolean doneMating; // true if creature is done mating but mate is not done
	private int steps; // number of steps that have been taken toward mate/food
	private int energySpentMating; // number of energy spent mating thus far
	
	/*
	 Creature constructor
	 */
	public Creature(int x, int y, Color color, int energy, int maxEnergy, int daySight, int nightSight, int maxEnergyDuringMating, Behavior behavior) {
		super(x, y);
		
		globalData = GlobalData.getInstance();
		
		this.color = color;
		this.energy = energy;
		this.maxEnergy = maxEnergy;
		this.daySight = daySight;
		this.nightSight= nightSight;
		this.behavior = behavior;
		this.maxEnergyDuringMating = maxEnergyDuringMating;
		maxSteps = globalData.maxScreenCol * globalData.maxScreenRow;

		
		nodes = new Node[globalData.maxScreenCol][globalData.maxScreenRow];
		
		doneMating = false;
		steps = 0;
		energySpentMating = 0;	
	}
	
	/*
	 Redraw creature every frame
	 */
	public void draw(Graphics2D g2) {
		g2.setColor(color);
		g2.fillRect(posX*globalData.tileSize, posY*globalData.tileSize, globalData.tileSize, globalData.tileSize);
	}
	
	/*
	 Create node for every tile
	 */
	private void initializeNodes() {
		int col = 0;
		int row = 0;
		while(col < globalData.maxScreenCol && row < globalData.maxScreenRow) {
				nodes[col][row] = new Node(col, row);
				col++;
				if (col == globalData.maxScreenCol) {
					col = 0;
					row++;
				}
			
			
		}
	}
	
	/*
	 Set starting node in path finding
	 */
	private void setStartNode(int col, int row) {
		nodes[col][row].setAsStart();
		startNode = nodes[col][row];
		currentNode = startNode;
	}
	
	/*
	 Set node to path find to
	 */
	private void setGoalNode(int col, int row) {
		nodes[col][row].setAsGoal();
		goalNode = nodes[col][row];
	}
	
	/*
	 Get cost of every node
	 */
	private void setCostOnNodes() {
		int col = 0;
		int row = 0;
		while(col < globalData.maxScreenCol && row < globalData.maxScreenRow) {
			getCost(nodes[col][row]);	
			if (!globalData.getEntities().posNotSolid(col, row)) {
				nodes[col][row].setAsSolid();
			}
			col++;
			if (col == globalData.maxScreenCol) {
				col = 0;
				row++;
			}
		}
	}
	
	/*
	 Get cost of path to node
	 */
	private void getCost(Node node) {
		int xDistance = Math.abs(node.col - startNode.col);
		int yDistance = Math.abs(node.row - startNode.row);
		node.gCost = xDistance + yDistance; // G = distance from start node
		
		xDistance = Math.abs(node.col - goalNode.col);
		yDistance = Math.abs(node.row - goalNode.row);
		node.hCost = xDistance + yDistance; // H = distance from goal node
		
		node.fCost = node.gCost + node.hCost; // F = G + H
	}
	
	/*
	 Search for path
	 */
	private boolean search() {
		while (true) {
			int col = currentNode.col;
			int row = currentNode.row;
			
			currentNode.setAsChecked();
			checkedList.add(currentNode);
			openList.remove(currentNode);
			
			if (row > 0) {
				openNode(nodes[col][row-1]); // up
			}
			if (col > 0) {
				openNode(nodes[col-1][row]); // left
			}
			if (row < globalData.maxScreenRow - 1) {
				openNode(nodes[col][row+1]); // down
			}
			if (col < globalData.maxScreenCol - 1) {
				openNode(nodes[col+1][row]); // right
			}
			
			// find best node
			int bestNodeIndex = 0;
			int bestNodefCost = 999;
			for (int i = 0; i < openList.size(); i++) {
				
				// check F cost
				if (openList.get(i).fCost < bestNodefCost) {
					bestNodeIndex = i;
					bestNodefCost = openList.get(i).fCost;
				} else if (openList.get(i).fCost == bestNodefCost) {// if F cost equal, check G cost
					if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
						bestNodeIndex = i;
					}
				}
			}
			if (openList.size() > 0)
				currentNode = openList.get(bestNodeIndex);
			if (currentNode == goalNode) {
				return true;
			}
			steps++;
			if (steps > maxSteps) {
				steps = 0;
				return false;
			}
		}
		

	}
	
	/*
	 Add node to open list
	 */
	private void openNode(Node node) {
		if (node.open == false && node.checked == false && (node.solid == false || node.goal == true)) {
			node.setAsOpen();
			node.parent = currentNode;
			openList.add(node);
		}
	}
	
	/*
	 Get next step in path
	 */
	private int[] trackPath() {
		Node current = goalNode;
		int[] nextStep = {goalNode.col, goalNode.row};
		while (current != startNode) {
			current = current.parent;
			if (current != startNode) {
				nextStep[0] = current.col;
				nextStep[1] = current.row;
			}
		}
		return nextStep;
	}
	
	/*
	 Return true if no energy
	 */
	public boolean isDead() {
		if (energy <= 0) {
			return true;
		}
		return false;
	}
	
	/*
	 Return color
	 */
	public Color getColor() {
		return color;
	}
	
	/*
	 Update creature every frame
	 */
	public void update() {	
		// set sight
		if (globalData.getTimerPanel().isDay()) {
			sight = daySight;
		} else {
			sight = nightSight;
		}
		
		
		// movement behavior
		switch(behavior) {
		case eat:
			this.moveToFood();
			break;
		case idle:
			break;
		case mate:
			this.doMating();
			break;
		case findMate:
			this.moveToMate();
			break;
		case random:
			this.moveRandom();
			break;
		default:
			break;
			
		}
		
		// check for food
		EntityCollection entities = globalData.getEntities();
		int foodIndex = entities.posHasFood(posX, posY);
		if (foodIndex >= 0) {
			this.eatFood(entities, foodIndex);
		}
	}
	
	/*
	 Mate
	 */
	private void doMating() {
		if (checkForMate()) {
			if (energySpentMating < maxEnergyDuringMating) {
				energy--;
				energySpentMating++;
			} else {
				doneMating = true;
				if (mate.isDoneMating()) {
					createBaby(mate);
					this.resetBehavior();
					mate.resetBehavior();
				}
			}
		} else {
			resetBehavior();
		}
	}
	
	/*
	 Create baby
	 */
	private void createBaby(Creature mate) {
		int babyEnergy = this.energySpentMating + mate.getEnergySpentMating();
		
		// need to add variation to these
		int red = (this.color.getRed() + mate.getColor().getRed()) / 2;
		int green = (this.color.getGreen() + mate.getColor().getGreen()) / 2;
		int blue = (this.color.getBlue() + mate.getColor().getBlue()) / 2;
		Color babyColor = new Color(red, green, blue);
		
		int babyMaxEnergy = (this.maxEnergy + mate.getMaxEnergy()) / 2;
		
		int babyMaxEnergyDuringMating = (this.maxEnergyDuringMating + mate.getMaxEnergyDuringMating()) / 2;
		
		globalData.getNewEntities().add(new Creature(posX, posY+1, babyColor, babyEnergy, babyMaxEnergy, 20, 20, babyMaxEnergyDuringMating, Behavior.random));
	}
	
	/*
	 Return max energy during mating
	 */
	private int getMaxEnergyDuringMating() {
		return maxEnergyDuringMating;
	}
	
	/*
	 Return max energy
	 */
	private int getMaxEnergy() {
		return maxEnergy;
	}
	
	/*
	 return how much energy this creature has given to its offspring
	 */
	private int getEnergySpentMating() {
		return energySpentMating;
	}
	
	/*
	 return true if done mating
	 */
	private boolean isDoneMating() {
		return doneMating;
	}
	
	/*
	 Check if mate exists and is alive, set mate to null if mate is dead
	 */
	private boolean checkForMate() {
		if (mate == null) {
			return false;
		}
		if (mate.getEnergy() <= 0 || !mate.getBehavior().equals(Behavior.mate)) {
			mate = null;
			return false;
		}
		return true;
	}
	
	/*
	 Set behavior according to schedule
	 */
	private void resetBehavior() {
		doneMating = false;
		energySpentMating = 0;
		//placeholder
		behavior = Behavior.random;
	}
	
	/*
	 Eat food at EntityCollection index
	 */
	private void eatFood(EntityCollection foods, int i) {
		Food food = (Food) foods.getEntity(i);
		if (food.isDead()) return;
		food.assertEaten();
		energy += globalData.getFoodEnergy();
		if (energy > maxEnergy)
			energy = maxEnergy;
	}
	
	/*
	 Set mate
	 */
	private void setMate(Creature mate) {
		doneMating = false;
		this.mate = mate;
		this.behavior = Behavior.mate;
	}
	
	/*
	 Move creature towards mate
	 */
	private void moveToMate() {
		Entity mate = mateBFS(new boolean[globalData.maxScreenCol][globalData.maxScreenRow], posX, posY);
		if (mate instanceof Creature) {
			int[] matePos = mate.getLocation();
			if (this.getDistance(matePos) == 1) {
				this.setMate((Creature)mate);
				((Creature)mate).setMate(this);
			} else {
				int[] nextStep = findNextStep(matePos);
				if (nextStep.length == 2) {
					this.move(nextStep[0], nextStep[1]);
				} else {
					this.moveRandom();
				}
			}
		} else {
			this.moveRandom();
		}
	}
	
	private int getDistance(int[] pos) {
		return Math.abs(pos[0] - posX)  + Math.abs(pos[1] - posY);
	}
	
	/*
	 Move creature towards food
	 */
	private void moveToFood() {
		int[] foodPos = foodBFS(new boolean[globalData.maxScreenCol][globalData.maxScreenRow], posX, posY);
		if (foodPos.length == 2) {
			
			int[] nextStep = findNextStep(foodPos);
			if (nextStep.length == 2) {
				this.move(nextStep[0], nextStep[1]);
			} else {
				this.moveRandom();
			}
		} else {
			this.moveRandom();
		}
	}
	
	private int[] findNextStep(int[] foodPos) {
		openList = new ArrayList<Node>();
		checkedList = new ArrayList<Node>();
		this.initializeNodes();
		this.setStartNode(posX, posY);
		this.setGoalNode(foodPos[0], foodPos[1]);
		this.setCostOnNodes();
		if (this.search())
			return trackPath();
		else
			return new int[1];
	}
	
	/*
	 returns true if location is not visited and within the screen
	 */
	private boolean isValid(boolean visited[][], int x, int y) {
		if (x < 0 || y < 0 || x >= globalData.maxScreenCol || y>= globalData.maxScreenRow) {
			return false;
		}
		if (visited[x][y]) {
			return false;
		}
		return true;
	}
	
	/*
	 returns true if location is within the screen
	 */
	private boolean isValid(int x, int y) {
		return !(x < 0 || y < 0 || x >= globalData.maxScreenCol || y>= globalData.maxScreenRow);	
	}
	
	/*
	 returns location of food
	 */
	private int[] foodBFS(boolean visited[][], int x, int y) {
		Queue<int[]> q = new LinkedList<>();
		q.add(new int[] {x,y});
		visited[x][y] = true;
		int[] foodPos = new int[2]; 
		while (!q.isEmpty()) {
			foodPos = q.peek();
			if (Math.abs(foodPos[0] - x) + Math.abs(foodPos[1] - y) > sight) return new int[0];
			if (globalData.getEntities().posHasFood(foodPos[0], foodPos[1]) >= 0) {
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
	 returns generic entity if no mate found, otherwise returns potential mate
	 */
	private Entity mateBFS(boolean visited[][], int x, int y) {
		Queue<int[]> q = new LinkedList<>();
		visited[x][y] = true;
		int[] matePos = new int[2];
		do {
			ArrayList<int[]> list = new ArrayList<int[]>();
			for (int i = 0; i < 4; i++) {
				int adjx = matePos[0] + dirX[i];
				int adjy = matePos[1] + dirY[i];
				if (isValid(visited, adjx, adjy)) {
					list.add(new int[] {adjx, adjy});
					visited[adjx][adjy] = true;
				}
			}
			Collections.shuffle(list, new Random());
			for (int i = 0; i < list.size(); i++) {
				q.add(list.get(i));
			}
			
			matePos = q.peek();
			if (Math.abs(matePos[0] - x) + Math.abs(matePos[1] - y) > sight) return new Entity(0, 0);
			Entity entity = globalData.getEntities().getCreature(matePos[0], matePos[1]);
			if (entity instanceof Creature) {
				if (((Creature)entity).getBehavior().equals(Behavior.findMate)) {
					return entity;
				}
				
			}
			
			q.remove();
		} while (!q.isEmpty());
		return new Entity(0, 0);
	}
	
	/*
	 Move creature randomly
	 */
	private void moveRandom() {
		Collections.shuffle(dirOptions);
		for (int i = 0; i < 4; i++) {
				int newX = posX+dirX[dirOptions.get(i)];
				int newY = posY+dirY[dirOptions.get(i)];
				if (isValid(newX, newY) && globalData.getEntities().posEmpty(newX, newY)) {
					this.move(newX, newY);
					return;
				}
					
		}
	}

	/*
	 Move creature to X and Y value
	 */
	private void move(int x, int y) {
		posX = x;
		posY = y;
		energy--;
	}
	
	/*
	 Return energy of creature
	 */
	public int getEnergy() {
		return energy;
	}
	
	/*
	 Return behavior of creature
	 */
	public Behavior getBehavior() {
		return behavior;
	}
	
	/*
	 Creature has collision
	 */
	@Override
	public boolean hasCollision() {
		return true;
	}
}
