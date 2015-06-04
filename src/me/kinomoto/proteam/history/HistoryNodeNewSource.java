package me.kinomoto.proteam.history;

import me.kinomoto.proteam.Surroundings;
import me.kinomoto.proteam.elements.BeamSource;

public class HistoryNodeNewSource extends HistoryNodeAbstract {
	BeamSource bs;

	public HistoryNodeNewSource(Surroundings s, BeamSource bs) {
		super(s);
		this.bs = bs;
	}

	@Override
	public void undo() {
		s.deleteSource(bs);
	}

	@Override
	public void redo() {
		s.add(bs);
	}

}
