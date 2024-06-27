package main;


import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JFrame;

import entity.Behavior;
import entity.Creature;
import entity.Entity;
import entity.EntityCollection;

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
	
	private static int foodEnergy; // energy gained from eating 1 food
	
	private static int maxFoodAge; // food disappears once it reaches this age
	
	private static int numFoodSpawn; // number of food that spawns at a time
	private static int foodRespawnTime; // amount of time before more food spawns
	private static int maxNumFood; // maximum number of food that can exist
	
	//END OF CONFIG VARIABLES
    private GlobalData() { }
	
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
            unpausedFPS = 60;
            FPS = unpausedFPS;
            
            foodEnergy = 10;
            maxFoodAge = 100;
            
            
            numFoodSpawn = 10;
            foodRespawnTime = 50;
            maxNumFood = 40;
            
            
        }
        return globalData;
    }
	
	/*
	 Set up entities before start
	 */
	public void setUpSim() {
		entities.addCreature(new Creature(4, 4, new Color(255, 0, 23), 100, 1000, 20, 10, Behavior.findMate));
		entities.addCreature(new Creature(10, 8, new Color(0, 100, 100), 100, 1000, 20, 10, Behavior.findMate));
		entities.addCreature(new Creature(30, 4, new Color(200, 0, 255), 100, 1000, 20, 10, Behavior.eat));
		
		/*
		entities.addCreature(new Creature(6, 6, 100, 1000, 20, Behavior.idle));
		entities.addCreature(new Creature(6, 5, 100, 1000, 20, Behavior.idle));
		entities.addCreature(new Creature(6, 4, 100, 1000, 20, Behavior.idle));
		entities.addCreature(new Creature(6, 3, 100, 1000, 20, Behavior.idle));
		entities.addCreature(new Creature(6, 2, 100, 1000, 20, Behavior.idle));
		entities.addCreature(new Creature(6, 1, 100, 1000, 20, Behavior.idle));
		entities.addCreature(new Creature(6, 0, 100, 1000, 20, Behavior.idle));
		
		entities.addCreature(new Creature(5, 6, 100, 1000, 20, Behavior.idle));
		entities.addCreature(new Creature(4, 6, 100, 1000, 20, Behavior.idle));
		entities.addCreature(new Creature(3, 6, 100, 1000, 20, Behavior.idle));
		entities.addCreature(new Creature(2, 6, 100, 1000, 20, Behavior.idle));
		entities.addCreature(new Creature(1, 6, 100, 1000, 20, Behavior.idle));
		entities.addCreature(new Creature(0, 6, 100, 1000, 20, Behavior.idle));
		 */

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
	 Get paused
	 */
	public boolean getPaused() {
		return paused;
	}
	
	
}
