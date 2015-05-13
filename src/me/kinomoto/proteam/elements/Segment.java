package me.kinomoto.proteam.elements;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;



public class Segment {
	public Point begin, end;
	
	public Segment(DataInputStream is) throws IOException{
		begin = new Point(is);
		end = new Point(is);
	}

	public Segment(Point begin, Point end) {
		super();
		this.begin = begin;
		this.end = end;
	}
	
	public void save(DataOutputStream os) throws IOException {
		begin.save(os);
		end.save(os);
	}

}
