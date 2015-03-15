package me.kinomoto.proteam;

public class Mirror extends AbstractOpticalElement {

	public Mirror(Point position) {
		super(position, AbstractOpticalElement.getMirror());
	}

	@Override
	void collisionSolution(Surroundings s, Beam b, Segment seg) {
		double sy = seg.end.x - seg.begin.x;
		double sx = seg.end.y - seg.begin.y;
		sy *= -1;
		double sl = sx * sx + sy * sy;

		double dx = b.segment.end.x - b.segment.begin.x;
		double dy = b.segment.end.y - b.segment.begin.y;

		double dot = dx * sx + dy * sy;
		double w = 2 * dot / sl;
		
		double nx = dx - w * sx;
		double ny = dy - w * sy;

		Point end = new Point(b.segment.end.x + nx, b.segment.end.y + ny);

		s.add(new Beam(new Segment(b.segment.end, end), b.wavelenght, 1));
	}

}
