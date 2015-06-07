package me.kinomoto.proteam.elements;

import java.awt.Graphics2D;
import java.io.Serializable;

import me.kinomoto.proteam.Collision;
import me.kinomoto.proteam.MultipleCollisionsException;
import me.kinomoto.proteam.Surroundings;

import com.mindprod.wavelength.Wavelength;

public class Beam  implements Serializable{
	private static final long serialVersionUID = 3031149333626031030L;
	
	public static final double RED = 700;
	public static final double BLUE = 400;
	private static final int MAX_LENGTH = 4500;
	private static final double STEP_SIZE = 200.0 / MAX_LENGTH;
	public static final double MIN_BRIGHTNESS = .2;

	Segment segment;
	private boolean collisionChecked = false;
	double wavelenght;
	double brightness = 1;

	private Line lastColision;

	public Beam(Segment segment, double wavelenght, double lightness, Line line) {
		lastColision = line;
		double dx = segment.end.x - segment.begin.x;
		double dy = segment.end.y - segment.begin.y;
		double max = Math.abs(dx) < Math.abs(dy) ? Math.abs(dy) : Math.abs(dx);
		double times = MAX_LENGTH / max;
		segment.end.x = segment.begin.x + dx * times;
		segment.end.y = segment.begin.y + dy * times;

		this.segment = segment;
		this.wavelenght = wavelenght;
		this.brightness = lightness;
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

			double step = 1;

			Segment tmp = new Segment(new Point(nbx, nby), new Point(nex, ney));
			for (AbstractOpticalElement e : s.getElements()) {
				try {
					Collision p = e.collision(tmp, lastColision);
					if (p != null && (collision == null || !collision.getLine().isEqual(p.getLine()))) {
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
				segment.end = collision.getPoint();
				collisionElement.findCollisionSolution(s, this, collision.getSegment(), collision.getLine());
				break;
			} else if (collisionNum == 0 && end == 1) {
				break;
			} else if (collisionNum == 0) {
				start = end;
				end += step;
			} else {
				end = (end - start) / 1.5;
				if (step == 1)
					step = STEP_SIZE;
				else
					step /= 1.5;
			}
			collisionNum = 0;
		}

		collisionChecked = true;
	}

	public void paint(Graphics2D g) {
		g.setColor(Wavelength.wvColor((float) wavelenght, (float) brightness));
		g.drawLine((int) segment.begin.x, (int) segment.begin.y, (int) segment.end.x, (int) segment.end.y);
	}

}
