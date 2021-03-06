package me.kinomoto.proteam.settings;

import javax.swing.JPanel;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.SpinnerNumberModel;

import me.kinomoto.proteam.Surroundings;
import me.kinomoto.proteam.history.History;
import me.kinomoto.proteam.history.HistoryNodeSurroundings;

/**
 * JPanel on SettingsPanel where properties of the Surroundings can be modified
 */
public class SurroundingsSettingsPanel extends JPanel {
	private static final long serialVersionUID = -8039564944519925324L;
	private static final double MAX_IOR = 10;
	private static final double MIN_IOR = 1;
	private static final double IOR_STEP = .1;

	private JSpinner spinner;

	public SurroundingsSettingsPanel(final Surroundings s) {
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
		
		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(s.getIor(), MIN_IOR, MAX_IOR, IOR_STEP));
		add(spinner, "4, 4");
		
		spinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent arg0) {
				History.addNode(new HistoryNodeSurroundings(s));
				s.setIOR((double) spinner.getValue());				
			}
		});

	}
}
