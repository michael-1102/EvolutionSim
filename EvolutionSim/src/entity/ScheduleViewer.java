package entity;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import main.TimerPanel;


public class ScheduleViewer extends JDialog {
	Schedule schedule;
	Schedule backup;
	Creature creature;
	
	private final static int viewerWidth = 300;
	private final static int viewerHeight = 520;
	private final static Color backgroundColor = Color.white;
	
	private JPanel panel;
	private JPanel legendPanel;
	private JPanel schedulePanel;
	
	private class ScheduleBox extends JPanel {
		private Color color;
		private int length;
		private int width;
		public ScheduleBox(Behavior behavior, int length, int width) {
			super();
			color = behavior.getColor();
			this.length = length;
			this.width = width;
			Dimension dim = new Dimension(length, width);
			this.setPreferredSize(dim);
			this.setMaximumSize(dim);
			this.setMinimumSize(dim);
			this.setBackground(color);
			
		}
	
		
	}
	
	public ScheduleViewer(Creature creature, Schedule schedule, Schedule backup) {
		this.creature = creature;
		this.schedule = schedule;
		this.backup = backup;
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setResizable(true);
		this.setTitle("Creature #" + creature.getCreatureNum() + " Schedule");
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(viewerWidth, viewerHeight));
		panel.setBackground(backgroundColor);
		panel.setDoubleBuffered(true);
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		this.add(panel);
		
		legendPanel = new JPanel();
		legendPanel.setBackground(backgroundColor);
		legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.LINE_AXIS));
		for (Behavior behavior : Behavior.values()) {
			if (behavior.isSchedulable()) {
				legendPanel.add(new ScheduleBox(behavior, 20, 20));
				legendPanel.add(new JLabel(" " + behavior.getName(), SwingConstants.LEFT));
				legendPanel.add(Box.createRigidArea(new Dimension(10, 0)));
			}
		}
		panel.add(legendPanel);
		
		Behavior[] scheduleArr = schedule.getBehaviorArr();
		Behavior[] backupArr = backup.getBehaviorArr();
		
		schedulePanel = new JPanel();
		schedulePanel.setBackground(backgroundColor);
		schedulePanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.weightx = .5;
		c.weighty = .5;
		c.gridy = 0;
		c.gridx = 0;
		schedulePanel.add(new JLabel("TIME"), c);
		c.gridx = 1;
		schedulePanel.add(new JLabel("MAIN SCHEDULE"), c);
		c.gridx = 2;
		schedulePanel.add(new JLabel("BACKUP SCHEDULE"), c);
		c.gridy = 1;
		for (int i = 0; i < scheduleArr.length; i++) {
			c.gridheight = 4;
			c.gridx = 0;
			if (i % c.gridheight == 0) 
				schedulePanel.add(new JLabel(TimerPanel.getTimeFromIndex(i, scheduleArr.length)), c);
			c.gridheight = 1;
			c.gridx++;
			schedulePanel.add(new ScheduleBox(scheduleArr[i], 100, 5), c);
			c.gridx++;
			schedulePanel.add(new ScheduleBox(backupArr[i], 100, 5), c);
			c.gridy++;
		}
		
		panel.add(schedulePanel);
		
		CreatureViewer.changeFont(panel, new Font(Font.MONOSPACED, Font.BOLD, 9));
		this.pack();
	}
	
	
}
