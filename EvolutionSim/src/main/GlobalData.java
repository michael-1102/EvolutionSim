package main;


import java.awt.Color;
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
	
	private static ArrayList<Entity> newEntities;
	
	private static boolean paused;
	
	//SCREEN SIZE VALUES
	public final int originalTileSize = 16;
	public final int scale = 1;
	
	public final int tileSize = originalTileSize * scale;
	
	public final int maxScreenCol = 48;
	public final int maxScreenRow = 36;
	
	public final int screenWidth = tileSize * maxScreenCol;
	public final int screenHeight = tileSize * maxScreenRow;
	
	//CONFIG VARIABLES

	private static int FPS; // FPS
	private static int unpausedFPS;
	
	private static double mutationRate;
	
	private static int maxSight; // constant value of daySight + nightSight
	
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
        	gridPanel = new GridPanel();
        	frame = new JFrame();
            entities = new EntityCollection();
            newEntities = new ArrayList<Entity>();
            timerPanel = new TimerPanel();
            
            paused = false;
            unpausedFPS = TimerPanel.getInitialFPS();
            FPS = unpausedFPS;
            
            mutationRate = 0.1;
            
            maxSight = 40;
            
            foodEnergy = 10;
            maxFoodAge = 150;
            
            
            numFoodSpawn = 50;
            foodRespawnTime = 50;
            maxNumFood = 40;
            
            
        }
        return globalData;
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
		
		entities.addCreature(new Creature(4, 4, new Color(255, 0, 23), 10, 100, 200, 20, 15, 500, new Schedule(behaviors1), new Schedule(behaviors2), null, null));
		entities.addCreature(new Creature(10, 8, new Color(0, 100, 100), 10, 100, 200, 20, 15, 500, new Schedule(behaviors1), new Schedule(behaviors2), null, null));
		entities.addCreature(new Creature(30, 4, new Color(200, 0, 255), 10, 100, 200, 20, 15, 500, new Schedule(behaviors1), new Schedule(behaviors2), null, null));

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
	 Return panel
	 */
	public GridPanel getGridPanel() {
		return gridPanel;
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
	
	
}
