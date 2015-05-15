package me.kinomoto.proteam.elements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BeamSource {
	Segment segment;
	private double wavelength;

	public double getWavelength() {
		return wavelength;
	}

	public void setWavelength(double wavelength) {
		this.wavelength = wavelength;
	}

	public BeamSource(DataInputStream is) throws IOException {
		segment = new Segment(is);
		wavelength = is.readDouble();
	}

	public BeamSource(Segment segment, double wavelength) {
		super();
		this.segment = segment;
		this.wavelength = wavelength;
	}

	public BeamSource(Point pos, double angle, double wavelength) {
		this.segment = new Segment(pos, new Point(pos.x + Math.cos(angle), pos.y + Math.sin(angle)));
		this.wavelength = wavelength;
	}

	public Beam getBeam() {
		double a = getAngle();
		double x = 20 * Math.cos(a);
		double y = 20 * Math.sin(a);
		return new Beam(segment.moveBy(new Point(x, y)), wavelength, 1, null);
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
		if (x < 10 && x > -10 && y < 20 && y > -20)
			return true;
		return false;
	}

	public void save(DataOutputStream os) throws IOException {
		segment.save(os);
		os.writeDouble(wavelength);
	}

	public void moveBy(int dx, int dy) {
		segment = segment.moveBy(new Point(dx, dy));
	}
}
