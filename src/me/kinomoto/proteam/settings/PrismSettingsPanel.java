package me.kinomoto.proteam.settings;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import me.kinomoto.proteam.Surroundings;
import me.kinomoto.proteam.elements.Prism;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class PrismSettingsPanel extends JPanel {
	private static final long serialVersionUID = 7397840660030370677L;

	private JSpinner spinner;
	
	/**
	 * Create the panel.
	 */
	public PrismSettingsPanel(final Prism p, final Surroundings s) {
		
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
		
		JLabel lblPrismN = new JLabel("Prism");
		add(lblPrismN, "3, 2, 4, 1, center, top");
		
		JLabel lblIor = new JLabel("IOR");
		add(lblIor, "3, 4, right, default");
		
		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(p.getIOR(), 1.0, 10.0, 0.1));
		add(spinner, "6, 4");
		
		spinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				p.setIOR((double) spinner.getValue());
				s.simulate();				
			}
		});

	}
}
