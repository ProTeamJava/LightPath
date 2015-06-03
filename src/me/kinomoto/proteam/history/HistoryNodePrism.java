package me.kinomoto.proteam.history;

import me.kinomoto.proteam.Surroundings;
import me.kinomoto.proteam.elements.Prism;

public class HistoryNodePrism  extends HistoryNodeAbstract{
	private double ior;
	
	public HistoryNodePrism(Prism p, Surroundings s) {
		super(p, s);
		ior = p.getIOR();
	}

	public void undo() {
		double tmp = p.getIOR();
		p.setIOR(ior);
		ior = tmp;
		s.simulate();
		s.updateSettingsPanel();
	}

	public void redo() {
		double tmp = p.getIOR();
		p.setIOR(ior);
		ior = tmp;
		s.simulate();
		s.updateSettingsPanel();
	}

}
