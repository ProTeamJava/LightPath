package me.kinomoto.proteam;

import javax.swing.JPanel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JLabel;
import javax.swing.JSpinner;

public class SurroundingsSettingsPanel extends JPanel {
	private static final long serialVersionUID = -8039564944519925324L;

	/**
	 * Create the panel.
	 */
	public SurroundingsSettingsPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("min:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblSurroundings = new JLabel("Surroundings");
		add(lblSurroundings, "2, 2, 3, 1, center, default");
		
		JLabel lblIor = new JLabel("IOR");
		add(lblIor, "2, 4");
		
		JSpinner spinner = new JSpinner();
		add(spinner, "4, 4");

	}

	/**
	 * @wbp.factory
	 */
	public static JPanel createJPanel() {
		JPanel panel = new SurroundingsSettingsPanel();
		return panel;
	}
}
