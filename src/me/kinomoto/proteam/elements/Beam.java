package me.kinomoto.proteam.elements;

import java.awt.Graphics2D;

import me.kinomoto.proteam.Collision;
import me.kinomoto.proteam.MultipleCollisionsException;
import me.kinomoto.proteam.Surroundings;

import com.mindprod.wavelength.Wavelength;

/**
 * Beam class is implementing the Beam of light creating methods as well as simulating its light path through different physical objects.
 *
 */
public class Beam {
	public static final double RED = 700;
	public static final double BLUE = 400;
	private static final int MAX_LENGTH = 4500;
	private static final double STEP_SIZE = 10.0 / MAX_LENGTH;
	public static final double MIN_BRIGHTNESS = .2;

	Segment segment;
	private boolean collisionChecked = false;
	double wavelenght;
	double brightness = 1;

	/**
	 * The constructor attributing segment coordinates, wavelength and brightness to the beam. 
	 * It is adjusting the beam length to the working plain dimensions. 
	 * @param segment
	 * @param wavelenght
	 * @param brightness
	 */
	public Beam(Segment segment, double wavelenght, double brightness) {
		double dx = segment.end.x - segment.begin.x;
		double dy = segment.end.y - segment.begin.y;
		double max = Math.abs(dx) < Math.abs(dy) ? Math.abs(dy) : Math.abs(dx);
		double times = MAX_LENGTH / max;
		segment.end.x = segment.begin.x + dx * times;
		segment.end.y = segment.begin.y + dy * times;

		this.segment = segment;
		this.wavelenght = wavelenght;
		this.brightness = brightness;
	}

	/**
	 * @return collisionChecked Boolean indicating whether the simulation of collisions on the light path of the beam took place
	 */
	public boolean getIfSimulated() {
		return collisionChecked;
	}

	/**
	 * The method simulating its  light path through different physical objects.
	 * The method is checking the collisions with optical objects and is adjusting the length to the bound of different media.
	 * 
	 * The methodology of checking collision: 
	 * A temporary segment is being elongated step by step.
	 * During the every elongation there is collision between beam and every {@link AbstractOpticalElement} on the plain being detected.
	 * If the segment is elongated too much and multiple collision was detected, program throws the exception and shorten the temporary segment. 
	 * When only one collision is detected, the point of collision is calculated by external functions and attributed to the end of the segment beam. 
	 * There is also collision solution calculated by {@link findCollisionSolution} method.
	 * 
	 * @param s Surroundings provides the list of the optical elements on the plain
	 */
	public void simulate(Surroundings s) {
		int collisionNum = 0;
		Collision collision = null;
		AbstractOpticalElement collisionElement = null;
		double start = 0.001;
		double end = 1;
		for (int nn = 0;nn < 1e4;nn++) {
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
					Collision p = e.collision(tmp);
					if (p != null) {
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
				collisionElement.findCollisionSolution(s, this, collision.getSegment());
				break;
			} else if (collisionNum == 0 && end == 1) {
				break;
			} else if (collisionNum == 0) {
				start = end;
				end += step;
			} else {
				end = (end - start) / 1.7;
				if (step == 1)
					step = STEP_SIZE;
				else
					step /= 1.7;
			}
			collisionNum = 0;
		}

		collisionChecked = true;
	}

	/**
	 * Method implementing graphical representation of the beam.
	 * It uses external library changing the wavelength in nanometers an brightness into color.
	 * @param g Graphics2D object 
	 */
	public void paint(Graphics2D g) {
		g.setColor(Wavelength.wvColor((float) wavelenght, (float) brightness));
		g.drawLine((int) segment.begin.x, (int) segment.begin.y, (int) segment.end.x, (int) segment.end.y);
	}

}
