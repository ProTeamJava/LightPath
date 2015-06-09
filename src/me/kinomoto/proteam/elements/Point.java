package me.kinomoto.proteam.elements;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Comparator;

/**
 * Point class is holding the information about the point coordinates 
 */
public class Point {
	public double x;
	public double y;

	/**
	 * The method is comparing the x coordinates of two points
	 */
	public static final Comparator<Point> xComparator = new Comparator<Point>() {

		@Override
		public int compare(Point p1, Point p2) {
			return p2.x > p1.x ? 1 : -1;
		}
	};
	/**
	 * The method is comparing the y coordinates of two points
	 */
	public static final Comparator<Point> yComparator = new Comparator<Point>() {

		@Override
		public int compare(Point p1, Point p2) {
			return p2.y > p1.y ? 1 : -1;
		}
	};

	/**
	 * The constructor that is creating the {@link Point} from the input file
	 * @param is input stream 
	 * @throws IOException reading from input stream exception
	 */
	public Point(DataInputStream is) throws IOException {
		x = is.readDouble();
		y = is.readDouble();
	}
	
	/**
	 * The constructor that is creating the {@link Point} using external library object
	 * Enables to get the coordinates from the working plain
	 * @param p the java.awt.Point object
	 */
	public Point(java.awt.Point p) {
		this.x = p.getX();
		this.y = p.getY();
	}
	
	/**
	 * The constructor that is creating the {@link Point} 
	 * @param x double x-coordinate
	 * @param y double y-coordinate
	 */
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return new String("Point (" + x + "," + y + ")");
	}

	/**
	 * Subtraction of the Point coordinates
	 * @param p given Point
	 * @return 
	 */
	public Point min(Point p) {
		return new Point(x - p.x, y - p.y);
	}

	/**
	 * Multiplication of the Point coordinates with constant
	 * @param s the double given constant
	 * @return
	 */
	public Point mul(double s) {
		return new Point(x * s, y * s);
	}
	
	/**
	 * The method is saving to the file the Point parameters
	 * @param os DataOutputStream
	 * @throws IOException the exception served while problems DataOutputSteam
	 */
	public void save(DataOutputStream os) throws IOException {
		os.writeDouble(x);
		os.writeDouble(y);
	}

	/**
	 * Translation of the Point coordinates
	 * @param dp the Point by which parameters the Point is moved by
	 * @return Point
	 */
	public Point moveBy(Point dp) {
		return new Point(x + dp.x, y + dp.y);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		try {
			Point p = (Point) obj;
			return p.x == x && p.y == y;
		} catch (Exception e) {
			return false;
		}

	}

	public Point abs() {
		return new Point(Math.abs(x), Math.abs(y));
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (int) (x * 1e10 + y);
	}

}
