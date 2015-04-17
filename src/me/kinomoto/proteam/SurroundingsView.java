package me.kinomoto.proteam;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public class SurroundingsView extends JPanel {

	private static final long serialVersionUID = 5447523639086911950L;

	Surroundings surroundings = new Surroundings(this);
	SettingsPanel settingsPanel;

	private int baseWidth = 3840;
	private int baseHeight = 2160;

	private int viewWidth = baseWidth;
	private int viewHeight = baseHeight;
	
	private double scale = 1;

	public SurroundingsView(final SettingsPanel settingsPanel) {
		this.settingsPanel = settingsPanel;
		
		this.setPreferredSize(new Dimension(viewWidth, viewHeight));

		surroundings.add(new Mirror(new Point(100, 100)));
		surroundings.add(new Prism(new Point(150, 180), 1.5));
		surroundings.add(new Mirror(new Point(75, 200)));
		surroundings.add(new BeamSource(new Segment(new Point(200, 0), new Point(70, 150)), 650));
		surroundings.add(new BeamSource(new Segment(new Point(50, 100), new Point(100, 90)), 550));
		surroundings.add(new BeamSource(new Segment(new Point(210, 250), new Point(100, 150)), 450));
		
		this.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				Point t = (new Point(e.getPoint())).mul(1/scale).min(new Point(baseWidth / 2, baseHeight / 2));
				surroundings.mousePressed(t);
				settingsPanel.setPanel(surroundings.getSelectedSettingsPanel());
			}
		});
		
		this.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(scale, scale);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.translate(baseWidth / 2, baseHeight / 2);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		surroundings.paint(g2);

	}
	
	private void updateSize() {
		viewWidth = (int) (scale * baseWidth);
		viewHeight = (int) (scale * baseHeight);
		this.setSize(new Dimension(viewWidth, viewHeight));
		this.setPreferredSize(new Dimension(viewWidth, viewHeight));
		revalidate();
		invalidate();
		repaint();
	}

	public void scaleUp() {
		if(scale > 10) return;
		scale *= Math.sqrt(2);
		updateSize();
	}

	public void scaleDown() {
		if(scale < 0.2) return;
		scale /= Math.sqrt(2);
		updateSize();
	}

}
