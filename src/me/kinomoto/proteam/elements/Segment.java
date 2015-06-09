package me.kinomoto.proteam.elements;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Segment class is representing the information about the segment coordinates 
 */
public class Segment {
	private static final int HASHCODE_CONST = 103456;
	public Point begin, end;

	
	/**
	 * The constructor that is creating the {@link Segment} from the input file
	 * @param is input stream 
	 * @throws IOException reading from input stream exception
	 */
	public Segment(DataInputStream is) throws IOException {
		begin = new Point(is);
		end = new Point(is);
	}
	/**
	 * The constructor that is creating the {@link Segment} from Points
	 * @param begin Point
	 * @param end Point
	 */
	public Segment(Point begin, Point end) {
		super();
		this.begin = begin;
		this.end = end;
	}

	/**
	 * Translation of the Segment coordinates
	 * @param dp the Point by which parameters the Point is moved by
	 * @return Segment
	 */
	public Segment moveBy(Point dp) {
		return new Segment(begin.moveBy(dp), end.moveBy(dp));
	}

	/**
	 * The method is saving to the file the Segment parameters
	 * @param os DataOutputStream
	 * @throws IOException the exception served while problems DataOutputSteam
	 */
	public void save(DataOutputStream os) throws IOException {
		begin.save(os);
		end.save(os);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		try {
			Segment s = (Segment) obj;
			return s.begin.equals(begin) && s.end.equals(end);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return begin.hashCode() * HASHCODE_CONST + end.hashCode();
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return begin.toString() + " " + end.toString();
	}

}
