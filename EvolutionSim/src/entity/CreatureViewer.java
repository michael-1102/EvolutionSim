package entity;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CreatureViewer extends JDialog {
	
	private final static int viewerWidth = 300;
	private final static int viewerHeight = 350;
	
	
	private class CreatureDrawing extends JPanel {
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(creature.getColor());
			g.fillRect(0, 0, 20, 20);
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(20, 20);
		}
	}
	
	private CreatureDrawing drawing;
	
	private JLabel genLabel;
	private JLabel posLabel;
	
	
	private Creature creature;
	private JPanel panel;
	
	public CreatureViewer(Creature creature) {
		this.creature = creature;
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setTitle("Creature Viewer");
		this.panel = new JPanel();
		panel.setPreferredSize(new Dimension(viewerWidth, viewerHeight));
		panel.setBackground(Color.white);
		panel.setDoubleBuffered(true);
		panel.setLayout(new GridLayout(3, 1));
		
		drawing = new CreatureDrawing();
		drawing.setBackground(Color.white);
		panel.add(drawing);
		genLabel = new JLabel();
		panel.add(genLabel);
		posLabel = new JLabel();
		panel.add(posLabel);
		
		this.add(panel);
		this.pack();
	}
	
	public void update() {
		genLabel.setText("Generation: " + creature.getGeneration());
		posLabel.setText("X: " + creature.posX + "   Y: " + creature.posY);
		
	}
	
}
