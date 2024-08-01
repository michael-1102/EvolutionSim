package entity;

import java.util.ArrayList;
import java.util.Random;

import main.GlobalData;

public class Schedule {
	
	GlobalData globalData;
	
	private Behavior[] schedule;
	private ArrayList<Behavior> schedulableBehaviors;
	private Random random;
	private final static int interval = 15;
	
	
	public Schedule(Behavior[] schedule) {
		globalData = GlobalData.getInstance();
		this.schedule = schedule;
		schedulableBehaviors = new ArrayList<Behavior>();
		for (Behavior behavior : Behavior.values()) {
			if (behavior.isSchedulable()) {
				schedulableBehaviors.add(behavior);
			}
		}
		random = new Random();
	}
	
	
	public Behavior getCurrentBehavior() {
		int min = globalData.getTimerPanel().getMinuteOfDay();
		return schedule[min/interval];
	}
	
	public Schedule getBabySchedule(Schedule mateSchedule) {
		Behavior[] babySchedule = new Behavior[schedule.length];
		double rand;
		double mutationRate = globalData.getMutationRate();
		for (int i = 0; i < schedule.length; i++) {
			rand = Math.random();
			if (rand >= mutationRate) { 
				if (random.nextInt(2) == 0) {
					babySchedule[i] = this.schedule[i];
				} else {
					babySchedule[i] = mateSchedule.schedule[i];
				}
			} else if (rand >= mutationRate / 2) {
				switch(random.nextInt(6)) {
				case 5:
					babySchedule[i] = mateSchedule.schedule[Math.floorMod(i+1, mateSchedule.schedule.length)];
					break;
				case 4:
					babySchedule[i] = this.schedule[Math.floorMod(i+1, schedule.length)];
					break;
				case 3:
					babySchedule[i] = mateSchedule.schedule[Math.floorMod(i-1, mateSchedule.schedule.length)];
					break;
				case 2:
					babySchedule[i] = this.schedule[Math.floorMod(i-1, schedule.length)];
					break;
				case 1:
					babySchedule[i] = mateSchedule.schedule[i];
					break;
				case 0:
					babySchedule[i] = this.schedule[i];
					break;
				default:
					break;
				}
			} else {
				babySchedule[i] = getRandomBehavior();
			}
		}
		return new Schedule(babySchedule);
	}
	
	public Behavior[] getBehaviorArr() {
		return schedule;
	}
	
	private Behavior getRandomBehavior() {
		int result = random.nextInt(schedulableBehaviors.size());
		return schedulableBehaviors.get(result);
	}
}
