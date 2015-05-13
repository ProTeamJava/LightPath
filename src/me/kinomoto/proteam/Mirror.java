package me.kinomoto.proteam;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.JPanel;

public class Mirror extends AbstractOpticalElement {
	
	private double absorption = .99; 

	public double getAbsorption() {
		return absorption;
	}

	public void setAbsorption(double absorption) {
		this.absorption = absorption;
	}

	public Mirror(Point position) {
		super(position, AbstractOpticalElement.getMirror());
	}

	@Override
	void findCollisionSolution(Surroundings s, Beam b, Segment seg) {
		double sy = seg.end.x - seg.begin.x;
		double sx = seg.end.y - seg.begin.y;
		sy *= -1;
		double sl = sx * sx + sy * sy;
		
		double div = Math.sqrt(sl);
		sy /= div;
		sx /= div;
		sl = sx * sx + sy * sy;
		

		double dx = b.segment.end.x - b.segment.begin.x;
		double dy = b.segment.end.y - b.segment.begin.y;

		double dot = dx * sx + dy * sy;
		double w = 2 * dot / sl;

		double nx = dx - w * sx;
		double ny = dy - w * sy;

		Point end = new Point(b.segment.end.x + nx, b.segment.end.y + ny);

		if (b.brightness*absorption > 0.01) {
			double bright = b.brightness * absorption;
			Segment tmp = new Segment(b.segment.end, end);
			s.add(new Beam(tmp, b.wavelenght, bright));
		}
	}
	
	/**
	 * Checks if point distance to line is less than 5 units;
	 * @param p1 - begin of segment
	 * @param p2 - end of segment
	 * @param s - point to check
	 * @return
	 */
	private boolean pointNearLine(Point p1, Point p2, Point s) {
		double px = p2.x - p1.x;
		double py = p2.y - p1.y;
		double tmp = px*px + py*py;
		double u = (s.x - p1.x) * px + (s.y - p1.y) * py;
		u /= tmp;
		if(u > 1)
			u = 1;
		else if(u < 0)
			u = 0;
		
		double tx = p1.x + u * px;
		double ty = p1.y + u * py;
		
		double dx = tx - s.x;
		double dy = ty - s.y;
		return dx*dx+dy*dy < 25;
		
	}

	@Override
	public boolean isPointInside(Point p) {
		p = p.min(position);
		for(int i = 0, j = 1; j < vertices.size(); i++, j++)
			if(pointNearLine(vertices.get(i), vertices.get(j), p))
				return true;
		
		return false;
	}

	@Override
	public JPanel getSettingsPanel(Surroundings s) {
		return new MirrorSettingsPanel(this, s);
	}

	@Override
	public void save(DataOutputStream os) throws IOException {
		// save magic
		saveAbstract(os);
		// save abs
		
	}

}
