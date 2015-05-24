package me.kinomoto.proteam;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class ToolBar extends JPanel {

	private static final long serialVersionUID = 9011244837363357691L;

	public ToolBar(Main main) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		ButtonGroup btnGroup = new ButtonGroup();

		JToggleButton pointerSelectButton = new JToggleButton(main.getIcon("transform-move.png"));
		JToggleButton mirrorSelectButton = new JToggleButton(main.getIcon("draw-line.png"));
		JToggleButton triangularPrismButton = new JToggleButton(main.getIcon("draw-triangle1.png"));
		JToggleButton squarePrismButton = new JToggleButton(main.getIcon("draw-rectangle.png"));

		btnGroup.add(pointerSelectButton);
		btnGroup.add(mirrorSelectButton);
		btnGroup.add(triangularPrismButton);
		btnGroup.add(squarePrismButton);
		
		pointerSelectButton.setSelected(true);

		this.add(pointerSelectButton);
		this.add(mirrorSelectButton);
		this.add(triangularPrismButton);
		this.add(squarePrismButton);

	}

}
