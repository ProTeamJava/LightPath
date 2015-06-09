package me.kinomoto.proteam.history;

import me.kinomoto.proteam.Surroundings;
import me.kinomoto.proteam.elements.Mirror;

/**
 *The class is creating a HistoryNode after changing parameters of the Mirror
 */
public class HistoryNodeMirror extends HistoryNodeAbstract {
	private double absorption = .99;
	private Mirror m;
	
	public HistoryNodeMirror(Mirror m, Surroundings s) {
		super(s);
		this.m = m;
		absorption = m.getAbsorption();
	}

	@Override
	public void undo() {
		double tmp = m.getAbsorption();
		m.setAbsorption(absorption);
		absorption = tmp;
		s.simulate();
		s.updateSettingsPanel();
	}

	@Override
	public void redo() {
		double tmp = m.getAbsorption();
		m.setAbsorption(absorption);
		absorption = tmp;
		s.simulate();
		s.updateSettingsPanel();
	}

}
