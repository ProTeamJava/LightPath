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
		JToggleButton mirrorSelectButton = new JToggleButton(main.getIcon("mirror.png"));
		JToggleButton triangularPrismButton = new JToggleButton(main.getIcon("triangle_prism.png"));
		JToggleButton squarePrismButton = new JToggleButton(main.getIcon("cube_prism.png"));
		JToggleButton sourceButton = new JToggleButton(main.getIcon("source.png"));
		JToggleButton drawMirrorButton = new JToggleButton(main.getIcon("draw_mirror.png"));
		JToggleButton drawPrismButton = new JToggleButton(main.getIcon("draw_prism.png"));

		btnGroup.add(pointerSelectButton);
		btnGroup.add(mirrorSelectButton);
		btnGroup.add(triangularPrismButton);
		btnGroup.add(squarePrismButton);
		btnGroup.add(sourceButton);
		btnGroup.add(drawPrismButton);
		btnGroup.add(drawMirrorButton);
		
		pointerSelectButton.setSelected(true);

		this.add(pointerSelectButton);
		this.add(mirrorSelectButton);
		this.add(triangularPrismButton);
		this.add(squarePrismButton);
		this.add(sourceButton);
		this.add(drawMirrorButton);
		this.add(drawPrismButton);

		pointerSelectButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				main.getSurroundingsView().setSelectedTool(TOOL.POINTER);
				
			}
		});
		
		mirrorSelectButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				main.getSurroundingsView().setSelectedTool(TOOL.MIRROR);
				
			}
		});
		
		triangularPrismButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				main.getSurroundingsView().setSelectedTool(TOOL.TRIANGLE_PRISM);
				
			}
		});
		
		squarePrismButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				main.getSurroundingsView().setSelectedTool(TOOL.SQUARE_PRISM);
				
			}
		});
		
		sourceButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				main.getSurroundingsView().setSelectedTool(TOOL.SOURCE);
				
			}
		});
		
		drawMirrorButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				main.getSurroundingsView().setSelectedTool(TOOL.DRAW_MIRROR);
			}
		});
		
		drawPrismButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				main.getSurroundingsView().setSelectedTool(TOOL.DRAW_PRISM);
			}
		});
	}

}
