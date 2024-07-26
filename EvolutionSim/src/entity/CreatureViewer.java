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
import javax.swing.SwingConstants;

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
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		middlePanel.add(new JLabel("-", SwingConstants.CENTER), c);
		c.gridwidth = 1;

		int[] col = new int[1];
		col[0] = 1;
		addAttribute(middlePanel, "Maximum Energy:", Integer.toString(creature.getMaxEnergy()), col, c);
		addAttribute(middlePanel, "Energy Given To Each Offspring:", Integer.toString(creature.getMaxEnergyDuringMating()), col, c);
		addAttribute(middlePanel, "Mating Cooldown:", getTimeStr(TimerPanel.framesToMins(creature.getMateCooldown())), col, c);
		addAttribute(middlePanel, "Daytime Vision:", Integer.toString(creature.getDaySight()), col, c);
		addAttribute(middlePanel, "Nighttime Vision:", Integer.toString(creature.getNightSight()), col, c);
		addAttribute(middlePanel, "Speed:", df.format(creature.getSpeed()), col, c);
		
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 7;
		middlePanel.add(new JLabel("-", SwingConstants.CENTER), c);
		
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
		offspringDropdown.setText("View Offspring  V");
		
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
	
	private void addAttribute(JPanel panel, String name, String valueStr, int[] col, GridBagConstraints c) {
		c.gridx = 0;
		c.gridy = col[0];
		panel.add(new JLabel(name), c);
		c.gridx = 1;
		panel.add(new JLabel(valueStr), c);
		col[0]++;
	}
	
	private String getTimeStr(int mins) {
		int hrs = mins / 60;
		if (hrs > 0) {
			return hrs + "h " + mins % 60 + "m";
		} else {
			return mins + "m";
		}
	}
	public void update() {
		positionLabel.setText("X: " + creature.posX + "   Y: " + creature.posY);
		energyLabel.setText("Energy: " + creature.getEnergy());
	}
	
	private static void changeFont(Component component, Font font) {
	    component.setFont (font);
	    if (component instanceof Container) {
	        for (Component child : ((Container) component).getComponents()) {
	            changeFont (child, font);
	        }
	    }
	}

	
	
}
