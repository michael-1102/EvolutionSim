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
		for (int i = 0; i < schedule.length; i++) {
			if (this.schedule[i].equals(mateSchedule.schedule[i])) {
				babySchedule[i] = this.schedule[i];
			} else {
				// needs work
			}
			if (Math.random() <= globalData.getMutationRate()) {
				babySchedule[i] = getRandomBehavior();
			}
		}
		return new Schedule(babySchedule);
	}
	
	private Behavior getRandomBehavior() {
		int result = random.nextInt(schedulableBehaviors.size());
		return schedulableBehaviors.get(result);
	}
}
