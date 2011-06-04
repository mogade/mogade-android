package com.mogade.android;

public enum ScoreScope {
	Daily(1),
	Weekly(2),
	Overall(3),
	Yesterday(4);
	
	private final int value;
	ScoreScope(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
