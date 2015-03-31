package me.kinomoto.proteam;

import java.awt.Color;
import java.awt.Graphics2D;

public class BeamSource {
	Segment segment;
	private double wavelenght;

	public BeamSource(Segment segment, double wavelenght) {
		super();
		this.segment = segment;
		this.wavelenght = wavelenght;
	}

	public Beam getBeam(double refractiveIndex) {
		return new Beam(segment, wavelenght, 1, refractiveIndex);
	}

	public void paint(Graphics2D g) {
		Graphics2D p = (Graphics2D) g.create();
		p.setColor(Color.YELLOW);
		p.translate(segment.begin.x, segment.begin.y);
		p.fillRect(-20, -40, 40, 80);
		// g.drawLine((int)segment.begin.x, (int)segment.begin.y, (int)segment.end.x, (int)segment.end.y);
	}
}
