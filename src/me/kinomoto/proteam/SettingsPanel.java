package me.kinomoto.proteam;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SettingsPanel extends JPanel {
	private static final long serialVersionUID = 7756992612679667166L;

	JPanel surroundingsSetPan;
	JPanel mirrorSetPan;
	JPanel prismSetPan;
	JPanel sourceSetPan;

	public SettingsPanel() {

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		surroundingsSetPan = new JPanel();
		mirrorSetPan = new JPanel();
		prismSetPan = new JPanel();
		sourceSetPan = new JPanel();

		sourceSetPan.setLayout(new FlowLayout());
		surroundingsSetPan.setLayout(new FlowLayout());
		mirrorSetPan.setLayout(new FlowLayout());
		prismSetPan.setLayout(new FlowLayout());

		sourceSetPan.add(new JLabel("Wavelenght"));
		sourceSetPan.add(new JTextField(10));
		prismSetPan.add(new JLabel("Coefficient"));
		prismSetPan.add(new JTextField(10));

		this.add(surroundingsSetPan);
		this.add(mirrorSetPan);
		this.add(prismSetPan);
		this.add(sourceSetPan);
	}

}
