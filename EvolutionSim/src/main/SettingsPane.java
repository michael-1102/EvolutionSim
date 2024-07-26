package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;


public class SettingsPane extends JTabbedPane {
	private GlobalData globalData;

	private JPanel creatureTab;
	private JPanel terrainTab;
	private JPanel configTab;
	
	public SettingsPane() {
		globalData = GlobalData.getInstance();
		this.setPreferredSize(new Dimension(globalData.tileSize * 16, globalData.screenHeight + globalData.getTimerPanel().getPreferredSize().height));
		this.setDoubleBuffered(true);
		this.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		
		creatureTab = new JPanel();
		terrainTab = new JPanel();
		configTab = new JPanel();
		
		this.addTab("Creatures", creatureTab);
		this.addTab("Terrain", terrainTab);
		this.addTab("Settings", configTab);

	}
}
