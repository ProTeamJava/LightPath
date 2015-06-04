package me.kinomoto.proteam.settings;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import me.kinomoto.proteam.Surroundings;
import me.kinomoto.proteam.elements.Mirror;
import me.kinomoto.proteam.history.History;
import me.kinomoto.proteam.history.HistoryNodeMirror;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class MirrorSettingsPanel extends JPanel {
	private static final long serialVersionUID = -724826379637207733L;

	private JSpinner spinner;
	
	/**
	 * Create the panel.
	 */
	public MirrorSettingsPanel(final Mirror m, final Surroundings s) {
		
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
		
		JLabel lblSurroundings = new JLabel("Mirror");
		add(lblSurroundings, "2, 2, 3, 1, center, default");
		
		JLabel lblIor = new JLabel("Absorption");
		add(lblIor, "2, 4");
		
		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(m.getAbsorption(), 0.0, .99, 0.01));
		add(spinner, "4, 4");
		
		spinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent arg0) {
				History.addNode(new HistoryNodeMirror(m, s));
				m.setAbsorption((double) spinner.getValue());
				s.simulate();
			}
		});
	}

}
