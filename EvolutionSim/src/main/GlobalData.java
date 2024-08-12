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

public class GlobalData {
    private static GlobalData globalData;
    private static GridPanel gridPanel;
    private static JFrame frame;

	private static EntityCollection entities;
	private static TimerPanel timerPanel;
	private static SettingsPane settingsPane;
	
	private static ArrayList<Entity> newEntities;
	
	private static boolean paused;
	
	//SCREEN SIZE VALUESS
	private int tileSize = 20;
	
	private final int maxScreenCol = 48; // x
	private final int maxScreenRow = 36; // y
	
	private int screenWidth = tileSize * maxScreenCol;
	private int screenHeight = tileSize * maxScreenRow;
	
	private static int FPS; // FPS
	private static int unpausedFPS;

	
	//CONFIG VARIABLES
	private static double mutationRate;
	
	private static int maxSight; // constant value of daySight + nightSight, rework this maybe
	
	private static boolean randomFoodSpawn;
	private static int foodEnergy; // energy gained from eating 1 food
	
	private static int maxFoodAge; // food disappears once it reaches this age
	
	private static int numFoodSpawn; // number of food that spawns at a time
	private static int foodRespawnTime; // amount of time before more food spawns
	private static int maxNumFood; // maximum number of food that can exist
	
	//END OF CONFIG VARIABLES
    	
    /*
    Get instance of GlobalData, create new GlobalData if null
     */
	public static synchronized GlobalData getInstance() {
        if (globalData == null) {
            globalData = new GlobalData();
            
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
            
        	gridPanel = new GridPanel();
        	frame = new JFrame();
            entities = new EntityCollection();
            newEntities = new ArrayList<Entity>();
            timerPanel = new TimerPanel();
            settingsPane = new SettingsPane();
        }
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
		Behavior[] behaviors3 = new Behavior[96];
		for (int i = 0; i < 96; i++) {
			behaviors1[i] = Behavior.findMate;
			behaviors2[i] = Behavior.eat;
			behaviors3[i] = Behavior.idle;;
		}
		entities.addCreature(new Creature(0, 0, new Color(255, 0, 23), 10, 100, 200, 20, 15, 500, new Schedule(behaviors3), new Schedule(behaviors3), null, null));
		entities.addCreature(new Creature(47, 0, new Color(255, 0, 23), 10, 100, 200, 20, 15, 500, new Schedule(behaviors3), new Schedule(behaviors3), null, null));
		entities.addCreature(new Creature(47, 35, new Color(255, 0, 23), 10, 100, 200, 20, 15, 500, new Schedule(behaviors3), new Schedule(behaviors3), null, null));
		entities.addCreature(new Creature(0, 35, new Color(255, 0, 23), 10, 100, 200, 20, 15, 500, new Schedule(behaviors3), new Schedule(behaviors3), null, null));

		
		entities.addCreature(new Creature(4, 4, new Color(255, 0, 23), 10, 100, 200, 20, 15, 500, new Schedule(behaviors1), new Schedule(behaviors2), null, null));
		entities.addCreature(new Creature(10, 8, new Color(0, 100, 100), 10, 100, 200, 20, 15, 500, new Schedule(behaviors1), new Schedule(behaviors2), null, null));
		entities.addCreature(new Creature(30, 4, new Color(200, 0, 255), 10, 100, 200, 20, 15, 500, new Schedule(behaviors1), new Schedule(behaviors2), null, null));

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
		return frame;
	}
	
	/*
	 Return GridPanel
	 */
	public GridPanel getGridPanel() {
		return gridPanel;
	}
	
	/*
	 Return SettingsPane
	 */
	public SettingsPane getSettingsPane() {
		return settingsPane;
	}
	
	/*
	 Return EntityCollection
	 */
	public EntityCollection getEntities() {
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
