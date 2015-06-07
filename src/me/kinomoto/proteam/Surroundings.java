package me.kinomoto.proteam;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

import me.kinomoto.proteam.elements.AbstractOpticalElement;
import me.kinomoto.proteam.elements.Beam;
import me.kinomoto.proteam.elements.BeamSource;
import me.kinomoto.proteam.elements.Mirror;
import me.kinomoto.proteam.elements.Point;
import me.kinomoto.proteam.elements.Prism;
import me.kinomoto.proteam.history.History;
import me.kinomoto.proteam.history.HistoryNodeDeleteElement;
import me.kinomoto.proteam.history.HistoryNodeDeleteSource;
import me.kinomoto.proteam.history.HistoryNodeMoveElement;
import me.kinomoto.proteam.history.HistoryNodeMoveSource;
import me.kinomoto.proteam.history.HistoryNodeNewElement;
import me.kinomoto.proteam.history.HistoryNodeNewSource;
import me.kinomoto.proteam.history.HistoryNodeRotationElement;
import me.kinomoto.proteam.history.HistoryNodeRotationSource;
import me.kinomoto.proteam.settings.BeamSourceSettingsPanel;
import me.kinomoto.proteam.settings.SurroundingsSettingsPanel;

public class Surroundings implements Serializable {
	private static final long serialVersionUID = -5054686324248695647L;

	/**
	 * Used to check if file is saved by this application.
	 */
	private static final int MAGIC_NUMBER = 0x5F6C7068;

	// selection stroke constants
	private static final float SELECTION_STROKE_WIDTH = 1.0f;
	private static final float SELECTION_STROKE_MITER_LIMIT = 10.0f;
	private static final float[] SELECTION_STROKE_DASH_PATERN = new float[] { 2.0f, 2.0f };
	private static final float SELECTION_STROKE_DASH_FASE = 0.0f;
	public static final BasicStroke selectionStroke = new BasicStroke(SELECTION_STROKE_WIDTH, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, SELECTION_STROKE_MITER_LIMIT, SELECTION_STROKE_DASH_PATERN, SELECTION_STROKE_DASH_FASE);

	public enum PointPosition {
		POINT_INSIDE, POINT_ROTATE, POINT_OUTSIDE
	}

	public enum SelectionType {
		SELECTED_BEAM_SOURCE, SELECTED_ELEMENT, SURROUNDINGS
	}

	private Main ref = null;

	// model variables
	private List<AbstractOpticalElement> elements;
	private List<BeamSource> sources;
	private List<Beam> beams;

	private double ior = 1;

	private SurroundingsView view;

	// file variables
	private String path = "";
	private boolean modyfied = false;

	// simulation variables
	private boolean isSimulating = false;
	private boolean simQueue = false;

	// adding new elements variables
	private SelectionType newDrawType = SelectionType.SURROUNDINGS;
	private AbstractOpticalElement newElement = null;
	private BeamSource newSource = null;

	// selection variables
	private SelectionType selection = SelectionType.SURROUNDINGS;
	private BeamSource selectedBeamSource = null;
	private AbstractOpticalElement selectedElement = null;

	// drawing variables
	private boolean drawing = false;
	private AbstractOpticalElement drawingElement = null;

	public Surroundings(SurroundingsView view, String path, Main ref) throws LoadException {
		this.path = path;
		this.view = view;
		this.ref = ref;
		elements = new ArrayList<AbstractOpticalElement>();
		sources = new ArrayList<BeamSource>();
		beams = new ArrayList<Beam>();
		load();
		simulate();
	}

	public Surroundings(SurroundingsView view, Main ref) {
		elements = new ArrayList<AbstractOpticalElement>();
		sources = new ArrayList<BeamSource>();
		beams = new ArrayList<Beam>();
		this.view = view;
		this.ref = ref;
	}

	public void simulate() {
		if (isSimulating) {
			simQueue = true;
			return;
		}
		isSimulating = true;
		beams.clear();
		for (BeamSource source : sources) {
			add(source.getBeam());
		}

		while (!simulated()) {
			for (int ii = 0; ii < beams.size(); ii++) {
				Beam beam = beams.get(ii);
				if (!beam.getIfSimulated()) {
					beam.simulate(this);
				}
			}
		}

		isSimulating = false;
		if (simQueue) {
			simQueue = false;
			simulate();
			return;
		}
		view.repaint();
	}

	public void add(AbstractOpticalElement e) {
		modyfied = true;
		getElements().add(e);
		simulate();
	}

	public void add(BeamSource s) {
		modyfied = true;
		sources.add(s);
		simulate();
	}

	public void add(Beam b) {
		modyfied = true;
		beams.add(b);
	}

	private boolean simulated() {
		for (Beam beam : beams) {
			if (!beam.getIfSimulated())
				return false;
		}
		return true;
	}

	public void paint(Graphics2D g) {

		for (Beam beam : beams) {
			beam.paint(g);
		}

		for (BeamSource beamSource : sources) {
			beamSource.paint(g);
		}

		for (AbstractOpticalElement abstractOpticalElement : getElements()) {
			abstractOpticalElement.paint(g);
		}

		if (newElement != null) {
			newElement.paint(g, Color.GRAY);
		}

		if (newSource != null) {
			newSource.paint(g);
		}

		if (drawing) {
			drawingElement.paint(g);
		}

		switch (selection) {
		case SELECTED_BEAM_SOURCE:
			selectedBeamSource.paintSelection(g);
			break;
		case SELECTED_ELEMENT:
			selectedElement.paintSelection(g);
			break;
		default:
			break;
		}
	}

	public void addNewVertexToDrawing(boolean mirrorOrPrism, Point p) {
		History.setStop(true);
		if (!drawing) {
			// make new drawing
			if (mirrorOrPrism) {
				// mirror
				drawingElement = new Mirror(p);
			} else {
				// prism
				drawingElement = new Prism(p, Prism.GLASS_IOR);
			}
			drawing = true;
		} else {
			drawingElement.addNewVertex(p);
		}
	}

	public void endDrawing() {
		History.setStop(false);
		if (drawing) {
			if (drawingElement.endDrawing()) {
				add(drawingElement);
				History.addNode(new HistoryNodeNewElement(this, drawingElement));
			}
			drawing = false;
			drawingElement = null;
		}
	}

	public PointPosition mousePressed(Point p) {

		for (BeamSource beamSource : sources) {
			PointPosition pos = beamSource.isPointInside(p, selectedBeamSource);
			if (pos != PointPosition.POINT_OUTSIDE) {
				selection = SelectionType.SELECTED_BEAM_SOURCE;
				selectedBeamSource = beamSource;
				selectedElement = null;
				return pos;
			}
		}

		for (AbstractOpticalElement element : getElements()) {
			PointPosition pos;
			if (element == selectedElement) {
				pos = element.isPointInsideSelected(p);
			} else {
				pos = element.isPointInside(p) ? PointPosition.POINT_INSIDE : PointPosition.POINT_OUTSIDE;
			}
			if (pos != PointPosition.POINT_OUTSIDE) {
				selection = SelectionType.SELECTED_ELEMENT;
				selectedElement = element;
				selectedBeamSource = null;
				return pos;
			}
		}

		selectedBeamSource = null;
		selectedElement = null;
		setSelection(SelectionType.SURROUNDINGS);
		return PointPosition.POINT_OUTSIDE;
	}

	public JPanel getSelectedSettingsPanel() {
		switch (getSelection()) {
		case SURROUNDINGS:
			return new SurroundingsSettingsPanel(this);
		case SELECTED_BEAM_SOURCE:
			return new BeamSourceSettingsPanel(selectedBeamSource, this);
		case SELECTED_ELEMENT:
			return selectedElement.getSettingsPanel(this);
		default:
			return null;
		}
	}

	public void setIOR(double ior) {
		this.setIor(ior);
		this.simulate();
	}

	public void updateSettingsPanel() {
		view.settingsPanel.setPanel(getSelectedSettingsPanel());
	}

	public void load() throws LoadException {
		try {
			DataInputStream is = new DataInputStream(new FileInputStream(path));
			int magic = is.readInt();
			if (magic != MAGIC_NUMBER) {
				is.close();
				throw new LoadException("Unknow file type.");
			}

			setIor(is.readDouble());

			int sourcesSize = is.readInt();
			for (int i = 0; i < sourcesSize; i++) {
				sources.add(new BeamSource(is));
			}

			int elementsSize = is.readInt();
			for (int i = 0; i < elementsSize; i++) {
				elements.add(AbstractOpticalElement.load(is));
			}

			is.close();
		} catch (IOException e) {
			Logger.getGlobal().log(Level.INFO, e.getLocalizedMessage());
			throw new LoadException("Can't open file");
		}
	}

	public void save() throws IOException {
		DataOutputStream os = new DataOutputStream(new FileOutputStream(path));
		os.writeInt(MAGIC_NUMBER);
		os.writeDouble(getIor());

		os.writeInt(sources.size());
		for (BeamSource s : sources) {
			s.save(os);
		}

		os.writeInt(elements.size());
		for (AbstractOpticalElement element : elements) {
			element.save(os);
		}

		os.close();

	}

	public void saveAs(String path) throws IOException {
		this.path = path;
		save();
	}

	public boolean isModyfied() {
		return modyfied;
	}

	public void setModyfied(boolean is) {
		modyfied = is;
	}

	public double getIor() {
		return ior;
	}

	public void setIor(double ior) {
		this.ior = ior;
	}

	public List<AbstractOpticalElement> getElements() {
		return elements;
	}

	public void setElements(List<AbstractOpticalElement> elements) {
		this.elements = elements;
	}

	public void deleteSelected() {
		switch (getSelection()) {
		case SELECTED_BEAM_SOURCE:
			History.addNode(new HistoryNodeDeleteSource(this, selectedBeamSource));
			sources.remove(selectedBeamSource);
			selectedBeamSource = null;
			break;
		case SELECTED_ELEMENT:
			History.addNode(new HistoryNodeDeleteElement(this, selectedElement));
			elements.remove(selectedElement);
			selectedElement = null;
			break;
		default:
			break;
		}
		setSelection(SelectionType.SURROUNDINGS);
		simulate();
		view.settingsPanel.setPanel(getSelectedSettingsPanel());
	}

	public SelectionType getSelection() {
		return selection;
	}

	public void setSelection(SelectionType selection) {
		this.selection = selection;
		updateSettingsPanel();
	}

	public void moveBy(int x, int y) {
		switch (selection) {
		case SELECTED_BEAM_SOURCE:
			selectedBeamSource.moveBy(x, y);
			break;
		case SELECTED_ELEMENT:
			selectedElement.moveBy(x, y);
			break;
		case SURROUNDINGS:
			ref.scroll(-x, -y);
			break;
		default:
			break;
		}
	}

	private void mouseOverBeamSource(Component c, Point p) {
		switch (selectedBeamSource.isPointInside(p, selectedBeamSource)) {
		case POINT_INSIDE:
			c.setCursor(new Cursor(Cursor.MOVE_CURSOR));
			break;
		case POINT_ROTATE:
			c.setCursor(Main.getRotateCursor());
			break;
		default:
			c.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			break;
		}
	}

	private void mouseOverElement(Component c, Point p) {
		switch (selectedElement.isPointInsideSelected(p)) {
		case POINT_INSIDE:
			c.setCursor(new Cursor(Cursor.MOVE_CURSOR));
			break;
		case POINT_ROTATE:
			c.setCursor(Main.getRotateCursor());
			break;
		default:
			c.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			break;
		}
	}

	public void mouseOver(Component c, Point p) {
		switch (selection) {
		case SELECTED_BEAM_SOURCE:
			mouseOverBeamSource(c, p);
			break;
		case SELECTED_ELEMENT:
			mouseOverElement(c, p);
			break;
		default:
			c.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			break;
		}
	}

	public double getSelectedAngle(Point p) {
		if (selection == SelectionType.SELECTED_BEAM_SOURCE) {
			return selectedBeamSource.getAngle(p);
		} else if (selection == SelectionType.SELECTED_ELEMENT) {
			return selectedElement.getAngle(p);
		}
		return 0;
	}

	public void rotateSelected(double da) {
		if (selection == SelectionType.SELECTED_BEAM_SOURCE) {
			selectedBeamSource.rotate(da);
		} else if (selection == SelectionType.SELECTED_ELEMENT) {
			selectedElement.rotate(da);
		}
	}

	public void mouseRelased() {
		if (selection == SelectionType.SELECTED_ELEMENT) {
			selectedElement.addAngle();
		}
		if (newDrawType == SelectionType.SELECTED_ELEMENT) {
			elements.add(newElement);
			History.addNode(new HistoryNodeNewElement(this, newElement));
			newElement = null;
			newDrawType = SelectionType.SURROUNDINGS;
		} else if (newDrawType == SelectionType.SELECTED_BEAM_SOURCE) {
			sources.add(newSource);
			History.addNode(new HistoryNodeNewSource(this, newSource));
			newSource = null;
			newDrawType = SelectionType.SURROUNDINGS;
			simulate();
		}

	}

	public void newElement(AbstractOpticalElement element) {
		newDrawType = SelectionType.SELECTED_ELEMENT;
		newElement = element;
	}

	public void newSource(BeamSource source) {
		newDrawType = SelectionType.SELECTED_BEAM_SOURCE;
		newSource = source;
	}

	public void moveNewTo(Point p) {
		switch (newDrawType) {
		case SELECTED_BEAM_SOURCE:
			newSource.setPosition(p);
			break;
		case SELECTED_ELEMENT:
			newElement.setPosition(p);
			break;
		default:
			break;

		}
	}

	public void cleanNew() {
		newDrawType = SelectionType.SURROUNDINGS;
		newElement = null;
		newSource = null;
	}

	public void makeRotationNode() {
		if (selection == SelectionType.SELECTED_BEAM_SOURCE) {
			History.addNode(new HistoryNodeRotationSource(this, selectedBeamSource));
		} else if (selection == SelectionType.SELECTED_ELEMENT) {
			History.addNode(new HistoryNodeRotationElement(this, selectedElement));
		}

	}

	public void makeMoveNode() {
		if (selection == SelectionType.SELECTED_ELEMENT) {
			History.addNode(new HistoryNodeMoveElement(this, selectedElement));
		} else if (selection == SelectionType.SELECTED_BEAM_SOURCE) {
			History.addNode(new HistoryNodeMoveSource(this, selectedBeamSource));
		}
	}

	public void deleteElement(AbstractOpticalElement element) {
		elements.remove(element);
		if (element == selectedElement) {
			setSelection(SelectionType.SURROUNDINGS);
			selectedElement = null;
			view.settingsPanel.setPanel(getSelectedSettingsPanel());
		}
		simulate();
	}

	public void deleteSource(BeamSource bs) {
		sources.remove(bs);
		if (bs == selectedBeamSource) {
			setSelection(SelectionType.SURROUNDINGS);
			selectedBeamSource = null;
			view.settingsPanel.setPanel(getSelectedSettingsPanel());
		}
		simulate();
	}

	public boolean hasPath() {
		return path != "";
	}

	public double getIorAt(Point p, AbstractOpticalElement e) {
		for (AbstractOpticalElement element : elements) {
			if (e != element && element.isPointInside(p)) {
				try {
					Prism prism = (Prism) element;
					return prism.getIOR();
				} catch (ClassCastException e1) {
					// nth
				}
			}
		}
		return ior;
	}

}
