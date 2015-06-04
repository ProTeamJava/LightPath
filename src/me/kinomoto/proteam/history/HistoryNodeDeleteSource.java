package me.kinomoto.proteam.history;

import me.kinomoto.proteam.Surroundings;
import me.kinomoto.proteam.elements.BeamSource;

public class HistoryNodeDeleteSource extends HistoryNodeAbstract {
	BeamSource bs;

	public HistoryNodeDeleteSource(Surroundings s, BeamSource bs) {
		super(s);
		this.bs = bs;
	}

	@Override
	public void undo() {
		s.add(bs);
	}

	@Override
	public void redo() {
		s.deleteSource(bs);
	}

}
