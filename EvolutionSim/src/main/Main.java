package main;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		GlobalData globalData = GlobalData.getInstance();
		JFrame frame = globalData.getFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frame.setTitle("Evolution Simulator");
		
		frame.addComponentListener(new ComponentAdapter() {
		      @Override
		      public void componentResized(ComponentEvent e) {
		        globalData.setTileSize();
		      }
		    });
		
		GridPanel gridPanel = globalData.getGridPanel();
		gridPanel.setLayout(null);
		frame.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 0;
		frame.add(globalData.getTimerPanel(), constraints);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 0;
		constraints.gridy = 1;
		frame.add(gridPanel,constraints);
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridheight = 2;
		frame.add(globalData.getSettingsPane(), constraints);
		
		frame.pack();

		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		globalData.setUpSim();
		gridPanel.run();
		
		
	}
}
