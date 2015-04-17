package me.kinomoto.proteam;

public abstract class HistoryNodeAbstract {
	protected Surroundings s;
	
	public HistoryNodeAbstract(Surroundings s) {
		this.s = s;
	}
	public abstract void undo();
	public abstract void redo();
}
