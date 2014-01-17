package com.danielkueffer.wator;

import java.awt.Color;
import java.awt.Point;

public class Shark extends Cell {
	
	private int lastFedTime = 1;
	
	/**
	 * Update sharks
	 */
	protected void internalUpdate(Planet pl, int x, int y) {
		Point[] freeCells;
		Point[] freeFishCells;
		
		int newCellX = x;
		int newCellY = y;
		
		int pWidth = pl.getPlanetWidth();
		int pHeight = pl.getPlanetHeight();
		
		freeFishCells = pl.getFishCells(x, y);
		
		// Shark eats fishes on neighbour cells
		if (freeFishCells.length > 0) {
			for (Point p : freeFishCells) {
				pl.setField(p.x, p.y, null);
				
				String newFishPos = pl.normalizeCoordinates(p.x, p.y);
				pl.usedCellsSet.add(newFishPos);
			}
			
			// Shark found fish, reset feed time
			lastFedTime = 1;
		}
		
		freeCells = pl.getFreeCells(x, y);
		
		// Shark moves to a free cell if the starvation time is not yet reached
		if (freeCells.length > 0 && ! isDead(pl)) {
			Point newCell = pl.getRandomPoint(freeCells);
			
			String newPos = pl.normalizeCoordinates(newCell.x, newCell.y);
			
			Shark oldShark = (Shark)pl.getField(x, y);
			
			pl.setField(x, y, null);
		
			pl.setField(newCell.x, newCell.y, oldShark);
			
			pl.usedCellsSet.add(newPos);
			
			lastFedTime++;
		}
		
		// Delete shark if starvation  time is reached
		if (isDead(pl)) {
			pl.setField(x, y, null);
		}
		
		// Create new shark if breed time is reached
		if (pl.getGeneration() % pl.getCurrentSharkBreed() == 0 && ! isDead(pl)) {
					
			if (newCellX == -1 || newCellX == pWidth) {
				newCellX = pWidth - 1;
			}
			
			if (newCellY == -1 || newCellY == pHeight) {
				newCellY = pHeight - 1;
			}
			
			this.createNewShark(pl, newCellX, newCellY);	
		}
	}
	
	/**
	 * Create new shark
	 * @param pl
	 * @param x
	 * @param y
	 */
	private void createNewShark(Planet pl, int x, int y) {
		
		Point[] newSharkCells;
		newSharkCells = pl.getFreeCells(x, y);
		
		if (newSharkCells.length > 0) {
			
			Point createCell = pl.getRandomPoint(newSharkCells);
			
			String posCreate = pl.normalizeCoordinates(createCell.x, createCell.y);
			
			pl.setField(createCell.x, createCell.y, pl.getNewCellInstance(CellType.SHARK));
			
			pl.usedCellsSet.add(posCreate);
		}
	}
	
	/**
	 * Check if starvation time is reached
	 * @param pl
	 * @return
	 */
	private boolean isDead(Planet pl) {
		if (lastFedTime >= pl.getCurrentStarvationTime()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Return shark color
	 */
	public Color cellColor() {
		return new Color(0xFC1100);
	}
}