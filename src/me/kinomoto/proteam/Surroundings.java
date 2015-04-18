package me.kinomoto.proteam;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class Surroundings {
	List<AbstractOpticalElement> elements;
	private List<BeamSource> sources;
	private List<Beam> beams;

	double ior = 1;
	
	private SurroundingsView view;

	private enum SelectionType {
		SELECTED_BEAM_SOURCE, SELECTED_ELEMENT, SURROUNDINGS
	};

	private SelectionType selection = SelectionType.SURROUNDINGS;
	private BeamSource selectedBeamSource = null;
	private AbstractOpticalElement selectedElement = null;

	public Surroundings(SurroundingsView view) {
		elements = new ArrayList<AbstractOpticalElement>();
		sources = new ArrayList<BeamSource>();
		beams = new ArrayList<Beam>();
		this.view = view;
	}

	public void simulate() {
		beams.clear();
		for (BeamSource source : sources) {
			add(source.getBeam());
		}

		while (!simulated()) {
			for (int ii = 0; ii < beams.size(); ii++) {
				Beam beam = beams.get(ii);
				if (!beam.getIfSimulated()) {
					beam.simulate(this);
				}
			}
		}
		
		view.repaint();

	}

	public void add(AbstractOpticalElement e) {
		elements.add(e);
		simulate();
	}

	public void add(BeamSource s) {
		sources.add(s);
		simulate();
	}

	public void add(Beam b) {
		beams.add(b);
	}

	private boolean simulated() {
		for (Beam beam : beams) {
			if (!beam.getIfSimulated())
				return false;
		}
		return true;
	}

	public void paint(Graphics2D g) {

		for (Beam beam : beams) {
			beam.paint(g);
		}

		for (BeamSource beamSource : sources) {
			beamSource.paint(g);
		}

		for (AbstractOpticalElement abstractOpticalElement : elements) {
			abstractOpticalElement.paint(g);
		}
	}

	public void mousePressed(Point p) {
		selection = SelectionType.SURROUNDINGS;
		selectedBeamSource = null;
		selectedElement = null;

		for (BeamSource beamSource : sources) {
			if (beamSource.isPointInside(p)) {
				selection = SelectionType.SELECTED_BEAM_SOURCE;
				selectedBeamSource = beamSource;
				return;
			}
		}

		for (AbstractOpticalElement element : elements) {
			if (element.isPointInside(p)) {
				selection = SelectionType.SELECTED_ELEMENT;
				selectedElement = element;
				return;
			}
		}
	}

	public JPanel getSelectedSettingsPanel() {
		switch (selection) {
		case SURROUNDINGS:
			return new SurroundingsSettingsPanel(this);
		case SELECTED_BEAM_SOURCE:
			return new BeamSourceSettingsPanel(selectedBeamSource, this);
		case SELECTED_ELEMENT:
			return selectedElement.getSettingsPanel(this);
		default:
			return null;
		}
	}
	
	public void setIOR(double ior) {
		this.ior = ior;
		this.simulate();
	}

	public double getIOR() {
		return ior;
	}
	
	public void updateSettingsPanel() {
		view.settingsPanel.setPanel(getSelectedSettingsPanel());
	}

}
