package me.kinomoto.proteam;

public class TestClass {

	public TestClass() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		System.out.println("test class");
		

		Surroundings s = new Surroundings();
		s.add(new BeamSource(new Segment(new Point(0,0), new Point(0, 1)), 650));
		s.add(new Prism(new Point(0, 2)));

	}

}
