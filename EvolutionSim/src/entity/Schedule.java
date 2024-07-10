package entity;

import main.GlobalData;

public class Schedule {
	
	GlobalData globalData;
	
	Behavior[] schedule;
	
	// use a better way to store these constants
	private final static int hoursInDay = 24; // number of real life hours in a real life day
	private final static int minsInHour = 60; // number of real life minutes in a real life hour
	
	private final static int minsInDay = minsInHour * hoursInDay; // number of real life minutes in a real life day

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
