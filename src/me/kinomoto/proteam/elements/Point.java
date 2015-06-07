package me.kinomoto.proteam.elements;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Comparator;

public class Point implements Serializable {
	private static final long serialVersionUID = -7537942395847131117L;
	
	public double x;
	public double y;

	public static final Comparator<Point> xComparator = new Comparator<Point>() {

		@Override
		public int compare(Point p1, Point p2) {
			return p2.x > p1.x ? 1 : -1;
		}
	};

	public static final Comparator<Point> yComparator = new Comparator<Point>() {

		@Override
		public int compare(Point p1, Point p2) {
			return p2.y > p1.y ? 1 : -1;
		}
	};

	public Point(DataInputStream is) throws IOException {
		x = is.readDouble();
		y = is.readDouble();
	}

	public Point(java.awt.Point p) {
		this.x = p.getX();
		this.y = p.getY();
	}

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return new String("Point (" + x + "," + y + ")");
	}

	public Point min(Point p) {
		return new Point(x - p.x, y - p.y);
	}

	public Point mul(double s) {
		return new Point(x * s, y * s);
	}

	public void save(DataOutputStream os) throws IOException {
		os.writeDouble(x);
		os.writeDouble(y);
	}

	public Point moveBy(Point dp) {
		return new Point(x + dp.x, y + dp.y);
	}

	@Override
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

	@Override
	public int hashCode() {
		return (int) (x * 1e10 + y);
	}

}
