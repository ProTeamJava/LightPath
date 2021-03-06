package me.kinomoto.proteam.history;

import me.kinomoto.proteam.Surroundings;
import me.kinomoto.proteam.elements.BeamSource;

/**
 *The class is creating a HistoryNode after rotating BeamSource on the working plain
 */
public class HistoryNodeRotationSource extends HistoryNodeRotationAbstract {
	private BeamSource bs;

	public HistoryNodeRotationSource(Surroundings s, BeamSource bs) {
		super(s);
		this.bs = bs;
	}

	@Override
	public void undo() {
		bs.rotate(-rotateBy);
		s.simulate();		
	}

	@Override
	public void redo() {
		bs.rotate(rotateBy);
		s.simulate();		
	}

}
