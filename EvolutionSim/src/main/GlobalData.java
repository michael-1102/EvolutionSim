package main;


import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JFrame;

import entity.Behavior;
import entity.Creature;
import entity.Entity;
import entity.EntityCollection;
import entity.Schedule;
import entity.Wall;

public class GlobalData {
    private static GlobalData globalData = new GlobalData();

    private GridPanel gridPanel;
    private JFrame frame;

	private EntityCollection entities;
	private TimerPanel timerPanel;
	private SettingsPane settingsPane;
	
	private ArrayList<Entity> newEntities;
	
	private boolean paused;
	
	//SCREEN SIZE VALUES
	private int tileSize = 20;
	
	private final int maxScreenCol = 72; // x
	private final int maxScreenRow = 54; // y
	
	private int screenWidth = tileSize * maxScreenCol;
	private int screenHeight = tileSize * maxScreenRow;
	
	private int FPS; // FPS
	private int unpausedFPS;

	
	//CONFIG VARIABLES
	private double mutationRate;
	
	private int maxSight; // constant value of daySight + nightSight, rework this maybe
	
	private boolean randomFoodSpawn;
	private int foodEnergy; // energy gained from eating 1 food
	
	private int maxFoodAge; // food disappears once it reaches this age
	
	private int numFoodSpawn; // number of food that spawns at a time
	private int foodRespawnTime; // amount of time before more food spawns
	private int maxNumFood; // maximum number of food that can exist
	
	//END OF CONFIG VARIABLES
    	
	private GlobalData() {
		paused = true;
        FPS = 0;
        unpausedFPS = TimerPanel.getInitialFPS();
        
        mutationRate = 0.1;
        
        maxSight = 40;
        
        randomFoodSpawn = true;
        foodEnergy = 10;
        maxFoodAge = TimerPanel.minsToFrames(25);
        
        
        numFoodSpawn = 20;
        foodRespawnTime = TimerPanel.minsToFrames(20);
        maxNumFood = 100;
        
        newEntities = new ArrayList<Entity>();
	}
	
    /*
     Get instance of GlobalData
     */
	public static synchronized GlobalData getInstance() {
        return globalData;
    }
	
	public void setTileSize() {
		Dimension dim = frame.getContentPane().getSize();
		int timerHeight = timerPanel.getHeight();
		tileSize = Math.min(dim.width/(maxScreenCol+8), (dim.height - timerHeight)/(maxScreenRow));
		screenWidth = tileSize * maxScreenCol;
		screenHeight = tileSize * maxScreenRow;
		gridPanel.revalidate();
	}
	
	/*
	 Set up entities before start
	 */
	public void setUpSim() {
		Behavior[] behaviors1 = new Behavior[96];
		Behavior[] behaviors2 = new Behavior[96];
		for (int i = 0; i < 96; i++) {
			behaviors1[i] = Behavior.findMate;
			behaviors2[i] = Behavior.eat;
		}
        entities = new EntityCollection();
		
		entities.addEntity(new Creature(4, 4, new Color(255, 0, 23), 10, 100, 200, 20, 15, 500, new Schedule(behaviors1), new Schedule(behaviors2), null, null));
		entities.addEntity(new Creature(10, 8, new Color(0, 100, 100), 10, 100, 200, 20, 15, 500, new Schedule(behaviors1), new Schedule(behaviors2), null, null));
		entities.addEntity(new Creature(30, 4, new Color(200, 0, 255), 10, 100, 200, 20, 15, 500, new Schedule(behaviors1), new Schedule(behaviors2), null, null));

		entities.addEntity(new Wall(0,0));
		entities.addEntity(new Wall(maxScreenCol-1,0));
		entities.addEntity(new Wall(maxScreenCol-1,maxScreenRow-1));
		entities.addEntity(new Wall(0,maxScreenRow-1));
	}
	
	/*
	 Return tileSize
	 */
	public int getTileSize() {
		return tileSize;
	}
	
	/*
	 Return randomFoodSpawn
	 */
	public boolean doesRandomFoodSpawn() {
		return randomFoodSpawn;
	}
	
	
	/*
	 Return maxScreenRow
	 */
	public int getMaxScreenRow() {
		return maxScreenRow;
	}
	
	/*
	 Return maxScreenCol
	 */
	public int getMaxScreenCol() {
		return maxScreenCol;
	}
	
	/*
	 Return screenHeight
	 */
	public int getScreenHeight() {
		return screenHeight;
	}
	
	/*
	 Return screenWidth
	 */
	public int getScreenWidth() {
		return screenWidth;
	}
	
	
	/*
	 Return maxSight
	 */
	public int getMaxSight() {
		return maxSight;
	}
	
	/*
	 Return frame
	 */
	public JFrame getFrame() {
		if (frame == null) {
	    	frame = new JFrame();
		}
		return frame;
	}
	
	/*
	 Return GridPanel
	 */
	public GridPanel getGridPanel() {
		if (gridPanel == null) {
	    	gridPanel = new GridPanel();
		}
		return gridPanel;
	}
	
	/*
	 Return SettingsPane
	 */
	public SettingsPane getSettingsPane() {
		if (settingsPane == null) {
	        settingsPane = new SettingsPane();
		}
		return settingsPane;
	}
	
	/*
	 Return EntityCollection
	 */
	public EntityCollection getEntities() {
		if (entities == null) {
	        entities = new EntityCollection();
		}
		return entities;
	}
	
	/*
	 Return new entities
	 */
	public ArrayList<Entity> getNewEntities() {
		return newEntities;
	}
	
	/*
	 Return amount of energy gained from 1 food
	 */
	public int getFoodEnergy() {
		return foodEnergy;
	}
	
	/*
	 Return timerPanel
	 */
	public TimerPanel getTimerPanel() {
		if (timerPanel == null) {
	        timerPanel = new TimerPanel();
		}
		return timerPanel;
	}
	
	
	
	/*
	 Return FPS
	 */
	public int getFPS() {
		return FPS;
	}
	
	/*
	 Set FPS
	 */
	public void setFPS(int newFPS) {
		FPS = newFPS;
	}
	
	/*
	 Set unpausedFPS
	 */
	public void setUnpausedFPS(int newFPS) {
		unpausedFPS = newFPS;
		if (!paused) {
			setFPS(unpausedFPS);
		}
	}
	
	/*
	 Return food respawn time
	 */
	public int getFoodRespawnTime() {
		return foodRespawnTime;
	}
	
	/*
	 Return number of food that spawns at a time
	 */
	public int getNumFoodSpawn() {
		return numFoodSpawn;
	}
	
	/*
	 Return max number of food that can exist
	 */
	public int getMaxNumFood() {
		return maxNumFood;
	}
	
	/*
	 Return max food age
	 */
	public int getMaxFoodAge() {
		return maxFoodAge;
	}

	/*
	 if paused, unpause. if unpaused, pause
	 */
	public void pause() {
		if (paused) {
			paused = false;
			FPS = unpausedFPS;
		} else{
			paused = true;
			FPS = 0;
		}
	}

	/*
	 Return paused
	 */
	public boolean getPaused() {
		return paused;
	}

	/*
	 Return mutationRate
	 */
	public double getMutationRate() {
		return mutationRate;
	}
	
	/*
	 Set mutationRate
	 */
	public void setMutationRate(double newMutationRate) {
		mutationRate = newMutationRate;
	}
	
	/*
	 Set randomFoodSpawn
	 */
	public void setRandomFoodSpawn(boolean newRandomFoodSpawn) {
		randomFoodSpawn = newRandomFoodSpawn;
	}
	
	
	public static void changeFont(Component component, Font font) {
	    component.setFont (font);
	    if (component instanceof Container) {
	        for (Component child : ((Container) component).getComponents()) {
	            changeFont(child, font);
	        }
	    }
	}

	/*
	 Set foodEnergy
	 */
	public void setFoodEnergy(int newFoodEnergy) {
		foodEnergy = newFoodEnergy;
	}
	
	/*
	 Set maxFoodAge
	 */
	public void setMaxFoodAge(int newMaxFoodAge) {
		maxFoodAge = newMaxFoodAge;
	}
	
	/*
	 Set foodRespawnTime
	 */
	public void setFoodRespawnTime(int newFoodRespawnTime) {
		foodRespawnTime = newFoodRespawnTime;
	}
	
	/*
	 Set numFoodSpawn
	 */
	public void setNumFoodSpawn(int newNumFoodSpawn) {
		numFoodSpawn = newNumFoodSpawn;
	}
	
	/*
	 Set maxNumFood
	 */
	public void setMaxNumFood(int newMaxNumFood) {
		maxNumFood = newMaxNumFood;
	}
}
