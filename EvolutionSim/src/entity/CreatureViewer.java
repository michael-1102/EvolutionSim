package entity;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.TimerPanel;

public class CreatureViewer extends JDialog {
	
	private final static DecimalFormat df = new DecimalFormat( "0.00" );
	private final static int viewerWidth = 400;
	private final static int viewerHeight = 450;
	private final static Color backgroundColor = Color.white;
	
	private class CreatureDrawing extends SubPanel {
		
		public CreatureDrawing() {
			super();
			this.setPreferredSize(new Dimension(50, 50));
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(creature.getColor());
			g.fillRect(10, 10, 40, 40);
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(20, 20);
		}
	}
	
	private class SubPanel extends JPanel {
			
			public SubPanel() {
				this.setBackground(backgroundColor);
			}
			
	}
	
	private class ViewerButton extends JButton {
		
		public ViewerButton() {
			this.setFocusable(false);
		}
		
	}
	
	private CreatureDrawing drawing;
	
	private JLabel creatureNumLabel;
	private JLabel generationLabel;
	private JLabel positionLabel;
	private JLabel energyLabel;
	
	private JLabel maxEnergyLabel;
	private JLabel energyGivenDuringMatingLabel;
	private JLabel matingCooldownLabel;
	private JLabel daySightLabel;
	private JLabel nightSightLabel;
	private JLabel speedLabel;
	
	private JLabel maxEnergyLabelValue;
	private JLabel energyGivenDuringMatingLabelValue;
	private JLabel matingCooldownLabelValue;
	private JLabel daySightLabelValue;
	private JLabel nightSightLabelValue;
	private JLabel speedLabelValue;

	
	private ViewerButton highlightButton;
	private ViewerButton scheduleButton;
	private ViewerButton parentOneButton;
	private ViewerButton parentTwoButton;
	
	private ViewerButton offspringDropdown;
	
	private JLabel parentLabel;
	
	private Creature creature;
	private JPanel panel;
	
	private SubPanel topPanel;
	private SubPanel middlePanel;
	private SubPanel bottomPanel;
	
	public CreatureViewer(Creature creature) {
		this.creature = creature;
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setResizable(true);
		this.setTitle("Creature Viewer");
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(viewerWidth, viewerHeight));
		panel.setBackground(backgroundColor);
		panel.setDoubleBuffered(true);
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		topPanel = new SubPanel();
		topPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 0));
		topPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = .5;
		c.weighty = .5;
		c.gridheight = 3;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		drawing = new CreatureDrawing();
		topPanel.add(drawing, c);
		
		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = 0;
		creatureNumLabel = new JLabel();
		topPanel.add(creatureNumLabel, c);
		creatureNumLabel.setText("Creature #" + creature.getCreatureNum());
			
		c.gridx = 1;
		c.gridy = 1;
		generationLabel = new JLabel();
		topPanel.add(generationLabel, c);
		generationLabel.setText("Generation: " + creature.getGeneration());

		c.gridx = 1;
		c.gridy = 2;
		positionLabel = new JLabel();
		topPanel.add(positionLabel, c);
		
		c.gridx = 2;
		c.gridy = 2;
		highlightButton = new ViewerButton();
		highlightButton.setPreferredSize(new Dimension(80, 30));
		highlightButton.setText("Highlight");
		highlightButton.addActionListener((new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent evt) {
		       if (creature.toggleHighlight()) {
			       highlightButton.setText("Highlight");
		       } else {
			       highlightButton.setText("Un-Highlight");
		       }
		    }
		}));
		topPanel.add(highlightButton, c);
		
		c.insets = new Insets(0, 10, 0, 0);
		c.gridx = 0;
		c.gridy = 3;
		energyLabel = new JLabel();
		topPanel.add(energyLabel, c);
		
		panel.add(topPanel);
		
		middlePanel = new SubPanel();
		middlePanel.setLayout(new GridBagLayout());
		
		c.gridx = 0;
		c.gridy = 5;
		maxEnergyLabel = new JLabel();
		middlePanel.add(maxEnergyLabel, c);
		maxEnergyLabel.setText("Maximum Energy:");
		
		c.gridx = 1;
		c.gridy = 5;
		maxEnergyLabelValue = new JLabel();
		middlePanel.add(maxEnergyLabelValue, c);
		maxEnergyLabelValue.setText(Integer.toString(creature.getMaxEnergy()));
		
		c.gridx = 0;
		c.gridy = 6;
		energyGivenDuringMatingLabel = new JLabel();
		middlePanel.add(energyGivenDuringMatingLabel, c);
		energyGivenDuringMatingLabel.setText("Energy Given To Each Offspring:");
		
		c.gridx = 1;
		c.gridy = 6;
		energyGivenDuringMatingLabelValue = new JLabel();
		middlePanel.add(energyGivenDuringMatingLabelValue, c);
		energyGivenDuringMatingLabelValue.setText(Integer.toString(creature.getMaxEnergyDuringMating()));
		
		c.gridx = 0;
		c.gridy = 7;
		matingCooldownLabel = new JLabel();
		middlePanel.add(matingCooldownLabel, c);
		matingCooldownLabel.setText("Mating Cooldown:");
		
		c.gridx = 1;
		c.gridy = 7;
		matingCooldownLabelValue = new JLabel();
		middlePanel.add(matingCooldownLabelValue, c);
		int mateCooldownInMins = TimerPanel.framesToMins(creature.getMateCooldown());
		int mateCooldownHours = mateCooldownInMins / 60;
		if (mateCooldownHours > 0) {
			matingCooldownLabelValue.setText(mateCooldownHours + "h " + mateCooldownInMins % 60 + "m");

		} else {
			matingCooldownLabelValue.setText(mateCooldownInMins + "m");
		}
		
		c.gridx = 0;
		c.gridy = 8;
		daySightLabel = new JLabel();
		middlePanel.add(daySightLabel, c);
		daySightLabel.setText("Daytime Vision:");
		
		c.gridx = 1;
		c.gridy = 8;
		daySightLabelValue = new JLabel();
		middlePanel.add(daySightLabelValue, c);
		daySightLabelValue.setText(Integer.toString(creature.getDaySight()));
		
		c.gridx = 0;
		c.gridy = 9;
		nightSightLabel = new JLabel();
		middlePanel.add(nightSightLabel, c);
		nightSightLabel.setText("Nighttime Vision:");
		
		c.gridx = 1;
		c.gridy = 9;
		nightSightLabelValue = new JLabel();
		middlePanel.add(nightSightLabelValue, c);
		nightSightLabelValue.setText(Integer.toString(creature.getNightSight()));
		
		c.gridx = 0;
		c.gridy = 10;
		speedLabel = new JLabel();
		middlePanel.add(speedLabel, c);
		speedLabel.setText("Speed:");
		
		c.gridx = 1;
		c.gridy = 10;
		speedLabelValue = new JLabel();
		middlePanel.add(speedLabelValue, c);
		speedLabelValue.setText(df.format(creature.getSpeed()));
		
		panel.add(middlePanel);
		
		bottomPanel = new SubPanel();
		GridLayout gridLayout = new GridLayout(3, 2);
		gridLayout.setHgap(10);
		bottomPanel.setLayout(gridLayout);
		
		scheduleButton = new ViewerButton();
		bottomPanel.add(scheduleButton);
		scheduleButton.setText("View Schedule");
		
		offspringDropdown = new ViewerButton(); // to be made into a dropdown
		bottomPanel.add(offspringDropdown);
		offspringDropdown.setText("Offspring    V");
		
		parentLabel = new JLabel();
		bottomPanel.add(parentLabel, c);
		parentLabel.setText("Parents:");
		
		bottomPanel.add(new SubPanel());
		
		Creature parent1 = creature.getParentOne();
		Creature parent2 = creature.getParentTwo();
		
		parentOneButton = new ViewerButton();
		parentOneButton.addActionListener((new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent evt) {
		    	parent1.openViewer();
		    }
		}));
		bottomPanel.add(parentOneButton);
		
		parentTwoButton = new ViewerButton();
		parentTwoButton.addActionListener((new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent evt) {
		    	parent2.openViewer();
		    }
		}));
		bottomPanel.add(parentTwoButton);
	
		
		if (parent1 == null || parent2 == null) {
			parentOneButton.setText("N/A");
			parentTwoButton.setText("N/A");
			parentOneButton.setEnabled(false);
			parentTwoButton.setEnabled(false);
		} else {
			parentOneButton.setText("Creature #" + parent1.getCreatureNum());
			parentTwoButton.setText("Creature #" + parent2.getCreatureNum());

		}
		
		panel.add(bottomPanel);
		
		CreatureViewer.changeFont(panel, new Font(Font.MONOSPACED, Font.PLAIN, 12));
		this.add(panel);
		this.pack();
	}
	
	public void update() {
		positionLabel.setText("X: " + creature.posX + "   Y: " + creature.posY);
		energyLabel.setText("Energy: " + creature.getEnergy());
	}
	
	public static void changeFont(Component component, Font font) {
	    component.setFont (font);
	    if (component instanceof Container) {
	        for (Component child : ((Container) component).getComponents()) {
	            changeFont (child, font);
	        }
	    }
	}

	
	
}
