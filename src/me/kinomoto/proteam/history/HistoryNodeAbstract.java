package me.kinomoto.proteam.history;

import me.kinomoto.proteam.Surroundings;

public abstract class HistoryNodeAbstract {
	protected Surroundings s;
	
	public HistoryNodeAbstract(Surroundings s) {
		this.s = s;
		s.setModyfied(true);
	}
	
	public abstract void undo();
	public abstract void redo();
	public boolean isEmpty() {
		return false;
	}
}
