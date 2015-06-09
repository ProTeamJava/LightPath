package me.kinomoto.proteam.history;

import me.kinomoto.proteam.Surroundings;


/**
 * The class is the abstract prototype for creating HistoryNodes for rotations in the Surroundings
 */
public abstract class HistoryNodeRotationAbstract extends HistoryNodeAbstract {
	protected double rotateBy = 0;

	public HistoryNodeRotationAbstract(Surroundings s) {
		super(s);
	}
	
	public void rotateBy(double da) {
		rotateBy += da;
	}
	
	@Override
	public boolean isEmpty() {
		return rotateBy == 0;
	}

}
