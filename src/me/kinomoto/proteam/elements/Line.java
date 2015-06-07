package me.kinomoto.proteam.elements;

import java.io.Serializable;

public class Line implements Serializable {
	private static final long serialVersionUID = -959526529300895798L;

	private boolean verticalOrHorizontal;
	private double a = 0;
	private double b = 0;

	public Line(Segment s) {
		if (s.begin.x == s.end.x) {
			verticalOrHorizontal = true;
			a = s.begin.x;
		} else if (s.begin.y == s.end.y) {
			verticalOrHorizontal = true;
			b = s.end.y;
		} else {

			verticalOrHorizontal = false;
			a = (s.begin.y - s.end.y) / (s.begin.x - s.end.x);
			b = s.begin.y - a * s.end.x;
		}
	}

	public boolean isEqual(Line l) {
		return a == l.a && b == l.b && verticalOrHorizontal == l.verticalOrHorizontal;
	}

}
