package me.kinomoto.proteam.settings;

import java.awt.Dimension;

import javax.swing.JPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * SettingsPanel class creates the JPanel on the right side of the interface where properties of the selected elements can be modified
 */
public class SettingsPanel extends JPanel {
	private static final long serialVersionUID = 7756992612679667166L;
	private static final int PREFFERED_WIDTH = 200;
	private JPanel panel;
	

	public SettingsPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.MIN_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		this.setPreferredSize(new Dimension(PREFFERED_WIDTH, 0));
		panel = new JPanel();
		add(panel, "2, 2, fill, fill");
	}
	
	public void setPanel(JPanel panel) {
		remove(this.panel);
		this.panel = panel; 
		add(panel, "2, 2, fill, fill");
		revalidate();
		repaint();
	}

}
