package me.kinomoto.proteam;

import java.util.ArrayList;
import java.util.List;

public class Surroundings {
	List<AbstractOpticalElement> elements;
	List<BeamSource> sources;
	List<Beam> beams;

	public Surroundings() {
		elements = new ArrayList<AbstractOpticalElement>();
		sources = new ArrayList<BeamSource>();
		beams = new ArrayList<Beam>();
	}

	public void simulate() {
		beams.clear();
		for (BeamSource source : sources) {
			System.out.println("source");
			add(source.getBeam());
		}

		while (!simulated()) {
			for (int ii = 0; ii < beams.size(); ii++) {
				Beam beam = beams.get(ii);
				if (!beam.getIfSimulated()) {
					System.out.println("beam no: " + String.valueOf(ii));
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
		System.out.println("new beam " + b.segment.begin.toString() + " " + b.segment.end.toString());
		beams.add(b);
	}

	private boolean simulated() {
		for (Beam beam : beams) {
			if (!beam.getIfSimulated())
				return false;
		}
		return true;
	}

}
