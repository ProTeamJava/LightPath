package me.kinomoto.proteam;

import java.util.ArrayList;
import java.util.List;

abstract public class AbstractOpticalElement {
	protected Point position;
	protected List<Point> vertices;

	public AbstractOpticalElement(Point position, List<Point> vertices) {
		this.position = position;
		this.vertices = vertices;
	}

	public AbstractOpticalElement(Point position) {
		this.position = position;
		this.vertices = new ArrayList<Point>();
	}

	public static List<Point> getTriangle()
	{
		List<Point> tmp = new ArrayList<Point>();
		tmp.add(new Point(0, 1));
		tmp.add(new Point(-.6, -.3));
		tmp.add(new Point(.6, -.3));
		tmp.add(new Point(0, 1));
		return tmp;
	}

	public static List<Point> getMirror()
	{
		List<Point> tmp = new ArrayList<Point>();
		tmp.add(new Point(-1, 0));
		tmp.add(new Point(1, 0));
		return tmp;
	}

	public static List<Point> getSquare()
	{
		List<Point> tmp = new ArrayList<Point>();
		tmp.add(new Point(-1, -1));
		tmp.add(new Point(-1, 1));
		tmp.add(new Point(1, 1));
		tmp.add(new Point(1, -1));
		tmp.add(new Point(-1, -1));
		return tmp;
	}

	public Point get(int i)
	{
		return new Point(vertices.get(i).x + position.x, vertices.get(i).y + position.y);
	}

	/**
	 * 
	 * @return null - no collision
	 * @throws MultipleCollisionsException if there is more than one collision
	 */
	public Point collision(Segment s) throws MultipleCollisionsException
	{
		Point tmp = null;

		for(int i = 0, j = 1; j < vertices.size(); i++, j++)
		{
			short d1 = direction(get(i), get(j), s.begin);
			short d2 = direction(get(i), get(j), s.end);
			short d3 = direction(s.begin, s.end, get(i));
			short d4 = direction(s.begin, s.end, get(j));

			if(((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0)) &&
					((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0)))
			{
				//mamy kolizję
				if(tmp == null)
				{
					//1st collision
					tmp = collisionPoint(get(i), get(j), s.begin, s.end);
				}
				else
				{
					//next collision
					throw new MultipleCollisionsException();
				}
			}
		}

		return tmp;
	}

	private short direction(Point pi, Point pj, Point pk)
	{
		double tmp = ( (pk.x - pi.x) * (pj.y - pi.y) - (pj.x - pi.x) * (pk.y - pi.y) );
		if(tmp > 0) return 1;
		if(tmp < 0) return -1;
		return 0;
	}

	private Point collisionPoint(Point p1, Point p2, Point p3, Point p4)
	{

		Point point = null;

		if(p1.x == p2.x)
		{
			Point tmp = p1;
			p1 = p3;
			p3 = tmp;
			tmp = p2;
			p2 = p4;
			p4 = tmp;
		}

		if(p3.x == p4.x)
		{
			double a1 = (p1.y - p2.y) / (p1.x - p2.x);
			double b1 = p1.y - a1 * p1.x; 
			point = new Point(p3.x, p3.x * a1 + b1);


		}
		else
		{


			double a1 = (p1.y - p2.y) / (p1.x - p2.x);
			double b1 = p1.y - a1 * p1.x; 
			double a2 = (p3.y - p4.y) / (p3.x - p4.x);
			
			double b2 = p3.y - a2 * p3.x; 
			double x = (b2 - b1) / (a1 - a2);
			point = new Point(x, x * a1 + b1);

		}
		return point;
	}
	
	abstract void collisionSolution(Surroundings s);

}
