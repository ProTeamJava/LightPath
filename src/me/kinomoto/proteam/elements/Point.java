package me.kinomoto.proteam.elements;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Point {
	double x, y;
	
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
		return new String("Point (" + String.valueOf(x) + "," + String.valueOf(y) + ")");
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

	public boolean equals(Point p) {
		return p.x == x && p.y == y;
	}
	
}
