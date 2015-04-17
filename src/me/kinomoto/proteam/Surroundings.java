package me.kinomoto.proteam;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class Surroundings {
	List<AbstractOpticalElement> elements;
	List<BeamSource> sources;
	private List<Beam> beams;
	double ior = 4;

	
	private enum SelectionType {SELECTED_BEAM_SOURCE, SELECTED_ELEMENT, NOTHING};
	private SelectionType selection = SelectionType.NOTHING;
	private BeamSource selectedBeamSource = null;
	private AbstractOpticalElement selectedElement = null;

	public Surroundings() {
		elements = new ArrayList<AbstractOpticalElement>();
		sources = new ArrayList<BeamSource>();
		beams = new ArrayList<Beam>();
	}

	public void simulate() {
		beams.clear();
		for (BeamSource source : sources) {
			double ior = 1;
			for (AbstractOpticalElement element : elements)
				if (element.isPointInside(source.segment.begin))
					ior = ((Prism) element).getRefractiveIndex();
			add(source.getBeam(ior));
		}

		while (!simulated()) {
			for (int ii = 0; ii < beams.size(); ii++) {
				Beam beam = beams.get(ii);
				if (!beam.getIfSimulated()) {
					beam.simulate(this);
				}
			}
		}

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
		selection = SelectionType.NOTHING;
		selectedBeamSource = null;
		selectedElement = null;
		
		for (BeamSource beamSource : sources) {
			if(beamSource.isPointInside(p)) {
				selection = SelectionType.SELECTED_BEAM_SOURCE;
				selectedBeamSource = beamSource;
				System.out.println("Selected source");
				return;
			}
		}
		
		for (AbstractOpticalElement element : elements) {
			if(element.isPointInside(p)) {
				selection = SelectionType.SELECTED_ELEMENT;
				selectedElement = element;
				System.out.println("Selected element");
				return;
			}
		}
	}

}
