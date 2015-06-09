package me.kinomoto.proteam.history;

import me.kinomoto.proteam.Surroundings;
import me.kinomoto.proteam.elements.Prism;

/**
 *The class is creating a HistoryNode after changing parameters of the Prism
 */
public class HistoryNodePrism  extends HistoryNodeAbstract{
	private double ior;
	private Prism p;
	
	public HistoryNodePrism(Prism p, Surroundings s) {
		super(s);
		this.p = p;
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
