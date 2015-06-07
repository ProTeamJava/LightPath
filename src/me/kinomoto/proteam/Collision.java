package me.kinomoto.proteam;

import java.io.Serializable;

import me.kinomoto.proteam.elements.Line;
import me.kinomoto.proteam.elements.Point;
import me.kinomoto.proteam.elements.Segment;

public class Collision implements Serializable {
	private static final long serialVersionUID = -5189513211791442267L;

	private Point point;
	private Segment segment;
	private Line line;

	public Collision(Point point, Segment segment, Line line) {
		super();
		this.setPoint(point);
		this.setSegment(segment);
		this.line = line;
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

	public Line getLine() {
		return line;
	}

}
