package entity;

import main.GlobalData;

public class Schedule {
	
	GlobalData globalData;
	
	Behavior[] schedule;

	private final static int interval = 15;
	
	
	public Schedule(Behavior[] schedule) {
		globalData = GlobalData.getInstance();
		this.schedule = schedule;
	}
	
	public Behavior getCurrentBehavior() {
		int min = globalData.getTimerPanel().getMinuteOfDay();
		return schedule[min/interval];
	}
	
	public Schedule getBabySchedule(Schedule mateSchedule) {
		return this;
	}
}
