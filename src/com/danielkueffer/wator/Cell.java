package com.danielkueffer.wator;

import java.awt.Color;

public abstract class Cell {
	
	public void update(Planet pl, int x, int y) {
		internalUpdate(pl,x,y);
	}
	
	protected abstract void internalUpdate(Planet pl, int x, int y);
	
	protected abstract Color cellColor();
}