package main;

import entity.CreatureCollection;
import entity.FoodCollection;

public class GlobalData {
    private static GlobalData globalData;

	private static CreatureCollection creatures;
	private static FoodCollection foods;
	
	private static int FPS; // FPS
	
	private static int foodEnergy; // energy gained from eating 1 food
	
	private static int maxFoodAge; // food disappears once it reaches this age
	
	private static int numFoodSpawn; // number of food that spawns at a time
	private static int foodRespawnTime; // number of frames before more food spawns
	private static int maxNumFood; // maximum number of food that can exist
	
    private GlobalData() { }
	
    /*
    Get instance of GlobalData, create new GlobalData if null
     */
	public static synchronized GlobalData getInstance(Panel p) {
        if (globalData == null) {
            globalData = new GlobalData();
            
            FPS = 5;
            
            creatures = new CreatureCollection();
            foods = new FoodCollection(p);
            foodEnergy = 10;
            maxFoodAge = 100;
            
            
            numFoodSpawn = 10;
            foodRespawnTime = 50;
            maxNumFood = 30;
        }
        return globalData;
    }
	
	public static synchronized GlobalData getInstance() {
		return globalData;
	}
	
	/*
	 Return CreatureCollection
	 */
	public CreatureCollection getCreatures() {
		return creatures;
	}
	
	/*
	 Return FoodCollection
	 */
	public FoodCollection getFoods() {
		return foods;
	}
	
	/*
	 Return amount of energy gained from 1 food
	 */
	public int getFoodEnergy() {
		return foodEnergy;
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
