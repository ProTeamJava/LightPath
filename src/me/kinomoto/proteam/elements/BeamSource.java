package me.kinomoto.proteam.elements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import me.kinomoto.proteam.Surroundings;
import me.kinomoto.proteam.Surroundings.PointPosition;

public class BeamSource {
	// must by divisible by 2
	private static final int WIDTH = 40;
	private static final int HIEGHT = 20;
	private static final int MARGIN = 10;
	private static final int WIDTH_M = WIDTH + 2 * MARGIN;
	private static final int HEIGHT_M = HIEGHT + 2 * MARGIN;

	Segment segment;
	private double wavelength;
	private double angle;
	private double cosA;
	private double sinA;

	public BeamSource(DataInputStream is) throws IOException {
		segment = new Segment(is);
		wavelength = is.readDouble();
	}

	public BeamSource(Segment segment, double wavelength) {
		this.segment = segment;
		this.wavelength = wavelength;
		updateAngle();
		updateTrig();
	}

	public BeamSource(Point pos, double angle, double wavelength) {
		this.wavelength = wavelength;
		this.angle = angle;
		updateTrig();
		this.segment = new Segment(pos, new Point(pos.x + cosA, pos.y + sinA));
	}

	public double getWavelength() {
		return wavelength;
	}

	public void setWavelength(double wavelength) {
		this.wavelength = wavelength;
	}

	public Beam getBeam() {
		double x = 20 * cosA;
		double y = 20 * sinA;
		return new Beam(segment.moveBy(new Point(x, y)), wavelength, 1, null);
	}

	private void updateTrig() {
		cosA = Math.cos(angle);
		sinA = Math.sin(angle);
	}

	private void updateAngle() {
		angle = Math.atan2(segment.end.y - segment.begin.y, segment.end.x - segment.begin.x);
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle % 360.0;
		updateTrig();
		segment.end = new Point(segment.begin.x + cosA, segment.begin.y + sinA);
	}

	public void paint(Graphics2D g) {
		Graphics2D p = (Graphics2D) g.create();
		p.setStroke(new BasicStroke(2));
		p.setColor(Color.YELLOW);
		p.translate(segment.begin.x, segment.begin.y);
		p.rotate(angle);
		p.fillRect(-WIDTH / 2, -HIEGHT / 2, WIDTH, HIEGHT);
		p.setColor(Color.BLACK);
		p.setStroke(new BasicStroke(1.5f));
		p.drawRect(-WIDTH / 2, -HIEGHT / 2, WIDTH, HIEGHT);
	}

	public void paintSelection(Graphics2D g) {
		Graphics2D p = (Graphics2D) g.create();
		p.translate(segment.begin.x, segment.begin.y);
		p.rotate(angle);
		p.setStroke(new BasicStroke(1.0f, // Width
				BasicStroke.CAP_SQUARE, // End cap
				BasicStroke.JOIN_BEVEL, // Join style
				10.0f, // Miter limit
				new float[] { 2.0f, 2.0f }, // Dash pattern
				0.0f)); // Dash phase
		p.setColor(Color.BLACK);
		p.drawRect(-WIDTH_M / 2, -HEIGHT_M / 2, WIDTH_M, HEIGHT_M);
		p.translate(-WIDTH_M / 2, -HEIGHT_M / 2);
		p.fillRect(-2, -2, 4, 4);
		p.translate(WIDTH_M, 0);
		p.fillRect(-2, -2, 4, 4);
		p.translate(0, HEIGHT_M);
		p.fillRect(-2, -2, 4, 4);
		p.translate(-WIDTH_M, 0);
		p.fillRect(-2, -2, 4, 4);

	}

	public Surroundings.PointPosition isPointInside(Point p, BeamSource selected) {
		Point t = p.min(segment.begin);
		double x = Math.abs(t.x * cosA + t.y * sinA);
		double y = Math.abs(t.x * sinA - t.y * cosA);
		
		double ax = x - WIDTH_M / 2;
		double ay = y - HEIGHT_M / 2;

		if (selected == this && ax > -4 && ax < 7 && ay > -4 && ay < 7)
			return PointPosition.POINT_ROTATE;
		if (y <= HIEGHT / 2 && x <= WIDTH / 2)
			return PointPosition.POINT_INSIDE;
		if (selected == this && y <= HEIGHT_M / 2 && x <= WIDTH_M / 2)
			return PointPosition.POINT_INSIDE;

		return PointPosition.POINT_OUTSIDE;
	}

	public void save(DataOutputStream os) throws IOException {
		segment.save(os);
		os.writeDouble(wavelength);
	}

	public void moveBy(int dx, int dy) {
		segment = segment.moveBy(new Point(dx, dy));
		updateAngle();
		updateTrig();
	}
	
	/**
	 * Used by {@link Surroundings#getSelectedAngle(Point)}
	 * @return
	 */
	public double getAngle(Point p) {
		return Math.atan2(p.y - segment.begin.y, p.x - segment.begin.x);
	}
	
	public void rotate(double da) {
		setAngle(angle + da); 
	}

	public void setPosition(Point p) {
		segment.begin = p;
		setAngle(angle);
	}
}
