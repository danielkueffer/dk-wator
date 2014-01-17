package com.danielkueffer.wator;

public enum CellType {
	OCEAN(1),
	FISH(2),
	SHARK(3);
	
	private final int value;
	
	private CellType(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}