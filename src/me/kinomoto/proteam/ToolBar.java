package me.kinomoto.proteam;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import me.kinomoto.proteam.SurroundingsView.TOOL;

public class ToolBar extends JPanel {

	private static final long serialVersionUID = 9011244837363357691L;

	public ToolBar(final Main main) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		ButtonGroup btnGroup = new ButtonGroup();

		JToggleButton pointerSelectButton = new JToggleButton(main.getIcon("transform-move.png"));
		JToggleButton mirrorSelectButton = new JToggleButton(main.getIcon("draw-line.png"));
		JToggleButton triangularPrismButton = new JToggleButton(main.getIcon("draw-triangle1.png"));
		JToggleButton squarePrismButton = new JToggleButton(main.getIcon("draw-rectangle.png"));
		JToggleButton sourceButton = new JToggleButton(main.getIcon("source.png"));

		btnGroup.add(pointerSelectButton);
		btnGroup.add(mirrorSelectButton);
		btnGroup.add(triangularPrismButton);
		btnGroup.add(squarePrismButton);
		btnGroup.add(sourceButton);
		
		pointerSelectButton.setSelected(true);

		this.add(pointerSelectButton);
		this.add(mirrorSelectButton);
		this.add(triangularPrismButton);
		this.add(squarePrismButton);
		this.add(sourceButton);

		pointerSelectButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				main.surroundingsView.setSelectedTool(TOOL.POINTER);
				
			}
		});
		
		mirrorSelectButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				main.surroundingsView.setSelectedTool(TOOL.MIRROR);
				
			}
		});
		
		triangularPrismButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				main.surroundingsView.setSelectedTool(TOOL.TRIANGLE_PRISM);
				
			}
		});
		
		squarePrismButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				main.surroundingsView.setSelectedTool(TOOL.SQUARE_PRISM);
				
			}
		});
		
		sourceButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				main.surroundingsView.setSelectedTool(TOOL.SOURCE);
				
			}
		});
	}

}
