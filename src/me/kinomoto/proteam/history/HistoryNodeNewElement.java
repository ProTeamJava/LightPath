package me.kinomoto.proteam.history;

import me.kinomoto.proteam.Surroundings;
import me.kinomoto.proteam.elements.AbstractOpticalElement;

/**
 *The class is creating a HistoryNode after creating new optical element on the working plain
 */
public class HistoryNodeNewElement extends HistoryNodeAbstract {
	AbstractOpticalElement element;

	public HistoryNodeNewElement(Surroundings s, AbstractOpticalElement element) {
		super(s);
		this.element = element;
	}

	@Override
	public void undo() {
		s.deleteElement(element);
	}

	@Override
	public void redo() {
		s.add(element);
	}

}
