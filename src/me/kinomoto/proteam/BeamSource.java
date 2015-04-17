package me.kinomoto.proteam;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class BeamSource {
	Segment segment;
	private double wavelength;

	public BeamSource(Segment segment, double wavelength) {
		super();
		this.segment = segment;
		this.wavelength = wavelength;
	}

	public Beam getBeam(double refractiveIndex) {
		return new Beam(segment, wavelength, 1, refractiveIndex);
	}
	
	public double getAngle() {
		return Math.atan2(segment.end.y - segment.begin.y, segment.end.x - segment.begin.x);
	}

	public void paint(Graphics2D g) {
		Graphics2D p = (Graphics2D) g.create();
		p.setStroke(new BasicStroke(2));
		p.setColor(Color.YELLOW);
		p.translate(segment.begin.x, segment.begin.y);
		p.rotate(getAngle());
		p.fillRect(-20, -10, 40, 20);

	}

	public boolean isPointInside(Point p) {
		Point t = p.min(segment.begin);
		double angle = this.getAngle();
		double x = t.x * Math.cos(angle) - t.y * Math.sin(angle);
		double y = t.x * Math.sin(angle) + t.y * Math.cos(angle);
		if(x < 10 && x > -10 && y < 20 && y > -20)
			return true;
		return false;
	}
}
