package me.kinomoto.proteam;

public class TestClass {

	public TestClass() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		System.out.println("test class");
		/*
		AbstractOpticalElement m = new Prism(new Point(0.1, 2));
		Segment s = new Segment(new Point(0, 0), new Point(0, 4));
		
		try {
			Point p = m.collision(s);
			if(p == null)
			{
				System.out.println("no collision");
			}
			else
			{
				System.out.println(p);
			}
		} catch (MultipleCollisionsException e) {
			System.out.println("multiple collisions");
		}
		*/
		Surroundings s = new Surroundings();
		s.add(new BeamSource(new Segment(new Point(0,0), new Point(0, 1)), 650));
		s.add(new Prism(new Point(0, 2)));

	}

}
