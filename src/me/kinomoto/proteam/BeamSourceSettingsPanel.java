package me.kinomoto.proteam;

import javax.swing.JPanel;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Dimension;

public class BeamSourceSettingsPanel extends JPanel {
	private static final long serialVersionUID = 5668064404100316575L;

	private JSpinner spinner;

	/**
	 * Create the panel.
	 */
	public BeamSourceSettingsPanel(final BeamSource beamSource, final Surroundings s) {
		
		setMinimumSize(new Dimension(120, 50));
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("min:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("min:grow(8)"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblBeamSourceNo = new JLabel("Beam Source");
		add(lblBeamSourceNo, "2, 2, 5, 1, center, default");
		
		JLabel lblWaveLenght = new JLabel("Wavelength");
		add(lblWaveLenght, "2, 4, right, default");
		
		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(beamSource.getWavelength(), 380.0, 780.0, 1.0));
		add(spinner, "4, 4");
		
		spinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
					beamSource.setWavelength((double) spinner.getValue());
					s.simulate();
			}
		});
		
		JLabel lblNm = new JLabel("nm");
		add(lblNm, "6, 4");

	}

}
