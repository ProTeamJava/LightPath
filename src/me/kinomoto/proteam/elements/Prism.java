package me.kinomoto.proteam.elements;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.JPanel;

import me.kinomoto.proteam.Surroundings;
import me.kinomoto.proteam.settings.PrismSettingsPanel;

public class Prism extends AbstractOpticalElement {

	private double refractiveIndex;

	public Prism(Point position, double ior) {
		super(position, AbstractOpticalElement.getSquare());
		refractiveIndex = ior;
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
		nx *= -1;
		
		double nl = nx * nx + ny * ny;
		double div = Math.sqrt(nl);
		ny /= div;
		nx /= div;

		// cooeficient ratio
		// incident to reflected
		double nir = s.getIor() / refractiveIndex;
		// reflected to incident
		double nri = refractiveIndex / s.getIor();

		// refracted or reflected beam vector coordinates
		double rx;
		double ry;

		double nDotI, nDotI2, a, k, kk, D2;

		nDotI = nx * sx + ny * sy;
		nDotI2 = nDotI * nDotI;
		if (nDotI >= 0.0) {
			k = nir;
			kk = k * k;
		} else {
			k = nri;
			kk = k * k;
		}
		D2 = 1.0 - kk * (1.0 - nDotI2);

		if (D2 >= 0.0) {
			if (nDotI >= 0.0) {
				a = k * nDotI - Math.sqrt(D2);
			} else {
				a = k * nDotI + Math.sqrt(D2);
			}
			rx = (a * nx - k * sx);
			ry = (a * ny - k * sy);

		} else {

			rx = 2 * nDotI * nx - sx;
			ry = 2 * nDotI * ny - sy;

		}

		Point end = new Point(b.segment.end.x - rx, b.segment.end.y - ry);

		if (b.brightness > 0.01) {
			double bright = b.brightness * .99;
			Segment tmp = new Segment(b.segment.end, end);
			s.add(new Beam(tmp, b.wavelenght, bright));
		}

	}

	public double getRefractiveIndex() {
		return refractiveIndex;
	}

	// TODO check if it is good
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
	
	public double getIOR(){
		return refractiveIndex;
	}
	
	public void setIOR(double ior) {
		refractiveIndex = ior;
	}

	@Override
	public void save(DataOutputStream os) throws IOException {
		// save magic
		saveAbstract(os);
		// save ior
		
	}

}
