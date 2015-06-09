package me.kinomoto.proteam.history;

import me.kinomoto.proteam.Surroundings;
import me.kinomoto.proteam.elements.BeamSource;

/**
 *The class is creating a HistoryNode after translating BeamSource on the working plain
 */
public class HistoryNodeMoveSource extends HistoryNodeMoveAbstract {
	BeamSource bs;

	public HistoryNodeMoveSource(Surroundings s, BeamSource bs) {
		super(s);
		this.bs = bs;
	}

	@Override
	public void undo() {
		bs.moveBy((int)-moveBy.x, (int)-moveBy.y);
		s.simulate();
	}

	@Override
	public void redo() {
		bs.moveBy((int)moveBy.x, (int)moveBy.y);
		s.simulate();
	}

}