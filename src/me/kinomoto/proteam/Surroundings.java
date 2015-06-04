package me.kinomoto.proteam;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import me.kinomoto.proteam.elements.AbstractOpticalElement;
import me.kinomoto.proteam.elements.Beam;
import me.kinomoto.proteam.elements.BeamSource;
import me.kinomoto.proteam.elements.Point;
import me.kinomoto.proteam.settings.BeamSourceSettingsPanel;
import me.kinomoto.proteam.settings.SurroundingsSettingsPanel;

public class Surroundings {
	private static final int MAGIC_NUMBER = 0x5F6C7068;

	private List<AbstractOpticalElement> elements;
	private List<BeamSource> sources;
	private List<Beam> beams;

	private double ior = 1;

	private SurroundingsView view;

	private String path = "";
	private boolean modyfied = false;

	private boolean isSimulating = false;
	private boolean simQueue = false;
	
	private SelectionType halfTranparentDrawType = SelectionType.SURROUNDINGS;
	private AbstractOpticalElement elementTransp = null;
	private BeamSource beamSourTransp = null;
	
	private Main ref = null;

	public enum PointPosition {
		POINT_INSIDE, POINT_ROTATE, POINT_OUTSIDE
	}

	public enum SelectionType {
		SELECTED_BEAM_SOURCE, SELECTED_ELEMENT, SURROUNDINGS
	}

	private SelectionType selection = SelectionType.SURROUNDINGS;
	private BeamSource selectedBeamSource = null;
	private AbstractOpticalElement selectedElement = null;

	public Surroundings(SurroundingsView view, String path, Main ref) {
		this.path = path;
		this.view = view;
		this.ref = ref;
		// TODO load
	}

	public Surroundings(SurroundingsView view, Main ref) {
		setElements(new ArrayList<AbstractOpticalElement>());
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
		
		if(elementTransp != null) {
			elementTransp.paint(g, Color.GRAY);
		}
		
		if(beamSourTransp != null) {
			//TODO color
			beamSourTransp.paint(g);			
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
			if(element == selectedElement) {
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

	public double getIOR() {
		return getIor();
	}

	public void updateSettingsPanel() {
		view.settingsPanel.setPanel(getSelectedSettingsPanel());
	}

	public void save() {
		try {
			DataOutputStream os = new DataOutputStream(new FileOutputStream(path));
			os.writeInt(MAGIC_NUMBER);
			os.writeDouble(getIor());
			os.writeInt(sources.size());

			for (BeamSource s : sources) {
				s.save(os);
			}

			for (AbstractOpticalElement element : getElements()) {
				element.save(os);
			}

			os.close();
		} catch (IOException e) {
			// TODO save error
		}
	}

	public void saveAs(String path) {
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
			sources.remove(selectedBeamSource);
			selectedBeamSource = null;

			break;
		case SELECTED_ELEMENT:
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

	public void mouseOver(Component c, Point p) {
		switch (selection) {
		case SELECTED_BEAM_SOURCE:
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
			break;
		case SELECTED_ELEMENT:
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
		if(selection == SelectionType.SELECTED_ELEMENT) {
			selectedElement.addAngle();
		}
		if(halfTranparentDrawType == SelectionType.SELECTED_ELEMENT) {
			elements.add(elementTransp);
			elementTransp = null;
			halfTranparentDrawType = SelectionType.SURROUNDINGS;
		} else if(halfTranparentDrawType == SelectionType.SELECTED_BEAM_SOURCE) {
			sources.add(beamSourTransp);
			beamSourTransp = null;
			halfTranparentDrawType = SelectionType.SURROUNDINGS;
			simulate();
		}
		
	}
	
	public void newTranp(AbstractOpticalElement element) {
		halfTranparentDrawType = SelectionType.SELECTED_ELEMENT;
		elementTransp = element;
	}
	
	public void newTranp(BeamSource source) {
		halfTranparentDrawType = SelectionType.SELECTED_BEAM_SOURCE;
		beamSourTransp = source;
	}
	
	public void moveTranspTo(Point p) {
		switch(halfTranparentDrawType) {
		case SELECTED_BEAM_SOURCE:
			beamSourTransp.setPosition(p);
			break;
		case SELECTED_ELEMENT:
			elementTransp.setPosition(p);
			break;
		default:
			break;
		
		}
	}
	
	public void cleanTransp() {
		halfTranparentDrawType = SelectionType.SURROUNDINGS;
		elementTransp = null;
		beamSourTransp = null;
	}
}
