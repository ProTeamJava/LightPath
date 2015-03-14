package me.kinomoto.proteam;

public class Point {
	double x,y;

	public Point(){
	}
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return new String("Point (" + String.valueOf(x) + "," + String.valueOf(y) + ")");
	}

	

}
