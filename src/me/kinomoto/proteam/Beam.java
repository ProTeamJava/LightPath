package me.kinomoto.proteam;

public class Beam {
	Segment segment;
	private boolean collisionChecked = false;
	double wavelenght;
	double brightness = 1;

	public Beam(Segment segment, double wavelenght, double lightness) {
		double dx = segment.end.x - segment.begin.x;
		double dy = segment.end.y - segment.begin.y;
		double max = dx < dy ? dy : dx;
		double times = 3000.0 / max;
		segment.end.x = segment.begin.x + dx * times;
		segment.end.y = segment.begin.y + dy * times;

		this.segment = segment;
		this.wavelenght = wavelenght;
	}

	public boolean getIfSimulated() {
		return collisionChecked;
	}

	public void simulate(Surroundings s) {
		int collisionNum = 0;
		Collision collision = null;
		AbstractOpticalElement collisionElement = null;
		double start = 0;
		double end = 1;
		for (;;) {
			double dx, dy;
			dx = segment.end.x - segment.begin.x;
			dy = segment.end.y - segment.begin.y;
			double nex = segment.begin.x + end * dx;
			double ney = segment.begin.y + end * dy;

			double nbx = segment.begin.x + start * dx;
			double nby = segment.begin.y + start * dy;

			Segment tmp = new Segment(new Point(nbx, nby), new Point(nex, ney));
			for (AbstractOpticalElement e : s.elements) {

				try {
					Collision p = e.collision(tmp);
					if (p == null) {
					} else {
						collisionElement = e;
						collision = p;
						collisionNum++;
					}
				} catch (MultipleCollisionsException ex) {
					collisionNum += 2;
					break;
				}
			}

			if (collisionNum == 1) {
				segment.end = collision.point;
				collisionElement.collisionSolution(s, this, collision.segment);
				System.out.println(collision.point);
				break;
			} else if (collisionNum == 0 && end == 1) {
				System.out.println("no collision");
				break;
			} else if (collisionNum == 0) {
				start = end;
				end = 1;
			} else {
				end = (end - start) / 2.0;
			}
			collisionNum = 0;
		}

		collisionChecked = true;
	}

}
