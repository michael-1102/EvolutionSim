package main;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		GlobalData globalData = GlobalData.getInstance();
		JFrame frame = globalData.getFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setTitle("Evolution Simulator");
		
		GridPanel gridPanel = globalData.getGridPanel();
		frame.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		frame.add(globalData.getTimerPanel(), constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		frame.add(gridPanel,constraints);
		
		frame.pack();
		

		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		globalData.setUpSim();
		gridPanel.startThread();
		
	}
}
