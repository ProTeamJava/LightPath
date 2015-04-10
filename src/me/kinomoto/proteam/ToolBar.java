package me.kinomoto.proteam;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ToolBar extends JPanel {

	private static final long serialVersionUID = 9011244837363357691L;

	public ToolBar(Main main) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JButton pointerSelectButton = new JButton(main.getIcon("transform-move.png"));
		JButton mirrorSelectButton = new JButton(main.getIcon("draw-line.png"));
		JButton triangularPrismButton = new JButton(main.getIcon("draw-triangle1.png"));
		JButton squarePrismButton = new JButton(main.getIcon("draw-rectangle.png"));

		this.add(pointerSelectButton);
		this.add(mirrorSelectButton);
		this.add(triangularPrismButton);
		this.add(squarePrismButton);

	}

}
