package me.kinomoto.proteam.elements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import me.kinomoto.proteam.Collision;
import me.kinomoto.proteam.MultipleCollisionsException;
import me.kinomoto.proteam.Surroundings;

/**
 * 
 * AbstractOplitalElement class is temporarily holding the prototype optical elements such: as triangular and square prism and flat mirror. It holds
 * an implementation of beam collision with the object detection
 * 
 * @param posistion
 *            is the point of reference to the optical element
 * @param vertices
 *            is the list of the vertex points of the optical element
 * 
 * @method findCollision is the algorithm of beam collision with the object detection
 * @method findCollisionPoint is to give exact point of collision
 *
 */
public abstract class AbstractOpticalElement {
	private static final int STROKE_WIDTH = 2;

	protected Point position;
	protected List<Point> vertices;

	public AbstractOpticalElement(Point position, List<Point> vertices) {
		this.position = position;
		this.vertices = vertices;
	}

	public AbstractOpticalElement(Point position) {
		this.position = position;
		this.vertices = new ArrayList<Point>();
	}

	public static List<Point> getTriangle() {
		List<Point> tmp = new ArrayList<Point>();
		tmp.add(new Point(0, 1));
		tmp.add(new Point(-.6, -.3));
		tmp.add(new Point(.6, -.3));
		tmp.add(new Point(0, 1));
		return tmp;
	}

	public abstract boolean isPointInside(Point p);

	public static List<Point> getMirror() {
		List<Point> tmp = new ArrayList<Point>();
		tmp.add(new Point(-10, -10));
		tmp.add(new Point(10, 10));
		return tmp;
	}

	public static List<Point> getSquare() {
		List<Point> tmp = new ArrayList<Point>();
		tmp.add(new Point(-20, -20));
		tmp.add(new Point(-20, 20));
		tmp.add(new Point(20, 20));
		tmp.add(new Point(20, -20));
		tmp.add(new Point(-20, -20));
		return tmp;
	}

	public Point get(int i) {
		return new Point(vertices.get(i).x + position.x, vertices.get(i).y + position.y);
	}

	/**
	 * 
	 * @return null - no collision
	 * @throws MultipleCollisionsException
	 *             if there is more than one collision
	 */
	public Collision collision(Segment s, Segment lastSegmentColision) throws MultipleCollisionsException {
		Collision tmp = null;

		for (int i = 0, j = 1; j < vertices.size(); i++, j++) {
			Segment checkingSegment = new Segment(get(i), get(j));

			if (lastSegmentColision != null && lastSegmentColision.equals(checkingSegment)) {
				// protection against multiple collisions with the same segment
				continue;
			}

			short d1 = direction(checkingSegment.begin, checkingSegment.end, s.begin);
			short d2 = direction(checkingSegment.begin, checkingSegment.end, s.end);
			short d3 = direction(s.begin, s.end, checkingSegment.begin);
			short d4 = direction(s.begin, s.end, checkingSegment.end);

			if (((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0)) && ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0))) {
				// collision
				if (tmp == null) {
					// 1st collision
					tmp = new Collision(findCollisionPoint(checkingSegment.begin, checkingSegment.end, s.begin, s.end), checkingSegment);
				} else {
					// next collision
					throw new MultipleCollisionsException();
				}
			}
		}

		return tmp;
	}

	private short direction(Point pi, Point pj, Point pk) {
		double tmp = (pk.x - pi.x) * (pj.y - pi.y) - (pj.x - pi.x) * (pk.y - pi.y);
		if (tmp > 0)
			return 1;
		if (tmp < 0)
			return -1;
		return 0;
	}

	private Point findCollisionPoint(Point p1, Point p2, Point p3, Point p4) {

		Point point = null;

		if (p1.x == p2.x) {
			double a2 = (p3.y - p4.y) / (p3.x - p4.x);
			double b2 = p3.y - a2 * p3.x;
			point = new Point(p1.x, p1.x * a2 + b2);

		} else if (p3.x == p4.x) {
			double a1 = (p1.y - p2.y) / (p1.x - p2.x);
			double b1 = p1.y - a1 * p1.x;
			point = new Point(p3.x, p3.x * a1 + b1);

		} else {

			double a1 = (p1.y - p2.y) / (p1.x - p2.x);
			double b1 = p1.y - a1 * p1.x;

			double a2 = (p3.y - p4.y) / (p3.x - p4.x);
			double b2 = p3.y - a2 * p3.x;

			double x = (b2 - b1) / (a1 - a2);
			point = new Point(x, x * a1 + b1);

		}
		return point;
	}

	abstract void findCollisionSolution(Surroundings s, Beam b, Segment seg);

	public void paint(Graphics2D g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(STROKE_WIDTH));
		for (int i = 0, j = 1; j < vertices.size(); i++, j++) {
			g2.drawLine((int) get(i).x, (int) get(i).y, (int) get(j).x, (int) get(j).y);
		}
	}

	public abstract JPanel getSettingsPanel(Surroundings s);

	public abstract void save(DataOutputStream os) throws IOException;

	protected void saveAbstract(DataOutputStream os) throws IOException {

	}

	public void moveBy(int x, int y) {
		position.x += x;
		position.y += y;
	}

}
