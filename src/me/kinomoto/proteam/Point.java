package me.kinomoto.proteam;

public class Point {
	double x, y;

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
	
	

}
