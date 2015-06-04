package me.kinomoto.proteam.history;

import me.kinomoto.proteam.Surroundings;

public abstract class HistoryNodeRotationAbstract extends HistoryNodeAbstract {
	protected double rotateBy = 0;

	public HistoryNodeRotationAbstract(Surroundings s) {
		super(s);
	}
	
	public void rotateBy(double da) {
		rotateBy += da;
	}

}
