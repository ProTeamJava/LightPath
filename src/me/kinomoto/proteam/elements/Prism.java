package me.kinomoto.proteam.elements;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import javax.swing.JPanel;

import me.kinomoto.proteam.Surroundings;
import me.kinomoto.proteam.settings.PrismSettingsPanel;

public class Prism extends AbstractOpticalElement {

	private double ior;
	private static final int magicNumberPrism = 0x7072;

	public Prism(Point position, double ior) {
		super(position, AbstractOpticalElement.getSquare());
		this.ior = ior;
		checkRightOrLeft();
	}

	public Prism(Point position, double ior, List<Point> vert) {
		super(position, vert);
		this.ior = ior;
		checkRightOrLeft();
	}

	public static Prism getSquarePrism(Point position) {
		return new Prism(position, 1.33, AbstractOpticalElement.getSquare());
	}

	public static Prism getTrianglePrism(Point position) {
		return new Prism(position, 1.33, AbstractOpticalElement.getTriangle());
	}

	@Override
	void findCollisionSolution(Surroundings s, Beam b, Segment seg) {

		// incident beam unit vector coordinates
		double sx = b.segment.end.x - b.segment.begin.x;
		double sy = b.segment.end.y - b.segment.begin.y;
		double sl = sx * sx + sy * sy;
		double div2 = Math.sqrt(sl);
		sy /= div2;
		sx /= div2;
		// sufrace normal unit vertor coordinates
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

		// cooeficient ratio
		// incident to reflected
		double nir = s.getIor() / ior;
		// reflected to incident
		double nri = ior / s.getIor();

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

		if (b.brightness > 0.01) {
			double bright = b.brightness * .99;
			Segment tmp = new Segment(b.segment.end, end);
			s.add(new Beam(tmp, b.wavelenght, bright, seg));
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
		os.writeDouble(magicNumberPrism);
		saveAbstract(os);
		os.writeDouble(ior);

	}

}
