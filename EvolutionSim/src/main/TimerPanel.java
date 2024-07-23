package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import entity.Creature;
import entity.Entity;
import entity.EntityCollection;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Image;

public class TimerPanel extends JPanel implements ActionListener {
	// use a better way to store these constants
	private final static int hoursInDay = 24; // number of real life hours in a real life day
	private final static int minsInHour = 60; // number of real life minutes in a real life hour
	private final static int minsInDay = minsInHour * hoursInDay; // number of real life minutes in a real life day
	private final static int dayLength = 4320; // amount of time in a day (and amount of time in a night)
	private final static int fullDayLength = dayLength * 2;
	
	private final static int dayCutOff = 8; // cutoff of when it is day time
	
	Image pauseImg; //pause button image
	Image pauseHoverImg; //pause button image when button is hovered
	
	Image playImg; //pause button image
	Image playHoverImg; //pause button image when button is hovered
	
	private int time; // time of day
	private int dayCount; // day count (starts at 1)
	private JLabel timeLabel;
	private JLabel dayLabel;
	private JButton pauseButton;
	private GlobalData globalData;

	public TimerPanel() {
		globalData = GlobalData.getInstance();
		time = 0;
		dayCount = 1;
		this.setPreferredSize(new Dimension(globalData.screenWidth, globalData.tileSize*2));
		this.setBackground(Color.blue);
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setLayout(new BorderLayout());
		
		timeLabel = new JLabel("00:00");
		timeLabel.setHorizontalAlignment(JLabel.LEFT);
		timeLabel.setVerticalAlignment(JLabel.CENTER);
		timeLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, globalData.tileSize*2));
		timeLabel.setForeground(Color.yellow);
		this.add(timeLabel, BorderLayout.WEST);
		
		dayLabel = new JLabel("Day " + dayCount);
		dayLabel.setHorizontalAlignment(JLabel.CENTER);
		dayLabel.setVerticalAlignment(JLabel.CENTER);
		dayLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, (int)(globalData.tileSize*1.5)));
		dayLabel.setForeground(Color.yellow);
		this.add(dayLabel, BorderLayout.CENTER);
		
		try {
			pauseImg = ImageIO.read(getClass().getResource("/resources/pause.png"));
			pauseImg = pauseImg.getScaledInstance(globalData.tileSize*2, globalData.tileSize*2,  java.awt.Image.SCALE_SMOOTH );
			
			playImg = ImageIO.read(getClass().getResource("/resources/play.png"));
			playImg = playImg.getScaledInstance(globalData.tileSize*2, globalData.tileSize*2,  java.awt.Image.SCALE_SMOOTH );
			
			pauseHoverImg = ImageIO.read(getClass().getResource("/resources/pauseHover.png"));
			pauseHoverImg = pauseHoverImg.getScaledInstance(globalData.tileSize*2, globalData.tileSize*2,  java.awt.Image.SCALE_SMOOTH );
			
			playHoverImg = ImageIO.read(getClass().getResource("/resources/playHover.png"));
			playHoverImg = playHoverImg.getScaledInstance(globalData.tileSize*2, globalData.tileSize*2,  java.awt.Image.SCALE_SMOOTH );
		} catch (Exception ex) {
			System.out.println(ex);
		}
		pauseButton = new JButton();
		pauseButton.setIcon(new ImageIcon(pauseImg));
		  
		
		pauseButton.setFocusable(false);
		pauseButton.setBorderPainted(false);
		pauseButton.setContentAreaFilled(false);
		pauseButton.setBorder(null);
		pauseButton.addActionListener(this);
		pauseButton.setPreferredSize(new Dimension(globalData.tileSize*2, globalData.tileSize*2));
		pauseButton.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	if (globalData.getPaused()) {
		    		pauseButton.setIcon(new ImageIcon(playHoverImg));

		    	} else {
		    		pauseButton.setIcon(new ImageIcon(pauseHoverImg));
		    	}
		    	
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	if (globalData.getPaused()) {
		    		pauseButton.setIcon(new ImageIcon(playImg));
		    	} else {
		    		pauseButton.setIcon(new ImageIcon(pauseImg));
		    	}
		    }
		});
		this.add(pauseButton, BorderLayout.EAST);
	}
	
	public void actionPerformed(ActionEvent e){  
		globalData.pause();
		if (globalData.getPaused()) {
			/*
			ArrayList<Entity> arr = globalData.getEntities().getEntities();
			for (Entity entity : arr) {
				if (entity instanceof Creature)
					System.out.println(entity.getPosX() + " " + entity.getPosY());
			}
			*/
			pauseButton.setIcon(new ImageIcon(playHoverImg));
		} else {
			pauseButton.setIcon(new ImageIcon(pauseHoverImg));
		}
	}  
	
	public void incrementTime() {
		time++;
		int mins =(int) (1.0*minsInDay/fullDayLength * time);
		int hour = mins/minsInHour % hoursInDay;
		if (time % fullDayLength == 0) dayCount++;
		dayLabel.setText("Day " + dayCount);
		timeLabel.setText(String.format("%02d", hour) + ":" + String.format("%02d", mins % minsInHour));
		int colorVal = (int) Math.abs(255.0/dayLength * ((time+dayLength) % fullDayLength - dayLength));
		GridPanel gridPanel = globalData.getGridPanel();
		gridPanel.setBackground(new Color(colorVal, colorVal, colorVal));
	}
	
	public int getTime() {
		return time;
	}
	
	public int getMinuteOfDay() {
		return (int) (1.0*minsInDay/fullDayLength * time) % minsInDay;

	}
	
	/*
	 return true if time is >= dayCutoff AM and < dayCutoff PM
	 */
	public boolean isDay() {
		int mins =(int) (1.0*minsInDay/fullDayLength * time);
		int hour = mins/minsInHour % hoursInDay;
		return (hour >= dayCutOff && hour < hoursInDay/2 + dayCutOff);
	}
}
