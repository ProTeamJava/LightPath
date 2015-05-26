package me.kinomoto.proteam;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.RenderedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import me.kinomoto.proteam.Surroundings.PointPosition;
import me.kinomoto.proteam.Surroundings.SelectionType;
import me.kinomoto.proteam.elements.BeamSource;
import me.kinomoto.proteam.elements.Mirror;
import me.kinomoto.proteam.elements.Point;
import me.kinomoto.proteam.elements.Prism;
import me.kinomoto.proteam.elements.Segment;
import me.kinomoto.proteam.settings.SettingsPanel;

public class SurroundingsView extends JPanel {
	private static final long serialVersionUID = 5447523639086911950L;

	public enum TOOL {
		POINTER, ROTATE
	}

	private JPopupMenu menuPopup = new JPopupMenu();
	private JMenuItem removeSelected = new JMenuItem(Messages.get("delete"), Main.getDeleteI());

	private TOOL selectedTool = TOOL.POINTER;

	Surroundings surroundings = new Surroundings(this);
	SettingsPanel settingsPanel;
	JFileChooser fc = new JFileChooser();

	private static final int BASE_WIDTH = 3840;
	private static final int BASE_HEIGHT = 2160;

	private int viewWidth = BASE_WIDTH;
	private int viewHeight = BASE_HEIGHT;

	private double scale = 1;

	int x = 0;
	int y = 0;
	double a = 0;

	public SurroundingsView(final SettingsPanel settingsPanel, Main ref) {
		this.settingsPanel = settingsPanel;

		this.setPreferredSize(new Dimension(viewWidth, viewHeight));

		surroundings.add(new Mirror(new Point(100, 100)));
		surroundings.add(new Prism(new Point(150, 180), 1.5));
		surroundings.add(new Mirror(new Point(75, 200)));
		surroundings.add(new BeamSource(new Point(200, 0), 105 / 180.0 * Math.PI, 650));
		surroundings.add(new BeamSource(new Segment(new Point(50, 100), new Point(100, 90)), 550));
		surroundings.add(new BeamSource(new Segment(new Point(210, 250), new Point(100, 150)), 450));
		surroundings.add(new BeamSource(new Point(280, 200), 190 * Math.PI / 180.0, 400));
		surroundings.add(new BeamSource(new Point(0, 0), .5 * Math.PI, 730));

		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {

				Point t = (new Point(e.getPoint())).mul(1 / scale).min(new Point(BASE_WIDTH / 2.0, BASE_HEIGHT / 2.0));
				PointPosition pos = surroundings.mousePressed(t);
				surroundings.mouseOver(SurroundingsView.this, t);

				switch (pos) {
				case POINT_ROTATE:
					selectedTool = TOOL.ROTATE;
					break;
				default:
					selectedTool = TOOL.POINTER;
					break;
				}

				settingsPanel.setPanel(surroundings.getSelectedSettingsPanel());
				if (selectedTool == TOOL.POINTER) {
					x = e.getX();
					y = e.getY();
				} else if (selectedTool == TOOL.ROTATE) {
					a = surroundings.getSelectedAngle(t);
				}
				repaint();

				if (e.getButton() == MouseEvent.BUTTON3 && surroundings.getSelection() != SelectionType.SURROUNDINGS) {
					menuPopup.show(SurroundingsView.this, e.getX(), e.getY());
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				surroundings.mouseRelased();
				surroundings.simulate();
			}

		});

		this.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				Point t = (new Point(e.getPoint())).mul(1 / scale).min(new Point(BASE_WIDTH / 2.0, BASE_HEIGHT / 2.0));
				surroundings.mouseOver(SurroundingsView.this, t);
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				if (surroundings.getSelection() != Surroundings.SelectionType.SURROUNDINGS) {
					Point t = (new Point(e.getPoint())).mul(1 / scale).min(new Point(BASE_WIDTH / 2.0, BASE_HEIGHT / 2.0));
					if (selectedTool == TOOL.POINTER) {
						int nx = e.getX();
						int ny = e.getY();
						surroundings.moveBy(nx - x, ny - y);
						x = nx;
						y = ny;
					} else if (selectedTool == TOOL.ROTATE) {
						double na = surroundings.getSelectedAngle(t);
						double da = na - a;
						surroundings.rotateSelected(da);
						a = surroundings.getSelectedAngle(t);
					}
					surroundings.simulate();
				}
			}
		});

		menuPopup.add(removeSelected);
		removeSelected.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				surroundings.deleteSelected();
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(scale, scale);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.translate(BASE_WIDTH / 2, BASE_HEIGHT / 2);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		surroundings.paint(g2);
	}

	private void updateSize() {
		viewWidth = (int) (scale * BASE_WIDTH);
		viewHeight = (int) (scale * BASE_HEIGHT);
		this.setSize(new Dimension(viewWidth, viewHeight));
		this.setPreferredSize(new Dimension(viewWidth, viewHeight));
		revalidate();
		invalidate();
		repaint();
	}

	public void scaleUp() {
		if (scale > 10)
			return;
		scale *= Math.sqrt(2);
		updateSize();
	}

	public void scaleDown() {
		if (scale < 0.2)
			return;
		scale /= Math.sqrt(2);
		updateSize();
	}

	public void saveAsPng() {

		Image png = createImage(this.getWidth(), this.getHeight());
		Graphics2D paint = (Graphics2D) png.getGraphics();
		this.paint(paint);
		int returnVal = fc.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				if (!ImageIO.write((RenderedImage) png, "png", fc.getSelectedFile())) {
					// TODO save error
				}
			} catch (IOException e) {
				// TODO save error
			}
		}
	}

}
