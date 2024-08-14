package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Image;
import java.awt.Insets;

public class TimerPanel extends JPanel implements ActionListener {
	// use a better way to store these constants
	private final static int hoursInDay = 24; // number of real life hours in a real life day
	private final static int minsInHour = 60; // number of real life minutes in a real life hour
	private final static int minsInDay = minsInHour * hoursInDay; // number of real life minutes in a real life day
	private final static int dayLength = 4320; // amount of time (frames) in a day (and amount of time in a night)
	private final static int fullDayLength = dayLength * 2;
	
	private final static int fpsMin = 1;
	private final static int fpsMax = 200;
	private final static int fpsInterval = 10;
	private final static int fpsInitial = fpsMin+fpsInterval*4;

	
	private final static int dayCutOff = 8; // cutoff of when it is day time
	
	private Image pauseImg; //pause button image
	private Image pauseHoverImg; //pause button image when button is hovered
	
	private Image playImg; //pause button image
	private Image playHoverImg; //pause button image when button is hovered
	
	private int time; // time of day
	private int dayCount; // day count (starts at 1)
	private JLabel timeLabel;
	private JLabel dayLabel;
	private JButton pauseButton;
	private JSlider fpsSlider;
	private GlobalData globalData;

	private int height = 34;
	
	public TimerPanel() {
		globalData = GlobalData.getInstance();		
		time = 0;
		dayCount = 1;
		this.setBackground(Color.blue);
		//this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		timeLabel = new JLabel("00:00");
		timeLabel.setHorizontalAlignment(JLabel.LEFT);
		timeLabel.setVerticalAlignment(JLabel.CENTER);
		timeLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, (int)(height * (3.0/4.0))));
		timeLabel.setForeground(Color.yellow);
		c.gridx = 0;
		c.gridy = 0;
		this.add(timeLabel, c);
		
		dayLabel = new JLabel("Day " + dayCount);
		dayLabel.setHorizontalAlignment(JLabel.CENTER);
		dayLabel.setVerticalAlignment(JLabel.CENTER);
		dayLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, (int)(height * (3.0/4.0))));
		dayLabel.setForeground(Color.yellow);
		c.gridx = 1;
		c.weightx = 1;
		this.add(dayLabel, c);
		
		fpsSlider = new JSlider(JSlider.HORIZONTAL, 0, fpsMax/fpsInterval, fpsInitial/fpsInterval);
		fpsSlider.setMinorTickSpacing(1);
		fpsSlider.setPaintTicks(true);

		fpsSlider.addChangeListener((new ChangeListener() {
		    @Override
		    public void stateChanged(ChangeEvent evt) {
		        if (!fpsSlider.getValueIsAdjusting()) {
		        	int fps = (int) (fpsInterval * Math.ceil(1.0*(fpsMax - fpsMin) / fpsMax) * fpsSlider.getValue() + 1);
		            globalData.setUnpausedFPS(fps);
		        }
		    }
		}));

		c.gridx = 2;
		this.add(fpsSlider, c);

		try {
			pauseImg = ImageIO.read(getClass().getResource("/resources/pause.png"));
			pauseImg = pauseImg.getScaledInstance(height, height,  java.awt.Image.SCALE_SMOOTH );
			
			playImg = ImageIO.read(getClass().getResource("/resources/play.png"));
			playImg = playImg.getScaledInstance(height, height,  java.awt.Image.SCALE_SMOOTH );
			
			pauseHoverImg = ImageIO.read(getClass().getResource("/resources/pauseHover.png"));
			pauseHoverImg = pauseHoverImg.getScaledInstance(height, height,  java.awt.Image.SCALE_SMOOTH );
			
			playHoverImg = ImageIO.read(getClass().getResource("/resources/playHover.png"));
			playHoverImg = playHoverImg.getScaledInstance(height, height,  java.awt.Image.SCALE_SMOOTH );
		} catch (Exception ex) {
			System.out.println(ex);
		}
		pauseButton = new JButton() {
			@Override
			public Dimension getPreferredSize() {
			    return new Dimension(height, height);
			}
		};
		if (!globalData.getPaused()) {
			pauseButton.setIcon(new ImageIcon(pauseImg));
		} else {
			pauseButton.setIcon(new ImageIcon(playImg));

		}
		  
		
		pauseButton.setFocusable(false);
		pauseButton.setBorderPainted(false);
		pauseButton.setContentAreaFilled(false);
		pauseButton.setBorder(null);
		pauseButton.addActionListener(this);
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
		c.gridx = 3;
		c.weightx = 0;
		c.insets = new Insets(0, 100, 0, 0); // left padding
		this.add(pauseButton, c);
	}
	
	@Override
	public int getHeight() {
		return height;
	}
	
	public static int getInitialFPS() {
		return fpsInitial;
	}
	
	public void actionPerformed(ActionEvent e){  
		globalData.pause();
		if (globalData.getPaused()) {
			pauseButton.setIcon(new ImageIcon(playHoverImg));
		} else {
			pauseButton.setIcon(new ImageIcon(pauseHoverImg));
		}
	}  
	
	
	public void incrementTime() {
		time++;
		if (time % fullDayLength == 0) dayCount++;
		dayLabel.setText("Day " + dayCount);
		int mins =(int) (1.0*minsInDay/fullDayLength * time);
		timeLabel.setText(getTimeString(mins));
	}
	
	public int getSkyAlpha() {
		int maxAlpha = 150;
		return 0;
		//TODO: calculate alpha based on time of day
		//return (int) (-1*Math.abs(255.0/dayLength * ((time+dayLength) % fullDayLength - dayLength))) + 255;
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
	
	/*
	 converts number of frames to number of minutes
	 */
	public static int framesToMins(int frames) {
		return (int) (1.0 * frames / fullDayLength * minsInDay);
	}
	
	
	public static int minsToFrames(int mins) {
		return (int) (1.0 * mins * fullDayLength / minsInDay);
	}
	
	private static String getTimeString(int totalMins) {
		int hour = totalMins/minsInHour % hoursInDay;
		return String.format("%02d", hour) + ":" + String.format("%02d", totalMins % minsInHour); 
	}
	
	public static String getTimeFromIndex(int i, int size) {
		int interval = minsInDay/size;
		int totalMins = interval * i;
		return getTimeString(totalMins);
	}
}
