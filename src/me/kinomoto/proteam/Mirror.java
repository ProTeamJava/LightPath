package me.kinomoto.proteam;

public class Mirror extends AbstractOpticalElement {

	public Mirror(Point position) {
		super(position, AbstractOpticalElement.getMirror());
	}

	@Override
	void collisionSolution(Surroundings s, Beam b) {
		Point end = new Point(-b.segment.begin.x, b.segment.begin.y);
		s.add(new Beam(new Segment(b.segment.end, end), b.wavelenght));
	}

}
