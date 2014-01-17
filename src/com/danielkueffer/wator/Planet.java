package com.danielkueffer.wator;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class Planet {
	
	/* Initial values */
	private int numberOfFish = 216;
	private int numberOfShark = 81;
	
	private int starvationTime = 4;
	
	private int sharkBreed = 3;
	private int fishBreed = 2;
	
	private int x = 90;
	private int y = 90;
	private int maxCells = 2700; /* 8100 / 3 */
	
	private final int cellWidth = 5;
	private final int cellHeight = 5;
	
	/* Updated values */
	private int updNumberOfFish = 0;
	private int updNumberOfShark = 0;
	private int updFishBreed = 0;
	private int updSharkBreed = 0;
	private int updStarvationTime = 0;
	
	private boolean customCellSize = false;
	private int updCellWidth = 0;
	private int updCellHeight = 0;

	private final Color planetColor = Color.black;
	private final Color borderColor = Color.black;
		
	private int generation = 1;
	
	private int fishCount = numberOfFish;
	private int sharkCount = numberOfShark;
	
	private Cell planetFields[][] = new Cell[x][y];
	
	CustomPanel cp;
	
	public ArrayList<String> newFishCells = new ArrayList<String>();
	
	public HashSet<String> usedCellsSet = new HashSet<String>();
	
	private boolean appLog = false;
	private boolean fishLog = false;
	
	public static final Point[] NEIGHBOUR_POINTS = {
		new Point(0, -1), new Point(1, 0), new Point(0, 1), new Point(-1, 0)
	};
	
	/**
	 * Init custom panel
	 * @return
	 */
	public CustomPanel initPanel() {
		this.cp = new CustomPanel(this);
		return this.cp;
	}
	
	/**
	 * Initialize planet
	 */
	public void initPlanet() {
		this.fillFields();
		this.fillPlanetRandom();
	}
	
	/**
	 * Initial field filling
	 */
	private void fillFields() {
		for(int i=0; i<planetFields.length; i++) {
			for(int j=0; j<planetFields[i].length; j++) {
				this.planetFields[i][j] = null;
			}
		}
	}
	
	/**
	 * Fill the initial cells
	 */
	public void fillPlanetRandom() {
		Random r = new Random();
		
		int w = this.getPlanetWidth();
		int h = this.getPlanetHeight();

		for (int i=0; i<this.numberOfFish; i++) {
			int x = r.nextInt(w);
			int y = r.nextInt(h);
			
			this.setField(x, y, this.getNewCellInstance(CellType.FISH));
		}
		
		for (int i=0; i<this.numberOfShark; i++) {
			int x = r.nextInt(w);
			int y = r.nextInt(h);
			
			this.setField(x, y, this.getNewCellInstance(CellType.SHARK));
		}
		
		this.cp.repaint();
	}
	
	/**
	 * Add new fishes control
	 */
	public void addFishes() {
		Random r = new Random();
		
		int w = this.getPlanetWidth();
		int h = this.getPlanetHeight();
		
		int k = 0;
		
		for (int i=0; i<this.updNumberOfFish; i++) {
			int x = r.nextInt(w);
			int y = r.nextInt(h);
			
			int check = this.checkField(this.planetFields[x][y]);
			
			if (check == CellType.OCEAN.getValue()) {
				this.setField(x, y, this.getNewCellInstance(CellType.FISH));
				k++;
			}
		}
		
		this.fishCount = k;
		this.cp.repaint();
	}
	
	/**
	 * Add new sharks control
	 */
	public void addSharks() {
		Random r = new Random();
		
		int w = this.getPlanetWidth();
		int h = this.getPlanetHeight();
		
		int k = 0;
		
		for (int i=0; i<this.updNumberOfShark; i++) {
			int x = r.nextInt(w);
			int y = r.nextInt(h);
			
			int check = this.checkField(this.planetFields[x][y]);
			
			if (check == CellType.OCEAN.getValue()) {
				this.setField(x, y, this.getNewCellInstance(CellType.SHARK));
				k++;
			}
		}
		
		this.sharkCount = k;
		
		this.cp.repaint();
	}
	
	/**
	 * Remove all fishes
	 */
	public void resetFishes() {
		for(int i=0; i<planetFields.length; i++) {
			for(int j=0; j<planetFields[i].length; j++) {
				int check = this.checkField(this.planetFields[i][j]);
				
				if (check == CellType.FISH.getValue()) {
					this.planetFields[i][j] = null;
				}
			}
		}
	}
	
	/**
	 * Remove all sharks
	 */
	public void resetSharks() {
		for(int i=0; i<planetFields.length; i++) {
			for(int j=0; j<planetFields[i].length; j++) {
				int check = this.checkField(this.planetFields[i][j]);
				
				if (check == CellType.SHARK.getValue()) {
					this.planetFields[i][j] = null;
				}
			}
		}
	}
	
	/**
	 * Reset planet to initial values
	 */
	public void resetPlanet() {
		this.initPlanet();
		
		this.fishCount = this.numberOfFish;
		this.sharkCount = this.numberOfShark;
		this.generation = 1;
		
		this.updNumberOfFish = 0;
		this.updNumberOfShark = 0;
		this.updFishBreed = 0;
		this.updSharkBreed = 0;
		this.updStarvationTime = 0;
	}
	
	/**
	 * Get the current fish breed time
	 * @return
	 */
	public int getCurrentFishBreed() {
		int actFishBreed;
		
		if (this.updFishBreed == 0) {
			actFishBreed = this.fishBreed;
		}
		else {
			actFishBreed = this.updFishBreed;
		}
		
		return actFishBreed;
	}
	
	/**
	 * Get the current shark breed time
	 * @return
	 */
	public int getCurrentSharkBreed() {
		int actSharkBreed;
		
		if (this.updSharkBreed == 0) {
			actSharkBreed = this.sharkBreed;
		}
		else {
			actSharkBreed = this.updSharkBreed;
		}
		
		return actSharkBreed;
	}
	
	/**
	 * Get the current starvation time
	 * @return
	 */
	public int getCurrentStarvationTime() {
		int actStarvation;
		
		if (this.updStarvationTime == 0) {
			actStarvation = this.starvationTime;
		}
		else {
			actStarvation = this.updStarvationTime;
		}
		
		return actStarvation;
	}
	
	
	
	/**
	 * Set new field value
	 * @param x
	 * @param y
	 * @param value
	 * @return
	 */
	public Cell setField(int x, int y, Cell value) {
		return planetFields[(this.getPlanetWidth() + (x % this.getPlanetWidth())) % this.getPlanetWidth()][(this.getPlanetHeight()+(y % this.getPlanetHeight())) % this.getPlanetHeight()] = value;
	}
	
	/**
	 * Get a field value
	 * @param x
	 * @param y
	 * @return
	 */
	public Cell getField(int x, int y) {
		return planetFields[(this.getPlanetWidth() + (x % this.getPlanetWidth())) % this.getPlanetWidth()][(this.getPlanetHeight()+(y % this.getPlanetHeight())) % this.getPlanetHeight()];
	}
	
	/**
	 * Check the class value of a field
	 * @param c
	 * @return
	 */
	public int checkField(Cell c) {
		
		int i = 0;
		
		if (c == null) {
			i = CellType.OCEAN.getValue();
		}
		else if (c instanceof Fish) {
			i = CellType.FISH.getValue();
		}
		else {
			i = CellType.SHARK.getValue();
		}
		
		return i;
	}
	
	/**
	 * Get cell color
	 * @param c
	 * @return
	 */
	public Color getColor(Cell c) {
		
		Color color;
		
		if (c == null) {
			color = this.getPlanetColor();
		}
		else {
			color = c.cellColor();
		}
		
		return color;
	}
	
	/**
	 * Make new instance of a cell
	 * @param x
	 * @param y
	 * @return
	 */
	public Cell getNewCellInstance(CellType ct) {
		
		Cell c = null;
		
		int val = ct.getValue();
		
		if (val == 2) {
			c = new Fish();
		}
		
		if (val == 3) {
			c = new Shark();
		}
		
		return c;
	}
	
	/**
	 * Get free neighbour cells
	 * @param x
	 * @param y
	 * @return
	 */
	public Point[] getFreeCells(int x, int y) {
		Point[] freeCells = {};
		Cell cell;
		
		for (int i=0; i<NEIGHBOUR_POINTS.length; i++) {
			Point point = NEIGHBOUR_POINTS[i];
			
			cell = this.getField(x + point.x, y + point.y);
			
			if (cell == null) {
				freeCells = Arrays.copyOf(freeCells, freeCells.length+1);
				freeCells[freeCells.length-1] = new Point(x + point.x, y + point.y);
			}
		}
		
		return freeCells;
	}
	
	/**
	 * Get fish cells
	 * @param x
	 * @param y
	 * @return
	 */
	public Point[] getFishCells(int x, int y) {
		Point[] freeCells = {};
		Cell cell;
		
		for (int i=0; i<NEIGHBOUR_POINTS.length; i++) {
			Point point = NEIGHBOUR_POINTS[i];
			
			cell = this.getField(x + point.x, y + point.y);
			
			if (this.checkField(cell) == CellType.FISH.getValue()) {
				freeCells = Arrays.copyOf(freeCells, freeCells.length+1);
				freeCells[freeCells.length-1] = new Point(x + point.x, y + point.y);
			}
		}
		
		return freeCells;
	}
	
	/**
	 * Get random point from array
	 * @param freeCells
	 * @return
	 */
	public Point getRandomPoint(Point[] freeCells) {
		Random generator = new Random();
		int randomIndex = generator.nextInt(freeCells.length);
		
		return freeCells[randomIndex];
	}
	
	/**
	 * Prepare coordinates for the usedCellsSet array
	 * @param x
	 * @param y
	 * @return
	 */
	public String normalizeCoordinates(int x, int y) {
		
		String posX = x + "";
		posX = posX.replace("-1", this.getPlanetWidth() - 1 + "");
		posX = posX.replace(this.getPlanetWidth() + "", "0");
		
		String posY = y + "";
		posY = posY.replace("-1", this.getPlanetHeight() - 1 + "");
		posY = posY.replace(this.getPlanetHeight() + "", "0");
		
		return posX + ":" + posY;
	}
	
	/**
	 * Perform single step
	 */
	public void step() {
		if (this.isAppLog()) {
			System.out.println("Generation: " + this.generation + " :");
		}
		
		int numFish = 0;
		int numShark = 0;
		
		int plWidth = planetFields.length;
		int plHeight = planetFields[0].length;
		
		for(int x=0; x<plWidth; x++) {
			for(int y=0; y<plHeight; y++) {
				Cell c = this.getField(x, y);
				
				if (c != null) {
					String pos = x + ":" + y;
					
					if (! this.usedCellsSet.contains(pos)) {
						if (this.fishLog) {
							System.out.println(c.getClass().getName() + ":X=" + x + " :Y=" + y);
						}
						
						c.update(this, x, y);
						
						int checkType = this.checkField(c);
						
						if (checkType == CellType.FISH.getValue()) {
							numFish++;
						}
						else {
							numShark++;
						}
					}
				}
			}
		}
		
		this.usedCellsSet.clear();
		
		this.generation++;
		
		if (this.isAppLog()) {
			System.out.println("Number of fishes: " + numFish);
			System.out.println("Number of sharks: " + numShark);
		}
		
		this.fishCount = numFish;
		this.sharkCount = numShark;
		
		cp.repaint();
	}
	
	public int getPlanetWidth() {
		return this.x;
	}
	
	public int getPlanetHeight() {
		return this.y;
	}
	
	public int getCellWidth() {
		return this.cellWidth;
	}
	
	public int getCellHeight() {
		return this.cellHeight;
	}
	
	public Cell[][] getPlanetFields() {
		return this.planetFields;
	}
	
	public int getNumberOfFish() {
		return this.numberOfFish;
	}

	public int getNumberOfShark() {
		return this.numberOfShark;
	}
	
	public int getStarvationTime() {
		return this.starvationTime;
	}
	
	public int getSharkBreed() {
		return this.sharkBreed;
	}
	
	public int getFishBreed() {
		return this.fishBreed;
	}
	
	public CustomPanel getCP() {
		return this.cp;
	}
	
	public int getGeneration() {
		return this.generation;
	}
	
	public boolean isAppLog() {
		return this.appLog;
	}
	
	public boolean isFishLog() {
		return this.fishLog;
	}
	
	public Color getPlanetColor() {
		return this.planetColor;
	}
	
	public Color getBorderColor() {
		return this.borderColor;
	}
	
	public int getFishCount() {
		return this.fishCount;
	}
	
	public int getSharkCount() {
		return this.sharkCount;
	}

	public int getUpdNumberOfFish() {
		return updNumberOfFish;
	}

	public void setUpdNumberOfFish(int updNumberOfFish) {
		this.updNumberOfFish = updNumberOfFish;
	}

	public int getUpdNumberOfShark() {
		return updNumberOfShark;
	}

	public void setUpdNumberOfShark(int updNumberOfShark) {
		this.updNumberOfShark = updNumberOfShark;
	}
	
	public int getMaxCells() {
		return this.maxCells;
	}

	public int getUpdFishBreed() {
		return updFishBreed;
	}

	public void setUpdFishBreed(int updFishBreed) {
		this.updFishBreed = updFishBreed;
	}

	public int getUpdSharkBreed() {
		return updSharkBreed;
	}

	public void setUpdSharkBreed(int updSharkBreed) {
		this.updSharkBreed = updSharkBreed;
	}
	
	public void setUpdStarvationTime(int updStarvationTime) {
		this.updStarvationTime = updStarvationTime;
	}
	
	public int getUpdStarvationTime() {
		return this.updStarvationTime;
	}

	public boolean isCustomCellSize() {
		return customCellSize;
	}

	public void setCustomCellSize(boolean customCellSize) {
		this.customCellSize = customCellSize;
	}

	public int getUpdCellWidth() {
		return updCellWidth;
	}

	public void setUpdCellWidth(int updCellWidth) {
		this.updCellWidth = updCellWidth;
	}

	public int getUpdCellHeight() {
		return updCellHeight;
	}

	public void setUpdCellHeight(int updCellHeight) {
		this.updCellHeight = updCellHeight;
	}
}