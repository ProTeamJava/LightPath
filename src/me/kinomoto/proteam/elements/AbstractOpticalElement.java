package me.kinomoto.proteam.elements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JPanel;

import me.kinomoto.proteam.Collision;
import me.kinomoto.proteam.MultipleCollisionsException;
import me.kinomoto.proteam.Surroundings;
import me.kinomoto.proteam.Surroundings.PointPosition;

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
	private double selectionAngle = 0;

	int l = 0;
	int r = 0;
	int t = 0;
	int b = 0;

	public AbstractOpticalElement(Point position, List<Point> vertices) {
		this.position = position;
		this.vertices = vertices;
		calcBounds();
	}

	public AbstractOpticalElement(Point position) {
		this.position = position;
		this.vertices = new ArrayList<Point>();
	}

	public static List<Point> getTriangle() {
		List<Point> tmp = new ArrayList<Point>();
		tmp.add(new Point(0, 40));
		tmp.add(new Point(-50, -12));
		tmp.add(new Point(50, -12));
		tmp.add(new Point(0, 40));
		return tmp;
	}

	public abstract boolean isPointInside(Point p);

	public Surroundings.PointPosition isPointInsideSelected(Point p) {
		int nx = (int) (p.x - position.x);
		int ny = (int) (p.y - position.y);

		if ((nx > l - 4 && nx < l + 7 && ny > t - 4 && ny < t + 7) || (nx > r - 7 && nx < r + 4 && ny > t - 4 && ny < t + 7) || (nx > r - 4 && nx < r + 7 && ny > b - 7 && ny < b + 4) || (nx > l - 7 && nx < l + 4 && ny > b - 7 && ny < b + 4))
			return PointPosition.POINT_ROTATE;

		if (nx >= l && nx <= r && ny >= t && ny <= b)
			return PointPosition.POINT_INSIDE;
		return PointPosition.POINT_OUTSIDE;
	}

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
	 * @return <code>null</code> - no collision
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
		Graphics2D p = (Graphics2D) g.create();
		p.setColor(Color.BLACK);
		p.setStroke(new BasicStroke(STROKE_WIDTH));

		p.translate(position.x, position.y);

		for (int i = 0, j = 1; j < vertices.size(); i++, j++) {
			p.drawLine((int) vertices.get(i).x, (int) vertices.get(i).y, (int) vertices.get(j).x, (int) vertices.get(j).y);
		}
	}

	public void paintSelection(Graphics2D g) {
		Graphics2D p = (Graphics2D) g.create();
		p.translate(position.x, position.y);
		p.rotate(selectionAngle);
		p.setStroke(new BasicStroke(1.0f, // Width
				BasicStroke.CAP_SQUARE, // End cap
				BasicStroke.JOIN_BEVEL, // Join style
				10.0f, // Miter limit
				new float[] { 2.0f, 2.0f }, // Dash pattern
				0.0f)); // Dash phase
		p.setColor(Color.BLACK);
		p.drawRect((int) l, (int) t, (int) (r - l), (int) (b - t));
		p.fillRect(l - 2, t - 2, 4, 4);
		p.fillRect(l - 2, b - 2, 4, 4);
		p.fillRect(r - 2, t - 2, 4, 4);
		p.fillRect(r - 2, b - 2, 4, 4);
	}

	public abstract JPanel getSettingsPanel(Surroundings s);

	public abstract void save(DataOutputStream os) throws IOException;

	protected void saveAbstract(DataOutputStream os) throws IOException {
		// TODO
	}

	public void moveBy(int x, int y) {
		position.x += x;
		position.y += y;
	}

	private void calcBounds() {
		r = (int) (Collections.min(vertices, new Comparator<Point>() {

			@Override
			public int compare(Point p1, Point p2) {
				return (int) ((p2.x - p1.x) * 1000);
			}

		}).x + 10);
		l = (int) (Collections.max(vertices, new Comparator<Point>() {

			@Override
			public int compare(Point p1, Point p2) {
				return (int) ((p2.x - p1.x) * 1000);
			}

		}).x - 10);
		b = (int) (Collections.min(vertices, new Comparator<Point>() {

			@Override
			public int compare(Point p1, Point p2) {
				return (int) ((p2.y - p1.y) * 1000);
			}

		}).y + 10);
		t = (int) (Collections.max(vertices, new Comparator<Point>() {

			@Override
			public int compare(Point p1, Point p2) {
				return (int) ((p2.y - p1.y) * 1000);
			}

		}).y - 10);
	}

	/**
	 * Used by {@link Surroundings#getSelectedAngle(Point)}
	 * 
	 * @return
	 */
	public double getAngle(Point p) {
		return Math.atan2(p.y - position.y, p.x - position.x);
	}

	public void rotate(double da) {
		selectionAngle += da;
		double sin = Math.sin(da);
		double cos = Math.cos(da);
		for(Point p : vertices) {
			double y = p.x * sin + p.y * cos;
			double x = p.x * cos - p.y * sin;
			p.x = x;
			p.y = y;
		}
	}
	
	public void addAngle() {
		selectionAngle = 0;
		calcBounds();
	}
}
