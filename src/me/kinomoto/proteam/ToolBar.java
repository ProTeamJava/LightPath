package me.kinomoto.proteam;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ToolBar extends JPanel {

	private static final long serialVersionUID = 9011244837363357691L;

	public ToolBar() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JButton pointerSelectButton = new JButton("Pointer");
		JButton mirrorSelectButton = new JButton("Mirror");
		JButton triangularPrismButton = new JButton("Triangle");
		JButton squarePrismButton = new JButton("Square");

		this.add(pointerSelectButton);
		this.add(mirrorSelectButton);
		this.add(triangularPrismButton);
		this.add(squarePrismButton);

	}

}
