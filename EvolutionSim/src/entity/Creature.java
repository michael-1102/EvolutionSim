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
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import main.GlobalData;

public class Creature extends Entity implements ActionListener {
	
	final private static int nightSightPenalty = 5; // amount of sight removed at night
	
	private static int nextCreatureNum = 1;
	private int creatureNum;
	
	private JButton button;
	
	private Node[][] nodes;
	private Node startNode, goalNode, currentNode;
	private ArrayList<Node> openList;
	boolean goalReached;
	
	private GlobalData globalData;
	
	// attributes
	private int slowness;
	private int maxEnergy;
	private int daySight;
	private int nightSight;
	private Schedule schedule;
	private Schedule backupSchedule;
	private int maxEnergyDuringMating; // amount of energy that creature is willing to give to mate
	private Color color;
	private int mateCooldown; // number of frames after mating / being born during which the creature cannot mate
	// end of attributes
	
	private int energy;
	private int fatigue;
	private int hunger;
	
	private Creature mate;
	
	private Creature parent1;
	private Creature parent2;
	private ArrayList<Creature> offspring;
	
	static private int dirX[] = { -1, 0, 1, 0 };
	static private int dirY[] = { 0, 1, 0, -1 };
	static private List<Integer> dirOptions = Arrays.asList(0, 1, 2, 3);
	
	private Behavior behavior; // current creature behavior
	
	private int generation;

	private boolean triedBackupBehavior;
	private boolean highlighted;
	private Color borderColor;
	private int sight;
	private boolean doneMating; // true if creature is done mating but mate is not done
	private int steps; // how many tiles checked during search
	private int maxSteps; // max number of tiles checked during search before search is given up
	private int energySpentMating; // number of energy spent mating thus far
	private int currentMateCooldown;
	
	private CreatureViewer viewer;
	
	/*
	 Creature constructor
	 */
	public Creature(int x, int y, Color color, int slowness, int energy, int maxEnergy, int daySight, int maxEnergyDuringMating, int mateCooldown, Schedule schedule, Schedule backupSchedule, Creature parent1, Creature parent2) {
		super(x, y);
		
		creatureNum = nextCreatureNum;
		nextCreatureNum++;
		if (!(parent1 == null || parent2 == null)) {
			if (parent1.creatureNum < parent2.creatureNum) { // parent1 should be parent with lower creature number
				this.parent1 = parent1;
				this.parent2 = parent2;
			} else {
				this.parent2 = parent1;
				this.parent1 = parent2;
			}
			
			generation = Math.max(parent1.generation, parent2.generation) + 1;
		} else {
			generation = 1;
		}
		
		offspring = new ArrayList<Creature>();
		globalData = GlobalData.getInstance();
		
		this.slowness = slowness;
		this.color = color;
		this.energy = energy;
		this.maxEnergy = maxEnergy;
		this.daySight = daySight;
		this.nightSight= globalData.getMaxSight() - daySight;
		this.schedule = schedule;
		this.backupSchedule = backupSchedule;
		this.maxEnergyDuringMating = maxEnergyDuringMating;
		this.mateCooldown = mateCooldown;
		maxSteps = globalData.getMaxScreenCol() * globalData.getMaxScreenRow();
		
		
		nodes = new Node[globalData.getMaxScreenCol()][globalData.getMaxScreenRow()];
		
		doneMating = false;
		steps = 0;
		energySpentMating = 0;	
		fatigue = 0;
		hunger = 0;
		behavior = Behavior.idle;
		borderColor = new Color(Math.abs(color.getRed() - 255), Math.abs(color.getGreen() - 255), Math.abs(color.getBlue() - 255));
		

		button = new JButton();
		int tileSize = globalData.getTileSize();
		button.setBounds(this.getPosX()*tileSize, this.getPosY()*tileSize, tileSize, tileSize);
		button.setFocusable(false);
		button.setContentAreaFilled(false);
		button.addActionListener(this);
		button.setBorder(null);
		highlighted = false;
		
		button.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
			    button.setBorder(new LineBorder(borderColor));
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
				if (!highlighted)
					button.setBorder(null);		
		    }
		});
		
		
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					globalData.getGridPanel().add(button);
				}
			});
		} catch (Exception e) {
	        e.printStackTrace();
		}	
	}
	
	/*
	 Highlights if unhighlighted, unhighlights if highlighted
	 Returns previous value of highlighted
	 */
	public boolean toggleHighlight() {
		if (highlighted) {
			button.setBorder(null);
			highlighted = false;
			return true;
		} else {
	    	button.setBorder(new LineBorder(borderColor));
			highlighted = true;
			return false;
		}
	}

	/*
	 Return parent1
	 */
	public Creature getParentOne() {
		return parent1;
	}
	
	/*
	 Return parent2
	 */
	public Creature getParentTwo() {
		return parent2;
	}
	
	/*
	 Return creatureNum
	 */
	public int getCreatureNum() {
		return creatureNum;
	}
	
	/*
	 Return daySight
	 */
	public int getDaySight() {
		return daySight;
	}
	
	/*
	 Return schedule object
	 */
	public Schedule getSchedule() {
		return schedule;
	}
	
	/*
	 Return maxEnergyDuringMating
	 */
	public int getMaxEnergyDuringMating() {
		return maxEnergyDuringMating;
	}
	
	/*
	 Return nightSight
	 */
	public int getNightSight() {
		return nightSight;
	}
	
	/*
	 Return speed
	 speed = 1.0/slowness
	 */
	public double getSpeed() {
		return 1.0 / slowness;
	}

	/*
	 Return color
	 */
	public Color getColor() {
		return color;
	}

	/*
	 Return mateCooldown
	 */
	public int getMateCooldown() {
		return mateCooldown;
	}
	
	/*
	 Return currentMateCooldown
	 */
	public int getCurrentMateCooldown() {
		return currentMateCooldown;
	}
	
	/*
	 Redraw creature every frame
	 */
	@Override
	public void draw(Graphics2D g2) {
		int posX = this.getPosX();
		int posY = this.getPosY();
		int tileSize = globalData.getTileSize();
		button.setBounds(posX*tileSize, posY*tileSize, tileSize, tileSize);
		g2.setColor(color);
		g2.fillRect(this.getPosX()*tileSize, this.getPosY()*tileSize, tileSize, tileSize);
	}
	
	/*
	 Create node for every tile
	 */
	private void initializeNodes() {
		int col = 0;
		int row = 0;
		while(col < globalData.getMaxScreenCol() && row < globalData.getMaxScreenRow()) {
				nodes[col][row] = new Node(col, row);
				col++;
				if (col == globalData.getMaxScreenCol()) {
					col = 0;
					row++;
				}
		}
	}
	
	public void actionPerformed(ActionEvent e){  
		this.openViewer();
	}  
	
	public void openViewer() {
		if (viewer == null)
			viewer = new CreatureViewer(this);
		viewer.setVisible(true);
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
		while(col < globalData.getMaxScreenCol() && row < globalData.getMaxScreenRow()) {
			getCost(nodes[col][row]);	
			if (!globalData.getEntities().posNotSolid(col, row)) {
				nodes[col][row].setAsSolid();
			}
			col++;
			if (col == globalData.getMaxScreenCol()) {
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
			if (row < globalData.getMaxScreenRow() - 1) {
				openNode(nodes[col][row+1]); // down
			}
			if (col < globalData.getMaxScreenCol() - 1) {
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
	 Return generation
	 */
	public int getGeneration() {
		return generation;
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
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						globalData.getGridPanel().remove(button);
						//viewer.setVisible(false);
					}
				});
			} catch (Exception e) {
		        e.printStackTrace();
			}
			return true;
		}
		return false;
	}
	
	/*
	 Update creature every frame
	 */
	@Override
	public void update() {
		super.update();
		
		// TODO: add hunger
		
		triedBackupBehavior = false;
		
		// set sight
		if (globalData.getTimerPanel().isDay()) {
			sight = daySight;
		} else {
			sight = nightSight - nightSightPenalty;
		}
		
		if (behavior.isSchedulable())
			behavior = schedule.getCurrentBehavior();
		
		// perform based on behavior
		this.doBehavior(behavior);
		
		if (currentMateCooldown > 0) {
			currentMateCooldown--;
		}
		;
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					// update viewer
					if (viewer != null) viewer.update();
				}
			});
		} catch (Exception e) {
	        e.printStackTrace();
		}
		if (isDead()) {
			globalData.getEntities().setNull(this.getPosX(), this.getPosY());
		}
	}
	
	private void doNothing() {
		// TODO: remove fatigue
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
	 Do given behavior
	 */
	private void doBehavior(Behavior behavior) {
		
		if (!(behavior.isMoving() && globalData.getTimerPanel().getTime() % slowness != 0)) { // don't do anything if moving but not time to move
			// behavior
			switch(behavior) {
			case eat:
				this.moveToFood();
				break;
			case idle:
				this.doNothing();
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
		
		int babyMateCooldown = getBabyInt(this.mateCooldown, mate.mateCooldown, 1);
		
		Schedule babySchedule = this.schedule.getBabySchedule(mate.schedule);
		Schedule babyBackupSchedule = this.backupSchedule.getBabySchedule(mate.backupSchedule);

		
		int[] babyPos = this.getBabyPos(new int[] {mate.getPosX(), mate.getPosY()});
		if (babyPos.length == 2) {
			Creature baby = new Creature(babyPos[0], babyPos[1], babyColor, babySlowness, babyEnergy, babyMaxEnergy, babyDaySight, babyMaxEnergyDuringMating, babyMateCooldown, babySchedule, babyBackupSchedule, this, mate);
			baby.setCurrentMateCooldown(babyMateCooldown);
			baby.setUpToDate(true);
			baby.checkForFood(babyPos[0], babyPos[1]);
			globalData.getEntities().addEntity(baby);
			offspring.add(baby);
			mate.offspring.add(baby);
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
		
		double mutationRate = globalData.getMutationRate();
		double mutationInstance = Math.random() * (mutationRate*2) - mutationRate;
		int babyStatInt = (int) Math.round(babyStatDouble * (1.0+mutationInstance));
		if (babyStatInt == 0) { // if stat = 0, there's a mutationRate chance it can become 1
			double mutationInstance2 = Math.random();
			if (mutationInstance2 <= mutationRate)
				babyStatInt = 1;
		}
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
		for (int i = 0; i < 4; i++) { // check positions next to one parent
			babyPos[0] = this.getPosX()+dirX[dirOptions.get(i)];
			babyPos[1] = this.getPosY()+dirY[dirOptions.get(i)];
			if (babyPos[0] < globalData.getMaxScreenCol() - 1 && babyPos[0] > 0 && babyPos[1] < globalData.getMaxScreenRow() - 1 && babyPos[1] > 0)
				if (globalData.getEntities().posNotSolid(babyPos[0], babyPos[1])) {
					return babyPos;
				}
		}
		for (int i = 0; i < 4; i++) { // check positions next to other parent
			babyPos[0] = matePos[0]+dirX[dirOptions.get(i)];
			babyPos[1] = matePos[1]+dirY[dirOptions.get(i)];
			if (babyPos[0] < globalData.getMaxScreenCol() - 1 && babyPos[0] > 0 && babyPos[1] < globalData.getMaxScreenRow() - 1 && babyPos[1] > 0)
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
	 Eat food
	 */
	private void eatFood() {
		// TODO: remove hunger
		globalData.getEntities().decrementNumFood();
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
			this.doBackupBehavior();
			return;
		}
		
		Entity mate = mateBFS(new boolean[globalData.getMaxScreenCol()][globalData.getMaxScreenRow()], this.getPosX(), this.getPosY());
		if (mate instanceof Creature) {
			int[] matePos = {mate.getPosX(), mate.getPosY()};
			if (this.getDistance(matePos) == 1) {
				this.setMate((Creature)mate);
				((Creature)mate).setMate(this);
			} else {
				int[] nextStep = findNextStep(matePos);
				if (nextStep.length == 2) {
					this.moveCreature(nextStep[0], nextStep[1]);
				} else {
					this.doBackupBehavior();
				}
			}
		} else {
			this.doBackupBehavior();
		}
	}
	
	private int getDistance(int[] pos) {
		return Math.abs(pos[0] - this.getPosX())  + Math.abs(pos[1] - this.getPosY());
	}
	
	/*
	 Move creature towards food
	 */
	private void moveToFood() {
		int[] foodPos = foodBFS(new boolean[globalData.getMaxScreenCol()][globalData.getMaxScreenRow()], this.getPosX(), this.getPosY());
		if (foodPos.length == 2) {
			
			int[] nextStep = findNextStep(foodPos);
			if (nextStep.length == 2) {
				this.moveCreature(nextStep[0], nextStep[1]);
			} else {
				this.doBackupBehavior();
			}
		} else {
			this.doBackupBehavior();
		}
	}
	
	/*
	 Do backup behavior if for any reason main behavior cannot be accomplished
	 */
	private void doBackupBehavior() {
		Behavior backupBehavior = backupSchedule.getCurrentBehavior();
		if (triedBackupBehavior || backupBehavior == behavior) {
			this.moveRandom();
			return;
		}
			
		triedBackupBehavior = true;
		this.doBehavior(backupBehavior);
	}
	
	private int[] findNextStep(int[] foodPos) {
		openList = new ArrayList<Node>();
		this.initializeNodes();
		this.setStartNode(this.getPosX(), this.getPosY());
		this.setGoalNode(foodPos[0], foodPos[1]);
		this.setCostOnNodes();
		if (this.search())
			return trackPath();
		else
			return new int[1];
	}
	
	/*
	 returns true if location is within the screen
	 */
	private boolean isValid(int x, int y) {
		return !(x < 0 || y < 0 || x >= globalData.getMaxScreenCol() || y>= globalData.getMaxScreenRow());	
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
			if (globalData.getEntities().getEntity(foodPos[0], foodPos[1]) instanceof Food) {
				return foodPos;
			}
			
			q.remove();
			
			ArrayList<int[]> list = new ArrayList<int[]>();
			for (int i = 0; i < 4; i++) {
				int adjx = foodPos[0] + dirX[i];
				int adjy = foodPos[1] + dirY[i];
				if (globalData.getEntities().isValid(visited, adjx, adjy)) {
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
			Entity entity = globalData.getEntities().getEntity(matePos[0], matePos[1]);
			if (entity instanceof Creature && !entity.equals(this)) {
				if (((Creature)entity).behavior.equals(Behavior.findMate) && ((Creature)entity).getCurrentMateCooldown() <= 0) {
					return entity;
				}
				
			}
			
			q.remove();
			
			ArrayList<int[]> list = new ArrayList<int[]>();
			for (int i = 0; i < 4; i++) {
				int adjx = matePos[0] + dirX[i];
				int adjy = matePos[1] + dirY[i];
				if (globalData.getEntities().isValid(visited, adjx, adjy)) {
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
				int newX = this.getPosX()+dirX[dirOptions.get(i)];
				int newY = this.getPosY()+dirY[dirOptions.get(i)];
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
		//TODO: add fatigue
		this.checkForFood(x, y);
		globalData.getEntities().moveEntity(this, x, y);
		this.setPosX(x);
		this.setPosY(y);
		energy--;
		
		
	}
	
	/*
	 Eat food if it is on x, y
	 */
	private void checkForFood(int x, int y) {
		Entity entity = globalData.getEntities().getEntity(x, y);
		if (entity instanceof Food) {
			this.eatFood();
		}
	}
	
	/*
	 Return energy of creature
	 */
	public int getEnergy() {
		return energy;
	}
	
	/*
	 Return max energy of creature
	 */
	public int getMaxEnergy() {
		return maxEnergy;
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

	/*
	 Return backupSchedule
	 */
	public Schedule getBackupSchedule() {
		return backupSchedule;
	}
}
