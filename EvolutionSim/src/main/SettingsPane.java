package main;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


import javax.swing.JTabbedPane;


public class SettingsPane extends JTabbedPane {
	
	private class SettingSpinner extends JSpinner {
		private int width;
		private int height;
		public SettingSpinner(SpinnerModel model, int width, int height) {
			super(model);
			this.width = width;
			this.height = height;
		}
		
		@Override
		public Dimension getPreferredSize() {
		    return new Dimension(width, height);
		}
		
		@Override
		public Dimension getMinimumSize() {
		    return new Dimension(width, height);
		}
	}

	
	private GlobalData globalData;

	private JPanel creatureTab;
	private JPanel terrainTab;
	private JPanel configTab;
	
	private JSpinner mutationRateSpinner;
	private JCheckBox randomFoodCheckBox;
	private JSpinner energyFromFoodSpinner;
	private JSpinner maxFoodAgeSpinner;
	private JSpinner foodRespawnTimeSpinner;
	private JSpinner numFoodSpawnSpinner;
	private JSpinner maxNumFoodSpinner;

	
	public SettingsPane() {
		globalData = GlobalData.getInstance();
		this.setDoubleBuffered(true);
		this.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		this.addChangeListener(new ChangeListener() {
	        public void stateChanged(ChangeEvent e) {
	        	SettingsPane pane = ((SettingsPane) e.getSource());
	            switch (pane.getSelectedIndex()) {
		            case 0:
		            	break;
		            case 1:
		            	break;
		            case 2:
		            	pane.resetConfigTab();
		            	break;
	            	default:
	            		break;
				}
	        }
	    });
		
		creatureTab = new JPanel(new GridBagLayout());
		terrainTab = new JPanel(new GridBagLayout());
		configTab = new JPanel(new GridBagLayout());
		
		this.addTab("Creatures", creatureTab);
		this.addTab("Terrain", terrainTab);
		this.addTab("Settings", configTab);
		
		try {
			setupConfigTab();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private void resetConfigTab() {
		mutationRateSpinner.setValue(globalData.getMutationRate());
		randomFoodCheckBox.setSelected(globalData.doesRandomFoodSpawn());
		energyFromFoodSpinner.setValue(globalData.getFoodEnergy());
		maxFoodAgeSpinner.setValue(TimerPanel.framesToMins(globalData.getMaxFoodAge()));
		foodRespawnTimeSpinner.setValue(TimerPanel.framesToMins(globalData.getFoodRespawnTime()));
		numFoodSpawnSpinner.setValue(globalData.getNumFoodSpawn());
		maxNumFoodSpinner.setValue(globalData.getMaxNumFood());

	}
	
	private void setupConfigTab() throws ParseException {
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 1;
		int[] col = new int[1];
		col[0] = 0;
		SpinnerModel model = new SpinnerNumberModel(globalData.getMutationRate(), 0, 1, 0.01);
		mutationRateSpinner = new SettingSpinner(model, 10, 30);
		mutationRateSpinner.setEditor(new JSpinner.NumberEditor(mutationRateSpinner,"0%"));
		
		randomFoodCheckBox = new JCheckBox() {
			@Override
			public Dimension getPreferredSize() {
			    return new Dimension(30, 30);
			}
		};
		randomFoodCheckBox.setSelected(globalData.doesRandomFoodSpawn());
		
		model = new SpinnerNumberModel(globalData.getFoodEnergy(), 1, 999999, 1);
		energyFromFoodSpinner = new SettingSpinner(model, 10, 30);
		
		model = new SpinnerNumberModel(TimerPanel.framesToMins(globalData.getMaxFoodAge()), 1, 999999, 1);
		maxFoodAgeSpinner = new SettingSpinner(model, 10, 30);

		model = new SpinnerNumberModel(TimerPanel.framesToMins(globalData.getFoodRespawnTime()), 1, 999999, 1);
		foodRespawnTimeSpinner = new SettingSpinner(model, 10, 30);
		
		int gridSize = globalData.getMaxScreenRow() * globalData.getMaxScreenCol();
		
		model = new SpinnerNumberModel(globalData.getNumFoodSpawn(), 1, gridSize, 1);
		numFoodSpawnSpinner = new SettingSpinner(model, 10, 30);
		
		model = new SpinnerNumberModel(globalData.getMaxNumFood(), 1, gridSize, 1);
		maxNumFoodSpinner = new SettingSpinner(model, 10, 30);
		
		addSetting(configTab, "Mutation Rate", mutationRateSpinner, col, c);
		addSetting(configTab, "Random Food Spawn?", randomFoodCheckBox, col, c);
		addSetting(configTab, "Energy Gained From 1 Food", energyFromFoodSpinner, col, c);
		addSetting(configTab, "Food Lifespan [Minutes]", maxFoodAgeSpinner, col, c);
		addSetting(configTab, "Food Respawn Time [Minutes]", foodRespawnTimeSpinner, col, c);
		addSetting(configTab, "Number of Food Spawned", numFoodSpawnSpinner, col, c);
		addSetting(configTab, "Max Number of Food", maxNumFoodSpinner, col, c);

		c.gridx = 1;
		c.gridy++;
		JButton applyButton = new JButton("Apply");
		applyButton.setFocusable(false);
		applyButton.addActionListener((new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent evt) {
		    	globalData.setMutationRate((double) mutationRateSpinner.getValue());
		    	globalData.setRandomFoodSpawn(randomFoodCheckBox.isSelected());
		    	globalData.setFoodEnergy((int)energyFromFoodSpinner.getValue());
		    	globalData.setMaxFoodAge(TimerPanel.minsToFrames((int) maxFoodAgeSpinner.getValue()));
		    	globalData.setFoodRespawnTime(TimerPanel.minsToFrames((int) foodRespawnTimeSpinner.getValue()));
		    	globalData.setNumFoodSpawn((int)numFoodSpawnSpinner.getValue());
		    	globalData.setMaxNumFood((int)maxNumFoodSpinner.getValue());


		    }
		}));
		configTab.add(applyButton, c);

		
		GlobalData.changeFont(configTab, new Font(Font.MONOSPACED, Font.PLAIN, 12));
	}
	
	private void addSetting(JPanel panel, String name, Component component, int[] col, GridBagConstraints c) {
		c.gridx = 0;
		c.gridy = col[0];
		c.insets = new Insets(0, 30, 0, 0);
		JLabel label = new JLabel(name);
		label.setLabelFor(component);
		panel.add(label, c);
		c.gridx = 1;
		c.insets = new Insets(0, 0, 0, 30);
		panel.add(component, c);
		col[0]++;
	}
}
