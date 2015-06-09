package me.kinomoto.proteam.history;

import me.kinomoto.proteam.Surroundings;
import me.kinomoto.proteam.elements.AbstractOpticalElement;

/**
 *The class is creating a HistoryNode after translating optical element on the working plain
 */
public class HistoryNodeMoveElement extends HistoryNodeMoveAbstract {
	AbstractOpticalElement element;

	public HistoryNodeMoveElement(Surroundings s, AbstractOpticalElement element) {
		super(s);
		this.element = element;
	}

	@Override
	public void undo() {
		element.moveBy((int)-moveBy.x, (int)-moveBy.y);
		s.simulate();
	}

	@Override
	public void redo() {
		element.moveBy((int)moveBy.x, (int)moveBy.y);
		s.simulate();
	}

}
