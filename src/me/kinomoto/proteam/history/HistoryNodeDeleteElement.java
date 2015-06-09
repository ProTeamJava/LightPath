package me.kinomoto.proteam.history;

import me.kinomoto.proteam.Surroundings;
import me.kinomoto.proteam.elements.AbstractOpticalElement;

/**
 *The class is creating a HistoryNode after deleting element from the working plain
 */
public class HistoryNodeDeleteElement  extends HistoryNodeAbstract {
	AbstractOpticalElement element;

	public HistoryNodeDeleteElement(Surroundings s, AbstractOpticalElement element) {
		super(s);
		this.element = element;
	}

	@Override
	public void undo() {
		s.add(element);
	}

	@Override
	public void redo() {
		s.deleteElement(element);
	}

}