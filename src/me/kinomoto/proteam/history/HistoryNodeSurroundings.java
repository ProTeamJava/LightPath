package me.kinomoto.proteam.history;

import me.kinomoto.proteam.Surroundings;

public class HistoryNodeSurroundings extends HistoryNodeAbstract {
	private double ior;
	
	public HistoryNodeSurroundings(Surroundings s) {
		super(s);
		ior = s.getIor();
	}

	@Override
	public void undo() {
		double tmp = s.getIor();
		s.setIOR(ior);
		ior = tmp;
		s.simulate();
		s.updateSettingsPanel();
	}

	@Override
	public void redo() {
		double tmp = s.getIor();
		s.setIOR(ior);
		ior = tmp;
		s.simulate();
		s.updateSettingsPanel();
	}

}
