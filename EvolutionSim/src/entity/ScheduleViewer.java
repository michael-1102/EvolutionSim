package entity;

import javax.swing.JDialog;
import javax.swing.JPanel;

public class ScheduleViewer extends JDialog {
	Schedule schedule;
	Schedule backup;
	Creature creature;
	
	
	public ScheduleViewer(Creature creature, Schedule schedule, Schedule backup) {
		this.creature = creature;
		this.schedule = schedule;
		this.backup = backup;
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setResizable(true);
		this.setTitle("Creature #" + creature.getCreatureNum() + " Schedule");
	}
}
