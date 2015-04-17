package me.kinomoto.proteam;

import javax.swing.JPanel;
import javax.swing.JLabel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JSpinner;

public class PrismSettingsPanel extends JPanel {
	private static final long serialVersionUID = 7397840660030370677L;

	/**
	 * Create the panel.
	 */
	public PrismSettingsPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.MIN_COLSPEC,
				ColumnSpec.decode("min(0dlu;min)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("min:grow(8)"),},
			new RowSpec[] {
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("15px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblPrismN = new JLabel("Prism n");
		add(lblPrismN, "3, 2, 4, 1, center, top");
		
		JLabel lblIor = new JLabel("IOR");
		add(lblIor, "3, 4, right, default");
		
		JSpinner spinner = new JSpinner();
		add(spinner, "6, 4");

	}

	/**
	 * @wbp.factory
	 */
	public static JPanel createJPanel() {
		JPanel panel = new PrismSettingsPanel();
		return panel;
	}
}
