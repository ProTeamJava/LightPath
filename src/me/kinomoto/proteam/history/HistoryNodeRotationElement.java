package me.kinomoto.proteam.history;

import me.kinomoto.proteam.Surroundings;
import me.kinomoto.proteam.elements.AbstractOpticalElement;

/**
 *The class is creating a HistoryNode after rotating optical element on the working plain
 */
public class HistoryNodeRotationElement extends HistoryNodeRotationAbstract {
	private AbstractOpticalElement element;

	public HistoryNodeRotationElement(Surroundings s, AbstractOpticalElement element) {
		super(s);
		this.element = element;
	}

	@Override
	public void undo() {
		element.rotate(-rotateBy);
		element.addAngle();
		s.simulate();
	}

	@Override
	public void redo() {
		element.rotate(rotateBy);
		element.addAngle();
		s.simulate();
	}

}
