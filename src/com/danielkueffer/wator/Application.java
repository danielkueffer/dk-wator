package com.danielkueffer.wator;

import javax.swing.SwingUtilities;

public class Application {
	
	public Application() {
		this.initialize();
	}
	
	/**
	 * Initialize application
	 */
	private void initialize() {
		Planet pl = new Planet();
		
		CustomPanel cp = pl.initPanel();
		
		pl.initPlanet();
		
		AppGUI ag = new AppGUI(cp, pl);
		ag.initGUI();
		ag.initTimer();
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					new Application();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
