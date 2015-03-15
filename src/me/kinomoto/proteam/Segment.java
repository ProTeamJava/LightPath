package me.kinomoto.proteam;

public class Segment {
	public Point begin, end;

	public Segment() {
		begin = new Point();
		end = new Point();
	}

	public Segment(Point begin, Point end) {
		super();
		this.begin = begin;
		this.end = end;
	}

}
