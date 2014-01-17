package com.danielkueffer.wator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class CustomPanel extends JPanel {
		
	Planet pl;
	CustomPanel cp = this;
	
	public CustomPanel(Planet planet) {
		setOpaque(false);
		
		this.pl = planet;
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				processEvent(e);
			}
		});
	}
	
	/**
	 * Mouse clicked
	 * @param e
	 */
	public void processEvent(MouseEvent e) {
		
		int cWidth = pl.getCellWidth();
		int cHeight = pl.getCellHeight();
		
		if (pl.isCustomCellSize()) {
			cWidth = pl.getUpdCellWidth();
			cHeight = pl.getUpdCellHeight();
		}
		
		int x = e.getX() / cWidth;
		int y = e.getY() / cHeight;
		
		Cell c = pl.getField(x, y);
		
		if (c == null) {
			pl.setField(x, y, pl.getNewCellInstance(CellType.FISH));
		}
		else if (c instanceof Fish) {
			pl.setField(x, y, pl.getNewCellInstance(CellType.SHARK));
		}
		else {
			pl.setField(x, y, pl.getNewCellInstance(CellType.OCEAN));
		}
		
		repaint();
	}
	
	/**
	 * Set window width and height
	 */
	public Dimension getPreferredSize() {
		return new Dimension(pl.getPlanetWidth()*pl.getCellWidth(), pl.getPlanetHeight()*pl.getCellHeight());
	}
	
	/**
	 * Paint cells
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int plHeight = pl.getPlanetFields().length;
		int plWidth = pl.getPlanetFields()[0].length;

		int cWidth = pl.getCellWidth();
		int cHeight = pl.getCellHeight();
		
		if (pl.isCustomCellSize()) {
			cWidth = pl.getUpdCellWidth();
			cHeight = pl.getUpdCellHeight();
		}
		
		Color cellBorder = pl.getBorderColor();
		
		for(int x=0; x<plHeight; x++) {
			for(int y=0; y<plWidth; y++) {
				
				Cell c = pl.getField(x, y);

				g.setColor(pl.getColor(c));
				
				// Cell background
				g.fillRect(x * cWidth, y * cHeight, cWidth, cHeight);
				
				// Cell border
				g.setColor(cellBorder);
				g.drawRect(x * cWidth, y * cHeight, cWidth, cHeight);
			}
		}
		
		if (pl.isAppLog()) {
			System.out.println("repaintet");
		}
	}
}