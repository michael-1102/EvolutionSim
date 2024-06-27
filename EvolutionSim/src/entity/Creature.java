package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
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
	private int energy;
	private int maxEnergy;
	private Behavior behavior;
	private int sight;
	
	static private int dirX[] = { -1, 0, 1, 0 };
	static private int dirY[] = { 0, 1, 0, -1 };
	
	private int maxSteps;
	private int steps;
	
	/*
	 Creature constructor
	 */
	public Creature(int x, int y, int energy, int maxEnergy, int sight, Behavior behavior) {
		super(x, y);
		this.energy = energy;
		this.maxEnergy = maxEnergy;
		this.sight = sight;
		this.behavior = behavior;
		globalData = GlobalData.getInstance();
		nodes = new Node[globalData.maxScreenCol][globalData.maxScreenRow];
		
		steps = 0;
		maxSteps = globalData.maxScreenCol * globalData.maxScreenRow;
	}
	
	/*
	 Redraw creature every frame
	 */
	public void draw(Graphics2D g2) {
		g2.setColor(Color.red);
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
		if (node.open == false && node.checked == false && node.solid == false) {
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
		case mating:
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
	
	private boolean isValidBFS(boolean visited[][], int x, int y) {
		if (x < 0 || y < 0 || x >= globalData.maxScreenCol || y>= globalData.maxScreenRow) {
			return false;
		}
		if (visited[x][y]) {
			return false;
		}
		return true;
	}
	
	
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
				if (isValidBFS(visited, adjx, adjy)) {
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
				if (posY <= 0) {
					moveRandom();
					return;
				} else if (!globalData.getEntities().posEmpty(posX, posY-1)) {
					moveRandom();
					return;
				} else {
					this.move(posX, posY-1);
				}
				break;
			case(1): // 1 = right
				if (posX >= globalData.maxScreenCol - 1) {
					moveRandom();
					return;
				} else if (!globalData.getEntities().posEmpty(posX+1, posY)) {
					moveRandom();
					return;
				} else {
					this.move(posX+1, posY);
				}
				break;
			case(2): // 2 = down
				if (posY >= globalData.maxScreenRow - 1) {
					moveRandom();
					return;
				} else if (!globalData.getEntities().posEmpty(posX, posY+1)) {
					moveRandom();
					return;
				} else {
					this.move(posX, posY+1);
				}
				break;
			case(3): // 3 = left
				if (posX <= 0) {
					moveRandom();
					return;
				} else if (!globalData.getEntities().posEmpty(posX-1, posY)) {
					moveRandom();
					return;
				} else {
					this.move(posX-1, posY);
				}
				break;
			default:
				break;
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
	 Creature has collision
	 */
	@Override
	public boolean hasCollision() {
		return true;
	}
}
