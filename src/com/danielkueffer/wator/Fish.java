package com.danielkueffer.wator;

import java.awt.Color;
import java.awt.Point;

public class Fish extends Cell {

	/**
	 * Update fishes
	 */
	protected void internalUpdate(Planet pl, int x, int y) {
		Point[] freeCells;
		freeCells = pl.getFreeCells(x, y);
		
		int newCellX = x;
		int newCellY = y;
		
		int pWidth = pl.getPlanetWidth();
		int pHeight = pl.getPlanetHeight();
		
		// Fish moves to a neighbour cell
		if (freeCells.length > 0) {
			Point newCell = pl.getRandomPoint(freeCells);
			
			String newPos = pl.normalizeCoordinates(newCell.x, newCell.y);
			
			Fish oldFish = (Fish) pl.getField(x, y);
			
			pl.setField(x, y, null);
		
			pl.setField(newCell.x, newCell.y, oldFish);
			
			pl.usedCellsSet.add(newPos);

			newCellX = newCell.x;
			newCellY = newCell.y;
			
			if (pl.isFishLog()) {
				System.out.println("Fish moved to: X=" + newCellX + " :Y=" + newCellY);
			}
		}
		
		// Create new fish if fish breed is reached
		if (pl.getGeneration() % pl.getCurrentFishBreed() == 0) {
			
			if (newCellX == -1 || newCellX == pWidth) {
				newCellX = pWidth - 1;
			}
			
			if (newCellY == -1 || newCellY == pHeight) {
				newCellY = pHeight - 1;
			}
			
			this.createNewFish(pl, newCellX, newCellY);	
		}
	}
	
	/**
	 * Create new fish
	 * @param pl
	 * @param x
	 * @param y
	 */
	private void createNewFish(Planet pl, int x, int y) {
		
		Point[] newFishCells;
		newFishCells = pl.getFreeCells(x, y);
		
		if (newFishCells.length > 0) {
			
			Point createCell = pl.getRandomPoint(newFishCells);
			
			String posCreate = pl.normalizeCoordinates(createCell.x, createCell.y);
			
			if (pl.isFishLog()) {
				System.out.println("new fish:X=" + createCell.x + " :Y=" + createCell.y + " :in arr: " + posCreate);
			}
			
			pl.setField(createCell.x, createCell.y, pl.getNewCellInstance(CellType.FISH));
			
			pl.usedCellsSet.add(posCreate);
		}
	}
	
	/**
	 * Return fish color
	 */
	public Color cellColor() {
		return new Color(0x0065FC);
	}
}