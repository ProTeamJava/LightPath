package me.kinomoto.proteam.history;

import me.kinomoto.proteam.Surroundings;
import me.kinomoto.proteam.elements.Point;

public abstract class HistoryNodeMoveAbstract extends HistoryNodeAbstract {

	protected Point moveBy = new Point(0, 0);

	public HistoryNodeMoveAbstract(Surroundings s) {
		super(s);
	}

	public void moveBy(Point p) {
		moveBy = moveBy.moveBy(p);
	}
	
	@Override
	public boolean isEmpty() {
		return moveBy.x == 0 && moveBy.y == 0;
	}
}
