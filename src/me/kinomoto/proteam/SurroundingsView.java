package me.kinomoto.proteam;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import me.kinomoto.proteam.elements.Beam;
import me.kinomoto.proteam.elements.BeamSource;
import me.kinomoto.proteam.elements.Mirror;
import me.kinomoto.proteam.elements.Point;
import me.kinomoto.proteam.elements.Prism;
import me.kinomoto.proteam.elements.Segment;
import me.kinomoto.proteam.history.History;
import me.kinomoto.proteam.history.HistoryNodeMoveAbstract;
import me.kinomoto.proteam.history.HistoryNodeRotationAbstract;
import me.kinomoto.proteam.settings.SettingsPanel;

/**
 *JPanel representing working plane
 */
public class SurroundingsView extends JPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 5447523639086911950L;

	public enum TOOL {
		POINTER, ROTATE, MIRROR, TRIANGLE_PRISM, SQUARE_PRISM, SOURCE, DRAW_PRISM, DRAW_MIRROR
	}

	private JPopupMenu menuPopup = new JPopupMenu();
	private JMenuItem removeSelected = new JMenuItem(Messages.get("delete"), Main.getDeleteI());

	private TOOL selectedTool = TOOL.POINTER;

	Surroundings surroundings;
	SettingsPanel settingsPanel;
	JFileChooser fc = new JFileChooser();

	private static final int BASE_WIDTH = 3840;
	private static final int BASE_HEIGHT = 2160;
	private static final float MAX_SCALE = 10;
	private static final float MIN_SCALE = .2f;
	private static final double SCALE_STEP = Math.sqrt(2);

	private static final int HALF_BASE_WIDTH = BASE_WIDTH / 2;
	private static final int HALF_BASE_HEIGHT = BASE_HEIGHT / 2;

	private int viewWidth = BASE_WIDTH;
	private int viewHeight = BASE_HEIGHT;

	private double scale = 1;

	int x = 0;
	int y = 0;
	double a = 0;

	/**
	 * The constructor of default working plane  with triangular Prism, Mirror and BeamSource of the red light
	 * @param settingsPanel SettingsPanel
	 * @param ref Main
	 */
	public SurroundingsView(SettingsPanel settingsPanel, Main ref) {
		surroundings = new Surroundings(this, ref);
		this.settingsPanel = settingsPanel;

		initUI();

		surroundings.add(Prism.getTrianglePrism(new Point(0, 0)));
		surroundings.add(Mirror.getMirror(new Point(235, 55)));
		surroundings.add(new BeamSource(new Segment(new Point(-300, 50), new Point(0, 0)), Beam.RED));
	}

	/**
	 * The constructor of the working plane loaded from the file 
	 * @param settingsPanel SettingsPanel
	 * @param ref Main
	 * @param path input file path
	 * @throws LoadException
	 */
	public SurroundingsView(SettingsPanel settingsPanel, Main ref, String path) throws LoadException {
		surroundings = new Surroundings(this, path, ref);
		this.settingsPanel = settingsPanel;
		initUI();
	}

	/**
	 * The method setting the plane size are implementing MouseListeners and object removal
	 */
	private void initUI() {

		this.setPreferredSize(new Dimension(viewWidth, viewHeight));

		this.addMouseListener(this);
		this.addMouseMotionListener(this);

		menuPopup.add(removeSelected);
		removeSelected.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				surroundings.deleteSelected();
			}
		});

	}

	/**
	 * The graphical representation of the plain with antialiasing implementation
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(scale, scale);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.translate(HALF_BASE_WIDTH, HALF_BASE_HEIGHT);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		surroundings.paint(g2);
	}

	/**
	 * The method updating size of the plane taking into account scaling options
	 */
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
		if (scale > MAX_SCALE)
			return;
		scale *= SCALE_STEP;
		updateSize();
	}

	public void scaleDown() {
		if (scale < MIN_SCALE)
			return;
		scale /= SCALE_STEP;
		updateSize();
	}

	/**
	 * The method is enabling savint the working plane to the PNG file
	 * @throws IOException
	 */
	public void saveAsPng() throws IOException {

		Image png = createImage(this.getWidth(), this.getHeight());
		Graphics2D paint = (Graphics2D) png.getGraphics();
		this.paint(paint);
		int returnVal = fc.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION && !ImageIO.write((RenderedImage) png, "png", fc.getSelectedFile())) {
			throw new IOException("Can't write to " + fc.getSelectedFile().getCanonicalPath());

		}
	}

	public void saveAs(String path) throws IOException {
		surroundings.saveAs(path);
	}

	public void save() throws IOException {
		surroundings.save();
	}

	public boolean hasPath() {
		return surroundings.hasPath();
	}

	public TOOL getSelectedTool() {
		return selectedTool;
	}

	/**
	 * The method serves the actions with the selected tools from the ToolBar and updates the plane
	 * @param selectedTool
	 */
	public void setSelectedTool(TOOL selectedTool) {
		if (this.selectedTool == TOOL.DRAW_MIRROR || this.selectedTool == TOOL.DRAW_PRISM) {
			surroundings.endDrawing();
		}
		this.selectedTool = selectedTool;
		if (selectedTool != TOOL.ROTATE && selectedTool != TOOL.POINTER) {
			surroundings.setSelection(SelectionType.SURROUNDINGS);
			repaint();
		}
	}

	/**
	 * The method serves the mouse action for rotating and pointing the objects
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		Point t = mouseEventToSurroundingsPosition(e);

		if (selectedTool == TOOL.ROTATE || selectedTool == TOOL.POINTER) {
			PointPosition pos = surroundings.mousePressed(t);
			surroundings.mouseOver(SurroundingsView.this, t);

			if (pos == PointPosition.POINT_ROTATE) {
				selectedTool = TOOL.ROTATE;
			} else {
				selectedTool = TOOL.POINTER;
			}

			settingsPanel.setPanel(surroundings.getSelectedSettingsPanel());
			if (selectedTool == TOOL.POINTER) {
				surroundings.makeMoveNode();
				x = e.getX();
				y = e.getY();
			} else if (selectedTool == TOOL.ROTATE) {
				surroundings.makeRotationNode();
				a = surroundings.getSelectedAngle(t);
			}
			repaint();

			if (e.getButton() == MouseEvent.BUTTON3 && surroundings.getSelection() != SelectionType.SURROUNDINGS) {
				menuPopup.show(SurroundingsView.this, e.getX(), e.getY());
			}
		}
	}

	/**
	 * The method serves the mouse action for drawing the objects
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		if (selectedTool == TOOL.DRAW_MIRROR || selectedTool == TOOL.DRAW_PRISM) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				surroundings.addNewVertexToDrawing(selectedTool == TOOL.DRAW_MIRROR, mouseEventToSurroundingsPosition(e));
			} else if (e.getButton() == MouseEvent.BUTTON3) {
				surroundings.endDrawing();
			}
			repaint();
		} else {
			surroundings.mouseRelased();
			surroundings.simulate();
			mouseEntered(e);
		}
	}

	/**
	 * The method serves the mouse action for placing instant objects onto the plane
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		Point t = mouseEventToSurroundingsPosition(e);

		if (selectedTool == TOOL.MIRROR) {
			surroundings.newElement(Mirror.getMirror(t));
			repaint();
		} else if (selectedTool == TOOL.SQUARE_PRISM) {
			surroundings.newElement(Prism.getSquarePrism(t));
			repaint();
		} else if (selectedTool == TOOL.TRIANGLE_PRISM) {
			surroundings.newElement(Prism.getTrianglePrism(t));
			repaint();
		} else if (selectedTool == TOOL.SOURCE) {
			surroundings.newSource(new BeamSource(t, 0, Beam.RED));
			repaint();
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		surroundings.cleanNew();
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// nth
	}

	/**
	 * The method serves the mouse action for rotating and pointing the objects
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		Point t = mouseEventToSurroundingsPosition(e);

		if (selectedTool == TOOL.ROTATE || selectedTool == TOOL.POINTER) {
			surroundings.mouseOver(SurroundingsView.this, t);
		} else {
			surroundings.moveNewTo(t);
			repaint();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Point t = mouseEventToSurroundingsPosition(e);

		if (selectedTool == TOOL.POINTER) {
			int nx = e.getX();
			int ny = e.getY();
			surroundings.moveBy(nx - x, ny - y);
			if (surroundings.getSelection() != SelectionType.SURROUNDINGS) {
				try {
					HistoryNodeMoveAbstract node = (HistoryNodeMoveAbstract) History.getLastNode();
					node.moveBy(new Point(nx - x, ny - y));
				} catch (ClassCastException ex) {
					// nth
				}
				x = nx;
				y = ny;
			}
			surroundings.simulate();
		} else if (selectedTool == TOOL.ROTATE) {
			double na = surroundings.getSelectedAngle(t);
			double da = na - a;
			try {
				HistoryNodeRotationAbstract node = (HistoryNodeRotationAbstract) History.getLastNode();
				node.rotateBy(da);
			} catch (ClassCastException ex) {
				// nth?
			}
			surroundings.rotateSelected(da);
			a = surroundings.getSelectedAngle(t);

			surroundings.simulate();

		} else {
			surroundings.moveNewTo(t);
			repaint();
		}
	}

	private Point mouseEventToSurroundingsPosition(MouseEvent e) {
		return (new Point(e.getPoint())).mul(1 / scale).min(new Point(HALF_BASE_WIDTH, HALF_BASE_HEIGHT));
	}
}
