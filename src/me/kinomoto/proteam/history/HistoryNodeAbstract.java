package me.kinomoto.proteam.history;

import me.kinomoto.proteam.Surroundings;
import me.kinomoto.proteam.elements.Mirror;
import me.kinomoto.proteam.elements.Prism;

public abstract class HistoryNodeAbstract {
	protected Surroundings s;
	protected Mirror m;
	protected Prism p;
	
	public HistoryNodeAbstract(Surroundings s) {
		this.s = s;
	}
	
	public HistoryNodeAbstract(Mirror m, Surroundings s) {
		this.m = m;
		this.s = s;
	}
	
	public HistoryNodeAbstract(Prism p, Surroundings s) {
		this.p = p;
		this.s = s;
	}
	
	public abstract void undo();
	public abstract void redo();
}
