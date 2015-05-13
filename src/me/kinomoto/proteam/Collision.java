package me.kinomoto.proteam;

import me.kinomoto.proteam.elements.Point;
import me.kinomoto.proteam.elements.Segment;



public class Collision {
	private Point point;
	private Segment segment;

	public Collision(Point point, Segment segment) {
		super();
		this.setPoint(point);
		this.setSegment(segment);
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public Segment getSegment() {
		return segment;
	}

	public void setSegment(Segment segment) {
		this.segment = segment;
	}

}
