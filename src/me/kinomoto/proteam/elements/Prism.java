package me.kinomoto.proteam.elements;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import javax.swing.JPanel;

import me.kinomoto.proteam.Surroundings;
import me.kinomoto.proteam.settings.PrismSettingsPanel;

/**
 * Prism class is representing the Prism optical properties 
 */
public class Prism extends AbstractOpticalElement {
	public static final int MAGIC_NUMBER = 0x7072;
	public static final double GLASS_IOR = 1.5;
	public static final double WATTER_IOR = 4.0/3.0;
	/**
	 * Should be less than 1.
	 */
	private static final double PRISM_ABSORPTION = .99;

	private double ior;

	public Prism(Point position, double ior) {
		super(position);
		vertices.add(new Point(0,0));
		vertices.add(new Point(0,0));
		this.ior = ior;
		
	}

	public Prism(Point position, double ior, List<Point> vert) {
		super(position, vert);
		this.ior = ior;
		calcCentroid(false, false);
		checkRightOrLeft();
	}
	
	public Prism(Point position, List<Point> vert, DataInputStream is) throws IOException {
		super(position, vert);
		ior = is.readDouble();
	}

	public static Prism getSquarePrism(Point position) {
		return new Prism(position, WATTER_IOR, AbstractOpticalElement.getSquare());
	}

	public static Prism getTrianglePrism(Point position) {
		return new Prism(position, WATTER_IOR, AbstractOpticalElement.getTriangle());
	}

	/** 
	 * The refraction, reflection and absorbtion algorithms implementation using vectors and dot products calculations.
	 * In the collisionPoint there is new Beam generated in the calculated direction.
	 * @see me.kinomoto.proteam.elements.AbstractOpticalElement#findCollisionSolution(me.kinomoto.proteam.Surroundings, me.kinomoto.proteam.elements.Beam, me.kinomoto.proteam.elements.Segment)
	 */
	@Override
	void findCollisionSolution(Surroundings s, Beam b, Segment seg) {

		// incident beam unit vector coordinates
		double sx = b.segment.end.x - b.segment.begin.x;
		double sy = b.segment.end.y - b.segment.begin.y;
		double sl = sx * sx + sy * sy;
		double div2 = Math.sqrt(sl);
		sy /= div2;
		sx /= div2;
		// Surface normal unit vector coordinates
		double ny = seg.begin.x - seg.end.x;
		double nx = seg.begin.y - seg.end.y;

		if(rotationRight)
			nx *= -1;
		else
			ny *= -1;

		double nl = nx * nx + ny * ny;
		double div = Math.sqrt(nl);
		ny /= div;
		nx /= div;

		// Coefficient ratio
		// incident to reflected
		double surroundingsIOR = s.getIorAt(b.segment.end, this);
		double nir = surroundingsIOR / ior;
		// reflected to incident
		double nri = ior / surroundingsIOR;

		// refracted or reflected beam vector coordinates
		double rx;
		double ry;

		double dot, dot2, a, k, kk, d2;

		dot = nx * sx + ny * sy;
		dot2 = dot * dot;
		if (dot >= 0.0) {
			k = nir;
			kk = k * k;
		} else {
			k = nri;
			kk = k * k;
		}
		d2 = 1.0 - kk * (1.0 - dot2);

		if (d2 >= 0.0) {
			if (dot >= 0.0) {
				a = k * dot - Math.sqrt(d2);
			} else {
				a = k * dot + Math.sqrt(d2);
			}
			rx = (a * nx - k * sx);
			ry = (a * ny - k * sy);
		} else {
			rx = 2 * dot * nx - sx;
			ry = 2 * dot * ny - sy;
		}

		Point end = new Point(b.segment.end.x - rx, b.segment.end.y - ry);

		double bright = b.brightness * PRISM_ABSORPTION;
		if (bright > Beam.MIN_BRIGHTNESS) {
			Segment tmp = new Segment(b.segment.end, end);
			s.add(new Beam(tmp, b.wavelenght, bright));
		}

	}

	public double getRefractiveIndex() {
		return ior;
	}

	@Override
	public boolean isPointInside(Point p) {
		boolean out = false;
		for (int i = 0, j = vertices.size() - 1; i < vertices.size(); j = i++)
			if (((get(i).y > p.y) != (get(j).y > p.y)) && (p.x < (get(j).x - get(i).x) * (p.y - get(i).y) / (get(j).y - get(i).y) + get(i).x))
				out = !out;
		return out;
	}

	@Override
	public JPanel getSettingsPanel(Surroundings s) {
		return new PrismSettingsPanel(this, s);
	}

	public double getIOR() {
		return ior;
	}

	public void setIOR(double ior) {
		this.ior = ior;
	}

	@Override
	public void save(DataOutputStream os) throws IOException {
		os.writeInt(MAGIC_NUMBER);
		saveAbstract(os);
		os.writeDouble(ior);
	}

	@Override
	public void addNewVertex(Point p) {
		vertices.add(vertices.size() - 1, p.moveBy(position.mul(-1)));		
	}

	/** 
	 * There is no possibility to stop creating the Prism unless four vertices are created, however the last vertex is automatically equal the first one. 
	 * @see me.kinomoto.proteam.elements.AbstractOpticalElement#endDrawing()
	 */
	@Override
	public boolean endDrawing() {
		if(vertices.size() < 4)
			return false;
		
		checkRightOrLeft();
		calcCentroid(true, false);
		calcBounds();
		return true;
	}

}
