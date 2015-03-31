package me.kinomoto.proteam;

import java.util.Scanner;

public class TestClass {

	public TestClass() {
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		double x, y, angle;
		System.out.println("Input x pos of beam source");
		Scanner reader = new Scanner(System.in);
		x = reader.nextDouble();
		System.out.println("Input y pos of beam source");
		y = reader.nextDouble();
		System.out.println("Input angle of beam source");
		angle = reader.nextDouble();

		reader.close();

		Surroundings s = new Surroundings();
		s.add(new Mirror(new Point(0, 1), "Mirror 1"));
		s.add(new Mirror(new Point(2, 1), "Mirror 2"));
		s.add(new Prism(new Point(2, 5), "Prism 1", 1.33));

		Point pos = new Point(0, 0);

		double rad = angle / 180.0 * Math.PI;
		Point vec = new Point(Math.cos(rad) + pos.x, Math.sin(rad) + pos.y);

		s.add(new BeamSource(new Segment(pos, vec), 650));

	}

}
