package me.kinomoto.proteam.history;

import me.kinomoto.proteam.Surroundings;
import me.kinomoto.proteam.elements.AbstractOpticalElement;

public class HistoryNodeMoveElement extends HistoryNodeMoveAbstract {
	AbstractOpticalElement element;

	public HistoryNodeMoveElement(Surroundings s, AbstractOpticalElement element) {
		super(s);
		this.element = element;
	}

	@Override
	public void undo() {
		System.out.println(moveBy);
		element.moveBy((int)-moveBy.x, (int)-moveBy.y);
		s.simulate();
	}

	@Override
	public void redo() {
		element.moveBy((int)moveBy.x, (int)moveBy.y);
		s.simulate();
	}

}
