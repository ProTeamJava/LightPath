package me.kinomoto.proteam;

public class Mirror extends AbstractOpticalElement {

	public Mirror(Point position) {
		super(position, AbstractOpticalElement.getMirror());
	}

	@Override
	void findCollisionSolution(Surroundings s, Beam b, Segment seg) {
		//System.out.println("Collision with " + name);
		double sy = seg.end.x - seg.begin.x;
		double sx = seg.end.y - seg.begin.y;
		sy *= -1;
		double sl = sx * sx + sy * sy;
		double div = Math.sqrt(sl);
		sy /= div;
		sx /= div;
		sl = sx * sx + sy * sy;

		double dx = b.segment.end.x - b.segment.begin.x;
		double dy = b.segment.end.y - b.segment.begin.y;

		double dot = dx * sx + dy * sy;
		double w = 2 * dot / sl;

		double nx = dx - w * sx;
		double ny = dy - w * sy;

		Point end = new Point(b.segment.end.x + nx, b.segment.end.y + ny);

		if (b.brightness > 0.01) {
			double bright = b.brightness * .99;
			Segment tmp = new Segment(b.segment.end, end);
			if(tmp.end.x - tmp.begin.x == 0 && tmp.end.y - tmp.begin.y == 0) {
				System.out.println("err");
			}
			s.add(new Beam(tmp, b.wavelenght, bright, b.refractiveIndex));
		}
	}

	@Override
	public boolean isPointInside(Point p) {
		return false;
	}

}
