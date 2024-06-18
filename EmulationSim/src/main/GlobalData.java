package main;

import java.awt.Color;

import entity.EntityCollection;

public class GlobalData {
    private static GlobalData globalData;
    private static Panel panel;

	private static EntityCollection entities;
	
	private static int time; // time of day
	private static int dayLength; // amount of time in a day (and amount of time in a night)
	
	//CONFIG VARIABLES
	private static int FPS; // FPS
	
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
	public static synchronized GlobalData getInstance(Panel p) {
        if (globalData == null) {
        	panel = p;
            globalData = new GlobalData();
            entities = new EntityCollection(p);
            time = 0;
            dayLength = 500;
            
            FPS = 5;
            
            foodEnergy = 10;
            maxFoodAge = 100;
            
            
            numFoodSpawn = 20;
            foodRespawnTime = 5;
            maxNumFood = 3000;
            
            
        }
        return globalData;
    }
	
	public static synchronized GlobalData getInstance() {
		return globalData;
	}
	
	/*
	 Return EntityCollection
	 */
	public EntityCollection getEntities() {
		return entities;
	}
	
	
	/*
	 Return amount of energy gained from 1 food
	 */
	public int getFoodEnergy() {
		return foodEnergy;
	}
	
	public double getTime() {
		return time;
	}
	
	public void incrementTime() {
		time++;
		int colorVal = (int) Math.abs(255.0/dayLength * (time - dayLength));
		panel.setBackground(new Color(colorVal, colorVal, colorVal));
	}
	
	/*
	 Return FPS
	 */
	public int getFPS() {
		return FPS;
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
	
	
}
