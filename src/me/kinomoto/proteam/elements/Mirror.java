package me.kinomoto.proteam.elements;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import javax.swing.JPanel;

import me.kinomoto.proteam.Surroundings;
import me.kinomoto.proteam.settings.MirrorSettingsPanel;

public class Mirror extends AbstractOpticalElement {

	private double absorption = .99;
	public static final int MAGIC_NUMBER = 0x6D69;

	public Mirror(Point position) {
		super(position, AbstractOpticalElement.getMirror());
	}
	
	public Mirror(Point position, List<Point> verts, DataInputStream is) throws IOException {
		super(position, verts);
		absorption = is.readDouble();
	}

	public double getAbsorption() {
		return absorption;
	}

	public void setAbsorption(double absorption) {
		this.absorption = absorption;
	}

	@Override
	void findCollisionSolution(Surroundings s, Beam b, Segment seg) {
		double ny = seg.end.x - seg.begin.x;
		double nx = seg.end.y - seg.begin.y;
		ny *= -1;
		double nl = nx * nx + ny * ny;

		double div = Math.sqrt(nl);
		ny /= div;
		nx /= div;

		double dx = b.segment.end.x - b.segment.begin.x;
		double dy = b.segment.end.y - b.segment.begin.y;

		double dot = dx * nx + dy * ny;
		double w = 2 * dot;

		double rx = dx - w * nx;
		double ry = dy - w * ny;

		Point end = new Point(b.segment.end.x + rx, b.segment.end.y + ry);

		if (b.brightness * absorption > 0.01) {
			double bright = b.brightness * absorption;
			Segment tmp = new Segment(b.segment.end, end);
			s.add(new Beam(tmp, b.wavelenght, bright, seg));
		}
	}

	/**
	 * Checks if point distance to line is less than 5 units;
	 * 
	 * @param p1
	 *            - begin of segment
	 * @param p2
	 *            - end of segment
	 * @param s
	 *            - point to check
	 * @return
	 */
	private boolean pointNearLine(Point p1, Point p2, Point s) {
		double px = p2.x - p1.x;
		double py = p2.y - p1.y;
		double tmp = px * px + py * py;
		double u = (s.x - p1.x) * px + (s.y - p1.y) * py;
		u /= tmp;
		if (u > 1)
			u = 1;
		else if (u < 0)
			u = 0;

		double tx = p1.x + u * px;
		double ty = p1.y + u * py;

		double dx = tx - s.x;
		double dy = ty - s.y;
		return dx * dx + dy * dy < 25;

	}

	@Override
	public boolean isPointInside(Point point) {
		Point p = point.min(position);
		for (int i = 0, j = 1; j < vertices.size(); i++, j++)
			if (pointNearLine(vertices.get(i), vertices.get(j), p))
				return true;

		return false;
	}

	@Override
	public JPanel getSettingsPanel(Surroundings s) {
		return new MirrorSettingsPanel(this, s);
	}

	@Override
	public void save(DataOutputStream os) throws IOException {
		os.writeInt(MAGIC_NUMBER);
		saveAbstract(os);
		os.writeDouble(absorption);
	}

}
