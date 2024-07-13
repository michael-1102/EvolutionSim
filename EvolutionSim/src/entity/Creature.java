package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import javax.swing.JButton;

import main.GlobalData;

public class Creature extends Entity implements ActionListener {
	
	private JButton button;
	
	private Node[][] nodes;
	private Node startNode, goalNode, currentNode;
	private ArrayList<Node> openList;
	boolean goalReached;
	
	private GlobalData globalData;
	
	// attributes
	private int slowness;
	private int energy;
	private int maxEnergy;
	private int daySight;
	private int nightSight;
	private Schedule schedule;
	private int maxEnergyDuringMating; // amount of energy that creature is willing to give to mate
	private Color color;
	private int mateCooldown; // number of frames after mating / being born during which the creature cannot mate
	// end of attributes
	
	private Creature mate;
	
	static private int dirX[] = { -1, 0, 1, 0 };
	static private int dirY[] = { 0, 1, 0, -1 };
	static private List<Integer> dirOptions = Arrays.asList(0, 1, 2, 3);
	
	private Behavior behavior; // current creature behavior
	
	private int sight;
	private boolean doneMating; // true if creature is done mating but mate is not done
	private int steps; // how many tiles checked during search
	private int maxSteps; // max number of tiles checked during search before search is given up
	private int energySpentMating; // number of energy spent mating thus far
	private int currentMateCooldown;
	
	/*
	 Creature constructor
	 */
	public Creature(int x, int y, Color color, int slowness, int energy, int maxEnergy, int daySight, int nightSight, int maxEnergyDuringMating, int mateCooldown, Schedule schedule) {
		super(x, y);
		
		globalData = GlobalData.getInstance();
		
		this.slowness = slowness;
		this.color = color;
		this.energy = energy;
		this.maxEnergy = maxEnergy;
		this.daySight = daySight;
		this.nightSight= nightSight;
		this.schedule = schedule;
		this.maxEnergyDuringMating = maxEnergyDuringMating;
		this.mateCooldown = mateCooldown;
		maxSteps = globalData.maxScreenCol * globalData.maxScreenRow;
		
		
		nodes = new Node[globalData.maxScreenCol][globalData.maxScreenRow];
		
		doneMating = false;
		steps = 0;
		energySpentMating = 0;	
		behavior = Behavior.idle;
		
		button = new JButton();
		button.setBounds(posX*globalData.tileSize, posY*globalData.tileSize, globalData.tileSize, globalData.tileSize);
		button.addActionListener(this);
		globalData.getGridPanel().add(button);
		globalData.getFrame().pack();
	}
	
	/*
	 Redraw creature every frame
	 */
	public void draw(Graphics2D g2) {
		button.setBounds(posX*globalData.tileSize, posY*globalData.tileSize, globalData.tileSize, globalData.tileSize);
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
	
	public void actionPerformed(ActionEvent e){  
		System.out.println("pressed");
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
	 Update creature every frame
	 */
	public void update() {	
		// set sight
		if (globalData.getTimerPanel().isDay()) {
			sight = daySight;
		} else {
			sight = nightSight;
		}
		
		if (behavior.isSchedulable())
			behavior = schedule.getCurrentBehavior();
		
		
		if (!(behavior.isMoving() && globalData.getTimerPanel().getTime() % slowness != 0)) { // don't do anything if moving but not time to move
			// behavior
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
		}
		
		if (currentMateCooldown > 0) {
			currentMateCooldown--;
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
				if (mate.doneMating) {
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
		
		int babyEnergy = this.energySpentMating + mate.energySpentMating;
		
		int babySlowness = getBabyInt(this.slowness, mate.slowness, 1);
		
		
		
		int red = getBabyInt(this.color.getRed(), mate.color.getRed(), 0, 255);
		int green = getBabyInt(this.color.getGreen(), mate.color.getGreen(), 0, 255);
		int blue = getBabyInt(this.color.getBlue(), mate.color.getBlue(), 0, 255);
		Color babyColor = new Color(red, green, blue);
		
		int babyMaxEnergy = getBabyInt(this.maxEnergy, mate.maxEnergy, babyEnergy);
		
		int babyMaxEnergyDuringMating =  getBabyInt(this.maxEnergyDuringMating, mate.maxEnergyDuringMating, 1, babyMaxEnergy);
		
		int babyDaySight = getBabyInt(this.daySight, mate.daySight, 1);
		int babyNightSight = getBabyInt(this.nightSight, mate.nightSight, 1);
		
		int babyMateCooldown = getBabyInt(this.mateCooldown, mate.mateCooldown, 1);
		
		Schedule babySchedule = this.schedule.getBabySchedule(mate.schedule);
		
		int[] babyPos = this.getBabyPos(new int[] {mate.posX, mate.posY});
		if (babyPos.length == 2) {
			Creature baby = new Creature(babyPos[0], babyPos[1], babyColor, babySlowness, babyEnergy, babyMaxEnergy, babyDaySight, babyNightSight, babyMaxEnergyDuringMating, babyMateCooldown, babySchedule);
			baby.setCurrentMateCooldown(babyMateCooldown);
			globalData.getNewEntities().add(baby);
		}
		this.currentMateCooldown = this.mateCooldown;
		mate.currentMateCooldown = mate.mateCooldown;
	}
	
	/*
	 Calculate the stat for a baby based on parents' stats and min
	 */
	private int getBabyInt(int stat1, int stat2, int min) {
		int babyStat = getBabyInt(stat1, stat2);
		if (babyStat < min) {
			babyStat = min;
		}
		return babyStat;
	}
	
	/*
	 Calculate the stat for a baby based on parents' stats
	 */
	private int getBabyInt(int stat1, int stat2) {
		double percent = Math.random();
		double babyStatDouble = ((stat1*percent) + (stat2*(1.0-percent)));
		
		double percentMutation = (Math.random() * 10.0 - 5.0) / 100.0;
		int babyStatInt = (int) Math.round(babyStatDouble * (1.0+percentMutation));
		return babyStatInt;
	}
	
	/*
	 Calculate the stat for a baby based on parents' stats and min and max
	 */
	private int getBabyInt(int stat1, int stat2, int min, int max) {
		int babyStat = getBabyInt(stat1, stat2, min);
		if (babyStat > max) {
			babyStat = max;
		}
		return babyStat;
	}
	
	private int[] getBabyPos(int[] matePos) {
		Collections.shuffle(dirOptions);
		int[] babyPos = new int[2];
		for (int i = 0; i < 4; i++) {
				babyPos[0] = posX+dirX[dirOptions.get(i)];
				babyPos[1] = posY+dirY[dirOptions.get(i)];
				if (globalData.getEntities().posNotSolid(babyPos[0], babyPos[1])) {
					return babyPos;
				}
		}
		for (int i = 0; i < 4; i++) {
			babyPos[0] = matePos[0]+dirX[dirOptions.get(i)];
			babyPos[1] = matePos[1]+dirY[dirOptions.get(i)];
			if (globalData.getEntities().posNotSolid(babyPos[0], babyPos[1])) {
				return babyPos;
			}
		}
		return new int[1];
	}
	
	
	/*
	 Check if mate exists and is alive, set mate to null if mate is dead
	 */
	private boolean checkForMate() {
		if (mate == null) {
			return false;
		}
		if (mate.energy <= 0 || !mate.behavior.equals(Behavior.mate)) {
			mate = null;
			return false;
		}
		return true;
	}
	
	/*
	 Set mate cooldown (for when baby is born)
	 */
	private void setCurrentMateCooldown(int currentMateCooldown) {
		this.currentMateCooldown = currentMateCooldown;
	}
	
	/*
	 Set behavior according to schedule
	 */
	private void resetBehavior() {
		doneMating = false;
		energySpentMating = 0;
		
		behavior = schedule.getCurrentBehavior();
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
		if (currentMateCooldown > 0) {
			this.moveToFood();
			return;
		}
		
		Entity mate = mateBFS(new boolean[globalData.maxScreenCol][globalData.maxScreenRow], posX, posY);
		if (mate instanceof Creature) {
			int[] matePos = {mate.posX, mate.posY};
			if (this.getDistance(matePos) == 1) {
				this.setMate((Creature)mate);
				((Creature)mate).setMate(this);
			} else {
				int[] nextStep = findNextStep(matePos);
				if (nextStep.length == 2) {
					this.moveCreature(nextStep[0], nextStep[1]);
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
				this.moveCreature(nextStep[0], nextStep[1]);
			} else {
				this.moveRandom();
			}
		} else {
			this.moveRandom();
		}
	}
	
	private int[] findNextStep(int[] foodPos) {
		openList = new ArrayList<Node>();
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
		q.add(new int[] {x,y});
		while (!q.isEmpty()) {
			matePos = q.peek();
			if (Math.abs(matePos[0] - x) + Math.abs(matePos[1] - y) > sight) return new Entity(0, 0);
			Entity entity = globalData.getEntities().getCreature(matePos[0], matePos[1]);
			if (entity instanceof Creature && !entity.equals(this)) {
				if (((Creature)entity).getBehavior().equals(Behavior.findMate)) {
					return entity;
				}
				
			}
			
			q.remove();
			
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
		}
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
					this.moveCreature(newX, newY);
					return;
				}	
		}
	}

	/*
	 Move creature to X and Y value
	 */
	private void moveCreature(int x, int y) {
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
