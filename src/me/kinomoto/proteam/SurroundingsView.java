package me.kinomoto.proteam;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class SurroundingsView extends JPanel {

	private static final long serialVersionUID = 5447523639086911950L;

	Surroundings surroundings = new Surroundings();

	private int baseWidth = 3840;
	private int baseHeight = 2160;

	private int viewWidth = baseWidth;
	private int viewHeight = baseHeight;
	
	private int scale = 1;

	public SurroundingsView() {
		this.setPreferredSize(new Dimension(viewWidth, viewHeight));

		surroundings.add(new Mirror(new Point(100, 100)));
		surroundings.add(new Prism(new Point(150, 180), 1.33));
		surroundings.add(new Mirror(new Point(75, 200)));
		surroundings.add(new BeamSource(new Segment(new Point(200, 0), new Point(70, 150)), 650));
		surroundings.add(new BeamSource(new Segment(new Point(50, 100), new Point(100, 90)), 550));
		surroundings.add(new BeamSource(new Segment(new Point(150, 250), new Point(120, 150)), 450));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(scale, scale);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.translate(viewWidth / 2, viewHeight / 2);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		surroundings.paint(g2);

	}

}
