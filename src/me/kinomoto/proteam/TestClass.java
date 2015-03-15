package me.kinomoto.proteam;

public class TestClass {

	public TestClass() {
	}

	public static void main(String[] args) {
		System.out.println("test class");

		Surroundings s = new Surroundings();
		s.add(new Mirror(new Point(0, 1)));
		s.add(new Mirror(new Point(2, 1)));
		s.add(new Mirror(new Point(2, 3.3)));
		s.add(new BeamSource(new Segment(new Point(0, 0), new Point(0, .5)), 650));
		

	}

}
