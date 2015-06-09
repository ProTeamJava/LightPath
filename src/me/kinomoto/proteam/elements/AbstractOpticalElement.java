package me.kinomoto.proteam.elements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import me.kinomoto.proteam.Collision;
import me.kinomoto.proteam.LoadException;
import me.kinomoto.proteam.MultipleCollisionsException;
import me.kinomoto.proteam.Surroundings;
import me.kinomoto.proteam.Surroundings.PointPosition;

/**
 * 
 * AbstractOplitalElement abstract class is representing general properties of Mirror and Prism
 * an implementation of beam collision with the object detection
 *
 */
public abstract class AbstractOpticalElement {
	private static final int STROKE_WIDTH = 2;
	private static final int DOT_SIZE = 4;
	private static final int HALF_DOT_SIZE = DOT_SIZE / 2;
	private static final int BOUND_SIZE = 10;
	private static final int SQUARE_LENGTH = 20;
	private static final int MIRROR_SIZE = 10;

	private static final int ROTATION_POINT_PLUS = 7;
	private static final int ROTATION_POINT_MINUS = 4;

	private static final double TRIANGLE_LENGHT = 70;
	private static final double TRIANGLE_HALF_LENGHT = TRIANGLE_LENGHT / 2;
	private static final double TRIANGLE_HEIGHT = TRIANGLE_LENGHT * Math.sqrt(3) / 2;

	protected Point position;
	protected List<Point> vertices;
	private double selectionAngle = 0;

	private int l = 0;
	private int r = 0;
	private int t = 0;
	private int b = 0;

	protected boolean rotationRight = true;

	/** The constructor of abstract optical object
	 * @param position is the point of reference to the optical element         
	 * @param vertices is the list of the vertex points of the optical element
	 */
	public AbstractOpticalElement(Point position, List<Point> vertices) {
		this.position = position;
		this.vertices = vertices;
		calcBounds();
	}

	/** The constructor of abstract optical object
	 * @param position is the point of reference to the optical element         
	 * @param vertices is the list of the vertex points of the optical element that is not determined yet
	 */
	public AbstractOpticalElement(Point position) {
		this.position = position;
		this.vertices = new ArrayList<Point>();
	}

	public static List<Point> getTriangle() {
		List<Point> tmp = new ArrayList<Point>();
		tmp.add(new Point(-TRIANGLE_HALF_LENGHT, 0));
		tmp.add(new Point(TRIANGLE_HALF_LENGHT, 0));
		tmp.add(new Point(0, -TRIANGLE_HEIGHT));
		tmp.add(new Point(-TRIANGLE_HALF_LENGHT, 0));
		return tmp;
	}

	/**
	 * The method checking whether the given Point is inside the object
	 * @param p given Point
	 * @return boolean
	 */
	public abstract boolean isPointInside(Point p);

	/**
	 * The method checking whether the given Point is inside the object so that the optical can be selected on the working plain
	 * @param p given Point
	 * @return PointPosition whether outside or inside
	 */
	public Surroundings.PointPosition isPointInsideSelected(Point p) {
		int nx = (int) (p.x - position.x);
		int ny = (int) (p.y - position.y);

		if ((nx > l - ROTATION_POINT_MINUS && nx < l + ROTATION_POINT_PLUS && ny > t - ROTATION_POINT_MINUS && ny < t + ROTATION_POINT_PLUS) || (nx > r - ROTATION_POINT_PLUS && nx < r + ROTATION_POINT_MINUS && ny > t - ROTATION_POINT_MINUS && ny < t + ROTATION_POINT_PLUS)
				|| (nx > r - ROTATION_POINT_MINUS && nx < r + ROTATION_POINT_PLUS && ny > b - ROTATION_POINT_PLUS && ny < b + ROTATION_POINT_MINUS) || (nx > l - ROTATION_POINT_PLUS && nx < l + ROTATION_POINT_MINUS && ny > b - ROTATION_POINT_PLUS && ny < b + ROTATION_POINT_MINUS))
			return PointPosition.POINT_ROTATE;

		if (nx >= l && nx <= r && ny >= t && ny <= b)
			return PointPosition.POINT_INSIDE;
		return PointPosition.POINT_OUTSIDE;
	}

	public static List<Point> getMirror() {
		List<Point> tmp = new ArrayList<Point>();
		tmp.add(new Point(MIRROR_SIZE, -MIRROR_SIZE));
		tmp.add(new Point(-MIRROR_SIZE, MIRROR_SIZE));
		return tmp;
	}

	public static List<Point> getSquare() {
		List<Point> tmp = new ArrayList<Point>();
		tmp.add(new Point(-SQUARE_LENGTH, -SQUARE_LENGTH));
		tmp.add(new Point(-SQUARE_LENGTH, SQUARE_LENGTH));
		tmp.add(new Point(SQUARE_LENGTH, SQUARE_LENGTH));
		tmp.add(new Point(SQUARE_LENGTH, -SQUARE_LENGTH));
		tmp.add(new Point(-SQUARE_LENGTH, -SQUARE_LENGTH));
		return tmp;
	}

	public Point get(int i) {
		return new Point(vertices.get(i).x + position.x, vertices.get(i).y + position.y);
	}

	private boolean checkSegmentCollision(Point a1, Point a2, Point b1, Point b2) {
		short d1 = direction(a1, a2, b1);
		short d2 = direction(a1, a2, b2);
		short d3 = direction(b1, b2, a1);
		short d4 = direction(b1, b2, a2);
		return ((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0)) && ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0));
	}

	/**
	 * The method is detecting collision with the incident Beam segment
	 * @param s Segment of the incident Beam
	 * @return <code>null</code> - no collision
	 * @throws MultipleCollisionsException
	 *             if there are multiple collisions
	 */
	public Collision collision(Segment s) throws MultipleCollisionsException {
		Collision tmp = null;

		for (int i = 0, j = 1; j < vertices.size(); i++, j++) {
			Segment checkingSegment = new Segment(get(i), get(j));
			
			if (checkSegmentCollision(checkingSegment.begin, checkingSegment.end, s.begin, s.end)) {
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

	/**
	 * The method is checking whether normal line is to inside or to outside of the object
	 * @param pi
	 * @param pj
	 * @param pk
	 * @return
	 */
	private short direction(Point pi, Point pj, Point pk) {
		double tmp = (pk.x - pi.x) * (pj.y - pi.y) - (pj.x - pi.x) * (pk.y - pi.y);
		if (tmp > 0)
			return 1;
		if (tmp < 0)
			return -1;
		return 0;
	}

	/**
	 * The method is to give exact point of collision, by calculating the common Point of two segments
	 * @param p1 beginning {@link Point} of the first {@link Segment}
	 * @param p2 ending {@link Point} of the first {@link Segment}
	 * @param p3 beginning {@link Point} of the second{@link Segment}
	 * @param p4 ending {@link Point} of the second{@link Segment}
	 * @return {@link Point}
	 */
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

	/**
	 * The method is the algorithm of beam collision with the object outcome
	 * In case of the Mirror it is reflection, In case of the Prism refraction algorithm.
	 * @param s Surroundings is where objects are located
	 * @param b Beam
	 * @param seg Segment is the side of the optical object
	 */
	abstract void findCollisionSolution(Surroundings s, Beam b, Segment seg);

	public void paint(Graphics2D g) {
		paint(g, Color.BLACK);
	}


	/**
	 * Method implementing graphical representation of the optical element.
	 * @param g Graphics2D object 
	 */
	public void paint(Graphics2D g, Color c) {
		Graphics2D p = (Graphics2D) g.create();
		p.setColor(c);
		p.setStroke(new BasicStroke(STROKE_WIDTH));

		p.translate(position.x, position.y);

		for (int i = 0, j = 1; j < vertices.size(); i++, j++) {
			p.drawLine((int) vertices.get(i).x, (int) vertices.get(i).y, (int) vertices.get(j).x, (int) vertices.get(j).y);
		}
	}

	/**
	 * Method implementing graphical representation of the optical element selection that is shown when the object is marked on the plain.
	 * @param g Graphics2D object 
	 */
	public void paintSelection(Graphics2D g) {
		Graphics2D p = (Graphics2D) g.create();
		p.translate(position.x, position.y);
		p.rotate(selectionAngle);
		p.setStroke(Surroundings.selectionStroke);
		p.setColor(Color.BLACK);
		p.drawRect((int) l, (int) t, (int) (r - l), (int) (b - t));
		p.fillRect(l - HALF_DOT_SIZE, t - HALF_DOT_SIZE, DOT_SIZE, DOT_SIZE);
		p.fillRect(l - HALF_DOT_SIZE, b - HALF_DOT_SIZE, DOT_SIZE, DOT_SIZE);
		p.fillRect(r - HALF_DOT_SIZE, t - HALF_DOT_SIZE, DOT_SIZE, DOT_SIZE);
		p.fillRect(r - HALF_DOT_SIZE, b - HALF_DOT_SIZE, DOT_SIZE, DOT_SIZE);
	}

	/**
	 * The method is to show the properties of the optical element the right SettingsPanel when the object is selected
	 * @param s Surroundings
	 * @return JPanel on the right SettingsPanel
	 */
	public abstract JPanel getSettingsPanel(Surroundings s);

	public abstract void save(DataOutputStream os) throws IOException;

	/**
	 * The method is saving to the file the optical element general parameters: the number of vertices and vertices positions
	 * @param os DataOutputStream
	 * @throws IOException the exception served while problems DataOutputSteam
	 */
	protected void saveAbstract(DataOutputStream os) throws IOException {
		position.save(os);

		os.writeInt(vertices.size());
		for (Point p : vertices) {
			p.save(os);
		}
	}

	/**
	 * The method creating the optical object while reading the input file it distinguish between Mirror and Prism by magic number
	 * @param is the DataInputStream
	 * @return
	 * @throws IOException
	 * @throws LoadException
	 */
	public static AbstractOpticalElement load(DataInputStream is) throws IOException, LoadException {
		int magicNum = is.readInt();
		Point position = new Point(is);
		int vertSize = is.readInt();
		List<Point> verts = new ArrayList<Point>();
		for (int i = 0; i < vertSize; i++) {
			verts.add(new Point(is));
		}

		if (magicNum == Mirror.MAGIC_NUMBER) {
			return new Mirror(position, verts, is);
		} else if (magicNum == Prism.MAGIC_NUMBER) {
			return new Prism(position, verts, is);
		} else {
			throw new LoadException(me.kinomoto.proteam.Messages.get("fileCorupt"));
		}
	}

	/**
	 * The method is changing the geometric center of the figure after translation
	 * @param dx 
	 * @param dy
	 */
	public void moveBy(int dx, int dy) {
		position.x += dx;
		position.y += dy;
	}

	/**
	 * The method calculating the best adjustment of the bounds of the selected objects
	 */
	protected void calcBounds() {
		r = (int) (Collections.min(vertices, Point.xComparator).x + BOUND_SIZE);
		l = (int) (Collections.max(vertices, Point.xComparator).x - BOUND_SIZE);
		b = (int) (Collections.min(vertices, Point.yComparator).y + BOUND_SIZE);
		t = (int) (Collections.max(vertices, Point.yComparator).y - BOUND_SIZE);
	}

	/**
	 * Used by {@link Surroundings#getSelectedAngle(Point)}
	 * 
	 * @return
	 */
	public double getAngle(Point p) {
		return Math.atan2(p.y - position.y, p.x - position.x);
	}

	/**
	 * The method changing vertices positions coordinates after rotation
	 * @param da the difference angle after rotation
	 */
	public void rotate(double da) {
		selectionAngle += da;
		double sin = Math.sin(da);
		double cos = Math.cos(da);
		for (Point p : vertices) {
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

	public void setPosition(Point p) {
		position = p;
	}

	/**
	 * The method implementing distinction between right and left handed figures
	 */
	protected void checkRightOrLeft() {
		double sum = 0;

		for (int i = 0, j = 1; j < vertices.size(); i++, j++) {
			sum += (vertices.get(j).x - vertices.get(i).x) * (vertices.get(j).y + vertices.get(i).y);
		}

		rotationRight = sum > 0;
	}

	/**
	 * The method calculating the geometric center of the figure 
	 * @param withMove the Boolean indicating whether the coordinates of the center is to be changed 
	 * @param isMirror the Boolean indicating whether the optical object that is being calculated is Mirror or Prism
	 */
	protected void calcCentroid(boolean withMove, boolean isMirror) {
		double x = 0;
		double y = 0;
		int n = 0;
		int max = isMirror ? vertices.size() : vertices.size() - 1;
		for (; n < max; n++) {
			x += vertices.get(n).x;
			y += vertices.get(n).y;
		}

		x /= n;
		y /= n;

		for (Point p : vertices) {
			p.x -= x;
			p.y -= y;
		}

		if (withMove) {
			position = position.moveBy(new Point(x, y));
		}

		calcBounds();
	}

	public abstract void addNewVertex(Point p);

	/**
	 * The method indicating how user created optical object is finished
	 * @return
	 */
	public abstract boolean endDrawing();
}
