package me.kinomoto.proteam.elements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import me.kinomoto.proteam.Surroundings;
import me.kinomoto.proteam.Surroundings.PointPosition;

/**
 * BeamSource class is implementing the BeamSource of light creating, saving, painting, moving, rotating and changing parameters methods.
 *
 */
public class BeamSource {
	private static final int WIDTH = 40;
	private static final int HEIGHT = 20;
	private static final int MARGIN = 10;
	private static final int STROKE_WIDTH = 2;
	
	private static final int WIDTH_M = WIDTH + 2 * MARGIN;
	private static final int HEIGHT_M = HEIGHT + 2 * MARGIN;
	private static final int HALF_WIDTH_M = WIDTH_M / 2;
	private static final int HALF_HEIGHT_M = HEIGHT_M / 2;
	private static final int HALF_WIDTH = WIDTH / 2;
	private static final int HALF_HEIGHT = HEIGHT / 2;
	

	private static final double DOUBLE_2PI = 2 * Math.PI;
	private static final int DOT_SIZE = 4;
	private static final int HALF_DOT_SIZE = DOT_SIZE / 2;

	private static final int ROTATION_POINT_PLUS = 7;
	private static final int ROTATION_POINT_MINUS = 4;

	Segment segment;
	private double wavelength;
	private double angle;
	private double cosA;
	private double sinA;
	

	/**
	 * The constructor that is creating the {@link BeamSource} from the input file
	 * @param is input stream 
	 * @throws IOException reading from input stream exception
	 */
	public BeamSource(DataInputStream is) throws IOException {
		segment = new Segment(is);
		wavelength = is.readDouble();
		updateAngle();
		updateTrig();
	}

	/**
	 * The constructor that is creating the {@link BeamSource} with given parameters and along the certain direction 
	 * @param segment indicated the direction in which the BeamSource is orientated 
	 * @param wavelength the double parameter in nanometers 
	 */
	public BeamSource(Segment segment, double wavelength) {
		this.segment = segment;
		this.wavelength = wavelength;
		updateAngle();
		updateTrig();
	}

	/**
	 * The constructor that is creating the {@link BeamSource} with given parameters after rotation
	 * @param pos Point that is geometric center of the object
	 * @param angle the angle along which the BeamSource is created
	 * @param wavelength the double parameter in nanometers 
	 */
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

	/**
	 * The method is creating the Beam that the BeamSource is producing 
	 * @return Beam 
	 */
	public Beam getBeam() {
		return new Beam(segment.moveBy(new Point(HEIGHT*cosA, HEIGHT*sinA)), wavelength, 1);
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
		this.angle = angle % DOUBLE_2PI;
		updateTrig();
		segment.end = new Point(segment.begin.x + cosA, segment.begin.y + sinA);
	}

	/**
	 * Method implementing graphical representation of the beam source.
	 * @param g Graphics2D object 
	 */
	public void paint(Graphics2D g) {
		Graphics2D p = (Graphics2D) g.create();
		p.setStroke(new BasicStroke(STROKE_WIDTH));
		p.setColor(Color.YELLOW);
		p.translate(segment.begin.x, segment.begin.y);
		p.rotate(angle);
		p.fillRect(-HALF_WIDTH, -HALF_HEIGHT, WIDTH, HEIGHT);
		p.setColor(Color.BLACK);
		p.setStroke(new BasicStroke(1.5f));
		p.drawRect(-HALF_WIDTH, -HALF_HEIGHT, WIDTH, HEIGHT);
	}

	/**
	 * Method implementing graphical representation of the beam source selection that is shown when the object is marked on the plain.
	 * @param g Graphics2D object 
	 */
	public void paintSelection(Graphics2D g) {
		Graphics2D p = (Graphics2D) g.create();
		p.translate(segment.begin.x, segment.begin.y);
		p.rotate(angle);
		p.setStroke(Surroundings.selectionStroke);
		p.setColor(Color.BLACK);
		p.drawRect(-HALF_WIDTH_M, -HALF_HEIGHT_M, WIDTH_M, HEIGHT_M);
		p.translate(-HALF_WIDTH_M, -HALF_HEIGHT_M);
		p.fillRect(-HALF_DOT_SIZE, -HALF_DOT_SIZE, DOT_SIZE, DOT_SIZE);
		p.translate(WIDTH_M, 0);
		p.fillRect(-HALF_DOT_SIZE, -HALF_DOT_SIZE, DOT_SIZE, DOT_SIZE);
		p.translate(0, HEIGHT_M);
		p.fillRect(-HALF_DOT_SIZE, -HALF_DOT_SIZE, DOT_SIZE, DOT_SIZE);
		p.translate(-WIDTH_M, 0);
		p.fillRect(-HALF_DOT_SIZE, -HALF_DOT_SIZE, DOT_SIZE, DOT_SIZE);

	}

	/**
	 * The method checking whether the given Point is inside the object so that the BeamSourve can be selected on the working plain
	 * @param p given Point
	 * @param selected BeamSource to be selected
	 * @return Surroundings.PointPosition
	 */
	public Surroundings.PointPosition isPointInside(Point p, BeamSource selected) {
		Point t = p.min(segment.begin);
		double x = Math.abs(t.x * cosA + t.y * sinA);
		double y = Math.abs(t.x * sinA - t.y * cosA);

		double ax = x - HALF_WIDTH_M;
		double ay = y - HALF_HEIGHT_M;

		if (selected == this && ax > -ROTATION_POINT_MINUS && ax < ROTATION_POINT_PLUS && ay > -ROTATION_POINT_MINUS && ay < ROTATION_POINT_PLUS)
			return PointPosition.POINT_ROTATE;
		if (y <= HALF_HEIGHT && x <= HALF_WIDTH)
			return PointPosition.POINT_INSIDE;
		if (selected == this && y <= HALF_HEIGHT_M && x <= HALF_WIDTH_M)
			return PointPosition.POINT_INSIDE;

		return PointPosition.POINT_OUTSIDE;
	}

	/**
	 * The method is saving to the file the BeamSource parameters
	 * @param os DataOutputStream
	 * @throws IOException the exception served while problems DataOutputSteam
	 */
	public void save(DataOutputStream os) throws IOException {
		segment.save(os);
		os.writeDouble(wavelength);
	}

	/**
	 * The method updating the BeamSource coordinated after moving object
	 * @param dx
	 * @param dy
	 */
	public void moveBy(int dx, int dy) {
		segment = segment.moveBy(new Point(dx, dy));
		updateAngle();
		updateTrig();
	}

	/**
	 * Used by {@link Surroundings#getSelectedAngle(Point)}
	 * 
	 * @return
	 */
	public double getAngle(Point p) {
		return Math.atan2(p.y - segment.begin.y, p.x - segment.begin.x);
	}

	/**
	 * The method updating angle after rotation
	 * @param da
	 */
	public void rotate(double da) {
		setAngle(angle + da);
	}

	public void setPosition(Point p) {
		segment.begin = p;
		setAngle(angle);
	}
}
