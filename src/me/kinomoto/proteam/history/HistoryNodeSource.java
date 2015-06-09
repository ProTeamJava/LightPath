package me.kinomoto.proteam.history;

import me.kinomoto.proteam.Surroundings;
import me.kinomoto.proteam.elements.BeamSource;

/**
 *The class is creating a HistoryNode after changing parameters of the BeamSource
 */
public class HistoryNodeSource extends HistoryNodeAbstract {
	BeamSource bs;
	double wave;

	public HistoryNodeSource(Surroundings s, BeamSource bs) {
		super(s);
		this.bs = bs;
		wave = bs.getWavelength();
	}

	@Override
	public void undo() {
		double tmp = bs.getWavelength();
		bs.setWavelength(wave);
		wave = tmp;
		s.simulate();
	}

	@Override
	public void redo() {
		double tmp = bs.getWavelength();
		bs.setWavelength(wave);
		wave = tmp;
		s.simulate();
	}

}
