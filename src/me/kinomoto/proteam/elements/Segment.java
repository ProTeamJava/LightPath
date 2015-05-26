package me.kinomoto.proteam.elements;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Segment {
	public Point begin, end;

	public Segment(DataInputStream is) throws IOException {
		begin = new Point(is);
		end = new Point(is);
	}

	public Segment(Point begin, Point end) {
		super();
		this.begin = begin;
		this.end = end;
	}

	public Segment moveBy(Point dp) {
		return new Segment(begin.moveBy(dp), end.moveBy(dp));
	}

	public void save(DataOutputStream os) throws IOException {
		begin.save(os);
		end.save(os);
	}

	public boolean equals(Object obj) {
		try {
			Segment s = (Segment) obj;
			return s.begin.equals(begin) && s.end.equals(end);
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return begin.hashCode() * 103456 + end.hashCode();
	}
	
	@Override
	public String toString() {
		return begin.toString() + " " + end.toString();
	}

}
