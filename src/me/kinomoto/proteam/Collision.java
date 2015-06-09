package me.kinomoto.proteam;

import me.kinomoto.proteam.elements.Point;
import me.kinomoto.proteam.elements.Segment;




/**
 * The class Collision object store information about point of intersection of beam and optical object as well as the incident beam
 */
public class Collision {
	private Point point;
	private Segment segment;

	/**
	 * The constructor attributing to the collision of beam and optical object the point of intersection and the incident beam direction
	 * @param point the point of intersection of beam and optical object
	 * @param segment indication of the incident beam direction
	 */
	public Collision(Point point, Segment segment) {
		super();
		this.setPoint(point);
		this.setSegment(segment);
	}

	/**
	 * The method returning the Point of collision
	 * @return point the point of intersection of beam and optical object
	 */
	public Point getPoint() {
		return point;
	}

	/**
	 * The method setting the Point of collision
	 * @param point the point of intersection of beam and optical object
	 */
	public void setPoint(Point point) {
		this.point = point;
	}

	/**
	 * The method returning the Segment indicating the incident beam direction
	 * @return segment indication of the incident beam direction
	 */
	public Segment getSegment() {
		return segment;
	}

	/**
	 * The method setting the Segment indicating the incident beam direction
	 * @param segment indication of the incident beam direction
	 */
	public void setSegment(Segment segment) {
		this.segment = segment;
	}

}
