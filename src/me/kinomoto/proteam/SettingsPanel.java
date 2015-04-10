package me.kinomoto.proteam;

import javax.swing.JPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class SettingsPanel extends JPanel {
	private static final long serialVersionUID = 7756992612679667166L;
	private JPanel panel;

	public SettingsPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.MIN_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.MIN_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		panel = SurroundingsSettingsPanel.createJPanel();
		add(panel, "2, 2, fill, fill");
		
		JPanel panel_1 = PrismSettingsPanel.createJPanel();
		add(panel_1, "2, 4, fill, fill");
		
		JPanel panel_2 = new BeamSourceSettingsPanel();
		add(panel_2, "2, 6, fill, fill");
	}

}
